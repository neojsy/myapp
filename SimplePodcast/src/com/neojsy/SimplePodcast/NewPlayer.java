package com.neojsy.SimplePodcast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class NewPlayer extends Activity implements Runnable {
	static final String TAG = "SimplePodcast";

	private ImageButton bt1;
	private ImageButton bt2;
	private ImageButton bt3;
	private ProgressBar bar;

	private NewPlayerService pService;
	private Handler handler = new Handler();
	private Thread background;

	boolean nowPlaying = false;
	boolean stopNplay =false;
	
	String s_mp3fileName;
	String s_imgfileName;
	String s_title;
	String s_maker;
	String s_summery;
	String s_listPos;
	String s_datafilename;
	String s_datafilepath;
	String s_startTime = "0";

	String backup_mp3fileName;
	String backup_imgfileName;
	String backup_title;
	String backup_maker;
	String backup_summery;
	String backup_listPos;
	String backup_datafilename;
	String backup_datafilepath;
	String backup_startTime = "0";
	
	int mp3fulltime;
	int mp3playtime;

	int button_play = 1;
	int button_back = 2;
	int button_forward = 3;
	int new_play = 50;

	String startType;
	String mstopByCall;
	String mstopByCallC;
	
	int control_1_time = 30000;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newplayer2);

		drawWallpaper();
		
		registerComponent();
		setSeekTime();

		Intent gintent = getIntent();
		
		startType = gintent.getStringExtra("startType");
		mstopByCall = gintent.getStringExtra("stopByCall");
		
		
		if(mstopByCall.equals("yes")){
			mstopByCallC = gintent.getStringExtra("stopByCallC");
			if(mstopByCallC.equals("start")){
				backup_mp3fileName = s_mp3fileName = gintent.getStringExtra("downfilename");
				backup_imgfileName = s_imgfileName = gintent.getStringExtra("imagefilename");
				backup_title = s_title = gintent.getStringExtra("title");
				backup_maker = s_maker = gintent.getStringExtra("maker");
				backup_summery = s_summery = gintent.getStringExtra("subtitle");
				backup_listPos = s_listPos = gintent.getStringExtra("position");
				backup_datafilename = s_datafilename = gintent.getStringExtra("datafilename");
				backup_datafilepath = s_datafilepath = gintent.getStringExtra("datafilepath");
				backup_startTime = s_startTime = gintent.getStringExtra("startTime");
				
				buttonAction(new_play);
				startprogressParse();
			}else{
				musicStop();
			}
			finish();
		}else{
		if(startType.equals("new")){
			
			backup_mp3fileName = s_mp3fileName = gintent.getStringExtra("downfilename");
			backup_imgfileName = s_imgfileName = gintent.getStringExtra("imagefilename");
			backup_title = s_title = gintent.getStringExtra("title");
			backup_maker = s_maker = gintent.getStringExtra("maker");
			backup_summery = s_summery = gintent.getStringExtra("subtitle");
			backup_listPos = s_listPos = gintent.getStringExtra("position");
			backup_datafilename = s_datafilename = gintent.getStringExtra("datafilename");
			backup_datafilepath = s_datafilepath = gintent.getStringExtra("datafilepath");
			backup_startTime = s_startTime = gintent.getStringExtra("startTime");
			
			buttonAction(new_play);
		}
		else if(startType.equals("afternew")){
			
			backup_mp3fileName = s_mp3fileName = gintent.getStringExtra("downfilename");
			backup_imgfileName = s_imgfileName = gintent.getStringExtra("imagefilename");
			backup_title = s_title = gintent.getStringExtra("title");
			backup_maker = s_maker = gintent.getStringExtra("maker");
			backup_summery = s_summery = gintent.getStringExtra("subtitle");
			backup_listPos = s_listPos = gintent.getStringExtra("position");
			backup_datafilename = s_datafilename = gintent.getStringExtra("datafilename");
			backup_datafilepath = s_datafilepath = gintent.getStringExtra("datafilepath");
			backup_startTime = s_startTime = gintent.getStringExtra("startTime");
			
			Log.d(TAG, "onCreate nowPlaying");
			stopNplay = true;		
		}
		
		startprogressParse();
		}
	}




	private void buttonAction(int i) {
		// TODO Auto-generated method stub
		if (i == button_play) {
			Log.d(TAG, "buttonAction button_play fileName : " + s_mp3fileName);

			if (!nowPlaying) {
				musicStart();
			} else {
				musicStop();
			}
		} else if (i == button_back) {
			int time = mp3playtime;
			if((time - control_1_time) > 0){
				musicStop();
				Util.Str2Int(s_startTime);
				s_startTime = Util.Int2Str(time - control_1_time);
				musicStart();
			}
		} else if (i == button_forward) {
			int time = mp3playtime;
			if((time + control_1_time) < mp3fulltime){
				musicStop();
				s_startTime = Util.Int2Str(time + control_1_time);
				musicStart();
			}
		} else if (i == new_play){
			//START NEW MUSIC
			musicStart();
		}

	}

	
	
	private void musicStop(){
		saveListenInfo(0);
		Intent intent = new Intent(this, NewPlayerService.class);
		stopService(intent);
		ImageButton bt = (ImageButton)this.findViewById(R.id.ImageButton01);
		bt.setImageResource(R.drawable.playerplay);
	}
	
	private void musicStart(){
		Intent intent = new Intent(NewPlayer.this,
				NewPlayerService.class);
		intent.putExtra("fileName", s_mp3fileName);
		intent.putExtra("imagefilename", s_imgfileName);
		intent.putExtra("title", s_title);
		intent.putExtra("maker", s_maker);
		intent.putExtra("subtitle", s_summery);
		intent.putExtra("position", s_listPos);
		intent.putExtra("datafilename", s_datafilename);
		intent.putExtra("datafilepath", s_datafilepath);
		intent.putExtra("starttime", s_startTime);
		startService(intent);
		ImageButton bt = (ImageButton)this.findViewById(R.id.ImageButton01);
		bt.setImageResource(R.drawable.playstop);
		background = new Thread(null, this, "test");
	}
	
	private void musicFinish(){
		SetData sData = new SetData(NewPlayer.this);
		if(sData.getEndFileDelete()){
			File file = new File(Environment.getExternalStorageDirectory()+ "/SimpleCast/" +s_mp3fileName);
			if (file.exists()){
				//Log.d(TAG, "file exist. delete");
				file.delete();
			}
		}
		saveListenInfo(1);
		finish();
	}
	
	private BroadcastReceiver completeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String state = intent.getStringExtra("state");
			String nowtime = intent.getStringExtra("nowtime");
			String totaltime = intent.getStringExtra("totaltime");

			if (state.equals("start")) {
				nowPlaying = true;
				mp3fulltime = Util.Str2Int(totaltime);
				mp3playtime = Util.Str2Int(nowtime);
				// ImageView pb4 = (ImageView) findViewById(R.id.imageButtonp);

				// pb4.setImageResource(R.drawable.p_sn1);

			} else if (state.equals("stop")) {
				nowPlaying = false;
				mp3fulltime = Util.Str2Int(totaltime);
				mp3playtime = Util.Str2Int(nowtime);
				// ImageView pb4 = (ImageView) findViewById(R.id.imageButtonp);

				// pb4.setImageResource(R.drawable.p_pn1);

			} else if (state.equals("pos")) {

				progressParse.dismiss();
					
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
				
				if(stopNplay){
					Log.d(TAG, "receiver stopnplay");
					
					musicStop();
					
					s_mp3fileName = backup_mp3fileName;
					s_imgfileName = backup_imgfileName;
					s_title = backup_title;
					s_maker = backup_maker;
					s_summery = backup_summery;
					s_listPos = backup_listPos;
					s_datafilename = backup_datafilename;
					s_datafilepath = backup_datafilepath;
					s_startTime = backup_startTime;
					
					stopNplay = false;
					buttonAction(new_play);
					startprogressParse();
					
				}else{
					drawAll();
				}
				
			} else if (state.equals("finish")) {
				musicFinish();

			} else if (state.equals("err")) {

			}
		}
	};

	private void drawAll() {
		drawImage();
		drawTime();
		drawText();
	}

	private void drawTime() {
		TextView playtime = (TextView) findViewById(R.id.fulltime);
		TextView fulltime = (TextView) findViewById(R.id.playtime);

		playtime.setText(Util.msInt2TimeString(mp3playtime));
		fulltime.setText(Util.msInt2TimeString(mp3fulltime));
		
		m_sb.setProgress(getTimeTo100p(mp3playtime));
	}

	private void drawText() {
		TextView title = (TextView) findViewById(R.id.pTitle);
		TextView summery = (TextView) findViewById(R.id.pIntro);

		title.setText(s_title);
		summery.setText(s_summery);

	}
	
	private void drawWallpaper(){
		ImageView wallpaper = (ImageView) findViewById(R.id.wall);
		
		int wallpapernum = Util.getRandomNumber(1,9);
		
		switch(wallpapernum){
		case 1:
			wallpaper.setImageResource(R.raw.wall1);
			break;
		case 2:
			wallpaper.setImageResource(R.raw.wall2);
			break;
		case 3:
			wallpaper.setImageResource(R.raw.wall3);
			break;
		case 4:
			wallpaper.setImageResource(R.raw.wall4);
			break;
		case 5:
			wallpaper.setImageResource(R.raw.wall5);
			break;
		case 6:
			wallpaper.setImageResource(R.raw.wall6);
			break;
		case 7:
			wallpaper.setImageResource(R.raw.wall7);
			break;	
		case 8:
			wallpaper.setImageResource(R.raw.wall8);
			break;				
		}
	}

	private void drawImage() {
		

		
		ImageView album = (ImageView) findViewById(R.id.palbum);

		if (s_imgfileName.equals("no")) {
			// //Log.d(TAG, "image no set");
			album.setImageResource(R.drawable.no);
		} else {

			File file = new File(s_imgfileName);
			if (file.exists()) {
				// //Log.d(TAG, "image exist");

				Display dp = ((WindowManager) getSystemService(WINDOW_SERVICE))
						.getDefaultDisplay();
				int width = dp.getWidth();

				Bitmap tempBitmap = BitmapFactory.decodeFile(s_imgfileName);
				Bitmap bitmap = Bitmap.createScaledBitmap(tempBitmap, width,
						width, true);
				album.setImageBitmap(bitmap);

			} else {
				// //Log.d(TAG, "image no exist");
				album.setImageResource(R.drawable.no);
			}
		}
		
		if(nowPlaying){
			ImageButton bt = (ImageButton)this.findViewById(R.id.ImageButton01);
			bt.setImageResource(R.drawable.playstop);
		}
	}


	private SeekBar m_sb;
	int seekBarTime = -1;
	private SeekBar.OnSeekBarChangeListener listenGenerator = new SeekBar.OnSeekBarChangeListener(){

			public void onStopTrackingTouch(SeekBar seekBar){
				Log.d(TAG, "onStopTrackingTouch");
				if(seekBarTime != -1){
					int time = get100pToTime(seekBarTime);
					s_startTime = Util.Int2Str(get100pToTime(seekBarTime));
					Log.d(TAG, "time "+ time);
					if(nowPlaying){
						musicStop();
						musicStart();
					}
					else{
						drawTime();
					}
				}
			}
		   
			//thumb 을 잡았을때 날라오는 메세지
			public void onStartTrackingTouch(SeekBar seekBar){
				Log.d(TAG, "onStartTrackingTouch");
			}
		   
			//thumb 의 위치가 변경될 때마다 오는 메세지
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				Log.d(TAG, "onProgressChanged : "+progress);
				seekBarTime = progress;
				
		}
	};
	
	private int get100pToTime(int per){
		int sec;
		sec = (per*mp3fulltime)/100;
		return sec;
	}
	
	private int getTimeTo100p(int sec){
		int per;
		per = (sec*100)/mp3fulltime;
		return per;
	}

	private void setSeekTime(){
    	SetData sData = new SetData(NewPlayer.this);
    	int seektime = sData.getSeektime();
    	switch(seektime){
    	case 0:
    		control_1_time = 5000;
    		break;
    	case 1:
    		control_1_time = 15000;
    		break;
    	case 2:
    		control_1_time = 30000;
    		break;
    	case 3:
    		control_1_time = 60000;
    		break;
    	case 4:
    		control_1_time = 120000;
    		break;
    	case 5:
    		control_1_time = 300000;
    		break;
    	}
	}
	
	private void registerComponent() {
		
        m_sb = (SeekBar)findViewById(R.id.seekBar1);
    	m_sb.setOnSeekBarChangeListener(listenGenerator);
    	
		bt1 = (ImageButton) findViewById(R.id.ImageButton01);
		bt2 = (ImageButton) findViewById(R.id.ImageButton02);
		bt3 = (ImageButton) findViewById(R.id.ImageButton03);

		bt1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				buttonAction(button_play);
			}
		});
		bt2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				buttonAction(button_back);
			}
		});
		bt3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				buttonAction(button_forward);
			}
		});
	}
	
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
		IntentFilter completeFilter = new IntentFilter(
				"com.neojsy.SimplePodcast.MUSIC");
		registerReceiver(completeReceiver, completeFilter);
	}

	public void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
		unregisterReceiver(completeReceiver);
	}
	
	private Runnable updateBar = new Runnable() {

		public void run() {
			Log.d(TAG, "Runnable updateBar"); // UI thread는 간단하게 * 짠다. loop문같은거
												// 넣지않는다. // 6. bar가 새롭게 그려진다.
			bar.setProgress(pService.getCurrentPosition());
		}
	};

	ArrayList<ItemEpisode> ie = new ArrayList<ItemEpisode>();
	private void saveListenInfo(int z){
		Log.d(TAG, "saveListenInfo");
    	ArrayList<String> mList = new ArrayList<String>();
    	mList.clear();
		
    	LoadFileToList lf = new LoadFileToList(NewPlayer.this);
    	File file = new File(s_datafilepath+s_datafilename);
		if (file.exists()){
			try {
				mList = lf.getList(s_datafilename);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UpdateCast uc = new UpdateCast(NewPlayer.this);
			ie = uc.Str2Epi(mList);
		}
		
		if(z == 1){
			//FINISH
			int t;
			for(t=0;t<ie.size();t++){
				////Log.d(TAG, ie.get(t).title);
				if(s_title.equals(ie.get(t).title)){
					////Log.d(TAG, "Matched!! "+mp3title);
					ie.get(t).mp3PlayedTime = Util.Int2Str(mp3fulltime);
					ie.get(t).mp3FullTime = Util.Int2Str(mp3fulltime);
					ie.get(t).read = "ok";
					break;
				}
			}
		}else if(z == 0){
			//STOP
			int t;
			for(t=0;t<ie.size();t++){
				////Log.d(TAG, ie.get(t).title);
				if(s_title.equals(ie.get(t).title)){
					////Log.d(TAG, "Matched!! "+mp3title);
					ie.get(t).mp3PlayedTime = Integer.toString(mp3playtime);
					ie.get(t).mp3FullTime = Util.Int2Str(mp3fulltime);
					ie.get(t).read = "ing";
					break;
				}
			}
		}
		
		ArrayList<String> kList = new ArrayList<String>();
		kList.clear();
		UpdateCast uc = new UpdateCast(NewPlayer.this);
		kList = uc.Epi2Str(ie);
		LoadFileToList f = new LoadFileToList(NewPlayer.this);
		f.saveFileListToFile(s_datafilename, kList);
	}
	
	public void run() { //
	 bar.setMax(pService.getDuration()); 
	 while(pService.isFlag()) { try { Log.d(TAG,"run"); 
	 Thread.sleep(1000);
	 handler.post(updateBar); } catch (Exception e) {} }
	 
	 }
	
	ProgressDialog progressParse;
	private void startprogressParse(){
		progressParse = new ProgressDialog(NewPlayer.this);
		progressParse.setMessage("Please Wait..");
		progressParse.setOnCancelListener(new OnCancelListener() {
		    public void onCancel(DialogInterface dialog) {
		    }
		   });
		progressParse.show();
	}
}