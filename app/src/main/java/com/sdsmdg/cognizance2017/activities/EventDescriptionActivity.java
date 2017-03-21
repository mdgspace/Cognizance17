package com.sdsmdg.cognizance2017.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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


public class EventDescriptionActivity extends AppCompatActivity {
    private TextView eventDate, eventDescription, eventLocation;;
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
        dialog = new ProgressDialog(this);
        dialog.setMessage("fetching data");
        eventDate = (TextView)findViewById(R.id.event_time);
        eventDescription = (TextView)findViewById(R.id.event_description);
        eventLocation = (TextView)findViewById(R.id.event_location);

        // accessing data from cognizance website
        dialog.setCancelable(false);
        dialog.show();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();
        final DataInterface api = adapter.create(DataInterface.class);
        api.getEventById(getIntent().getIntExtra("id",6), new Callback<JsonObject>() {
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
                        EventModel model = realm.where(EventModel.class).equalTo("id",getIntent().getIntExtra("id",6)).findFirst();
                        CollapsingToolbarLayout appBar = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
                        appBar.setTitle(model.getName());
                        eventDate.setText(model.getDate());
                        String ss = model.getDescription();
                        int endIndex = ss.length()-15;
                        eventDescription.setText(ss.substring(11,endIndex));
                        eventLocation.setText(model.getVenue());
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
    }
}
