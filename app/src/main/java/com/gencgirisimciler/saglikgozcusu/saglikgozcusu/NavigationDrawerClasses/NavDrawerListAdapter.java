package com.gencgirisimciler.saglikgozcusu.saglikgozcusu.NavigationDrawerClasses;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.gencgirisimciler.saglikgozcusu.saglikgozcusu.R;

import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	public static ArrayList<Boolean>  tikliMiArray ;


	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems) {
			this.context = context;
			this.navDrawerItems = navDrawerItems;

		tikliMiArray = new ArrayList<>(getCount());
		for (int i = 0; i < getCount(); i++) {
			tikliMiArray.add(true);
		}

		}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
			holder = new ViewHolder();

			holder.textView = (TextView) convertView.findViewById(R.id.title);
			holder.checkBox = (com.rey.material.widget.CheckBox) convertView.findViewById(R.id.check);
			if(tikliMiArray.get(position))
				holder.checkBox.setChecked(true);

			else
				holder.checkBox.setChecked(false);

			convertView.setTag(holder);

		}
		// view is recycling
		else {
			holder = (ViewHolder) convertView.getTag();
			if(tikliMiArray.get(position))
			{
				holder.checkBox.setChecked(true);
//				tikliMiArray[position] = true;
				holder.checkBox.setTextColor(Color.parseColor("#23b4f5"));
			}
			else
			{
				holder.checkBox.setChecked(false);
//				tikliMiArray[position] = false;
				holder.checkBox.setTextColor(Color.parseColor("#33999999"));
			}
		}

		holder.textView.setText(navDrawerItems.get(position).getTitle());

		holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked) {
					tikliMiArray.set(position,false);
					holder.textView.setTextColor(Color.parseColor("#23b4f5"));
				}

				else {
					tikliMiArray.set(position,true);
					holder.textView.setTextColor(Color.parseColor("#33999999"));
				}
			}
		});
		return convertView;
	}

	private static class ViewHolder {

		public TextView textView;
		public com.rey.material.widget.CheckBox checkBox;

	}

}
