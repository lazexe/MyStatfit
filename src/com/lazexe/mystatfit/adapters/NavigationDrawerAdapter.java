package com.lazexe.mystatfit.adapters;

import java.util.List;

import com.lazexe.mystatfit.R;

import android.content.Context;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NavigationDrawerAdapter extends ArrayAdapter<DrawerItem> {

	Context context;
	List<DrawerItem> list;
	int layoutResourseID;
	
	public NavigationDrawerAdapter(Context context, int layoutResourseID, List<DrawerItem> list) {
		super(context, layoutResourseID, list);
		this.context = context;
		this.list = list;
		this.layoutResourseID = layoutResourseID;
	}
	
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		DrawerItemHolder drawerHolder;
		View view = convertView;
		
		if (view == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();
			
			view = inflater.inflate(layoutResourseID, parent, false);
			
			drawerHolder.itemName = (TextView) view.findViewById(R.id.drawer_itemname);
			drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);
			drawerHolder.title = (TextView) view.findViewById(R.id.drawer_title);
			
			drawerHolder.headerLayout = (LinearLayout) view.findViewById(R.id.header_layout);
			drawerHolder.itemLayout = (LinearLayout) view.findViewById(R.id.item_layout);

			view.setTag(drawerHolder);
		} else {
			drawerHolder = (DrawerItemHolder) view.getTag();
		}
		
		DrawerItem drawerItem  = this.list.get(position);
		
		if (drawerItem.getTitle() != null) {
			drawerHolder.headerLayout.setVisibility(LinearLayout.VISIBLE);
			drawerHolder.itemLayout.setVisibility(LinearLayout.INVISIBLE);
			drawerHolder.title.setText(drawerItem.getTitle());
		} else {
			drawerHolder.headerLayout.setVisibility(LinearLayout.INVISIBLE);
			drawerHolder.itemLayout.setVisibility(LinearLayout.VISIBLE);
			drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(drawerItem.getImageResourseID()));
			drawerHolder.itemName.setText(drawerItem.getItemName());
		}
		
		return view;
	}




	private static class DrawerItemHolder {
		TextView itemName, title;
		ImageView icon;
		LinearLayout headerLayout, itemLayout;
	}
}
