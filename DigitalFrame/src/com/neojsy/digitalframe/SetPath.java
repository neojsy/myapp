package com.neojsy.digitalframe;

import java.io.*;
import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.text.*;
import android.view.*;
import android.widget.*;

public class SetPath extends Activity {
	String getPath;
	EditText editset;
	Editable data;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpath);
        Intent gintent = getIntent();
        getPath = gintent.getStringExtra("path");
        editset = (EditText) findViewById(R.id.id_edit);
        data = editset.getText();
        
        if(getPath.equals("empty")){
        	data.insert(0, "/mnt/sdcard/");
        }
        else{
        	data.insert(0, getPath);
        }
        
    }
    
    public void mOnClickSet(View v) {
    	Intent intent = new Intent(this, FolderEx.class);
    	startActivity(intent);
    	/*
        Editable data = editset.getText();
        String inputPath = data.toString();
        String anotherPath = getResources().getString(R.string.sp_msg_nonotsdcard);
        
        if(inputPath.equals("/mnt/sdcard")){
        	Toast.makeText(SetPath.this, anotherPath, Toast.LENGTH_SHORT).show();
        }
        else if
        (inputPath.equals("/mnt/sdcard/")){
        	Toast.makeText(SetPath.this, anotherPath, Toast.LENGTH_SHORT).show();
        }
        else if
        (inputPath.equals("mnt/sdcard")){
        	Toast.makeText(SetPath.this, anotherPath, Toast.LENGTH_SHORT).show();
        }
        else if
        (inputPath.equals("mnt/sdcard/")){
        	Toast.makeText(SetPath.this, anotherPath, Toast.LENGTH_SHORT).show();
        }
        else{
            File files = new File(inputPath);
            if(files.exists()==true){
	        	saveListToFile(inputPath);
	        	Toast.makeText(SetPath.this, data+" is saved!", Toast.LENGTH_SHORT).show();
	        	finish();
            }
            else{
            	Toast.makeText(SetPath.this, getResources().getString(R.string.sp_msg_notexistfolder), Toast.LENGTH_SHORT).show();
            }
        }
        */
    }
    
    public boolean saveListToFile(String path){
    	//add??folderlist??file�??????�떎.
    	try {	
    			FileOutputStream fos = openFileOutput("baseFolderPath.txt", Context.MODE_WORLD_READABLE);
    			//String str = "Android File IO Test";
    			//fos.write(str.getBytes());
    			fos.write(path.getBytes());
    			fos.close();
        	} catch(Exception e) {
        }
    	return true;
    }
}
