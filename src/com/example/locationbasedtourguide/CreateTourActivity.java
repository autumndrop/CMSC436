package com.example.locationbasedtourguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateTourActivity extends Activity {
    EditText tourTitle;
    Button addStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);

        tourTitle = (EditText) findViewById(R.id.tour_title);
        addStopButton = (Button) findViewById(R.id.add_first_stop_btn);

        addStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddStop = new Intent(CreateTourActivity.this, AddStopActivity.class);
                toAddStop.putExtra("tourTitle", tourTitle.getText().toString());
                startActivity(toAddStop);
            }
        });
    }

}
