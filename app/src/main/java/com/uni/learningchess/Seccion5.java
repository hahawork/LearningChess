package com.uni.learningchess;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Seccion5 extends AppCompatActivity {

    private VistaAvatar avatar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_seccion5);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        // Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BungeeShade-Regular.ttf");
        // Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/HoltwoodOneSC.ttf");
        Typeface fuente1 = Typeface.createFromAsset(getAssets(),"fonts/Gorditas-Bold.ttf");

        TextView textoTituloApp = findViewById(R.id.textoCapitulo5);
        textoTituloApp.setTypeface(fuente1);
        TextView textoContenidos = findViewById(R.id.textoLeccion);
        textoContenidos.setTypeface(fuente);


        avatar = findViewById(R.id.vistaAvatar1);
        avatar.setActividad(this);

       // findViewById(R.id.botonPractica1).setRotation(45);
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

    public void boton1(View v) {
        Intent i = new Intent(Seccion5.this, VerVideo.class);
        i.putExtra("video_id", "Z6_uYARH_Vo");
        startActivity(i);
    }

    public void botonPractica1(View view) {
        startActivity(new Intent(this, Seccion5Practica1.class));
    }
    public void botonPractica2(View view) {
        startActivity(new Intent(this, Seccion5Practica2.class));
    }
    public void botonPractica3(View view) {
        startActivity(new Intent(this, Seccion5Practica3.class));
    }
    public void botonPractica4(View view) {
    }
}
