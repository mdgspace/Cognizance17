package com.sdsmdg.cognizance2017.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.ui.IconGenerator;
import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.models.EventModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isMapReady, shouldReset;
    private Realm realm;
    private RealmResults<EventModel> results;
    private ArrayList<Marker> markers;
    private Button resetBtn;
    private CameraUpdate cu;
    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Realm.init(this);
        realm = Realm.getDefaultInstance();
        results = realm.where(EventModel.class).findAll();
        isMapReady = false;
        resetBtn = (Button) findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMapReady) {
                    mMap.animateCamera(cu);
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markers = new ArrayList<>();
    }

    public String loadJSONFromAsset() {
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
            return null;
        }
        return json;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isMapReady = true;
        mMap = googleMap;
        //addMarkers();
        setUpClusterer();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
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
        /*
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=29.865866,77.896316");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                return true;
            }
        });
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (!shouldReset) {
//                    mMap.moveCamera(cu);
                    shouldReset = true;
                }
            }
        });
        */
    }

    private void addMarkers() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        IconGenerator icon = new IconGenerator(this);
        try {
            JSONArray mArray = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < mArray.length(); i++) {
                //LatLng latLng = new LatLng(mArray.getJSONObject(i).getDouble("Latitude"),
                //      mArray.getJSONObject(i).getDouble("Longitude"));
                MyItem offsetItem = new MyItem(mArray.getJSONObject(i).getDouble("Latitude"), mArray.getJSONObject(i).getDouble("Longitude"),
                        mArray.getJSONObject(i).getString("Location"));
                mClusterManager.addItem(offsetItem);
                //Marker marker = mMap.addMarker(new MarkerOptions().position(latLng)
                //      .icon(BitmapDescriptorFactory.fromBitmap(icon.makeIcon(mArray.getJSONObject(i).getString("Location")))));
                //markers.add(marker);
                //builder.include(marker.getPosition());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //LatLngBounds bounds = builder.build();

        //mMap.setLatLngBoundsForCameraTarget(bounds);
        //cu = CameraUpdateFactory.newLatLngBounds(bounds, 300);
        //mClusterManager.getMarkerManager().getCollection("")
        MarkerManager.Collection markers = mClusterManager.getClusterMarkerCollection();
        markers.addMarker(new MarkerOptions().position(new LatLng(29.864321, 77.899181)).icon(BitmapDescriptorFactory.fromBitmap(
                new IconGenerator(getApplicationContext()).makeIcon())));

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
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

    private void setUpClusterer() {
        // Position the map.
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, mMap);


        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addMarkers();
    }

    public class MyItem implements ClusterItem {
        private LatLng mPosition;
        private String mSnippet;
        private String mTitle;

        public MyItem(double lat, double lng) {
            mPosition = new LatLng(lat, lng);

        }

        public MyItem(double lat, double lng, String title) {
            mPosition = new LatLng(lat, lng);
            mTitle = title;
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

    }
}
