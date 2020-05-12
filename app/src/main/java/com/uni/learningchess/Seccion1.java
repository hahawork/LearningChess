package com.uni.learningchess;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Seccion1 extends AppCompatActivity {

    private VistaAvatar avatar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_seccion1);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        // Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BungeeShade-Regular.ttf");
        // Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/HoltwoodOneSC.ttf");
        Typeface fuente1 = Typeface.createFromAsset(getAssets(),"fonts/Gorditas-Bold.ttf");
        Button boton6 = findViewById(R.id.boton6);
        boton6.setTypeface(fuente);
        Button boton7 = findViewById(R.id.boton7);
        boton7.setTypeface(fuente);
        Button boton8 = findViewById(R.id.boton8);
        boton8.setTypeface(fuente);

        TextView textoCapitulo1 = findViewById(R.id.textoCapitulo1);
        textoCapitulo1.setTypeface(fuente1);
        TextView textoLeccion = findViewById(R.id.textoLeccion);
        textoLeccion.setTypeface(fuente);
        TextView textoEjercicios = findViewById(R.id.textoEjercicios);
        textoEjercicios.setTypeface(fuente);

        avatar = findViewById(R.id.vistaAvatar1);
        avatar.setActividad(this);
       // presentacion();
    }

    @Override
    public void onResume() {
        super.onResume();
        avatar.reanudar();
    }

    @Override
    public void onPause() {
        avatar.pausar();
        super.onPause();
    }

    public void boton6(View v) {
        startActivity(new Intent(this, SenalarCasillas.class));
    }
    public void boton7(View v) {
        startActivity(new Intent(this, ConocerPiezas.class));
    }
    public void boton8(View v) {
        startActivity(new Intent(this, ColocarPiezasActivity.class));
    }

}
