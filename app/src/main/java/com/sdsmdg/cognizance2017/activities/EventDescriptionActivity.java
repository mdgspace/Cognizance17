package com.sdsmdg.cognizance2017.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sdsmdg.cognizance2017.R;

import static com.sdsmdg.cognizance2017.R.id.fab;

public class EventDescriptionActivity extends AppCompatActivity {
    TextView eventNameTextview, dateTextView, eventDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        eventNameTextview = (TextView) findViewById(R.id.event_name_textview);
        dateTextView = (TextView) findViewById(R.id.date_and_time_textview);
        eventDescriptionTextView = (TextView) findViewById(R.id.event_description_textview);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
