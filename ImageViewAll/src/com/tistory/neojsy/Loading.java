package com.tistory.neojsy;

import java.io.*;
import java.util.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class Loading extends Activity {
	String basePath;
	ArrayList<String> mDirList = new ArrayList<String>();
	ArrayList<String> mFileList = new ArrayList<String>();
	private int value = 0;
	private CountDownTimer timer;
	String scannigFolder = "Initializing...";
	String folderListFileName = null;
	String fileListFileName = null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        Intent gintent = getIntent();
    	basePath = gintent.getStringExtra("path");
        folderListFileName = gintent.getStringExtra("folderListfileName");
        fileListFileName = gintent.getStringExtra("fileListFileName");
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        
        timer = new CountDownTimer(50000, 100) {
            public void onTick(long millisUntilFinished) {
            	value++;
            	//setContentView(vw);
            	drawLoading();
            	if( value == 15 ){
            		startFolderScanThread();
            	}
            }

            public void onFinish() {
            }
        };
        timer.start();
    }

    public void drawLoading(){

        ImageView image = new ImageView(this);
        image = (ImageView)findViewById(R.id.loading);
        TextView pathView = (TextView) findViewById(R.id.textView1);
        
        int na;
        na = value%6;
        if(na==0)
            image.setImageResource(R.drawable.p1);
        else if(na==1)
            image.setImageResource(R.drawable.p2);
        else if(na==2)
            image.setImageResource(R.drawable.p3);
        else if(na==3)
            image.setImageResource(R.drawable.p4);
        else if(na==4)
            image.setImageResource(R.drawable.p5);
        else if(na==5)
            image.setImageResource(R.drawable.p6);
    
 
        pathView.setText(scannigFolder);
    }
   
    
    public void startFolderScanThread(){
    	Thread backGround = new Thread() {
    		public void run() {
    			folderScan();
    			mCompleteHandler.sendEmptyMessage(0);
    		}
    	};	
    	backGround.start();
    }
    
    public Handler mCompleteHandler = new Handler(){
    	public void handleMessage(Message msg){
    		timer.cancel();
    		finish();
    	}
    };
    
    public void folderScan(){
    	addFolderList(basePath);
    	addFileList();
    	saveFolderListToFile();
    	saveFileListToFile();
    	Intent intent = new Intent(this, ImageViewer.class);
    	intent.putExtra("folderListfileName", folderListFileName);
    	intent.putExtra("fileListfileName", fileListFileName);
    	startActivity(intent);
    }
    
    public boolean saveFolderListToFile(){
    	//add??folderlist瑜?file濡???옣?쒕떎.
    	try {	
    			FileOutputStream fos = openFileOutput(folderListFileName, Context.MODE_WORLD_READABLE);
    			fos.write(mDirList.toString().getBytes());
    			fos.close();
        	} catch(Exception e) {
        }
    	return true;
    }
    
    public boolean saveFileListToFile(){
    	//add??folderlist瑜?file濡???옣?쒕떎.
    	try {	
    			FileOutputStream fos = openFileOutput(fileListFileName, Context.MODE_WORLD_READABLE);
    			fos.write(mFileList.toString().getBytes());
    			fos.close();
        	} catch(Exception e) {
        }
    	return true;
    }
    
    public void addFileList(){
    	int fileNumber = 0;
    	for(int i=0; i < mDirList.size() ; i++){
        	File fp = new File(mDirList.get(i));
    		File[] file_list = fp.listFiles(new FilenameFilter() {
    	        public boolean accept( File dir, String name ) {
    	        	String lowName = name.toLowerCase();
    	        	if(lowName.endsWith( ".jpg" ))
    	        		return true;
    	        	else if(lowName.endsWith( ".gif" ))
    	        		return true;
    	        	else if(lowName.endsWith( ".jpeg" ))
    	        		return true;
    	        	else if(lowName.endsWith( ".png" ))
    	        		return true;
    	        	else if(lowName.endsWith( ".bmp" ))
    	        		return true;
    	        	else
    	        		return false;
    	        }
    	    } );
    		
    		if(file_list.length != 0){
    			for(int k=0;k<file_list.length;k++){
    				mFileList.add(fileNumber, i+":::"+file_list[k].getName());
    				scannigFolder = "File searching...\n"+file_list[k].getName();
    				fileNumber++;
    			}
    		}
    	}
    	
    	Log.d(null, "detected fileNumber:"+fileNumber);
    }
 
    public void addFolderList(String folderPath){

    	File fp = new File(folderPath);
		File[] file_list = fp.listFiles(new FilenameFilter() {
	        public boolean accept( File dir, String name ) {
	        	if(name.equalsIgnoreCase(".android_secure"))
	        		return false;
	        	else if(name.equalsIgnoreCase(".thumbnails"))
	        		return false;
	        	else if(name.equalsIgnoreCase("LOST.DIR"))
        			return false;
	        	else if(name.equalsIgnoreCase(".Android"))
        			return false;
	        	else if(name.equalsIgnoreCase("Android"))
        			return false;
	        	else if(name.equalsIgnoreCase("ARM"))
        			return false;
	        	else if(name.equalsIgnoreCase(".systemlib"))
        			return false;
	        	else if(name.equalsIgnoreCase("data"))
        			return false;
	        	else if(name.startsWith("."))
	        		return false;
	        	else
	        		return true;
	        }
	    } );
		
    	for(File c : file_list) {
    	   if(c.isDirectory()) {
    	       mDirList.add(folderPath+"/"+c.getName());
    	   }
    	}
    	
    	int i=0;

		for(; i < mDirList.size() ; i++){
			File fp2 = new File(mDirList.get(i));
			File[] file_list2 = fp2.listFiles(new FilenameFilter() {
		        public boolean accept( File dir, String name ) {
		        	if(name.equalsIgnoreCase(".android_secure"))
		        		return false;
		        	else if(name.equalsIgnoreCase(".thumbnails"))
		        		return false;
		        	else if(name.equalsIgnoreCase("LOST.DIR"))
	        			return false;
		        	else
		        		return true;
		        }
		    } );
			
	    	for(File c : file_list2) {
	    		if(c.isDirectory()) {
	    			mDirList.add(mDirList.get(i)+"/"+c.getName());
	    			scannigFolder = "Folder searching...\n"+mDirList.get(i)+"/"+c.getName();
//	    			folderNum++;
	    	   }
	    	}    			
		}
		
		mDirList.add(folderPath);
		
    }
    
    public int getLCDwidth(){
        Display dp = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        return dp.getWidth();  	
    }
    
    public int getLCDheight(){
        Display dp = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        return dp.getHeight();   	
    }
}
