package com.sdsmdg.cognizance2017.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.models.MarkerData;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isMapReady;
    private FloatingActionButton resetBtn;
    private ArrayList<MarkerData> markersData;
    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        isMapReady = false;
        resetBtn = (FloatingActionButton) findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMapReady) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29.865866, 77.896316), 18));
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        loadJSONFromAsset();
    }

    public void loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("campus_locations.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            markersData = new ArrayList<>();
            JSONArray mArray = new JSONArray(json);
            for (int i = 0; i < mArray.length(); i++) {
                MarkerData markerData = new MarkerData();
                markerData.setLatitude(mArray.getJSONObject(i).getDouble("Latitude"));
                markerData.setLongitude(mArray.getJSONObject(i).getDouble("Longitude"));
                markerData.setTitle(mArray.getJSONObject(i).getString("Location"));
                markersData.add(markerData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapReady = true;
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29.865866, 77.896316), 18));
        setUpClusterManager();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            12);
                } else {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    private void addMarkers() {
        IconGenerator icon = new IconGenerator(this);
        icon.setStyle(IconGenerator.STYLE_RED);
        for (int i = 0; i < markersData.size(); i++) {
            mClusterManager.addItem(new MyItem(markersData.get(i), i));
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 12: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request

        }
    }

    private void setUpClusterManager() {
        mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        addMarkers();
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem myItem) {
                String location = "google.navigation:q=" + myItem.getPosition().latitude + "," + myItem.getPosition().longitude;
                Uri gmmIntentUri = Uri.parse(location);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                return true;
            }
        });
        mClusterManager.setRenderer(new MyClusterRenderer(this, mMap,
                mClusterManager));
    }


    public class MyClusterRenderer extends DefaultClusterRenderer<MyItem> {

        public MyClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
            IconGenerator iconGenerator = new IconGenerator(getApplicationContext());
            iconGenerator.setStyle(IconGenerator.STYLE_RED);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(markersData.get(item.getId()).getTitle())
            ));
        }

        @Override
        protected void onClusterItemRendered(MyItem clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
        }
    }

    public class MyItem implements ClusterItem {
        private LatLng mPosition;
        private String mTitle, mSnippet;
        private int mId;

        public MyItem(double lat, double lng) {
            mPosition = new LatLng(lat, lng);

        }

        public MyItem(MarkerData markerData, int id) {
            mPosition = new LatLng(markerData.getLatitude(), markerData.getLongitude());
            mId = id;
        }

        public MyItem(double lat, double lng, String title, String snippet) {
            mPosition = new LatLng(lat, lng);
            mTitle = title;
            mSnippet = snippet;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public String getTitle() {
            return mTitle;
        }

        @Override
        public String getSnippet() {
            return mSnippet;
        }

        public int getId() {
            return mId;
        }
    }
}
