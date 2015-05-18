package com.mhmt.autotextmate.dataobjects;

/**
 * 
 * @author Mehmet Kologlu
 * @version November May 18, 2015
 * 
 */
public class SMS {
	
	private int time;
	private String text;
	private String to;
	private String rule;
	
	public SMS(int time, String text, String to, String rule) {
		this.time = time;
		this.text = text;
		this.to = to;
		this.rule = rule;
		
	}
	
	public int getTime() {
		return time;
	}

	public String getText() {
		return text;
	}

	public String getTo() {
		return to;
	}

	public String getRule() {
		return rule;
	}
}