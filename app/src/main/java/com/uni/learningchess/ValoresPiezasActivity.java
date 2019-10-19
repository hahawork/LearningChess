package com.uni.learningchess;

import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Vector;

public class ValoresPiezasActivity extends AppCompatActivity implements View.OnClickListener {
    private Vector<String> vectorParesPiezas;
    private String parPiezasSeleccionado;
    private int audioParesPiezaSeleccionado;
    private int respuestasCorrectas;
    private Random random;
    private ImageView imagenPiezaIzquierda, imagenPiezaDerecha;
    private VistaAvatar avatar;

    private CountDownTimer cuentaAtras;

    private String piezaIzquierda;
    private String piezaDerecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valor_piezas);
        vectorParesPiezas = new Vector<>();
        vectorParesPiezas.add("dama_torre");
        vectorParesPiezas.add("caballo_torre");
        vectorParesPiezas.add("peon_alfil");
        vectorParesPiezas.add("alfil_dama");
        vectorParesPiezas.add("dama_peon");
        vectorParesPiezas.add("torre_peon");
        vectorParesPiezas.add("alfil_torre");
        vectorParesPiezas.add("caballo_peon");
        vectorParesPiezas.add("caballo_dama");

        parPiezasSeleccionado = null;

        TextView titulo = findViewById(R.id.tituloValorPiezas);
        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/Nunito-ExtraBold.ttf");
        titulo.setTypeface(fuente);

        imagenPiezaIzquierda = findViewById(R.id.piezaIzquierda);
        imagenPiezaDerecha = findViewById(R.id.piezaDerecha);

        imagenPiezaIzquierda.setOnClickListener(this);
        imagenPiezaDerecha.setOnClickListener(this);


        int TIEMPO_CUENTA_ATRAS = 8000;
        configuraCuentaAtras(TIEMPO_CUENTA_ATRAS);


        respuestasCorrectas = 0;
        random = new Random(System.currentTimeMillis());

        avatar = findViewById(R.id.vistaAvatar);
        avatar.setActividad(this);


        avatar.habla(R.raw.valorespiezas_presentacion, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                seleccionarParPiezas();
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


    public void seleccionarParPiezas() {
        int indice = random.nextInt(vectorParesPiezas.size());
        parPiezasSeleccionado = vectorParesPiezas.elementAt(indice);
        vectorParesPiezas.remove(indice);
        String[] piezas = parPiezasSeleccionado.split("_");
        piezaIzquierda = piezas[0];
        piezaDerecha = piezas[1];
        audioParesPiezaSeleccionado = getResources().getIdentifier("valorespiezas_" + parPiezasSeleccionado.toLowerCase(), "raw", this.getPackageName());
        mostrarPiezas();
    }

    private void mostrarPiezas() {

        piezaIzquierda = piezaIzquierda.toLowerCase();
        piezaDerecha = piezaDerecha.toLowerCase();
        imagenPiezaIzquierda.setImageResource(getResources().getIdentifier(piezaIzquierda.toLowerCase() + "_blanco", "drawable", this.getPackageName()));
        imagenPiezaDerecha.setImageResource(getResources().getIdentifier(piezaDerecha.toLowerCase() + "_blanco", "drawable", this.getPackageName()));
        crearTagValorPieza(piezaIzquierda, imagenPiezaIzquierda);
        crearTagValorPieza(piezaDerecha, imagenPiezaDerecha);
        hacerPregunta();

    }


    public void crearTagValorPieza(String pieza, ImageView imagenPieza) {

        switch (pieza) {
            case "peon":
                imagenPieza.setTag("1");
                break;
            case "caballo":
            case "alfil":
                imagenPieza.setTag("3");
                break;
            case "torre":
                imagenPieza.setTag("5");
                break;
            case "dama":
                imagenPieza.setTag("9");
                break;

        }


    }


    public void hacerPregunta() {

        avatar.habla(audioParesPiezaSeleccionado, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                empiezaCuentaAtras();
            }
        });

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
        avatar.habla(audioParesPiezaSeleccionado, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                empiezaCuentaAtras();

            }
        });
    }

    @Override
    public void onClick(View v) {

        LinearLayout marco;
        if (parPiezasSeleccionado != null) {
            cancelaCuentaAtras();
            if (seHaPulsadoPiezaIquierda(v)) {
                marco = findViewById(R.id.marcoIzquierda);
            } else {
                marco = findViewById(R.id.marcoDerecha);
            }

            if (esCorrectaPulsacion(v)) {
                respuestasCorrectas++;
                avatar.mueveCejas(VistaAvatar.MovimientoCejas.ARQUEAR);
                switch (respuestasCorrectas) {
                    case 1:
                        avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
                        avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
                        marco.setBackgroundResource(R.drawable.animacion_marco_correcto);
                        animar(marco);

                        avatar.habla(R.raw.ok_has_acertado, new VistaAvatar.OnAvatarHabla() {
                            @Override
                            public void onTerminaHabla() {
                                seleccionarParPiezas();
                            }
                        });
                        break;

                    case 2:
                        avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
                        avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
                        marco.setBackgroundResource(R.drawable.animacion_marco_correcto);
                        animar(marco);
                        avatar.habla(R.raw.ok_muy_bien, new VistaAvatar.OnAvatarHabla() {
                            @Override
                            public void onTerminaHabla() {
                                seleccionarParPiezas();
                            }
                        });
                        break;

                    case 3:
                        avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.EJERCICIO_SUPERADO);
                        avatar.lanzaAnimacion(VistaAvatar.Animacion.EJERCICIO_SUPERADO);
                        marco.setBackgroundResource(R.drawable.animacion_marco_correcto);
                        animar(marco);
                        avatar.habla(R.raw.ok_superado, new VistaAvatar.OnAvatarHabla() {
                            @Override
                            public void onTerminaHabla() {
                                finish();
                            }
                        });
                        break;
                }
            } else {
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
                avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_INCORRECTO);
                marco.setBackgroundResource(R.drawable.animacion_marco_incorrecto);
                animar(marco);
                avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);
                hacerPregunta();
            }
        }
    }

    public boolean MetodoPracticaSeccion5(
            View v,
            LinearLayout marco,
            VistaAvatar avatar,
            ImageView imgPI,
            ImageView imgPD) {

        boolean EsCorrecto = false;
        imagenPiezaIzquierda = imgPI;
        imagenPiezaDerecha = imgPD;

        if (esCorrectaPulsacion(v)) {
            EsCorrecto = true;
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.ARQUEAR);
            switch (new Random(System.currentTimeMillis()).nextInt(2)) {
                case 0:
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
                    avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
                    marco.setBackgroundResource(R.drawable.animacion_marco_correcto);
                    animar(marco);

                    avatar.habla(R.raw.ok_has_acertado, new VistaAvatar.OnAvatarHabla() {
                        @Override
                        public void onTerminaHabla() {
                        }
                    });
                    break;

                case 1:
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
                    avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
                    marco.setBackgroundResource(R.drawable.animacion_marco_correcto);
                    animar(marco);
                    avatar.habla(R.raw.ok_muy_bien, new VistaAvatar.OnAvatarHabla() {
                        @Override
                        public void onTerminaHabla() {
                        }
                    });
                    break;

            }
        } else {
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
            avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_INCORRECTO);
            marco.setBackgroundResource(R.drawable.animacion_marco_incorrecto);
            animar(marco);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);
        }

        return EsCorrecto;
    }

    public boolean seHaPulsadoPiezaIquierda(View v) {
        return v.getId() == R.id.piezaIzquierda;
    }

    public boolean esCorrectaPulsacion(View v) {
        switch (v.getId()) {
            case R.id.piezaIzquierda:
                return valeMas(imagenPiezaIzquierda, imagenPiezaDerecha);
            case R.id.piezaDerecha:
                return valeMas(imagenPiezaDerecha, imagenPiezaIzquierda);
            default:
                return false;
        }

    }

    public boolean valeMas(View piezaSeleccionada, View piezaNoSeleccionada) {
        int valorPiezaSeleccionada = Integer.parseInt(piezaSeleccionada.getTag().toString());
        int valorPiezaNoSeleccionada = Integer.parseInt(piezaNoSeleccionada.getTag().toString());

        return valorPiezaSeleccionada >= valorPiezaNoSeleccionada;
    }

    public void animar(View v) {
        AnimationDrawable animarMarco;
        animarMarco = (AnimationDrawable) v.getBackground();
        animarMarco.stop();
        animarMarco.start();

    }


}
