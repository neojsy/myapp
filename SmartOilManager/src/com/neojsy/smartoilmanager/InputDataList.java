package com.neojsy.smartoilmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class InputDataList extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		drawList();
	}
	
	private void drawList(){
		loadRecord();
		
		setContentView(R.layout.input_record_list);

		// Intent gintent = getIntent();
		// AddGr = gintent.getStringExtra("group");

		// loadCastList();

		ListView l1 = (ListView) findViewById(R.id.ListView01);
		l1.setAdapter(new EfficientAdapter(this));
		
		l1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				optDelete(position);
			}
		});	
	}
	
	private void optDelete(final int pos) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.checklist_opt_deleteq))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Util.print("delete id " + id);
								od.remove(pos);
								saveRecord();
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

	static ArrayList<CarData> od = new ArrayList<CarData>();
	static int listNumber;
	private void loadRecord(){
		FileRW f = new FileRW();
		od = f.readFile_CarData();
		listNumber = od.size();
		Util.print("Cardata size"+od.size());
	}
	
	private void saveRecord() {
		FileRW f = new FileRW();
		f.writeFile_CarData(od);
	}
	
	public void onResume() {
		super.onResume();

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
				convertView = mInflater.inflate(R.layout.list_input_record, null);
				holder = new ViewHolder();
				holder.text1 = (TextView) convertView.findViewById(R.id.inputdate);
				holder.text2 = (TextView) convertView.findViewById(R.id.inputkilo);
				holder.text3 = (TextView) convertView.findViewById(R.id.inputliterwon);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			
			CarData odn = od.get(position);
			holder.text1.setText(odn.getDateStr(true)+" "+odn.getTimeStr(true));
			holder.text2.setText(Util.Int2Str(odn.distance)+"km");
			holder.text3.setText(Util.Int2Str(odn.literwon)+"¿ø");

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
