package com.uni.learningchess;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Vector;

public class ConocerPiezas extends AppCompatActivity {

    private Vector<String> vectorPiezas;
    private Random random;
    private ImageView imagenPiezaIzquierda;
    private VistaAvatar avatar;

    private CountDownTimer cuentaAtras;

    private String piezaIzquierda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conocer_piezas);


        vectorPiezas = new Vector<>();
        vectorPiezas.add("rey");
        vectorPiezas.add("dama");
        vectorPiezas.add("torre");
        vectorPiezas.add("caballo");
        vectorPiezas.add("alfil");
        vectorPiezas.add("peon");

        TextView titulo = findViewById(R.id.tituloValorPiezas);
        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/Nunito-ExtraBold.ttf");
        titulo.setTypeface(fuente);

        imagenPiezaIzquierda = findViewById(R.id.piezaIzquierda);

        random = new Random(System.currentTimeMillis());

        avatar = findViewById(R.id.vistaAvatar);
        avatar.setActividad(this);

        int TIEMPO_CUENTA_ATRAS = 8000;
        configuraCuentaAtras(TIEMPO_CUENTA_ATRAS);

        avatar.habla(R.raw.conocer_las_piezas, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                mostrarPiezas();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        avatar.reanudar();

    }

    @Override
    public void onPause() {

        cancelaCuentaAtras();
        avatar.pausar();

        super.onPause();

    }

    private void mostrarPiezas() {
        String pieza = "";
        do {
            int indice = random.nextInt(vectorPiezas.size());
            pieza = vectorPiezas.get(indice);
        } while (pieza.equalsIgnoreCase(piezaIzquierda));

        piezaIzquierda = pieza;
        vectorPiezas.remove(pieza);

        piezaIzquierda = piezaIzquierda.toLowerCase();
        imagenPiezaIzquierda.setImageResource(getResources().getIdentifier(piezaIzquierda.toLowerCase() + "_blanco", "drawable", this.getPackageName()));
        Animation animSequential;
        animSequential = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animacion_rotar_elemento);
        imagenPiezaIzquierda.startAnimation(animSequential);

        //crearTagValorPieza(piezaIzquierda, imagenPiezaIzquierda);

    }

    private void configuraCuentaAtras(long millisUntilFinished) {
        cuentaAtras = new CountDownTimer(millisUntilFinished, millisUntilFinished) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                onFinalCuentaAtras();
            }
        };
    }

    public void empiezaCuentaAtras() {
        cuentaAtras.start();
    }

    public void cancelaCuentaAtras() {
        cuentaAtras.cancel();
    }


    protected void onFinalCuentaAtras() {
        avatar.habla(R.raw.conocer_las_piezas, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                empiezaCuentaAtras();

            }
        });
    }

    public void SeleccionaNombrePieza(View view) {

        String botonTag = view.getTag().toString();

        if (botonTag.equalsIgnoreCase(piezaIzquierda)) {
            avatar.lanzaAnimacion(VistaAvatar.Animacion.EJERCICIO_SUPERADO);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.EJERCICIO_SUPERADO);
            avatar.habla(R.raw.ok_muy_bien, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {

                    if (vectorPiezas.size() > 0) {
                        mostrarPiezas();
                    } else {
                        finish();
                    }
                }
            });
        } else {
            avatar.habla(R.raw.incorrecto);
            avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_INCORRECTO);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);
            avatar.habla(R.raw.mal_intenta_otra_vez);
        }
    }
}
