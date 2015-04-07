package com.mhmt.autotextmate.database;

import com.mhmt.autotextmate.database.RuleDatabaseContract.RuleEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class RuleDatabaseSQLHelper extends SQLiteOpenHelper{

	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	public static boolean INITIALIZED = false;

	//SQL command to create INCOME table
	private static final String SQL_CREATE_INCOME=
			"CREATE TABLE " + RuleEntry.INCOME_TABLE_NAME + " (" +
					RuleEntry._ID + " INTEGER PRIMARY KEY," +
					RuleEntry.INCOME_COLUMN_TYPE + TEXT_TYPE + COMMA_SEP +
					RuleEntry.INCOME_COLUMN_HOUR + TEXT_TYPE + COMMA_SEP +
					RuleEntry.INCOME_COLUMN_RATE + TEXT_TYPE + COMMA_SEP +
					RuleEntry.INCOME_COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
					RuleEntry.INCOME_COLUMN_RECURRENCE +TEXT_TYPE+")";
	
	//SQL command to create EXPENSE table
	private static final String SQL_CREATE_EXPENSE=
			"CREATE TABLE " + RuleEntry.EXPENSE_TABLE_NAME + " (" +
					RuleEntry._ID + " INTEGER PRIMARY KEY," +
					RuleEntry.EXPENSE_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
					RuleEntry.EXPENSE_COLUMN_TYPE + TEXT_TYPE + COMMA_SEP +
					RuleEntry.EXPENSE_COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
					RuleEntry.EXPENSE_COLUMN_AMOUNT + TEXT_TYPE + ")";

	//SQL command to drop (delete) the income table
	private static final String SQL_DELETE_INCOME = 
			"DROP TABLE IF EXISTS " + RuleEntry.INCOME_TABLE_NAME;
	
	//SQL command to drop (delete) the expense table
	private static final String SQL_DELETE_EXPENSE =
			"DROP TABLE IF EXISTS " + RuleEntry.EXPENSE_TABLE_NAME;

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
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_INCOME);
		db.execSQL(SQL_CREATE_EXPENSE);
	}


	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_INCOME);
		db.execSQL(SQL_DELETE_EXPENSE);
	}
	
	public void delete(SQLiteDatabase db, String name, String type, String date, String amount) {
		db.execSQL("DELETE FROM " + RuleEntry.EXPENSE_TABLE_NAME + " WHERE " 
				+ RuleEntry.EXPENSE_COLUMN_NAME + "='" + name + "' AND "
				+ RuleEntry.EXPENSE_COLUMN_TYPE + "='" + type + "' AND "
				+ RuleEntry.EXPENSE_COLUMN_DATE + "='" + date + "' AND "
				+ RuleEntry.EXPENSE_COLUMN_AMOUNT + "='" + amount + "'"
				);
	}
}
