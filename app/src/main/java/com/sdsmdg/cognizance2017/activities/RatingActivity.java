package com.sdsmdg.cognizance2017.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.SessionManager;

import io.techery.properratingbar.ProperRatingBar;
import io.techery.properratingbar.RatingListener;

public class RatingActivity extends AppCompatActivity {

    private String imageId;
    private String cogniId;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        imageId = getIntent().getStringExtra("ImageKey");
        sessionManager = new SessionManager(getApplicationContext());
        cogniId = sessionManager.getUserCogniId();
        //Fetch the image details from local json
        final TextView ratingResult = (TextView) findViewById(R.id.text);
        TextView photographer = (TextView) findViewById(R.id.photographer);
        TextView caption = (TextView) findViewById(R.id.desc);
        TextView contact = (TextView) findViewById(R.id.contact_details);
        TextView location = (TextView) findViewById(R.id.location);
        final AppCompatButton button = (AppCompatButton) findViewById(R.id.submit_button);
        //Set the data to the view

        final ProperRatingBar ratingBar = (ProperRatingBar) findViewById(R.id.rating_bar);
        ratingBar.setListener(new RatingListener() {
            @Override
            public void onRatePicked(ProperRatingBar properRatingBar) {
                switch (ratingBar.getRating()) {
                    case 0: {
                        ratingResult.setVisibility(View.GONE);
                        break;
                    }
                    case 1: {
                        ratingResult.setText("Hated it");
                        break;
                    }
                    case 2: {
                        ratingResult.setText("Disliked it");
                        break;
                    }
                    case 3: {
                        ratingResult.setText("It's OK");
                        break;
                    }
                    case 4: {
                        ratingResult.setText("Liked it");
                        break;
                    }
                    case 5: {
                        ratingResult.setText("Loved it");
                        break;
                    }
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingBar.getRating() == 0)
                    Snackbar.make(button, "Incorrect Cognizance Id", Snackbar.LENGTH_SHORT).show();
                else {
                    //Send the data to firebase db
                    Snackbar.make(button, "Thanks for feedback!", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
}
