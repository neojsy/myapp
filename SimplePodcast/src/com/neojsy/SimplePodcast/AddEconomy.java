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

public class AddEconomy extends Activity {

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
	            	if(!Util.isConnected(AddEconomy.this)){
	            		Toast.makeText(AddEconomy.this, getResources().getString(R.string.msg_networkerror), Toast.LENGTH_SHORT).show();
	            	}
	            	else{
	            		addCast(position);
	            	}
            	}
            	else{
            		Toast.makeText(AddEconomy.this, getResources().getString(R.string.msg_notcomwork), Toast.LENGTH_SHORT).show();
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
		progressSave = ProgressDialog.show(AddEconomy.this, "", getResources().getString(R.string.msg_SavingCastAdd), true);
    	Thread backGround2 = new Thread() {
    		public void run() {

    			Log.d(TAG, "!! save start");
    			ControlCastListFile cc = new ControlCastListFile(AddEconomy.this);
    			addresult = cc.Add2CastFile(ic, xmlURL);
    			Log.d(TAG, "!! save end");

    			mCompleteHandler.sendEmptyMessage(SAVE_END);

    		}
    	};
    	backGround2.start();
	}

	ProgressDialog progressParse;
	private void startprogressParse(String msg){
		progressParse = new ProgressDialog(AddEconomy.this);
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


		cTitle.add("나는꼽사리다");
		cDesc.add("나는꼼수다 경제편.");
		cImage.add(R.drawable.r_nksr);
		xml.add("http://old.ddanzi.com/appstream/ggobsari.xml");
		
		cTitle.add("장하준 교수의 그들이 말하지 않는 23가지");
		cDesc.add("세계적인 경제학자이자 로 주목을 받았던 장하준 교수가 들려주는 자본주의 이야기그들이 말하지 않는 23가지.");
		cImage.add(R.drawable.r_janghajun);
		xml.add("http://nemo.podics.com/131113737488");
		
		cTitle.add("오마이스쿨 경제학 특강");
		cDesc.add("배워서 남주자! 오마이뉴스가 만드는 '오마이스쿨 경제학 특강'. 김상조, 우석훈, 홍기빈 등이 함께한 오프라인 강좌.");
		cImage.add(R.drawable.r_ec_con);
		xml.add("http://rss.ohmynews.com/rss/podcast_EC_online_main.xml");
		
		cTitle.add("벤처야설");
		cDesc.add("벤처에 대한 솔직 담백한 이야기, 그리고 은밀한 뒷이야기까지~ 섹시한 벤처야설");
		cImage.add(R.drawable.r_venture_story_3);
		xml.add("http://awsmovie.cafe24.com/venture_s/venture_story.xml");

		cTitle.add("대한민국직업이야기 잡수다");
		cDesc.add("대한민국 온갖 직업의 실체 파헤치기. 품격없는 안회장과 척척박사의 토크쇼.");
		cImage.add(R.drawable.r_jobsooda);
		xml.add("http://feeds.feedburner.com/libsyn/jobsooda");
		
		cTitle.add("김광수 소장의 진짜 경제이야기");
		cDesc.add("김광수경제연구소 김광수 소장의 여러 강연과 인터뷰 녹음파일입니다.");
		cImage.add(R.drawable.r_seminar);
		xml.add("http://podcast.kseri.net/kseri/interview_n_seminar.xml");
		
		cTitle.add("잉글리쉬 인 코리언");
		cDesc.add("영어 고민, 원어민과 함께 한국말로 풀어 봅시다!");
		cImage.add(R.drawable.r_eng);
		xml.add("http://englishinkorean.com/?feed=podcast");	
		
		cTitle.add("스피킹맥스 영어공식 (동영상)");
		cDesc.add("2011년 영어회화 베스트 셀러.");
		cImage.add(R.drawable.r_max);
		xml.add("http://www.speakingmax.com/podcast/maxpodcast.php");	
		
		cTitle.add("5분 영어회화_Make My Day");
		cDesc.add("화장실 5분, 커피 브레이크 5분, 지하철 기다리면서 5분. 이렇게 매일 5분씩만 투자해 보는 영어.");
		cImage.add(R.drawable.r_5mineng);
		xml.add("http://nemo.podics.com/126697529249");	

		cTitle.add("닥치고영어");
		cDesc.add("닥치고영어-4시간후 말문트임을 경험하게 됨");
		cImage.add(R.drawable.r_dakeng);
		xml.add("http://www.iblug.com/xml/itunes/johnykim.xml");
		
		cTitle.add("CNN Student News (동영상)");
		cDesc.add("CNN Student News utilizes CNN's worldwide resources");
		cImage.add(R.drawable.r_studentnews1);
		xml.add("http://rss.cnn.com/services/podcasting/studentnews/rss.xml");
		/*
		cTitle.add("TEDTalks (hd) (동영상)");
		cDesc.add("TED is a nonprofit devoted to Ideas Worth Spreading.");
		cImage.add(R.drawable.r_tedtalks);
		xml.add("http://feeds.feedburner.com/TedtalksHD");		
		*/
		cTitle.add("부동산경매이야기");
		cDesc.add("알면 살아가는데 피가 되고 살이 되는 부동산경매이야기. 어렵지도 쉽지도 않은 부동산경매세계를 토크쇼방식으로 전달합니다.");
		cImage.add(R.drawable.r_auction);
		xml.add("http://feeds.feedburner.com/libsyn/TjyK");	
		
		cTitle.add("일빵빵 스토리가 있는 영어회화");
		cDesc.add("원어민과 실제 회화 하기 전에 미리 실제 회화 표현, 발음 요령, 듣기등을 대본을 통해 공부하는 강의입니다");
		cImage.add(R.drawable.r_story);
		xml.add("http://www.iblug.com/podcastxml/ilbangbang");	
		
		cTitle.add("컬투! 심통사연");
		cDesc.add("컬투가 들려주는 재미난 사연! 그리고 건강보험심사평가원 통계로 알아보는 건강이야기");
		cImage.add(R.drawable.e_yeshira);
		xml.add("http://pod.ssenhosting.com/rss/yeshira.xml");	
		
		cTitle.add("구성애의 아우성");
		cDesc.add("건강한 성문화를 위해 구성애가 드디어 나섰다. ");
		cImage.add(R.drawable.ausung);
		xml.add("http://www.aoosung.com/podcast/aoosung.xml");	
	
		cTitle.add("과학이 빛나는 밤에");
		cDesc.add("가급적 매주 금요일 밤 업데이트. 트위터,네이버카페 과빛밤");
		cImage.add(R.drawable.bec78aa2_m);
		xml.add("http://feeds.feedburner.com/twinklescience");	
		
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