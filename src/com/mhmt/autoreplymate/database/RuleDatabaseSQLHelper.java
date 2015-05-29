package com.mhmt.autoreplymate.database;

import com.mhmt.autoreplymate.database.RuleDatabaseContract.RuleEntry;
import com.mhmt.autoreplymate.database.RuleDatabaseContract.SMSEntry;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * 
 * @author Mehmet Kologlu
 * @version November May 29, 2015
 * 
 */
public class RuleDatabaseSQLHelper extends SQLiteOpenHelper{

	private String logTag = "DatabaseHelper";

	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";
	private static final String NOT_NULL = " NOT_NULL";
	private static final String UNIQUE = " UNIQUE";
	private static final String DEFAULT = " DEFAULT";
//	private static final String REPLY_TO_CHECK = " CHECK(" + RuleEntry.RULE_COLUMN_REPLYTO + " < 3)";
	private static final String STATUS_DEFAULT_VALUE = " 1";

//	public static boolean INITIALIZED = false;


	//SQL command to create RULE table
	private static final String SQL_CREATE_RULE=
			"CREATE TABLE IF NOT EXISTS " + RuleEntry.RULE_TABLE_NAME + " (" +
					RuleEntry._ID + " INTEGER PRIMARY KEY," +
					RuleEntry.RULE_COLUMN_NAME + TEXT_TYPE + NOT_NULL + UNIQUE + COMMA_SEP +
					RuleEntry.RULE_COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
					RuleEntry.RULE_COLUMN_TEXT + TEXT_TYPE + NOT_NULL + COMMA_SEP +
					RuleEntry.RULE_COLUMN_ONLYCONTACTS + INTEGER_TYPE + COMMA_SEP +
					RuleEntry.RULE_COLUMN_REPLYTO + INTEGER_TYPE + COMMA_SEP +
//					RuleEntry.RULE_COLUMN_REPLYTO + INTEGER_TYPE + REPLY_TO_CHECK + COMMA_SEP +
					RuleEntry.RULE_COLUMN_STATUS + INTEGER_TYPE + DEFAULT + STATUS_DEFAULT_VALUE + COMMA_SEP +
					RuleEntry.RULE_COLUMN_WIDGET_ID + INTEGER_TYPE + DEFAULT + " " + AppWidgetManager.INVALID_APPWIDGET_ID + ")";

	private static final String SQL_CREATE_SMS = 
			"CREATE TABLE IF NOT EXISTS " + SMSEntry.SMS_TABLE_NAME + " (" +
					SMSEntry.SMS_COLUMN_TIME + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
					SMSEntry.SMS_COLUMN_TEXT + TEXT_TYPE + NOT_NULL + COMMA_SEP +
					SMSEntry.SMS_COLUMN_TO + TEXT_TYPE + NOT_NULL + COMMA_SEP +
					SMSEntry.SMS_COLUMN_RULE + TEXT_TYPE + NOT_NULL + ")";
	
	//SQL commands to drop (delete) the tables
	@SuppressWarnings("unused")
	private static final String SQL_DELETE_RULE_TABLE = 
			"DROP TABLE IF EXISTS " + RuleEntry.RULE_TABLE_NAME;
	@SuppressWarnings("unused")
	private static final String SQL_DELETE_SMS_TABLE =
			"DROP TABLE IF EXISTS " + SMSEntry.SMS_TABLE_NAME;

	private static final String DATABASE_NAME = "ATMDatabase.db";
	private static final int DATABASE_VERSION = 1;

	/**
	 * @param context
	 * @param name - The name of the database
	 * @param factory
	 * @param version
	 */
	public RuleDatabaseSQLHelper(Context context, String name,CursorFactory factory, int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.i(logTag, "Constructor called " + SQL_CREATE_RULE + "\n" + SQL_CREATE_SMS);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_RULE);
		db.execSQL(SQL_CREATE_SMS);
		Log.i(logTag, "Table created (onCreate called) " + SQL_CREATE_RULE + "\n" + SQL_CREATE_SMS);
	}


	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 * 
	 * Does nothing
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL(SQL_DELETE_RULE_TABLE);
//		Log.i(logTag,  "Database upgraded from " + oldVersion + " to " + newVersion);
	}
}