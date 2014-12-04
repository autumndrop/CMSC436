package com.example.locationbasedtourguide;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;

public class AddStopActivity extends Activity {
    private static final int GALLERY_PHOTO = 1;
    TextView tourTitle;
    EditText stopName;
    EditText description;
    EditText address;
    EditText youtubeUri;
    ImageButton addPhotoButton;
    TextView numPhotosAdded;
    Button createStopButton;
    ArrayList<String> photoUriList;

    int numPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stop);

        photoUriList = new ArrayList<String>();

        // get and set tourTitle that was given as input on previous screen
        final String givenTourTitle = getIntent().getStringExtra("tourTitle");
        tourTitle = (TextView) findViewById(R.id.given_tour_title);
        tourTitle.setText(givenTourTitle);

        stopName = (EditText) findViewById(R.id.stop_name);
        description = (EditText) findViewById(R.id.description);
        address = (EditText) findViewById(R.id.stop_address);

        youtubeUri = (EditText) findViewById(R.id.youtube_uri);
        addPhotoButton = (ImageButton) findViewById(R.id.add_photo_button);

        numPhotos = 0;
        numPhotosAdded = (TextView) findViewById(R.id.num_stop_photos);
        numPhotosAdded.setText(numPhotos + " photos added");

        createStopButton = (Button) findViewById(R.id.create_stop_button);

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPhotoGallery = new Intent(Intent.ACTION_GET_CONTENT, null);
                toPhotoGallery.setType("image/*");
                toPhotoGallery.putExtra("return-data", true);
                startActivityForResult(toPhotoGallery, GALLERY_PHOTO);
            }
        });

        createStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmpty(stopName.getText().toString()) || isEmpty(description.getText().toString())
                        || isEmpty(address.getText().toString()) || isEmpty(youtubeUri.getText().toString())) {
                    if (isEmpty(stopName.getText().toString())) {
                        stopName.setError("Stop name cannot be empty.");
                    }

                    if (isEmpty(description.getText().toString())) {
                        description.setError("Description cannot be empty.");
                    }

                    if (isEmpty(address.getText().toString())) {
                        address.setError("Stop address cannot be empty.");
                    }

                    if (isEmpty(youtubeUri.getText().toString())) {
                        youtubeUri.setError("YouTube URI for stop video cannot be empty.");
                    }
                } else {
                    if (AddStopActivity.this.getIntent().getStringExtra("forResult") != null) {
                        AddStopActivity.this.getIntent().putExtra("stopName", stopName.getText().toString());
                        AddStopActivity.this.getIntent().putExtra("description", description.getText().toString());
                        AddStopActivity.this.getIntent().putExtra("address", address.getText().toString());
                        AddStopActivity.this.getIntent().putExtra("youtubeUri", youtubeUri.getText().toString());
                        AddStopActivity.this.getIntent().putStringArrayListExtra("images", photoUriList);
                        setResult(RESULT_OK, AddStopActivity.this.getIntent());
                        finish();
                    } else {
                        Intent toCurrentTour = new Intent(AddStopActivity.this, CurrentTourActivity.class);
                        toCurrentTour.putExtra("tourTitle", givenTourTitle);
                        toCurrentTour.putExtra("stopName", stopName.getText().toString());
                        toCurrentTour.putExtra("description", description.getText().toString());
                        toCurrentTour.putExtra("address", address.getText().toString());
                        toCurrentTour.putExtra("youtubeUri", youtubeUri.getText().toString());
                        toCurrentTour.putStringArrayListExtra("images", photoUriList);
                        startActivity(toCurrentTour);
                    }
                }

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("ENTERED: ", "onActivityResult");
        if (requestCode == GALLERY_PHOTO) {
            if (resultCode == RESULT_OK && data != null) {
                Log.i("RETURNED INTENT: ", "NOT NULL");
                Uri uri = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                cursor.moveToFirst();

                for (int i = 0; i < filePathColumn.length; i++) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[i]);
                    String photoPath = cursor.getString(columnIndex);
                    Log.i("PHOTO PATH: ", photoPath);
                    if (!photoUriList.contains(photoPath)) {
                        photoUriList.add(photoPath);
                        numPhotos = numPhotos + 1;
                        numPhotosAdded.setText(numPhotos + " photos added");
                    }
                }

                cursor.close();

            }
        }
    }
}