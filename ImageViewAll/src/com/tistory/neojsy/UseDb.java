package com.tistory.neojsy;

import android.app.*;
import android.content.*;
import android.util.*;

public class UseDb {
	
    public String getValue(Context context, String item, String base){
    	SharedPreferences pref = context.getSharedPreferences("neojsy", Activity.MODE_PRIVATE);
		String value = pref.getString(item, base);
//		Log.d(null, item + " " + value);
		return value;
    }
    
    public void setValue(Context context, String item, String value){
    	SharedPreferences pref = context.getSharedPreferences("neojsy", Activity.MODE_PRIVATE);
		SharedPreferences.Editor ed = pref.edit();
		ed.putString(item, value);
//		Log.d(null, item + " " + value);
		ed.commit();
    }
}
