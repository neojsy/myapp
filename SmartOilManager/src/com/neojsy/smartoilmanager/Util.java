package com.neojsy.smartoilmanager;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.util.Log;

public class Util {
	static final String logTag = "OIL";
	
	public static void print(String str){
		Log.d(logTag, str);
	}
	
	public static String Int2Str(int src){
		return Integer.toString(src);
	}
	
	public static String Long2Str(long src){
		return Long.toString(src);
	}
	
	public static int Str2Int(String src){
		return Integer.parseInt(src);
	}
	
	public static ArrayList<OilData> getPeriodOilData(int sYear, int sMonth, int sDay, int eYear, int eMonth, int eDay){
		ArrayList<OilData> originOd = new ArrayList<OilData>();
		ArrayList<OilData> newOd = new ArrayList<OilData>();
		Log.d(logTag, "getPeriodOilData");
		
		FileRW f = new FileRW();
		originOd = f.readFile_OilData();
		
		for(int i=0;i<originOd.size();i++){
			//GregorianCalendar nTime = originOd.get(i).time;
			///int year = nTime.get(nTime.YEAR);
			//int month = nTime.get(nTime.MONTH);
			//int day = nTime.get(nTime.DAY_OF_MONTH);
			
			//Log.d(logTag, "Start "+i+" : "+"year "+sYear+" month "+sMonth+"day"+sDay);
			//Log.d(logTag, "NowIs "+i+" : "+"year "+year+" month "+month+"day"+day);
			//Log.d(logTag, "EndIs "+i+" : "+"year "+eYear+" month "+eMonth+"day"+eDay);
			
			String sTs = Util.Int2Str(sYear)
					+ String.format("%02d", sMonth)
					+ String.format("%02d", sDay);

			String eTs = Util.Int2Str(eYear)
					+ String.format("%02d", eMonth)
					+ String.format("%02d", eDay);

			String nTs = originOd.get(i).getDateStr(false);
			
			//Log.d(logTag, "sTs "+sTs+" nTs "+nTs+"eTs"+eTs);
			
			int sTd = Util.Str2Int(sTs);
			int nTd = Util.Str2Int(nTs);
			int eTd = Util.Str2Int(eTs);
			
			if(sTd <= nTd && nTd <= eTd){
				//Log.d(logTag, "This data is OK");
				newOd.add(originOd.get(i));
			}
			
		}
		
		return newOd;
	}
}
