package com.neojsy.SimplePodcast;


import android.app.TabActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;


public class AddFamousCast extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final TabHost tabHost = getTabHost();

    	Intent intentPol = new Intent(AddFamousCast.this, AddPolitic.class);
    	intentPol.putExtra("group", "POL");
    	
    	Intent intentCul = new Intent(AddFamousCast.this, AddCulture.class);
    	intentCul.putExtra("group", "CUL");
    	
    	Intent intentEco = new Intent(AddFamousCast.this, AddEconomy.class);
    	intentEco.putExtra("group", "ECO");
    	
    	Intent intentEtc = new Intent(AddFamousCast.this, AddEtc.class);
    	intentEtc.putExtra("group", "ETC");
		
		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator(getResources().getString(R.string.mnu_famo_politic), getResources().getDrawable(R.drawable.fa_pol))
				.setContent(intentPol));

		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator(getResources().getString(R.string.mnu_famo_culture), getResources().getDrawable(R.drawable.fa_cul))
				.setContent(intentCul));

		tabHost.addTab(tabHost.newTabSpec("tab3")
				.setIndicator(getResources().getString(R.string.mnu_famo_economy), getResources().getDrawable(R.drawable.fa_eco))
				.setContent(intentEco));
		
		tabHost.addTab(tabHost.newTabSpec("tab4")
				.setIndicator(getResources().getString(R.string.mnu_famo_etc), getResources().getDrawable(R.drawable.fa_etc))
				.setContent(intentEtc));
		
/*
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i)
					.setBackgroundColor(Color.parseColor("#ffffff"));
		}
*/
	}
}