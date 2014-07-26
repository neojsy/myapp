package com.neojsy.SimplePodcast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadMng extends Activity {
	// public ..
	ProgressBar m_PrograssReceive;
	private int tempCount = 0;

	String mState;
	String mTitle;
	String mPercent;
	String mDownK;
	String mTotalK;
	
	int fr;
	
	private BroadcastReceiver completeReceiver = new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent) {
		
			mState = intent.getStringExtra("state");
			mTitle = intent.getStringExtra("title");
			mDownK = intent.getStringExtra("downK");
			mTotalK = intent.getStringExtra("totalK");
			mPercent = intent.getStringExtra("percent");
	
			draw();
		}
	};
	
	private void draw(){

		TextView title = (TextView) findViewById(R.id.dtitle);
		TextView statu = (TextView) findViewById(R.id.dstatus);
		TextView perce = (TextView) findViewById(R.id.dpercent);
		TextView downk = (TextView) findViewById(R.id.ddownk);
		TextView total = (TextView) findViewById(R.id.dtotalk);
		ImageView album = (ImageView) findViewById(R.id.dpicture1);

		title.setText(mTitle);
		perce.setText(mPercent+" %");
		downk.setText(mDownK);
		total.setText(mTotalK);
		
		if(mState.equals("down")){
			statu.setText("다운로드 중...");
		}else if(mState.equals("end")){
			statu.setText("다운로드 완료");
		}
		
		m_PrograssReceive.setProgress(Util.Str2Int(mPercent));
		if(fr > 8)
			fr = 1;
		
		if(fr == 1)
			album.setImageResource(R.drawable.f1);
		else if(fr == 2)
			album.setImageResource(R.drawable.f2);
		else if(fr == 3)
			album.setImageResource(R.drawable.f3);
		else if(fr == 4)
			album.setImageResource(R.drawable.f4);
		else if(fr == 5)
			album.setImageResource(R.drawable.f5);
		else if(fr == 6)
			album.setImageResource(R.drawable.f6);
		else if(fr == 7)
			album.setImageResource(R.drawable.f7);
		else if(fr == 8)
			album.setImageResource(R.drawable.f8);
		
		fr++;
	}
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.downloadmng);

		m_PrograssReceive = (ProgressBar) findViewById(R.id.ID_PGB_RCVPER);
		
		fr = 1;
	}


	@Override
	protected void onStart() {
		super.onStart();
        IntentFilter completeFilter = new IntentFilter("com.neojsy.SimplePodcast.DOWN");
        registerReceiver(completeReceiver, completeFilter); 
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(completeReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected void OnActivityResult(int requestCode, int resultCode, Intent data) {

	}
}
/*
 * public void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState);
 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
 * WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
 * 
 * AlertDialog.Builder alt_bld = new AlertDialog.Builder(DownloadMng.this);
 * alt_bld.setMessage(getResources().getString(R.string.msg_canceldown))
 * .setCancelable(false) //.setIcon(R.drawable.delete)
 * .setPositiveButton(getResources().getString(R.string.yes), new
 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
 * dialog, int id) { // Action for 'Yes' Button Intent intent = new
 * Intent(DownloadMng.this, Download.class); // intent.putExtra("downfilename",
 * ie.get(index).title+".mp3"); intent.putExtra("downfilename", "stop");
 * intent.putExtra("downloadUrl", "stop"); intent.putExtra("name", "stop");
 * startService(intent); finish();
 * 
 * } }) .setNegativeButton(getResources().getString(R.string.no), new
 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
 * dialog, int id) { // Action for 'NO' Button finish(); } }); AlertDialog alert
 * = alt_bld.create(); // Title for AlertDialog // alert.setTitle("Title"); //
 * Icon for AlertDialog alert.show(); } }
 */
