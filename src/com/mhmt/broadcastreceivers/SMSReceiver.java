package com.mhmt.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		SmsMessage[] msg = null;
		String str = "";
		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			msg = new SmsMessage[pdus.length];
			for (int i=0; i<msg.length; i++) {
				msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				str += "SMS received from " + msg[i].getOriginatingAddress() + ":"
						+ msg[i].getMessageBody().toString() + "\n";
			}
			Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
		}
	}
	
	public void invokeRules() {
		
	}
}
