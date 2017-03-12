package com.sdsmdg.cognizance2017.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import com.sdsmdg.cognizance2017.fragments.ExpandedListFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isOnFavSelectionFragment = false;
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        if (realm.isEmpty())
            loadDatabase();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        results = realm.where(Event.class).equalTo("fav", true).findAll();
        Toast.makeText(this, "" + results.size(), Toast.LENGTH_SHORT).show();
        if (results.size() == 0) {
            TextView fav = (TextView) findViewById(R.id.no_favorite_selected_text);
            fav.setVisibility(View.VISIBLE);
        } else {
            /*ExpandedListFragment expandedListFragment = new ExpandedListFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.events_container, expandedListFragment, "expandable").commit();*/
            fragment = getSupportFragmentManager().findFragmentByTag("favList");
            if (fragment == null) {
                fragment = AllEventsRecyclerFragment.newInstance(6);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.events_container, fragment, "favList");
                fragmentTransaction.commit();
            }
        }


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnFavSelectionFragment) {
                    fragment = getSupportFragmentManager().findFragmentByTag("fav");
                    if (fragment == null) {
                        fragment = AllEventsRecyclerFragment.newInstance(5);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.events_container, fragment, "fav");
                        fragmentTransaction.commit();
                        fab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_send));
                        isOnFavSelectionFragment = true;
                    }
                } else {
                    //List<Event> events = ((AllEventsRecyclerFragment) fragment).getAdapter().getFavEvents();
                    fragment = getSupportFragmentManager().findFragmentByTag("favRec");
                    if (fragment == null) {
                        fragment = AllEventsRecyclerFragment.newInstance(6);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.events_container, fragment, "favRec");
                        fragmentTransaction.commit();
                    }
                    fab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_menu_gallery));
                    isOnFavSelectionFragment = false;
                    /*realm.beginTransaction();
                    List<Event> el = realm.where(Event.class).findAll();
                    for (int i = 0; i < el.size(); i++)
                        el.get(i).setFav(false);
                    for (int i = 0; i < events.size(); i++) {
                        for (int j = 0; j < el.size(); j++) {
                            if ((events.get(i).getTitle()).equals(el.get(j).getTitle())) {
                                el.get(j).setFav(true);
                            }
                        }
                    }
                    realm.commitTransaction();*/
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
        // AllEventsRecyclerFragment fragment = null;
        if (id == R.id.all_events) {
            fragment = getSupportFragmentManager().findFragmentByTag("pager");
            if (fragment == null) {
                fragment = new AllEventsFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.events_container, fragment, "pager");
                fragmentTransaction.commit();
            }
        } else {
            if (id == R.id.home) {

            }
            fragment = new ExpandedListFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.events_container, fragment, "expandable");
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}