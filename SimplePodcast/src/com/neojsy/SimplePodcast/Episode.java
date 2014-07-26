package com.neojsy.SimplePodcast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

public class Episode extends Activity{
	
	static final String TAG = "SimplePodcast";

	static ArrayList<ItemEpisode> ie = new ArrayList<ItemEpisode>();
	static int castListNumber = 0;
	ListView l1;
	ListAdapter ad;
	
	String datafilename;
	String datafilepath;
	String dataMaker;
	String imagefilename;
	String dataTitle;
	
	String downloadingMaker;
	static String downloadingPercent = " ";
	
	static boolean isDownThisEpi = false;
	static String downfileURL = "none";
	static int tick = 0;;
	
	static boolean isWaitThisEpi = false;
	String[] waitFileMaker;
	static String[] waitFileURL;
	
	static int LCDH;
	
    private BroadcastReceiver completeReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String state = intent.getStringExtra("state");
			String title = intent.getStringExtra("title");
//			String downKB = intent.getStringExtra("bytedown");
//			String totalKB = intent.getStringExtra("bytetotal");
			String url = intent.getStringExtra("url");
			downloadingMaker = intent.getStringExtra("dMaker");
			downloadingPercent = intent.getStringExtra("percent");
			String waitMaker = intent.getStringExtra("waitingMaker");
			String waitURL = intent.getStringExtra("waitingURL");
			
			//Log.d(TAG, "waitFileMaker : "+waitMaker);
			//Log.d(TAG, "waitFileURL : "+waitURL);
			//Log.d(TAG, "completeReceiver bytedown : "+downKB);
			//Log.d(TAG, "completeReceiver bytetotal : "+totalKB);
			
			waitFileMaker = waitMaker.split(">");
			waitFileURL = waitURL.split(">");
			
