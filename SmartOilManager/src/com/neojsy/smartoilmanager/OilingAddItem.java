package com.neojsy.smartoilmanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class OilingAddItem extends Activity {
	//Calendar calendar = Calendar.getInstance();
	GregorianCalendar calendar = new GregorianCalendar();
	TextView txtLabel;

	DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			calendar.set(year, monthOfYear, dayOfMonth);
			setDate(year, monthOfYear, dayOfMonth);
			//setLabel();
		}
	};

	TimePickerDialog.OnTimeSetListener timeSetListenr = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calendar.set(Calendar.MINUTE, minute);
			setTime(hourOfDay, minute);
			//setLabel();
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oiling_add);

		Button btnDate = (Button) findViewById(R.id.oiling_date);

		btnDate.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				new DatePickerDialog(OilingAddItem.this,
						dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

		Button btnTime = (Button) findViewById(R.id.oiling_time);

		btnTime.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				new TimePickerDialog(OilingAddItem.this, timeSetListenr, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
			}
		});
		setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
	}

	private void setDate(int year, int month, int day){
		Button btn = (Button) findViewById(R.id.oiling_date);
		btn.setText(Util.Int2Str(year)+"."+String.format("%02d", (month + 1))+"."+String.format("%02d", day));
		
	}
	
	
	private void setTime(int hour, int min){
		Button btn = (Button) findViewById(R.id.oiling_time);
		btn.setText(String.format("%02d", hour)+":"+String.format("%02d", min));
		
	}
	
	public void oildataadd(View v){

//		Util.print("date.getYear()"+Util.Int2Str(date.getYear()));
//		Util.print("date.getMonth()"+Util.Int2Str(date.getMonth()));
//		Util.print("date.getDay()"+Util.Int2Str(date.getDay()));
//		Util.print("date.getHours()"+Util.Int2Str(date.getHours()));
//		Util.print("date.getMinutes()"+Util.Int2Str(date.getMinutes()));
		
//		GregorianCalendar adddate = new GregorianCalendar(1900+date.getYear(), date.getMonth(), date.getDay(), date.getHours(), date.getMinutes());
//		adddate.set(date.getYear(), date.getMonth(), date.getDay(), date.getHours(), date.getMinutes());
		
		EditText editTextCard = (EditText) findViewById(R.id.oiling_input_text1);
		EditText editTextWon = (EditText) findViewById(R.id.oiling_input_text2);
		
		if((!editTextCard.getText().toString().equals("")) && (!editTextWon.getText().toString().equals(""))){
			String card = editTextCard.getText().toString();
			long won = Integer.parseInt("" + editTextWon.getText());
			
			ArrayList<OilData> od = new ArrayList<OilData>();
			FileRW f = new FileRW();
			od = f.readFile_OilData();
			od.add(0, new OilData(" ", Util.Long2Str(won), card, calendar));
			f.writeFile_OilData(od);
			finish();
		}
		else{
			new AlertDialog.Builder(OilingAddItem.this)
			.setMessage(getResources().getString(R.string.oilinput_msg_noinput))
			.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
	    		/* User clicked OK so do some stuff */
	    		
				}
			})
			.show();
		}
	}
}