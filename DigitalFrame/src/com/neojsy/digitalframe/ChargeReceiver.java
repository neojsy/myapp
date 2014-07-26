package com.neojsy.digitalframe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class ChargeReceiver extends BroadcastReceiver {

    static final String logTag = "OIL";
    static final String mstart = "android.intent.action.ACTION_POWER_CONNECTED";
    static final String mstop = "android.intent.action.ACTION_POWER_DISCONNECTED";
    @Override
    public void onReceive(Context context, Intent intent) {

           if (intent.getAction().equals(mstart)) {
        	   Log.d("ss", "ChargeReceiver ACTION_POWER_CONNECTED");

               intent.setClass(context, CheckAutoStartOption.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);
           }
           else if(intent.getAction().equals(mstop)) {
        	   Log.d("ss", "ChargeReceiver ACTION_POWER_DISCONNECTED");
           }
    }
}
