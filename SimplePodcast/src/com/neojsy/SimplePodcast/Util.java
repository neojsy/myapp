package com.neojsy.SimplePodcast;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class Util {
	static final String TAG = "SimplePodcast";
	
	public static int getLCDheight(Context con) {
		WindowManager wm = (WindowManager)con.getSystemService(Context.WINDOW_SERVICE);
		Display dsp = wm.getDefaultDisplay();

		return dsp.getHeight();
	}
	
	public static String getAppFolderPath(){
		return Environment.getExternalStorageDirectory() + "/SimpleCast/";
	}
	
	public static int getRandomNumber(int min, int max)
	{
	    return min + (int) (Math.random() * (max - min));
	}
	
	public static String msInt2TimeString(int src){
		String t = "";
		int sec;
		int min;
		int hour;
	
		src = src/1000;
		
		hour = src/3600;
		min = (src%3600)/60;
		sec = src - (hour*3600 + min*60);
				
		t = String.format( "%d:", hour )+String.format( "%02d:", min )+String.format( "%02d", sec );
		
		return t;
	}
	
	public static String Int2Str(int src){
		return Integer.toString(src);
	}
	
	public static int Str2Int(String src){
		return Integer.parseInt(src);
	}
	
	public static String Url2Mp3name(String path){
		String filename = path;
		//String dir = "";
		int n = filename.lastIndexOf("/");
		if (n >= 0) {
		    //dir = filename.substring(0, n);
		    filename = filename.substring(n + 1);
		    //if (n == 0)
		        //dir = "/";
		}
		//Log.d(TAG, "filename : "+filename);
		
		String[] time = path.split("/");
		for(int h=0;h<time.length;h++){
			//Log.d(TAG, "time "+h+" "+time[h]);
		}
		
		String finalname = time[time.length-2]+"_"+filename;
		
		//Log.d(TAG, "mp3 file name : "+finalname);
		
		return finalname;
	}
	
	
	public static String Month2Num(String word){
		String num = "00";
		
		if(word.equalsIgnoreCase("Jan") || word.equalsIgnoreCase("January")){
			num = "01";
		}else if(word.equalsIgnoreCase("Feb") || word.equalsIgnoreCase("Febuary")){
			num = "02";
		}else if(word.equalsIgnoreCase("Mar") || word.equalsIgnoreCase("March")){
			num = "03";
		}else if(word.equalsIgnoreCase("Apr") || word.equalsIgnoreCase("April")){
			num = "04";
		}else if(word.equalsIgnoreCase("May")){
			num = "05";
		}else if(word.equalsIgnoreCase("Jun") || word.equalsIgnoreCase("June")){
			num = "06";
		}else if(word.equalsIgnoreCase("Jul") || word.equalsIgnoreCase("July")){
			num = "07";
		}else if(word.equalsIgnoreCase("Aug") || word.equalsIgnoreCase("August")){
			num = "08";
		}else if(word.equalsIgnoreCase("Sep") || word.equalsIgnoreCase("September")){
			num = "09";
		}else if(word.equalsIgnoreCase("Oct") || word.equalsIgnoreCase("October")){
			num = "10";
		}else if(word.equalsIgnoreCase("Nov") || word.equalsIgnoreCase("November")){
			num = "11";
		}else if(word.equalsIgnoreCase("Dec") || word.equalsIgnoreCase("December")){
			num = "12";
		}
		
		return num;
	}
	

    public static boolean isConnected(Context con){
   	 ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiAvail = ni.isAvailable();
        boolean isWifiConn = ni.isConnected();
        ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileAvail = ni.isAvailable();
        boolean isMobileConn = ni.isConnected();
        
        //Log.d(TAG, "isWifiAvail : "+isWifiAvail+"| isWifiConn : "+isWifiConn+"| isMobileAvail : "+isMobileAvail+"| isMobileConn : "+isMobileConn);
        
        if (isWifiConn || isMobileConn) {
            return true;
        }else{
       	 	return false;
        }
   }
    
}
