package com.tistory.neojsy;


import java.io.*;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class FolderEx extends Activity {
	String getPath;
	String nowPath;
	String dbPath;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folderex);

        Intent gintent = getIntent();
        getPath = gintent.getStringExtra("path");
        dbPath = gintent.getStringExtra("dbpath");
        
        
        FileList _FileList = new FileList(this);
        _FileList.setOnPathChangedListener(_OnPathChanged);
        _FileList.setOnFileSelected(_OnFileSelected);
        
        LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout01);
        layout.addView(_FileList);
        
        if(getPath.equals("empty")){
        	_FileList.setPath("/");
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
        String inputPath = nowPath;
        UseDb db = new UseDb();
        
            File files = new File(inputPath);
            if(files.exists()==true){
            	db.setValue(this, dbPath, nowPath);
	        	
	        	Toast.makeText(FolderEx.this," "+ inputPath+getResources().getString(R.string.sp_msg_saved), Toast.LENGTH_SHORT).show();
	        	finish();
            }
            else{
            	Toast.makeText(FolderEx.this, getResources().getString(R.string.sp_msg_notexistfolder), Toast.LENGTH_SHORT).show();
            }

        
    }
   
	
}

