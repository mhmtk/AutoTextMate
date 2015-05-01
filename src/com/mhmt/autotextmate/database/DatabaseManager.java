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
 * @version November May 1, 2015
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

	/**
	 * For the given widget ID, resets the widgetID cell in the DB to its default (invalid).
	 * 
	 * @param widgetIDs IDs of the widgets whose references should be removed from the DB
	 */
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
	 * @return MAY return null if no widgetID matches
	 */
	public Rule getRule(int widgetID) {

		db = dbHelper.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + RuleEntry.RULE_TABLE_NAME + " WHERE "
				+ RuleEntry.RULE_COLUMN_WIDGET_ID + " ='" + widgetID + "'";

		//Log the query
		Log.i("DatabaseManager", selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		Rule rule = null;
		
		if (c.moveToFirst()) {
			rule = new Rule(c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_NAME)),
					c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_DESCRIPTION)),
					c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_TEXT)),
					c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_ONLYCONTACTS)),
					c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_STATUS)));
		}
		else 
			Log.i("DatabaseManager", "The cursor returned by getRule was null for given widgetID. This is normal during widget creation");
		
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
		if (c != null)
			c.moveToFirst();
		else 
			Log.e("DatabaseManager", "The cursor returned by getRulesArray was null");

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
		if (c != null)
			c.moveToFirst();
		else 
			Log.e("DatabaseManager", "The cursor returned by getApplicableRules was null");


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
	 * Called to toggle the status of the rule with the given name
	 * 
	 * @param name The name of the rule of which the status will be toggled
	 */
	public void toggleRuleStatus(String name) {
		db = dbHelper.getWritableDatabase();

		String selectQuery = "SELECT " + RuleEntry.RULE_COLUMN_STATUS + 
				" FROM " + RuleEntry.RULE_TABLE_NAME + 
				" WHERE " + RuleEntry.RULE_COLUMN_NAME + " ='" + name + "'";

		//Log the query
		Log.i("DatabaseManager", selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();
		else 
			Log.e("DatabaseManager", "The cursor returned by toggleRule(Str s) was null for given ruleName");

		int curStatus = c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_STATUS));
		int statusToSet = (curStatus == 1) ? 0 : 1;

		String updateQuery = "UPDATE " + RuleEntry.RULE_TABLE_NAME +
				" SET " + RuleEntry.RULE_COLUMN_STATUS + "='" + statusToSet + "'" +
				" WHERE " + RuleEntry.RULE_COLUMN_NAME + "='" + name + "'";

		db.execSQL(updateQuery);

		Log.i("DatabaseManager", "Executed: " + updateQuery);

		db.close();
	}

	/**
	 * Called to change the status of the rule with the given name to the given state
	 * 
	 * @param name The name of the rule of which the status will be toggled
	 * @param status The state the rule's status should be set to
	 * 
	 * @return Returns the widget ID of the 
	 */
	public int setRuleStatus(String name, boolean state) {
		int status = state ? 1 : 0;

		db = dbHelper.getWritableDatabase();

		//Get the widget ID of the rule whose state is about to change
		String selectQuery = "SELECT " + RuleEntry.RULE_COLUMN_WIDGET_ID + 
				" FROM " + RuleEntry.RULE_TABLE_NAME +
				" WHERE " + RuleEntry.RULE_COLUMN_NAME + " ='" + name + "'";
		Cursor c = db.rawQuery(selectQuery, null); //Cursor with the select query
		Log.i("DatabaseManager", selectQuery); //Log
		if (c != null) //make sure cursor isnt empty
			c.moveToFirst();
		else //Cursor is empty = Error
			Log.e("DatabaseManager", "The cursor returned by setRuleStatus(Str n, bool s) was null for given ruleName");
		int wID = c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_WIDGET_ID)); //save the widget ID

		// Update the database to set the state of the rule to the parameter
		String updateQuery = "UPDATE " + RuleEntry.RULE_TABLE_NAME +
				" SET " + RuleEntry.RULE_COLUMN_STATUS + "='" + status + "'" +
				" WHERE " + RuleEntry.RULE_COLUMN_NAME + "='" + name +"'";
		db.execSQL(updateQuery);
		Log.i("DatabaseManager", "Executed: " + updateQuery);

		db.close();

		return wID; //return the widget ID
	}
}