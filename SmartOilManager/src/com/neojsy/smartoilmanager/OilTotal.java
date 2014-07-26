package com.neojsy.smartoilmanager;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class OilTotal extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oil_total);
		
		ArrayList<OilData> od = new ArrayList<OilData>();
		FileRW f = new FileRW();
		od = f.readFile_OilData();
		
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
			
			TextView textTotalwon = (TextView) findViewById(R.id.oiltotal_totalwon);
			TextView textTotalnum = (TextView) findViewById(R.id.oiltotal_totalnum);
			TextView textEverwon = (TextView) findViewById(R.id.oiltotal_everwon);
			TextView textFistrec = (TextView) findViewById(R.id.oiltotal_firstrec);
			TextView textLastrec = (TextView) findViewById(R.id.oiltotal_lastrec);
			
			textTotalwon.setText(Util.Int2Str(totalwon)+"¿ø");
			textTotalnum.setText(Util.Int2Str(totalnum)+"È¸");
			textEverwon.setText(Util.Int2Str(everwon)+"¿ø");
			textFistrec.setText(firsttimes);
			textLastrec.setText(lasttimes);
		}
	}
}
