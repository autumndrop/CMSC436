package com.example.locationbasedtourguide;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.os.Build;



public class MainActivity extends TabActivity {

    // TabSpec Names
    private static final String MAP_SPEC = "Map";
    private static final String NEARBY_SPEC = "Nearby";
    private static final String CREATE_SPEC = "Create";
    private static final String SETTING_SPEC = "Setting";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        TabHost tabHost = getTabHost();
        
        // Map Tab
        TabSpec mapSpec = tabHost.newTabSpec(MAP_SPEC);
        // Tab Icon
        mapSpec.setIndicator(MAP_SPEC);
        Intent mapIntent = new Intent(this, MapActivity.class);
        // Tab Content
        mapSpec.setContent(mapIntent);
        
//        // Nearby Tab
//        TabSpec nearbySpec = tabHost.newTabSpec(NEARBY_SPEC);
//        // Tab Icon
//        nearbySpec.setIndicator(NEARBY_SPEC);
//        Intent nearbyIntent = new Intent(this, NearbyActivity.class);
//        // Tab Content
//        nearbySpec.setContent(nearbyIntent);      
        
        // Create Tab
        TabSpec createSpec = tabHost.newTabSpec(CREATE_SPEC);
        // Tab Icon
        createSpec.setIndicator(CREATE_SPEC);
        Intent createIntent = new Intent(this, CreateTourActivity.class);
        // Tab Content
        createSpec.setContent(createIntent);
        
     // Setting Tab
        TabSpec settingSpec = tabHost.newTabSpec(SETTING_SPEC);
        // Tab Icon
        settingSpec.setIndicator(SETTING_SPEC);
        Intent settingIntent = new Intent(this, SettingActivity.class);
        // Tab Content
        settingSpec.setContent(settingIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(mapSpec); // Adding Inbox tab
        tabHost.addTab(createSpec); // Adding Outbox tab 
        tabHost.addTab(settingSpec);
        
//        Button b = (Button) findViewById(R.id.bLaunchMapActivity);
//        b.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent i=new Intent(MainActivity.this, MapActivity.class);
//				startActivity(i);
//			}
//        	
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
