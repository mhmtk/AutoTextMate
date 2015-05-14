package com.mhmt.autotextmate.broadcastreceivers;

import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author Mehmet Kologlu
 * @version November April 14, 2015
 * 
 */
public class CallReceiver extends BroadcastReceiver{

	private String logTag = "CallReceiver";
	private Context context;
	private DatabaseManager dbManager;
	private SmsManager smsManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(logTag, "Received call intent" + intent);

		// instantiate variables for the use of MPhoneStateListener
		dbManager = new DatabaseManager(context);
		smsManager = SmsManager.getDefault();
		this.context = context;

		MPhoneStateListener phoneListener=new MPhoneStateListener();
		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		telephony.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
	}

	private class MPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state,String incomingNumber){
			super.onCallStateChanged(state, incomingNumber);
			Log.i(logTag, "Call state changed");
			switch(state){
			case TelephonyManager.CALL_STATE_IDLE:
				Log.i(logTag, "IDLE");
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.i(logTag, "OFFHOOK " + incomingNumber);
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				Log.i(logTag, "RINGING");
				// TODO only make it reply once in the same call

				for (Rule r : dbManager.getEnabledCallRules()) { //Reply for each rule
					if (r.getOnlyContacts() == 1) { // Reply only if the sender no is in the contacts
						if (inContacts(incomingNumber)) { // Check if the sender is in the contacts
							String replyText = r.getText();
							smsManager.sendTextMessage(incomingNumber, null, replyText, null, null);
							//documentation & feedback
							Toast.makeText(context, "Replied to " + incomingNumber + ": " + replyText, Toast.LENGTH_SHORT).show();
							Log.i(logTag, "Sent out an SMS to " + incomingNumber);
						}
					}
					else {
						String replyText = r.getText();
						smsManager.sendTextMessage(incomingNumber, null, replyText, null, null);
						//documentation & feedback
						Toast.makeText(context, "Replied to " + incomingNumber + ": " + replyText, Toast.LENGTH_SHORT).show();		            		  
						Log.i(logTag, "Sent out an SMS to " + incomingNumber);
					}
				}
				break;
			} //end of ringing case
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