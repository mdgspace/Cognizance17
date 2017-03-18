package com.sdsmdg.cognizance2017.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.fragments.AllEventsFragment;
import com.sdsmdg.cognizance2017.fragments.AllEventsRecyclerFragment;
import com.sdsmdg.cognizance2017.fragments.EventDescription;
import com.sdsmdg.cognizance2017.models.Event;
import com.sdsmdg.cognizance2017.models.EventList;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Fragment fragment;
    private Realm realm;
    private RealmResults<Event> results;
    private TabLayout tabLayout;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private int actionBarSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarSize = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        showTabs("All Events");
    }

    private void loadDatabase() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    InputStream is = getAssets().open("events.json");
                    realm.createAllFromJson(EventList.class, is);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
        if (id == R.id.fav) {
            fragment = getSupportFragmentManager().findFragmentByTag("fav");
            if(fragment == null) {
                fragment = AllEventsFragment.newInstance(50);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.events_container, fragment, "fav");
                fragmentTransaction.commit();
            }
            }
        else {
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
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void showSingleEventFragment(){
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        tabLayout.setVisibility(View.GONE);
        layoutParams.height = actionBarSize;
        toolbar.setLayoutParams(layoutParams);
        appBar.setExpanded(true);
        fragment = getSupportFragmentManager().findFragmentByTag("event");

            fragment = EventDescription.newInstance(1, 1);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.events_container, fragment, "event")
                    .addToBackStack("AllEventsFragment");
            fragmentTransaction.commit();
        toolbar.setTitle("Event No.");

    }
    public void showTabs(String title){
        CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        tabLayout.setVisibility(View.VISIBLE);
        layoutParams.height = 2*actionBarSize;
        toolbar.setLayoutParams(layoutParams);
        appBar.setExpanded(true);
        toolbar.setTitle(title);
    }
}
