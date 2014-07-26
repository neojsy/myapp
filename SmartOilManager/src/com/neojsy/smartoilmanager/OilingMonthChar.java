package com.neojsy.smartoilmanager;


import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OilingMonthChar extends Activity {
	static final String logTag = "OIL";
	int mYear;
	static int [] month_number = new int [12];
	static long [] month_totalWon = new long [12];
	static long month_wonMax;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oil_month_char);

		GregorianCalendar time = new GregorianCalendar();
		mYear = time.get(time.YEAR);
		
		inputMonthDataAndDrawChart();
	}
	
	public void oilmonthcartpre(View v){
		mYear = mYear - 1;
		inputMonthDataAndDrawChart();
	}
	
	public void oilmonthcartnext(View v){
		mYear = mYear + 1;
		inputMonthDataAndDrawChart();
	}
	
	ProgressDialog progress;
	private void inputMonthDataAndDrawChart(){
		progress = new ProgressDialog(OilingMonthChar.this);
		progress.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
			}
		});
		progress.show();
		
		Thread backGround = new Thread() {
    		public void run() {
    			month_wonMax = 0;
    			
    			for(int i=0;i<12;i++){
    				ArrayList<OilData> od = new ArrayList<OilData>();
    				od = Util.getPeriodOilData(mYear, i+1, 1, mYear, i+1, 31);
    				month_number[i] = od.size();
    				month_totalWon[i] = 0;
    				
    				if(od.size() > 0){
    					for(int j=0;j<od.size();j++)
    						month_totalWon[i] = month_totalWon[i] + Util.Str2Int(od.get(j).won); 
    				
    					if(month_totalWon[i] > month_wonMax)
    						month_wonMax = month_totalWon[i];
    				}
    			}
    	    	mCompleteHandler.sendEmptyMessage(0);
    		}
    	};	
    	backGround.start();
		
	}
	
    public Handler mCompleteHandler = new Handler(){
    	public void handleMessage(Message msg){
    		progress.dismiss();
    		drawChart();
    	}
    };
	
	private void drawChart(){
		TextView text1 = (TextView) findViewById(R.id.oilyear);
		text1.setText(Util.Int2Str(mYear));
		
		Button b1 = (Button) findViewById(R.id.oilprebt);
		b1.setText(Util.Int2Str(mYear-1));
		
		Button b2 = (Button) findViewById(R.id.oilnextbt);
		b2.setText(Util.Int2Str(mYear+1));
		
		
		ListView l1 = (ListView) findViewById(R.id.oilmonthcharListView);
		l1.setAdapter(new EfficientAdapter(this));
	}
	
	private static class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		public int getCount() {
			return 12;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_chart_month, null);
				holder = new ViewHolder();
				holder.text1 = (TextView) convertView.findViewById(R.id.chartmonth);
				holder.textwon = (TextView) convertView.findViewById(R.id.chartwon);
				holder.prb1 = (ProgressBar) convertView.findViewById(R.id.chartprogressBar1);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}


			holder.text1.setText(Util.Int2Str(position+1)+"¿ù ");
			holder.textwon.setText(Util.Int2Str(month_number[position])+"°Ç, " + Util.Long2Str(month_totalWon[position])+"¿ø");
			
			long SangDae = 0;
			if(month_number[position] > 0){
				SangDae = month_totalWon[position]*100/month_wonMax;
			}

			holder.prb1.setMax(100);
			holder.prb1.setProgress((int) SangDae);
			
			Log.d(logTag, "SangDae :"+SangDae);
			Log.d(logTag, "month_totalWon :"+month_totalWon[position]);
			Log.d(logTag, "month_wonMax :"+month_wonMax);
			
			return convertView;
		}

		// http://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/

		static class ViewHolder {
			TextView text1;
			TextView textwon;
			ProgressBar prb1;
		}
	}
	
}
