package com.neojsy.smartoilmanager;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InputData extends Activity {
	static final String logTag = "OIL";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputdata);
	}

	public void editok(View v) {
		int kilo = 0;
		int literwon = 0;
		
		EditText editText1 = (EditText) findViewById(R.id.editkilo);
		EditText editText2 = (EditText) findViewById(R.id.editliter);
		
		if((!editText1.getText().toString().equals("")) && (!editText2.getText().toString().equals(""))){
			kilo = Integer.parseInt("" + editText1.getText());
			literwon = Integer.parseInt("" + editText2.getText());
			
			ArrayList<CarData> od = new ArrayList<CarData>();
			FileRW f = new FileRW();
			od = f.readFile_CarData();
			
			if((od.size() != 0) && (od.get(0).distance >= kilo)){	
				new AlertDialog.Builder(InputData.this)
				.setMessage(getResources().getString(R.string.input_nodistance))
				.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
		    		/* User clicked OK so do some stuff */
		    		
					}
				})
				.show();
			}
			else{
				Util.print("kilo"+kilo);
				Util.print("literwon"+literwon);
				
				if(kilo > 0 && literwon > 0){
	
					od.add(0, new CarData(kilo, literwon, new GregorianCalendar()));
					f.writeFile_CarData(od);
					
					Toast toast = Toast.makeText(this, getResources().getString(R.string.input_logsaved), Toast.LENGTH_SHORT); 
					toast.show(); 
					finish();
				}
				else{
					Toast toast = Toast.makeText(this, getResources().getString(R.string.input_logfailed), Toast.LENGTH_SHORT); 
					toast.show();
				}
			}
		}
	}
	
	public void backto(View v){
    	finish();
	}
	
	public void goviewlog(View v){
    	Intent intent = new Intent(InputData.this, InputDataList.class);
    	startActivity(intent);
    	finish();
	}
	
}
