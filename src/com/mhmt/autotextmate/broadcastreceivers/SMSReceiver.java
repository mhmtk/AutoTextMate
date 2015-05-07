package com.mhmt.autotextmate.broadcastreceivers;

import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author Mehmet Kologlu
 * @version November May 5, 2015
 * 
 */
public class SMSReceiver extends BroadcastReceiver{

	
	private static long delay = 2000;
	private String logTag = "SMSReceiver"; 

	@Override
	public void onReceive(final Context context, Intent intent) {
		String phoneNo = "";
//		String message = "";
		
		final DatabaseManager dbManager = new DatabaseManager(context);
		
		
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
		              for (Rule r : dbManager.getApplicableRules()) { //Reply for each rule
		            	  String replyText = r.getText();
		            	  smsManager.sendTextMessage(pn, null, replyText, null, null);
		            	  //documentation & feedback
		            	  Toast.makeText(context, "Replied to " + pn + ": " + replyText, Toast.LENGTH_SHORT).show();
		            	  Log.i(logTag, "Sent out an SMS");
		              }
		         } 
		    }, delay );
		}
	}
}
