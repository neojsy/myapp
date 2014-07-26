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


		cTitle.add("���²Ż縮��");
		cDesc.add("���²ļ��� ������.");
		cImage.add(R.drawable.r_nksr);
		xml.add("http://old.ddanzi.com/appstream/ggobsari.xml");
		
		cTitle.add("������ ������ �׵��� ������ �ʴ� 23����");
		cDesc.add("�������� ������������ �� �ָ��� �޾Ҵ� ������ ������ ����ִ� �ں����� �̾߱�׵��� ������ �ʴ� 23����.");
		cImage.add(R.drawable.r_janghajun);
		xml.add("http://nemo.podics.com/131113737488");
		
		cTitle.add("�����̽��� ������ Ư��");
		cDesc.add("����� ������! �����̴����� ����� '�����̽��� ������ Ư��'. �����, �켮��, ȫ��� ���� �Բ��� �������� ����.");
		cImage.add(R.drawable.r_ec_con);
		xml.add("http://rss.ohmynews.com/rss/podcast_EC_online_main.xml");
		
		cTitle.add("��ó�߼�");
		cDesc.add("��ó�� ���� ���� ����� �̾߱�, �׸��� ������ ���̾߱����~ ������ ��ó�߼�");
		cImage.add(R.drawable.r_venture_story_3);
		xml.add("http://awsmovie.cafe24.com/venture_s/venture_story.xml");

		cTitle.add("���ѹα������̾߱� �����");
		cDesc.add("���ѹα� �°� ������ ��ü ����ġ��. ǰ�ݾ��� ��ȸ��� ôô�ڻ��� ��ũ��.");
		cImage.add(R.drawable.r_jobsooda);
		xml.add("http://feeds.feedburner.com/libsyn/jobsooda");
		
		cTitle.add("�豤�� ������ ��¥ �����̾߱�");
		cDesc.add("�豤������������ �豤�� ������ ���� ������ ���ͺ� ���������Դϴ�.");
		cImage.add(R.drawable.r_seminar);
		xml.add("http://podcast.kseri.net/kseri/interview_n_seminar.xml");
		
		cTitle.add("�ױ۸��� �� �ڸ���");
		cDesc.add("���� ���, ����ΰ� �Բ� �ѱ����� Ǯ�� ���ô�!");
		cImage.add(R.drawable.r_eng);
		xml.add("http://englishinkorean.com/?feed=podcast");	
		
		cTitle.add("����ŷ�ƽ� ������� (������)");
		cDesc.add("2011�� ����ȸȭ ����Ʈ ����.");
		cImage.add(R.drawable.r_max);
		xml.add("http://www.speakingmax.com/podcast/maxpodcast.php");	
		
		cTitle.add("5�� ����ȸȭ_Make My Day");
		cDesc.add("ȭ��� 5��, Ŀ�� �극��ũ 5��, ����ö ��ٸ��鼭 5��. �̷��� ���� 5�о��� ������ ���� ����.");
		cImage.add(R.drawable.r_5mineng);
		xml.add("http://nemo.podics.com/126697529249");	

		cTitle.add("��ġ����");
		cDesc.add("��ġ����-4�ð��� ����Ʈ���� �����ϰ� ��");
		cImage.add(R.drawable.r_dakeng);
		xml.add("http://www.iblug.com/xml/itunes/johnykim.xml");
		
		cTitle.add("CNN Student News (������)");
		cDesc.add("CNN Student News utilizes CNN's worldwide resources");
		cImage.add(R.drawable.r_studentnews1);
		xml.add("http://rss.cnn.com/services/podcasting/studentnews/rss.xml");
		/*
		cTitle.add("TEDTalks (hd) (������)");
		cDesc.add("TED is a nonprofit devoted to Ideas Worth Spreading.");
		cImage.add(R.drawable.r_tedtalks);
		xml.add("http://feeds.feedburner.com/TedtalksHD");		
		*/
		cTitle.add("�ε������̾߱�");
		cDesc.add("�˸� ��ư��µ� �ǰ� �ǰ� ���� �Ǵ� �ε������̾߱�. ������� ������ ���� �ε����ż��踦 ��ũ�������� �����մϴ�.");
		cImage.add(R.drawable.r_auction);
		xml.add("http://feeds.feedburner.com/libsyn/TjyK");	
		
		cTitle.add("�ϻ��� ���丮�� �ִ� ����ȸȭ");
		cDesc.add("����ΰ� ���� ȸȭ �ϱ� ���� �̸� ���� ȸȭ ǥ��, ���� ���, ������ �뺻�� ���� �����ϴ� �����Դϴ�");
		cImage.add(R.drawable.r_story);
		xml.add("http://www.iblug.com/podcastxml/ilbangbang");	
		
		cTitle.add("����! ����翬");
		cDesc.add("������ ����ִ� ��̳� �翬! �׸��� �ǰ�����ɻ��򰡿� ���� �˾ƺ��� �ǰ��̾߱�");
		cImage.add(R.drawable.e_yeshira);
		xml.add("http://pod.ssenhosting.com/rss/yeshira.xml");	
		
		cTitle.add("�������� �ƿ켺");
		cDesc.add("�ǰ��� ����ȭ�� ���� �����ְ� ���� ������. ");
		cImage.add(R.drawable.ausung);
		xml.add("http://www.aoosung.com/podcast/aoosung.xml");	
	
		cTitle.add("������ ������ �㿡");
		cDesc.add("������ ���� �ݿ��� �� ������Ʈ. Ʈ����,���̹�ī�� ������");
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