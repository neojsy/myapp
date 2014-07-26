package com.neojsy.smartoilmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class YunbiInfo extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yunbiinfo);
        drawYunbiInfo();
    }
    
    private void drawYunbiInfo(){
    	ArrayList<CarData> cd = new ArrayList<CarData>();
		FileRW f = new FileRW();
		cd = f.readFile_CarData();
		
    	ArrayList<OilData> od = new ArrayList<OilData>();
		FileRW fo = new FileRW();
		od = fo.readFile_OilData();
		
		if(cd.size() < 2 || od.size() < 2){
			//too small data
				new AlertDialog.Builder(YunbiInfo.this)
				.setMessage(getResources().getString(R.string.yunbi_total_msg_noinfo))
			    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			    	public void onClick(DialogInterface dialog, int whichButton) {
			    		/* User clicked OK so do some stuff */
			    		finish();
			      }
			    })
			    .show();
		}
		else{

			float sum_liter = 0;
			for(int i=cd.size()-1;i>0;i--){
				String startDate = cd.get(i).getDateStr(false) + cd.get(i).getTimeStr(false);
				String endDate = cd.get(i-1).getDateStr(false) + cd.get(i-1).getTimeStr(false);
				endDate = endDate.substring(2, endDate.length());
				startDate = startDate.substring(2, startDate.length());
				Util.print("yunbi startDate : "+startDate+" endDate : "+endDate);
				
				long sum_won = 0;

				for(int j=0;j<od.size();j++){
					String odDate = od.get(j).getDateStr(false) + od.get(j).getTimeStr(false);
					odDate = odDate.substring(2, odDate.length());
					Util.print("yunbi oildatadate : "+odDate);
										
					if(Util.Str2Int(startDate) <= Util.Str2Int(odDate) && Util.Str2Int(odDate) < Util.Str2Int(endDate)){
						Util.print("yunbi ok. plub won");
						sum_won = Util.Str2Int(od.get(j).won) + sum_won;
					}
				}
				
				if(sum_won > 0){
					float liter = sum_won / cd.get(i-1).literwon;
					sum_liter = sum_liter + liter;
				}
				
				Util.print("yunbi "+i+"st literwon : "+cd.get(i-1).literwon);
				Util.print("yunbi "+i+"st sum_won : "+sum_won);
				//Util.print("yunbi "+i+"st liter : "+liter);
				Util.print("yunbi "+i+"st sum_liter : "+sum_liter);
	
			}
			
			int totalDistance = cd.get(0).distance - cd.get(cd.size() - 1).distance;
			float yunbi = totalDistance/sum_liter;
			
			Util.print("totalDistance : "+ totalDistance);
			Util.print("sum_liter : "+ sum_liter);
			String literFinal = String.format("%.2f", sum_liter);
			Util.print("yunbi yunbi : "+ yunbi);
			String yunbiFinal = String.format("%.2f", yunbi);
			Util.print("yunbi yunbiFinal : "+ yunbiFinal);
			
			TextView textyunbi_total = (TextView) findViewById(R.id.yunbi_total);
			TextView textyunbi_total_distance = (TextView) findViewById(R.id.yunbi_total_distance);
			TextView textyunbi_total_oiling = (TextView) findViewById(R.id.yunbi_total_oiling);
			TextView textyunbi_total_record = (TextView) findViewById(R.id.yunbi_total_record);
			
			textyunbi_total.setText(yunbiFinal);
			textyunbi_total_distance.setText(Util.Int2Str(totalDistance)+" km");
			textyunbi_total_oiling.setText(literFinal+" L");
			textyunbi_total_record.setText(cd.get(cd.size()-1).getDateStr(true)+" - "+cd.get(0).getDateStr(true));
			
		}
    }
    
    public void yunbilist(View v){
    	Intent intent = new Intent(YunbiInfo.this, RecordList.class);
    	startActivity(intent);
    }
}
