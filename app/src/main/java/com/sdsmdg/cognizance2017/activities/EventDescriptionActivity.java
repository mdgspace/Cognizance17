package com.sdsmdg.cognizance2017.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.models.Event;
import com.sdsmdg.cognizance2017.models.EventModel;

import io.realm.Realm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.sdsmdg.cognizance2017.activities.MainActivity.BASE_URL;


public class EventDescriptionActivity extends AppCompatActivity {
    TextView eventName, eventDate, eventDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        Toast.makeText(this, realm.where(EventModel.class).equalTo("id",35).findFirst().getType().getCategory(), Toast.LENGTH_SHORT).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.event_toolbar);
        setSupportActionBar(toolbar);
        eventName = (TextView) findViewById(R.id.event_name);
        eventDate = (TextView) findViewById(R.id.event_time);
        eventDescription = (TextView) findViewById(R.id.event_description);
       // getEventDetails(getIntent().getIntExtra("id",0));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
    public Event getEventDetails(int id){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("loading data");
        dialog.show();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();

        DataInterface api = adapter.create(DataInterface.class);
        /*api.getEventById(id, new Callback<EventModel>() {
            @Override
            public void success(EventModel event, Response response) {
                dialog.dismiss();
                Toast.makeText(EventDescriptionActivity.this, event.getType().getName(), Toast.LENGTH_SHORT).show();
                eventName.setText(event.getName());
                CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
                collapsingToolbarLayout.setTitle("Event Category");
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
                Toast.makeText(EventDescriptionActivity.this, "Please check your Internet connectivity", Toast.LENGTH_SHORT).show();
                finish();
            }
        });*/
        return null;
    }
    private void addEvent(){

    }

}
