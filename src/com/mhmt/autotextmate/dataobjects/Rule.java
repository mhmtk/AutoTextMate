package com.mhmt.autotextmate.dataobjects;


/**
 * 
 * @author Mehmet Kologlu
 * @version November May 16, 2015
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
	 * Constructor with all fields except status, used when adding a rule,
	 * sets status = 1 because new rules are always set to be enabled.
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
	 * Constructor with all fields
	 * 
	 * @param name	Name of the rule
	 * @param description Description of the rule
	 * @param text Text SMS text of the rule
	 * @param onlyContacts Should the rule only apply to contacts?
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

	/**
	 * Constructing a Rule with only name and status, the rest will be either "" or 0.
	 * For use of getRule(widgetID), so it does less work, avoids unnecessary fields
	 * 
	 * @param name Name of the rule
	 * @param status The rules status
	 */
	public Rule(String name, int status) {
		this.name = name;
		this.description = "";
		this.text = "";
		this.onlyContacts = -1;
		this.replyTo = -1;
		this.status = status;
	}
	
	/**
	 * Used by getEnabled***Rules() of the DBManager, creates a lightweight Rule object.
	 * Parameter order is reversed to differentiate from Rule(String name, int status).
	 * 
	 * @param onlyContacts Should the rule only apply to contacts?
	 * @param text Reply text of the rule
	 */
	public Rule(String name, int onlyContacts, String text) {
		this.name = name;
		this.description = "";
		this.text = text;
		this.onlyContacts = onlyContacts;
		this.replyTo = -1;
		this.status = -1;
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
	 * @return an int corresponding to the replyTo field
	 */
	public int getReplyTo() {
		return replyTo;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String toString() {
		return name + ":\t" +  ((description.length() <= 30) ? (description) : (description.substring(0, 30) + "...")) + "\n"
				+ ((text.length() <= 60) ? (text) : (text.substring(0, 60) + "...")) + "\n"
				+ ((getReplyTo() == 0) ? "SMS & Calls" : ((getReplyTo() == 1) ? "SMS" : "Calls"))
				+ ((onlyContacts == 1) ? "+\n Contacts Only"  : "");
	}
}