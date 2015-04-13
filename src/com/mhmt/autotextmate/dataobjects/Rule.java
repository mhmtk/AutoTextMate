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
	Boolean onlyContacts;
	Boolean status;
	
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
		this.onlyContacts = onlyContacts;
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

	public Boolean getOnlyContacts() {
		return onlyContacts;
	}
	
	
}
