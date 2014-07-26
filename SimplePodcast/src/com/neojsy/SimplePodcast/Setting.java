package com.neojsy.SimplePodcast;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Setting extends Activity {
	static final String TAG = "SimplePodcast";
	
	ListView l1;
	ListAdapter ad;
	
    public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		initSetValue();
		setContentView(R.layout.setting);
		
		l1 = (ListView) findViewById(R.id.setListView01);
		ad = new EfficientAdapter(this);
		l1.setAdapter(ad);
		setListListener();
	}
    
    public void onResume() {
		super.onResume();

    }	
    
	static ArrayList<String> cName = new ArrayList<String>();
	static ArrayList<String> cInfo = new ArrayList<String>();
	static ArrayList<String> cButt = new ArrayList<String>(); 
	SetData sData = new SetData(Setting.this);
	

    
	final static String B_ON = "bon";
	final static String B_OFF = "boff";
	final static String B_NONE = "bnone";
	
	private void initSetValue(){
		cName.clear();
		cInfo.clear();
		cButt.clear();
		
		cName.add(getResources().getString(R.string.setting_externalplayeruse));
		cInfo.add(getResources().getString(R.string.setting_externalplayeruseinfo));
		if(sData.getExplayerPlaying()){
			cButt.add(B_ON);
		}else{
			cButt.add(B_OFF);
		}

		cName.add(getResources().getString(R.string.setting_autodeletefile));
		cInfo.add(getResources().getString(R.string.setting_autodeletefileinfo));
		if(sData.getEndFileDelete()){
			cButt.add(B_ON);
		}else{
			cButt.add(B_OFF);
		}	
		
		cName.add(getResources().getString(R.string.setting_seektime));
		cInfo.add(getResources().getString(R.string.setting_seektimeinfo));
		cButt.add(B_NONE);
		
		cName.add(getResources().getString(R.string.setting_castinfo));
		cInfo.add(getResources().getString(R.string.setting_castinfoinfo));
		cButt.add(B_NONE);

		cName.add(getResources().getString(R.string.setting_listpos));
		cInfo.add(getResources().getString(R.string.setting_listposinfo));
		cButt.add(B_NONE);

		/*
		cName.add("Korea Use?");
		cInfo.add("Korea Use this app??");
		if(sData.getUseKorea()){
			cButt.add(B_ON);
		}else{
			cButt.add(B_OFF);
		}
		*/
		
		castListNumber = cName.size();
	}
	
	final static int S_EX_PLAYER_EXE = 0;
	final static int S_DEL_READ_FILE = 1;
	final static int S_SET_SEEK_TIME = 2;
	final static int S_CAST_INFO = 3;
	final static int S_LIST_POS = 4;
	final static int S_USE_KOERA = 5;
	final static int S_END = 6;
	
	private void handleSetValue(int position){
    	switch(position){
    	case S_EX_PLAYER_EXE:
    		if(sData.getExplayerPlaying()){
    			sData.setExplayerPlaying(false);
    			cButt.set(S_EX_PLAYER_EXE, B_OFF);
    		}else{
    			sData.setExplayerPlaying(true);
    			cButt.set(S_EX_PLAYER_EXE, B_ON);
    		}
    		break;
    		
    	case S_DEL_READ_FILE:
    		if(sData.getEndFileDelete()){
    			sData.setEndFileDelete(false);
    			cButt.set(S_DEL_READ_FILE, B_OFF);
    		}else{
    			sData.setEndFileDelete(true);
    			cButt.set(S_DEL_READ_FILE, B_ON);
    		}    		
    		break;

    	case S_SET_SEEK_TIME:
    		final CharSequence[] PhoneModels = {
    				getResources().getString(R.string.setting_5s)
    				, getResources().getString(R.string.setting_15s)
    				, getResources().getString(R.string.setting_30s)
    				, getResources().getString(R.string.setting_1m)
    				, getResources().getString(R.string.setting_2m)
    				, getResources().getString(R.string.setting_5m)
    				};
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
            alt_bld.setTitle(getResources().getString(R.string.setting_seektime));
            alt_bld.setSingleChoiceItems(PhoneModels, sData.getSeektime(), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                
                    sData.setSeektime(item);
                    dialog.cancel();
                }
            });
            AlertDialog alert = alt_bld.create();
            alert.show();
    		
    		break;	
 
    	case S_CAST_INFO:
    		final CharSequence[] PhoneModels2 = {
    				getResources().getString(R.string.setting_epiinfo)
    				, getResources().getString(R.string.setting_listeninfo)
    				};
            AlertDialog.Builder alt_bld_cast_info = new AlertDialog.Builder(this);
            alt_bld_cast_info.setTitle(getResources().getString(R.string.setting_castinfo));
            alt_bld_cast_info.setSingleChoiceItems(PhoneModels2, sData.getCastInfo(), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                
                    sData.setCastInfo(item);
                    dialog.cancel();
                }
            });
            AlertDialog alertCastInfo = alt_bld_cast_info.create();
            alertCastInfo.show();
    		
    		break;	    		
    		
    	case S_LIST_POS:
    		final CharSequence[] ListPosOption = {
    				getResources().getString(R.string.setting_list_def)
    				,getResources().getString(R.string.setting_list_first)
    				,getResources().getString(R.string.setting_list_last)
    				};
            AlertDialog.Builder alt_bld_cast_info3 = new AlertDialog.Builder(this);
            alt_bld_cast_info3.setTitle(getResources().getString(R.string.setting_listpos));
            alt_bld_cast_info3.setSingleChoiceItems(ListPosOption, sData.getListPos(), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                
                    sData.setListPos(item);
                    dialog.cancel();
                }
            });
            AlertDialog alertCastInfo3 = alt_bld_cast_info3.create();
            alertCastInfo3.show();    		
    		break;
    		
    	case S_USE_KOERA:
    		if(sData.getUseKorea()){
    			sData.setUseKorea(false);
    			cButt.set(S_USE_KOERA, B_OFF);
    		}else{
    			sData.setUseKorea(true);
    			cButt.set(S_USE_KOERA, B_ON);
    		}    		
    		break;    		
    		
    	}
    	((BaseAdapter) ad).notifyDataSetChanged();		
	}
    
    private void setListListener(){
        l1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	//Log.d(TAG, "Short position : "+position);
            	handleSetValue(position);
            }
        });
    }
    
    static int castListNumber = 0;
    
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
				convertView = mInflater.inflate(R.layout.listsetting, null);
				holder = new ViewHolder();
				holder.album = (ImageView) convertView.findViewById(R.id.Setbutton);
				holder.text1 = (TextView) convertView.findViewById(R.id.SetTitle);
				holder.text3 = (TextView) convertView.findViewById(R.id.SetInfo);
 
				convertView.setTag(holder);
			}
			else 
			{
				holder = (ViewHolder) convertView.getTag();
			}
 
			Log.d(TAG, "setting number is "+castListNumber);
			for(int i = 0; i< cName.size() ; i++){
				Log.d(TAG, "setting number is "+cName.get(i));
				Log.d(TAG, "cInfo number is "+cInfo.get(i));
				Log.d(TAG, "cButt number is "+cButt.get(i));
			}
			
			String buttonImage = cButt.get(position);
			if(buttonImage.equals(B_ON)){
				holder.album.setImageResource(R.drawable.seton);
			}else if(buttonImage.equals(B_OFF)){
				holder.album.setImageResource(R.drawable.setoff);
			}else{
				holder.album.setImageResource(R.drawable.arrow);
			}
			Log.d(TAG, "setting number is 1"+castListNumber);
			holder.text1.setText(cName.get(position));
			holder.text3.setText(cInfo.get(position));
			Log.d(TAG, "setting number is 2"+castListNumber);
			return convertView;
		}
 
		//http://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/
		
		static class ViewHolder 
		{
			ImageView album;
			TextView text1;
			TextView text3;
		}
	}
	
	
}

