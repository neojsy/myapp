package com.neojsy.SimplePodcast;

import java.io.IOException;
import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;


public class Teatime extends Activity {
	MediaPlayer mediaPlayer;
	
    public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.teatime);
    	
    	play();
    	
    	mCompleteHandler.sendEmptyMessage(0);
	}
    
    public void onResume() {
		super.onResume();
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    
    	return true;
    }
    
    public Handler mCompleteHandler = new Handler(){
    	public void handleMessage(Message msg){
    		draw();
    		mCompleteHandler.sendEmptyMessageDelayed(0, 1000);
    	}
    };
    
    int i = 0;
    int res = 0;
    
    private void draw(){
    	int time = (i%11);
    	
    	switch(time){
    	case 0:
    		res = R.drawable.gk1;
    		break;
    	case 1:
    		res = R.drawable.gk2;
    		break;
    	case 2:
    		res = R.drawable.gk3;
    		break;
    	case 3:
    		res = R.drawable.gk4;
    		break;
    	case 4:
    		res = R.drawable.gk5;
    		break;
    	case 5:
    		res = R.drawable.gk6;
    		break;
    	case 6:
    		res = R.drawable.gk7;
    		break;
    	case 7:
    		res = R.drawable.gk8;
    		break;
    	case 8:
    		res = R.drawable.gk9;
    		break;
    	case 9:
    		res = R.drawable.gk10;
    		break;
    	case 10:
    		res = R.drawable.gk0;
    		break;
    	
    	}
    	
    	int viewn = (i%4);
    	ImageView iv;
    	iv = (ImageView) findViewById(R.id.gaka1);
    	
    	switch(viewn){
    	case 0:
    		iv = (ImageView) findViewById(R.id.gaka1);
    		break;
    	case 1:
    		iv = (ImageView) findViewById(R.id.gaka2);
    		break;
    	case 2:
    		iv = (ImageView) findViewById(R.id.gaka3);
    		break;
    	case 3:
    		iv = (ImageView) findViewById(R.id.gaka4);
    		break;
    	}
    	
    	iv.setImageResource(res);
    	
    	i++;
    }
    
    private void play(){
		SharedPreferences pref = Teatime.this.getSharedPreferences("Simplepodcast", Activity.MODE_PRIVATE);
		String value = pref.getString("teatime", "0");
    	
		int inn = Util.Str2Int(value);
		int song = inn%3;
		
		inn++;
    	SharedPreferences.Editor ed = pref.edit();
    	ed.putString("teatime", Util.Int2Str(inn));
		ed.commit();
    	
    	mediaPlayer = new MediaPlayer(); 
    	
    	switch(song){
    		case 0:
    			mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.n01);
    			break;
    		case 1:
    			mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.n03);
        		break;
    		case 2:
    			mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.n04);
        		break;
/*
    		case 5:
    			mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.n06);
        		break;
    		case 6:
    			mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.n07);
        		break;*/
    	}
    	
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		mediaPlayer.start();
    }
    
	public void mOnClickGk(View v) {
		if(mediaPlayer!=null){
			try{
				//MediaPlayer �먯썝�댁젣
				mediaPlayer.release();
			}catch(Exception e){
				e.printStackTrace();
			}
			mediaPlayer = new MediaPlayer(); 
			mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.ok);
			try {
				mediaPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mediaPlayer.start();
		}
		finish();
	}
}
