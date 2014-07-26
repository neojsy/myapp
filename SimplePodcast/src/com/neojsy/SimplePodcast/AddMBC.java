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

public class AddMBC extends Activity {

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
	            	if(!Util.isConnected(AddMBC.this)){
	            		Toast.makeText(AddMBC.this, getResources().getString(R.string.msg_networkerror), Toast.LENGTH_SHORT).show();
	            	}
	            	else{
	            		addCast(position);
	            	}
            	}
            	else{
            		Toast.makeText(AddMBC.this, getResources().getString(R.string.msg_notcomwork), Toast.LENGTH_SHORT).show();
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
		progressSave = ProgressDialog.show(AddMBC.this, "", getResources().getString(R.string.msg_SavingCastAdd), true);
    	Thread backGround2 = new Thread() {
    		public void run() {

    			Log.d(TAG, "!! save start");
    			ControlCastListFile cc = new ControlCastListFile(AddMBC.this);
    			addresult = cc.Add2CastFile(ic, xmlURL);
    			Log.d(TAG, "!! save end");

    			mCompleteHandler.sendEmptyMessage(SAVE_END);

    		}
    	};
    	backGround2.start();
	}

	ProgressDialog progressParse;
	private void startprogressParse(String msg){
		progressParse = new ProgressDialog(AddMBC.this);
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

		//
		cTitle.add("�ռ����� �ü�����");
		cDesc.add("MBC ǥ��FM ���� 6�� 15��~8��");
		cImage.add(R.drawable.mbc_ssh);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1000674100000100000");
		
		cTitle.add("������� ���ٸ� ����");
		cDesc.add("MBC ǥ��FM �� 9��35��~10��(��~��)");
		cImage.add(R.drawable.r_pod_proimg_counseling);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1002586100000100000 ");
		
		cTitle.add("FM ���ǵ��� ���ð��Դϴ�");
		cDesc.add("MBC FM4U �� 10��~12�� ");
		cImage.add(R.drawable.mbc_ssk);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1002600100000100000");
		
		cTitle.add("���Ѽ�, ��ĥ���� ��������");
		cDesc.add("MBC ǥ��FM ���� 11��45��~12�� ");
		cImage.add(R.drawable.mbc_gjyj);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1002488100000100000");

		cTitle.add("Ǫ���� �����Դϴ�");
		cDesc.add("MBC FM4U �� 12��~ 2��");
		cImage.add(R.drawable.mbc_jy);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1000578100000100000");

		cTitle.add("�ν��� ����Ʈ �ֿ����Դϴ�");
		cDesc.add("MBC FM4U ���� 2��~4�� ");
		cImage.add(R.drawable.mbc_jyh);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1000586100000100000");
		
		cTitle.add("�־���� ����ִ� ����");
		cDesc.add("MBC ǥ��FM ���� 8��10��(��,�� 8��5��)~9�� ");
		cImage.add(R.drawable.mbc_cyr);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?acode=1000576100000100000");
		
		cTitle.add("�������� ��ȭ����");
		cDesc.add("MBC FM4U ���� 3��~ 4�� ");
		cImage.add(R.drawable.mbc_ljy);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1000580100000100000");
		
		cTitle.add("���� ��Ŭ�� �������Դϴ�");
		cDesc.add("MBC ǥ��FM �� ��ħ 7��10��~8��");
		cImage.add(R.drawable.mbc_kje);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1000698100000100000");
		
		cTitle.add("����ö�� Ghost Station");
		cDesc.add("MBC FM4U ���� 2��~ 3�� ");
		cImage.add(R.drawable.mbc_shc);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1002585100000100000");
		
		cTitle.add("������ ���� �ô�");
		cDesc.add("MBC ǥ��FM ���� 4��5��~6�� ");
		cImage.add(R.drawable.mbc_radioside);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1000670100000100000");
		
		cTitle.add("������ ���� ������ �㿡");
		cDesc.add("MBC ǥ��FM �� 10��5��~12�� ");
		cImage.add(R.drawable.mbc_starnight);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1000677100000100000");
		
		cTitle.add("�ŵ��� �ɽ�Ÿ��");
		cDesc.add("MBC ǥ��FM �� 12��5��~12�� ");
		cImage.add(R.drawable.mbc_enjoy);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1000664100000100000");
		
		cTitle.add("������ �ٲٴ� ����");
		cDesc.add("MBC ǥ��FM ��ħ 6��5��~25��(��)");
		cImage.add(R.drawable.mbc_changeworld);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1002590100000100000");
		
		cTitle.add("������ ����� ��ſ��Դϴ�");
		cDesc.add("MBC FM4U �� 12��~2�� ");
		cImage.add(R.drawable.mbc_musicparty);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1001919100000100000");
		
		cTitle.add("������ �߰� �����ҷο��Դϴ�");
		cDesc.add("MBC FM4U ���� 4�� ~ 6��");
		cImage.add(R.drawable.mbc_swit);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1002782100000100000");
		
		cTitle.add("���þ�ħ �������Դϴ�");
		cDesc.add("MBC FM4U ���� 9��~11�� ");
		cImage.add(R.drawable.mbc_todaym);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1000576100000100000");
		
		cTitle.add("������ ������ ģ��ģ��");
		cDesc.add("MBC FM4U ���� 8��~10��");
		cImage.add(R.drawable.mbc_bestfriend2);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1002413100000100000");
		
		cTitle.add("���� ���� �� �������Դϴ�");
		cDesc.add("MBC ǥ��FM ���� 3��~4��");
		cImage.add(R.drawable.mbc_night);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1001831100000100000");
		
		cTitle.add("�����ô� ������, �������Դϴ�");
		cDesc.add("MBC ǥ��FM ���� 9��5�� ~ 11��");
		cImage.add(R.drawable.mbc_womenera);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1000630100000100000");
		
		cTitle.add("������ ���� ��ħ ���ټ��Դϴ�");
		cDesc.add("MBC FM4U ��ħ 6��~7��");
		cImage.add(R.drawable.mbc_worldmorning);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1000581100000100000");
		//
		cTitle.add("�ǰ��� ��ħ Ȳ�����Դϴ�");
		cDesc.add("MBC ǥ��FM ��ħ 5��10��~6��(��~��) ");
		cImage.add(R.drawable.mbc_healthmorning);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1002588100000100000");

		cTitle.add("���̿�, ������ �νø���");
		cDesc.add("MBC ǥ��FM ���� 2��20��(��,�� 2��10��) ~ 4�� ");
		cImage.add(R.drawable.mbc_hurray);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1000661100000100000");
		
		cTitle.add("�������� �����Դϴ�");
		cDesc.add("MBC ǥ��FM �� 9��35��~9��45��");
		cImage.add(R.drawable.mbc_funtalk);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1002689100000100000");
		
		cTitle.add("�質���� ���赵�ÿ���");
		cDesc.add("MBC ǥ��FM ���� 6�� 25��~7��");
		cImage.add(R.drawable.mbc_pod_proimg_citytour);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1000621100000100000");
		
		cTitle.add("�¸��FM �������Դϴ�");
		cDesc.add("MBC FM4U ��ħ 7��~9��");
		cImage.add(R.drawable.mbc_seo);
		xml.add("http://minicast.imbc.com/PodCast/pod.aspx?code=1000587100000100000");

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