package com.mhmt.autotextmate.dataobjects;


/**
 * 
 * @author Mehmet Kologlu
 * @version November May 12, 2015
 * 
 */
public class Rule {

	String name;
	String description;
	String text;
	int onlyContacts;
	int replyTo;
	int status;

	/**
	 * 
	 * @param name	Name of the rule
	 * @param description Description of the rule
	 * @param text Text SMS text of the rule
	 * @param onlyContacts	Should the rule only apply to contacts?
	 * @param replyTo SMS, Call, or Both
	 */
	public Rule(String name, String description, String text, boolean onlyContacts, int replyTo) {
		this.name = name;
		this.description = description;
		this.text = text;
		this.onlyContacts = onlyContacts ? 1 : 0;
		this.replyTo = replyTo;
		this.status = 1;
	}
	
	/**
	 * 
	 * @param name	Name of the rule
	 * @param description Description of the rule
	 * @param text Text SMS text of the rule
	 * @param onlyContacts	Should the rule only apply to contacts?
	 * @param replyTo SMS, Call, or Both
	 * @param status Is the rule on or off
	 */
	public Rule(String name, String description, String text, int onlyContacts, int replyTo, int status) {
		this.name = name;
		this.description = description;
		this.text = text;
		this.onlyContacts = onlyContacts;
		this.status = status;
		this.replyTo = replyTo;
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
	
	/**
	 * 0 = Both
	 * 1 = SMS
	 * 2 = Call
	 * @return
	 */
	public int getReplyTo() {
		return replyTo;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String toString() {
		return name + ":\t" +  ((text.length() <= 30) ? (text) : (text.substring(0, 30) + "...")) + "\n"
				+ ((onlyContacts == 1) ? "Contacts Only"  : "Any sender");
	}
}