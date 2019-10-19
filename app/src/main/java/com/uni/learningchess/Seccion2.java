package com.uni.learningchess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Seccion2 extends AppCompatActivity {

    private VistaAvatar avatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_seccion2);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        // Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BungeeShade-Regular.ttf");
        // Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/HoltwoodOneSC.ttf");
        Typeface fuente1 = Typeface.createFromAsset(getAssets(), "fonts/Gorditas-Bold.ttf");
        Button boton9 = (Button) findViewById(R.id.boton9);
        boton9.setTypeface(fuente);
        Button boton10 = (Button) findViewById(R.id.boton10);
        boton10.setTypeface(fuente);
        Button boton11 = (Button) findViewById(R.id.boton11);
        boton11.setTypeface(fuente);
        Button boton12 = (Button) findViewById(R.id.boton12);
        boton12.setTypeface(fuente);
        Button boton13 = (Button) findViewById(R.id.boton13);
        boton13.setTypeface(fuente);
        Button boton14 = (Button) findViewById(R.id.boton14);
        boton14.setTypeface(fuente);
        Button botonVerVideo = findViewById(R.id.botonVerVideo);
        botonVerVideo.setTypeface(fuente);


        TextView textoCapitulo12 = (TextView) findViewById(R.id.textoCapitulo12);
        textoCapitulo12.setTypeface(fuente1);
        TextView textoLeccion = (TextView) findViewById(R.id.textoLeccion);
        textoLeccion.setTypeface(fuente);
        TextView textoEjercicios = (TextView) findViewById(R.id.textoEjercicios);
        textoEjercicios.setTypeface(fuente);

        avatar = (VistaAvatar) findViewById(R.id.vistaAvatar1);
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

    public void botonMovimientos(View view) {
        ((TextView) findViewById(R.id.textoEjercicios)).setText(getResources().getString(R.string.ejercicios) + " Movimientos.");
        findViewById(R.id.tlMovimientos).setVisibility(View.VISIBLE);
        findViewById(R.id.tlCapturas).setVisibility(View.GONE);
        findViewById(R.id.tlJugadasEspeciales).setVisibility(View.GONE);
    }

    public void botonCaptura(View view) {
        ((TextView) findViewById(R.id.textoEjercicios)).setText(getResources().getString(R.string.ejercicios) + " Capturas.");
        findViewById(R.id.tlMovimientos).setVisibility(View.GONE);
        findViewById(R.id.tlCapturas).setVisibility(View.VISIBLE);
        findViewById(R.id.tlJugadasEspeciales).setVisibility(View.GONE);
    }

    public void botonJugadasEspeciales(View view) {
        ((TextView) findViewById(R.id.textoEjercicios)).setText(getResources().getString(R.string.ejercicios) + " Jugadas Especiales.");
        findViewById(R.id.tlMovimientos).setVisibility(View.GONE);
        findViewById(R.id.tlCapturas).setVisibility(View.GONE);
        findViewById(R.id.tlJugadasEspeciales).setVisibility(View.VISIBLE);
    }

    public void botonMoverTorre(View v) {
        startActivity(new Intent(this, MoverTorreActivity.class));
    }

    public void botonMoverAlfil(View v) {
        startActivity(new Intent(this, MoverAlfilActivity.class));
    }

    public void botonMoverDama(View v) {
        startActivity(new Intent(this, MoverDamaActivity.class));
    }

    public void botonMoverCaballo(View v) {
        startActivity(new Intent(this, MoverCaballoActivity.class));
    }

    public void botonMoverPeon(View v) {
        startActivity(new Intent(this, MoverPeonActivity.class));
    }

    public void botonMoverRey(View v) {
        startActivity(new Intent(this, MoverReyActivity.class));
    }

    public void botonjugEspec1(View view) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tipo de Enroque.");
        builder.setIcon(R.mipmap.ic_launcher);
        // add a list
        String[] enroques = {"Enroque largo.", "Enroque corto."};
        builder.setItems(enroques, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startActivity(new Intent(Seccion2.this, MovimientosEnroque.class)
                        .putExtra("tipoEnroque",which));
             /*   switch (which) {
                    case 0: // largo

                    case 1: // corto
                }*/
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void botonjugEspec2(View view) {
        startActivity(new Intent(this, MovimientoPeonAlPaso.class));
    }

    public void botonjugEspec3(View view) {
        startActivity(new Intent(this, MovimientosCoronacion.class));
    }

    public void botonVerVideo(View view) {
        Intent i = new Intent(Seccion2.this, VerVideo.class);
        i.putExtra("video_id", "Z6_uYARH_Vo");
        startActivity(i);
    }

    public void botonValorPiezas(View view) {
        startActivity(new Intent(this, ValoresPiezasActivity.class));
    }

    public void botonCapturar(View view) {

        Intent intent = new Intent(this,MovimientosCapturas.class);

        if (view == findViewById(R.id.botonCapturaRey)){
            intent.putExtra("pieza",Pieza.Tipo.REY.ordinal());
        }
        if (view == findViewById(R.id.botonCapturaDama)){
            intent.putExtra("pieza",Pieza.Tipo.DAMA.ordinal());
        }
        if (view == findViewById(R.id.botonCapturaTorre)){
            intent.putExtra("pieza",Pieza.Tipo.TORRE.ordinal());
        }
        if (view == findViewById(R.id.botonCapturaCaballo)){
            intent.putExtra("pieza",Pieza.Tipo.CABALLO.ordinal());
        }
        if (view == findViewById(R.id.botonCapturaAlfil)){
            intent.putExtra("pieza",Pieza.Tipo.ALFIL.ordinal());
        }
        if (view == findViewById(R.id.botonCapturaPeon)){
            intent.putExtra("pieza",Pieza.Tipo.PEON.ordinal());
        }

        startActivity(intent);
    }
}
