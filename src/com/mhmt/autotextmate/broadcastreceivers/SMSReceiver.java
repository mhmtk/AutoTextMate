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
 * @version November April 7, 2015
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
				
				phoneNo = msg[i].getOriginatingAddress();
				message = msg[i].getMessageBody().toString();
				
			}
			Toast.makeText(context, "replied", Toast.LENGTH_SHORT).show();
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNo, null, message, null, null);
			Log.i("SMSReceiver", "Sent out an SMS");
		}
	}
	
	public void invokeRules() {
		
	}
}
