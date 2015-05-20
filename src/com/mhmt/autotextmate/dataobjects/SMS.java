package com.mhmt.autotextmate.dataobjects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * @author Mehmet Kologlu
 * @version November May 20, 2015
 * 
 */
public class SMS {

	private long time;
	private String text;
	private String to;
	private String rule;
	private DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());

	/**
	 * 
	 * @param time Time the SMS was sent
	 * @param text Text of the SMS
	 * @param to To whom the SMS was sent
	 * @param rule Name of the Rule the SMS was sent by
	 */
	public SMS(long time, String text, String to, String rule) {
		this.time = time;
		this.text = text;
		this.to = to;
		this.rule = rule;

	}

	public long getTimeAsMilli() {
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