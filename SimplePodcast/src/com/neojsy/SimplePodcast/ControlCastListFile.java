package com.neojsy.SimplePodcast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ControlCastListFile {
	static final String TAG = "SimplePodcast";
	
	Context mContext ;
	
	ControlCastListFile(Context con){
		mContext = con;
	}
	
	public boolean Add2CastFile(ItemCast ic, String xmlURL){
    	ArrayList<String> mList = new ArrayList<String>();
    	mList.clear();
    	
    	LoadFileToList lf = new LoadFileToList(mContext);
    	File file = new File("/data/data/com.neojsy.SimplePodcast/files/cl.txt");
		if (file.exists()){
			try {
				mList = lf.getList("cl.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}    	
		
		if(mList.size()<2)
			mList.clear();
		/*
		for(int v=0;v<mList.size();v++){
			if(mList.get(v).equals(ic.title)){
				//Toast.makeText(this, "��?�??�붽����?�틦�ㅽ?��?�땲��",Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		*/
	   	//Log.d(TAG, "Cast list file : "+mList);
    	mList.add(ic.title);
    	mList.add(ic.subTitle);
    	mList.add(ic.desc);
    	mList.add(ic.author);
    	
    	String imageFilePath = "/sdcard/SimpleCast";
    	//String imageFileName = ic.title+ic.author+".jpg";
    	String imageFileName = " ";
    	
    	int n = ic.image.lastIndexOf("/");
		if (n >= 0) {
			imageFileName = ic.image.substring(n + 1);
		}
    	
    	try {
    		ImageDownloader id = new ImageDownloader();
    		id.download(ic.image, imageFilePath, imageFileName);
    		int imgSize = 110;
    		if(Util.getLCDheight(mContext) > 1200){
    			imgSize = 200;
    		}else if(Util.getLCDheight(mContext) > 800){
    			imgSize = 165;
    		}
    		id.resize(imageFilePath, imageFileName, imageFileName+"_mini", imgSize, imgSize);//110
    		/*
			downImg(ic.image, imageFileName, imageFilePath);
			resize(imageFileName);
			*/
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	mList.add(imageFilePath + "/" + imageFileName);
    	mList.add(" ");
    	mList.add(xmlURL);
    	
    	lf.saveFileListToFile("cl.txt", mList);
    	
    	try {
			writeToNewFile(ic.title);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return true;
    }
	
	public void resize(String filepath){
		Log.d(TAG, "filepath : "+filepath);
		Bitmap tempBitmap = BitmapFactory.decodeFile("/sdcard/SimpleCast"+"/"+filepath);
		Bitmap bitmap = Bitmap.createScaledBitmap(tempBitmap, 100,	100, true);
		
		FileOutputStream fos = null;
		try {
			Log.d(TAG, "filepath : start "+filepath);
			fos = new FileOutputStream("/sdcard/SimpleCast"+"/"+filepath+"1.png");
			if (fos != null) {
				Log.d(TAG, "compress : compress "+filepath);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.close();
			}
		} catch (Exception e) {
		}

	}
    
    private void writeToNewFile(String title) throws IOException{
    	ArrayList<String> mList = new ArrayList<String>();
    	mList.clear();
    	
    	LoadFileToList lf = new LoadFileToList(mContext);
    	mList.add("new");
    	mList.add(title);
    	lf.saveFileListToFile("new.txt", mList);
    }
	
    private boolean downImg(String url, String FileName, String FilePath) throws IOException, Throwable{

    	File path = new File(FilePath);
	     if(!path.isDirectory()) {
	             path.mkdirs();
	     }
    	
	     InputStream inputStream = new URL(url).openStream();
        
	     File file = new File(FilePath + "/" + FileName);
	     OutputStream out = new FileOutputStream(file);
 
	     int c = 0;
	     while((c = inputStream.read()) != -1)
        	out.write(c);
	     out.flush();
    	
	     out.close();
    	
	     return true;
    }
}
