package com.mhmt.autotextmate.broadcastreceivers;

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
 * @version November April 16, 2015
 * 
 */
public class SMSReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		String phoneNo = "";
		String message = "";
		
		Bundle bundle = intent.getExtras();
		SmsMessage[] msg = null;
		
		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			msg = new SmsMessage[pdus.length];
			for (int i=0; i<msg.length; i++) {
				msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				
				//get the phoneNo of the sender, and the text message
				phoneNo = msg[i].getOriginatingAddress();
				message = msg[i].getMessageBody().toString();
				
			}
			
			SmsManager smsManager = SmsManager.getDefault();
			
//			smsManager.sendTextMessage(phoneNo, null, message, null, null);
			
			//documentation & feedback
			Toast.makeText(context, "ATM has replied to a SMS", Toast.LENGTH_SHORT).show();
			Log.i("SMSReceiver", "Sent out an SMS");
		}
	}
	
	public void invokeRules() {
		
	}
}
