package com.example.locationbasedtourguide;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public class TourStopsAdapter extends BaseAdapter {
	private ArrayList<Stop> stops;
	private final Context context;
	
	public TourStopsAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return stops.size();
	}

	@Override
	public Object getItem(int position) {
		return stops.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
}
