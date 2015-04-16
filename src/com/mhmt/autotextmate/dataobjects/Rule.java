package com.mhmt.autotextmate.dataobjects;


/**
 * 
 * @author Mehmet Kologlu
 * @version November April 13, 2015
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
	public Rule(String name, String description, String text, Boolean onlyContacts) {
		this.name = name;
		this.description = description;
		this.text = text;
		this.onlyContacts = onlyContacts ? 1 : 0;
		this.status = 1;
	}
	
	/**
	 * 
	 * @param name	Name of the rule
	 * @param description Description of the rule
	 * @param text Text SMS text of the rule
	 * @param onlyContacts	Should the rule only apply to contacts?
	 * @param status Is the rule on or off
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
	
	public int getstatus() {
		return status;
	}
	
	public String toString() {
		return name + " " + description;
	}
	
	
}
