package com.mhmt.autotextmate.dataobjects;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yy");

	/**
	 * 
	 * @param time Time the SMS was sent
	 * @param text Text of the SMS
	 * @param to To whom the SMS was sent
	 * @param rule Name of the Rule the SMS was sent by
	 */
	public SMS(int time, String text, String to, String rule) {
		this.time = time;
		this.text = text;
		this.to = to;
		this.rule = rule;

	}

	public int getTimeAsMilli() {
		return time;
	}
	
	public String getTimeAsDate() {
		return dateFormat.format(new Date(time));
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

	public String toString() {
		return getTimeAsDate() + ", To: " + to + "\n" +
				text + "\n" +
				"by Rule " + rule;
	}
}