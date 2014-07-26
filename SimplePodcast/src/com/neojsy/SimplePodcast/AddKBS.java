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

public class AddKBS extends Activity {

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
	            	if(!Util.isConnected(AddKBS.this)){
	            		Toast.makeText(AddKBS.this, getResources().getString(R.string.msg_networkerror), Toast.LENGTH_SHORT).show();
	            	}
	            	else{
	            		addCast(position);
	            	}
            	}
            	else{
            		Toast.makeText(AddKBS.this, getResources().getString(R.string.msg_notcomwork), Toast.LENGTH_SHORT).show();
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
		progressSave = ProgressDialog.show(AddKBS.this, "", getResources().getString(R.string.msg_SavingCastAdd), true);
    	Thread backGround2 = new Thread() {
    		public void run() {

    			Log.d(TAG, "!! save start");
    			ControlCastListFile cc = new ControlCastListFile(AddKBS.this);
    			addresult = cc.Add2CastFile(ic, xmlURL);
    			Log.d(TAG, "!! save end");

    			mCompleteHandler.sendEmptyMessage(SAVE_END);

    		}
    	};
    	backGround2.start();
	}

	ProgressDialog progressParse;
	private void startprogressParse(String msg){
		progressParse = new ProgressDialog(AddKBS.this);
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

		cTitle.add("[2FM] �������� ��ž�����");
		cDesc.add("���� 24:00-26:00 ������Ϸ� ģ�츮���� ����Ʈ! �������� ��ž�� �������� �Բ� �� �̾߱� ������");
		cImage.add(R.drawable.kbs_rooftop_120_100);
		xml.add("http://tune.kbs.co.kr/program/rss.php?pgNo=268");
		
		cTitle.add("[2FM] ������� ���䱤��");
		cDesc.add("���� 12:00 - 14:00 ��! ����� ���� �ٿö���! ������� ���䱤��");
		cImage.add(R.drawable.kbs_kbs);
		xml.add("http://tune.kbs.co.kr/program/rss.php?pgNo=257");
		
		cTitle.add("[2FM] Ȳ������ FM������");
		cDesc.add("���� 07:00 - 09:00 ����� ��� ��Ʈ��! Ȳ������ FM ������.");
		cImage.add(R.drawable.kbs_2fm_fmparade);
		xml.add("http://tune.kbs.co.kr/program/rss.php?pgNo=105");
		
		cTitle.add("[2FM] ���γ��� ������ ������");
		cDesc.add("���� 20:00 - 22:00 ����� ���� �ູ�� �ֹ� ���γ��� ������ ������.");
		cImage.add(R.drawable.kbs_2fm_uvolum);
		xml.add("http://tune.kbs.co.kr/program/rss.php?pgNo=188");
		
		cTitle.add("[2FM] �������� ���Ǿٹ�");
		cDesc.add("���� 09:00 - 11:00 ���� �� � ������ ���� �̾߱�~ ���ǰ� �Բ� �˷��帳�ϴ�.");
		cImage.add(R.drawable.kbs_2fm_album);
		xml.add("http://tune.kbs.co.kr/program/rss.php?pgNo=29");
		
		cTitle.add("[2FM] �̱�ö�� �¸���˽�");
		cDesc.add("���� 06:00 - 07:00 �ų��� ��ħ, ǳ���� ��ħ, ������ �ذ��!! �¸�� �˽��� å�����ϴ�.");
		cImage.add(R.drawable.kbs_2fm_gmp);
		xml.add("http://tune.kbs.co.kr/program/rss.php?pgNo=1");
		
		cTitle.add("[2FM] ������ �˽� �˽�");
		cDesc.add("���� 11:00 - 12:00 �� ���� ���� ���α׷�.");
		cImage.add(R.drawable.kbs_2fm_pops);
		xml.add("http://tune.kbs.co.kr/program/rss.php?pgNo=104");
		
		cTitle.add("[2FM] �����ִϾ��� Ű��������");
		cDesc.add("���� 22:00 - 24:00 ���� �� ����, ����� �Բ� �ϴ� ������ ����Ʈ!");
		cImage.add(R.drawable.kbs_2fm_kiss);
		xml.add("http://tune.kbs.co.kr/program/rss.php?pgNo=6");
		
		cTitle.add("[2FM] ����ϱ� ������ �̱����Դϴ�");
		cDesc.add("���� 18:00 - 20:00 ������ ���� �̱���� ��� �̾߱⸦ ��������.");
		cImage.add(R.drawable.kbs_2fm_loveday);
		xml.add("http://tune.kbs.co.kr/program/rss.php?pgNo=107");
		
		cTitle.add("[2FM] ������ ������");
		cDesc.add("���� 16:00 - 18:00 90��� ������ �߽����� �� ��ǰ�� �������� ���α׷�.");
		cImage.add(R.drawable.kbs_2fm_music);
		xml.add("http://tune.kbs.co.kr/program/rss.php?pgNo=43");
		
		cTitle.add("[1FM] �뷡�� ��������");
		cDesc.add("���� 16:00 - 17:00 ���� ������ �Ǳ�, ����� ��Ҹ��� �����ϴ� ���ǰ� ���� ���α׷��Դϴ�.");
		cImage.add(R.drawable.kbs_1fm_wing);
		xml.add("http://tune.kbs.co.kr/program/rss.php?pgNo=33");
		
		cTitle.add("[2R] ���� ������");
		cDesc.add("�� 08:05 - 09:00 �ѱ��ο��� �ͼ��� ������ǰ�� ��ǰ�� ���� ��󸶷� ��������!");
		cImage.add(R.drawable.kbs_2r_library);
		xml.add("http://tune.kbs.co.kr/program/rss.php?pgNo=159");
		
		cTitle.add("[2R] �豤���� ������Ŀ��");
		cDesc.add("��-�� 07:10 - 09:00 �� Ŭ���� �豤��, ������ �뷡�ϴ�");
		cImage.add(R.drawable.kbs_2r_ecofocus);
		xml.add("http://tune.kbs.co.kr/program/rss.php?pgNo=34");
		
		cTitle.add("[1R] �ż����� ��ȭ�б�");
		cDesc.add("���� 22:10 - 22:58 ��ȭ�� ���� ��ũ.");
		cImage.add(R.drawable.kbs_1r_culture);
		xml.add("http://tune.kbs.co.kr/program/rss.php?pgNo=41");

/*
		cTitle.add("���γ��� ������ ������");
		cDesc.add("KBS 2FM 20:00 ~ [89.1MHz ] ");
		cImage.add(R.drawable.kbs_uina);
		xml.add("http://tune.kbs.co.kr/rss/188.xml");
		
		cTitle.add("�̱�ö�� �¸�� �˽�");
		cDesc.add("KBS 2FM 6:00 AM ~ [89.1MHZ] ");
		cImage.add(R.drawable.kbs_gompops);
		xml.add("http://tune.kbs.co.kr/rss/1.xml");
		
		cTitle.add("���� ������");
		cDesc.add("KBS 2RADIO ~ [�� ���� 8�� 5�� ~ 9�� / 106.1MHz] ");
		cImage.add(R.drawable.kbs_radok);
		xml.add("http://tune.kbs.co.kr/rss/159.xml");
		
		cTitle.add("�ż����� ��ȭ�б�");
		cDesc.add("KBS 1RADIO 10:10 PM ~ [97.3MHZ] ");
		cImage.add(R.drawable.kbs_sinsung);
		xml.add("http://tune.kbs.co.kr/rss/41.xml");
		
		cTitle.add("�ְ����� �߰�����");
		cDesc.add("KBS 2FM 00:00 AM ~ [89.1MHz ] ");
		cImage.add(R.drawable.kbs_yagan);
		xml.add("http://tune.kbs.co.kr/rss/190.xml");
		
		cTitle.add("�����ִϾ��� Kiss the Radio");
		cDesc.add("KBS 2FM 22:00 PM ~ [89.1MHZ] ");
		cImage.add(R.drawable.kbs_kiss);
		xml.add("http://tune.kbs.co.kr/rss/6.xml");
		
		cTitle.add("������ �˽� �˽�");
		cDesc.add("KBS 2FM 11:00 AM ~ [89.1MHZ] ");
		cImage.add(R.drawable.kbs_pops);
		xml.add("http://tune.kbs.co.kr/rss/131.xml");
		
		cTitle.add("����ϱ� ������ �̱����Դϴ�");
		cDesc.add("KBS 2FM 18:00 PM ~ [89.1MHZ] ");
		cImage.add(R.drawable.kbs_sarangkbs_pops);
		xml.add("http://tune.kbs.co.kr/rss/107.xml");
		
		cTitle.add("Ȳ������ FM������");
		cDesc.add("KBS 2FM 07:00 AM ~ [89.1MHZ] ");
		cImage.add(R.drawable.kbs_hwang);
		xml.add("http://tune.kbs.co.kr/rss/105.xml");
*/
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