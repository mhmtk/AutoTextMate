package com.mhmt.autotextmate.database;

import java.util.ArrayList;

import com.mhmt.autotextmate.database.RuleDatabaseContract.RuleEntry;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * 
 * @author Mehmet Kologlu
 * @version November April 20, 2015
 * 
 */
public class DatabaseManager {

	private RuleDatabaseSQLHelper dbHelper;
	private SQLiteDatabase db;
	private ArrayList<Rule> ruleArray;

	/**
	 * Constructor
	 * @param context
	 */
	public DatabaseManager(Context context){
		this.dbHelper = new RuleDatabaseSQLHelper(context, "", null, 0);

	}

	/**
	 * Sets the widget ID of the rule in the DB with the given name to widgetID.
	 * Should also be called when a widget is deleted, and the INVALID_WIDGET_ID
	 * should be passed as the widgetID.
	 * 
	 * @param ruleName The name of the rule whose widgetID will be changed
	 * @param widgetID The widget ID to set
	 */
	public void setWidgetID(String ruleName, int widgetID) {
		Log.i("DatabaseManager", "Add widget ID was called");

		//get writable database
		db = dbHelper.getWritableDatabase();

		String query = "UPDATE " + RuleEntry.RULE_TABLE_NAME +
				" SET " + RuleEntry.RULE_COLUMN_WIDGET_ID + "='" + widgetID +"'" +
				" WHERE " + RuleEntry.RULE_COLUMN_NAME + "='" + ruleName + "'" ;

		try {
			db.execSQL(query);
		} catch (SQLException e) {
			Log.e("DatabaseManager", "SQLException " + e + "cought");
		}

		Log.i("DatabaseManager", query);

		db.close();
	}

