package com.sdsmdg.cognizance2017.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int MY_PERMISSIONS_REQUEST_CAM = 1;
    private ZXingScannerView mScannerView;
    private String barCodeResult;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        //Request Camera permissions

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAM);
            } else {
                //Permission is granted
            }
        } else {
            //Permission available
        }
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAM: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted

                } else {
                    // permission denied
                    MainActivity mainActivity = (MainActivity) MainActivity.mainAct;
                    mainActivity.navigationView.setCheckedItem(mainActivity.getCurrentSelectedFragmentId());
                    finish();
                }
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void onBackPressed() {
        MainActivity mainActivity = (MainActivity) MainActivity.mainAct;
        mainActivity.navigationView.setCheckedItem(mainActivity.getCurrentSelectedFragmentId());
        finish();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        //Log.v(TAG, rawResult.getText()); // Prints scan results
        //Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        barCodeResult = rawResult.getText();
        //Toast.makeText(this, barCodeResult, Toast.LENGTH_SHORT).show();
        MainActivity mainActivity = (MainActivity) MainActivity.mainAct;
        mainActivity.navigationView.setCheckedItem(mainActivity.getCurrentSelectedFragmentId());
        Intent intent = new Intent(this, RatingActivity.class);
        intent.putExtra("ImageKey", barCodeResult);
        if (barCodeResult.charAt(0) == '#')
            startActivity(intent);
        finish();

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }

}
