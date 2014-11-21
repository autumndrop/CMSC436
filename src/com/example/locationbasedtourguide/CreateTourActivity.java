package com.example.locationbasedtourguide;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CreateTourActivity extends ListActivity {
	private static final int ADD_STOP_REQUEST = 0;
	EditText tourTitle;
	TourStopsAdapter tourStopsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_tour);
		
		// get a reference to the EditText field for the tour title
		tourTitle = (EditText) findViewById(R.id.tourTitle);
		
		// get a reference to the ListView which holds user-created stops
		ListView stops = getListView();
		
		// create onItemClick functionality for an item in the list
		stops.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Stop stopToEdit = (Stop) parent.getItemAtPosition(position);
				
			}
			
		});
		
		stops.setFooterDividersEnabled(true);
		
		// inflate the footer view to make it a click-able entity to start 'AddOrEditStopActivity'
		TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.footer_view, null);
		
		// create onClick functionality for the list footer
		footerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent toAddStop = new Intent(CreateTourActivity.this, AddOrEditStopActivity.class);
				startActivityForResult(toAddStop, ADD_STOP_REQUEST);	
			}
			
		});
		
		// add the footer to the ListView
		stops.addFooterView(footerView);
		
		// collect the state of each component of the activity bind it to the ListView
		tourStopsAdapter = new TourStopsAdapter(getApplicationContext());
		stops.setAdapter(tourStopsAdapter);
		
		
	}
}
