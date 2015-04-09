package com.mhmt.autotextmate.database;

import java.util.ArrayList;

import com.mhmt.autotextmate.dataobjects.Rule;

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
	}
}
