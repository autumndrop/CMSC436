package com.example.locationbasedtourguide;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TourStopsAdapter extends ArrayAdapter {
    private ArrayList<Stop> stops;
    private static LayoutInflater inflater = null;

    public TourStopsAdapter(Context c, int resourceId, ArrayList<Stop> stops) {
        super(c, resourceId, stops);
        inflater = LayoutInflater.from(c);
        this.stops = stops;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        ViewHolder holder;

        Stop curr = stops.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            newView = inflater.inflate(R.layout.stop_view, parent, false);
            holder.thumbnail = (ImageView) newView.findViewById(R.id.thumbnail);
            holder.stopName = (TextView) newView.findViewById(R.id.created_stop_name);
            holder.stopDescription = (TextView) newView.findViewById(R.id.created_stop_description);
            newView.setTag(holder);

        } else {
            holder = (ViewHolder) newView.getTag();
        }

        if (curr.getImages().size() > 0) {
            Uri uri = Uri.parse(curr.getImages().get(0));
            holder.thumbnail.setImageURI(uri);
        } else {
            Uri uri = Uri.parse("../res/drawable-hdpi/no_photo.png");
            holder.thumbnail.setImageURI(uri);
        }

        holder.stopName.setText(curr.getStopName());
        holder.stopDescription.setText(curr.getDescription());

        return newView;
    }

    private class ViewHolder {
        ImageView thumbnail;
        TextView stopName;
        TextView stopDescription;
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
    public void clear() { stops.clear(); }

    public void add(Stop stop) { stops.add(stop); notifyDataSetChanged();}
}
