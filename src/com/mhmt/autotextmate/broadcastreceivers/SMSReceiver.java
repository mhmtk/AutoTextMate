package com.mhmt.autotextmate.broadcastreceivers;

import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author Mehmet Kologlu
 * @version November May 10, 2015
 * 
 */
public class SMSReceiver extends BroadcastReceiver{

	private static long delay = 2000; // 2 secs delay before responding
	private String logTag = "SMSReceiver"; 
	private Context context;

	@Override
	public void onReceive(final Context c, Intent intent) {
		context = c;

		String phoneNo = "";
		//		String message = "";

		final DatabaseManager dbManager = new DatabaseManager(c);

		Bundle bundle = intent.getExtras();
		SmsMessage[] msg = null;

		if (bundle != null) {
			Log.i(logTag, "Non-null intent received");
			Object[] pdus = (Object[]) bundle.get("pdus");
			msg = new SmsMessage[pdus.length];
			for (int i=0; i<msg.length; i++) {
				msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

				//get the phoneNo of the sender
				phoneNo = msg[i].getOriginatingAddress();
				//				message = msg[i].getMessageBody().toString();

			}
			//REPLY			
			final String pn = phoneNo;//re-create phone no string, to make it final 

			new Handler().postDelayed(new Runnable() { //Handler/Runnable usage in order to delay the reply
				public void run() {
					SmsManager smsManager = SmsManager.getDefault();
					for (Rule r : dbManager.getEnabledSMSRules()) { //Reply for each rule
						if (r.getOnlyContacts() == 1) { // Reply only if the sender no is in the contacts
							if (inContacts(pn)) { // Check if the sender is in the contacts
								String replyText = r.getText();
								smsManager.sendTextMessage(pn, null, replyText, null, null);
								//documentation & feedback
								Toast.makeText(context, "Replied to " + pn + ": " + replyText, Toast.LENGTH_SHORT).show();
								Log.i(logTag, "Sent out an SMS to " + String.valueOf(pn));
							}
						}
						else {
							String replyText = r.getText();
							smsManager.sendTextMessage(pn, null, replyText, null, null);
							//documentation & feedback
							Toast.makeText(context, "Replied to " + pn + ": " + replyText, Toast.LENGTH_SHORT).show();		            		  
							Log.i(logTag, "Sent out an SMS to " + String.valueOf(pn));
						}
					}
				} 
			}, delay );
		}
	}

	/**
	 * Checks if the given no is in the contacts
	 * 
	 * @param no The phone no to check for
	 * @return True if the passed no is saved in the contacts, false otherwise 
	 */
	public boolean inContacts(String no) {
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(no));
		//	    String name = "?";

		ContentResolver contentResolver = context.getContentResolver();
		Cursor contactLookup = contentResolver.query(uri,
				new String[] {BaseColumns._ID }, //ContactsContract.PhoneLookup.DISPLAY_NAME }
				null, null, null);

		if (contactLookup != null)
		{
			try {
				if (contactLookup.getCount() > 0) {
					Log.i(logTag, contactLookup.getCount() + " contact(s) found with the senders no");
					return true;
					//name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
					//String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
				}
			} finally {
					contactLookup.close();
			}
		}
		return false;
	}
}