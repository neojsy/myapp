package com.tistory.neojsy;

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
				+"\n"
				
				+"\n"+
				"V 2.7 (2012.03.10)"+"\n"+
				getResources().getString(R.string.update0004)
				+"\n"
				
				+"\n"+
				"V 2.0 (2012.03.03)"+"\n"+
				getResources().getString(R.string.update0003)
				+"\n"

				+"\n"+
				"V 1.5 (2012.03.01)"+"\n"+
				getResources().getString(R.string.update0002)
				+"\n"

				+"\n"+
				"V 1.0 (2012.02.27)"+"\n"+
				getResources().getString(R.string.update0001)
				+"\n";
		
		devInfo.setText(di);
	}
}
