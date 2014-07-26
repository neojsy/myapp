package com.neojsy.SimplePodcast;

import java.util.Vector;

import com.neojsy.SimplePodcast.Episode.Custom;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PopupList extends ArrayAdapter<String> {
	private Vector items;
	private Activity activity;
	private int textViewResourceId;

	public PopupList(Context context, int textViewResourceId, Vector items,
			Activity activity) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.activity = activity;
		this.textViewResourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(textViewResourceId, null);
		}
		return searchRow(position, v);
	}

	public View searchRow(int position, View v) {
		Custom p = (Custom) items.get(position);
		if (p != null) {
			TextView text1 = (TextView) v.findViewById(R.id.tv_item);
			text1.setText(p.getText1());
		}
		return v;
	}
}
