package com.neojsy.SimplePodcast;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class NewPlayerService extends Service implements Runnable {
	static final String TAG = "SimplePodcast";
	MediaPlayer player;

	boolean flag = true;
	private int duration;
	private int currentPosition;

	String imagefilename;
	String mp3title;
	String datafilepath;
	String mp3maker;
	String mp3summery;
	String position;
	String datafilename;
	
	int startTime;
	
	// getter, setter함수
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	// thread 에 의해 실행되는 코드...
	// Bind되는 순간부터 run이 실행되면서 매 초마다 현재 Position를 설정한다.
	public void run() {
		// run을 빠져나가면 thread는 끝난다.
		// 장시간 해야되는건 주로 loop를 만들어 놓는다.
		while (isFlag()) // 음원이 끝나면 나가라.
		{
			setCurrentPosition(player.getCurrentPosition());
			SendMsg(SEND_POSITION);
			try {
				// sleep은 항상 try~cath와 같이 쓰여야 한다.
				Thread.sleep(1000); // milisecond unit
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	String fileName;

	private void MusicStart(){
		player = new MediaPlayer();
		try {
			player.setDataSource(getFileD());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		try {
			player.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		setDuration(player.getDuration());
		player.setLooping(false);
		if(startTime > 0){
			player.seekTo(startTime);
		}
		player.start();
		
    	player.setOnCompletionListener(new OnCompletionListener()
		{
			public void onCompletion(MediaPlayer arg0) {
				SendMsg(PLAY_FINISH);
				stopForeground(true);
				stopSelf();
		    }
		});
		
		SendMsg(PLAY_START);
		setFlag(true);

		Thread t = new Thread(null, this, "player");
		t.start(); // bind되는 순간 PlayService의 run()이 시작됨.
		
		Intent intent = new Intent(this, NewPlayer.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("startType", "no_new");
		intent.putExtra("stopByCall", "no");
		PendingIntent contentIntent = PendingIntent.getActivity(this.getBaseContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		Notification noti = new Notification(R.drawable.playnoti, "Podcast Play", System.currentTimeMillis());
		noti.setLatestEventInfo(this, mp3title, "SimplePodcast Playing", contentIntent);
		noti.flags = noti.flags|Notification.FLAG_ONGOING_EVENT;
		
		startForeground(1000, noti);
		
	}
	
	private void musicStop(){
		player.stop(); // service 멈추기
		SendMsg(PLAY_STOP);
		setFlag(false);
		
		stopForeground(true);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		musicStop();
	}

	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		//mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		super.onStart(intent, startId);
		fileName = intent.getStringExtra("fileName");
		imagefilename = intent.getStringExtra("imagefilename");
		mp3title = intent.getStringExtra("title");
		mp3maker = intent.getStringExtra("maker");
		mp3summery = intent.getStringExtra("subtitle");
		position = intent.getStringExtra("position");
    	datafilename = intent.getStringExtra("datafilename");
    	datafilepath = intent.getStringExtra("datafilepath");
		
		startTime = Util.Str2Int(intent.getStringExtra("starttime"));
		MusicStart();
		
		//return START_REDELIVER_INTENT;
	}

	
	final int PLAY_START = 0;
	final int PLAY_STOP = 1;
	final int PLAY_FINISH = 5;
	final int SEND_POSITION = 2;
	final int ERROR = 4;
	
	private void SendMsg(int type){
		Intent intent = new Intent("com.neojsy.SimplePodcast.MUSIC");
		
		switch(type){
		case PLAY_START:
			intent.putExtra("state", "start");  
			intent.putExtra("nowtime", Util.Int2Str(getCurrentPosition()));  
			intent.putExtra("totaltime", Util.Int2Str(getDuration()));
			break;
		case PLAY_STOP:
			intent.putExtra("state", "stop");  
			intent.putExtra("nowtime", Util.Int2Str(getCurrentPosition()));  
			intent.putExtra("totaltime", Util.Int2Str(getDuration()));
			//mNM.cancel(R.string.app_name);
			break;
		case PLAY_FINISH:
			intent.putExtra("state", "finish");  
			intent.putExtra("nowtime", "0");  
			intent.putExtra("totaltime", "0");
			//mNM.cancel(R.string.app_name);
			break;	
		case SEND_POSITION:
			intent.putExtra("state", "pos");  
			intent.putExtra("nowtime", Util.Int2Str(getCurrentPosition()));  
			intent.putExtra("totaltime", Util.Int2Str(getDuration()));

			intent.putExtra("playFileName", fileName);
			intent.putExtra("imagefilename", imagefilename);
			intent.putExtra("title", mp3title);
			intent.putExtra("maker", mp3maker);
			intent.putExtra("subtitle", mp3summery);
			intent.putExtra("position", position);
			intent.putExtra("datafilename", datafilename);
			intent.putExtra("datafilepath", datafilepath);

			//showNotification(getResources().getString(R.string.app_name)+" playing", mp3title);
			break;
		case ERROR:
			intent.putExtra("state", "err");  
			intent.putExtra("nowtime", "0");  
			intent.putExtra("totaltime", "0");
			break;	
		}
		
		//Log.d(TAG,"nowtime "+Util.Int2Str(getCurrentPosition())+" totaltime "+Util.Int2Str(getDuration()));
		sendBroadcast(intent); 
	}
	
    /*
	private NotificationManager mNM;
	void showNotification(String message, String title) {

		CharSequence text = message;
		Notification notification = new Notification(R.drawable.playnoti, "Podcast Play", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_ONGOING_EVENT;

		Intent intent = new Intent(this, NewPlayer.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("startType", "no_new");
		PendingIntent contentIntent = PendingIntent.getActivity(this.getBaseContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		notification.setLatestEventInfo(this, title, text, contentIntent);
		mNM.notify(R.string.app_name, notification);
	}
	*/
	
	private FileDescriptor getFileD(){
		Log.d(TAG,"getFileD fileName : "+Environment.getExternalStorageDirectory()+"/SimpleCast/"+fileName);
		
		FileInputStream fis = null; 
		try {
			fis = new FileInputStream(Environment.getExternalStorageDirectory()+"/SimpleCast/"+fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"FileNotFoundException");
			e.printStackTrace();
		} 
		FileDescriptor fd = null;
		try {
			fd = fis.getFD();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"IOException");
			e.printStackTrace();
		}
		return fd;
	}
	
	@Override
	public IBinder onBind(Intent arg) {
		// TODO Auto-generated method stub
		return new MyBinder(); // bind될 객체 리턴해야됨.
	}
	// Activity bind시 Activity에 전달시킬 객체
	public class MyBinder extends Binder {
		NewPlayerService getService() {
			return NewPlayerService.this;
		}
	}

}