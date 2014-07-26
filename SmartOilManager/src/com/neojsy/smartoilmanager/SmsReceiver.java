package com.neojsy.smartoilmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    static final String logTag = "OIL";
    static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
           if (intent.getAction().equals(ACTION)) {
                   //Bundel 澄 眉农
                   Bundle bundle = intent.getExtras();
                   if (bundle == null) {
                          return;
                   }

                   //pdu 按眉 澄 眉农
                   Object[] pdusObj = (Object[]) bundle.get("pdus");
                   if (pdusObj == null) {
                          return;
                   }

                   //message 贸府
                   SmsMessage[] smsMessages = new SmsMessage[pdusObj.length];
                   for (int i = 0; i < pdusObj.length; i++) {
                          smsMessages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                          /*
                          Log.d(logTag, "NEW SMS " + i + "th");
                          Log.d(logTag, "DisplayOriginatingAddress : "
                                         + smsMessages[i].getDisplayOriginatingAddress());
                          Log.d(logTag, "DisplayMessageBody : "
                                         + smsMessages[i].getDisplayMessageBody());
                          Log.d(logTag, "EmailBody : "
                                         + smsMessages[i].getEmailBody());
                          Log.d(logTag, "EmailFrom : "
                                         + smsMessages[i].getEmailFrom());
                          Log.d(logTag, "OriginatingAddress : "
                                         + smsMessages[i].getOriginatingAddress());
                          Log.d(logTag, "MessageBody : "
                                         + smsMessages[i].getMessageBody());
                          Log.d(logTag, "ServiceCenterAddress : "
                                         + smsMessages[i].getServiceCenterAddress());
                          Log.d(logTag, "TimestampMillis : "
                                         + smsMessages[i].getTimestampMillis());
                           */
                          String text = smsMessages[i].getDisplayMessageBody();
                          
                          CheckOilMsgStr cos = new CheckOilMsgStr(text);
                          if(cos.isOilMsg()){
                        	  
                        	  String won = cos.getWon();
                        	  String brand = cos.getBrand();
                              intent.putExtra("SMS", text);
                              intent.putExtra("WON", won);
                              intent.putExtra("BRAND", brand);
                              intent.setClass(context, SaveData.class);
                              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                              context.startActivity(intent);
                        	  
                          }
                   }
           }
    }
}
