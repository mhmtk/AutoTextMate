package com.mhmt.autotextmate.database;

import com.mhmt.autotextmate.database.RuleDatabaseContract.RuleEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * 
 * @author Mehmet Kologlu
 * @version November April 9, 2015
 * 
 */
public class RuleDatabaseSQLHelper extends SQLiteOpenHelper{

	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";
	public static boolean INITIALIZED = false;

	//SQL command to create RULE table
	private static final String SQL_CREATE_RULE=
			"CREATE TABLE " + RuleEntry.RULE_TABLE_NAME + " (" +
					RuleEntry._ID + " INTEGER PRIMARY KEY," +
					RuleEntry.RULE_TABLE_NAME + TEXT_TYPE + COMMA_SEP +
					RuleEntry.RULE_COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
					RuleEntry.RULE_COLUMN_TEXT + TEXT_TYPE + COMMA_SEP +
					RuleEntry.RULE_COLUMN_ONLYCONTACTS +INTEGER_TYPE+")";
	
	//SQL command to drop (delete) the rule table
	private static final String SQL_DELETE_RULE = 
			"DROP TABLE IF EXISTS " + RuleEntry.RULE_TABLE_NAME;

	private static final String DATABASE_NAME = "ATMRuleDatabase.db";
	private static final int DATABASE_VERSION = 1;
	
	/**
	 * @param context
	 * @param name - The name of the database
	 * @param factory
	 * @param version
	 */
	public RuleDatabaseSQLHelper(Context context, String name,CursorFactory factory, int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.d("Database operations",  "Table created");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_RULE);
		Log.d("Database operations",  "Table created (onCraete called)");
	}


	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_RULE);
		Log.d("Database operations",  "Database upgraded from " + oldVersion + " to " + newVersion);
	}
	
//	public void delete(SQLiteDatabase db, String name, String type, String date, String amount) {
//		db.execSQL("DELETE FROM " + RuleEntry.EXPENSE_TABLE_NAME + " WHERE " 
//				+ RuleEntry.EXPENSE_COLUMN_NAME + "='" + name + "' AND "
//				+ RuleEntry.EXPENSE_COLUMN_TYPE + "='" + type + "' AND "
//				+ RuleEntry.EXPENSE_COLUMN_DATE + "='" + date + "' AND "
//				+ RuleEntry.EXPENSE_COLUMN_AMOUNT + "='" + amount + "'"
//				);
//	}
}