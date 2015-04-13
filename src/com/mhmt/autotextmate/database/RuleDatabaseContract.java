package com.mhmt.autotextmate.database;


import android.provider.BaseColumns;

/**
 * 
 * @author Mehmet Kologlu
 * @version November April 13, 2015
 * 
 */
public class RuleDatabaseContract {
	//To prevent someone from accidentally instantiating the contract
	//give it an empty constructor

	public RuleDatabaseContract() {}

	/*Inner class defines table contents */
	public static abstract class RuleEntry implements BaseColumns{

		//Income table
		public static final String RULE_TABLE_NAME = "rules";
		public static final String RULE_COLUMN_NAME = "name";
		public static final String RULE_COLUMN_DESCRIPTION = "description";
		public static final String RULE_COLUMN_TEXT = "text";
		public static final String RULE_COLUMN_ONLYCONTACTS = "onlyContacts";
		public static final String RULE_COLUMN_STATUS = "status";
	}

}
