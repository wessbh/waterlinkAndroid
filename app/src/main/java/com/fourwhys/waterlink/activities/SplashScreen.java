package com.fourwhys.waterlink.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.fourwhys.waterlink.R;

public class SplashScreen extends AppCompatActivity {
    private final int splashTimeOut = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashScreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalshscreen);
        Animation animZoom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        ImageView maisonLogo = findViewById(R.id.logo);
        maisonLogo.startAnimation(animZoom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        },splashTimeOut);
    }
}
