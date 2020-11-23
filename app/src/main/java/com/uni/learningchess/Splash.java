package com.uni.learningchess;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.File;

import static com.uni.learningchess.interf_general.RESOURCE_SDCARD_PATH;

public class Splash extends AppCompatActivity implements Animation.AnimationListener {

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(Splash.this, Home.class));
            Splash.this.finishAffinity();
        }
    };

    ImageView imgPoster;
    // Animation
    Animation animZoomIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imgPoster = findViewById(R.id.imgLogo);
        // load the animation
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
// start the animation
        imgPoster.startAnimation(animZoomIn);

        VerificaCarpetaRecursosCreada();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(AUTO_HIDE_DELAY_MILLIS);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void VerificaCarpetaRecursosCreada(){
        if (!new File(RESOURCE_SDCARD_PATH).exists()){
            new File(RESOURCE_SDCARD_PATH).mkdirs();
        }
    }
}