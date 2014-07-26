package com.neojsy.digitalframe;

import android.app.*;
import android.content.*;
import android.os.*;

public class CheckAutoStartOption extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SetData sData = new SetData(CheckAutoStartOption.this);
		if(sData.getChargingStart()){
			Intent intent = new Intent(this, ImageViewer.class);
			intent.putExtra("type", "auto");
        	startActivity(intent);
		}
		else{
			
		}
		finish();
	}
}
