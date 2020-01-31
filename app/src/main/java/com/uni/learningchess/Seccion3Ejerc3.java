package com.uni.learningchess;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Seccion3Ejerc3 extends Seccion3BaseActivity {

    Button botonOpcion1, botonOpcion2;
    ArrayList<casilla> Casillas = new ArrayList<>();
    COLOR casillaAleatoria;
    private VistaAvatar avatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = findViewById(R.id.ejercicio3);
        linearLayout.setVisibility(View.VISIBLE);

        String[] Columnas = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};

        //Llena el arreglo con la cantidad total de casillas
        COLOR color = COLOR.NEGRA;
        Casillas.clear();
        for (String letra : Columnas) {
            for (int i = 0; i < Columnas.length; i++) {

                Casillas.add(new casilla(letra, (i + 1), color));
                //el nuevo color sera el color contrario del actual
                color = color == COLOR.NEGRA ? COLOR.BLANCA : COLOR.NEGRA;
            }
            //el nuevo color sera el color contrario del actual
            color = color == COLOR.NEGRA ? COLOR.BLANCA : COLOR.NEGRA;
        }

        int seleccionada = new Random().nextInt(Casillas.size());
        TextView titulo = findViewById(R.id.textoSeccion3Ejerc3Titulo);
        titulo.setText(String.format(getResources().getString(R.string.seccion3Ejerc3Titulo),
                Casillas.get(seleccionada).getColumna() + "" + Casillas.get(seleccionada).getFila()));
        Animation animSequential;
        animSequential = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animacion_rotar_elemento);
        titulo.startAnimation(animSequential);

        casillaAleatoria = Casillas.get(seleccionada).getColor();
        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        botonOpcion1 = findViewById(R.id.botonBlanca);
        botonOpcion1.setTypeface(fuente);
        botonOpcion2 = findViewById(R.id.botonNegra);
        botonOpcion2.setTypeface(fuente);

        avatar = getAvatar();
        avatar.habla(R.raw.mover_rey_en_jaque, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                empiezaCuentaAtras();
            }
        });


    }

    public void EventosBotones(View view) {

        boolean botonBlanca = view == botonOpcion1;
        boolean casillaBlanca = casillaAleatoria == COLOR.BLANCA;

        boolean botonNegra = view == botonOpcion2;
        boolean casillaNegra = casillaAleatoria == COLOR.NEGRA;

        // si es el boton de blanca y el color es blanca
        if (botonBlanca && casillaBlanca) {
            avatar.lanzaAnimacion(VistaAvatar.Animacion.EJERCICIO_SUPERADO);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.EJERCICIO_SUPERADO);
            avatar.habla(R.raw.excelente_completaste_ejercicios, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    startActivity(new Intent(Seccion3Ejerc3.this,Seccion3Ejerc4.class));
                    finish();
                }
            });
        }
        // si es el boton de negra y el color es negra
        else if (botonNegra && casillaNegra) {
            avatar.lanzaAnimacion(VistaAvatar.Animacion.EJERCICIO_SUPERADO);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.EJERCICIO_SUPERADO);
            avatar.habla(R.raw.excelente_completaste_ejercicios, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    startActivity(new Intent(Seccion3Ejerc3.this,Seccion3Ejerc4.class));
                    finish();
                }
            });
        } else {
            avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_INCORRECTO);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);
            avatar.habla(R.raw.incorrecto, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                    empiezaCuentaAtras();
                    Seccion3Ejerc3.this.recreate();
                }
            });
        }

    }

    public enum COLOR {
        BLANCA, NEGRA
    }

    static class casilla {
        private String columna;
        private int fila;
        private COLOR color;

        public casilla(String columna, int fila, COLOR color) {
            this.columna = columna;
            this.fila = fila;
            this.color = color;
        }

        public String getColumna() {
            return columna;
        }

        public int getFila() {
            return fila;
        }

        public COLOR getColor() {
            return color;
        }
    }
}