			isDownThisEpi = false;
			isWaitThisEpi = false;
			if(state.equals("start")){
				downloadingPercent = " ";
				Toast.makeText(context, title+getResources().getString(R.string.msg_downloadstart),Toast.LENGTH_SHORT).show();
			}else if(state.equals("down")){
				boolean listUpdate = false;
				if(!waitMaker.equals(">")){
					for(int h = 0;h<waitFileMaker.length;h++){
						//Log.d(TAG, "waitFileMaker[h] : "+waitFileMaker[h]);
						if(waitFileMaker[h].equals(dataMaker)){
							isWaitThisEpi = true;
							listUpdate = true;
						}
					}
				}
				downfileURL = url;
				if(downloadingMaker.equals(dataMaker)){
					//Log.d(TAG, "download make equal list update!");
					isDownThisEpi = true;
					listUpdate = true;
				}
				if(listUpdate){
					((BaseAdapter) ad).notifyDataSetChanged();
				}
			}else if(state.equals("end")){
				downfileURL = "none";
				if(downloadingMaker.equals(dataMaker)){
					Log.d(TAG, "download make equal list update!");
					((BaseAdapter) ad).notifyDataSetChanged();
				}
				Toast.makeText(context, title+getResources().getString(R.string.msg_downloadfinish),Toast.LENGTH_SHORT).show();
			}else if(state.equals("memory")){
				Toast.makeText(context, getResources().getString(R.string.msg_downloadmemoryerror),Toast.LENGTH_SHORT).show();
			}else if(state.equals("stop")){
				Log.d(TAG, "stop");
				((BaseAdapter) ad).notifyDataSetChanged();
				Toast.makeText(context, getResources().getString(R.string.msg_downcanceled),Toast.LENGTH_SHORT).show();
			}

		}
    	
    };
    
    static boolean nowplaying = false;
    static String nowplayingTitle = null;
    
    private BroadcastReceiver musicReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String state = intent.getStringExtra("state");

			if(state.equals("pos")){
				nowplaying = true;
				nowplayingTitle = intent.getStringExtra("title");
				((BaseAdapter) ad).notifyDataSetChanged();
			}else{
				nowplaying = false;
			}
		}
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		LCDH = Util.getLCDheight(Episode.this);
        Intent gintent = getIntent();
    	datafilename = gintent.getStringExtra("datafilename");
    	datafilepath = gintent.getStringExtra("datafilepath");
    	imagefilename = gintent.getStringExtra("imagefilename");
    	dataMaker = gintent.getStringExtra("maker");
    	dataTitle = gintent.getStringExtra("title");
    	//Log.d(TAG, "datafilepath : "+datafilepath);
    	//Log.d(TAG, "datafilename : "+datafilename);
    	//pos = 0;
    	
		isDownThisEpi = false;
		isWaitThisEpi = false;
    	boolean loadsuccess = false;
    	try {
    		loadsuccess = loadCastList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	setContentView(R.layout.episode);
    	
    	if(loadsuccess){
    		//Log.d(TAG, "loadsuccess");
    		makeList();
    		//l1.setSelection(pos);
    		SetData sData = new SetData(Episode.this);
    		switch(sData.getListPos()){
    		case 1:

    			int pos1 = ie.size() - 1;
    			for(int x=0 ; x<ie.size() ; x++){
    				if(ie.get(x).read.equals("ok")){
    					if(x==0){
    						pos1 = 0;
    					}
    					else{
    						pos1 = x - 1;
    					}
    					break;
    				}
    			}
    			if(pos1 > 0 && pos1 != (ie.size()-1)){
    				pos1--;
    			}
    			
    			l1.setSelection(pos1);
    			break;
    		case 2:
    			int pos2 = 0;
    			for(int x=(ie.size()-1);x>0;x--){
    				if(ie.get(x).read.equals("no")){
    					pos2 = x;
    					break;
    				}
    			}
    			if(pos2 > 0 && pos2 != (ie.size()-1)){
    				pos2--;
    			}
    			
    			l1.setSelection(pos2);
    			break;
    		case 0:
    		default:
    			break;
    		}
    	}
	}
    
    protected void onStart(){
    	super.onStart();
    	nowplaying = false;
        IntentFilter completeFilter = new IntentFilter("com.neojsy.SimplePodcast.DOWN");
        registerReceiver(completeReceiver, completeFilter); 
        
        IntentFilter playFilter = new IntentFilter("com.neojsy.SimplePodcast.MUSIC");
        registerReceiver(musicReceiver, playFilter); 
    }
    
    public void onResume() {
		super.onResume();
		
    	boolean loadsuccess = false;
    	try {
    		loadsuccess = loadCastList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	makeHead();
    	
    	if(loadsuccess){
    		//Log.d(TAG, "loadsuccess");
    		((BaseAdapter) ad).notifyDataSetChanged();
    		//l1.setSelection(pos);
    	}
    	

    }
    
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}
    
	protected void onStop(){
		super.onStop();
		unregisterReceiver(completeReceiver);
		unregisterReceiver(musicReceiver);
	}
	
    private void makeList(){
		l1 = (ListView) findViewById(R.id.ListView01);
		ad = new EfficientAdapter(this);
		l1.setAdapter(ad);
		setListListener();
		
    }
    
    private void makeHead(){
    	if(imagefilename != null){
	    	File file = new File(imagefilename+"_mini.png");
		if (file.exists()){
				//Log.d(TAG, "image exist");
				ImageView album = (ImageView)findViewById(R.id.imageViewh1);
				
				Bitmap tempBitmap = BitmapFactory.decodeFile(imagefilename+"_mini.png");
				album.setImageBitmap(tempBitmap);
			}
    	}
		
		TextView TV_epititle = (TextView)findViewById(R.id.episode_title);
		TextView TV_listeninfo = (TextView)findViewById(R.id.episode_listenInfo);
		
		if(LCDH > 800){
			TV_epititle.setTextSize(TypedValue.COMPLEX_UNIT_PX, 38);
			TV_listeninfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, 25);
		}
		
		
		TV_epititle.setText(dataTitle);
		
		int unlead = 0;
		int complete = 0;
		int ing = 0;
		for(int x=0;x<ie.size();x++){
			if(ie.get(x).read.equals("no")){
				unlead++;
			}
			else if(ie.get(x).read.equals("ok")){
				complete++;
			}
			else if(ie.get(x).read.equals("ing")){
				ing++;
			}			
		}
		
		TV_listeninfo.setText(getResources().getString(R.string.tit_totalepisode)
				+ " : "
				+ ie.size() 
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
    }
 
    @Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		 // TODO Auto-generated method stub
		 menu.add(0, 1, 0, getResources().getString(R.string.mnu_allunread)).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		 menu.add(0, 2, 0, getResources().getString(R.string.mnu_allread)).setIcon(android.R.drawable.ic_menu_view);
		 return true;
	}

    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case 1: //all read
    			questionAllUnread();
    			break;
    		case 2: //all unlead
    			questionAllRead();
    			break;
    	}
    	return true;
    }
    
    private void setListListener(){
        l1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			if (isMp3FileExist(position)){
    				checkFileFormat(position);
    			}else{
    				questionDown(position);
    			}
            	
            	
            }
        });
        
        
        l1.setOnItemLongClickListener(new OnItemLongClickListener() {
        	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            	//Log.d(TAG, "Long position : "+position);
        		//download(position);
        		DrawListPopup(position);
        		return true;
        	}
        });    	
    }
    
    private void questionAllRead(){
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(Episode.this);
		alt_bld.setMessage(getResources().getString(R.string.msg_allread))
				.setCancelable(true)
				//.setIcon(R.drawable.delete)
				.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Action for 'Yes' Button
		    			for(int z=0;z<ie.size();z++){
		      		  		ie.get(z).read = "ok";
		      		  	}
		      		  	saveEpisodeFile();
		      		  	((BaseAdapter) ad).notifyDataSetChanged();
					}
				})
				.setNegativeButton(getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int id) {
						// Action for 'NO' Button
						
					}
				});
			AlertDialog alert = alt_bld.create();
			// Title for AlertDialog
			// alert.setTitle("Title");
			// Icon for AlertDialog
			alert.show();
    	    	
    }

    private void questionAllUnread(){
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(Episode.this);
		alt_bld.setMessage(getResources().getString(R.string.msg_allunread))
				.setCancelable(true)
				//.setIcon(R.drawable.delete)
				.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Action for 'Yes' Button
		    			for(int z=0;z<ie.size();z++){
		      		  		ie.get(z).read = "no";
		      		  	}
		      		  	saveEpisodeFile();
		      		  	((BaseAdapter) ad).notifyDataSetChanged();
					}
				})
				.setNegativeButton(getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int id) {
						// Action for 'NO' Button
						
					}
				});
			AlertDialog alert = alt_bld.create();
			// Title for AlertDialog
			// alert.setTitle("Title");
			// Icon for AlertDialog
			alert.show();
    	    	
    }
    
