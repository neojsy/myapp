package com.tistory.neojsy;

import java.io.*;
import java.util.*;

import android.content.*;
import android.util.*;

public class SettingInfo {
    String TAG = "sy";
    ArrayList<String> mDirList = new ArrayList<String>();
    final static int indexDelete = 0;
    final static int indexInfo = 1;
    final static int indxeDelPopup = 2;
    final static int indexSs = 3;
    final static int indxeMax = 4;
    Context context;
    
    public SettingInfo(Context con) {
        context = con;
        File files = new File("/data/data/com.tistory.neojsy/files/st.txt");
        if (files.exists() == true) {
            Log.d(TAG, "file yes");
            try {
                loadFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "file no");
            for(int i=0;i<indxeMax;i++){
            	mDirList.add(i, "Y");
 //           mDirList.add(indexInfo, "Y");
 //           mDirList.add(indexInfo, "Y");
            }
            saveFile();
        }
    }

    public boolean isDeleteOn() {
        Log.d(TAG, "");
        String value = mDirList.get(indexDelete);
        if(value.equals("Y")){
            return true;
        }else{
            return false;
        }
    }

    public void setDeleteOn(boolean set) {
        
        if(set == true){
            mDirList.set(indexDelete, "Y");
            Log.d(TAG, "Y");
        }else{
            mDirList.set(indexDelete, "N");
            Log.d(TAG, "N");
        }
        
        saveFile();
    }

    public boolean isDelPopupOn() {
        Log.d(TAG, "");
        String value = mDirList.get(indxeDelPopup);
        if(value.equals("Y")){
            return true;
        }else{
            return false;
        }
    }

    public void setDelPopupOn(boolean set) {
        
        if(set == true){
            mDirList.set(indxeDelPopup, "Y");
            Log.d(TAG, "Y");
        }else{
            mDirList.set(indxeDelPopup, "N");
            Log.d(TAG, "N");
        }
        
        saveFile();
    }
    
    public boolean isInfoOn() {
        Log.d(TAG, "");
        String value = mDirList.get(indexInfo);
        if(value.equals("Y")){
            return true;
        }else{
            return false;
        }
    }

    public void setInfoOn(boolean set) {
        Log.d(TAG, "");
        if(set == true){
            mDirList.set(indexInfo, "Y");
            Log.d(TAG, "Y");
        }else{
            mDirList.set(indexInfo, "N");
            Log.d(TAG, "N");
        }
        saveFile();
    }

    public boolean isSsOn() {
        Log.d(TAG, "");
        String value = mDirList.get(indexSs);
        if(value.equals("Y")){
            return true;
        }else{
            return false;
        }
    }

    public void setSsOn(boolean set) {
        Log.d(TAG, "");
        if(set == true){
            mDirList.set(indexSs, "Y");
            Log.d(TAG, "Y");
        }else{
            mDirList.set(indexSs, "N");
            Log.d(TAG, "N");
        }
        saveFile();
    }
    
    private void loadFile() throws IOException{
        //load setting..
        FileInputStream fis = context.getApplicationContext().openFileInput("st.txt");
        byte[] data = new byte[fis.available()];
        while (fis.read(data) != -1) {;}
        fis.close();
        
        mDirList.clear();
        //byte -> str
        String str = new String(data, "UTF-8");
        
        //str -> str array
        String[] temps = str.split(", ");
        // [ , ] remove
        temps[0] = temps[0].replace("[","");
        temps[temps.length-1] = temps[temps.length-1].replace("]","");
        
        int i=0;
        //str add to mDirList
        for(i=0 ; i < temps.length ; i++){
            mDirList.add(temps[i]);  
            Log.d(TAG, "0 : "+mDirList.get(i));
        }
    }
    
    private void saveFile() {
    	Log.d(TAG, mDirList.toString());
        try {
            FileOutputStream fos = context.openFileOutput("st.txt", Context.MODE_WORLD_READABLE);
            fos.write(mDirList.toString().getBytes());
            fos.close();
        } catch (Exception e) {
        }
    }

}
