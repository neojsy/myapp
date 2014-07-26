package com.neojsy.digitalframe;

import java.util.*;

import android.util.*;

public class Util {
	public static String getFolderlistFileName(){
		return "folderlist1.txt";
	}
	
	public static String getFileListFileName(){
		return "filelist1.txt";
	}
	
	public static String getUserFolderFileName(){
		return "baseFolderPath1.txt";
	}
	
	
	public static String Int2Str(int src){
		return Integer.toString(src);
	}
	
	public static int Str2Int(String src){
		return Integer.parseInt(src);
	}
	
	static final String logTag = "DigitalFrame";
	
	public static void print(String str){
		Log.d(logTag, str);
	}
	
	public static String getDateStr() {
		// String year = Util.Int2Str(time.get(time.YEAR));
		// String month = Util.Int2Str(time.get(time.MONTH) + 1);
		// String day = Util.Int2Str(time.get(time.DAY_OF_MONTH));

		GregorianCalendar time = new GregorianCalendar();
		
		String t;
		
		int day = time.get(time.DAY_OF_WEEK) ;
		String conversionDay = null ;   
		switch (day) {        
        case 1 :
            conversionDay = "Sun";
            break ;

        case 2 :
            conversionDay = "Mon" ;
            break ;

        case 3 :
            conversionDay = "Tue" ;
            break ;

        case 4 :
            conversionDay = "Wed" ;
            break ;
    
        case 5 :
            conversionDay = "Thu" ;            
            break ;

        case 6 :
            conversionDay = "Fri" ;
            break ;

        case 7 :
            conversionDay = "Sat" ;        
    }
		
				
				
		t = Util.Int2Str(time.get(time.YEAR))
				+ "."
				+ String.format(String.format("%02d", time.get(time.MONTH) + 1))
				+ "." + String.format("%02d", time.get(time.DAY_OF_MONTH))
				+ " " + conversionDay;
		
		
		return t;
	}

	public static String getTimeStr() {
		// String hour = Util.Int2Str(time.get(time.HOUR));
		// String min = Util.Int2Str(time.get(time.MINUTE));
		
		GregorianCalendar time = new GregorianCalendar();
		
		String t;
		
		String hour = String.format("%02d", time.get(time.HOUR));
		if(hour.equals("00"))
			hour = "12";
		
		String apm;
		if(time.get(time.AM_PM) == time.AM)
			apm = "AM";
		else
			apm = "PM";

		t = hour + ":" + String.format("%02d", time.get(time.MINUTE));
		
		
		
		return t;
	}
	
	public static String getAmPm(){
		// String hour = Util.Int2Str(time.get(time.HOUR));
		// String min = Util.Int2Str(time.get(time.MINUTE));
		
		GregorianCalendar time = new GregorianCalendar();
		
		String t;
		
		String hour = String.format("%02d", time.get(time.HOUR));
		if(hour.equals("00"))
			hour = "12";
		
		String apm;
		if(time.get(time.AM_PM) == time.AM)
			apm = "AM";
		else
			apm = "PM";

		t = apm;
		
		
		
		return t;
	}
}
