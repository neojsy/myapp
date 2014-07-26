package com.tistory.neojsy;

import java.io.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;

public class CheckFolderList extends Activity {
	String baseFolderPath;
	String resync;
	
	String folderListFileName;
	String fileListFileName;
		
	String dbScan;
	
	String scan;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent gintent = getIntent();
        resync = gintent.getStringExtra("resync");
        baseFolderPath = gintent.getStringExtra("path");
        
        folderListFileName = gintent.getStringExtra("folderListFileName");
        fileListFileName = gintent.getStringExtra("fileListFileName");
        
        dbScan = gintent.getStringExtra("dbscan");

		UseDb db = new UseDb();
		scan = db.getValue(this, dbScan, "yes");
        
        if(scan.equals("yes") == false)
        {
        	if(resync.equals("yes")){
        		//re scan.
        		goviewer();
        	}
        	else{
        		//
	        	Intent intent = new Intent(this, ImageViewer.class);
	        	intent.putExtra("folderListfileName", folderListFileName);
	        	intent.putExtra("fileListfileName", fileListFileName);
	        	startActivity(intent);
	        	finish();
        	}
        }
        else
        {
        	//first view. scan.
        	goviewer();
        }
    }
    
    private void goviewer() {
    	UseDb db = new UseDb();
		db.setValue(this, dbScan, "no");
    	Intent intent = new Intent(this, Loading.class);
    	intent.putExtra("path", baseFolderPath);
    	intent.putExtra("folderListfileName", folderListFileName);
    	intent.putExtra("fileListFileName", fileListFileName);
    	startActivity(intent);
    	finish();
    }


}
