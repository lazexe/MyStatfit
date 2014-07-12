package com.lazexe.mystatfit.spinnernavigation;

import java.util.ArrayList;

import com.lazexe.mystatfit.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleNavigationAdapter extends BaseAdapter {
	
	private ImageView imgIcon;
	private TextView txtTitle;
	private ArrayList<SpinnerNavigationItem> spinnerNavigationItem;
	private Context context;
	
	public TitleNavigationAdapter(Context context, ArrayList<SpinnerNavigationItem> items) {
		this.context = context;
		this.spinnerNavigationItem = items;
	}
	
	@Override
	public int getCount() {
		return spinnerNavigationItem.size();
	}

	@Override
	public Object getItem(int position) {
		return spinnerNavigationItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item_title_navigation, null);
		}
		
		imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
		txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
		
		imgIcon.setImageResource(spinnerNavigationItem.get(position).getIcon());
		imgIcon.setVisibility(View.GONE);
		txtTitle.setText(spinnerNavigationItem.get(position).getTitle());
		
		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item_title_navigation, null);
		}
		
		imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
		txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
		
		imgIcon.setImageResource(spinnerNavigationItem.get(position).getIcon());
		imgIcon.setVisibility(View.GONE);
		txtTitle.setText(spinnerNavigationItem.get(position).getTitle());
		
		return convertView;
	}
	
	

}
