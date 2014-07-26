package com.neojsy.smartoilmanager;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class CarData implements Serializable {
	int distance;
	int literwon;
	GregorianCalendar time;

	CarData(int dis, int won, GregorianCalendar t){
		distance = dis;
		literwon = won;
		time = t;
	}
	
	public String getDateStr(boolean isDotAdd) {
		// String year = Util.Int2Str(time.get(time.YEAR));
		// String month = Util.Int2Str(time.get(time.MONTH) + 1);
		// String day = Util.Int2Str(time.get(time.DAY_OF_MONTH));

		String t;
		
		if(isDotAdd){
			t = Util.Int2Str(time.get(time.YEAR))
					+ "."
					+ String.format(String.format("%02d", time.get(time.MONTH) + 1))
					+ "." + String.format("%02d", time.get(time.DAY_OF_MONTH));
		}
		else{
			t = Util.Int2Str(time.get(time.YEAR))
					+ String.format(String.format("%02d", time.get(time.MONTH) + 1))
					+ String.format("%02d", time.get(time.DAY_OF_MONTH));
		}
		
		return t;
	}

	public String getTimeStr(boolean isDotAdd) {
		// String hour = Util.Int2Str(time.get(time.HOUR));
		// String min = Util.Int2Str(time.get(time.MINUTE));
		/*
		String t;
		if(isDotAdd){
			t = String.format("%02d", time.get(time.HOUR)) + ":" + String.format("%02d", time.get(time.MINUTE));
		}
		else{
			t = String.format("%02d", time.get(time.HOUR)) + String.format("%02d", time.get(time.MINUTE));
		}
		*/
		
		String hour24 = String.format("%02d", time.get(time.HOUR));
		String hour = String.format("%02d", time.get(time.HOUR));
		
		if(hour.equals("00")){
			hour = "12";
		}
		
		String apm;
		if(time.get(time.AM_PM) == time.AM){
			apm = "AM";
		}
		else{
			apm = "PM";
			int time24 = Util.Str2Int(hour24) + 12;
			hour24 = Util.Int2Str(time24);
		}

		String t;
		if(isDotAdd){
			t = hour + ":" + String.format("%02d", time.get(time.MINUTE)) + " " + apm;
		}
		else{
			t = hour24 + String.format("%02d", time.get(time.MINUTE));
		}
		
		
		return t;
	}
}
