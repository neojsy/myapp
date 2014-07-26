package com.neojsy.SimplePodcast;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<ItemPopup> 
{
	private Context m_Context = null;
	private List<ItemPopup> m_lstDevice = null;
	private final int resId;
	
//	private DMRIconLoader m_DMRIconLoader = null;
	
	public ListAdapter(Context context, int textViewResourceId, List<ItemPopup> objects) 
	{
		super(context, textViewResourceId, objects);
		
		m_Context = context;
		m_lstDevice = objects;
		resId = textViewResourceId;
	}
	
//	public void SetDMRIconLoader(DMRIconLoader _DMRIconLoader)
//	{
//		m_DMRIconLoader = _DMRIconLoader;
//	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		DMRViewHolder holder;
		
		View row = convertView;
		if (row == null)
		{
			LayoutInflater inflator = (LayoutInflater)m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflator.inflate(resId, null);
			
			holder = new DMRViewHolder();
			
			holder.ivIcon = (ImageView)row.findViewById(R.id.tv_image);
			holder.tvName = (TextView)row.findViewById(R.id.tv_item);
			
			row.setTag(holder);
		}
		else
		{
			holder = (DMRViewHolder)row.getTag();
		}
		
		ItemPopup item = m_lstDevice.get(position);
		
		holder.ivIcon.setImageResource(item.getImgID());
		holder.tvName.setText(item.getText());
		
		return row;
	}
	
	class DMRViewHolder 
	{
		ImageView ivIcon;
		TextView tvName;
	}
}