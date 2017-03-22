package com.sdsmdg.cognizance2017.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.adapters.RecyclerAdapter;
import com.sdsmdg.cognizance2017.models.EventModel;
import com.yalantis.phoenix.PullToRefreshView;

import io.realm.Realm;
import io.realm.RealmResults;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

public class TestAct extends AppCompatActivity {
    private PullToRefreshView mPullToRefreshView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 5000);
            }
        });

        Realm realm = Realm.getDefaultInstance();
        RealmResults<EventModel> eventModels = realm.where(EventModel.class).findAll();
        RecyclerView eventsRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        RecyclerAdapter adapter = new RecyclerAdapter(TestAct.this, eventModels, false);
        eventsRecyclerView.setAdapter(adapter);
        eventsRecyclerView.setItemAnimator(new FadeInUpAnimator());
        eventsRecyclerView.getItemAnimator().setRemoveDuration(500);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(TestAct.this));
        eventsRecyclerView.setNestedScrollingEnabled(true);
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        Toast.makeText(TestAct.this, "heeleere", Toast.LENGTH_SHORT).show();
                    }
                }, 5000);
            }
        });
    }
}
