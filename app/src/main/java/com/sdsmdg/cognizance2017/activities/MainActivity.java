package com.sdsmdg.cognizance2017.activities;

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
import com.sdsmdg.cognizance2017.models.Event;
import com.sdsmdg.cognizance2017.models.EventList;
import com.sdsmdg.cognizance2017.models.EventModel;
import com.sdsmdg.cognizance2017.models.Type;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Fragment fragment;
    private Realm realm;
    private RealmResults<Event> results;
    private TabLayout tabLayout;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private int actionBarSize;
    public static int curDay = 24;
    public static final String BASE_URL = "https://cognizance.org.in/";
    private ArrayList<EventModel> eventList;
    private DataInterface api;
    private ArrayList<EventModel> eventModels;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //session class instance
        session = new SessionManager(getApplicationContext());
        /**
         * call this function when you want to check if the user is logged in or not
         * this will check if the user is logged in or not and then direct it to login activity
         */
        session.checkLogIn();


        tabLayout = (TabLayout) findViewById(R.id.vpager_tabs);

        appBar = (AppBarLayout) findViewById(R.id.appbar);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        if (realm.isEmpty())
            loadDatabase();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        results = realm.where(Event.class).equalTo("fav", true).findAll();
        Toast.makeText(this, "" + results.size(), Toast.LENGTH_SHORT).show();

        //    TextView fav = (TextView) findViewById(R.id.no_favorite_selected_text);
        //    fav.setVisibility(View.VISIBLE);
        fragment = getSupportFragmentManager().findFragmentByTag("all_events");
        if (fragment == null) {
            fragment = AllEventsFragment.newInstance(0);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.events_container, fragment, "all_events");
            fragmentTransaction.commit();
        }
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
        navigationView.getMenu().getItem(1).setChecked(true);


        // accessing data from cognizance
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();

        api = adapter.create(DataInterface.class);

        api.getAllEvents(new Callback<ArrayList<EventModel>>() {
            @Override
            public void success(ArrayList<EventModel> eventList, Response response) {
                MainActivity.this.eventList = eventList;
                Toast.makeText(MainActivity.this, eventList.get(0).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        for(int i=6;i<=176;i++){
            RealmObject result = realm.where(EventModel.class).equalTo("id",i).findFirst();
            if(result ==null) {
                api.getEventById(i, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject json, Response response) {
                        final JsonObject jsonObject = json;
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.beginTransaction();
                                realm.createObjectFromJson(EventModel.class, jsonObject.toString());
                                realm.commitTransaction();
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("Cogni data", "error");
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        showTabs("All Events");
    }

    private void loadDatabase() {
        realm.beginTransaction();
        EventModel model = realm.createObject(EventModel.class);
        model.setId(0);
        model.setName("Name");
        Type type = new Type();
        type.setName("t_name");
        Type managedType = realm.copyToRealm(type);
        type.setCategory("t_category");
        model.setType(managedType);
        realm.commitTransaction();
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
        if (id == R.id.fav) {
            fragment = getSupportFragmentManager().findFragmentByTag("fav");
            if (fragment == null) {
                fragment = AllEventsFragment.newInstance(50);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.events_container, fragment, "fav");
                fragmentTransaction.commit();
            }
        } else {
            if (id == R.id.all_events) {
                fragment = getSupportFragmentManager().findFragmentByTag("all_events");
                if (fragment == null) {
                    fragment = AllEventsFragment.newInstance(0);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.events_container, fragment, "all_events");
                    fragmentTransaction.commit();
                }
            } else if (id == R.id.theme_events) {
                fragment = getSupportFragmentManager().findFragmentByTag("theme_events");
                if (fragment == null) {
                    fragment = AllEventsFragment.newInstance(1);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.events_container, fragment, "theme_events");
                    fragmentTransaction.commit();
                }
            } else if (id == R.id.robotics) {
                fragment = getSupportFragmentManager().findFragmentByTag("robotics");
                if (fragment == null) {
                    fragment = AllEventsFragment.newInstance(2);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.events_container, fragment, "robotics");
                    fragmentTransaction.commit();
                }
            } else if (id == R.id.literario) {
                fragment = getSupportFragmentManager().findFragmentByTag("literario");
                if (fragment == null) {
                    fragment = AllEventsFragment.newInstance(3);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.events_container, fragment, "literario");
                    fragmentTransaction.commit();
                }
            } else if (id == R.id.competitions) {
                fragment = getSupportFragmentManager().findFragmentByTag("competitions");
                if (fragment == null) {
                    fragment = AllEventsFragment.newInstance(4);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.events_container, fragment, "competitions");
                    fragmentTransaction.commit();
                }
            } else if (id == R.id.online) {
                fragment = getSupportFragmentManager().findFragmentByTag("online");
                if (fragment == null) {
                    fragment = AllEventsFragment.newInstance(5);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.events_container, fragment, "online");
                    fragmentTransaction.commit();
                }
            } else if (id == R.id.barcode) {
                Intent i = new Intent(MainActivity.this, BarCodeActivity.class);
                startActivity(i);
            }
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
        tabLayout.setVisibility(View.VISIBLE);
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
}
