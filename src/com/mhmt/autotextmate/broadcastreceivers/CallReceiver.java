package com.mhmt.autotextmate.broadcastreceivers;

import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.Rule;
import com.mhmt.autotextmate.dataobjects.SMS;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
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
 * @version November May 20, 2015
 * 
 */
public class CallReceiver extends BroadcastReceiver{

	private String logTag = "CallReceiver";
	private DatabaseManager dbManager;
	private SmsManager smsManager = SmsManager.getDefault();
	private AudioManager aManager;

	private static MPhoneStateListener phoneListener;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(logTag, "Received call intent");

		// instantiate variables for the use of MPhoneStateListener

		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		if (phoneListener == null) {
			dbManager = new DatabaseManager(context);
			phoneListener = new MPhoneStateListener(context);
			aManager = (AudioManager) context
	                .getSystemService(Context.AUDIO_SERVICE);
			telephony.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
		}
	}

	private class MPhoneStateListener extends PhoneStateListener {

		// Flag used to avoid invoking rules multiple times due to receiving more than one one RINGING state change
		private boolean handled = false;
		private Context mContext;
		

		private MPhoneStateListener(Context c) {
			mContext = c;
		}
		@Override
		public void onCallStateChanged(int state,String incomingNumber){
			super.onCallStateChanged(state, incomingNumber);
			Log.i(logTag, "Call state changed");
			switch(state){
			case TelephonyManager.CALL_STATE_IDLE:
				Log.i(logTag, "IDLE");
				handled = false;
				Log.i(logTag, "Reset handled flag");
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.i(logTag, "OFFHOOK ");
				handled = false;
				Log.i(logTag, "Reset handled flag");
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				Log.i(logTag, "RINGING");
				if (!handled) { // In order 
					Log.i(logTag, "Call hasn't been handled, will invoke applicable rules");
					for (Rule r : dbManager.getEnabledCallRules()) { //Reply for each rule
						if (r.getOnlyContacts() == 1) { // Reply only if the sender no is in the contacts
							if (inContacts(mContext, incomingNumber)) { // Check if the sender is in the contacts
								applyRule(r, incomingNumber);
							}
						}
						else {
							applyRule(r, incomingNumber);
						}
					} //end of for each loop
					handled = true;
				} // end of if (!handled)
				else
					Log.i(logTag, "Call has been handled, will not try to invoke rules");
				break;
			} //end of ringing case
		}
		
		/**
		 * Applies the rule for the given phoneNo.
		 * 
		 * That is sends and SMS, and mutes the ringer.
		 * 
		 * @param r
		 * @param phoneNo
		 */
		private void applyRule(Rule r, String phoneNo) {
			// Reply
			String replyText = r.getText();
			smsManager.sendTextMessage(phoneNo, null, replyText, null, null);

			// Add the reply to the Outbox DB
			dbManager.addSMS(new SMS(System.currentTimeMillis(), replyText, String.valueOf(phoneNo), r.getName()));

			//documentation & feedback
			Toast.makeText(mContext, "Replied to " + phoneNo + ": " + replyText, Toast.LENGTH_SHORT).show();
			Log.i(logTag, "Sent out an SMS to " + phoneNo);
			
			aManager.setStreamMute(AudioManager.STREAM_RING, true);
			Log.i(logTag, "Muted");
		}
	}
	
	/**
	 * Checks if the given no is in the contacts
	 * 
	 * @param c Context 
	 * @param no The phone no to check for
	 * @return True if the passed no is saved in the contacts, false otherwise 
	 */
	private boolean inContacts(Context c, String no) {
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(no));
		//	    String name = "?";

		ContentResolver contentResolver = c.getContentResolver();
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