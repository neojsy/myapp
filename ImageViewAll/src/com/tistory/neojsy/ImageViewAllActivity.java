package com.tistory.neojsy;

import java.io.*;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.*;
import android.view.*;
import android.widget.*;

public class ImageViewAllActivity<SamBuJeActivity> extends Activity {
	String folderListfile1 = "/data/data/com.tistory.neojsy/files/folderlist1.txt";
	String folderListfile2 = "/data/data/com.tistory.neojsy/files/folderlist2.txt";
	String folderListfile3 = "/data/data/com.tistory.neojsy/files/folderlist3.txt";
	String folderListfile1Name = "folderlist1.txt";
	String folderListfile2Name = "folderlist2.txt";
	String folderListfile3Name = "folderlist3.txt";
	String fileListfile1Name = "filelist1.txt";
	String fileListfile2Name = "filelist2.txt";
	String fileListfile3Name = "filelist3.txt";
	File basePathFile1 = new File(
			"/data/data/com.tistory.neojsy/files/baseFolderPath1.txt");
	String basePathFile1Name = "baseFolderPath1.txt";
	File basePathFile2 = new File(
			"/data/data/com.tistory.neojsy/files/baseFolderPath2.txt");
	String basePathFile2Name = "baseFolderPath2.txt";
	File basePathFile3 = new File(
			"/data/data/com.tistory.neojsy/files/baseFolderPath3.txt");
	String basePathFile3Name = "baseFolderPath3.txt";
	String path1;
	String path2;
	String path3;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onResume() {
		super.onResume();	
		setContentView(R.layout.main);
		try {
			showUpdate(this);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setPathView();
		
		((TextView) findViewById(R.id.head1)).setSelected(true);
		((TextView) findViewById(R.id.head2)).setSelected(true);
		((TextView) findViewById(R.id.head3)).setSelected(true);
	}

	public void showUpdate(Context context) throws NameNotFoundException {
		SharedPreferences pref = context.getSharedPreferences("VER", 0);

			PackageManager pm = context.getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			int VERSION = packageInfo.versionCode;
			int old_Ver = pref.getInt("version", 0);

			if (old_Ver < VERSION) 
			{
				UseDb db = new UseDb();
				db.setValue(this, "slot1scan", "yes");
				db.setValue(this, "slot2scan", "yes");
				db.setValue(this, "slot3scan", "yes");
				
				new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.main_update))
				.setMessage(getResources().getString(R.string.update0009))
				.setPositiveButton("OK", null).show();
				
				SharedPreferences.Editor edit = pref.edit();
				edit.putInt("version", VERSION);
				edit.commit();

			}

	}

	public void setPathView() {
		TextView pathview1 = (TextView) findViewById(R.id.head1);
		TextView pathview2 = (TextView) findViewById(R.id.head2);
		TextView pathview3 = (TextView) findViewById(R.id.head3);

		UseDb db = new UseDb();
		path1 = db.getValue(this, "slot1path", "empty");
		path2 = db.getValue(this, "slot2path", "empty");
		path3 = db.getValue(this, "slot3path", "empty");
		
		Log.d(null, path1);
		Log.d(null, path2);
		Log.d(null, path3);
		
		if(path1.equals("empty"))
			pathview1.append(getResources().getString(R.string.main_info_setfolderurl));
		else
			pathview1.append(path1);
		
		if(path2.equals("empty"))
			pathview2.append(getResources().getString(R.string.main_info_setfolderurl));
		else
			pathview2.append(path2);
		
		if(path3.equals("empty"))
			pathview3.append(getResources().getString(R.string.main_info_setfolderurl));
		else
			pathview3.append(path3);
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
			Intent intent = new Intent(this, CheckFolderList.class);
			intent.putExtra("path", path1);
			intent.putExtra("resync", "no");
			intent.putExtra("dbscan", "slot1scan");
			intent.putExtra("folderListFileName", folderListfile1Name);
			intent.putExtra("fileListFileName", fileListfile1Name);
			startActivity(intent);
		} else
			Toast.makeText(ImageViewAllActivity.this,
					getResources().getString(R.string.main_info_setfolderurl),
					Toast.LENGTH_SHORT).show();
	}

	public void mOnClick12(View v) {
		if(path1.equals("empty") == false) {
			Intent intent = new Intent(this, CheckFolderList.class);
			intent.putExtra("path", path1);
			intent.putExtra("resync", "yes");
			intent.putExtra("dbscan", "slot1scan");
			intent.putExtra("folderListFileName", folderListfile1Name);
			intent.putExtra("fileListFileName", fileListfile1Name);
			startActivity(intent);
		} else
			Toast.makeText(ImageViewAllActivity.this,
					getResources().getString(R.string.main_info_setfolderurl),
					Toast.LENGTH_SHORT).show();
	}

	public void mOnClick13(View v) {
		UseDb db = new UseDb();
		db.setValue(this, "slot1scan", "yes");
		
		Intent intent = new Intent(this, FolderEx.class);
		intent.putExtra("path", path1);
		intent.putExtra("dbpath", "slot1path");
		
		startActivity(intent);
	}

	public void mOnClick21(View v) {
		if(path2.equals("empty") == false) {
			Intent intent = new Intent(this, CheckFolderList.class);
			intent.putExtra("path", path2);
			intent.putExtra("resync", "no");
			intent.putExtra("dbscan", "slot2scan");
			intent.putExtra("folderListFileName", folderListfile2Name);
			intent.putExtra("fileListFileName", fileListfile2Name);
			startActivity(intent);
		} else
			Toast.makeText(ImageViewAllActivity.this,
					getResources().getString(R.string.main_info_setfolderurl),
					Toast.LENGTH_SHORT).show();
	}

	public void mOnClick22(View v) {
		if(path2.equals("empty") == false) {
			Intent intent = new Intent(this, CheckFolderList.class);
			intent.putExtra("path", path2);
			intent.putExtra("resync", "yes");
			intent.putExtra("dbscan", "slot2scan");
			intent.putExtra("folderListFileName", folderListfile2Name);
			intent.putExtra("fileListFileName", fileListfile2Name);
			startActivity(intent);
		} else
			Toast.makeText(ImageViewAllActivity.this,
					getResources().getString(R.string.main_info_setfolderurl),
					Toast.LENGTH_SHORT).show();
	}

	public void mOnClick23(View v) {
		UseDb db = new UseDb();
		db.setValue(this, "slot2scan", "yes");
		
		Intent intent = new Intent(this, FolderEx.class);
		intent.putExtra("path", path2);
		intent.putExtra("dbpath", "slot2path");
		
		startActivity(intent);
	}

	public void mOnClick31(View v) {
		if(path3.equals("empty") == false) {
			Intent intent = new Intent(this, CheckFolderList.class);
			intent.putExtra("path", path3);
			intent.putExtra("resync", "no");
			intent.putExtra("dbscan", "slot3scan");
			intent.putExtra("folderListFileName", folderListfile3Name);
			intent.putExtra("fileListFileName", fileListfile3Name);
			startActivity(intent);
		} else
			Toast.makeText(ImageViewAllActivity.this,
					getResources().getString(R.string.main_info_setfolderurl),
					Toast.LENGTH_SHORT).show();
	}

	public void mOnClick32(View v) {
		if(path3.equals("empty") == false) {
			Intent intent = new Intent(this, CheckFolderList.class);
			intent.putExtra("path", path3);
			intent.putExtra("resync", "yes");
			intent.putExtra("dbscan", "slot3scan");
			intent.putExtra("folderListFileName", folderListfile3Name);
			intent.putExtra("fileListFileName", fileListfile3Name);
			startActivity(intent);
		} else
			Toast.makeText(ImageViewAllActivity.this,
					getResources().getString(R.string.main_info_setfolderurl),
					Toast.LENGTH_SHORT).show();
	}

	public void mOnClick33(View v) {
		UseDb db = new UseDb();
		db.setValue(this, "slot3scan", "yes");
		
		Intent intent = new Intent(this, FolderEx.class);
		intent.putExtra("path", path3);
		intent.putExtra("dbpath", "slot3path");
		
		startActivity(intent);
	}

	public void mOnClickSetting(View v) {
		Intent intent = new Intent(this, Setting.class);
		startActivity(intent);
	}

	public void mOnClickDev(View v) {
		String dev = 
				getResources().getString(R.string.main_info_name) + " : "
				+ getResources().getString(R.string.main_info_maker) + "\n"
				+ getString(R.string.main_info_email) + " : "
				+ getResources().getString(R.string.main_info_email_url) + "\n"
				+ getResources().getString(R.string.main_info_blog) + " : "
				+ getResources().getString(R.string.main_info_blog_url) + "\n" + "\n"
				+ getResources().getString(R.string.main_info_des)
				+ "\n\n" + getResources().getString(R.string.main_info_tk);

		new AlertDialog.Builder(ImageViewAllActivity.this)
				.setTitle(" Thank you !")
				.setMessage(dev)
				.setIcon(R.drawable.tk)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).show();
	}

	public void mOnClickInfo(View v) {
		ScrollView scv = new ScrollView(this);
		scv.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		scv.setMinimumHeight(100);
		LinearLayout parentLinear = new LinearLayout(this);
		parentLinear.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		parentLinear.setOrientation(LinearLayout.VERTICAL);
		scv.addView(parentLinear);

		LinearLayout linear;
		TextView tv;

		linear = new LinearLayout(this);
		linear.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		linear.setOrientation(LinearLayout.HORIZONTAL);
		parentLinear.addView(linear);
		// property name
		tv = new TextView(this);
		tv.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		// tv.setWidth(80);
		tv.setPadding(5, 5, 5, 5);
		// tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		tv.setText(
				getResources().getString(R.string.gag) +"\n\n"
				+ getResources().getString(R.string.update0009)
				+ getResources().getString(R.string.update0008)
				+ getResources().getString(R.string.update0007)
				+ getResources().getString(R.string.update0006)
				+ getResources().getString(R.string.update0005)
				+ getResources().getString(R.string.update0004)
				+ getResources().getString(R.string.update0003)
				+ getResources().getString(R.string.update0002)
				+ getResources().getString(R.string.update0001));
		linear.addView(tv);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.main_update));
		builder.setIcon(android.R.drawable.ic_menu_info_details);
		builder.setView(scv);
		builder.setPositiveButton("OK", null);
		builder.show();
	}
}
