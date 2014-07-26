package com.neojsy.digitalframe;

import android.app.*;
import android.os.*;
import android.widget.*;

public class DevInfo extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.devinfo);
		addDevInfo();
	}
	
	public void addDevInfo(){
		TextView devInfo = (TextView) findViewById(R.id.devinfo);
		String di;
		di = 
				"<< Version history >>" 
				+"\n";
				
		
		devInfo.setText(di);
	}
}
