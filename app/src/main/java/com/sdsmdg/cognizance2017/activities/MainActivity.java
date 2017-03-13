package com.sdsmdg.cognizance2017.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.fragments.AllEventsFragment;
import com.sdsmdg.cognizance2017.fragments.AllEventsRecyclerFragment;
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
    private boolean isOnFavSelectionFragment;
    private TabLayout tabLayout;
    private AppBarLayout appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.vpager_tabs);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        isOnFavSelectionFragment = false;
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        if (realm.isEmpty())
            loadDatabase();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        results = realm.where(Event.class).equalTo("fav", true).findAll();
        Toast.makeText(this, "" + results.size(), Toast.LENGTH_SHORT).show();
        if (results.size() == 0) {
            TextView fav = (TextView) findViewById(R.id.no_favorite_selected_text);
            fav.setVisibility(View.VISIBLE);
        } else {
            fragment = getSupportFragmentManager().findFragmentByTag("home");
            if (fragment == null) {
                fragment = AllEventsFragment.newInstance(50);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.events_container, fragment, "home");
                fragmentTransaction.commit();
            }
        }


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnFavSelectionFragment) {
                    //Make tabs disappear
                    tabLayout.setVisibility(View.GONE);
                    //Go to favorite event selection page
                    fragment = getSupportFragmentManager().findFragmentByTag("favSelection");
                    if (fragment == null) {
                        fragment = AllEventsRecyclerFragment.newInstance(5, -1);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.events_container, fragment, "favSelection");
                        fragmentTransaction.commit();
                        fab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_send));
                        isOnFavSelectionFragment = true;
                    }
                } else {
                    //Go to home page showing all fav events
                    tabLayout.setVisibility(View.VISIBLE);
                    fragment = getSupportFragmentManager().findFragmentByTag("home");
                    if (fragment == null) {
                        fragment = AllEventsFragment.newInstance(50);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.events_container, fragment, "home");
                        fragmentTransaction.commit();
                    }
                    fab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_gallery));
                    isOnFavSelectionFragment = false;
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
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
        tabLayout.setVisibility(View.VISIBLE);

        if (id == R.id.home) {
            fragment = getSupportFragmentManager().findFragmentByTag("home");
            if (fragment == null) {
                fragment = AllEventsFragment.newInstance(50);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.events_container, fragment, "home");
                fragmentTransaction.commit();
            }
        } else if (id == R.id.all_events) {
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
