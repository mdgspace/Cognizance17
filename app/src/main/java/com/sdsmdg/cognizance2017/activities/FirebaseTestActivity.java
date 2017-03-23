package com.sdsmdg.cognizance2017.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.models.PictureCompetition;

import static android.R.attr.data;

public class FirebaseTestActivity extends AppCompatActivity {
     EditText cogniIdText,ratingText;
    Button button,queryButton;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_test);
        cogniIdText = (EditText) findViewById(R.id.textView1);
        ratingText = (EditText) findViewById(R.id.textView2);
        button = (Button) findViewById(R.id.button);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        queryButton = (Button) findViewById(R.id.queryButton);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query();
            }
        });

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

        mDatabase.child("a1").push().setValue(picture);
    }
    private  void query(){
        Query query = mDatabase.child("ImageId").orderByChild("cogniId").equalTo("cogni123");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null)
                    Toast.makeText(FirebaseTestActivity.this, "This worked!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FirebaseTestActivity.this, "Check your network connection!!", Toast.LENGTH_SHORT).show();
            }
        });

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PictureCompetition picture = dataSnapshot.getValue(PictureCompetition.class);
                String key = dataSnapshot.getKey();
                if (picture.getCogniId()==null)
                    Toast.makeText(FirebaseTestActivity.this, "null", Toast.LENGTH_SHORT).show();
               else {
                    Toast.makeText(FirebaseTestActivity.this, "Cogni Id:" + key, Toast.LENGTH_SHORT).show();
                    mDatabase.child("ImageId").child(key).child("rating").setValue("sammy rocks");
                }


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
                Toast.makeText(FirebaseTestActivity.this, "Check your network connection!!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
