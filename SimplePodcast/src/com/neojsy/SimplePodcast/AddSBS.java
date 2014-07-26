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

public class AddSBS extends Activity {

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
	            	if(!Util.isConnected(AddSBS.this)){
	            		Toast.makeText(AddSBS.this, getResources().getString(R.string.msg_networkerror), Toast.LENGTH_SHORT).show();
	            	}
	            	else{
	            		addCast(position);
	            	}
            	}
            	else{
            		Toast.makeText(AddSBS.this, getResources().getString(R.string.msg_notcomwork), Toast.LENGTH_SHORT).show();
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
		progressSave = ProgressDialog.show(AddSBS.this, "", getResources().getString(R.string.msg_SavingCastAdd), true);
    	Thread backGround2 = new Thread() {
    		public void run() {

    			Log.d(TAG, "!! save start");
    			ControlCastListFile cc = new ControlCastListFile(AddSBS.this);
    			addresult = cc.Add2CastFile(ic, xmlURL);
    			Log.d(TAG, "!! save end");

    			mCompleteHandler.sendEmptyMessage(SAVE_END);

    		}
    	};
    	backGround2.start();
	}

	ProgressDialog progressParse;
	private void startprogressParse(String msg){
		progressParse = new ProgressDialog(AddSBS.this);
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
		
		cTitle.add("정선희의 오늘 같은 밤");
		cDesc.add("SBS 파워FM AM 00:00 ~ 02:00");
		cImage.add(R.drawable.sbs_mza);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000349378A.xml");
		
		cTitle.add("김형준의 뮤직하이");
		cDesc.add("SBS 파워FM 02:00 ~ 03:00");
		cImage.add(R.drawable.sbs_mza_1629696);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000328480.xml");
		
		cTitle.add("이숙영의 파워 FM");
		cDesc.add("SBS 파워FM 07:00 ~ 09:00 ");
		cImage.add(R.drawable.sbs_powerfm);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000010343.xml");
		
		cTitle.add("아름다운 이 아침 김창완");
		cDesc.add("SBS 파워FM 09:00 ~ 11:00 ");
		cImage.add(R.drawable.sbs_morning);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000010355.xml");
		
		cTitle.add("공형진의 씨네타운");
		cDesc.add("SBS 파워FM 11:00 ~ 12:00 ");
		cImage.add(R.drawable.sbs_cine);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000321755.xml");
		
		cTitle.add("최화정의 파워타임");
		cDesc.add("SBS 파워FM 12:00 ~ 14:00");
		cImage.add(R.drawable.sbs_powertime);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000010346.xml");
		
		cTitle.add("김창렬의 올드스쿨");
		cDesc.add("SBS 파워FM 16:00 ~ 18:00 ");
		cImage.add(R.drawable.sbs_oldschool);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000329545.xml");
		
		cTitle.add("김영철의 Fun Fun today");
		cDesc.add("SBS 파워FM 06:00 ~ 07:00 ");
		cImage.add(R.drawable.sbs_pop);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000351036.xml");
		
		cTitle.add("박소현의 러브게임");
		cDesc.add("SBS 파워FM 18:00 ~ 20:00 ");
		cImage.add(R.drawable.sbs_lovegame);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000336099.xml");
		
		cTitle.add("붐의 영스트리트");
		cDesc.add("SBS 파워FM 20:00 ~ 22:00 ");
		cImage.add(R.drawable.sbs_young);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000352536.xml");
		
		cTitle.add("씨네타운 나인틴");
		cDesc.add("풍문으로 듣는 방송!");
		cImage.add(R.drawable.mza_4128);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000365040.xml");
		
		cTitle.add("장기하의 대단한 라디오");
		cDesc.add("SBS 파워FM 22:00 ~ 23:59");
		cImage.add(R.drawable.sbs_jangkiha);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000363536.xml");
		
		cTitle.add("SBS 전망대");
		cDesc.add("SBS 러브FM 주중 07:10 ~ 08:00");
		cImage.add(R.drawable.sbs_jeonmang);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000337960.xml");
		
		cTitle.add("최혜림의 책하고 놀자");
		cDesc.add("SBS 러브FM 토,일 06:05 ~ 07:00");
		cImage.add(R.drawable.sbs_book);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000328499.xml");
		
		cTitle.add("SBS 라디오 스페셜");
		cDesc.add("SBS 러브FM 09:00 ~ 10:00");
		cImage.add(R.drawable.sbs_special);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000372536.xml");
		
		cTitle.add("김지선,김일중의 세상을 만나자");
		cDesc.add("SBS 러브FM 09:05 ~ 12:00 ");
		cImage.add(R.drawable.sbs_manna);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000337995.xml");
		
		cTitle.add("박영진, 박지선의 명랑특급");
		cDesc.add("SBS 러브FM 16:05 ~ 18:00");
		cImage.add(R.drawable.sbs_jisun);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000364036.xml");
		
		cTitle.add("희망사항 변진섭입니다");
		cDesc.add("SBS 러브FM 14:20 ~ 16:00");
		cImage.add(R.drawable.sbs_byun);
		xml.add("http://wizard2.sbs.co.kr/w3/podcast/V0000349388.xml");
	
		
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