package com.application.chatthingsup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    ImageView appLogo;
    TextView appName, developerName;
    Animation animationTop, animationBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appLogo = findViewById(R.id.appIcon);
        appName = findViewById(R.id.appName);
        developerName = findViewById(R.id.developerName);

        animationTop = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        animationBottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        appLogo.setAnimation(animationTop);
        appName.setAnimation(animationBottom);
        developerName.setAnimation(animationBottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}