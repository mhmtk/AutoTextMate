package com.mhmt.autotextmate.database;

import java.util.ArrayList;

import com.mhmt.autotextmate.database.RuleDatabaseContract.RuleEntry;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.BaseBundle;
import android.util.Log;
import android.widget.ArrayAdapter;


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
		this.db = dbHelper.getReadableDatabase();
		this.ruleArray = new ArrayList<Rule>();
	}
	
	/**
	 * Adds the given rule to the database
	 * 
	 * @param rule Rule to be added
	 */
	public void addRule(Rule rule){
		
		Log.d("Database Manager", "Add rule was called");
		
		// map of values
		ContentValues values = new ContentValues();
		values.put(RuleEntry.RULE_COLUMN_NAME, rule.getName());
		values.put(RuleEntry.RULE_COLUMN_DESCRIPTION, rule.getDescription());
		values.put(RuleEntry.RULE_COLUMN_TEXT, rule.getText());
		values.put(RuleEntry.RULE_COLUMN_ONLYCONTACTS, rule.getOnlyContacts());

		// Insert the new row
		db.insert(RuleEntry.RULE_TABLE_NAME, null, values);
		RuleDatabaseSQLHelper.INITIALIZED = true;
	}

	public ArrayList<Rule> getRulesArray() {
		
		db = dbHelper.getReadableDatabase();
		ruleArray = new ArrayList<Rule>();

		//define a projection that specifies which columns from the database to use
		String[] projection = {
				RuleEntry.RULE_COLUMN_NAME,
				RuleEntry.RULE_COLUMN_DESCRIPTION,
				RuleEntry.RULE_COLUMN_TEXT,
				RuleEntry.RULE_COLUMN_ONLYCONTACTS,
				RuleEntry.RULE_COLUMN_STATUS,
				
		};

		//sort descending
//		String sortOrder = Rule.COLUMN_NAME_TABLE_ID + " DESC";

		//create cursor with the whole database
		Cursor c = db.query(
				RuleEntry.RULE_TABLE_NAME,  // The table to query
				projection,				// The columns to return
				null,		            // The columns for the WHERE clause
				null,                   // The values for the WHERE clause
				null,                   // don't group the rows
				null,					// don't filter by row groups
				null	               	// don't sort
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
}
