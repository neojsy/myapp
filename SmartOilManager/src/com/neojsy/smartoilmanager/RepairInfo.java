package com.neojsy.smartoilmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RepairInfo extends Activity {
	static int listNumber = 0;
	static ArrayList<RepairData> od = new ArrayList<RepairData>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repairinfo);

		loadRepairInfo();

		drawList();

	}

	private void drawList() {
		listNumber = od.size();
		ListView l1 = (ListView) findViewById(R.id.ListView01);
		l1.setAdapter(new EfficientAdapter(this));

		l1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				drawOptionPopup(position);
			}
		});
	}

	public void repairadd(View v){
		optAdd();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0,
				getResources().getString(R.string.repair_list_add_item));
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			//optAdd();
			optAdd();
			return true;
		}
		return false;
	}

	private void drawOptionPopup(final int position) {
		final CharSequence[] items = {
				getResources().getString(R.string.repair_list_change),
				getResources().getString(R.string.repair_list_modify),
				getResources().getString(R.string.repair_list_delete) };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(od.get(position).name);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {
					optChange(position);
				}
				if (item == 1) {
					optModify(position);
				}
				if (item == 2) {
					optDelete(position);
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	
	protected Dialog optAdd() {
		Context mContext = getApplicationContext();
		LayoutInflater factory = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View textEntryView = factory.inflate(R.layout.repair_input_dialog, (ViewGroup) findViewById(R.id.layout_root));
		return new AlertDialog.Builder(RepairInfo.this)
		.setTitle(getResources().getString(R.string.repair_list_input_title))
		.setView(textEntryView)
		.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				if((!((EditText) textEntryView.findViewById(R.id.repair_input_text1)).getText().toString().equals(""))
						&& (!((EditText) textEntryView.findViewById(R.id.repair_input_text2)).getText().toString().equals(""))){
					String name = ((EditText) textEntryView.findViewById(R.id.repair_input_text1)).getText().toString();
					int period = Integer.parseInt(((EditText) textEntryView.findViewById(R.id.repair_input_text2)).getText().toString());
	
					od.add(new RepairData(name, period, 0, null));
					saveRepairData();
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
	
	private Dialog optChange(final int pos) {
		Context mContext = getApplicationContext();
		LayoutInflater factory = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View textEntryView = factory.inflate(R.layout.repair_change_dialog, (ViewGroup) findViewById(R.id.layout_root));
		return new AlertDialog.Builder(RepairInfo.this)
		.setTitle(getResources().getString(R.string.repair_list_change_title))
		.setView(textEntryView)
		.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				if(!((EditText) textEntryView.findViewById(R.id.repair_change_text)).getText().toString().equals("")){
					
					int period = Integer.parseInt(((EditText) textEntryView.findViewById(R.id.repair_change_text)).getText().toString());
	
					RepairData mod = od.get(pos);
					mod.change = period;
					od.set(pos, mod);
					
					saveRepairData();
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
		final View textEntryView = factory.inflate(R.layout.repair_modify_dialog, (ViewGroup) findViewById(R.id.layout_root));
		
		((EditText) textEntryView.findViewById(R.id.repair_modify_text1)).setText(od.get(pos).name);
		((EditText) textEntryView.findViewById(R.id.repair_modify_text2)).setText(Util.Int2Str(od.get(pos).period));
		
		return new AlertDialog.Builder(RepairInfo.this)
		.setTitle(getResources().getString(R.string.repair_list_modify_title))
		.setView(textEntryView)
		.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				if((!((EditText) textEntryView.findViewById(R.id.repair_modify_text1)).getText().toString().equals(""))
						&& (!((EditText) textEntryView.findViewById(R.id.repair_modify_text2)).getText().toString().equals(""))){
				
					String name = ((EditText) textEntryView.findViewById(R.id.repair_modify_text1)).getText().toString();
					int period = Integer.parseInt(((EditText) textEntryView.findViewById(R.id.repair_modify_text2)).getText().toString());
	
					RepairData mod = od.get(pos);
					mod.period = period;
					mod.name = name;
					od.set(pos, mod);
					
					saveRepairData();
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
		builder.setMessage(getResources().getString(R.string.repair_msg_delete))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Util.print("delete id " + id);
								od.remove(pos);
								saveRepairData();
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

	private void loadRepairInfo() {
		FileRW f = new FileRW();
		od = f.readFile_RepairData();
		if (od.size() == 0) {
			// initialize
			Util.print("RepairInfo initialize");
			od.add(new RepairData(getResources().getString(
					R.string.repair_engine_oil), 10000, 0, null));
			od.add(new RepairData(getResources().getString(
					R.string.repair_mission_oil), 20000, 0, null));
			od.add(new RepairData(getResources().getString(
					R.string.repair_break_oil), 30000, 0, null));
			od.add(new RepairData(getResources().getString(
					R.string.repair_power_steering_oil), 30000, 0, null));
			od.add(new RepairData(getResources().getString(
					R.string.repair_spark_plug), 20000, 0, null));
			od.add(new RepairData(getResources().getString(
					R.string.repair_spark_plug_cable), 20000, 0, null));
			od.add(new RepairData(getResources().getString(
					R.string.repair_timing_belt), 80000, 0, null));
			od.add(new RepairData(getResources().getString(
					R.string.repair_break_pad), 20000, 0, null));
			od.add(new RepairData(getResources().getString(
					R.string.repair_break_lining), 30000, 0, null));
			od.add(new RepairData(getResources().getString(
					R.string.repair_tire_change), 10000, 0, null));
			saveRepairData();
		}

		listNumber = od.size();
		Util.print("RepairInfo listNumber " + listNumber);
		for (int i = 0; i < od.size(); i++) {
			Util.print("name " + od.get(i).name);
			Util.print("period " + od.get(i).period);
		}
	}

	private void saveRepairData() {
		FileRW f = new FileRW();
		f.writeFile_RepairData(od);
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

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_repair_info, null);
				holder = new ViewHolder();
				holder.text_repair_name = (TextView) convertView.findViewById(R.id.repaire_name);
				holder.text_repair_period = (TextView) convertView.findViewById(R.id.repair_period);
				holder.text_repair_pretime = (TextView) convertView.findViewById(R.id.repair_pretime);
				holder.text_repair_nextkilo = (TextView) convertView.findViewById(R.id.repair_nextkilo);
				//holder.text_repair_ingkilo = (TextView) convertView.findViewById(R.id.repari_nextkilo);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			RepairData odn = od.get(position);

			holder.text_repair_name.setText(odn.name);
			holder.text_repair_period.setText(" : " + Util.Int2Str(odn.period)	+ " km");

			holder.text_repair_pretime.setText(" : " + Util.Int2Str(odn.change)	+ " km");

			//holder.text_repair_ingkilo.setText(" : " + " " + " km");

			holder.text_repair_nextkilo.setText(" : " + Util.Int2Str(odn.change + odn.period) + " km");

			return convertView;
		}

		// http://www.androidhive.info/2012/02/android-custom-listview-with-image-and-text/

		static class ViewHolder {
			TextView text_repair_name;
			TextView text_repair_period;
			TextView text_repair_pretime;
			TextView text_repair_nextkilo;
			//TextView text_repair_ingkilo;
		}
	}
}
