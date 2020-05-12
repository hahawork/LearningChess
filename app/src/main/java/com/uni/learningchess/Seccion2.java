package com.uni.learningchess;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
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

        Button boton9 = findViewById(R.id.boton9);
        boton9.setTypeface(fuente);

        Button boton10 = findViewById(R.id.boton10);
        boton10.setTypeface(fuente);

        Button boton11 = findViewById(R.id.boton11);
        boton11.setTypeface(fuente);

        Button boton12 = findViewById(R.id.boton12);
        boton12.setTypeface(fuente);

        Button boton13 = findViewById(R.id.boton13);
        boton13.setTypeface(fuente);

        Button boton14 = findViewById(R.id.boton14);
        boton14.setTypeface(fuente);

        TextView textoCapitulo12 = findViewById(R.id.textoCapitulo12);
        textoCapitulo12.setTypeface(fuente1);

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

    public void botonMovimientos(View view) {
        ((TextView) findViewById(R.id.textoEjercicios)).setText(getResources().getString(R.string.ejercicios) + " Movimientos");
        View tblLayout = findViewById(R.id.tlMovimientos);
        tblLayout.setVisibility(View.VISIBLE);

        findViewById(R.id.tlCapturas).setVisibility(View.GONE);
        findViewById(R.id.tlJugadasEspeciales).setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void botonCaptura(View view) {

        ((TextView) findViewById(R.id.textoEjercicios)).setText(getResources().getString(R.string.ejercicios) + " Capturas");
        findViewById(R.id.tlMovimientos).setVisibility(View.GONE);
        findViewById(R.id.tlCapturas).setVisibility(View.VISIBLE);
        findViewById(R.id.tlJugadasEspeciales).setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void botonJugadasEspeciales(View view) {
        ((TextView) findViewById(R.id.textoEjercicios)).setText(getResources().getString(R.string.ejercicios) + " Jugadas Especiales");
        findViewById(R.id.tlMovimientos).setVisibility(View.GONE);
        findViewById(R.id.tlCapturas).setVisibility(View.GONE);
        findViewById(R.id.tlJugadasEspeciales).setVisibility(View.VISIBLE);
    }

    public void botonMoverTorre(View v) {
        MostrarDialogoVideo_Practica("d_1ZNqL8ZCA","MoverTorreActivity");
        // startActivity(new Intent(this, MoverTorreActivity.class));
    }

    public void botonMoverAlfil(View v) {
        MostrarDialogoVideo_Practica("0WmPWFOhkOY","MoverAlfilActivity");
        //startActivity(new Intent(this, MoverAlfilActivity.class));
    }

    public void botonMoverDama(View v) {
        MostrarDialogoVideo_Practica("Q6bAgnOVSSM","MoverDamaActivity");
        //startActivity(new Intent(this, MoverDamaActivity.class));
    }

    public void botonMoverCaballo(View v) {
        MostrarDialogoVideo_Practica("sq7TlC8IVm4","MoverCaballoActivity");
        //startActivity(new Intent(this, MoverCaballoActivity.class));
    }

    public void botonMoverPeon(View v) {
        MostrarDialogoVideo_Practica("Y2fOHkq6Ke0","MoverPeonActivity");
        //startActivity(new Intent(this, MoverPeonActivity.class));
    }

    public void botonMoverRey(View v) {
        MostrarDialogoVideo_Practica("KfW9070tQx0","MoverReyActivity");
        //startActivity(new Intent(this, MoverReyActivity.class));
    }

    public void botonjugEspec1(View view) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tipo de Enroque");
        builder.setIcon(R.mipmap.ic_launcher);
        // add a list
        String[] enroques = {"Enroque largo", "Enroque corto"};
        builder.setItems(enroques, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                startActivity(new Intent(Seccion2.this, MovimientosEnroque.class)
                        .putExtra("tipoEnroque", which));
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

    public void botonValorPiezas(View view) {
        startActivity(new Intent(this, ValoresPiezasActivity.class));
    }

    public void botonCapturar(View view) {

        Intent intent = new Intent(this, MovimientosCapturas.class);

        if (view == findViewById(R.id.botonCapturaRey)) {
            intent.putExtra("pieza", Pieza.Tipo.REY.ordinal());
        }
        if (view == findViewById(R.id.botonCapturaDama)) {
            intent.putExtra("pieza", Pieza.Tipo.DAMA.ordinal());
        }
        if (view == findViewById(R.id.botonCapturaTorre)) {
            intent.putExtra("pieza", Pieza.Tipo.TORRE.ordinal());
        }
        if (view == findViewById(R.id.botonCapturaCaballo)) {
            intent.putExtra("pieza", Pieza.Tipo.CABALLO.ordinal());
        }
        if (view == findViewById(R.id.botonCapturaAlfil)) {
            intent.putExtra("pieza", Pieza.Tipo.ALFIL.ordinal());
        }
        if (view == findViewById(R.id.botonCapturaPeon)) {
            intent.putExtra("pieza", Pieza.Tipo.PEON.ordinal());
        }

        startActivity(intent);
    }


    public void MostrarDialogoVideo_Practica(final String URL, final String Activit) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogo_movim_piezas_video_practica);

        ImageButton Practicar = dialog.findViewById(R.id.ibtnPracticar_dlgmpvp);
        Practicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NombrePaquete = getApplicationContext().getPackageName() + "." + Activit;
                try {
                    Class<?> c = Class.forName(NombrePaquete);
                    Intent intent = new Intent(getApplicationContext(), c);
                    startActivity(intent);
                } catch (ClassNotFoundException ignored) {
                }
            }
        });

        ImageButton VerVideo = dialog.findViewById(R.id.ibtnVerVideo_dlgmpvp);
        VerVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Seccion2.this, VerVideo.class);
                i.putExtra("video_id", URL);
                startActivity(i);
            }
        });


        ImageButton dialogButton = dialog.findViewById(R.id.ibtnCerrar_dlgmpvp);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

}
