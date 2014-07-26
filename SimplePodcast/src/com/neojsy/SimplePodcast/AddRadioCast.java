package com.neojsy.SimplePodcast;

import android.content.Intent;
import android.os.Bundle;
import android.app.TabActivity;
import android.widget.TabHost;

public final class AddRadioCast extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final TabHost tabHost = getTabHost();

    	Intent intentMBS = new Intent(AddRadioCast.this, AddMBC.class);
    	intentMBS.putExtra("group", "MBC");
    	
    	Intent intentSBS = new Intent(AddRadioCast.this, AddSBS.class);
    	intentSBS.putExtra("group", "SBS");
    	
    	Intent intentKBS = new Intent(AddRadioCast.this, AddKBS.class);
    	intentKBS.putExtra("group", "KBS");
    	
    	Intent intentCBS = new Intent(AddRadioCast.this, AddCBS.class);
    	intentCBS.putExtra("group", "CBS");
		
		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator("", getResources().getDrawable(R.drawable.m_mbc))
				.setContent(intentMBS));

		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator("", getResources().getDrawable(R.drawable.m_sbs))
				.setContent(intentSBS));

		tabHost.addTab(tabHost.newTabSpec("tab3")
				.setIndicator("", getResources().getDrawable(R.drawable.m_kbs))
				.setContent(intentKBS));
		
		tabHost.addTab(tabHost.newTabSpec("tab4")
				.setIndicator("", getResources().getDrawable(R.drawable.m_cbs))
				.setContent(intentCBS));
		
/*
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i)
					.setBackgroundColor(Color.parseColor("#ffffff"));
		}
*/
	}
}
