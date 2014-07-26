package com.neojsy.smartoilmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OilingInfo extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oilinginfo);
        
        
    }
    
	public void onResume() {
		super.onResume();
		
        putText();
    }
    
    private void putText(){
		ArrayList<OilData> od = new ArrayList<OilData>();
		FileRW f = new FileRW();
		od = f.readFile_OilData();
		
		TextView textTotalwon = (TextView) findViewById(R.id.oiltotal_totalwon);
		TextView textTotalnum = (TextView) findViewById(R.id.oiltotal_totalnum);
		TextView textEverwon = (TextView) findViewById(R.id.oiltotal_everwon);
		TextView textFistrec = (TextView) findViewById(R.id.oiltotal_firstrec);
		TextView textLastrec = (TextView) findViewById(R.id.oiltotal_lastrec);
		
		if(od.size() > 0){
			int totalwon = 0;
			int totalnum = od.size();
			int firtstime = Util.Str2Int(od.get(0).getDateStr(false));
			String firsttimes = " ";
			int lasttime = Util.Str2Int(od.get(0).getDateStr(false));
			String lasttimes = " ";
			
			for(int i=0;i<totalnum;i++){
				totalwon = Util.Str2Int(od.get(i).won) + totalwon;
				
				if(firtstime >= Util.Str2Int(od.get(i).getDateStr(false))){
					firsttimes = od.get(i).getDateStr(true);
				}
				
				if(lasttime <= Util.Str2Int(od.get(i).getDateStr(false))){
					lasttimes = od.get(i).getDateStr(true);
				}
			}
			int everwon = totalwon/totalnum;
			
			textTotalwon.setText(Util.Int2Str(totalwon)+"원");
			textTotalnum.setText(Util.Int2Str(totalnum)+"회");
			textEverwon.setText(Util.Int2Str(everwon)+"원");
			textFistrec.setText(firsttimes);
			textLastrec.setText(lasttimes);
		}
		else{
			textTotalwon.setText("0"+"원");
			textTotalnum.setText("0"+"회");
			textEverwon.setText("0"+"원");
			textFistrec.setText("기록없음");
			textLastrec.setText("기록없음");
			/*
			new AlertDialog.Builder(OilingInfo.this)
			.setMessage(getResources().getString(R.string.oiltotal_msg_noinfo))
		    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
		    	public void onClick(DialogInterface dialog, int whichButton) {
		    		finish();
		      }
		    })
		    .show();
		    */
		}
    }
    
    public void oilinfoadd(View v){
    	Intent intent = new Intent(OilingInfo.this, OilingAddItem.class);
    	startActivity(intent);
    }
    
    public void recordlist(View v){
    	Intent intent = new Intent(OilingInfo.this, RecordList.class);
    	startActivity(intent);
    }
    
    public void monthchart(View v){
    	Intent intent = new Intent(OilingInfo.this, OilingMonthChar.class);
    	startActivity(intent);
    }
    
    public void smsFilter(View v){
    	Intent intent = new Intent(OilingInfo.this, OilingSetFilter.class);
    	startActivity(intent);
    }
}
