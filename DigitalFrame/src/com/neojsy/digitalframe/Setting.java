package com.neojsy.digitalframe;
import java.util.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

public class Setting extends Activity {
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
		
		cName.add(getResources().getString(R.string.setting_charging));
		cInfo.add(getResources().getString(R.string.setting_charging_info));
		if(sData.getChargingStart()){
			cButt.add(B_ON);
		}else{
			cButt.add(B_OFF);
		}

		cName.add(getResources().getString(R.string.setting_nonoff));
		cInfo.add(getResources().getString(R.string.setting_nonoff_info));
		if(sData.getScreenOff()){
			cButt.add(B_ON);
		}else{
			cButt.add(B_OFF);
		}	
		
		cName.add(getResources().getString(R.string.setting_screensize));
		cInfo.add(getResources().getString(R.string.setting_screensize_info));
		cButt.add(B_NONE);
		
		cName.add(getResources().getString(R.string.setting_interval));
		cInfo.add(getResources().getString(R.string.setting_interval_info));
		cButt.add(B_NONE);
		
		cName.add(getResources().getString(R.string.setting_clock));
		cInfo.add(getResources().getString(R.string.setting_clock_info));
		if(sData.getClock()){
			cButt.add(B_ON);
		}else{
			cButt.add(B_OFF);
		}	
		
		cName.add(getResources().getString(R.string.setting_clock_size));
		cInfo.add(getResources().getString(R.string.setting_clock_size_info));
		cButt.add(B_NONE);
		
		cName.add(getResources().getString(R.string.setting_clock_color));
		cInfo.add(getResources().getString(R.string.setting_clock_color_info));
		cButt.add(B_NONE);
		
		cName.add(getResources().getString(R.string.setting_clock_pos));
		cInfo.add(getResources().getString(R.string.setting_clock_pos_info));
		cButt.add(B_NONE);

