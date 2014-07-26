package com.neojsy.SimplePodcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class ChargeReceiver extends BroadcastReceiver {

	static final String logTag = "OIL";
	static final String ACTION = "com.neojsy.SimplePodcast.MUSIC";
	private String LOG_TAG = "CheckCall";
	private static int pState = TelephonyManager.CALL_STATE_IDLE;

	static String s_mp3fileName;
	static String s_imgfileName;
	static String s_title;
	static String s_maker;
	static String s_summery;
	static String s_listPos;
	static String s_datafilename;
	static String s_datafilepath;
	static String s_startTime = "0";
	static boolean nowPlaying = false;
	int mp3fulltime;
	int mp3playtime;
	static boolean puased = false;

	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (intent.getAction().equals(ACTION)) {
			String state = intent.getStringExtra("state");
			String nowtime = intent.getStringExtra("nowtime");
			String totaltime = intent.getStringExtra("totaltime");

			if (state.equals("pos")) {
				Log.d("onReceive", "music ing...");
				nowPlaying = true;

				mp3fulltime = Util.Str2Int(totaltime);
				mp3playtime = Util.Str2Int(nowtime);
				s_startTime = nowtime;

				s_mp3fileName = intent.getStringExtra("playFileName");
				s_imgfileName = intent.getStringExtra("imagefilename");
				s_title = intent.getStringExtra("title");
				s_maker = intent.getStringExtra("maker");
				s_summery = intent.getStringExtra("subtitle");
				s_listPos = intent.getStringExtra("position");
				s_datafilename = intent.getStringExtra("datafilename");
				s_datafilepath = intent.getStringExtra("datafilepath");
				
				Log.d("s_mp3fileName ", s_mp3fileName);
				Log.d("s_imgfileName ", s_imgfileName);
				Log.d("s_title ", s_title);
				Log.d("s_maker ", s_maker);
				Log.d("s_summery ", s_summery);
				Log.d("s_listPos ", s_listPos);
				Log.d("s_datafilename ", s_datafilename);
				Log.d("s_datafilepath ", s_datafilepath);
				Log.d("s_startTime ", s_startTime);
			} else if (state.equals("stop")) {
				nowPlaying = false;

			} else {
				TelephonyManager telManager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);

				telManager.listen(new PhoneStateListener() {
					public void onCallStateChanged(int state,
							String incomingNumber) {
						if (state != pState) {
							if (state == TelephonyManager.CALL_STATE_IDLE) {
								if (puased) {
									Log.d("onReceive","restart after call...");
									Log.d("s_mp3fileName ", s_mp3fileName);
									Log.d("s_imgfileName ", s_imgfileName);
									Log.d("s_title ", s_title);
									Log.d("s_maker ", s_maker);
									Log.d("s_summery ", s_summery);
									Log.d("s_listPos ", s_listPos);
									Log.d("s_datafilename ", s_datafilename);
									Log.d("s_datafilepath ", s_datafilepath);
									Log.d("s_startTime ", s_startTime);

									
									intent.putExtra("stopByCall", "yes");
									intent.putExtra("stopByCallC", "start");
							    	
							    	intent.putExtra("downfilename", s_mp3fileName);
									intent.putExtra("title", s_title);
									intent.putExtra("subtitle", s_summery);
									intent.putExtra("maker", s_maker);
									intent.putExtra("imagefilename", s_imgfileName);
									intent.putExtra("datafilename", s_datafilename);
									intent.putExtra("datafilepath", s_datafilepath);
									intent.putExtra("position", s_listPos);
									intent.putExtra("startTime", s_startTime);
									intent.setClass(context, NewPlayer.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									context.startActivity(intent);
								}
							} else if (state == TelephonyManager.CALL_STATE_RINGING) {
								if (nowPlaying) {
									intent.putExtra("stopByCall", "yes");
									intent.putExtra("stopByCallC", "stop");
									intent.setClass(context, NewPlayer.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									context.startActivity(intent);
									puased = true;
								}
							} else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {

							}
							pState = state;
						}
					}
				}, PhoneStateListener.LISTEN_CALL_STATE);

			}
		}
	}
}
