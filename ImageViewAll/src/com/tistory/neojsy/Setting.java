package com.tistory.neojsy;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class Setting extends Activity implements AdapterView.OnItemSelectedListener {
 //   private SettingInfo SettingInfo;
    String YES = "yes";
    String NO = "no";
    String DEL = "del";
    String INFO = "info";
    String DELPOP = "delpop";
    String SLIDE = "slide";
    String MOVE = "move";
    String WIDGET = "WidgetSlot";
    
 //   String[] items = {"Off", "SLOT 1", "SLOT 2", "SLOT 3"};
    
//    Spinner spin;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        CheckBox checkDel = (CheckBox) findViewById(R.id.CheckBoxDel);
        CheckBox checkInfo = (CheckBox) findViewById(R.id.CheckBoxInfo);
        CheckBox checkDelPop = (CheckBox) findViewById(R.id.CheckBoxDelPopup);
        CheckBox checkSs = (CheckBox) findViewById(R.id.CheckBoxSs);
        CheckBox checkMov = (CheckBox) findViewById(R.id.CheckBoxMov);

        checkDel.setChecked(getValue(this, DEL));
        checkDelPop.setChecked(getValue(this, DELPOP));
        checkInfo.setChecked(getValue(this, INFO));
        checkSs.setChecked(getValue(this, SLIDE));
        checkMov.setChecked(getValue(this, MOVE));
        
        checkDel.setOnCheckedChangeListener(mCheckDel);
        checkDelPop.setOnCheckedChangeListener(mCheckDelPopup);
        checkInfo.setOnCheckedChangeListener(mCheckInfo);
        checkSs.setOnCheckedChangeListener(mCheckSs);
        checkMov.setOnCheckedChangeListener(mCheckMov);
        /*   
 //       spin = (Spinner)findViewById(R.id.spinner1);
 //       ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
 //       aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 //       spin.setAdapter(aa);
        
        UseDb db = new UseDb();
        String WidgetSlot  = db.getValue(this, WIDGET, "0");
     
        if(WidgetSlot.equals("0")){
        	spin.setSelection(0);        	
        }else if(WidgetSlot.equals("1")){
        	spin.setSelection(1);
        }else if(WidgetSlot.equals("2")){
        	spin.setSelection(2);
        }else if(WidgetSlot.equals("3")){
        	spin.setSelection(3);
        }
*/
    }
/*    
    protected void onPause() {
    	super.onPause();
    	
    	int se = (int) spin.getSelectedItemId();

    	switch(se){
    	case 0:
    		setValue(this, WIDGET, "0");
    		Log.d(null, "0");
    		break;
    	case 1:
    		setValue(this, WIDGET, "1");
    		Log.d(null, "1");
    		break;
    	case 2:
    		setValue(this, WIDGET, "2");
    		Log.d(null, "2");
    		break;
    	case 3:
    		setValue(this, WIDGET, "3");
    		Log.d(null, "3");
    		break;
    	}
    	
    	
    }
  */  
    public boolean getValue(Context context, String item){
    	SharedPreferences pref = context.getSharedPreferences("neojsy", Activity.MODE_PRIVATE);
		String value = pref.getString(item, YES);
		if(value.equals(YES)){
			return true;
		}else{
			return false;
		}
    }
    
    public void setValue(Context context, String item, String value){
    	SharedPreferences pref = context.getSharedPreferences("neojsy", Activity.MODE_PRIVATE);
		SharedPreferences.Editor ed = pref.edit();
		ed.putString(item, value);
		ed.commit();
    }
 
 
    
    CompoundButton.OnCheckedChangeListener mCheckMov = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
        	if (isChecked) setValue(Setting.this, MOVE, YES);
        	else setValue(Setting.this, MOVE, NO);
        }
    };    

    CompoundButton.OnCheckedChangeListener mCheckSs = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
        	if (isChecked) setValue(Setting.this, SLIDE, YES);
        	else setValue(Setting.this, SLIDE, NO);
        }
    };
    
    CompoundButton.OnCheckedChangeListener mCheckDel = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
        	if (isChecked) setValue(Setting.this, DEL, YES);
        	else setValue(Setting.this, DEL, NO);
        }
    };

    CompoundButton.OnCheckedChangeListener mCheckDelPopup = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
        	if (isChecked) setValue(Setting.this, DELPOP, YES);
        	else setValue(Setting.this, DELPOP, NO);
        }
    };    
    
    CompoundButton.OnCheckedChangeListener mCheckInfo = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
        	if (isChecked) setValue(Setting.this, INFO, YES);
        	else setValue(Setting.this, INFO, NO);
        }
    };

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}