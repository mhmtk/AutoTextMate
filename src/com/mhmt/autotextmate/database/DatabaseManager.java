package com.mhmt.autotextmate.database;

import java.util.ArrayList;

import com.mhmt.autotextmate.database.RuleDatabaseContract.RuleEntry;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


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
}
