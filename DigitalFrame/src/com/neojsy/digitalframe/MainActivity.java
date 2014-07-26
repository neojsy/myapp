package com.neojsy.digitalframe;

import java.io.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.*;
import android.view.*;
import android.widget.*;

public class MainActivity<SamBuJeActivity> extends Activity {

	String path1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onResume() {
		super.onResume();	
		setContentView(R.layout.main2);

		setPathView();
		
		((TextView) findViewById(R.id.head1)).setSelected(true);

	}



	public void setPathView() {
		TextView pathview1 = (TextView) findViewById(R.id.head1);

		UseDb db = new UseDb();
		path1 = db.getValue(this, "slot1path", "empty");
	
		Log.d(null, path1);

		if(path1.equals("empty"))
			pathview1.append(getResources().getString(R.string.main_info_setfolderurl));
		else
			pathview1.append(path1);
		
	}

	public String getSavedBaseFolderPath(String fileName) throws IOException {
		FileInputStream fis = openFileInput(fileName);
		byte[] fileData = new byte[fis.available()];
		while (fis.read(fileData) != -1) {
			;
		}
		fis.close();
		String savedPath = new String(fileData, "UTF-8");
		return savedPath;
	}

	public void mOnClick11(View v) {
		if(path1.equals("empty") == false) {
			Intent intent = new Intent(this, ImageViewer.class);
			intent.putExtra("type", "manual");
        	startActivity(intent);
		} else
			Toast.makeText(MainActivity.this,
					getResources().getString(R.string.main_info_setfolderurl),
					Toast.LENGTH_SHORT).show();
	}

	public void mOnClick12(View v) {
		UseDb db = new UseDb();
		db.setValue(this, "slot1scan", "yes");
		
		Intent intent = new Intent(this, FolderEx.class);
		intent.putExtra("path", path1);
		intent.putExtra("dbpath", "slot1path");
		
		startActivity(intent);
	}



	public void mOnClick13(View v) {
		Intent intent = new Intent(this, Setting.class);
		startActivity(intent);
	}

	public void mOnClick14(View v) {
		String dev = 
				getResources().getString(R.string.main_info_name) + " : "
				+ getResources().getString(R.string.main_info_maker) + "\n"
				+ getString(R.string.main_info_email) + " : "
				+ getResources().getString(R.string.main_info_email_url) + "\n"
				+ getResources().getString(R.string.main_info_blog) + " : "
				+ getResources().getString(R.string.main_info_blog_url) + "\n" + "\n"
				+ "\n" + getResources().getString(R.string.main_info_tk);

		new AlertDialog.Builder(MainActivity.this)
				.setTitle("Thank you!")
				.setMessage(dev)
				.setIcon(R.drawable.tk)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).show();
	}

}
