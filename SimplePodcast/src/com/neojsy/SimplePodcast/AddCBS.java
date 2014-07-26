package com.neojsy.SimplePodcast;

import java.util.ArrayList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddCBS extends Activity {

	static final String TAG = "SimplePodcast";

	static ArrayList<String> cTitle = new ArrayList<String>();
	static ArrayList<Integer> cImage = new ArrayList<Integer>();
	static ArrayList<String> xml = new ArrayList<String>();
	static ArrayList<String> cDesc = new ArrayList<String>();
	static int castListNumber = 0;

	String xmlURL;
	ItemCast ic;

	boolean addresult = true;

	String AddGr = null;

	int TStep;
	final int T_GO = 1;
	final int T_CANCEL = 2;

	boolean nowParsing = false;

	final int PARSE_END = 1;
	final int SAVE_END = 2;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addfamouscast);

		Intent gintent = getIntent();
		AddGr = gintent.getStringExtra("group");
		
    	loadCastList();

		ListView l1 = (ListView) findViewById(R.id.ListView01);
		l1.setAdapter(new EfficientAdapter(this));

        l1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	//Log.d(TAG, "position : "+position);
            	if(!nowParsing){
	            	if(!Util.isConnected(AddCBS.this)){
	            		Toast.makeText(AddCBS.this, getResources().getString(R.string.msg_networkerror), Toast.LENGTH_SHORT).show();
	            	}
	            	else{
	            		addCast(position);
	            	}
            	}
            	else{
            		Toast.makeText(AddCBS.this, getResources().getString(R.string.msg_notcomwork), Toast.LENGTH_SHORT).show();
            	}
            }
        });
	}


	public void onResume() {
		super.onResume();

	}

	private void addCast(int position){
    	xmlURL = xml.get(position);
    	startprogressParse(cTitle.get(position)+getResources().getString(R.string.msg_loadingCastAdd));
    	TStep = T_GO;
    	nowParsing = false;
    	Thread backGround1 = new Thread() {
    		public void run() {
            	ic = new ItemCast();
    			XmlParser xp = new XmlParser();

    			if(TStep == T_GO){
    				Log.d(TAG, "!! parce start");
    				nowParsing = true;
    				try {
						ic = xp.getCast(xmlURL);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				nowParsing = false;
    				Log.d(TAG, "!! parce end");
    			}

    			mCompleteHandler.sendEmptyMessage(PARSE_END);
    		}
    	};
    	backGround1.start();
	}

	private void saveCast(){
		progressSave = ProgressDialog.show(AddCBS.this, "", getResources().getString(R.string.msg_SavingCastAdd), true);
    	Thread backGround2 = new Thread() {
    		public void run() {

    			Log.d(TAG, "!! save start");
    			ControlCastListFile cc = new ControlCastListFile(AddCBS.this);
    			addresult = cc.Add2CastFile(ic, xmlURL);
    			Log.d(TAG, "!! save end");

    			mCompleteHandler.sendEmptyMessage(SAVE_END);

    		}
    	};
    	backGround2.start();
	}

	ProgressDialog progressParse;
	private void startprogressParse(String msg){
		progressParse = new ProgressDialog(AddCBS.this);
		progressParse.setMessage(msg);
		progressParse.setOnCancelListener(new OnCancelListener() {
		    public void onCancel(DialogInterface dialog) {
		    	Log.d(TAG, "!! Cancel");
		    	TStep = T_CANCEL;
		    }
		   });
		progressParse.show();
	}

	ProgressDialog progressSave;

    public Handler mCompleteHandler = new Handler(){
    	public void handleMessage(Message msg){
    		if(msg.what == PARSE_END){
    			Log.d(TAG, "PARSE_END");
    			if(TStep == T_GO){
    				progressParse.dismiss();
    				saveCast();
    			}
    		}
    		if(msg.what == SAVE_END){
    			Log.d(TAG, "SAVE_END");
    			progressSave.dismiss();
    			finishParse();
    		}
    	}
    };

    private void finishParse(){
    	if(addresult){

    	}else{
    		Toast.makeText(this, getResources().getString(R.string.msg_duplicateCast),Toast.LENGTH_SHORT).show();
    	}
    	finish();
    }


	private void loadCastList(){
		cTitle.clear();
		cImage.clear();
		cDesc.clear();
		xml.clear();

		

		cTitle.add("시사자키 정관용입니다");
		cDesc.add("CBS Radio 표준FM 98.1MHz 월~금 18:00~20:00");
		cImage.add(R.drawable.cbs_j_si);
		xml.add("http://1.11.71.236/podcast/sisa/sisa.xml");
		
		cTitle.add("김미화의 여러분");
		cDesc.add("[ CBS Radio 표준 FM 98.1MHz ] '친절한 미화씨'의 전방위 시사토크");
		cImage.add(R.drawable.cbs_kmh_everyone);
		xml.add("http://1.11.71.236/podcast/k_everyone/k_everyone.xml");
		
		cTitle.add("변상욱 기자수첩");
		cDesc.add("CBS Radio 표준 FM 98.1MHz");
		cImage.add(R.drawable.cbs_newsshow_journal);
		xml.add("http://1.11.71.236/podcast/newsshow_journal/newsshow_journal.xml");
		
		cTitle.add("김현정의 뉴스 쇼");
		cDesc.add("CBS Radio 표준FM 98.1MHz 월~금 07:00~09:00");
		cImage.add(R.drawable.cbs_show);
		xml.add("http://1.11.71.236/podcast/newshow/newshow.xml");
		
		cTitle.add("신지혜의 영화음악");
		cDesc.add("CBS Radio 음악FM 93.9MHz 매일 11:00 ~ 12:00");
		cImage.add(R.drawable.cbs_cinemusic);
		xml.add("http://1.11.71.236/podcast/cinemusic/cinemusic.xml");
		
		cTitle.add("아름다운 당신에게 김석훈입니다");
		cDesc.add("CBS Radio 음악FM 93.9MHz 매일 09:00 ~ 11:00");
		cImage.add(R.drawable.cbs_kim);
		xml.add("http://1.11.71.236/podcast/kdk_adang/kdk_adang.xml");
		
		cTitle.add("배미향의 저녁스케치");
		cDesc.add("CBS Radio 음악FM 93.9MHz 매일 18:00~20:00");
		cImage.add(R.drawable.cbs_photo);
		xml.add("http://1.11.71.236/podcast/sketch/sketch.xml");
		
		cTitle.add("좋은 아침 김윤주입니다");
		cDesc.add("");
		cImage.add(R.drawable.cbs_goodmo);
		xml.add("http://1.11.71.236/podcast/goodmorning/goodmorning.xml");
		
		cTitle.add("손숙, 한대수의 행복의 나라로");
		cDesc.add("");
		cImage.add(R.drawable.cbs_happy);
		xml.add("http://1.11.71.236/podcast/happynara/happynara.xml");
		
		cTitle.add("FM POPS 한동준입니다");
		cDesc.add("");
		cImage.add(R.drawable.cbs_han);
		xml.add("http://1.11.71.236/podcast/fmpops/fmpops.xml");
		
		cTitle.add("오미희의 행복한 동행");
		cDesc.add("");
		cImage.add(R.drawable.cbs_oh);
		xml.add("http://1.11.71.236/podcast/happydongheang/happydongheang.xml");
		
		
		castListNumber = cTitle.size();


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
				convertView = mInflater.inflate(R.layout.listnormal, null);
				holder = new ViewHolder();
				holder.album = (ImageView) convertView.findViewById(R.id.imageView1);
				holder.text1 = (TextView) convertView.findViewById(R.id.TextView01);
				holder.text3 = (TextView) convertView.findViewById(R.id.TextView03);

				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}

			holder.album.setImageResource(cImage.get(position));
			holder.text1.setText(cTitle.get(position));
			holder.text3.setText(cDesc.get(position));

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