package com.mhmt.autotextmate.dataobjects;


/**
 * 
 * @author Mehmet Kologlu
 * @version November April 7, 2015
 * 
 */
public class Rule {

	String name;
	String description;
	String text;
	Boolean onlyContacts;
	
	/**
	 * 
	 * @param name	Name
	 * @param description Description
	 * @param text Text
	 * @param onlyContacts	Only contacts?
	 */
	public Rule(String name, String description, String text, Boolean onlyContacts) {
		this.name = name;
		this.description = description;
		this.text = text;
		this.onlyContacts = onlyContacts;
	}
}
