package com.uni.learningchess;

import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Seccion4 extends AppCompatActivity {

    private VistaAvatar avatar;
    MetodosGenerales MG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_seccion4);

        MG = new MetodosGenerales(this);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        // Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BungeeShade-Regular.ttf");
        // Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/HoltwoodOneSC.ttf");
        Typeface fuente1 = Typeface.createFromAsset(getAssets(), "fonts/Gorditas-Bold.ttf");

        Button botonJaqueMate = findViewById(R.id.botonJaqueMate);
        botonJaqueMate.setTypeface(fuente);
        Button botonAhogado = findViewById(R.id.botonAhogado);
        botonAhogado.setTypeface(fuente);
        Button botonMoverRey = findViewById(R.id.botonMoverRey);
        botonMoverRey.setTypeface(fuente);


        TextView textoCapitulo21 = findViewById(R.id.textoCapitulo21);
        textoCapitulo21.setTypeface(fuente1);
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

    public void botonJaqueMate(View v) {
        MG.MostrarDialogoVideo_Practica("Y4zJekczDNA", "Seccion4JaqueMateReyActivity");
    }

    public void botonAhogado(View v) {
        MG.MostrarDialogoVideo_Practica("PB-r8JQtefQ", "Seccion4Ahogado");
    }

    public void botonMoverRey(View view) {
        MG.MostrarDialogoVideo_Practica("vUmJhxVGKzc", "Seccion4SalvarReyDelJaque");
    }
}
