package com.neojsy.SimplePodcast;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SetData {
	
	final static String EX_PLAYER = "explayer";
	final static String KOREA_USE = "korea";
	final static String DEL_END_FILE = "delendfile";
	final static String SEEK_TIME = "seektime";
	final static String EPISODE_INFO = "episodeinfo";
	final static String LIST_POSITION = "listpos";
	
	final static String ON = "on";
	final static String OFF = "off";
	
	Context mCon;
	
	SetData(Context con){
		mCon = con;
	}
	
	public boolean getExplayerPlaying(){
		String res = load(EX_PLAYER, OFF);
		if(res.equals(ON)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void setExplayerPlaying(boolean set){
		if(set){
			save(EX_PLAYER, ON);
		}
		else{
			save(EX_PLAYER, OFF);
		}
	}
	
	public boolean getEndFileDelete(){
		String res = load(DEL_END_FILE, OFF);
		if(res.equals(ON)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void setEndFileDelete(boolean set){
		if(set){
			save(DEL_END_FILE, ON);
		}
		else{
			save(DEL_END_FILE, OFF);
		}
	}	
	
	public boolean getUseKorea(){
		String res = load(KOREA_USE, ON);
		if(res.equals(ON)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void setUseKorea(boolean set){
		if(set){
			save(KOREA_USE, ON);
		}
		else{
			save(KOREA_USE, OFF);
		}
	}	
	
	public int getSeektime(){
		String res = load(SEEK_TIME, "3");
		return Util.Str2Int(res);
	}
	
	public void setSeektime(int set){
		save(SEEK_TIME, Util.Int2Str(set));
	}	
	
	public int getCastInfo(){
		String res = load(EPISODE_INFO, "0");
		return Util.Str2Int(res);
	}
	
	public void setCastInfo(int set){
		save(EPISODE_INFO, Util.Int2Str(set));
	}	
	
	public int getListPos(){
		String res = load(LIST_POSITION, "0");
		return Util.Str2Int(res);
	}
	
	public void setListPos(int set){
		save(LIST_POSITION, Util.Int2Str(set));
	}	
	
	private String load(String item, String defaultValue){
		SharedPreferences pref = mCon.getSharedPreferences("Simplepodcast", Activity.MODE_PRIVATE);
		String value = pref.getString(item, defaultValue);
		return value;
	}
	
	private void save(String item, String value){
    	SharedPreferences pref = mCon.getSharedPreferences("Simplepodcast", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor ed = pref.edit();
    	ed.putString(item, value);
		ed.commit();
	}
}
