package com.neojsy.smartoilmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class OilingSetFilter extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onResume() {
		super.onResume();
		setContentView(R.layout.oil_filter_list);

		drawList();
	}
	
	private void drawList(){
		loadRecord();
		
		ListView l1 = (ListView) findViewById(R.id.ListView01);
		l1.setAdapter(new EfficientAdapter(this));
		
		l1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				drawOptionPopup(position);
			}
		});		
	}
	
	public void filteradd(View v){
		optAdd();
	}
	
	private void drawOptionPopup(final int position) {
		final CharSequence[] items = {
				//getResources().getString(R.string.oilinput_opt_view),
				getResources().getString(R.string.oiling_filter_mod),
				getResources().getString(R.string.oiling_filter_del) };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(od.get(position));
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				/*
				if (item == 0) {
					optViewSMS(position);
				}*/
				if (item == 0) {
					optModify(position);
				}
				if (item == 1) {
					if(od.size() == 1){
						new AlertDialog.Builder(OilingSetFilter.this)
						.setMessage(getResources().getString(R.string.oiling_filter_needone))
					    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
					    	public void onClick(DialogInterface dialog, int whichButton) {
					    		/* User clicked OK so do some stuff */
					      }
					    })
					    .show();
					}
					else{
						optDelete(position);
					}
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private Dialog optAdd() {
		Context mContext = getApplicationContext();
		LayoutInflater factory = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View textEntryView = factory.inflate(R.layout.oiling_filter_change_dialog, (ViewGroup) findViewById(R.id.layout_root));
		
		return new AlertDialog.Builder(OilingSetFilter.this)
		.setTitle(getResources().getString(R.string.oiling_filter_add))
		.setView(textEntryView)
		.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				if((!((EditText) textEntryView.findViewById(R.id.oil_filter_str)).getText().toString().equals(""))){
				
					String name = ((EditText) textEntryView.findViewById(R.id.oil_filter_str)).getText().toString();
	
					od.add(name);
					
					saveOilingData();
					drawList();
				}
			}
		})
		.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

		    
			}
		})
		.show();
	}
	
	private Dialog optModify(final int pos) {
		Context mContext = getApplicationContext();
		LayoutInflater factory = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View textEntryView = factory.inflate(R.layout.oiling_filter_change_dialog, (ViewGroup) findViewById(R.id.layout_root));
		
		((EditText) textEntryView.findViewById(R.id.oil_filter_str)).setText(od.get(pos));
		
		return new AlertDialog.Builder(OilingSetFilter.this)
		.setTitle(getResources().getString(R.string.oiling_filter_mod))
		.setView(textEntryView)
		.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				if((!((EditText) textEntryView.findViewById(R.id.oil_filter_str)).getText().toString().equals(""))){
				
					String name = ((EditText) textEntryView.findViewById(R.id.oil_filter_str)).getText().toString();
	
					String mod = od.get(pos);
					mod = name;

					od.set(pos, mod);
					
					saveOilingData();
					drawList();
				}
			}
		})
		.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

		    
			}
		})
		.show();
	}
	
	private void optDelete(final int pos) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.oiling_filter_del))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Util.print("delete id " + id);
								od.remove(pos);
								saveOilingData();
								drawList();
							}
						})
				.setNegativeButton(getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void saveOilingData() {
		FileRW f = new FileRW();
		f.writeFile_FilterData(od);
	}
	
	
	static ArrayList<String> od = new ArrayList<String>();
	static int listNumber;
	private void loadRecord(){
		FileRW f = new FileRW();
		od = f.readFile_FilterData();
		listNumber = od.size();
	}
	

	private static class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		public int getCount() {
			return listNumber;
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
				convertView = mInflater.inflate(R.layout.list_oiling_filter, null);
				holder = new ViewHolder();
				holder.text1 = (TextView) convertView.findViewById(R.id.oilingfilter);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			
			String odn = od.get(position);
			holder.text1.setText(odn);

			return convertView;
		}

		// http://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/

		static class ViewHolder {
			TextView text1;
		}
	}
}
