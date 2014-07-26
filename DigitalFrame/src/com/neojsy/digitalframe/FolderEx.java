package com.neojsy.digitalframe;


import java.io.*;
import android.app.Activity;
import android.content.*;
import android.os.*;
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
        
        Util.print("getPath = " + getPath);
        String path = " ";
        
        if(getPath.equals("empty")){
        	File root = Environment.getExternalStorageDirectory();
        	_FileList.setPath(root.getAbsolutePath());
        	path = root.getAbsolutePath();
        }
        else{
        	_FileList.setPath(getPath);
        	path = getPath;
        }        
        
        Util.print("path = " + path);
        
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
        	
        	Intent intent = new Intent(this, Loading.class);
        	intent.putExtra("path", nowPath);
        	startActivity(intent);
        	
        	finish();
        }
        else{
        	Toast.makeText(FolderEx.this, getResources().getString(R.string.sp_msg_notexistfolder), Toast.LENGTH_SHORT).show();
        }

    }
   
	
}

