package com.uni.learningchess;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Seccion3 extends AppCompatActivity {
    private VistaAvatar avatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_seccion3);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        // Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BungeeShade-Regular.ttf");
        // Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/HoltwoodOneSC.ttf");
        Typeface fuente1 = Typeface.createFromAsset(getAssets(), "fonts/Gorditas-Bold.ttf");
        Button boton12 = findViewById(R.id.boton12);
        boton12.setTypeface(fuente);


        TextView textoCapitulo21 = findViewById(R.id.textoCapitulo21);
        textoCapitulo21.setTypeface(fuente1);
        TextView textoLeccion = findViewById(R.id.textoLeccion);
        textoLeccion.setTypeface(fuente);
        TextView textoEjercicios = findViewById(R.id.textoEjercicios);
        textoEjercicios.setTypeface(fuente);

        avatar = findViewById(R.id.vistaAvatar1);
        avatar.setActividad(this);
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

    public void boton12(View v) {
        Intent i = new Intent(Seccion3.this, VerVideo.class);
        i.putExtra("video_id", "XqO5Ck-Fm4E");
        startActivity(i);
    }

    public void botonIniciarEjercicios(View view) {
        startActivity(new Intent(this, Seccion3Ejerc1.class));
    }
}
