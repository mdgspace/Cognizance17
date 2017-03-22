package com.sdsmdg.cognizance2017.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sdsmdg.cognizance2017.FavReceiver;
import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.fragments.AllEventsFragment;
import com.sdsmdg.cognizance2017.fragments.AllEventsRecyclerFragment;
import com.sdsmdg.cognizance2017.models.EventModel;

import java.util.ArrayList;
import java.util.Calendar;

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
    private TabLayout tabLayout;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private int actionBarSize;
    public static int curDay = 24;
    public static final String BASE_URL = "https://cognizance.org.in/";
    private ArrayList<EventModel> eventList;
    private DataInterface api;
    private boolean shouldLoadEvents;
    public static Activity mainAct;
    private ProgressDialog dialog;
    private boolean isOnHome, isReady;
    private int currentSelectedFragmentId;
    public boolean isOnDeptViewPagerFragment;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainAct = this;
        isOnDeptViewPagerFragment = false;


        tabLayout = (TabLayout) findViewById(R.id.vpager_tabs);

        appBar = (AppBarLayout) findViewById(R.id.appbar);

        //Is related to transparent toolbar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        // accessing data from cognizance website
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .build();
        api = adapter.create(DataInterface.class);
        loadDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*showTabs("Home");
        if (!isOnHome) {
            showEvents("all_events", "Home");
            navigationView.getMenu().getItem(0).setChecked(true);
            isOnHome = true;
        }*/
    }

    private void loadDatabase() {
        if(isNetworkAvailable()){
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("please wait");
            dialog.show();
            api.getAllEvents(new Callback<JsonArray>() {
                @Override
                public void success(final JsonArray json, Response response) {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.createOrUpdateAllFromJson(EventModel.class, json.toString());
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            dialog.dismiss();
                            showEvents("all_events", "Home");
                            RealmResults<EventModel> eventModels = realm.where(EventModel.class).equalTo("isFav",true).findAll();
                           /* for(EventModel model:eventModels){
                                if(!(model.getTime().equals("") || model.getDate().equals(""))){
                                    int hr = Integer.parseInt(model.getTime().substring(0,2));
                                    int min = Integer.parseInt(model.getTime().substring(2,4));
                                    int day = Integer.parseInt(model.getDate().substring(0,2));
                                    // Toast.makeText(ctx, ""+hr+" " + min+" "+ day, Toast.LENGTH_SHORT).show();
                                    Calendar calender = Calendar.getInstance();
                                    calender.set(Calendar.MONTH,Calendar.MARCH);
                                    calender.set(Calendar.YEAR,2017);
                                    calender.set(Calendar.DAY_OF_MONTH,day);
                                    calender.set(Calendar.HOUR_OF_DAY,hr);
                                    calender.set(Calendar.MINUTE,min);
                                    if(System.currentTimeMillis()<calender.getTimeInMillis())
                                    createNotification(calender,model);
                                    else {
                                        Toast.makeText(mainAct, "This event has already started", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(mainAct, "can't set alarm date is null", Toast.LENGTH_SHORT).show();
                                }
                            }*/
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    Snackbar.make(getWindow().getDecorView().getRootView(),"make sure that you have an active internet connection to get latest updates",Snackbar.LENGTH_INDEFINITE).show();
                    dialog.dismiss();
                }
            });
        } else {
            if (!realm.isEmpty()) {
                Snackbar.make(getWindow().getDecorView().getRootView(),"make sure that you have an active internet connection to get latest updates",Snackbar.LENGTH_SHORT).show();
                showEvents("all_events", "Home");
            } else {
                Snackbar.make(getWindow().getDecorView().getRootView(),"make sure that you have an active internet connection to get latest updates",Snackbar.LENGTH_INDEFINITE).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (isOnDeptViewPagerFragment) {
            getSupportFragmentManager().popBackStack();
            isOnDeptViewPagerFragment = false;
            hideTabs("Departmentals");
            navigationView.setCheckedItem(R.id.dept);
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
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
        if (id != R.id.barcode) currentSelectedFragmentId = id;
        isOnHome = false;
        if (id == R.id.all_events) {
            showEvents("all_events", "Home");
            isOnHome = true;
        } else if (id == R.id.theme_events) {
            showEvents("theme_events", "Theme Events");
        } else if (id == R.id.robotics) {
            showEvents("robotics", "Robotics");
        } else if (id == R.id.literario) {
            showEvents("literario", "Literario");
        } else if (id == R.id.competitions) {
            showEvents("competitions", "Competitions");
        } else if (id == R.id.online) {
            showEvents("online", "Online Events");
        } else if (id == R.id.fun_events) {
            showEvents("fun", "FUN EVENTS");
        } else if (id == R.id.workshop) {
            showEvents("workshop", "Workshop");
        } else if (id == R.id.dept) {
            fragment = getSupportFragmentManager().findFragmentByTag("dept");
            if (fragment == null) {
                fragment = AllEventsRecyclerFragment.newInstance(4, "DepartmentList");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.events_container, fragment, "dept");
                fragmentTransaction.commit();
            }
            hideTabs("Departmentals");
        } else if (id == R.id.summit) {
            showEvents("summit", "E-Summit");
        } else if (id == R.id.mainstay) {
            showEvents("mainstay", "Mainstay");
        } else if (id == R.id.mars) {
            showEvents("mars", "Project M.A.R.S");
        } else if (id == R.id.fav) {
            showEvents("fav", "Favorites");
        } else if (id == R.id.barcode) {
            Intent i = new Intent(MainActivity.this, BarCodeActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showEvent(int id) {
        Intent eventIntent = new Intent(MainActivity.this, EventDescriptionActivity.class);
        eventIntent.putExtra("id", id);
        startActivity(eventIntent);
        if (isNetworkAvailable()) {
            Toast.makeText(mainAct, "connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mainAct, "not connected", Toast.LENGTH_SHORT).show();
        }
    }

    public void showTabs(String title) {
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.height = 2 * actionBarSize;
        tabLayout.setVisibility(View.VISIBLE);
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
/*
    private void loadEvents(final int id) {
        if (id < ids.size() && shouldLoadEvents) {
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
                                loadEvents(id + 1);
                                dialog.setProgress(ids.get(id) * 100 / ids.size());
                                Log.d("Cogni data", "" + ids.get(id));
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
            } else {
                loadEvents(id + 1);
                dialog.setProgress(ids.get(id) * 100 / ids.size());
            }
        } else {
            dialog.dismiss();
            Toast.makeText(mainAct, "Download complete" + ids.size(), Toast.LENGTH_SHORT).show();
            showEvents("all_events", "Home");
        }
    }
*/
    public void showEvents(String tag, String title) {
        fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = AllEventsFragment.newInstance(title);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.events_container, fragment, tag);
            if (isOnDeptViewPagerFragment)
                fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public int getCurrentSelectedFragmentId() {
        return currentSelectedFragmentId;
    }

    public void hideTabs(String title) {
        CollapsingToolbarLayout.LayoutParams
                layoutParams = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        tabLayout.setVisibility(View.GONE);
        layoutParams.height = actionBarSize;
        toolbar.setLayoutParams(layoutParams);
        appBar.setExpanded(true);
        toolbar.setTitle(title);
    }

    public void createNotification(Calendar cal,EventModel model) {
        Intent intent = new Intent(mainAct, FavReceiver.class);
        int idString = Integer.parseInt(cal.get(Calendar.DAY_OF_MONTH)+""+model.getId());
        intent.putExtra("id",idString);
        intent.putExtra("realId",model.getId());
        intent.putExtra("title",model.getName());
        Log.d("Alarm:","id:" + idString+" Cal: "+cal.getTime());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mainAct, idString, intent, 0);
        AlarmManager alarmManager = (AlarmManager) mainAct.getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    public void cancelNotification(int id) {
        Intent intent = new Intent(mainAct, FavReceiver.class);
        intent.putExtra("id",id);
        Log.d("alarm cancel",""+id);
        intent.putExtra("cancel",true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mainAct, id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) mainAct.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        ((NotificationManager)mainAct.getSystemService(mainAct.NOTIFICATION_SERVICE)).cancel(id);
        Toast.makeText(mainAct, "Alarm has been cancelled", Toast.LENGTH_LONG).show();
    }
}
