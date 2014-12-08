package com.example.locationbasedtourguide;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MapActivity extends Activity implements 
	GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
	
	
	LocationClient mLocationClient;
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final String STORAGE_FILE = "app_tours";
	GoogleMap map;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        mLocationClient = new LocationClient(this,this,this);
        map.setMyLocationEnabled(true);

        Geocoder geocoder= new Geocoder(this);
        try{
        FileInputStream inputStream = openFileInput(STORAGE_FILE);
        int c;
        String data = "";
        while ((c = inputStream.read()) != -1) {
            data = data + Character.toString((char) c);
        }

        JSONObject jsonFormatted = new JSONObject(data);
        JSONArray tours = jsonFormatted.getJSONArray("tours");
        for(int i = 0; i < tours.length();i++){
        	JSONObject tour = tours.getJSONObject(i);
        	String tourName= tour.getString("tour_name");
        	double startLat= geocoder.getFromLocationName(tour.getJSONArray("stops").getJSONObject(0).getString("address"), 1).get(0).getLatitude();
        	double startLng= geocoder.getFromLocationName(tour.getJSONArray("stops").getJSONObject(0).getString("address"), 1).get(0).getLongitude();
        	LatLng stopPos=new LatLng(startLat,startLng);
        	map.addMarker(new MarkerOptions()
        	.title(tourName)
        	.snippet(tour.getJSONArray("stops").getJSONObject(0).getString("address"))
        	.position(stopPos));
        	
        }
        } catch(Exception e){
            e.printStackTrace();
            Log.i("ERROR: ", "READING OR UPDATING JSON");
        }
        
        
 
    }
    

    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		/*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showDialog(connectionResult.getErrorCode());
        }
	}
	
	 public static class ErrorDialogFragment extends DialogFragment {
	        // Global field to contain the error dialog
	        private Dialog mDialog;
	        // Default constructor. Sets the dialog field to null
	        public ErrorDialogFragment() {
	            super();
	            mDialog = null;
	        }
	        // Set the dialog to display
	        public void setDialog(Dialog dialog) {
	            mDialog = dialog;
	        }
	        // Return a Dialog to the DialogFragment.
	        @Override
	        public Dialog onCreateDialog(Bundle savedInstanceState) {
	            return mDialog;
	        }
	    }
	 
	 @Override
	    protected void onActivityResult(
	            int requestCode, int resultCode, Intent data) {
	        // Decide what to do based on the original request code
	        switch (requestCode) {
	            
	            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
	            /*
	             * If the result code is Activity.RESULT_OK, try
	             * to connect again
	             */
	                switch (resultCode) {
	                    case Activity.RESULT_OK :
	                    /*
	                     * Try the request again
	                     */
	                    
	                    break;
	                }

	        }
	     }

	    private boolean servicesConnected() {
	        // Check that Google Play services is available
	        int resultCode =
	                GooglePlayServicesUtil.
	                        isGooglePlayServicesAvailable(this);
	        // If Google Play services is available
	        if (ConnectionResult.SUCCESS == resultCode) {
	            // In debug mode, log the status
	            Log.d("Location Updates",
	                    "Google Play services is available.");
	            // Continue
	            return true;
	        // Google Play services was not available for some reason.
	        // resultCode holds the error code.
	        } else {
	            // Get the error dialog from Google Play services
	            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
	                    resultCode,
	                    this,
	                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

	            // If Google Play services can provide an error dialog
	            if (errorDialog != null) {
	                // Create a new DialogFragment for the error dialog
	                ErrorDialogFragment errorFragment =
	                        new ErrorDialogFragment();
	                // Set the dialog in the DialogFragment
	                errorFragment.setDialog(errorDialog);
	                // Show the error dialog in the DialogFragment
	                errorFragment.show(getFragmentManager(),
	                        "Location Updates");
	            }
	        }
	        return false;
	    }
	
	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
        //.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        Location here = mLocationClient.getLastLocation();
        if(here == null){

        //    Toast.makeText(this, "NULL", Toast.LENGTH_SHORT).show();
        }
        else
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(here.getLatitude(), here.getLongitude()), 13));
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
      //  Toast.makeText(this, "Disconnected. Please re-connect.",
        //        Toast.LENGTH_SHORT).show();
		
	}
    
}