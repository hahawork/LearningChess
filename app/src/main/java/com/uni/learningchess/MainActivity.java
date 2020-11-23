package com.uni.learningchess;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private VistaAvatar avatar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        Typeface fuente1 = Typeface.createFromAsset(getAssets(), "fonts/Gorditas-Bold.ttf");
        //Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BungeeShade-Regular.ttf");
        //Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/HoltwoodOneSC.ttf");
        Button boton1 = findViewById(R.id.boton1);
        boton1.setTypeface(fuente);
        Button boton2 = findViewById(R.id.boton2);
        boton2.setTypeface(fuente);
        Button boton3 = findViewById(R.id.boton3);
        boton3.setTypeface(fuente);

        Button boton4 = findViewById(R.id.boton4);
        boton4.setTypeface(fuente);

        TextView textoTituloApp = findViewById(R.id.textoTituloApp);
        textoTituloApp.setTypeface(fuente1);
        TextView textoContenidos = findViewById(R.id.textoContenidos);
        textoContenidos.setTypeface(fuente);


        avatar = findViewById(R.id.vistaAvatar);
        avatar.setActividad(this);

    }


    public void boton1(View v) {
        startActivity(new Intent(this, Seccion1.class));
    }

    public void boton2(View v) {
        startActivity(new Intent(this, Seccion2.class));
    }

    public void boton3(View v) {
        startActivity(new Intent(this, Seccion3.class));
    }

    public void boton4(View v) {
        startActivity(new Intent(this, Seccion4.class));
    }

}
