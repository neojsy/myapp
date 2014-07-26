package com.neojsy.SimplePodcast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ImageDownloader {
	
    public boolean download(String url, String FilePath, String FileName) throws IOException, Throwable{

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
    
	public void resize(String FilePath, String fileName, String newFileName, int w, int h){
		Bitmap tempBitmap = BitmapFactory.decodeFile(FilePath+"/"+fileName);
		Bitmap bitmap = Bitmap.createScaledBitmap(tempBitmap, w, h, true);
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(FilePath+"/"+newFileName+".png");
			if (fos != null) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.close();
			}
		} catch (Exception e) {
		}

	}
}
