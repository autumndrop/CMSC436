package com.example.locationbasedtourguide;

import android.content.Intent;
import android.location.Location;

import java.util.ArrayList;

public class Stop {

    // the name of the stop to be displayed
    private String stopName;

    // any text about the stop the user input's
    private String description;

    private String address;

    // the URI of the video on youtube.com for the YouTube API
    private String videoUri;

    // the Bitmap array of image's on user's iPhone (possibly saved to it from the web)
    private ArrayList<String> images;

    public Stop() {
        images = new ArrayList<String>();
    }

    public Stop(String stopName, String description, String address,
                String videoUri, ArrayList<String> images) {
        this.stopName = stopName;
        this.description = description;
        this.address = address;
        this.videoUri = videoUri;
        this.images = images;
    }

    public Stop(Intent intent) {
        stopName = intent.getStringExtra("stopName");
        description = intent.getStringExtra("description");
        address = intent.getStringExtra("address");
        videoUri = intent.getStringExtra("youtubeUri");
        images = intent.getStringArrayListExtra("images");
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public Intent packageToIntent() {
        Intent intent = new Intent();
        intent.putExtra("stopName", stopName);
        intent.putExtra("description", description);
        intent.putExtra("youtubeUri", videoUri);
        intent.putStringArrayListExtra("images", images);
        return intent;
    }

    @Override
    public String toString() {
        return "Stop Name: " + stopName + " Description: " + description;

    }
}
