package com.example.locationbasedtourguide;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class CurrentTourActivity extends ListActivity {
    private static final int ADD_STOP_REQUEST = 1;
    private final String STORAGE_FILE = "app_tours";
    private ArrayList<Stop> createdStops = new ArrayList<Stop>();
    TourStopsAdapter theAdapter;
    TextView currTourName;
    ImageButton createTourButton;
    String tourTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ENTERED", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_tour);

        Intent sentIntent = getIntent();

        tourTitle = sentIntent.getStringExtra("tourTitle");

        currTourName = (TextView) findViewById(R.id.curr_tour_name);
        currTourName.setText(tourTitle);
        currTourName.setTypeface(null, Typeface.BOLD);

        createTourButton = (ImageButton) findViewById(R.id.add_new_stop_btn);

        // onClick go to MainActivity.java and write the Tour to Internal Storage
        createTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewTour(createdStops);
                Toast.makeText(getApplicationContext(), "Tour " + tourTitle + " has been created!", Toast.LENGTH_SHORT).show();
                Intent home = new Intent(CurrentTourActivity.this, MainActivity.class);
                startActivity(home);
            }
        });

        theAdapter = new TourStopsAdapter(this, R.layout.stop_view, createdStops);

        Stop justCreated = new Stop(sentIntent.getStringExtra("stopName"), sentIntent.getStringExtra("description"),
                sentIntent.getStringExtra("address"), sentIntent.getStringExtra("youtubeUri"),
                sentIntent.getStringArrayListExtra("images"));
        theAdapter.add(justCreated);

        getListView().setFooterDividersEnabled(true);
        TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.footer_view, null);
        getListView().addFooterView(footerView);

        // onClick go to addStopActivity
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddStop = new Intent(CurrentTourActivity.this, AddStopActivity.class);
                toAddStop.putExtra("tourTitle", tourTitle);
                toAddStop.putExtra("forResult", "yes");
                startActivityForResult(toAddStop, ADD_STOP_REQUEST);
            }
        });

        setListAdapter(theAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_STOP_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.i("ON RESULT", "CAME FROM HERE FIRST");
                Stop recvdStop = new Stop(data);
                theAdapter.add(recvdStop);
            }
        }
    }

    protected void saveNewTour(ArrayList<Stop> stops) {
        if (fileExistence(STORAGE_FILE)) {
            try {
                FileInputStream inputStream = openFileInput(STORAGE_FILE);
                int c;
                String data = "";
                while ((c = inputStream.read()) != -1) {
                    data = data + Character.toString((char) c);
                }

                JSONObject jsonFormatted = new JSONObject(data);
                JSONArray tours = jsonFormatted.getJSONArray("tours");

                // build the JSONObject tour to be added to the array of Tours
                JSONObject tour = new JSONObject();
                tour.put("tour_name", tourTitle);

                JSONArray tourStops = new JSONArray();
                for (Stop s : createdStops) {
                    // create new stop object to be added to JSONObject tour
                    JSONObject stop = new JSONObject();
                    stop.put("stop_name", s.getStopName());
                    stop.put("description", s.getDescription());
                    stop.put("address", s.getAddress());
                    stop.put("youtubeUri", s.getVideoUri());

                    JSONArray photoUris = new JSONArray();
                    for (String photo : s.getImages()) {
                        photoUris.put(photo);
                    }
                    stop.put("photoUris", photoUris);
                    tourStops.put(stop);
                }

                tour.put("stops", tourStops);
                tours.put(tour);

                // write this JSON to a file in Internal Storage
                FileOutputStream outputStream = openFileOutput(STORAGE_FILE, Context.MODE_PRIVATE);
                outputStream.write(jsonFormatted.toString().getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("ERROR: ", "READING OR UPDATING JSON");
            }
        } else {
            try {
                JSONObject entireFile = new JSONObject();
                JSONArray tours = new JSONArray();

                // build the JSONObject tour to be added to the array of Tours
                JSONObject tour = new JSONObject();
                tour.put("tour_name", tourTitle);

                JSONArray tourStops = new JSONArray();
                for (Stop s : createdStops) {
                    // create new stop object to be added to JSONObject tour
                    JSONObject stop = new JSONObject();
                    stop.put("stop_name", s.getStopName());
                    stop.put("description", s.getDescription());
                    stop.put("address", s.getAddress());
                    stop.put("youtubeUri", s.getVideoUri());

                    JSONArray photoUris = new JSONArray();
                    for (String photo : s.getImages()) {
                        photoUris.put(photo);
                    }
                    stop.put("photoUris", photoUris);
                    tourStops.put(stop);
                }

                tour.put("stops", tourStops);

                // append the JSONObject tour to the end of the tours array
                tours.put(tour);

                // add outermost object tours which encapsulates all other data
                entireFile.put("tours", tours);

                // write this JSON to a file in Internal Storage
                FileOutputStream outputStream = openFileOutput(STORAGE_FILE, Context.MODE_PRIVATE);
                outputStream.write(entireFile.toString().getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("ERROR: ", "BUILDING OR WRITING JSON");
            }
        }
    }

    public boolean fileExistence(String filename) {
        File file = getBaseContext().getFileStreamPath(filename);
        return file.exists();
    }
}