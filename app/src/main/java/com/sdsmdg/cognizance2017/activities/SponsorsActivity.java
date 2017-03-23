package com.sdsmdg.cognizance2017.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.sdsmdg.cognizance2017.R;

import java.util.HashMap;

public class SponsorsActivity extends AppCompatActivity {

    private SliderLayout mDemoSlider;
    private boolean isOnSponser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsors);
        isOnSponser = getIntent().getBooleanExtra("isOnSponser", false);
        //ImageView sponsorImage = (ImageView) findViewById(R.id.sponsor_images);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();


        if (isOnSponser) {
            file_maps.put("Hannibal", R.drawable.cogni_splash);
            file_maps.put("Big Bang Theory", R.drawable.logo_two);
            file_maps.put("House of Cards", R.drawable.cogni_splash);
            file_maps.put("Game of Thrones", R.drawable.logo_two);
        } else {
            file_maps.put("Hannibal", R.drawable.cogni_splash);
            file_maps.put("Big Bang Theory", R.drawable.logo_two);
            file_maps.put("House of Cards", R.drawable.cogni_splash);
            file_maps.put("Game of Thrones", R.drawable.logo_two);
        }


        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
    }

    @Override
    public void onBackPressed() {
        MainActivity mainActivity = (MainActivity) MainActivity.mainAct;
        mainActivity.navigationView.setCheckedItem(mainActivity.getCurrentSelectedFragmentId());
        finish();
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
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }
}
