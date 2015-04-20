package com.mhmt.autotextmate.broadcastreceivers;

import com.mhmt.autotextmate.database.DatabaseManager;
import com.mhmt.autotextmate.dataobjects.Rule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author Mehmet Kologlu
 * @version November April 20, 2015
 * 
 */
public class SMSReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		String phoneNo = "";
//		String message = "";
		
		DatabaseManager dbManager = new DatabaseManager(context);
		
		Bundle bundle = intent.getExtras();
		SmsMessage[] msg = null;
		
		if (bundle != null) {
			Log.i("SMSReceiver", "Non-null intent received");
			Object[] pdus = (Object[]) bundle.get("pdus");
			msg = new SmsMessage[pdus.length];
			for (int i=0; i<msg.length; i++) {
				msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				
				//get the phoneNo of the sender
				phoneNo = msg[i].getOriginatingAddress();
//				message = msg[i].getMessageBody().toString();
				
			}
			
			//Reply
			SmsManager smsManager = SmsManager.getDefault();
			for (Rule r : dbManager.getApplicableRules()) {
				String replyText = r.getText();
				smsManager.sendTextMessage(phoneNo, null, replyText, null, null);
				//documentation & feedback
				Toast.makeText(context, "Replied to" + phoneNo + ": " + replyText, Toast.LENGTH_SHORT).show();
				Log.i("SMSReceiver", "Sent out an SMS");
			}
			
		}
	}
}
