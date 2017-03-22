package com.sdsmdg.cognizance2017.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.models.PictureCompetition;

public class FirebaseTestActivity extends AppCompatActivity {
     EditText cogniIdText,ratingText;
    Button button;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_test);
        cogniIdText = (EditText) findViewById(R.id.textView1);
        ratingText = (EditText) findViewById(R.id.textView2);
        button = (Button) findViewById(R.id.button);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRating();
                startActivity(new Intent(FirebaseTestActivity.this,MainActivity.class));

            }
        });
    }

    private void submitRating(){
        PictureCompetition picture = new PictureCompetition(cogniIdText.getText().toString().trim(),ratingText.getText().toString().trim());

        mDatabase.child("ImageId").push().setValue(picture);
    }
}
