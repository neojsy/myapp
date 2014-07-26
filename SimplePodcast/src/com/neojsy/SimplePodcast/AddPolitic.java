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

public class AddPolitic extends Activity {

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
	            	if(!Util.isConnected(AddPolitic.this)){
	            		Toast.makeText(AddPolitic.this, getResources().getString(R.string.msg_networkerror), Toast.LENGTH_SHORT).show();
	            	}
	            	else{
	            		addCast(position);
	            	}
            	}
            	else{
            		Toast.makeText(AddPolitic.this, getResources().getString(R.string.msg_notcomwork), Toast.LENGTH_SHORT).show();
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
		progressSave = ProgressDialog.show(AddPolitic.this, "", getResources().getString(R.string.msg_SavingCastAdd), true);
    	Thread backGround2 = new Thread() {
    		public void run() {

    			Log.d(TAG, "!! save start");
    			ControlCastListFile cc = new ControlCastListFile(AddPolitic.this);
    			addresult = cc.Add2CastFile(ic, xmlURL);
    			Log.d(TAG, "!! save end");

    			mCompleteHandler.sendEmptyMessage(SAVE_END);

    		}
    	};
    	backGround2.start();
	}

	ProgressDialog progressParse;
	private void startprogressParse(String msg){
		progressParse = new ProgressDialog(AddPolitic.this);
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


		cTitle.add("나는꼼수다");
		cDesc.add("딴지일보에서 제작하는 국내 유일 각하 헌정방송");
		cImage.add(R.drawable.r_nks);
		xml.add("http://old.ddanzi.com/appstream/ddradio.xml");

		cTitle.add("김어준의 뉴욕타임스");
		cDesc.add("한겨레TV [김어준의 뉴욕타임스] 팟캐스트 서비스를 한시적으로 재개합니다.");
		cImage.add(R.drawable.r_20120502_cctv_a);
		xml.add("http://vod.hani.co.kr/podcast/cctv_audio.xml.rss");
		
		cTitle.add("주진우의 현대사");
		cDesc.add("주진우의 정통시사 라디오 활극");
		cImage.add(R.drawable.r_joo);
		xml.add("http://old.ddanzi.com/appstream/drama.xml");
		
		cTitle.add("그것은 알기싫다");
		cDesc.add("딴지일보 수뇌부와 유엠씨의 히스테리 사건파일, 그것은 알기 싫다");
		cImage.add(R.drawable.p_noknow);
		xml.add("http://old.ddanzi.com/appstream/umcuw.xml");
		
		cTitle.add("뉴스타파(동영상)");
		cDesc.add("전국언론노동조합, 해직언론인 인터넷 뉴스.");
		cImage.add(R.drawable.r_newstapa);
		xml.add("http://www.iblug.com/xml/itunes/newstapa.xml");

		cTitle.add("노무현의 사람사는 세상");
		cDesc.add(" ");
		cImage.add(R.drawable.r_noh);
		xml.add("http://www.iblug.com/podcastxml/rohmoohyun");
		
		cTitle.add("이슈 털어주는 남자");
		cDesc.add("오마이뉴스가 제작하고 김종배가 진행하는 데일리 팟캐스트 방송입니다");
		cImage.add(R.drawable.r_etn);
		xml.add("http://rss.ohmynews.com/RSS/podcast_etul_main.xml");

		cTitle.add("유시민 노회찬의 저공비행");
		cDesc.add("저공비행시즌2");
		cImage.add(R.drawable.lowflying);
		xml.add("http://www.iblug.com/xml/itunes/lowflying.xml");
	
		cTitle.add("파업채널M");
		cDesc.add("MBC노동조합 저화질 공정방송\n파업특보, 제대로 뉴스데스크, 파워업 PD수첩, 사진, 영상, 소식 제공.");
		cImage.add(R.drawable.r_mbcpaup);
		xml.add("http://www.iblug.com/xml/itunes/mbcunion.xml");
		
		cTitle.add("파업채널 리셋 KBS");
		cDesc.add("전국언론노동조합 KBS본부 파업채널 팟캐스트");
		cImage.add(R.drawable.kbsrs);
		xml.add("http://www.iblug.com/xml/itunes/kbsunion.xml");
		
		cTitle.add("나는 친박(親朴)이다");
		cDesc.add(" ");
		cImage.add(R.drawable.r_park);
		xml.add("http://www.iblug.com/xml/itunes/propark.xml");
		
		cTitle.add("나는 의사다");
		cDesc.add("의료계 소문난 이빨 4인방이 깨알같은 의학 상식과 보건의료계 이슈를 거침없이 다룹니다.");
		cImage.add(R.drawable.r_img_imdoc);
		xml.add("http://www.docdocdoc.co.kr/podcast/iam_doctors.xml");
		
		cTitle.add("라디오 반민특위");
		cDesc.add("개념있는 여자들의 쎈 수다, 라디오 반민특위");
		cImage.add(R.drawable.r_banmin);
		xml.add("http://www.615tv.net/podcastbanmin/jkbsbanmin.xml");
		
		cTitle.add("망치부인 시사수다방");
		cDesc.add("아프리카 방송 : 망치부인 시사수다방 음성 녹화방송");
		cImage.add(R.drawable.r_mangchi);
		xml.add("http://nemo.podics.com/131192848437");

		cTitle.add("추정60분(추정15분)");
		cDesc.add("추정60분은 괴담으로 치부되는 수많은 의혹들을 국민의 시각에서 접근하여 과학적으로 검증하는 오락적 탐사 프로그램");
		cImage.add(R.drawable.r_chujeong);
		xml.add("http://www.615tv.net/csi/jkbscsi.xml");
		
		cTitle.add("이상호기자의 발뉴스 (동영상)");
		cDesc.add("탐사보도 특종뉴스 인터뷰 & 토크 출판 뉴스");
		cImage.add(R.drawable.r_balnews);
		xml.add("http://www.iblug.com/podcastxml/balnews");
		
		cTitle.add("찌라시바");
		cDesc.add("국내 최초 본격 찌라시 해석방송, 찌라시바!");
		cImage.add(R.drawable.r_jjirasi);
		xml.add("http://feeds.feedburner.com/zzirasiba");
		
		cTitle.add("노동골든벨");
		cDesc.add("곽현화와 딴지일보와 민주노총이 함께하는 유쾌한 퀴즈쇼");
		cImage.add(R.drawable.r_nodong);
		xml.add("http://old.ddanzi.com/appstream/bell.xml");
		
		cTitle.add("나는 문재인이다");
		cDesc.add("문재인 의원 헌정 방송, 나는 문재인이다.");
		cImage.add(R.drawable.r_imoonjain_1343125268684);
		xml.add("http://www.iblug.com/xml/itunes/imoonjain.xml");
		
		cTitle.add("이박사와 이작가의 이이제이");
		cDesc.add("전 세계 최초 시사대담 헌정방송!! 그들의 폭주기관차 같은 멈춤없는 폭풍질주!! 이것이 꿈인가? 생시인가?");
		cImage.add(R.drawable.r_vamp666);
		xml.add("http://pod.ssenhosting.com/rss/vamp666.xml");
		
		cTitle.add("신문 비교해주는 남자");
		cDesc.add(" ");
		cImage.add(R.drawable.p_newsnam);
		xml.add("http://www.iblug.com/xml/itunes/newsnam.xml");
		
		cTitle.add("맥코리아(Mac Korea)");
		cDesc.add("거대자본 맥쿼리와 이명박 대통령과의 관계를 폭로하는 '맥 코리아(mac korea)'. 10월 18일 대개봉");
		cImage.add(R.drawable.r_mack);
		xml.add("http://www.iblug.com/xml/itunes/mackorea.xml");
		
		cTitle.add("서영석 김용민의 정치토크");
		cDesc.add("리얼텍스트 대표 서영석과 나는 꼼수다 PD 김용민의 속이 시원해지는 정치에 관한 가벼운 토크!");
		cImage.add(R.drawable.seokimtalk);
		xml.add("http://www.iblug.com/podcastxml/seokimtalk");
		
		cTitle.add("이철희의 이쑤시개");
		cDesc.add("리얼텍스트 대표 서영석과 나는 꼼수다 PD 김용민의 속이 시원해지는 정치에 관한 가벼운 토크!");
		cImage.add(R.drawable.pressian_1353298465219);
		xml.add("http://www.iblug.com/xml/itunes/pressian.xml");
		
		cTitle.add("팟캐스트 윤여준");
		cDesc.add("윤여준이 여러분을 위해 준비한 정치이야기");
		cImage.add(R.drawable.warmwalk);
		xml.add("http://pod.ssenhosting.com/rss/warmwalk.xml");
		
		cTitle.add("벙커1 특강");
		cDesc.add("벙커1에서 펼쳐지는 실전파 지식인들의 무차별 릴레이 특강");
		cImage.add(R.drawable.a83934);
		xml.add("http://old.ddanzi.com/appstream/bunker1");
		
		cTitle.add("표창원이 알고 싶다");
		cDesc.add("표창원 박사 헌정방송 제공 - Insane Film(인세인 필름)");
		cImage.add(R.drawable.junhyunbae_1358564223460);
		xml.add("http://www.iblug.com/xml/itunes/junhyunbae.xml");
		
		cTitle.add("한겨레캐스트 -2012(Audio)");
		cDesc.add("한겨레 기자들이 진행하는 대선과 시사 이슈에 대한 심층분석");
		cImage.add(R.drawable.a20130123_hcastimg);
		xml.add("http://vod.hani.co.kr/newpod/hcast.xml");
		
		
		
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