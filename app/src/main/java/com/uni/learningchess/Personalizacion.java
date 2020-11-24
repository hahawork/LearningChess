package com.uni.learningchess;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

public class Personalizacion extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizacion);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/CabinSketch-Bold.ttf");
        Typeface fuente1 = Typeface.createFromAsset(getAssets(), "fonts/LoveYaLikeASister.ttf");
        Typeface fuente2 = Typeface.createFromAsset(getAssets(), "fonts/IndieFlower.ttf");
        Typeface fuente3 = Typeface.createFromAsset(getAssets(), "fonts/Dekko-Regular.ttf");

    }

    public void seleccionarAvatar(View view) {
        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this);
        Toast.makeText(this, "Avata seleccionado.", Toast.LENGTH_LONG).show();
        String tag = view.getTag().toString();
        SharedPreferences.Editor editor = setting.edit();
        editor.putString("setting_avatar", tag);
        editor.apply();
        editor.commit();
        startActivity(new Intent(this,Home.class));
        this.finishAffinity();
    }
}