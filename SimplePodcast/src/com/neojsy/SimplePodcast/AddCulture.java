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

public class AddCulture extends Activity {

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
	            	if(!Util.isConnected(AddCulture.this)){
	            		Toast.makeText(AddCulture.this, getResources().getString(R.string.msg_networkerror), Toast.LENGTH_SHORT).show();
	            	}
	            	else{
	            		addCast(position);
	            	}
            	}
            	else{
            		Toast.makeText(AddCulture.this, getResources().getString(R.string.msg_notcomwork), Toast.LENGTH_SHORT).show();
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
		progressSave = ProgressDialog.show(AddCulture.this, "", getResources().getString(R.string.msg_SavingCastAdd), true);
    	Thread backGround2 = new Thread() {
    		public void run() {

    			Log.d(TAG, "!! save start");
    			ControlCastListFile cc = new ControlCastListFile(AddCulture.this);
    			addresult = cc.Add2CastFile(ic, xmlURL);
    			Log.d(TAG, "!! save end");

    			mCompleteHandler.sendEmptyMessage(SAVE_END);

    		}
    	};
    	backGround2.start();
	}

	ProgressDialog progressParse;
	private void startprogressParse(String msg){
		progressParse = new ProgressDialog(AddCulture.this);
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

		
		cTitle.add("나는 딴따라다");
		cDesc.add("나는 딴따라다");
		cImage.add(R.drawable.r_ddanddara);
		xml.add("http://old.ddanzi.com/appstream/ddanddara.xml");
	
		cTitle.add("이동진의 빨간책방");
		cDesc.add("이동진 작가 특유의 섬세하고 날카로운 눈으로 잡아낸 책의 진면목을 만나는 공간, 집안 곳곳에 쌓아둔 만권의 책에 관한 이야기");
		cImage.add(R.drawable.r_redbook);
		xml.add("http://hw.libsyn.com/p/d/0/9/d0996a60a3c2ad2c/redbooks.xml?sid=a6f735586d43ce9a3d8534e083e82151&l_sid=36679&l_eid=&l_mid=2977610&expiration=1334693154&hwt=65abefee573077e143c22339b46f5a2d");
		
		cTitle.add("김영하의 책 읽는 시간");
		cDesc.add(" ");
		cImage.add(R.drawable.r_booktime);
		xml.add("http://feeds.feedburner.com/KIMYoungha");	
		
		cTitle.add("아트앤스터디");
		cDesc.add("자꾸만 감춰진 진실을 보게 만드는, 인문학은 “해롭다!”");
		cImage.add(R.drawable.r_artn_logo);
		xml.add("http://www.artnstudy.com/podcast.xml");		
		
		cTitle.add("서정욱 미술토크");
		cDesc.add("바쁘십니까? 혹시 마음의 여유가 필요하진 않으십니까? 미술을 즐기시며, 잠깐 쉬어 가시죠.");
		cImage.add(R.drawable.r_misultalk);
		xml.add("http://sjwgallery.libsyn.com/rss");
		
		cTitle.add("나는 무식(MUSIC)하다");
		cDesc.add("무식한 놈들의 무식(MUSIC)과 엔터테인먼트에 관한 이야기.");
		cImage.add(R.drawable.r_iammusichada);
		xml.add("http://feeds.feedburner.com/musichada");
		
		
		cTitle.add("전진희의 음악일기");
		cDesc.add("안녕하세요 전진희의 음악일기입니다. 제가 보고 느끼는 음악, 미술, 인문학의 매력에 대해 소개하는 방송입니다.");
		cImage.add(R.drawable.r_jh);
		xml.add("http://feeds.feedburner.com/palaisdegala");
		
		cTitle.add("민트 페이퍼");
		cDesc.add("모던 음악 중심의 문화 포털 사이트 민트페이퍼의 팟캐스트. 1주일에 1회 업데이트 되는 민트라디오.");
		cImage.add(R.drawable.r_mintpaper);
		xml.add("http://mintpaper2.cafe24.com/radio/podcast/mintradio.xml");
		
		cTitle.add("Ourlist 아워리스트");
		cDesc.add("각인 각색 Lister와 함께하는 음악 코스요리. 매번 다른 주제의 선곡들로 여러분들을 찾아갑니다.");
		cImage.add(R.drawable.r_ourlist);
		xml.add("http://feeds.feedburner.com/ourlist");
		
		cTitle.add("최진기의 인문학 특강 (동영상)");
		cDesc.add("수능시장의 스타강사! 인문학 강의의 새장에 뛰어들다! 세상에서 가장 쉬운 강의, 세상에서 가장 재밌는 강의.");
		cImage.add(R.drawable.r_inmun);
		xml.add("http://rss.ohmynews.com/rss/podcast_cjk_online_main.xml");
		
		cTitle.add("오마이스쿨 온라인강좌 (동영상)");
		cDesc.add("배워서 남주자! 오마이뉴스가 만드는 '오마이스쿨 온라인강좌'. 조국, 김호기, 우석훈 등이 함께한 오프라인 강좌.");
		cImage.add(R.drawable.r_online_thum);
		xml.add("http://rss.ohmynews.com/rss/podcast_full_online_main.xml");

		cTitle.add("오마이TV 저자와의 대화 (동영상)");
		cDesc.add("'오마이뉴스'가 만드는 유명 저자들과의 특별한 만남!");
		cImage.add(R.drawable.r_ohmytvmanman);
		xml.add("http://rss.ohmynews.com/rss/podcast_authortalk_main.xml");		
		
		cTitle.add("뻔뻔하고 식상한 사진찍기");
		cDesc.add("뻔식사는 사진가 셔터의 달인과 디렉터 쮸띠가 만드는 재미있는 사진이야기입니다.");
		cImage.add(R.drawable.r_bbunphoto);
		xml.add("http://phototip.libsyn.com/rss");

		cTitle.add("우울한 빵집옆 음반가게");
		cDesc.add("쉐이커의 음악과 인생에 대한 자유로운 음악방송!");
		cImage.add(R.drawable.r_shakemusic_1336906721948);
		xml.add("http://www.iblug.com/xml/itunes/shakemusic.xml");
		
		cTitle.add("김지현의 시간산책");
		cDesc.add("음악과 더불어 일상 속 소소한 이야기를 나누는 공간입니다. 바쁜 일상 여유있는 시간산책으로 여러분을 초대합니다.");
		cImage.add(R.drawable.r_jhstellspage);
		xml.add("http://www.iblug.com/xml/itunes/jhstellspage.xml");
		
		cTitle.add("언플러그드 라디오");
		cDesc.add("카페언플러그드의 인디음악 프로젝트! 홍대씬에서 활동하고 있는 인디뮤지션 1팀을 선정하여 그들의 라이브음악과 이야기를 직접 듣는 인디음악 라디오.");
		cImage.add(R.drawable.r_unplugged);
		xml.add("http://pod.ssenhosting.com/rss/unplugged.xml");
		
		cTitle.add("책, 즐겁게 듣자!!");
		cDesc.add(" ");
		cImage.add(R.drawable.r_booktalk);
		xml.add("http://audien.libsyn.com/rss");
		
		cTitle.add("편파음악감상실(페이스북/onesidemusic)");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ppmusic);
		xml.add("http://www.iblug.com/xml/itunes/devinlee.xml");
		
		cTitle.add("영미 문학관");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ymm);
		xml.add("http://www.ebs.co.kr/actions/EncodingMngList?cmd=podcastEpisodeList&program_id=BP0PHPK0000000054");
		
		cTitle.add("고전읽기");
		cDesc.add(" ");
		cImage.add(R.drawable.c_kojeon);
		xml.add("http://www.ebs.co.kr/actions/EncodingMngList?cmd=podcastEpisodeList&program_id=BP0PHPK0000000050");
		

		cTitle.add("TEDTalks 사회와 문화");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ted_cul);
		xml.add("http://feeds.feedburner.com/KoreanPodcastSocietyAndCulture");
		
		cTitle.add("TEDTalks 예술");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ted_art);
		xml.add("http://feeds.feedburner.com/KoreanPodcastArt");
		
		cTitle.add("TEDTalks 기술");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ted_skill);
		xml.add("http://feeds.feedburner.com/KoreanPodcastTechnology");
		
		cTitle.add("TEDTalks 교육");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ted_edu);
		xml.add("http://feeds.feedburner.com/KoreanPodcastEducation");
		
		cTitle.add("TEDTalks 과학과 의료");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ted_sci);
		xml.add("http://feeds.feedburner.com/KoreanPodcastScienceAndMedicine");
		
		cTitle.add("TEDTalks 음악");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ted_music);
		xml.add("http://feeds.feedburner.com/KoreanPodcastMusic");

		cTitle.add("TEDTalks 뉴스와 정치");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ted_new);
		xml.add("http://feeds.feedburner.com/KoreanPodcastNewsAndPolitics");
		
		cTitle.add("TEDTalks 비즈니스");
		cDesc.add(" ");
		cImage.add(R.drawable.ted_busi);
		xml.add("http://feeds.feedburner.com/KoreanPodcastBusiness");
		
		cTitle.add("손미나의 여행사전");
		cDesc.add(" ");
		cImage.add(R.drawable.bbrcast);
		xml.add("http://pod.ssenhosting.com/rss/bbrcast.xml");
		
		cTitle.add("옥상달빛 옥탑라됴");
		cDesc.add("옥상달빛과 함께하는 팟캐스트");
		cImage.add(R.drawable.okdalradio);
		xml.add("http://pod.ssenhosting.com/rss/msbsound/okdalradio.xml");

		cTitle.add("이승욱의 공공상담소");
		cDesc.add("상담과 심리학이 사회적 공공재로 사용되기를 바랍니다");
		cImage.add(R.drawable.imokutoo);
		xml.add("http://pod.ssenhosting.com/rss/imokutoo.xml");
		
		
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