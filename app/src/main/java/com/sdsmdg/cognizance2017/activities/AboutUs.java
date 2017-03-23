package com.sdsmdg.cognizance2017.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdsmdg.cognizance2017.R;

public class AboutUs extends AppCompatActivity {

    private ImageView websiteLinkImage;
    private ImageView facebookLinkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
        registerListeners();
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
    public void onBackPressed() {
        MainActivity mainActivity = (MainActivity) MainActivity.mainAct;
        mainActivity.navigationView.setCheckedItem(mainActivity.getCurrentSelectedFragmentId());
        finish();
    }

    private void registerListeners() {
        websiteLinkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browser = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://mdg.sdslabs.co/"));
                startActivity(browser);
            }
        });
        facebookLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browser = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://goo.gl/6Cznj6"));
                startActivity(browser);
            }
        });
    }

    private void initView() {
        websiteLinkImage = (ImageView) findViewById(R.id.about_website_link);
        facebookLinkTextView = (ImageView) findViewById(R.id.about_facebook_link);

    }
}
