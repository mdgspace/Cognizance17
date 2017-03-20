package com.sdsmdg.cognizance2017.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.sdsmdg.cognizance2017.R;
import com.sdsmdg.cognizance2017.SessionManager;

public class SplashActivity extends AppCompatActivity {


    private int SPLASH_TIME_OUT = 2000;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView = (ImageView) findViewById(R.id.circle);
        final Animation startRotateAnimation = AnimationUtils.loadAnimation(this, R.anim.android_rotate_animation);
        startRotateAnimation.setRepeatCount(Animation.INFINITE);
        imageView.startAnimation(startRotateAnimation);
        //session class instance
        session = new SessionManager(getApplicationContext());

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             */

            @Override
            public void run() {
                if(session.isLoggedIn()){
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }
                else {
                    session.checkLogIn();
                }
                /**
                 * call this function when you want to check if the user is logged in or not
                 * this will check if the user is logged in or not and then direct it to login activity
                 */
            }
        }, SPLASH_TIME_OUT);
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

}
