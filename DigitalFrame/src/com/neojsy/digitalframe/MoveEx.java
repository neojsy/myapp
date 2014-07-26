package com.neojsy.digitalframe;

import java.io.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class MoveEx extends Activity {
	String getPath;
	String nowPath;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folderex);

        Intent gintent = getIntent();
        getPath = gintent.getStringExtra("path");
        nowPath = getPath;
        
        FileList _FileList = new FileList(this);
        _FileList.setOnPathChangedListener(_OnPathChanged);
        _FileList.setOnFileSelected(_OnFileSelected);
        
        LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout01);
        layout.addView(_FileList);
        
        if(getPath.equals("empty")){
        	_FileList.setPath("/mnt/sdcard/");
        }
        else{
        	_FileList.setPath(getPath);
        }        
        
        _FileList.setFocusable(true);
        _FileList.setFocusableInTouchMode(true);       
        
        ((TextView) findViewById(R.id.TextView01)).setSelected(true);
    }
    
    private OnPathChangedListener _OnPathChanged = new OnPathChangedListener() {
		public void onChanged(String path) {
			((TextView) findViewById(R.id.TextView01)).setText(path);
			//nowPath = path;
			nowPath = path.substring(0, path.length()-1);
		}
	};
    
    private OnFileSelectedListener _OnFileSelected = new OnFileSelectedListener() {
		public void onSelected(String path, String basePathFileName) {
			// TODO
		}
	};
    
    public void mOnClickSave(View v) {

	  saveListToFile(nowPath);
	  Toast.makeText(MoveEx.this," "+ nowPath+getResources().getString(R.string.sp_msg_saved), Toast.LENGTH_SHORT).show();
	  finish();

      }
    
    public boolean saveListToFile(String path){
    	SharedPreferences pref = MoveEx.this.getSharedPreferences("neojsy", Activity.MODE_PRIVATE);
		SharedPreferences.Editor ed = pref.edit();
		ed.putString("movepath", path);
		Log.d(null, path);
		ed.commit();
		return true;
    }
}

