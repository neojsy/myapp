package com.neojsy.SimplePodcast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class SelectAddCastType extends Activity {
    public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.selectaddcasttype);
	}
    
	public void mOnClick10(View v) {
		Intent intent = new Intent(this, AddFamousCast.class);
//		intent.putExtra("path", path3);
//		intent.putExtra("dbpath", "slot3path");
		
		startActivity(intent);
		finish();
	}
	
	public void mOnClick11(View v) {
		Intent intent = new Intent(this, AddRadioCast.class);
//		intent.putExtra("path", path3);
//		intent.putExtra("dbpath", "slot3path");
		
		startActivity(intent);
		finish();
	}
	
	public void mOnClick20(View v) {
		Intent intent = new Intent(this, AddInputUrl.class);
//		intent.putExtra("path", path3);
//		intent.putExtra("dbpath", "slot3path");
		
		startActivity(intent);
		finish();
	}
}
