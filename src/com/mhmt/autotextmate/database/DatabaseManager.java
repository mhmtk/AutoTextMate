package com.mhmt.autotextmate.database;

import java.util.ArrayList;

import com.mhmt.autotextmate.dataobjects.Rule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseManager {

	private RuleDatabaseSQLHelper dbHelper;
	private SQLiteDatabase db;
	private ArrayList<Rule> ruleArray;

	public DatabaseManager(Context context){
		this.dbHelper = new RuleDatabaseSQLHelper(context, "", null, 0);
		this.db = dbHelper.getReadableDatabase();
		this.ruleArray = new ArrayList<Rule>();
	}
	
	public void addRule(Rule rule){
		
	}
}