private void checkFileFormat(int position){
    	String fileName = Util.Url2Mp3name(ie.get(position).mp3url);
    	
    	String lowName = fileName.toLowerCase();
    	if(lowName.endsWith( ".mp3" ) || lowName.endsWith( ".m4a" ))
    	{
    		SetData sData = new SetData(Episode.this);
    		if(sData.getExplayerPlaying()){
    			Intent intentEx = new Intent(Intent.ACTION_VIEW);
    			File fileEx = new File(Environment.getExternalStorageDirectory()+"/SimpleCast/"+fileName);
    			intentEx.setDataAndType(Uri.fromFile(fileEx), "audio/*");
    			startActivity(intentEx);
    			ie.get(position).read = "ok";
    			saveEpisodeFile();
    		}
    		else{
    			questionRead(position);
    		}
    	}
    	else if(lowName.endsWith( ".mp4" )||lowName.endsWith( ".m4v" ))
    	{
			Intent intentVi = new Intent(Intent.ACTION_VIEW);
			File fileEx = new File(Environment.getExternalStorageDirectory()+"/SimpleCast/"+fileName);
			intentVi.setDataAndType(Uri.fromFile(fileEx), "video/*");
			startActivity(intentVi);
			ie.get(position).read = "ok";
			saveEpisodeFile();
    	}
    	else
    	{
    		Toast.makeText(Episode.this, getResources().getString(R.string.episode_notsupportedformat), Toast.LENGTH_SHORT).show();
    	}
    }

    private void questionDown(final int index){
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(Episode.this);
		alt_bld.setMessage(ie.get(index).title + getResources().getString(R.string.msg_downloadCast))
				.setCancelable(true)
				//.setIcon(R.drawable.delete)
				.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Action for 'Yes' Button
						download(index);
					}
				})
				.setNegativeButton(getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int id) {
						// Action for 'NO' Button
						
					}
				});
			AlertDialog alert = alt_bld.create();
			// Title for AlertDialog
			// alert.setTitle("Title");
			// Icon for AlertDialog
			alert.show();
    	    	
    }
    
    private void questionDelete(final int index){
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(Episode.this);
		alt_bld.setMessage(getResources().getString(R.string.msg_deleteFile))
				.setCancelable(true)
				//.setIcon(R.drawable.delete)
				.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Action for 'Yes' Button
						deleteMp3file(index);
					}
				})
				.setNegativeButton(getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int id) {
						// Action for 'NO' Button
						
					}
				});
			AlertDialog alert = alt_bld.create();
			// Title for AlertDialog
			// alert.setTitle("Title");
			// Icon for AlertDialog
			alert.show();
    	    	
    }
    
    private void questionRead(final int index){
    	
		if(isDownThisEpi){
			if(downfileURL.equals(ie.get(index).mp3url)){
				Toast.makeText(Episode.this, getResources().getString(R.string.msg_dontplayfiledownload), Toast.LENGTH_SHORT).show();
				return;
			}
		}
    	
    	
    	Log.d(TAG, "nowplaying : "+nowplaying);
    	Log.d(TAG, "nowplayingTitle : "+nowplayingTitle);
    	Log.d(TAG, "ie.get(position).title : "+ie.get(index).title);
    	if(nowplaying && ie.get(index).title.equals(nowplayingTitle)){
    		Intent intent = new Intent(Episode.this, NewPlayer.class);
    		intent.putExtra("startType", "no_new");
    		startActivity(intent);
    	}else{
		
	    	if(ie.get(index).read.equals("ing")){
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(Episode.this);
			alt_bld.setMessage(getResources().getString(R.string.tit_eyerser) + "\n(" + getResources().getString(R.string.epi_lasttime) + "  " + Util.msInt2TimeString(Util.Str2Int(ie.get(index).mp3PlayedTime))+")")
					.setCancelable(true)
					//.setIcon(R.drawable.delete)
					.setTitle(ie.get(index).title)
					.setPositiveButton(getResources().getString(R.string.tit_eyerok), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// Action for 'Yes' Button
							goPlayer(index, false);
						}
					})
					.setNeutralButton(getResources().getString(R.string.tit_eyernono), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							/* User clicked Something so do some stuff */
							goPlayer(index, true);	
						}
					});/*
					.setNegativeButton(getResources().getString(R.string.tit_eyernono),
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int id) {
									// Action for 'NO' Button
								
						}
					});*/
				AlertDialog alert = alt_bld.create();
				// Title for AlertDialog
				// alert.setTitle("Title");
				// Icon for AlertDialog
				alert.show();
	    
	    	}else{
	    		goPlayer(index, true);
	    	}    	
    	}
    }
    
    
    //int pos = 0;
    private void goPlayer(int position, boolean newRead){
	{
    	
	    	//Log.d(TAG, "Short position : "+position);
	    	Intent intent = new Intent(Episode.this, NewPlayer.class);
	//    	intent.putExtra("downfilename", ie.get(position).title+".mp3");
	    	if(nowplaying)
	    		intent.putExtra("startType", "afternew");
	    	else
	    		intent.putExtra("startType", "new");
	    	
	    	intent.putExtra("stopByCall", "no");
	    	intent.putExtra("downfilename", Util.Url2Mp3name(ie.get(position).mp3url));
			intent.putExtra("title", ie.get(position).title);
			intent.putExtra("subtitle", ie.get(position).summary);
			intent.putExtra("maker", dataMaker);
			intent.putExtra("imagefilename", imagefilename);
			intent.putExtra("datafilename", datafilename);
			intent.putExtra("datafilepath", datafilepath);
			intent.putExtra("position", Util.Int2Str(position));
			//pos = position;
			if(newRead){
				intent.putExtra("startTime", "0");
			}else{
				intent.putExtra("startTime", ie.get(position).mp3PlayedTime);
			}

			startActivity(intent);
    	}
    }
    
    private void DrawListPopup(final int index)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(ie.get(index).title)
       .setCancelable(true)
       .setAdapter(setAdapter(index), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
        	  switch(which){
        	  case 0:
            	  if (isMp3FileExist(index)){
            		  questionDelete(index);
            	  }else{
            		  download(index);
            	  }
        		  break;
        	  case 1:
        		  ie.get(index).read = "ok";
        		  saveEpisodeFile();
        		  ((BaseAdapter) ad).notifyDataSetChanged();
        		  break;
        	  case 2:
        		  ie.get(index).read = "no";
        		  saveEpisodeFile();
        		  ((BaseAdapter) ad).notifyDataSetChanged();
        		  break;
        	  case 3:
        		  for(int z=index;z<ie.size();z++){
        			  ie.get(z).read = "ok";
        		  }
        		  saveEpisodeFile();
        		  ((BaseAdapter) ad).notifyDataSetChanged();
        		  break;
        	  }
        	  


          }
       });
       builder.create().show(); 
    }
    
    public class Custom {
        private String Text1, Text2;
        public Custom(String _Text1, String _Text2) {
          this.Text1 = _Text1;
          this.Text2 = _Text2;
         }
        public Custom(String _Text1) {
          this.Text1 = _Text1;
        }
        public String getText1() {
          return Text1;
        }
        public String getText2() {
          return Text2;
        }
    }
    
    private PopupList setAdapter(int index){

        Vector<Custom> vector = new Vector<Custom>();
        if (isMp3FileExist(index)){
        	vector.add(new Custom(getResources().getString(R.string.tit_deletemp3)));
        }
        else{
        	vector.add(new Custom(getResources().getString(R.string.tit_downloadmp3)));
        }
        vector.add(new Custom(getResources().getString(R.string.tit_markcomplete)));
        vector.add(new Custom(getResources().getString(R.string.tit_markuncomplete)));
        vector.add(new Custom(getResources().getString(R.string.tit_markcompleteUnder)));

        return new PopupList(this, R.layout.popuplist ,vector, this);
   }
    
    private void deleteMp3file(int index){
    	String fileName = Util.Url2Mp3name(ie.get(index).mp3url);
    	
    	File file = new File(Environment.getExternalStorageDirectory()+ "/SimpleCast/" +fileName);
    	
		if (file.exists()){
			//Log.d(TAG, "file exist");
			//return true;
			file.delete();
		}else{
			//return false;
		}
		((BaseAdapter) ad).notifyDataSetChanged();
    }
    
    
    
    private void download(int index){
    
    	Intent intent = new Intent(Episode.this, Download.class);
//		intent.putExtra("downfilename", ie.get(index).title+".mp3");
    	intent.putExtra("downfilename", Util.Url2Mp3name(ie.get(index).mp3url));
		intent.putExtra("downloadUrl", ie.get(index).mp3url);
		intent.putExtra("name", ie.get(index).title);
		intent.putExtra("maker", dataMaker);
		startService(intent);
    	
    }
    
    private static boolean isMp3FileExist(int index){
    	
    	String fileName = Util.Url2Mp3name(ie.get(index).mp3url);
    	
    	File file = new File(Environment.getExternalStorageDirectory()+ "/SimpleCast/" +fileName);
    	//Log.d(TAG, "mp3 file : "+Environment.getExternalStorageDirectory()+ "/SimpleCast/" +fileName);
		if (file.exists()){
			//Log.d(TAG, "file exist");
			return true;
		}else{
			return false;
		}
    }
 
    
	private boolean loadCastList() throws IOException{
    	ArrayList<String> mList = new ArrayList<String>();
    	mList.clear();
		
    	LoadFileToList lf = new LoadFileToList(Episode.this);
    	File file = new File(datafilepath+datafilename);
		if (file.exists()){
			mList = lf.getList(datafilename);
			UpdateCast uc = new UpdateCast(Episode.this);
			ie = uc.Str2Epi(mList);

			castListNumber = ie.size();
			
			return true;	
		}else{
			//cl file not exist
			return false;
		}
	}

	private void saveEpisodeFile(){

		ArrayList<String> mList = new ArrayList<String>();
		mList.clear();
		UpdateCast uc = new UpdateCast(Episode.this);
		mList = uc.Epi2Str(ie);
		LoadFileToList lf = new LoadFileToList(Episode.this);
		lf.saveFileListToFile(datafilename, mList);
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
			ViewHolder2 holder;
			if (convertView == null) 
			{
				convertView = mInflater.inflate(R.layout.epilist, null);
				holder = new ViewHolder2();
				holder.album = (ImageView) convertView.findViewById(R.id.arrow6);
				holder.status = (ImageView) convertView.findViewById(R.id.timebar);
				holder.text1 = (TextView) convertView.findViewById(R.id.TextHead1);
				holder.text2 = (TextView) convertView.findViewById(R.id.TextHead3);
				holder.text3 = (TextView) convertView.findViewById(R.id.TextHead2);
				holder.text4 = (TextView) convertView.findViewById(R.id.Read); 
				holder.text5 = (TextView) convertView.findViewById(R.id.newepi);
				convertView.setTag(holder);
			}
			else 
			{
				holder = (ViewHolder2) convertView.getTag();
			}

			
			if(isDownThisEpi && downfileURL.equals(ie.get(position).mp3url)){
				tick++;
				int step = tick%3;
				if(step == 0){
					holder.album.setImageResource(R.drawable.down1);
				}else if(step == 1){
					holder.album.setImageResource(R.drawable.down2);
				}else{
					holder.album.setImageResource(R.drawable.down3);
				}
				
			}
			else if(nowplaying && ie.get(position).title.equals(nowplayingTitle)){
				//now playing index...
				holder.album.setImageResource(R.drawable.playnoti);
			}
			else{
				if (isMp3FileExist(position)){
					String fileName = Util.Url2Mp3name(ie.get(position).mp3url);
			    	String lowName = fileName.toLowerCase();
			    	if(lowName.endsWith( ".mp3" ) || lowName.endsWith( ".m4a" )){
			    		holder.album.setImageResource(R.drawable.file_mp3);
			    	}
		    		else if(lowName.endsWith( ".mp4" ) || lowName.endsWith( ".m4v" )){
		    			holder.album.setImageResource(R.drawable.file_mp4);
		    		}
		    		else {
		    			holder.album.setImageResource(R.drawable.file_un);
		    		}
				}else{
					holder.album.setImageResource(R.drawable.bookmark_non);
				}
				
				if(isWaitThisEpi){
					Log.d(TAG, "let's find wait file");
					for(int y = 0; y< waitFileURL.length;y++){
						if(waitFileURL[y].equals(ie.get(position).mp3url)){
							holder.album.setImageResource(R.drawable.downw);
							Log.d(TAG, "ok mark wait file");
						}
					}
				}
			}
			
			
			holder.text1.setText(ie.get(position).title);
			/*
			String desc;

			if(ie.get(position).summary.length() > 100)
				desc = ie.get(position).summary.substring(0, 98) + "...";
			else
				desc = ie.get(position).summary;
						
			holder.text2.setText(desc);
			*/
			holder.text2.setText(ie.get(position).summary);
			holder.text3.setText(ie.get(position).pubDate);
			holder.text5.setText(ie.get(position).newepi);
			
			if(isDownThisEpi && downfileURL.equals(ie.get(position).mp3url)){
				holder.text4.setText(downloadingPercent + "%");
				holder.status.setImageResource(R.drawable.timebar);
			}else{
				if(ie.get(position).read.equals("no")){//+""+ie.get(position).mp3FullTime);
					holder.text4.setText(R.string.tit_new);
					holder.status.setImageResource(R.drawable.timebar3);
				}else if(ie.get(position).read.equals("ing")){
					//int pt = Util.Str2Int(ie.get(position).mp3PlayedTime);
					//int ft = Util.Str2Int(ie.get(position).mp3FullTime);
					holder.text4.setText(R.string.tit_not_complete);
					holder.status.setImageResource(R.drawable.timebar2);
				}else if(ie.get(position).read.equals("ok")){// "+""+Util.msInt2TimeString(ft));//int ft = Util.Str2Int(ie.get(position).mp3FullTime);
					holder.text4.setText(R.string.tit_complete);
					holder.status.setImageResource(R.drawable.timebar1);
				} 
		}

			
			return convertView;
		}
 
		//http://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/
		
		static class ViewHolder2 
		{
			ImageView album;
			ImageView status;
			TextView text1;
			TextView text2;
			TextView text3;
			TextView text4;
			TextView text5;
		}
	}
}