	public void resetWidgetIDs(int[] widgetIDs) {
		Log.i("DatabaseManager", "Reset widget ID was called");

		//get writable database
		db = dbHelper.getWritableDatabase();
		String query;

		for (int i=0; i<widgetIDs.length;i++) {
			query = "UPDATE " + RuleEntry.RULE_TABLE_NAME +
					" SET " + RuleEntry.RULE_COLUMN_WIDGET_ID + "='" + AppWidgetManager.INVALID_APPWIDGET_ID +"'" +
					" WHERE " + RuleEntry.RULE_COLUMN_WIDGET_ID + "='" + widgetIDs[i] + "'" ;
			try {
				db.execSQL(query);
			} catch (SQLException e) {
				Log.e("DatabaseManager", "SQLException " + e + "cought");
			}

			Log.i("DatabaseManager", query);
		}

		db.close();
	}
	/**
	 * Returns a rule object from the database that corresponds to the widgetID 
	 * 
	 * @param widgetID The widgetID associated with the rule to return
	 * @return
	 */
	public Rule getRule(int widgetID) {

		db = dbHelper.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + RuleEntry.RULE_TABLE_NAME + " WHERE "
				+ RuleEntry.RULE_COLUMN_WIDGET_ID + " ='" + widgetID + "'";

		//Log the query
		Log.i("DatabaseManager", selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();
		else 
			Log.e("DatabaseManager", "The cursor returned by getRule was null for given widgetID");

		Rule rule = new Rule(c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_NAME)),
				c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_DESCRIPTION)),
				c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_TEXT)),
				c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_ONLYCONTACTS)),
				c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_STATUS)));

		db.close();

		return rule;
	}
	/**
	 * Adds the given rule to the database
	 * 
	 * @param rule Rule to be added
	 */
	public void addRule(Rule rule){
		Log.i("DatabaseManager", "Add rule was called");

		//get writable database
		db = dbHelper.getWritableDatabase();

		// map of values
		ContentValues values = new ContentValues();
		values.put(RuleEntry.RULE_COLUMN_NAME, rule.getName());
		values.put(RuleEntry.RULE_COLUMN_DESCRIPTION, rule.getDescription());
		values.put(RuleEntry.RULE_COLUMN_TEXT, rule.getText());
		values.put(RuleEntry.RULE_COLUMN_ONLYCONTACTS, rule.getOnlyContacts());

		// Insert the new row
		db.insertOrThrow(RuleEntry.RULE_TABLE_NAME, null, values);
		RuleDatabaseSQLHelper.INITIALIZED = true;

		db.close(); //close database 
	}

	/**
	 * Returns all entries in the rules database table as an arraylist
	 * 
	 * @return
	 */
	public ArrayList<Rule> getRulesArray() {
		ruleArray = new ArrayList<Rule>();

		//get readable database
		db = dbHelper.getReadableDatabase();
		//define a projection that specifies which columns from the database to use
		String[] projection = {
				BaseColumns._ID,
				RuleEntry.RULE_COLUMN_NAME,
				RuleEntry.RULE_COLUMN_DESCRIPTION,
				RuleEntry.RULE_COLUMN_TEXT,
				RuleEntry.RULE_COLUMN_ONLYCONTACTS,
				RuleEntry.RULE_COLUMN_STATUS
		};

		//sort descending
		String sortOrder = BaseColumns._ID + " DESC";

		//create cursor with the whole database
		Cursor c = db.query(
				RuleEntry.RULE_TABLE_NAME,  // The table to query
				projection,				// The columns to return
				null,		            // The columns for the WHERE clause
				null,                   // The values for the WHERE clause
				null,                   // don't group the rows
				null,					// don't filter by row groups
				sortOrder	            // sort
				);

		//move cursor to the beginning
		c.moveToFirst();

		while(!c.isAfterLast())
		{
			Rule p = new Rule(c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_NAME)),
					c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_DESCRIPTION)),
					c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_TEXT)),
					c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_ONLYCONTACTS)),
					c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_STATUS)))
			;
			ruleArray.add(p);
			c.moveToNext();
		}

		db.close();
		return ruleArray;
	}

	/**
	 * Returns the rules that are on
	 * 
	 * @return an arraylist<rule> of rules that are turned on (status == 1)
	 */
	public ArrayList<Rule> getApplicableRules(){
		ruleArray = new ArrayList<Rule>();

		//get readable database
		db = dbHelper.getReadableDatabase();

		//define a projection that specifies which columns from the database to use
		String[] projection = {
				RuleEntry.RULE_COLUMN_TEXT,
				RuleEntry.RULE_COLUMN_ONLYCONTACTS,
		};

		//sort descending
		//				String sortOrder = BaseColumns._ID + " DESC";

		//create cursor with only entries with status = 1 (on) 
		Cursor c = db.query(
				RuleEntry.RULE_TABLE_NAME,  		// The table to query
				projection,							// The columns to return
				RuleEntry.RULE_COLUMN_STATUS + "=?",// The columns for the WHERE clause
				new String[] { "1" },				// The values for the WHERE clause
				null,			                    // don't group the rows
				null,								// don't filter by row groups
				null	            				// sort
				);

		//move cursor to the beginning
		c.moveToFirst();

		while(!c.isAfterLast())
		{ //add the rules to the arraylist
			ruleArray.add(new Rule("",	//no name
					"",					//no description
					c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_TEXT)),
					c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_ONLYCONTACTS)),
					1					//they should all be 1 (on) anyway
					));
			c.moveToNext();
		}

		db.close();
		return ruleArray;
	}

	/**
	 * Called to change the status of the rule with the given name to the given state
	 * 
	 * @param name The name of the rule of which the status will be toggled
	 * @param status The state the rule's status should be set to
	 */
	public void toggleRule(String name, boolean state) {
		int status = state ? 1 : 0;

		//get readable database
		db = dbHelper.getWritableDatabase();

		db.execSQL("UPDATE " + RuleEntry.RULE_TABLE_NAME +
				" SET " + RuleEntry.RULE_COLUMN_STATUS + "='" + status + "'" +
				" WHERE " + RuleEntry.RULE_COLUMN_NAME + "='" + name +"'");

		Log.i("DatabaseManager", "executed: "+ "UPDATE " + RuleEntry.RULE_TABLE_NAME +
				" SET " + RuleEntry.RULE_COLUMN_STATUS + "='" + status + "'" +
				" WHERE " + RuleEntry.RULE_COLUMN_NAME + "='" + name +"'");

		db.close();
	}
}