package com.neojsy.SimplePodcast;

import java.io.IOException;
import java.util.Random;

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


public class Teatime2 extends Activity {
	MediaPlayer mediaPlayer;
	boolean isEnd = false;
	static final String TAG = "SimplePodcast";
	
    public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.teatime2);
    	
    	isEnd = false;
    	
    	play();
    	
    	mCompleteHandler.sendEmptyMessage(0);
	}
    
    public void onResume() {
		super.onResume();
    }
    
    protected void onStop(){
    	super.onPause();
    	isEnd = true;
    	exit();
    }
    
    public Handler mCompleteHandler = new Handler(){
    	public void handleMessage(Message msg){
    		if(!isEnd){
    			draw();
    			mCompleteHandler.sendEmptyMessageDelayed(0, 1000);
    		}
    	}
    };
    
    int i = 0;
    int res = 0;
    int [] map = new int [16];
    
    private void draw(){
    	int time = (i%27);
    	Log.d("Tea", "time "+time);
    	switch(time){
    	case 0:	res = R.drawable.gk1; break;
    	case 1:	res = R.drawable.gk2; break;
    	case 2: res = R.drawable.gk3; break;
    	case 3:	res = R.drawable.gk4; break;
    	case 4: res = R.drawable.gk5; break;
    	case 5: res = R.drawable.gk6; break;
    	case 6: res = R.drawable.gk7; break;
    	case 7: res = R.drawable.gk8; break;
    	case 8: res = R.drawable.gk9; break;
    	case 9: res = R.drawable.gk10; break;
    	case 10: res = R.drawable.gk0; 	break;
    	case 11: res = R.drawable.gk11; break;
    	case 12: res = R.drawable.gk12; break;
    	case 13: res = R.drawable.gk13; break;
    	case 14: res = R.drawable.gk14; break;
    	case 15: res = R.drawable.gk15; break;
    	case 16: res = R.drawable.gk16; break;
    	case 17: res = R.drawable.gk17; break;
    	case 18: res = R.drawable.gk18; break;
    	case 19: res = R.drawable.gk19; break;
    	case 20: res = R.drawable.gk20; break;
    	case 21: res = R.drawable.gk21; break;
    	case 22: res = R.drawable.gk22; break;
    	case 23: res = R.drawable.gk23; break;
    	case 24: res = R.drawable.gk24; break;
    	case 25: res = R.drawable.gk25; break;
    	case 26: res = R.drawable.gk26; break;
    	
    	}
    	
    	
    	if(i%16 == 0){
    		for(int i=0;i<16;i++){
    			map[i] = 0;
    		}
    	}
    	
    	int viewn;
    	
    	Random oRandom = new Random();
    	do{
    		viewn = oRandom.nextInt(16);
    	}while(map[viewn] != 0);
    	map[viewn] = 1;
    	
    	ImageView iv;
    	
    	switch(viewn){
    	case 0:
    		iv = (ImageView) findViewById(R.id.A1);
    		break;
    	case 1:
    		iv = (ImageView) findViewById(R.id.A2);
    		break;
    	case 2:
    		iv = (ImageView) findViewById(R.id.A3);
    		break;
    	case 3:
    		iv = (ImageView) findViewById(R.id.A4);
    		break;
    	case 4:
    		iv = (ImageView) findViewById(R.id.B1);
    		break;
    	case 5:
    		iv = (ImageView) findViewById(R.id.B2);
    		break;
    	case 6:
    		iv = (ImageView) findViewById(R.id.B3);
    		break;
    	case 7:
    		iv = (ImageView) findViewById(R.id.B4);	
    		break;
    	case 8:
    		iv = (ImageView) findViewById(R.id.C1);
    		break;
    	case 9:
    		iv = (ImageView) findViewById(R.id.C2);
    		break;
    	case 10:
    		iv = (ImageView) findViewById(R.id.C3);
    		break;
    	case 11:
    		iv = (ImageView) findViewById(R.id.C4);	
    		break;	
    	case 12:
    		iv = (ImageView) findViewById(R.id.D1);
    		break;
    	case 13:
    		iv = (ImageView) findViewById(R.id.D2);
    		break;
    	case 14:
    		iv = (ImageView) findViewById(R.id.D3);
    		break;
    	case 15:
    	default:
    		iv = (ImageView) findViewById(R.id.D4);	
    		break;		
    	}
    	
    	iv.setImageResource(res);
    	
    	i++;
    }
    int startnum;
    private void play(){
		SharedPreferences pref = Teatime2.this.getSharedPreferences("Simplepodcast", Activity.MODE_PRIVATE);
		String value = pref.getString("teatime", "0");
    	
		int inn = Util.Str2Int(value);
		int song = inn%3;
		startnum = inn;
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
    
	public void exit() {
		if(mediaPlayer!=null){
			try{
				//MediaPlayer 占쎈Ŋ�앾옙�곸젫
				mediaPlayer.release();
			}catch(Exception e){
				e.printStackTrace();
			}
			mediaPlayer = new MediaPlayer();
			
			int time = (startnum%2);
			Log.d(TAG, "i="+i+" time="+time);
	    	switch(time){
	    		case 0:	
	    			mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.ok); 
	    			break;
	    		case 1:	
	    			mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.ok2); 
	    			break;
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
