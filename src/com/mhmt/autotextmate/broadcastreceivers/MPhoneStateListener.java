package com.mhmt.autotextmate.broadcastreceivers;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MPhoneStateListener extends PhoneStateListener {
	
	private String logTag = "MPhoneStateListener";
	
	public void onCallStateChanged(int state,String incomingNumber){
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