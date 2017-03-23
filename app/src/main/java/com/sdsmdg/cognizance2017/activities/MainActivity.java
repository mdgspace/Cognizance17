package com.sdsmdg.cognizance2017.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;

import com.google.gson.JsonArray;
import com.sdsmdg.cognizance2017.FavReceiver;
import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.fragments.AllEventsFragment;
import com.sdsmdg.cognizance2017.fragments.AllEventsRecyclerFragment;
import com.sdsmdg.cognizance2017.models.EventModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
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
    private ImageView refreshImg;

    public boolean isOnDeptViewPagerFragment;
    public NavigationView navigationView;
    public ImageView toolbarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainAct = this;
        isOnDeptViewPagerFragment = false;
        toolbarImageView =(ImageView) findViewById(R.id.imageview_toolbar);
        currentSelectedFragmentId = R.id.all_events;
        tabLayout = (TabLayout) findViewById(R.id.vpager_tabs);
        refreshImg  = (ImageView) findViewById(R.id.refresh_btn);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        String url ="https://scontent.fdel1-1.fna.fbcdn.net/v/t1.0-9/17265243_1432659656807162_6388188857608893324_n.png?oh=d6268dc86d261acadaeef43f9191f5c2&oe=596E349F";

        setImageBackground(url);
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
        refreshImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDatabase();
            }
        });
        loadDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadDatabase() {
        if (isNetworkAvailable()) {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Fetching data please wait....");
            dialog.setCancelable(false);
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
                            RealmResults<EventModel> eventModels = realm.where(EventModel.class).equalTo("isFav1",true).findAll();
                            for(EventModel model:eventModels) {
                                if (model.isFav1())
                                    createNotification(model, 24);
                                else if (model.isFav2()) {
                                    createNotification(model, 25);
                                } else if (model.isFav3()) {
                                    createNotification(model, 26);
                                }
                            }
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    Snackbar.make(refreshImg,"Network Error",Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
        } else {
            if (!realm.isEmpty()) {
                Snackbar.make(refreshImg, "Connect to internet to get latest updates", Snackbar.LENGTH_LONG).show();
                showEvents("all_events", "Home");
            } else {
                Snackbar.make(refreshImg, "Connect to internet", Snackbar.LENGTH_LONG).show();
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
            Intent mapIntent = new Intent(MainActivity.this,MapsActivity.class);
                mapIntent.putExtra("location","Main Building Lawns");
            startActivity(mapIntent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id != R.id.barcode && id != R.id.about_us && id != R.id.sponsors && id != R.id.techtainment)
            currentSelectedFragmentId = id;
        isOnHome = false;
        if (id == R.id.all_events) {
            toolbarImageView.setImageResource(R.drawable.main_placeholder);
            showEvents("all_events", "Home");
            isOnHome = true;
        } else if (id == R.id.theme_events) {
            setImageBackground("https://scontent.fdel1-1.fna.fbcdn.net/v/t1.0-9/16939604_1409408232465638_2732071995952707105_n.png?oh=e5801aba48c41f8dd153496e8db82fd6&oe=5965A858");
            showEvents("theme_events", "Theme Events");
        } else if (id == R.id.robotics) {
            setImageBackground("https://scontent.fdel1-1.fna.fbcdn.net/v/t31.0-8/q82/s960x960/16463152_1374411109298684_5412964868764400500_o.jpg?oh=139bc5cf09880a00598e27ffb484077d&oe=595DC83D");
            showEvents("robotics", "Robotics");
        } else if (id == R.id.literario) {
            setImageBackground("https://scontent.fdel1-1.fna.fbcdn.net/v/t31.0-8/s960x960/16587302_1375407662532362_6304259759811779212_o.png?oh=2dd711f479183e62250bb62ffaf6cb22&oe=5957B88B");
            showEvents("literario", "Literario");
        } else if (id == R.id.competitions) {
            setImageBackground("https://scontent.fdel1-1.fna.fbcdn.net/v/t1.0-9/17190808_1425555330850928_1236046315941752612_n.png?oh=3576e90bb5ff1b9221fde3b9d74be3b0&oe=5973DB93");
            showEvents("competitions", "Competitions");
        } else if (id == R.id.online) {
            setImageBackground("https://scontent.fdel1-1.fna.fbcdn.net/v/t31.0-8/s960x960/17358806_1428987000507761_8539195445186412766_o.png?oh=362e8f8d95f92da987b94e204c19351a&oe=595A62F6");
            showEvents("online", "Online Events");
        } else if (id == R.id.fun_events) {
            setImageBackground("https://scontent.fdel1-1.fna.fbcdn.net/v/t1.0-9/17353358_1432963893443405_8012701290388919154_n.jpg?oh=89f343c9431496f51cc86b6dd91ac7bb&oe=595DC8F6");
            showEvents("fun", "FUN EVENTS");
        } else if (id == R.id.workshop) {
            setImageBackground("https://scontent.fdel1-1.fna.fbcdn.net/v/t1.0-9/16684338_1380874858652309_7071854506961857899_n.png?oh=51ea2c93dab3767eb6ae09f9c3ae4335&oe=595A27F6");
            showEvents("workshop", "Workshop");
        } else if (id == R.id.dept) {
            setImageBackground("https://scontent.fdel1-1.fna.fbcdn.net/v/t31.0-8/s960x960/16665063_1382042308535564_5884962650034691312_o.png?oh=68f0223c9f1a0ad4c7f6543a08244a59&oe=5960BE2E");
            fragment = getSupportFragmentManager().findFragmentByTag("dept");
            if (fragment == null) {
                fragment = AllEventsRecyclerFragment.newInstance(4, "DepartmentList");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.events_container, fragment, "dept");
                fragmentTransaction.commit();
            }
            hideTabs("Departmentals");
        } else if (id == R.id.summit) {
            toolbarImageView.setImageResource(R.drawable.events);
            showEvents("summit", "E-Summit");
        } else if (id == R.id.mainstay) {
            setImageBackground("https://scontent.fdel1-1.fna.fbcdn.net/v/t1.0-9/17342703_1427536257319502_4398272506967097961_n.png?oh=24bea22918782b3f135a8f86f70ea26f&oe=5924D29D");
            showEvents("mainstay", "Mainstay");
        } else if (id == R.id.mars) {
            setImageBackground("https://scontent.fdel1-1.fna.fbcdn.net/v/t31.0-8/s960x960/17038491_1406266746113120_6845539489281441160_o.png?oh=409d1d2ed5d87b4c830084d23edb2e1f&oe=5959735D");
            showEvents("mars", "Project M.A.R.S");
        } else if (id == R.id.fav) {
            setImageBackground("https://scontent.fdel1-1.fna.fbcdn.net/v/t1.0-9/17361505_1431776796895448_1464994987889437576_n.png?oh=704b763b2588f11d98355698496346d5&oe=595DE449");
            showEvents("fav", "Favorites");
        } else if (id == R.id.barcode) {
            Intent i = new Intent(MainActivity.this, BarCodeActivity.class);
            startActivity(i);
        } else if (id == R.id.sponsors) {
            Intent i = new Intent(MainActivity.this, SponsorsActivity.class);
            i.putExtra("isOnSponser", true);
            startActivity(i);
        } else if (id == R.id.about_us) {
            Intent i = new Intent(MainActivity.this, AboutUs.class);
            startActivity(i);
        } else if (id == R.id.techtainment) {
            Intent i = new Intent(MainActivity.this, SponsorsActivity.class);
            i.putExtra("isOnSponser", false);
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
    }

    public void showTabs(String title) {
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.height = 2 * actionBarSize;
        tabLayout.setVisibility(View.VISIBLE);
        //Sets indicator color  of tabs to colorPrimary.
        //Using resource file was giving error so used the hex code as it is.
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#A6A14A"));
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

    public void createNotification(EventModel model,int day) {
        if(day == 24 && !(model.getDay1().equals(""))) {
            if (!(model.getDay1().equals(""))) {
                int hr = Integer.parseInt(model.getDay1().substring(0, 2));
                int min = Integer.parseInt(model.getDay1().substring(2, 4));
                Calendar calender = Calendar.getInstance();
                calender.set(Calendar.MONTH, Calendar.MARCH);
                calender.set(Calendar.YEAR, 2017);
                calender.set(Calendar.DAY_OF_MONTH, day);
                calender.set(Calendar.HOUR_OF_DAY, hr);
                calender.set(Calendar.MINUTE, min);
                if (System.currentTimeMillis() < calender.getTimeInMillis()) {
                    Intent intent = new Intent(mainAct, FavReceiver.class);
                    int idString = Integer.parseInt(calender.get(Calendar.DAY_OF_MONTH) + "" + model.getId());
                    intent.putExtra("id", idString);
                    intent.putExtra("realId", model.getId());
                    intent.putExtra("title", model.getName());
                    Log.d("Alarm:", "id:" + idString + " Cal: " + calender.getTime());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mainAct, idString, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) mainAct.getSystemService(ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                }
            }
        }
        else if(day == 25 && !(model.getDay2().equals(""))) {
            if (!(model.getDay2().equals(""))) {
                int hr = Integer.parseInt(model.getDay2().substring(0, 2));
                int min = Integer.parseInt(model.getDay2().substring(2, 4));
                Calendar calender = Calendar.getInstance();
                calender.set(Calendar.MONTH, Calendar.MARCH);
                calender.set(Calendar.YEAR, 2017);
                calender.set(Calendar.DAY_OF_MONTH, day);
                calender.set(Calendar.HOUR_OF_DAY, hr);
                calender.set(Calendar.MINUTE, min);
                if (System.currentTimeMillis() < calender.getTimeInMillis()) {
                    Intent intent = new Intent(mainAct, FavReceiver.class);
                    int idString = Integer.parseInt(calender.get(Calendar.DAY_OF_MONTH) + "" + model.getId());
                    intent.putExtra("id", idString);
                    intent.putExtra("realId", model.getId());
                    intent.putExtra("title", model.getName());
                    Log.d("Alarm:", "id:" + idString + " Cal: " + calender.getTime());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mainAct, idString, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) mainAct.getSystemService(ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                }
            }
        }
        if(day == 26 && !(model.getDay3().equals(""))) {
            if (!(model.getDay3().equals(""))) {
                int hr = Integer.parseInt(model.getDay3().substring(0, 2));
                int min = Integer.parseInt(model.getDay3().substring(2, 4));
                Calendar calender = Calendar.getInstance();
                calender.set(Calendar.MONTH, Calendar.MARCH);
                calender.set(Calendar.YEAR, 2017);
                calender.set(Calendar.DAY_OF_MONTH, day);
                calender.set(Calendar.HOUR_OF_DAY, hr);
                calender.set(Calendar.MINUTE, min);

                if (System.currentTimeMillis() < calender.getTimeInMillis()) {
                    Intent intent = new Intent(mainAct, FavReceiver.class);
                    int idString = Integer.parseInt(calender.get(Calendar.DAY_OF_MONTH) + "" + model.getId());
                    intent.putExtra("id", idString);
                    intent.putExtra("realId", model.getId());
                    intent.putExtra("title", model.getName());
                    Log.d("Alarm:", "id:" + idString + " Cal: " + calender.getTime());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mainAct, idString, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) mainAct.getSystemService(ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                }
            }
        }
    }

    public void cancelNotification(int id,int day) {
        AlarmManager alarmManager = (AlarmManager) mainAct.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(mainAct, FavReceiver.class);
        int idString = Integer.parseInt(day + "" + id);
        intent.putExtra("idString",idString);
        intent.putExtra("cancel",true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mainAct, idString, intent, 0);
        alarmManager.cancel(pendingIntent);
        ((NotificationManager)mainAct.getSystemService(mainAct.NOTIFICATION_SERVICE)).cancel(idString);
        Log.d("Alarm ID",""+idString);
        Snackbar.make(refreshImg,"Notification has been disabled",Snackbar.LENGTH_LONG).show();
    }
    public void setImageBackground(String url) {
        Picasso.with(getApplicationContext())
                .load(url).placeholder(R.drawable.main_placeholder)
                .into(toolbarImageView);
    }
}
