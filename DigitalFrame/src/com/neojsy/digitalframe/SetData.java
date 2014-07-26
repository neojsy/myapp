package com.neojsy.digitalframe;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SetData {
	
	final static String ON = "on";
	final static String OFF = "off";
	
	Context mCon;
	
	SetData(Context con){
		mCon = con;
	}
	
	public boolean getClock(){
		String res = load("clock", OFF);
		if(res.equals(ON)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void setClock(boolean set){
		if(set){
			save("clock", ON);
		}
		else{
			save("clock", OFF);
		}
	}
	
	public boolean getChargingStart(){
		String res = load("chargingstart", ON);
		if(res.equals(ON)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void setChargingStart(boolean set){
		if(set){
			save("chargingstart", ON);
		}
		else{
			save("chargingstart", OFF);
		}
	}
	
	public boolean getScreenOff(){
		String res = load("screenoff", ON);
		if(res.equals(ON)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void setScreenOff(boolean set){
		if(set){
			save("screenoff", ON);
		}
		else{
			save("screenoff", OFF);
		}
	}	
	
	public int getCColor(){
		String res = load("clockcolor", "4");
		return Util.Str2Int(res);
	}
	
	public void setCColor(int set){
		save("clockcolor", Util.Int2Str(set));
	}	
	
	public int getCPos(){
		String res = load("clockpos", "0");
		return Util.Str2Int(res);
	}
	
	public void setCPos(int set){
		save("clockpos", Util.Int2Str(set));
	}	
	
	public int getCSize(){
		String res = load("clocksize", "3");
		return Util.Str2Int(res);
	}
	
	public void setCSize(int set){
		save("clocksize", Util.Int2Str(set));
	}	
	
	public int getInterval(){
		String res = load("interval", "2");
		return Util.Str2Int(res);
	}
	
	public void setInterval(int set){
		save("interval", Util.Int2Str(set));
	}	
	
	public int getScreenSize(){
		String res = load("screensize", "0");
		return Util.Str2Int(res);
	}
	
	public void setScreenSize(int set){
		save("screensize", Util.Int2Str(set));
	}	
	
	
	private String load(String item, String defaultValue){
		SharedPreferences pref = mCon.getSharedPreferences("digitalframe", Activity.MODE_PRIVATE);
		String value = pref.getString(item, defaultValue);
		return value;
	}
	
	private void save(String item, String value){
    	SharedPreferences pref = mCon.getSharedPreferences("digitalframe", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor ed = pref.edit();
    	ed.putString(item, value);
		ed.commit();
	}
}
