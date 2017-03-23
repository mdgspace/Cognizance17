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
            file_maps.put("1", R.drawable.capture_one);
            file_maps.put("2", R.drawable.capture_two);
            file_maps.put("3", R.drawable.capture_three);
            file_maps.put("4", R.drawable.capture_four);
            file_maps.put("5", R.drawable.capture_five);
            file_maps.put("6", R.drawable.capture_six);
            file_maps.put("7", R.drawable.capture_seven);
            file_maps.put("8", R.drawable.capture_eight);
            file_maps.put("9", R.drawable.capture_nine);
            file_maps.put("10", R.drawable.capture_ten);
            file_maps.put("11", R.drawable.capture_eleven);
        } else {
            file_maps.put("Coke Studio", R.drawable.dayone);
            file_maps.put("The Viral Fever", R.drawable.test_image);
            file_maps.put("Papon", R.drawable.day_three_one);
            file_maps.put("Sachin-Jigar", R.drawable.day_three_two);
        }


        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            if (isOnSponser)
                textSliderView
                        //.description(name)
                        .image(file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop);
            else
                textSliderView
                        .description(name)
                        .image(file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop);

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
