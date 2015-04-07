package com.mhmt.autotextmate.dataobjects;

public class Rule {

	String name;
	String description;
	String text;
	Boolean onlyContacts;
	
	public Rule(String name, String description, String text, Boolean onlyContacts) {
		this.name = name;
		this.description = description;
		this.text = text;
		this.onlyContacts = onlyContacts;
	}
}
