package com.mhmt.autotextmate.database;

import java.util.ArrayList;
import java.util.Arrays;

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
 * @version November May 12, 2015
 * 
 */
public class DatabaseManager {

	private RuleDatabaseSQLHelper dbHelper;
	private SQLiteDatabase db;
	private ArrayList<Rule> ruleArray;
	private String logTag = "DatabaseManager";

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
		Log.i(logTag, "Add widget ID was called");

		//while (db != null && db.isOpen()) {Log.i(logTag, "waiting for DB");} // Wait until DB is closed to act on it
		//get writable database
		db = dbHelper.getWritableDatabase();

		String query = "UPDATE " + RuleEntry.RULE_TABLE_NAME +
				" SET " + RuleEntry.RULE_COLUMN_WIDGET_ID + "='" + widgetID +"'" +
				" WHERE " + RuleEntry.RULE_COLUMN_NAME + "='" + ruleName + "'" ;

		try {
			db.execSQL(query);
		} catch (SQLException e) {
			Log.e(logTag, "SQLException " + e + "cought");
		}

		Log.i(logTag, query);

		db.close();
	}

	/**
	 * For the given widget ID, resets the widgetID cell in the DB to its default (invalid).
	 * 
	 * @param widgetIDs IDs of the widgets whose references should be removed from the DB
	 */
	public void resetWidgetIDs(int[] widgetIDs) {
		Log.i(logTag, "Reset widget ID was called");

		//while (db != null && db.isOpen()) {Log.i(logTag, "waiting for DB");} // Wait until DB is closed to act on it
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
				Log.e(logTag, "SQLException " + e + "cought");
			}

			Log.i(logTag, query);
		}

		db.close();
	}

	/**
	 * Returns a rule object from the database that corresponds to the given rule name
	 * 
	 * @param ruleName The name of the rule requested
	 * @return The Rule object of the given rule
	 */
	public Rule getRule(String ruleName) {
		//while (db != null && db.isOpen()) {Log.i(logTag, "waiting for DB");} // Wait until DB is closed to act on it
		db = dbHelper.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + RuleEntry.RULE_TABLE_NAME + " WHERE "
				+ RuleEntry.RULE_COLUMN_NAME+ " =?";

		//Log the query
		Log.i(logTag, selectQuery + " ** " + ruleName);

		Cursor c = db.rawQuery(selectQuery, new String[] {ruleName});

		Rule rule = null;

		if (c.moveToFirst()) {
			rule = new Rule(c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_NAME)),
					c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_DESCRIPTION)),
					c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_TEXT)),
					c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_ONLYCONTACTS)),
					c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_REPLY_TO)),
					c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_STATUS)));
		}
		else 
			Log.e(logTag, "The cursor returned by getRule was null for given rule name. This should NOT happen");

		db.close();

		return rule;
	}

	/**
	 * Returns a rule object from the database that corresponds to the given widgetID 
	 * 
	 * @param widgetID The widgetID associated with the rule to return
	 * @return MAY return null if no widgetID matches
	 */
	public Rule getRule(int widgetID) {

		//while (db != null && db.isOpen()) {Log.i(logTag, "waiting for DB");} // Wait until DB is closed to act on it
		db = dbHelper.getReadableDatabase();

		String selectQuery = "SELECT " + RuleEntry.RULE_COLUMN_NAME 
				+ " , " + RuleEntry.RULE_COLUMN_STATUS + 
				" FROM " + RuleEntry.RULE_TABLE_NAME + " WHERE "
				+ RuleEntry.RULE_COLUMN_WIDGET_ID + " =?";

		//Log the query
		Log.i(logTag, selectQuery + " ** " + widgetID);

		Cursor c = db.rawQuery(selectQuery, new String[] {String.valueOf(widgetID)});

		Rule rule = null; //dummy instantiation

		if (c.moveToFirst()) {
			rule = new Rule(c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_NAME)),
					c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_STATUS)));
		}
		else 
			Log.i(logTag, "The cursor returned by getRule was null for given widgetID. This is normal during widget creation");

		db.close();

		return rule;
	}

	/**
	 * Updates the columns of the rule with the given oldRuleName to the fields of newRule.
	 * If requested, returns the wID of the rule. 
	 * 
	 * @param widgetIdRequestFlag True if the widgetID of the rule is requested, false otherwise.
	 * @param oldRuleName The name of the rule to be changed
	 * @param newRule A rule object that will take the old rules place in the DB
	 * @return Returns the wID of the old rule if it has been requested (flag == true), -1 otherwise.
	 */
	public int editRule(boolean widgetIdRequestFlag, String oldRuleName, Rule newRule) {
		Log.i(logTag, "editRule was called");

		//while (db != null && db.isOpen()) {Log.i(logTag, "waiting for DB");} // Wait until DB is closed to act on it
		//get writable database
		db = dbHelper.getWritableDatabase();

		int wID = -1; // create an arbitrary wID

		// if the wID is requested, get it from the DB
		if (widgetIdRequestFlag) {
			// Get the widget ID
			String selectQuery = "SELECT " + RuleEntry.RULE_COLUMN_WIDGET_ID +
					" FROM " + RuleEntry.RULE_TABLE_NAME + 
					" WHERE " + RuleEntry.RULE_COLUMN_NAME + " =?";
			Cursor c = db.rawQuery(selectQuery, new String[] {oldRuleName});
			Log.i(logTag, selectQuery + " ** " + oldRuleName);

			if (c.moveToFirst()) {
				wID = c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_WIDGET_ID));
			}
			else 
				Log.i(logTag, "The cursor returned by getRule was null for given widgetID. This is normal during widget creation");			
		}


		// Edit the rule in the DB
		String updateQuery = "UPDATE " + RuleEntry.RULE_TABLE_NAME +
				" SET " + RuleEntry.RULE_COLUMN_NAME + "=?" + 
				" , " + RuleEntry.RULE_COLUMN_DESCRIPTION + "=?" +
				" , " + RuleEntry.RULE_COLUMN_TEXT + "=?" +
				" , " + RuleEntry.RULE_COLUMN_ONLYCONTACTS + "=?" +
				" , " + RuleEntry.RULE_COLUMN_REPLY_TO + "=?" +
				" WHERE " + RuleEntry.RULE_COLUMN_NAME + "=?" ;		
		String[] updateQueryArgs = new String[] {
				newRule.getName(),
				newRule.getDescription(),
				newRule.getText(),
				String.valueOf(newRule.getOnlyContacts()),
				String.valueOf(newRule.getReplyTo()),
				oldRuleName
		};
		Log.i(logTag, updateQuery + " ** " + Arrays.toString(updateQueryArgs));
		db.execSQL(updateQuery, updateQueryArgs);

		db.close(); //close database 

		return wID; //return the widgetID

	}

	/**
	 * Adds the given rule to the database
	 * 
	 * @param rule Rule to be added
	 */
	public void addRule(Rule rule){
		Log.i(logTag, "Add rule was called");

		//while (db != null && db.isOpen()) {Log.i(logTag, "waiting for DB");} // Wait until DB is closed to act on it
		//get writable database
		db = dbHelper.getWritableDatabase();

		// map of values
		ContentValues values = new ContentValues();
		values.put(RuleEntry.RULE_COLUMN_NAME, rule.getName());
		values.put(RuleEntry.RULE_COLUMN_DESCRIPTION, rule.getDescription());
		values.put(RuleEntry.RULE_COLUMN_TEXT, rule.getText());
		values.put(RuleEntry.RULE_COLUMN_ONLYCONTACTS, rule.getOnlyContacts());

		// TODO FEEDBACK
		//Insert the new row
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

		//while (db != null && db.isOpen()) {Log.i(logTag, "waiting for DB");} // Wait until DB is closed to act on it 
		//get readable database
		db = dbHelper.getReadableDatabase();
		//define a projection that specifies which columns from the database to use
		String[] projection = {
				BaseColumns._ID,
				RuleEntry.RULE_COLUMN_NAME,
				RuleEntry.RULE_COLUMN_DESCRIPTION,
				RuleEntry.RULE_COLUMN_TEXT,
				RuleEntry.RULE_COLUMN_ONLYCONTACTS,
				RuleEntry.RULE_COLUMN_REPLY_TO,
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
			Log.e(logTag, "The cursor returned by getRulesArray was null");

		while(!c.isAfterLast())
		{
			Rule p = new Rule(c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_NAME)),
					c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_DESCRIPTION)),
					c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_TEXT)),
					c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_ONLYCONTACTS)),
					c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_REPLY_TO)),
					c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_STATUS)));
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
	public ArrayList<Rule> getEnabledSMSRules(){		
		ruleArray = new ArrayList<Rule>();

		//while (db != null && db.isOpen()) {Log.i(logTag, "waiting for DB");} // Wait until DB is closed to act on it
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
			Log.e(logTag, "The cursor returned by getApplicableRules was null");


		while(!c.isAfterLast())
		{ //add the rules to the ArrayList
			ruleArray.add(new Rule("",	//no name
					"",					//no description
					c.getString(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_TEXT)), //text
					c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_ONLYCONTACTS)), //onlyContacts
					c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_REPLY_TO)), //reply to
					1					//status - all should be 1 (on) anyway
					)); // TODO new constructor with only used fields
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

		//while (db != null && db.isOpen()) {Log.i(logTag, "waiting for DB");} // Wait until DB is closed to act on it
		db = dbHelper.getWritableDatabase();

		String selectQuery = "SELECT " + RuleEntry.RULE_COLUMN_STATUS + 
				" FROM " + RuleEntry.RULE_TABLE_NAME + 
				" WHERE " + RuleEntry.RULE_COLUMN_NAME + " ='" + name + "'";

		//Log the query
		Log.i(logTag, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();
		else 
			Log.e(logTag, "The cursor returned by toggleRule(Str s) was null for given ruleName");

		int curStatus = c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_STATUS));
		int statusToSet = (curStatus == 1) ? 0 : 1;

		String updateQuery = "UPDATE " + RuleEntry.RULE_TABLE_NAME +
				" SET " + RuleEntry.RULE_COLUMN_STATUS + "='" + statusToSet + "'" +
				" WHERE " + RuleEntry.RULE_COLUMN_NAME + "='" + name + "'";

		db.execSQL(updateQuery);

		Log.i(logTag, "Executed: " + updateQuery);

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

		//while (db != null && db.isOpen()) {Log.i(logTag, "waiting for DB");} // Wait until DB is closed to act on it
		db = dbHelper.getWritableDatabase();

		//Get the widget ID of the rule whose state is about to change
		String selectQuery = "SELECT " + RuleEntry.RULE_COLUMN_WIDGET_ID + 
				" FROM " + RuleEntry.RULE_TABLE_NAME +
				" WHERE " + RuleEntry.RULE_COLUMN_NAME + " ='" + name + "'";
		Cursor c = db.rawQuery(selectQuery, null); //Cursor with the select query
		Log.i(logTag, selectQuery); //Log
		if (c != null) //make sure cursor isnt empty
			c.moveToFirst();
		else //Cursor is empty = Error
			Log.e(logTag, "The cursor returned by setRuleStatus(Str n, bool s) was null for given ruleName");
		int wID = c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_WIDGET_ID)); //save the widget ID

		// Update the database to set the state of the rule to the parameter
		String updateQuery = "UPDATE " + RuleEntry.RULE_TABLE_NAME +
				" SET " + RuleEntry.RULE_COLUMN_STATUS + "='" + status + "'" +
				" WHERE " + RuleEntry.RULE_COLUMN_NAME + "='" + name +"'";
		db.execSQL(updateQuery);
		Log.i(logTag, "Executed: " + updateQuery);

		db.close();

		return wID; //return the widget ID
	}

	/**
	 * Deletes the given rule from the database
	 * 
	 * @param ruleName The name of the rule to delete
	 * @return The widget ID of the deleted rule
	 */
	public int deleteRule(String ruleName) {

		//while (db != null && db.isOpen()) {Log.i(logTag, "waiting for DB");} // Wait until DB is closed to act on it

		db = dbHelper.getWritableDatabase();

		String selectQuery = "SELECT " + RuleEntry.RULE_COLUMN_WIDGET_ID + 
				" FROM " + RuleEntry.RULE_TABLE_NAME +
				" WHERE " + RuleEntry.RULE_COLUMN_NAME + " =?";
		Cursor c = db.rawQuery(selectQuery, new String[] {ruleName}); //Cursor with the select query
		Log.i(logTag, selectQuery + " ** " + ruleName);
		if (c != null) //make sure cursor isnt empty
			c.moveToFirst();

		// Delete and close DB
		int result = db.delete(RuleEntry.RULE_TABLE_NAME, RuleEntry.RULE_COLUMN_NAME + "=?", new String[] {ruleName});			
		Log.i(logTag, "Deleted " + result + " entries.");
		db.close();

		//return the result
		return c.getInt(c.getColumnIndexOrThrow(RuleEntry.RULE_COLUMN_WIDGET_ID));
	}
}