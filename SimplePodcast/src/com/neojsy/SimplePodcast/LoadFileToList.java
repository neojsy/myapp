package com.neojsy.SimplePodcast;

import java.io.*;
import java.util.*;

import android.content.*;
import android.util.Log;

public class LoadFileToList {
	static final String TAG = "SimplePodcast";
	
	Context context;

	LoadFileToList(Context con) {
		context = con;
	}

	public ArrayList<String> getList(String fileName) throws IOException {
		
		//Log.d(TAG, "fileName : "+fileName);
		
		ArrayList<String> mList = new ArrayList<String>();

		FileInputStream fis = context.openFileInput(fileName);
		byte[] data = new byte[fis.available()];
		while (fis.read(data) != -1) {
			;
		}
		fis.close();

		mList.clear();
		// byte -> str
		String str = new String(data, "UTF-8");

		// str -> str array
		String[] temps = str.split(", ");
		// [ , ] remove
		temps[0] = temps[0].replace("[", "");
		temps[temps.length - 1] = temps[temps.length - 1].replace("]", "");

		int i = 0;
		// str add to mDirList
		for (i = 0; i < temps.length; i++) {
			mList.add(temps[i]);
		}

		return mList;
	}
	
    public boolean saveFileListToFile(String fileName, ArrayList<String> as){
    	//Log.d(TAG, "fileName : "+fileName);
    	
    	try {	
    			FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
    			fos.write(as.toString().getBytes());
    			fos.close();
        	} catch(Exception e) {
        }
    	return true;
    }

}
