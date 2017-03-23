package com.sdsmdg.cognizance2017.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.models.EventModel;

import io.realm.Realm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.sdsmdg.cognizance2017.activities.MainActivity.BASE_URL;
import static com.sdsmdg.cognizance2017.activities.MainActivity.mainAct;


public class EventDescriptionActivity extends AppCompatActivity {
    private TextView eventDate, eventDescription, eventLocation ,eventContact;
    private Realm realm;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        //Is related to transparent toolbar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        realm = Realm.getDefaultInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.event_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialog = new ProgressDialog(this);
        dialog.setMessage("fetching data");
        eventDate = (TextView)findViewById(R.id.event_time);
        eventDescription = (TextView)findViewById(R.id.event_description);
        eventLocation = (TextView)findViewById(R.id.event_location);
        eventContact = (TextView) findViewById(R.id.contact_details);

        // accessing data from cognizance website
        dialog.setCancelable(false);
        if(((MainActivity)mainAct).isNetworkAvailable()) {
            dialog.show();
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(BASE_URL)
                    .build();
            final DataInterface api = adapter.create(DataInterface.class);
            api.getEventById(getIntent().getIntExtra("id", 6), new Callback<JsonObject>() {
                @Override
                public void success(JsonObject json, Response response) {
                    final JsonObject jsonObject = json;

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.createOrUpdateObjectFromJson(EventModel.class, jsonObject.toString());
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            final EventModel model = realm.where(EventModel.class).equalTo("id", getIntent().getIntExtra("id", 6)).findFirst();
                            CollapsingToolbarLayout appBar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
                            appBar.setTitle(model.getName());

                            String timings = "Timings :-";
                            if(!model.getDay1().isEmpty()){
                                timings += "\nDay 1 (24 March) : " + model.getTime(model.getDay1());
                            }
                            if(!model.getDay2().equals("")){
                                timings += "\nDay 2 (25 March) : " + model.getTime(model.getDay2());
                            }
                            if(!model.getDay3().equals("")){
                                timings += "\nDay 3 (26 March) : " + model.getTime(model.getDay3());
                            }
                            eventDate.setText(timings);
                            String description = Html.fromHtml(model.getDescription()).toString();
                            eventDescription.setText(description);
                            eventLocation.setText(model.getVenue());
                            eventLocation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent mapIntent = new Intent(EventDescriptionActivity.this,MapsActivity.class);
                                    if(!model.getVenue().equals(""))
                                    mapIntent.putExtra("location",model.getVenue());
                                    Log.d("location",model.getVenue());
                                    startActivity(mapIntent);
                                }
                            });
                            eventContact.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String[] imageMode = {model.getContact_person()+" : " + model.getContact_phone(),
                                            model.getContact_person2()+ " : " + model.getContact_phone2(),
                                            "E-mail : " + model.getContact_email()};
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EventDescriptionActivity.this);
                                    builder.setTitle("Contact Details")
                                            .setItems(imageMode, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if(which==0){
                                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                                        intent.setData(Uri.parse("tel:"+model.getContact_phone()));
                                                        startActivity(intent);                                                     }
                                                    else if(which == 1) {
                                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                                        intent.setData(Uri.parse("tel:"+model.getContact_phone2()));
                                                        startActivity(intent);
                                                    }else {
                                                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                                        intent.setType("text/html");
                                                        intent.putExtra(Intent.EXTRA_SUBJECT,  "Query about " + model.getName());
                                                        intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { model.getContact_email() });
                                                        Toast.makeText(EventDescriptionActivity.this, model.getContact_email(), Toast.LENGTH_SHORT).show();
                                                        startActivity(Intent.createChooser(intent, "Send Email"));
                                                    }
                                                }
                                            });
                                    builder.show();
                                }
                            });
                            dialog.dismiss();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {

                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(EventDescriptionActivity.this, "error while fetching data", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }else{
            final EventModel model = realm.where(EventModel.class).equalTo("id", getIntent().getIntExtra("id", 6)).findFirst();
            CollapsingToolbarLayout appBar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            appBar.setTitle(model.getName());
            String timings = "Timings :- \n";
            if(!model.getDay1().isEmpty()){
                timings += "Day 1 (24 March) : " + model.getTime(model.getDay1());
            }
            if(!model.getDay2().equals("")){
                timings += "\nDay 2 (25 March) : " + model.getTime(model.getDay2());
            }
            if(!model.getDay3().equals("")){
                timings += "\nDay 3 (26 March) : " + model.getTime(model.getDay3());
            }

            eventDate.setText(timings);
            if(model.getDescription() !=null){
                String description = Html.fromHtml(model.getDescription()).toString();
                eventDescription.setText(description);
            }
            eventLocation.setText(model.getVenue());
            if(!model.getContact_phone().equals("")){
                eventContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] imageMode = {model.getContact_person()+" : " + model.getContact_phone(),
                                model.getContact_person2()+ " : " + model.getContact_phone2(),
                                "E-mail : " + model.getContact_email()};
                        AlertDialog.Builder builder = new AlertDialog.Builder(EventDescriptionActivity.this);
                        builder.setTitle("Contact Details")
                                .setItems(imageMode, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which==0){
                                            Intent intent = new Intent(Intent.ACTION_DIAL);
                                            intent.setData(Uri.parse("tel:"+model.getContact_phone()));
                                            startActivity(intent);                                                     }
                                        else if(which == 1) {
                                            Intent intent = new Intent(Intent.ACTION_DIAL);
                                            intent.setData(Uri.parse("tel:"+model.getContact_phone2()));
                                            startActivity(intent);
                                        }else {
                                            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                            intent.setType("text/html");
                                            intent.putExtra(Intent.EXTRA_SUBJECT,  "Query about " + model.getName());
                                            intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { model.getContact_email() });
                                            Toast.makeText(EventDescriptionActivity.this, model.getContact_email(), Toast.LENGTH_SHORT).show();
                                            startActivity(Intent.createChooser(intent, "Send Email"));
                                        }
                                    }
                                });
                        builder.show();
                    }
                });
            }
            dialog.dismiss();
        }
    }
}
