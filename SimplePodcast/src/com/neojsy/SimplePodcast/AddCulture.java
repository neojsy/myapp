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

		
		cTitle.add("���� �������");
		cDesc.add("���� �������");
		cImage.add(R.drawable.r_ddanddara);
		xml.add("http://old.ddanzi.com/appstream/ddanddara.xml");
	
		cTitle.add("�̵����� ����å��");
		cDesc.add("�̵��� �۰� Ư���� �����ϰ� ��ī�ο� ������ ��Ƴ� å�� ������� ������ ����, ���� ������ �׾Ƶ� ������ å�� ���� �̾߱�");
		cImage.add(R.drawable.r_redbook);
		xml.add("http://hw.libsyn.com/p/d/0/9/d0996a60a3c2ad2c/redbooks.xml?sid=a6f735586d43ce9a3d8534e083e82151&l_sid=36679&l_eid=&l_mid=2977610&expiration=1334693154&hwt=65abefee573077e143c22339b46f5a2d");
		
		cTitle.add("�迵���� å �д� �ð�");
		cDesc.add(" ");
		cImage.add(R.drawable.r_booktime);
		xml.add("http://feeds.feedburner.com/KIMYoungha");	
		
		cTitle.add("��Ʈ�ؽ��͵�");
		cDesc.add("�ڲٸ� ������ ������ ���� �����, �ι����� ���طӴ�!��");
		cImage.add(R.drawable.r_artn_logo);
		xml.add("http://www.artnstudy.com/podcast.xml");		
		
		cTitle.add("������ �̼���ũ");
		cDesc.add("�ٻڽʴϱ�? Ȥ�� ������ ������ �ʿ����� �����ʴϱ�? �̼��� ���ø�, ��� ���� ������.");
		cImage.add(R.drawable.r_misultalk);
		xml.add("http://sjwgallery.libsyn.com/rss");
		
		cTitle.add("���� ����(MUSIC)�ϴ�");
		cDesc.add("������ ����� ����(MUSIC)�� �������θ�Ʈ�� ���� �̾߱�.");
		cImage.add(R.drawable.r_iammusichada);
		xml.add("http://feeds.feedburner.com/musichada");
		
		
		cTitle.add("�������� �����ϱ�");
		cDesc.add("�ȳ��ϼ��� �������� �����ϱ��Դϴ�. ���� ���� ������ ����, �̼�, �ι����� �ŷ¿� ���� �Ұ��ϴ� ����Դϴ�.");
		cImage.add(R.drawable.r_jh);
		xml.add("http://feeds.feedburner.com/palaisdegala");
		
		cTitle.add("��Ʈ ������");
		cDesc.add("��� ���� �߽��� ��ȭ ���� ����Ʈ ��Ʈ�������� ��ĳ��Ʈ. 1���Ͽ� 1ȸ ������Ʈ �Ǵ� ��Ʈ����.");
		cImage.add(R.drawable.r_mintpaper);
		xml.add("http://mintpaper2.cafe24.com/radio/podcast/mintradio.xml");
		
		cTitle.add("Ourlist �ƿ�����Ʈ");
		cDesc.add("���� ���� Lister�� �Բ��ϴ� ���� �ڽ��丮. �Ź� �ٸ� ������ ������ �����е��� ã�ư��ϴ�.");
		cImage.add(R.drawable.r_ourlist);
		xml.add("http://feeds.feedburner.com/ourlist");
		
		cTitle.add("�������� �ι��� Ư�� (������)");
		cDesc.add("���ɽ����� ��Ÿ����! �ι��� ������ ���忡 �پ���! ���󿡼� ���� ���� ����, ���󿡼� ���� ��մ� ����.");
		cImage.add(R.drawable.r_inmun);
		xml.add("http://rss.ohmynews.com/rss/podcast_cjk_online_main.xml");
		
		cTitle.add("�����̽��� �¶��ΰ��� (������)");
		cDesc.add("����� ������! �����̴����� ����� '�����̽��� �¶��ΰ���'. ����, ��ȣ��, �켮�� ���� �Բ��� �������� ����.");
		cImage.add(R.drawable.r_online_thum);
		xml.add("http://rss.ohmynews.com/rss/podcast_full_online_main.xml");

		cTitle.add("������TV ���ڿ��� ��ȭ (������)");
		cDesc.add("'�����̴���'�� ����� ���� ���ڵ���� Ư���� ����!");
		cImage.add(R.drawable.r_ohmytvmanman);
		xml.add("http://rss.ohmynews.com/rss/podcast_authortalk_main.xml");		
		
		cTitle.add("�����ϰ� �Ļ��� �������");
		cDesc.add("���Ļ�� ������ ������ ���ΰ� ���� ��찡 ����� ����ִ� �����̾߱��Դϴ�.");
		cImage.add(R.drawable.r_bbunphoto);
		xml.add("http://phototip.libsyn.com/rss");

		cTitle.add("����� ������ ���ݰ���");
		cDesc.add("����Ŀ�� ���ǰ� �λ��� ���� �����ο� ���ǹ��!");
		cImage.add(R.drawable.r_shakemusic_1336906721948);
		xml.add("http://www.iblug.com/xml/itunes/shakemusic.xml");
		
		cTitle.add("�������� �ð���å");
		cDesc.add("���ǰ� ���Ҿ� �ϻ� �� �Ҽ��� �̾߱⸦ ������ �����Դϴ�. �ٻ� �ϻ� �����ִ� �ð���å���� �������� �ʴ��մϴ�.");
		cImage.add(R.drawable.r_jhstellspage);
		xml.add("http://www.iblug.com/xml/itunes/jhstellspage.xml");
		
		cTitle.add("���÷��׵� ����");
		cDesc.add("ī����÷��׵��� �ε����� ������Ʈ! ȫ������� Ȱ���ϰ� �ִ� �ε������ 1���� �����Ͽ� �׵��� ���̺����ǰ� �̾߱⸦ ���� ��� �ε����� ����.");
		cImage.add(R.drawable.r_unplugged);
		xml.add("http://pod.ssenhosting.com/rss/unplugged.xml");
		
		cTitle.add("å, ��̰� ����!!");
		cDesc.add(" ");
		cImage.add(R.drawable.r_booktalk);
		xml.add("http://audien.libsyn.com/rss");
		
		cTitle.add("�������ǰ����(���̽���/onesidemusic)");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ppmusic);
		xml.add("http://www.iblug.com/xml/itunes/devinlee.xml");
		
		cTitle.add("���� ���а�");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ymm);
		xml.add("http://www.ebs.co.kr/actions/EncodingMngList?cmd=podcastEpisodeList&program_id=BP0PHPK0000000054");
		
		cTitle.add("�����б�");
		cDesc.add(" ");
		cImage.add(R.drawable.c_kojeon);
		xml.add("http://www.ebs.co.kr/actions/EncodingMngList?cmd=podcastEpisodeList&program_id=BP0PHPK0000000050");
		

		cTitle.add("TEDTalks ��ȸ�� ��ȭ");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ted_cul);
		xml.add("http://feeds.feedburner.com/KoreanPodcastSocietyAndCulture");
		
		cTitle.add("TEDTalks ����");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ted_art);
		xml.add("http://feeds.feedburner.com/KoreanPodcastArt");
		
		cTitle.add("TEDTalks ���");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ted_skill);
		xml.add("http://feeds.feedburner.com/KoreanPodcastTechnology");
		
		cTitle.add("TEDTalks ����");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ted_edu);
		xml.add("http://feeds.feedburner.com/KoreanPodcastEducation");
		
		cTitle.add("TEDTalks ���а� �Ƿ�");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ted_sci);
		xml.add("http://feeds.feedburner.com/KoreanPodcastScienceAndMedicine");
		
		cTitle.add("TEDTalks ����");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ted_music);
		xml.add("http://feeds.feedburner.com/KoreanPodcastMusic");

		cTitle.add("TEDTalks ������ ��ġ");
		cDesc.add(" ");
		cImage.add(R.drawable.r_ted_new);
		xml.add("http://feeds.feedburner.com/KoreanPodcastNewsAndPolitics");
		
		cTitle.add("TEDTalks ����Ͻ�");
		cDesc.add(" ");
		cImage.add(R.drawable.ted_busi);
		xml.add("http://feeds.feedburner.com/KoreanPodcastBusiness");
		
		cTitle.add("�չ̳��� �������");
		cDesc.add(" ");
		cImage.add(R.drawable.bbrcast);
		xml.add("http://pod.ssenhosting.com/rss/bbrcast.xml");
		
		cTitle.add("����޺� ��ž���");
		cDesc.add("����޺��� �Բ��ϴ� ��ĳ��Ʈ");
		cImage.add(R.drawable.okdalradio);
		xml.add("http://pod.ssenhosting.com/rss/msbsound/okdalradio.xml");

		cTitle.add("�̽¿��� ��������");
		cDesc.add("���� �ɸ����� ��ȸ�� ������� ���Ǳ⸦ �ٶ��ϴ�");
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