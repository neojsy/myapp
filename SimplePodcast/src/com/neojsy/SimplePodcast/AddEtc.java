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

public class AddEtc extends Activity {

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
	            	if(!Util.isConnected(AddEtc.this)){
	            		Toast.makeText(AddEtc.this, getResources().getString(R.string.msg_networkerror), Toast.LENGTH_SHORT).show();
	            	}
	            	else{
	            		addCast(position);
	            	}
            	}
            	else{
            		Toast.makeText(AddEtc.this, getResources().getString(R.string.msg_notcomwork), Toast.LENGTH_SHORT).show();
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
		progressSave = ProgressDialog.show(AddEtc.this, "", getResources().getString(R.string.msg_SavingCastAdd), true);
    	Thread backGround2 = new Thread() {
    		public void run() {

    			Log.d(TAG, "!! save start");
    			ControlCastListFile cc = new ControlCastListFile(AddEtc.this);
    			addresult = cc.Add2CastFile(ic, xmlURL);
    			Log.d(TAG, "!! save end");

    			mCompleteHandler.sendEmptyMessage(SAVE_END);

    		}
    	};
    	backGround2.start();
	}

	ProgressDialog progressParse;
	private void startprogressParse(String msg){
		progressParse = new ProgressDialog(AddEtc.this);
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


		cTitle.add("야구마구토크쇼");
		cDesc.add("");
		cImage.add(R.drawable.r_yagu);
		xml.add("http://www.iblug.com/podcastxml/ya9ma9");
		
		cTitle.add("김대환의 파이트캐스트");
		cDesc.add("Korean MMA Podcast introduced by renowned Korean MMA commentator, Kim Daehwan and his friends in the field offered in Korean only.");
		cImage.add(R.drawable.r_fight);
		xml.add("http://fightcast.libsyn.com/rss");
		
		cTitle.add("원나잇 스탠드");
		cDesc.add("성,연애,사랑 등에 관한 솔직하고 화끈한 이야기");
		cImage.add(R.drawable.r_onenight);
		xml.add("http://jayventura.godohosting.com/onenightstand.xml");
		
		cTitle.add("법륜스님의 즉문즉설");
		cDesc.add("[법륜스님의 즉문즉설] 공식 팟캐스트입니다.");
		cImage.add(R.drawable.r_jmjs);
		xml.add("http://jungto.libsyn.com/rss");
		
		cTitle.add("나는 연애 고수다");
		cDesc.add("이 방송은 high-value 소셜 클럽 ATTROPOLIS에서 제공하는 방송으로 연애에서 고민(짝사랑, 이별, 유혹하기 등)이 있는 사람들에게 명쾌한 해답과 실천방안을 제시해주는 방송입니다.");
		cImage.add(R.drawable.r_gosu);
		xml.add("http://feeds.feedburner.com/attropolis");	
		
		cTitle.add("게임 개발자 랩소디");
		cDesc.add("게임 개발자들의 팟캐스트.");
		cImage.add(R.drawable.r_agebreak);
		xml.add("http://www.iblug.com/podcastxml/agebreak");
		
		cTitle.add("LBC의 닥치고연애");
		cDesc.add(" ");
		cImage.add(R.drawable.r_dacyeon);
		xml.add("http://www.iblug.com/podcastxml/gagblack");
		
		cTitle.add("앱방송 - 재미있는 어플리케이션 동영상 리뷰");
		cDesc.add("앱방송 - 재미있는 어플리케이션 동영상 리뷰");
		cImage.add(R.drawable.r_appsfilm);
		xml.add("http://www.iblug.com/xml/itunes/appsfilm.xml");
		
		cTitle.add("나는 호구다");
		cDesc.add("베타뉴스가 만든 IT 전문 팟케스트, 새롭고 신기한 그래서 뜨거운 관심을 받는 소식들을 재미있게 들려 드립니다.");
		cImage.add(R.drawable.r_hogu);
		xml.add("http://movie.betanews.net/pod_hogu/hogu.xml");
		
		cTitle.add("거인사생");
		cDesc.add("거인사생 님의 팟캐스트 채널에 오신것을 환영합니다!");
		cImage.add(R.drawable.r_lotted);
		xml.add("http://nemo.podics.com/133760804373");
		
		cTitle.add("나는 일반인이다");
		cDesc.add("대한민국에는 나도 살고 있다!!! 국내 유일의 일반인 토크쇼, 나는 일반인이다");
		cImage.add(R.drawable.r_iamnormal_pic);
		xml.add("http://iamnormal.co.kr/programs/iamnormal.xml");
		
		cTitle.add("왓떠뻑");
		cDesc.add("살아가면서 갖게되는 나만의 사소한 불만들을 털어놓는 팟캐스트");
		cImage.add(R.drawable.r_what);
		xml.add("http://jayventura2.godohosting.com/watdepuck.xml");
		
		cTitle.add("주간 서형욱");
		cDesc.add("축구저널리스트 서형욱, 축구와 일상을 말하다");
		cImage.add(R.drawable.e_minariboy);
		xml.add("http://pod.ssenhosting.com/rss/minariboy.xml");
		
		cTitle.add("하하의 OOO");
		cDesc.add(" ");
		cImage.add(R.drawable.e_haha);
		xml.add("http://nemo.podics.com/133466477987");
		
		cTitle.add("게무시");
		cDesc.add("전국의 헤아릴수 없는 게이머들을 위한 정통 게임구타 방송");
		cImage.add(R.drawable.dalhoent);
		xml.add("http://pod.ssenhosting.com/rss/dalhoent/GEMUSI.xml");
		
		
		cTitle.add("최동호의 한국스포츠당");
		cDesc.add("스포츠도 공부해야한다. 색다른 뉴스, 스포츠시사 팟캐스트.");
		cImage.add(R.drawable.sparty);
		xml.add("http://pod.ssenhosting.com/rss/sparty.xml");
		
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