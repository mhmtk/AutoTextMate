package com.mhmt.autotextmate.dataobjects;


/**
 * 
 * @author Mehmet Kologlu
 * @version November April 8, 2015
 * 
 */
public class Rule {

	String name;
	String description;
	String text;
	int onlyContacts;
	int status;
	
	/**
	 * 
	 * @param name	Name of the rule
	 * @param description Description of the rule
	 * @param text Text SMS text of the rule
	 * @param onlyContacts	Should the rule only apply to contacts?
	 */
	public Rule(String name, String description, String text, int onlyContacts) {
		this.name = name;
		this.description = description;
		this.text = text;
		this.onlyContacts = onlyContacts;
	}
	
	/**
	 * 
	 * @param name	Name of the rule
	 * @param description Description of the rule
	 * @param text Text SMS text of the rule
	 * @param onlyContacts	Should the rule only apply to contacts?
	 */
	public Rule(String name, String description, String text, int onlyContacts, int status) {
		this.name = name;
		this.description = description;
		this.text = text;
		this.onlyContacts = onlyContacts;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getText() {
		return text;
	}

	public int getOnlyContacts() {
		return onlyContacts;
	}
	
	
}
