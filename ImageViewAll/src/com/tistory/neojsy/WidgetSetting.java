package com.tistory.neojsy;

import java.io.*;
import java.util.*;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class WidgetSetting extends Activity {
	String getPath;
	String nowPath;
	String basePathFileName;
	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	ArrayList<String> mDirList = new ArrayList<String>();
	ArrayList<String> mFileList = new ArrayList<String>();
	int fileNum;
	String callWidgetName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.folderex);

		getPath = "/mnt/sdcard";
		basePathFileName = "ws.txt";

		nowPath = getPath;
		FileList _FileList = new FileList(this);
		_FileList.setOnPathChangedListener(_OnPathChanged);
		LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout01);
		layout.addView(_FileList);
		if (getPath.equals("empty")) {
			_FileList.setPath("/mnt/sdcard/");
		} else {
			_FileList.setPath(getPath);
		}
		_FileList.setFocusable(true);
		_FileList.setFocusableInTouchMode(true);
		((TextView) findViewById(R.id.TextView01)).setSelected(true);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			callWidgetName = manager.getAppWidgetInfo(mAppWidgetId).provider.getClassName();
			Log.d(null, callWidgetName);
		}

	}

	private OnPathChangedListener _OnPathChanged = new OnPathChangedListener() {
		public void onChanged(String path) {
			((TextView) findViewById(R.id.TextView01)).setText(path);
			// nowPath = path;
			nowPath = path.substring(0, path.length() - 1);
		}
	};
	
	ProgressDialog progress;
	public void mOnClickSave(View v) {
		progress = ProgressDialog.show(WidgetSetting.this, getResources().getString(R.string.wg_loadingt), getResources().getString(R.string.wg_loadingm), true);

		startFolderScanThread();
	}

    public void startFolderScanThread(){
    	Thread backGround = new Thread() {
    		public void run() {
    			scanfile();
    			mCompleteHandler.sendEmptyMessage(0);
    		}
    	};	
    	backGround.start();
    }
    
    public Handler mCompleteHandler = new Handler(){
    	public void handleMessage(Message msg){
    		exitToWidget();
    		progress.dismiss();
    	}
    };
	
    private void scanfile(){

		addFolderList(nowPath);
		addFileList();

		if(callWidgetName.equals("com.tistory.neojsy.WidgetVier")){
			saveFolderListToFile("wfoler.txt");
			saveFileListToFile("wfile.txt");
			

		}
		else if(callWidgetName.equals("com.tistory.neojsy.WidgetVier2x")){
			saveFolderListToFile("wfoler2x.txt");
			saveFileListToFile("wfile2x.txt");
			

		}
		else if(callWidgetName.equals("com.tistory.neojsy.WidgetVier4x")){
			saveFolderListToFile("wfoler4x.txt");
			saveFileListToFile("wfile4x.txt");
			
		}    	
    }
	
	private void exitToWidget() {
		final Context context = WidgetSetting.this;
		AppWidgetManager widgetMgr = AppWidgetManager.getInstance(context);

		if(callWidgetName.equals("com.tistory.neojsy.WidgetVier")){

			WidgetVier.setList(context, "wfoler.txt", "wfile.txt", fileNum);
			WidgetVier.updateWidget(context, widgetMgr, mAppWidgetId);
		}
		else if(callWidgetName.equals("com.tistory.neojsy.WidgetVier2x")){

			WidgetVier2x.setList(context, "wfoler2x.txt", "wfile2x.txt", fileNum);
			WidgetVier2x.updateWidget(context, widgetMgr, mAppWidgetId);
		}
		else if(callWidgetName.equals("com.tistory.neojsy.WidgetVier4x")){
	
			WidgetVier4x.setList(context, "wfoler4x.txt", "wfile4x.txt", fileNum);
			WidgetVier4x.updateWidget(context, widgetMgr, mAppWidgetId);
		}
		
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		finish();
	}

	public boolean saveFolderListToFile(String fn) {
		try {
			FileOutputStream fos = openFileOutput(fn,
					Context.MODE_WORLD_READABLE);
			fos.write(mDirList.toString().getBytes());
			fos.close();
		} catch (Exception e) {
		}
		return true;
	}

	public boolean saveFileListToFile(String fn) {
		try {
			FileOutputStream fos = openFileOutput(fn,
					Context.MODE_WORLD_READABLE);
			fos.write(mFileList.toString().getBytes());
			fos.close();
		} catch (Exception e) {
		}
		return true;
	}

	public void addFileList() {
		int fileNumber = 0;
		for (int i = 0; i < mDirList.size(); i++) {
			File fp = new File(mDirList.get(i));
			File[] file_list = fp.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String lowName = name.toLowerCase();
					if (lowName.endsWith(".jpg"))
						return true;
					else if (lowName.endsWith(".gif"))
						return true;
					else if (lowName.endsWith(".jpeg"))
						return true;
					else if (lowName.endsWith(".png"))
						return true;
					else if (lowName.endsWith(".bmp"))
						return true;
					else
						return false;
				}
			});

			if (file_list.length != 0) {
				for (int k = 0; k < file_list.length; k++) {
					mFileList.add(fileNumber,
							i + ":::" + file_list[k].getName());
					// scannigFolder =
					// "File searching...\n"+file_list[k].getName();
					fileNumber++;
				}
			}
		}

		Log.d(null, "detected fileNumber:" + fileNumber);
		fileNum = fileNumber;
	}

	public void addFolderList(String folderPath) {

		File fp = new File(folderPath);
		File[] file_list = fp.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.equalsIgnoreCase(".android_secure"))
					return false;
				else if (name.equalsIgnoreCase(".thumbnails"))
					return false;
				else if (name.equalsIgnoreCase("LOST.DIR"))
					return false;
				else if (name.equalsIgnoreCase(".Android"))
					return false;
				else if (name.equalsIgnoreCase("Android"))
					return false;
				else if (name.equalsIgnoreCase("ARM"))
					return false;
				else if (name.equalsIgnoreCase(".systemlib"))
					return false;
				else if (name.equalsIgnoreCase("data"))
					return false;
				else if (name.startsWith("."))
					return false;
				else
					return true;
			}
		});

		for (File c : file_list) {
			if (c.isDirectory()) {
				mDirList.add(folderPath + "/" + c.getName());
			}
		}

		int i = 0;

		for (; i < mDirList.size(); i++) {
			File fp2 = new File(mDirList.get(i));
			File[] file_list2 = fp2.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.equalsIgnoreCase(".android_secure"))
						return false;
					else if (name.equalsIgnoreCase(".thumbnails"))
						return false;
					else if (name.equalsIgnoreCase("LOST.DIR"))
						return false;
					else
						return true;
				}
			});

			for (File c : file_list2) {
				if (c.isDirectory()) {
					mDirList.add(mDirList.get(i) + "/" + c.getName());
					// scannigFolder =
					// "Folder searching...\n"+mDirList.get(i)+"/"+c.getName();
					// folderNum++;
				}
			}
		}

		mDirList.add(folderPath);

	}

}
