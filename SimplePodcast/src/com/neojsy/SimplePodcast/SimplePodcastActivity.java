package com.neojsy.SimplePodcast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SimplePodcastActivity extends Activity {
	
	static final String TAG = "SimplePodcast";
	
	static ArrayList<ItemCast> ie = new ArrayList<ItemCast>();
	static ArrayList<String> ienum = new ArrayList<String>();

	static int castListNumber = 0;
	ListView l1;
	
	SetData sData;
	static int InfoType;
	static final int INFO_DESC = 0;
	static final int INFO_LISTEN_NUM = 1;
	
	static int LCDH;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		LCDH = Util.getLCDheight(SimplePodcastActivity.this);
    	//eventPopup();
	}
    
    public void onResume() {
		super.onResume();
		
		loadSetting();

    	boolean loadsuccess = false;
    	try {
    		loadsuccess = loadCastList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	setContentView(R.layout.main);
    	drawHeadText();
    	
    	if(loadsuccess){
    		if(InfoType == INFO_LISTEN_NUM){
    			loadCastListenNum();
    		}
    		updateList();
    		try {
				CheckAndUpdateNewCast();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}

    	//showNotice(SimplePodcastActivity.this);
/*    	
       	if(ie.size() == 0){
    			new AlertDialog.Builder(this)
    			//.setTitle(getResources().getString(R.string.main_update))
    			//.setMessage(getResources().getString(R.string.update0007))
    			.setMessage("\n�듭???�붽�瑜��??�� �잛??��?�듃?��?�붽���二?�꽭��\n")
    			.setPositiveButton("OK", null).show();
        }
*/
    }
    
	private void eventPopup() {
		
		Random oRandom = new Random();
		int rand = oRandom.nextInt(7);

		int res = R.drawable.t01;
		switch(rand){
		case 0:
			res = R.drawable.t01;
			break;
		case 1:
			res = R.drawable.t02;
			break;
		case 2:
			res = R.drawable.t03;
			break;
		case 3:
			res = R.drawable.t04;
			break;
		case 4:
			res = R.drawable.t05;
			break;
		case 5:
			res = R.drawable.t06;
			break;
		case 6:
			res = R.drawable.t07;
			break;
		}
		
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(res));
		dialog.show();
		
		/*
		new AlertDialog.Builder(SimplePodcastActivity.this)
				.setTitle(" ")
				.setMessage("12월19일! 투표로 말합시다!")
				.setIcon(res)
				.setNegativeButton(
						"GO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {}
						}).show();
*/
	}
    
	public void showNotice(Context context) {
		SharedPreferences pref = context.getSharedPreferences("VER", 0);

			PackageManager pm = context.getPackageManager();
			PackageInfo packageInfo = null;
			try {
				packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int VERSION = packageInfo.versionCode;
			int old_Ver = pref.getInt("version", 0);

			if (old_Ver < VERSION) 
			{
				new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.main_notice))
				.setMessage(getResources().getString(R.string.main_notice_info))
				.setPositiveButton("OK", null).show();
				
				SharedPreferences.Editor edit = pref.edit();
				edit.putInt("version", VERSION);
				edit.commit();
				
				checkDownloadedImage();
			}

	}

	private void checkDownloadedImage(){
		for(int i=0;i<ie.size();i++){
	    	File imagefile = new File(ie.get(i).image);
	    	Log.d(TAG, "imagefile "+imagefile);
	    	if(imagefile.exists()){
	    		Log.d(TAG, "imagefile detected.");
	    		File imagefile_mini = new File(ie.get(i).image+"_mini.png");
	    		if(!imagefile_mini.exists()){
	    			Log.d(TAG, "imagefile mini file not detected. create!");
	    			
	    			ImageDownloader id = new ImageDownloader();
	        		int imgSize = 110;
	        		if(Util.getLCDheight(SimplePodcastActivity.this) > 800){
	        			imgSize = 165;
	        		}
	        		
	        		int n = ie.get(i).image.lastIndexOf("/");
	        		String filename;
	        		if (n >= 0) {
	        		    filename = ie.get(i).image.substring(n + 1);
	        		    id.resize(Util.getAppFolderPath(), filename, filename+"_mini", imgSize, imgSize);//110
	        		}

	    		}
	    	}
		}
	}
    
    private void loadSetting(){
		sData = new SetData(SimplePodcastActivity.this);
		switch(sData.getCastInfo()){
			case 0:
				InfoType = INFO_DESC;
				break;
			case 1:
				InfoType = INFO_LISTEN_NUM;
				break;
		}
    }
    
    private void drawHeadText(){
		TextView tv1 = (TextView)findViewById(R.id.h1);
		TextView tv2 = (TextView)findViewById(R.id.h2);
		tv1.setText(getResources().getString(R.string.app_name)+" "+getResources().getString(R.string.app_ver));
		
		if(ie.size()==0){
			tv2.setText(getResources().getString(R.string.msg_addpodcast));
		}else{
			SharedPreferences pref = this.getSharedPreferences("Simplepodcast", Activity.MODE_PRIVATE);
			String value = pref.getString("update_date", " ");
			tv2.setText(ie.size()+getResources().getString(R.string.msg_describe)+"  " + value);
		}
    }
    
	public void drawDetailPopup() {
		ScrollView scv = new ScrollView(this);
		scv.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		scv.setMinimumHeight(100);
		LinearLayout parentLinear = new LinearLayout(this);
		parentLinear.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		parentLinear.setOrientation(LinearLayout.VERTICAL);
		scv.addView(parentLinear);

		LinearLayout linear;
		TextView tv;

		linear = new LinearLayout(this);
		linear.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		linear.setOrientation(LinearLayout.HORIZONTAL);
		parentLinear.addView(linear);
		// property name
		tv = new TextView(this);
		tv.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		// tv.setWidth(80);
		tv.setPadding(5, 5, 5, 5);
		// tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		tv.setText(getResources().getString(R.string.update001));
		linear.addView(tv);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.main_updateinfo));
		builder.setIcon(android.R.drawable.ic_menu_info_details);
		builder.setView(scv);
		builder.setPositiveButton("OK", null);
		builder.show();
	}

    private void DrawinfoPopup(){
    	
		String dev = 
				getResources().getString(R.string.main_info_name) + " : "
				+ getResources().getString(R.string.main_info_maker) + "\n"
				+ getString(R.string.main_info_email) + " : "
				+ getResources().getString(R.string.main_info_email_url) + "\n"
				+ getResources().getString(R.string.main_info_blog) + " : "
				+ getResources().getString(R.string.main_info_blog_url) + "\n" + "\n"
				+ "\n" + getResources().getString(R.string.main_info_tk) + "\n" + "\n"
				+ "\n" + getResources().getString(R.string.main_info_my)
				;

		new AlertDialog.Builder(SimplePodcastActivity.this)
				.setTitle(" Thank you !")
				.setMessage(dev)
				.setIcon(R.drawable.tk)
				
				.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				})	
				.setNeutralButton(getResources().getString(R.string.main_updateinfo), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						/* User clicked Something so do some stuff */
						drawDetailPopup();
						//showUpdate();
					}
				}).show();
		
		//		.setNegativeButton(getResources().getString(R.string.mnu_jeonghwa), new DialogInterface.OnClickListener() {
			//		public void onClick(DialogInterface dialog, int whichButton) {
			//			/* User clicked Something so do some stuff */
			//			Intent intent = new Intent(SimplePodcastActivity.this, Teatime2.class);
		    //			startActivity(intent);
			//		}
			//	})
    	
    }
 
	public void showUpdateResult(String res){
		new AlertDialog.Builder(this)
		.setMessage(res)
		.setPositiveButton(getResources().getString(R.string.yes), null).show();
	}   
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		 // TODO Auto-generated method stub
		 menu.add(0, 1, 0, getResources().getString(R.string.mnu_add)).setIcon(android.R.drawable.ic_menu_add);
		 menu.add(0, 2, 0, getResources().getString(R.string.mnu_update)).setIcon(android.R.drawable.ic_menu_rotate);
		 menu.add(0, 3, 0, getResources().getString(R.string.mnu_setting)).setIcon(android.R.drawable.ic_menu_preferences);
		 menu.add(0, 4, 0, getResources().getString(R.string.mnu_info)).setIcon(android.R.drawable.ic_menu_info_details);

		 return true;
	}

    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent;
    	switch (item.getItemId()) {
    		case 1:
    			DrawAddListPopup();
    			break;
    		case 2:
    			if(!Util.isConnected(SimplePodcastActivity.this)){
            		Toast.makeText(SimplePodcastActivity.this, getResources().getString(R.string.msg_networkerror), Toast.LENGTH_SHORT).show();
    			}
            	else{
            		updateAll();
            	}
    			break;
    		case 3:
    			intent = new Intent(this, Setting.class);
    			startActivity(intent);
    			break;
    		case 4:
    			DrawinfoPopup();
    			break;
    	}
    	return true;
    }
    
    private void updateDateResist(){
    	SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy.MM.dd HH:mm:ss", Locale.KOREA );
    	Date currentTime = new Date ( );
    	String mTime = mSimpleDateFormat.format ( currentTime );
    	mTime = mTime.substring(0, 16);
    	
    	SharedPreferences pref = this.getSharedPreferences("Simplepodcast", Activity.MODE_PRIVATE);
    	SharedPreferences.Editor ed = pref.edit();
    	ed.putString("update_date", mTime+" "+getResources().getString(R.string.msg_update));
		ed.commit();
    }
    
	final static int UPDATE_ALL_END = 1;
	final static int UPDATE_ONE_END = 2;
	final static int MSG_UPDATE_TIMEOUT = 3;
	final static int UPDATE_STEP = 4;
	boolean IsUpdateFail = false;
	String UpdateFailResult = "";
	
	ProgressDialog ua_progress;
	int progressStep;
    private void updateAll(){
    	//Log.d(TAG, "updateAll !!");
    	
    	ua_progress = new ProgressDialog(SimplePodcastActivity.this);
    	ua_progress.setCancelable(false);
    	ua_progress.setMessage("File downloading ...");
    	ua_progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	ua_progress.setProgress(0);
    	ua_progress.setMax(castListNumber);
    	ua_progress.setIcon(R.drawable.tk);
    	ua_progress.show();
    	
    	progressStep = 0;
    	IsUpdateFail = false;
    	UpdateFailResult = "";
    	//ua_progress = ProgressDialog.show(SimplePodcastActivity.this, "", getResources().getString(R.string.msg_allupdateloading), true);
    	Thread backGround = new Thread() {
    		public void run() {
    	    	for(int i=0; i<castListNumber ; i++){
    	    		progressStep = i;
    	    		mCompleteHandler.sendEmptyMessage(UPDATE_STEP);
    	    		try {
    	    		ie.get(i).view = updateCast(ie.get(i).xmlUrl, ie.get(i).title+ie.get(i).author);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						IsUpdateFail = true;
						UpdateFailResult = UpdateFailResult + "\n" + ie.get(i).title;
					}
    	    	}
    	    	ua_progress.setProgress(castListNumber);
    	    	try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    	updateDateResist();
    	    	mCompleteHandler.sendEmptyMessage(UPDATE_ALL_END);
    		}
    	};	
    	backGround.start();
    	
    }

    ProgressDialog uo_progress;
    private void updateOne(final int index){
    	//Log.d(TAG, "updateAll !!");
    	IsUpdateFail = false;
    	uo_progress = ProgressDialog.show(SimplePodcastActivity.this, "", ie.get(index).title + getResources().getString(R.string.msg_oneupdateloading), true);
    	Thread backGround = new Thread() {
    		public void run() {
    			try {
    			ie.get(index).view = updateCast(ie.get(index).xmlUrl, ie.get(index).title+ie.get(index).author);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					IsUpdateFail = true;
				}
    			mCompleteHandler.sendEmptyMessage(UPDATE_ONE_END);
    		}
    	};	
    	backGround.start();	    	
    }
    
	
    
    public Handler mCompleteHandler = new Handler(){
    	public void handleMessage(Message msg){
    		
    		if(msg.what == UPDATE_ALL_END || msg.what == UPDATE_ONE_END){
    			//Log.d(TAG, "MSG_UPDATE_END");
	    		try {
					writeToFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		updateList();
	    		
	    		if(msg.what == UPDATE_ALL_END){
	    			ua_progress.dismiss();
	    			if(IsUpdateFail){
	    				showUpdateResult(UpdateFailResult + getResources().getString(R.string.msg_allupdatefail));
	    			}
	    			Toast.makeText(SimplePodcastActivity.this, getResources().getString(R.string.msg_allupdateloadingend), Toast.LENGTH_SHORT).show();
	    		}
	    		if(msg.what == UPDATE_ONE_END){
	    			uo_progress.dismiss();
	    			if(IsUpdateFail){
	    				showUpdateResult(getResources().getString(R.string.msg_oneupdatefail));
	    			}
	    			Toast.makeText(SimplePodcastActivity.this, getResources().getString(R.string.msg_oneupdateloadingend), Toast.LENGTH_SHORT).show();
	    		}
    		}
    		else if(msg.what == UPDATE_STEP){
   	    		ua_progress.setProgress(progressStep);
	    		ua_progress.setMessage(getResources().getString(R.string.msg_allupdateloading) + "\n\n" + ie.get(progressStep).title);
    		}
    	}
    };
    
    
    private String updateCast(final String xml, String fileName) throws Exception{
    	//Log.d(TAG, "updateCast !!");
    	
    	UpdateCast uc = new UpdateCast(SimplePodcastActivity.this);
    	String result = uc.Update(xml, fileName);
    	return result;
		//Log.d(TAG, "Cast updated : "+xml);
    }
    
    private void CheckAndUpdateNewCast() throws IOException{
    	ArrayList<String> mList = new ArrayList<String>();
    	mList.clear();
		
    	LoadFileToList lf = new LoadFileToList(SimplePodcastActivity.this);
    	File file = new File("/data/data/com.neojsy.SimplePodcast/files/cl.txt");
		if (file.exists()){
			mList = lf.getList("new.txt");
			
			if(mList.get(0).equals("new")){
				//Log.d(TAG, "new cast exist");
				
				int index = 0;
				for(int t = 0;t<ie.size();t++){
					if(ie.get(t).title.equals(mList.get(1))){
						index = t;
					}
				}
				mList.clear();
				lf.saveFileListToFile("new.txt", mList);
				updateOne(index);
			}else{
				//Log.d(TAG, "new cast non exist");
			}
		}else{
			//Log.d(TAG, "new file non exist");
		}    	
    }
    
    
    private void updateList(){
		l1 = (ListView) findViewById(R.id.ListView01);
		l1.setAdapter(new EfficientAdapter(this));
		setListListener();
		drawHeadText();
    }
    
    private void setListListener(){
        l1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	//Log.d(TAG, "Short position : "+position);
            	Intent intent = new Intent(SimplePodcastActivity.this, Episode.class);
    			intent.putExtra("datafilename", ie.get(position).title+ie.get(position).author);
    			intent.putExtra("datafilepath", "/data/data/com.neojsy.SimplePodcast/files/");
    			intent.putExtra("maker", ie.get(position).author);
    			intent.putExtra("title", ie.get(position).title);
    			intent.putExtra("imagefilename", ie.get(position).image);
				
    			startActivity(intent);
            }
        });
        
        l1.setOnItemLongClickListener(new OnItemLongClickListener() {
        	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            	//Log.d(TAG, "Long position : "+position);
            	final int pos = position;   	
        		AlertDialog.Builder alt_bld = new AlertDialog.Builder(SimplePodcastActivity.this);
        		alt_bld.setMessage(ie.get(pos).title+" "+getResources().getString(R.string.msg_deleteCast))
        				.setCancelable(true)
        				//.setIcon(R.drawable.delete)
        				.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
        							public void onClick(DialogInterface dialog, int id) {
        								// Action for 'Yes' Button
        				            	removeCast(pos);
        				            	try {
        									writeToFile();
        								} catch (IOException e) {
        									// TODO Auto-generated catch block
        									e.printStackTrace();
        								}
        				            	updateList();
        							}
        						})
        				.setNegativeButton(getResources().getString(R.string.no),
        						new DialogInterface.OnClickListener() {
        							public void onClick(DialogInterface dialog, int id) {
        								// Action for 'NO' Button
        								dialog.cancel();
        							}
        						});
        		AlertDialog alert = alt_bld.create();
        		alert.show();	
            	
            	return true;
            }
        });    	
    }
    
    private void writeToFile() throws IOException{
    	ArrayList<String> mList = new ArrayList<String>();
    	mList.clear();
    	
    	if(castListNumber != 0){
    		mList = Cast2Str(ie);
    	}
    	
    	LoadFileToList lf = new LoadFileToList(SimplePodcastActivity.this);
    	lf.saveFileListToFile("cl.txt", mList);
    }
    
    ProgressDialog rm_progress;
    private void removeCast(int index){
    	//Log.d(TAG, "removeCast");
    	//rm_progress = ProgressDialog.show(SimplePodcastActivity.this, "", ie.get(index).title+getResources().getString(R.string.msg_deleteCasting), true);
    	
    	//image file delete
    	File imagefile = new File(ie.get(index).image);
    	//Log.d(TAG, "imagefile "+imagefile);
    	if(imagefile.exists()){
    		//Log.d(TAG, "fileName detected. will be delete");
    		imagefile.delete();
    	}
    	File imagefilemini = new File(ie.get(index).image+"_mini");
    	//Log.d(TAG, "imagefile "+imagefile);
    	if(imagefilemini.exists()){
    		//Log.d(TAG, "fileName detected. will be delete");
    		imagefilemini.delete();
    	}
    	
    	//download file delete
    	File file = new File("/data/data/com.neojsy.SimplePodcast/files/"+ie.get(index).title+ie.get(index).author);
		if (file.exists()){
	    	ArrayList<String> mList = new ArrayList<String>();
	    	ArrayList<ItemEpisode> tempEpisode = new ArrayList<ItemEpisode>();
	    	mList.clear();
	    	LoadFileToList lf = new LoadFileToList(SimplePodcastActivity.this);
	    	Log.d(TAG, "1");
	    	try {
				mList = lf.getList(ie.get(index).title+ie.get(index).author);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UpdateCast uc = new UpdateCast(SimplePodcastActivity.this);
			tempEpisode = uc.Str2Epi(mList);			
			Log.d(TAG, "2");
			if(tempEpisode.size() > 0){
				for(int h=0;h<tempEpisode.size();h++){
					String fileName = Util.Url2Mp3name(tempEpisode.get(h).mp3url);
					Log.d(TAG, "fileName "+fileName);
					File downfile = new File(Util.getAppFolderPath()+fileName);
					if(downfile.exists()){
						//Log.d(TAG, "fileName detected. will be delete");
						downfile.delete();
					}
				}
			}
			file.delete();
			Log.d(TAG, "3");
		}  
		
    	ie.remove(index);
    	
    	//rm_progress.dismiss();
    	
		castListNumber  = ie.size();
    }
    


    AlertDialog m_adlgChangeDisplay;
    private void DrawAddListPopup()
    {
    	SetData sData = new SetData(SimplePodcastActivity.this);
    	
    	ArrayList<ItemPopup> ip = new ArrayList<ItemPopup>();
    	ip.add(new ItemPopup(R.drawable.select1, getResources().getString(R.string.mnu_add_recommed)));
    	if(sData.getUseKorea()){
    		ip.add(new ItemPopup(R.drawable.select2, getResources().getString(R.string.mnu_add_radio)));
    	}
    	ip.add(new ItemPopup(R.drawable.select3, getResources().getString(R.string.mnu_add_write)));
    			
    	final ListAdapter adapter = new ListAdapter(this, R.layout.popuplist, ip);
    		//adapter.SetDMRIconLoader(m_DMRIconLoader);
    		
    		m_adlgChangeDisplay = new AlertDialog.Builder(this)
    			//.setTitle("Change Display")
    			.setAdapter(adapter, new DialogInterface.OnClickListener()
    			{
    				public void onClick(DialogInterface dialog, int which) 
    				{
    					SetData sData = new SetData(SimplePodcastActivity.this);
    					
    		        	  switch(which){
    		        	  case 0:
    		        		  Intent intent1 = new Intent(SimplePodcastActivity.this, AddFamousCast.class);
    		        		  startActivity(intent1);
    		        		  break;
    		        	  case 1:
    		        		  Intent intent2;
    		        		  if(sData.getUseKorea()){
    		        			  intent2 = new Intent(SimplePodcastActivity.this, AddRadioCast.class);
    		        		  }
    		        		  else{
    		        			  intent2 = new Intent(SimplePodcastActivity.this, AddInputUrl.class);
    		        		  }
    		        		  startActivity(intent2);
    		        		  break;
    		        	  case 2:
    		        		  Intent intent3 = new Intent(SimplePodcastActivity.this, AddInputUrl.class);
    		        		  startActivity(intent3);
    		        		  break;
    		        	  }    					
    				}
    			})
    			.create();

    		m_adlgChangeDisplay.show();
       
    }


	private boolean loadCastList() throws IOException{
    	ArrayList<String> mList = new ArrayList<String>();
    	mList.clear();
    	ie.clear();
		
    	LoadFileToList lf = new LoadFileToList(SimplePodcastActivity.this);
    	File file = new File("/data/data/com.neojsy.SimplePodcast/files/cl.txt");
		if (file.exists()){
			mList = lf.getList("cl.txt");
			ie = Str2Cast(mList);
			castListNumber = ie.size();
			return true;	
		}else{
			//Log.d(TAG, "cl file not exist");
			return false;
		}
	}

	private void loadCastListenNum(){
		ienum.clear();
		
		if(ie.size() == 0){
			return;
		}
		
		for(int i = 0;i<ie.size();i++){
			ArrayList<String> mList = new ArrayList<String>();
	    	mList.clear();
	    	ArrayList<ItemEpisode> ie_temp = new ArrayList<ItemEpisode>();
	    	
	    	String fileName = ie.get(i).title+ie.get(i).author;
	    	LoadFileToList lf = new LoadFileToList(SimplePodcastActivity.this);
	    	File file = new File("/data/data/com.neojsy.SimplePodcast/files/"+fileName);
	    	
			if (file.exists()){
				try {
					mList = lf.getList(fileName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				UpdateCast uc = new UpdateCast(SimplePodcastActivity.this);
				ie_temp = uc.Str2Epi(mList);
				
				int unlead = 0;
				int complete = 0;
				int ing = 0;
				for(int x=0;x<ie_temp.size();x++){
					if(ie_temp.get(x).read.equals("no")){
						unlead++;
					}
					else if(ie_temp.get(x).read.equals("ok")){
						complete++;
					}
					else if(ie_temp.get(x).read.equals("ing")){
						ing++;
					}			
				}
				
				String info = 
						(getResources().getString(R.string.tit_totalepisode)
						+ " : "
						+ ie_temp.size() 
						+ "\n"
						+ getResources().getString(R.string.tit_new)
						+ " : "
						+ unlead
						+ ", "
						+ getResources().getString(R.string.tit_not_complete)
						+ " : "
						+ ing
						+ ", "
						+ getResources().getString(R.string.tit_complete)
						+ " : "
						+ complete);
					
				ienum.add(info);
			}
			else{
				ienum.add(" ");
			}
		}
	}
	
    public ArrayList<ItemCast> Str2Cast(ArrayList<String> src){
    	ArrayList<ItemCast> rs = new ArrayList<ItemCast>();
    	ItemCast temp = new ItemCast();
    	int num = temp.number;
    	int epinum = src.size()/num;

    	for(int k=0;k<epinum;k++){
    		rs.add(new ItemCast(
    				src.get(k*num + 0),
    				src.get(k*num + 1),
    				src.get(k*num + 2),
    				src.get(k*num + 3),
    				src.get(k*num + 4),
    				src.get(k*num + 5),
    				src.get(k*num + 6)
					));
    	}
    	/*
		for(int k=0;k<rs.size();k++){
			Log.d(TAG, "------------String to Episode---- array : "+k);
			Log.d(TAG, "ie.get(k).title : "+rs.get(k).title);
			Log.d(TAG, "ie.get(k).summary : "+rs.get(k).summary);
			Log.d(TAG, "ie.get(k).image : "+rs.get(k).image);
			Log.d(TAG, "ie.get(k).mp3url : "+rs.get(k).mp3url);
			Log.d(TAG, "ie.get(k).pubDate : "+rs.get(k).pubDate);
			Log.d(TAG, "ie.get(k).read : "+rs.get(k).read);
			Log.d(TAG, "ie.get(k).mp3PlayedTime : "+rs.get(k).mp3PlayedTime);
			Log.d(TAG, "ie.get(k).mp3FullTime : "+rs.get(k).mp3FullTime);
			Log.d(TAG, "ie.get(k).s1 : "+rs.get(k).s1);
			Log.d(TAG, "ie.get(k).newepi : "+rs.get(k).newepi);
		}
    	*/
    	return rs;
    }
    
    public ArrayList<String> Cast2Str(ArrayList<ItemCast> src){
    	ArrayList<String> mList = new ArrayList<String>();
 //   	ArrayList<ItemCast> ie = new ArrayList<ItemCast>();
    	mList.clear();
    	
		for(int k=0;k<src.size();k++){
			mList.add(src.get(k).title);
			mList.add(src.get(k).subTitle);
			mList.add(src.get(k).desc);
			mList.add(src.get(k).author);
			mList.add(src.get(k).image);
			mList.add(src.get(k).view);
			mList.add(src.get(k).xmlUrl);
		}
/*		
		for(int k=0;k<mList.size();k++){
			Log.d(TAG, "------------Episode to String----------- array : "+k);
			Log.d(TAG, "mList.get(k).title : "+mList.get(k));

		}
	*/	
		return mList;
    }	

	
	private static class EfficientAdapter extends BaseAdapter 
	{
		private LayoutInflater mInflater;
 
		public EfficientAdapter(Context context) 
		{
			mInflater = LayoutInflater.from(context);
 
		}
 
		public int getCount() 
		{
			return castListNumber;
		}
 
		public Object getItem(int position) 
		{
			return position;
		}
 
		public long getItemId(int position) 
		{
			return position;
		}
 
		public View getView(final int position, View convertView, ViewGroup parent) 
		{
			ViewHolder holder;
			if (convertView == null) 
			{
				convertView = mInflater.inflate(R.layout.list, null);
				holder = new ViewHolder();
				holder.album = (ImageView) convertView.findViewById(R.id.imageView1);
				holder.text1 = (TextView) convertView.findViewById(R.id.TextView01);
				holder.text2 = (TextView) convertView.findViewById(R.id.TextView02);
				holder.text3 = (TextView) convertView.findViewById(R.id.TextView03);
				holder.text4 = (TextView) convertView.findViewById(R.id.TextView04); 
				/*
				if(LCDH > 800){
					holder.text1.setTextSize(TypedValue.COMPLEX_UNIT_PX, 38);
					holder.text2.setTextSize(TypedValue.COMPLEX_UNIT_PX, 25);
					holder.text3.setTextSize(TypedValue.COMPLEX_UNIT_PX, 19);
	    		}
*/
				
				convertView.setTag(holder);
			}
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
 
			//Log.d(TAG, "cTitle.get(position) : "+cTitle.get(position));
			//Log.d(TAG, "cMaker.get(position) : "+cMaker.get(position));
			//Log.d(TAG, "cImage.get(position) : "+cImage.get(position));
			
			if(ie.get(position).image.equals("no")){
				//Log.d(TAG, "image no set");
				holder.album.setImageResource(R.drawable.no);
			}else{
		    	File file = new File(ie.get(position).image+"_mini.png");
		    	File file_spare = new File(ie.get(position).image);
				if (file.exists()){
					//Log.d(TAG, "image exist");
//					Bitmap bitmap;
					Bitmap tempBitmap = BitmapFactory.decodeFile(ie.get(position).image+"_mini.png");
					//bitmap = Bitmap.createScaledBitmap(tempBitmap, 80, 80, true);
					holder.album.setImageBitmap(tempBitmap);
				}
				else if (file_spare.exists()){
					//Log.d(TAG, "image exist");
//					Bitmap bitmap;
					Bitmap tempBitmap = BitmapFactory.decodeFile(ie.get(position).image);
					//bitmap = Bitmap.createScaledBitmap(tempBitmap, 80, 80, true);
					holder.album.setImageBitmap(tempBitmap);
				}
				else{
					//Log.d(TAG, "image no exist");
					holder.album.setImageResource(R.drawable.no);
				}
			}
			
			holder.text1.setText(ie.get(position).title);
			holder.text2.setText(ie.get(position).author);
			
			//Log.d(TAG, "length "+cDesc.get(position).length());
			/*
			String desc;
			if(ie.get(position).desc.length() > 70)
				desc = ie.get(position).desc.substring(0, 70) + "...";
			else
				desc = ie.get(position).desc;
			*/
			
			switch(InfoType){
			case INFO_DESC:
				String desc1 = ie.get(position).desc.replaceAll("\n", " ");
				holder.text3.setText(desc1);
				break;
			case INFO_LISTEN_NUM:
				holder.text3.setText(ienum.get(position));
				break;
			}
			
			
			holder.text4.setText(ie.get(position).view);
			
			return convertView;
		}
 
		//http://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/
		
		static class ViewHolder 
		{
			ImageView album;
			TextView text1;
			TextView text2;
			TextView text3;
			TextView text4;
		}
	}
}