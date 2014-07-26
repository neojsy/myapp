package com.neojsy.smartoilmanager;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SaveData extends Activity {
	static final String logTag = "OIL";
	String text;
	String won;
	String brand;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		text = intent.getStringExtra("SMS");
		won = intent.getStringExtra("WON");
		brand = intent.getStringExtra("BRAND");

		Log.d(logTag, "SaveData SMS:"+text+" BRAND:"+brand+" WON:"+won);

		//Log.d(logTag, "SaveData year:"+today.get(today.YEAR)+" month:"+today.get(today.MONTH)+" day:"+today.get(today.DAY_OF_MONTH));
		//Log.d(logTag, "SaveData hour:"+today.get(today.HOUR)+" min:"+today.get(today.MINUTE)+" sec:"+today.get(today.SECOND));
		
		saveDate();
		
		finish();
	}
	
	private void saveDate(){
		ArrayList<OilData> od = new ArrayList<OilData>();
		FileRW f = new FileRW();
		od = f.readFile_OilData();
		od.add(0, new OilData(text,won,brand, new GregorianCalendar()));
		f.writeFile_OilData(od);
	}
}
