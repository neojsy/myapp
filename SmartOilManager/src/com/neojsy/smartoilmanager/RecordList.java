package com.neojsy.smartoilmanager;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RecordList extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * l1.setOnItemClickListener(new OnItemClickListener() { public void
		 * onItemClick(AdapterView<?> parent, View view, int position, long id)
		 * { // Log.d(TAG, "position : "+position); if (!nowParsing) { if
		 * (!Util.isConnected(AddCBS.this)) { Toast.makeText( AddCBS.this,
		 * getResources().getString( R.string.msg_networkerror),
		 * Toast.LENGTH_SHORT).show(); } else { addCast(position); } } else {
		 * Toast.makeText(AddCBS.this,
		 * getResources().getString(R.string.msg_notcomwork),
		 * Toast.LENGTH_SHORT).show(); } } });
		 */
	}

	public void onResume() {
		super.onResume();
		setContentView(R.layout.oil_record_list);

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
	
	private void drawOptionPopup(final int position) {
		final CharSequence[] items = {
				//getResources().getString(R.string.oilinput_opt_view),
				getResources().getString(R.string.oilinput_opt_modify),
				getResources().getString(R.string.oilinput_opt_delete) };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//builder.setTitle(od.get(position).name);
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
					optDelete(position);
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void optViewSMS(final int pos) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(od.get(pos).sms)
				.setTitle(getResources().getString(R.string.oilinput_opt_view))
				.setCancelable(false)
				.setNegativeButton(getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private Dialog optModify(final int pos) {
		Context mContext = getApplicationContext();
		LayoutInflater factory = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View textEntryView = factory.inflate(R.layout.oiling_modity_diag, (ViewGroup) findViewById(R.id.layout_root));
		
		((EditText) textEntryView.findViewById(R.id.oiling_modify_text1)).setText(od.get(pos).brand);
		((EditText) textEntryView.findViewById(R.id.oiling_modify_text2)).setText(od.get(pos).won);
		
		return new AlertDialog.Builder(RecordList.this)
		.setTitle(getResources().getString(R.string.oilinput_opt_modify))
		.setView(textEntryView)
		.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				if((!((EditText) textEntryView.findViewById(R.id.oiling_modify_text1)).getText().toString().equals(""))
						&& (!((EditText) textEntryView.findViewById(R.id.oiling_modify_text2)).getText().toString().equals(""))){
				
					String name = ((EditText) textEntryView.findViewById(R.id.oiling_modify_text1)).getText().toString();
					String won = ((EditText) textEntryView.findViewById(R.id.oiling_modify_text2)).getText().toString();
	
					OilData mod = od.get(pos);
					mod.brand = name;
					mod.won = won;
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
		builder.setMessage(getResources().getString(R.string.oilinput_opt_deleteq))
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
		f.writeFile_OilData(od);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0,
				getResources().getString(R.string.oilinput_add));
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(RecordList.this, OilingAddItem.class);
			startActivity(intent);
			return true;
		}
		return false;
	}

	
	static ArrayList<OilData> od = new ArrayList<OilData>();
	static int listNumber;
	private void loadRecord(){
		FileRW f = new FileRW();
		od = f.readFile_OilData();
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
				convertView = mInflater.inflate(R.layout.list_oiling_record, null);
				holder = new ViewHolder();
				holder.text1 = (TextView) convertView.findViewById(R.id.oilingtime);
				holder.text2 = (TextView) convertView.findViewById(R.id.oilingcard);
				holder.text3 = (TextView) convertView.findViewById(R.id.oilingwon);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			
			OilData odn = od.get(position);
			holder.text1.setText(odn.getDateStr(true)+" "+odn.getTimeStr(true));
			holder.text2.setText(odn.brand);
			holder.text3.setText(odn.won+"¿ø");

			return convertView;
		}

		// http://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/

		static class ViewHolder {
			TextView text1;
			TextView text2;
			TextView text3;
		}
	}
}
