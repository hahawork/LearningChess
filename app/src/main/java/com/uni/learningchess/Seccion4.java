package com.uni.learningchess;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Seccion4 extends AppCompatActivity {

    private VistaAvatar avatar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_seccion4);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        // Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BungeeShade-Regular.ttf");
        // Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/HoltwoodOneSC.ttf");
        Typeface fuente1 = Typeface.createFromAsset(getAssets(),"fonts/Gorditas-Bold.ttf");
        Button botonVerVideo = findViewById(R.id.botonVerVideo);
        botonVerVideo.setTypeface(fuente);
        Button botonJaqueMate = findViewById(R.id.botonJaqueMate);
        botonJaqueMate.setTypeface(fuente);
        Button botonAhogado = findViewById(R.id.botonAhogado);
        botonAhogado.setTypeface(fuente);
        Button botonMoverRey = findViewById(R.id.botonMoverRey);
        botonMoverRey.setTypeface(fuente);



        TextView textoCapitulo21 = findViewById(R.id.textoCapitulo21);
        textoCapitulo21.setTypeface(fuente1);
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

    public void botonVerVideo(View v) {

        Intent i = new Intent(Seccion4.this, VerVideo.class);
        i.putExtra("video_id", "Z6_uYARH_Vo");
        startActivity(i);
    }

    public void botonJaqueMate(View v) {
        startActivity(new Intent(this, Seccion4JaqueMateReyActivity.class));
    }

    public void botonAhogado(View v) {
        startActivity(new Intent(this, Seccion4Ahogado.class));
    }

    public void botonMoverRey(View view) {
        startActivity(new Intent(this, Seccion4SalvarReyDelJaque.class));
    }
}