		castListNumber = cName.size();
	}
	
	final static int S_CHAGING_START = 0;
	final static int S_NONSCREENOFF = 1;
	final static int S_IMAGESIZE = 2;
	final static int S_INTERVAL = 3;
	final static int S_CLOCK = 4;
	final static int S_CLOCK_SIZE = 5;
	final static int S_CLOCK_COLOR = 6;
	final static int S_CLOCK_POS = 7;
	final static int S_END = 8;
	
	private void handleSetValue(int position){
    	switch(position){
 
    	case S_CLOCK_POS:
    		final CharSequence[] CLOCK_POS = {
    				getResources().getString(R.string.setting_clock_pos_center)
    				, getResources().getString(R.string.setting_clock_pos_lt)
    				, getResources().getString(R.string.setting_clock_pos_rt)
    				, getResources().getString(R.string.setting_clock_pos_lb)
    				, getResources().getString(R.string.setting_clock_pos_rb)
    				};
            AlertDialog.Builder alt_bldCLOCK_POS = new AlertDialog.Builder(this);
            alt_bldCLOCK_POS.setTitle(getResources().getString(R.string.setting_clock_pos));
            alt_bldCLOCK_POS.setSingleChoiceItems(CLOCK_POS, sData.getCPos(), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                
                    sData.setCPos(item);
                    dialog.cancel();
                }
            });
            AlertDialog alertCLOCK_POS = alt_bldCLOCK_POS.create();
            alertCLOCK_POS.show();
    		
    		break;	
    	
    	case S_CLOCK_COLOR:
    		final CharSequence[] CLOCK_COLOR = {
    				getResources().getString(R.string.setting_clock_color_BLACK)
    				, getResources().getString(R.string.setting_clock_color_DKGRAY)
    				, getResources().getString(R.string.setting_clock_color_GRAY)
    				, getResources().getString(R.string.setting_clock_color_LTGRAY)
    				, getResources().getString(R.string.setting_clock_color_WHITE)
    				, getResources().getString(R.string.setting_clock_color_RED)
    				, getResources().getString(R.string.setting_clock_color_GREEN)
    				, getResources().getString(R.string.setting_clock_color_BLUE)
    				, getResources().getString(R.string.setting_clock_color_YELLOW)
    				, getResources().getString(R.string.setting_clock_color_CYAN)
    				, getResources().getString(R.string.setting_clock_color_MAGENTA)
    				};
            AlertDialog.Builder alt_bldCLOCK_COLOR = new AlertDialog.Builder(this);
            alt_bldCLOCK_COLOR.setTitle(getResources().getString(R.string.setting_clock_color));
            alt_bldCLOCK_COLOR.setSingleChoiceItems(CLOCK_COLOR, sData.getCColor(), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                
                    sData.setCColor(item);
                    dialog.cancel();
                }
            });
            AlertDialog alert = alt_bldCLOCK_COLOR.create();
            alert.show();
    		
    		break;	
    	
    	case S_CLOCK_SIZE:
    		final CharSequence[] CLOCK_SIZE = {
    				getResources().getString(R.string.setting_clock_size_1)
    				, getResources().getString(R.string.setting_clock_size_2)
    				, getResources().getString(R.string.setting_clock_size_3)
    				, getResources().getString(R.string.setting_clock_size_4)
    				, getResources().getString(R.string.setting_clock_size_5)
    				};
            AlertDialog.Builder alt_bldCLOCK_SIZE = new AlertDialog.Builder(this);
            alt_bldCLOCK_SIZE.setTitle(getResources().getString(R.string.setting_clock_size));
            alt_bldCLOCK_SIZE.setSingleChoiceItems(CLOCK_SIZE, sData.getCSize(), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                
                    sData.setCSize(item);
                    dialog.cancel();
                }
            });
            AlertDialog alertCLOCK_SIZE = alt_bldCLOCK_SIZE.create();
            alertCLOCK_SIZE.show();
    		
    		break;	
    	
    	
    	case S_CLOCK:
    		if(sData.getClock()){
    			sData.setClock(false);
    			cButt.set(S_CLOCK, B_OFF);
    		}else{
    			sData.setClock(true);
    			cButt.set(S_CLOCK, B_ON);
    		}
    		break;
    	
    	case S_CHAGING_START:
    		if(sData.getChargingStart()){
    			sData.setChargingStart(false);
    			cButt.set(S_CHAGING_START, B_OFF);
    		}else{
    			sData.setChargingStart(true);
    			cButt.set(S_CHAGING_START, B_ON);
    		}
    		break;
    		
    	case S_NONSCREENOFF:
    		if(sData.getScreenOff()){
    			sData.setScreenOff(false);
    			cButt.set(S_NONSCREENOFF, B_OFF);
    		}else{
    			sData.setScreenOff(true);
    			cButt.set(S_NONSCREENOFF, B_ON);
    		}
    		break;
    		
    	case S_INTERVAL:
    		final CharSequence[] INTERVAL = {
    				getResources().getString(R.string.setting_interval_3s)
    				, getResources().getString(R.string.setting_interval_5s)
    				, getResources().getString(R.string.setting_interval_10s)
    				, getResources().getString(R.string.setting_interval_15s)
    				, getResources().getString(R.string.setting_interval_30s)
    				, getResources().getString(R.string.setting_interval_60s)
    				};
            AlertDialog.Builder alt_bldINTERVAL = new AlertDialog.Builder(this);
            alt_bldINTERVAL.setTitle(getResources().getString(R.string.setting_interval));
            alt_bldINTERVAL.setSingleChoiceItems(INTERVAL, sData.getInterval(), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                
                    sData.setInterval(item);
                    dialog.cancel();
                }
            });
            AlertDialog alertINTERVAL = alt_bldINTERVAL.create();
            alertINTERVAL.show();
    		
    		break;	
 
    	case S_IMAGESIZE:
    		final CharSequence[] PhoneModels2 = {
    				getResources().getString(R.string.setting_screensize_imagefull)
    				, getResources().getString(R.string.setting_screensize_screenfull)
    				};
            AlertDialog.Builder alt_bld_cast_info = new AlertDialog.Builder(this);
            alt_bld_cast_info.setTitle(getResources().getString(R.string.setting_screensize));
            alt_bld_cast_info.setSingleChoiceItems(PhoneModels2, sData.getScreenSize(), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                
                    sData.setScreenSize(item);
                    dialog.cancel();
                }
            });
            AlertDialog alertCastInfo = alt_bld_cast_info.create();
            alertCastInfo.show();
    		
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
 
			
			String buttonImage = cButt.get(position);
			if(buttonImage.equals(B_ON)){
				holder.album.setImageResource(R.drawable.seton);
			}else if(buttonImage.equals(B_OFF)){
				holder.album.setImageResource(R.drawable.setoff);
			}else{
				holder.album.setImageResource(R.drawable.arrow);
			}

			holder.text1.setText(cName.get(position));
			holder.text3.setText(cInfo.get(position));

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

