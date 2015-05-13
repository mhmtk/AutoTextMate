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
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author Mehmet Kologlu
 * @version November April 7, 2015
 * 
 */
public class CallReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		MPhoneStateListener phoneListener=new MPhoneStateListener();
		TelephonyManager telephony = (TelephonyManager) 
				context.getSystemService(Context.TELEPHONY_SERVICE);
		telephony.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
	}
}