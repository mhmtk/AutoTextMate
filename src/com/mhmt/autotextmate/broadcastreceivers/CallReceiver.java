package com.mhmt.autotextmate.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 
 * @author Mehmet Kologlu
 * @version November April 13, 2015
 * 
 */
public class CallReceiver extends BroadcastReceiver{
	
	private String logTag = "CallReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(logTag, "Received call intent" + intent);
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
				break;
			}
		} 
	}
}