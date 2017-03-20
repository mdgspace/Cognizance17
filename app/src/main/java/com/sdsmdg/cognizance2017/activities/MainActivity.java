package com.sdsmdg.cognizance2017.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.SessionManager;
import com.sdsmdg.cognizance2017.fragments.AllEventsFragment;
import com.sdsmdg.cognizance2017.models.EventModel;
import com.sdsmdg.cognizance2017.models.Type;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Fragment fragment;
    private Realm realm;
    private TabLayout tabLayout;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private int actionBarSize,size;
    public static int curDay = 24;
    public static final String BASE_URL = "https://cognizance.org.in/";
    private ArrayList<EventModel> eventList;
    private DataInterface api;
    private boolean shouldLoadEvents;
    public static Activity mainAct;
    private ProgressDialog dialog;
    private ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainAct = this;


        tabLayout = (TabLayout) findViewById(R.id.vpager_tabs);

        appBar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarSize = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        // accessing data from cognizance website
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();
        api = adapter.create(DataInterface.class);
        if (realm.isEmpty())
            loadDatabase();
        else {
            showEvents("all_events","All Events");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showTabs("All Events");
    }

    private void loadDatabase() {
        realm.beginTransaction();
        EventModel model = realm.createObject(EventModel.class,0);
        model.setName("Name");
        model.setTime("Time");
        model.setVenue("Venue");
        Type type = new Type();
        type.setName("t_name");
        type.setCategory("t_category");
        Type managedType = realm.copyToRealm(type);
        model.setType(managedType);
        realm.commitTransaction();

        // To get all ID's
        ids = new ArrayList<>();
        api.getAllEvents(new Callback<ArrayList<EventModel>>() {
            @Override
            public void success(ArrayList<EventModel> eventModels, Response response) {
                for(int i=0;i<eventModels.size();i++){
                    ids.add(eventModels.get(i).getId());
                }
                size = ids.size();

                shouldLoadEvents = true;

                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("please wait");
                dialog.setTitle("loading");
                dialog.setMax(100);
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.show();
                loadEvents(0);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.map) {
            startActivity(new Intent(this, MapsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.all_events) {
            showEvents("all_events","All Events");
        } else if (id == R.id.theme_events) {
            showEvents("theme_events","Theme Events");
        } else if (id == R.id.robotics) {
            showEvents("robotics","Robotics");
        } else if (id == R.id.literario) {
            showEvents("literario","Literario");
        } else if (id == R.id.competitions) {
            showEvents("competitions","Competitions");
        } else if (id == R.id.online) {
            showEvents("online","Online Events");
        }
        else if(id == R.id.fun_events){
            showEvents("fun","FUN EVENTS");
        } else if(id == R.id.workshop){
            showEvents("workshop","Workshop");
        }else if(id == R.id.dept){
            showEvents("dept","Departmental");
        }
        else if(id == R.id.summit){
            showEvents("summit","E-Summit");
        }else if(id == R.id.mainstay){
            showEvents("mainstay","Mainstay");
        }else if(id == R.id.mars){
            showEvents("mars","Project M.A.R.S");
        }else if(id == R.id.fav){
            showEvents("fav","Favorites");
        }else if (id == R.id.barcode) {
            Intent i = new Intent(MainActivity.this, BarCodeActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showEvent() {
        Intent eventIntent = new Intent(MainActivity.this, EventDescriptionActivity.class);
        eventIntent.putExtra("id", 9);
        startActivity(eventIntent);
    }

    public void showTabs(String title) {
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.height = 2 * actionBarSize;
        toolbar.setLayoutParams(layoutParams);
        appBar.setExpanded(true);
        toolbar.setTitle(title);
    }

    public ArrayList<EventModel> getEventsByType(String type) {
        ArrayList<EventModel> events = new ArrayList<>();
        for (EventModel event : eventList) {
            if (event.getType().equals(type)) {
                events.add(event);
            }
        }
        return events;
    }
    private void loadEvents(final int id){
        if(id <ids.size() && shouldLoadEvents) {
            RealmObject result = realm.where(EventModel.class).equalTo("id", ids.get(id)).findFirst();
            if (result == null) {
                api.getEventById(ids.get(id), new Callback<JsonObject>() {
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
                                loadEvents(id+1);
                                dialog.setProgress(ids.get(id)*100/ids.size());
                                Log.d("Cogni data",""+ids.get(id));
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                // Transaction failed and was automatically canceled.
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Cogni data", "error");
                        shouldLoadEvents = false;
                        Toast.makeText(MainActivity.this, "error while downloading", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }else {
                loadEvents(id+1);
                dialog.setProgress(ids.get(id)*100/ids.size());
            }
        }
        else {
            dialog.dismiss();
            Toast.makeText(mainAct, "Download complete"+ids.size(), Toast.LENGTH_SHORT).show();
            showEvents("all_events","All Events");
        }
    }
    private void showEvents(String tag,String title){
        fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = AllEventsFragment.newInstance(title);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.events_container, fragment, tag);
            fragmentTransaction.commit();
        }
    }
}
