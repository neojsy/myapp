package com.neojsy.SimplePodcast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;

public class Download extends Service {

	static final String TAG = "SimplePodcast";
	
	SharedPreferences preferences;

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private NotificationManager mNM;
	String downloadUrl;
	public static boolean serviceState = false;

	private String fileName = "test";
	private String titleName = "";
	private String maker;
	
	Intent intent = new Intent("com.neojsy.SimplePodcast.DOWN");

	private static boolean downloading = false;
	static ArrayList<DownloadItem> downList = new ArrayList<DownloadItem>();
	
    public class DownloadItem {
    	public String fileName;
    	public String url;
    	public String title;
    	public String maker;
    	
    	DownloadItem(String n, String u, String t, String m){
    		fileName = n;
    		url = u;
    		title = t;
    		maker = m;
    	}
    }
	
	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			//Log.d(TAG, "handleMessage");

			downloadFile();
			
			while(downList.size() > 0){
				////Log.d(TAG, "file download remain "+ downList.size());
				//int i = downList.size()-1;
				fileName = downList.get(0).fileName;
				downloadUrl = downList.get(0).url;
				titleName = downList.get(0).title;
				maker = downList.get(0).maker;
				downList.remove(0);
				downloadFile();
			}
			
			mNM.cancel(R.string.app_name);
			stopSelf(msg.arg1);
		}
	}

	@Override
	public void onCreate() {
		//Log.d(TAG, "onCreate");
		downList.clear();
		serviceState = true;
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		HandlerThread thread = new HandlerThread("ServiceStartArguments", 1);
		thread.start();
		// Get the HandlerThread's Looper and use it for our Handler
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Log.d(TAG, "onStartCommand");
		Bundle extra = intent.getExtras();
		if (extra != null) {
			String downloadUrl = extra.getString("downloadUrl");
			String downloadFileName = extra.getString("downfilename");
			String downloadFileTitle = extra.getString("name");
			String downloadMaker = extra.getString("maker");
			
			Log.d(TAG,"downloadUrl "+ downloadUrl);

			if(downloading){
				if(downloadFileName.equals("stop")){
					downStop = true;
					downList.clear();
				}else{
					downList.add(new DownloadItem(downloadFileName, downloadUrl, downloadFileTitle, downloadMaker));
				}
			}else{
				this.fileName = downloadFileName;
				this.downloadUrl = downloadUrl;
				this.titleName = downloadFileTitle;
				this.maker = downloadMaker;
			}
		}

		if(downloading){
			
		}else{
			Message msg = mServiceHandler.obtainMessage();
			msg.arg1 = startId;
			mServiceHandler.sendMessage(msg);
		}
		// If we get killed, after returning from here, restart
		return START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		//Log.d(TAG, "DESTORY");
		serviceState = false;
		// Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// We don't provide binding, so return null
		return null;
	}

	public void downloadFile() {
		//Log.d(TAG, "downloadFile");
		downloading = true;
		downloadFile(this.downloadUrl, fileName);
		downloading = false;
	}

	
	void showNotification(String message, String title) {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		CharSequence text = message;

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.ic_launcher, "Podcast Download", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_ONGOING_EVENT;

		Intent intent = new Intent(this, DownloadMng.class); 
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this.getBaseContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, title, text, contentIntent);
		// Send the notification.
		// We use a layout id because it is a unique number. We use it later to
		// cancel.
		mNM.notify(R.string.app_name+1, notification);
	}

	private String ConvByteSpeed(int sp){
		String res;
		if(sp > 1024){
			res = Util.Int2Str(sp/1024)+"."+Util.Int2Str(sp%1024).substring(0, 1)+"MB";
		}else{
			res = Util.Int2Str(sp)+"KB";
		}
		return res;
	}
	
	private boolean downStop = false;
	
	private int newDownload(String fileURL, String fileName){
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		int MAX_BUFFER = 4096;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;

			HttpGet get = new HttpGet(fileURL);
			response = client.execute(get);


			final HttpEntity entity = response.getEntity();
			
			bis = new BufferedInputStream( entity.getContent());

			File f = new File(fileName);
			if (f.exists() == false) 
				f.createNewFile();
			Log.d(TAG, "newDownload 4_");
			bos = new BufferedOutputStream(new FileOutputStream(f));
			Log.d(TAG, "newDownload 4");
			long totalBytes = entity.getContentLength();
			long readBytes = 0;
			Log.d(TAG, "newDownload 5");
			byte[] buffer = new byte[MAX_BUFFER];
			long time = 0;
			String percent;

			while(totalBytes > readBytes) {
				
				if(downStop){
					Log.d(TAG, "Stop download");

					this.showNotification(getResources().getString(R.string.msg_downcanceled), titleName);
					
					intent.putExtra("state", "stop");      
					intent.putExtra("title", titleName);   
					intent.putExtra("dMaker", maker);
					intent.putExtra("percent", "0");
					intent.putExtra("waitingMaker", waitingMaker);
					intent.putExtra("waitingURL", waitingFileUrl);
			        sendBroadcast(intent); 
					
					File dfile = new File(fileName);
					if(dfile.exists()){
						Log.d(TAG, "file detected. delete...");
						dfile.delete();
					}
			        
					try {
						bos.close();
						bis.close();
					} catch (IOException e) {}


			        
			        Log.d(TAG, "stopSelf");
					stopSelf();
					return 2;
				}
				
				int read = bis.read(buffer);
				readBytes += read;
				Log.d(TAG, "file download :"+readBytes);
				bos.write(buffer, 0, read);
				
				if(time + 400 < System.currentTimeMillis()){

					percent = Util.Int2Str((int) (readBytes*100/totalBytes));
					
					this.showNotification(ConvByteSpeed((int) readBytes/1024)+"/"+ConvByteSpeed((int) totalBytes/1024)+" ["+percent+"%] ", titleName);
					
					//write wating file
					waitingMaker = ">";
					waitingFileUrl = ">";
					if(downList.size() > 0){
						for(int i = 0 ; i < downList.size() ; i++){
							waitingMaker = waitingMaker + ">" + downList.get(i).maker;
							waitingFileUrl = waitingFileUrl + ">" + downList.get(i).url;
						}
					}
					
					intent.putExtra("state", "down");      
					intent.putExtra("title", titleName);   
					intent.putExtra("percent", percent);
					intent.putExtra("dMaker", maker);
					intent.putExtra("url", fileURL);
					intent.putExtra("waitingMaker", waitingMaker);
					intent.putExtra("waitingURL", waitingFileUrl);
					
					intent.putExtra("downK", ConvByteSpeed((int) readBytes/1024));
					intent.putExtra("totalK", ConvByteSpeed((int) totalBytes/1024));
					
	                sendBroadcast(intent);  
	                
					time = System.currentTimeMillis();
				}
			}
			bos.flush();
			Log.d(TAG, "complete");
			
			return 0;
		}catch (IOException e) {
			Log.d(TAG, "error");
			return 1;
		} finally {
			try {
				if (bos != null) bos.close();
				if (bis != null) bis.close();
			} catch (IOException e) {}
		}
	}
	
	String waitingMaker = ">";
	String waitingFileUrl = ">";
	public void downloadFile(String fileURL, String fileName) {


		
		intent.putExtra("state", "start");      
		intent.putExtra("title", titleName);   
//		intent.putExtra("bytedown", "0");  
//		intent.putExtra("bytetotal", "0");
		intent.putExtra("dMaker", maker);
		intent.putExtra("percent", "0");
		intent.putExtra("waitingMaker", waitingMaker);
		intent.putExtra("waitingURL", waitingFileUrl);
        sendBroadcast(intent); 
		showNotification(getResources().getString(R.string.msg_downloadstartnoti), titleName);
		
		downStop = false;
		
		Log.d(TAG, "downloadFile fileName :"+fileName);
		StatFs stat_fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double avail_sd_space = (double) stat_fs.getAvailableBlocks() * (double) stat_fs.getBlockSize();
		// double GB_Available = (avail_sd_space / 1073741824);
		double MB_Available = (avail_sd_space / 1048576);
		// System.out.println("Available MB : " + MB_Available);
		
		try {
			File root = new File(Environment.getExternalStorageDirectory() + "/SimpleCast");
			if (root.exists() && root.isDirectory()) {

			} else {
				root.mkdir();
			}
			Log.d(TAG, root.getPath());
			

			int result = newDownload(fileURL, root.getPath()+"/"+fileName);

			
			/*
			URL u = new URL(fileURL);
			HttpURLConnection c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();
			int fileSizeReal = c.getContentLength();
			int fileSize = fileSizeReal / 1048576;
			Log.d(TAG, "fileSizeReal " + fileSizeReal);
			Log.d(TAG, "FILESIZE " + fileSize);
			Log.d(TAG, "MB_Available " + MB_Available);
			Log.d(TAG, "fileSize " + fileSize);
			if (MB_Available <= fileSize) {
				this.showNotification(getResources().getString(R.string.msg_downloaderror), titleName);
				
				intent.putExtra("state", "memory");      
				intent.putExtra("title", titleName);   
//				intent.putExtra("bytedown", "0");  
//				intent.putExtra("bytetotal", "0");
				intent.putExtra("dMaker", maker);
				intent.putExtra("percent", "0");
				intent.putExtra("waitingMaker", waitingMaker);
				intent.putExtra("waitingURL", waitingFileUrl);
		        sendBroadcast(intent); 
		        Log.d(TAG, "Stop download1");
				
				c.disconnect();
				return;
			}

			FileOutputStream f = new FileOutputStream(new File(root.getPath(), fileName));

			InputStream in = c.getInputStream();

			byte[] buffer = new byte[1024];
			int len1 = 0;
			int display = 1;
			
			long downloaded = 0;
			int downloadedK = 0;
			fileSizeReal = fileSizeReal/1024;
			
			long time = 0;
			String percent;
			
			int olddownloadk = 0;
			
			while ((len1 = in.read(buffer)) > 0) {
				if(downStop){
					Log.d(TAG, "Stop download");
					f.close();
					File dfile = new File(root.getPath(), fileName);
					if(dfile.exists()){
						//Log.d(TAG, "file detected. delete...");
						dfile.delete();
					}
					
					this.showNotification(getResources().getString(R.string.msg_downcanceled), titleName);
					
					intent.putExtra("state", "stop");      
					intent.putExtra("title", titleName);   
//					intent.putExtra("bytedown", "0");  
//					intent.putExtra("bytetotal", "0");
					intent.putExtra("dMaker", maker);
					intent.putExtra("percent", "0");
					intent.putExtra("waitingMaker", waitingMaker);
					intent.putExtra("waitingURL", waitingFileUrl);
			        sendBroadcast(intent); 
					
			        Log.d(TAG, "stopSelf");
					stopSelf();
				}
				
				
				f.write(buffer, 0, len1);
				downloaded = downloaded + len1;
				downloadedK = (int) (downloaded/1024);
				
				display = display + 1;
				
				if(downloadedK < fileSizeReal){
					Log.d(TAG, "downloadedK");
					if(time + 700 < System.currentTimeMillis()){
						
						int speed = 0;
						if(olddownloadk > 0){
							speed = (downloadedK - olddownloadk)*7/10;
						}
						olddownloadk = downloadedK;
						
						percent = Util.Int2Str(downloadedK*100/fileSizeReal);
						
						this.showNotification(ConvByteSpeed(downloadedK)+"/"+ConvByteSpeed(fileSizeReal)+" ["+percent+"%] "+ConvByteSpeed(speed)+"/S", titleName);
						
						//write wating file
						waitingMaker = ">";
						waitingFileUrl = ">";
						if(downList.size() > 0){
							for(int i = 0 ; i < downList.size() ; i++){
								waitingMaker = waitingMaker + ">" + downList.get(i).maker;
								waitingFileUrl = waitingFileUrl + ">" + downList.get(i).url;
							}
						}
						
						intent.putExtra("state", "down");      
						intent.putExtra("title", titleName);   
//						intent.putExtra("bytedown", Util.Int2Str(downloadedK));  
//						intent.putExtra("bytetotal", Util.Int2Str(fileSizeReal));
						intent.putExtra("percent", percent);
						intent.putExtra("dMaker", maker);
						intent.putExtra("url", fileURL);
						intent.putExtra("waitingMaker", waitingMaker);
						intent.putExtra("waitingURL", waitingFileUrl);
		                sendBroadcast(intent);  
		                
						time = System.currentTimeMillis();
					}
					*/
					/*
					if(display%500 == 0){
						////Log.d(TAG, "down |Now:"+downloaded+"|Total:"+fileSizeReal);
						this.showNotification(downloadedK+"KB"+" / "+fileSizeReal+"KB", titleName);
						      
					}
					if(display%50 == 0){
						////Log.d(TAG, "down |Now:"+downloaded+"|Total:"+fileSizeReal);
						intent.putExtra("state", "down");      
						intent.putExtra("title", titleName);   
						intent.putExtra("bytedown", Util.Int2Str(downloadedK));  
						intent.putExtra("bytetotal", Util.Int2Str(fileSizeReal));
						intent.putExtra("url", fileURL);
		                sendBroadcast(intent);              
					}
					
				}
			}
			f.close();
			*/
			
			if(result == 0){
				intent.putExtra("state", "end");      
				intent.putExtra("title", titleName);   
	//			intent.putExtra("bytedown", "0");  
	//			intent.putExtra("bytetotal", "0");
				intent.putExtra("dMaker", maker);
				intent.putExtra("percent", "100");
				intent.putExtra("waitingMaker", waitingMaker);
				intent.putExtra("waitingURL", waitingFileUrl);
		        sendBroadcast(intent); 
		        
				showNotification(getResources().getString(R.string.msg_downloadendnoti), titleName);
				mNM.cancel(R.string.app_name+1);
			}else if(result == 1){
				this.showNotification(getResources().getString(R.string.msg_downloaderror), titleName);
				
				intent.putExtra("state", "memory");      
				intent.putExtra("title", titleName);   
//				intent.putExtra("bytedown", "0");  
//				intent.putExtra("bytetotal", "0");
				intent.putExtra("dMaker", maker);
				intent.putExtra("percent", "0");
				intent.putExtra("waitingMaker", waitingMaker);
				intent.putExtra("waitingURL", waitingFileUrl);
		        sendBroadcast(intent); 
		        Log.d(TAG, "Stop download1");
		        mNM.cancel(R.string.app_name+1);
			}else{
				mNM.cancel(R.string.app_name+1);
			}
			
			/*
			File file = new File(root.getAbsolutePath() + "/" + "some.pdf");
			if (file.exists()) {
				file.delete();
				//Log.d(TAG, "YES");
			} else {
				//Log.d(TAG, "NO");
			}
			File from = new File(root.getAbsolutePath() + "/" + fileName);
			File to = new File(root.getAbsolutePath() + "/" + "some.pdf");
			*/

		} catch (Exception e) {
			////Log.d(TAG, e.getMessage());

		}
	}
}