package com.sdsmdg.cognizance2017.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.SessionManager;
import com.sdsmdg.cognizance2017.models.PictureCompetition;

import io.techery.properratingbar.ProperRatingBar;
import io.techery.properratingbar.RatingListener;

import static com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.Q;

public class RatingActivity extends AppCompatActivity {

    private String imageId;
    private String cogniId;
    private SessionManager sessionManager;
    private DatabaseReference mDatabase;
    private String rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        imageId = getIntent().getStringExtra("ImageKey").substring(1);
        sessionManager = new SessionManager(getApplicationContext());
        cogniId = sessionManager.getUserCogniId();
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
                rating = String.valueOf(ratingBar.getRating());
                if (ratingBar.getRating() == 0)
                    Snackbar.make(button, "Incorrect Cognizance Id", Snackbar.LENGTH_SHORT).show();
                else {
                    //Send the data to firebase db
                    submitRating();

                    Snackbar.make(button, "Thanks for feedback!", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
    private void submitRating(){
        Query query = mDatabase.child(imageId).orderByChild("cogniId").equalTo(cogniId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null) {
                    mDatabase.child(imageId).push().setValue(new PictureCompetition(cogniId, rating));
                    Toast.makeText(RatingActivity.this, ""+cogniId+"rat"+rating, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String key = dataSnapshot.getKey();
                mDatabase.child(imageId).child(key).child("rating").setValue(rating);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
