package com.uni.learningchess;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;
import java.util.Vector;

import static com.uni.learningchess.Pieza.Color.NEGRO;


public class Seccion5Practica2 extends MoverPiezaActivity implements TextToSpeech.OnInitListener {

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.LANG_MISSING_DATA | status == TextToSpeech.LANG_NOT_SUPPORTED) {
            //Toast.makeText(this, "ERROR LANG_MISSING_DATA | LANG_NOT_SUPPORTED", Toast.LENGTH_SHORT).show();

            // check for TTS data
            Intent checkTTSIntent = new Intent();
            checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkTTSIntent, 100);

        }
        // check for successful instantiation
        if (status == TextToSpeech.SUCCESS) {
            if (textToSpeech.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                textToSpeech.setLanguage(Locale.US);
        } else if (status == TextToSpeech.ERROR) {
            //Toast.makeText(this, "Sorry! Text To Speech failed...",
                    //Toast.LENGTH_LONG).show();
        }

    }

    private enum MODO {VALORPIEZAS, MOVPIEZAS, CAPTURAS}

    private Random random;
    private VistaAvatar avatar;
    TextView tvTituloEjercicio;
    ImageView ivSaltarEjercicio;
    int columnaAleatoria, filaAleatoria;
    Pieza.Tipo piezaSeleccionada;
    String coordenadaSolicitada;
    MODO tipoJuego;
    MetodosGenerales MG;
    Vector<Pieza> vectorPiezasBlancas;
    Vector<Pieza> vectorPiezasNegras;
    String[] letrasColumnas = {"A", "B", "C", "D", "E", "F", "G", "H"};

    TextToSpeech textToSpeech;


    SharedPreferences setting;
    BaseDatos baseDatos;
    String idUsuario = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        random = new Random(System.currentTimeMillis());

        baseDatos = new BaseDatos(this);
        setting = PreferenceManager.getDefaultSharedPreferences(this);
        idUsuario = setting.getString("spIdUsurioActual", "1");


        MG = new MetodosGenerales(this);
        vectorPiezasBlancas = new Vector<>();
        vectorPiezasNegras = new Vector<>();

        textToSpeech = new TextToSpeech(this, this);
        textToSpeech.setLanguage(new Locale("es", "NI"));


        tvTituloEjercicio = findViewById(R.id.tvTituloEjerciciosPracticas);
        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        tvTituloEjercicio.setTypeface(fuente);

        avatar = getAvatar();
        avatar.habla(R.raw.seccion5_piezas, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                seleccionaCoordenada();
            }
        });

        ivSaltarEjercicio = findViewById(R.id.ivSaltarEjercicio);
        ivSaltarEjercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionaCoordenada();
            }
        });
    }

    /**
     * Handle the results from the recognition activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // the user has the necessary data - create the TTS
                textToSpeech = new TextToSpeech(this, this);
            } else {
                // no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent
                        .setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.tablero;
    }

    public void seleccionaCoordenada() {
        columnaAleatoria = random.nextInt(7);
        filaAleatoria = random.nextInt(7);
        String columna = letrasColumnas[columnaAleatoria];

        tvTituloEjercicio.setVisibility(View.VISIBLE);
        ivSaltarEjercicio.setVisibility(View.VISIBLE);

        int fila = 1 + filaAleatoria;
        coordenadaSolicitada = columna + fila;

        seleccionaTipoJuego();
    }

    public void seleccionaTipoJuego() {

        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        retiraPiezas();

        tipoJuego = MODO.values()[random.nextInt(MODO.values().length)];
        AlternarDisenyo(tipoJuego);

        switch (tipoJuego) {
            case VALORPIEZAS:
                ModoValorPiezas();
                break;
            case MOVPIEZAS:
                ModoMoverPiezas();
                break;
            case CAPTURAS:
                ModoCapturarPiezas();
                break;
        }
    }

    //********  VALOR DE LAS PIEZAS ****************************
    //region Modo valor de las piezas

    String piezaIzquierda, piezaDerecha;
    ImageView imagenPiezaIzquierda, imagenPiezaDerecha;

    public void ModoValorPiezas() {

        Pieza.Tipo pieza1 = Pieza.Tipo.values()[random.nextInt(Pieza.Tipo.values().length)];
        while (pieza1 == Pieza.Tipo.REY) // mientras no sea un rey
            pieza1 = Pieza.Tipo.values()[random.nextInt(Pieza.Tipo.values().length)];

        Pieza.Tipo pieza2;
        // la pieza dos debe cumplir las siguientes condiciones
        /*  1- Pieza1 y pieza2 deben ser diferentes.
            2- El rey no tiene valor
            3- Piezas como caballo y alfil tienen mismo valor entonces
            no deben salir juntas las dos.*/
        do {
            pieza2 = Pieza.Tipo.values()[random.nextInt(Pieza.Tipo.values().length)];
        } while ((pieza1 == pieza2) ||
                (pieza2 == Pieza.Tipo.REY) ||
                (pieza1 == Pieza.Tipo.CABALLO && pieza2 == Pieza.Tipo.ALFIL) ||
                (pieza1 == Pieza.Tipo.ALFIL && pieza2 == Pieza.Tipo.CABALLO)
        );

        piezaIzquierda = pieza1.toString();
        piezaDerecha = pieza2.toString();
        mostrarPiezas();
    }

    private void mostrarPiezas() {

        piezaIzquierda = piezaIzquierda.toLowerCase();
        piezaDerecha = piezaDerecha.toLowerCase();

        tvTituloEjercicio.setText(String.format("¿Qué vale más?, ¿%s o %s?", piezaIzquierda, piezaDerecha));

        imagenPiezaDerecha = findViewById(R.id.piezaDerecha);
        imagenPiezaIzquierda = findViewById(R.id.piezaIzquierda);

        imagenPiezaIzquierda.setImageResource(getResources().getIdentifier(piezaIzquierda.toLowerCase() + "_blanco", "drawable", this.getPackageName()));
        imagenPiezaDerecha.setImageResource(getResources().getIdentifier(piezaDerecha.toLowerCase() + "_blanco", "drawable", this.getPackageName()));
        new ValoresPiezasActivity().crearTagValorPieza(piezaIzquierda, imagenPiezaIzquierda);
        new ValoresPiezasActivity().crearTagValorPieza(piezaDerecha, imagenPiezaDerecha);

        imagenPiezaIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout marco = findViewById(R.id.marcoIzquierda);
                if (new ValoresPiezasActivity().MetodoPracticaSeccion5(v, marco, avatar, imagenPiezaIzquierda, imagenPiezaDerecha)) {
                    baseDatos.IncrementaAcierto(idUsuario, "2");
                    seleccionaCoordenada();
                } else {
                    baseDatos.IncrementaEjercicioFalla(idUsuario, "2");
                }
            }
        });
        imagenPiezaDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout marco = findViewById(R.id.marcoDerecha);
                if (new ValoresPiezasActivity().MetodoPracticaSeccion5(v, marco, avatar, imagenPiezaIzquierda, imagenPiezaDerecha)) {
                    baseDatos.IncrementaAcierto(idUsuario, "2");
                    seleccionaCoordenada();
                } else {
                    baseDatos.IncrementaEjercicioFalla(idUsuario, "2");
                }
            }
        });
    }

    //endregion
    //*** FIN VALOR PIEZAS *************************************

    //********  MOVER PIEZAS ****************************
    //region Modo mover piezas
    public void ModoMoverPiezas() {

        // se encierra en un ciclo mientras la fila sea 7 (8) y la pieza sea Peon
        // no puede haber peon blanco fila 8.
        do {
            piezaSeleccionada = Pieza.Tipo.values()[random.nextInt(Pieza.Tipo.values().length)];
        } while (filaAleatoria == 7 && piezaSeleccionada == Pieza.Tipo.PEON);

        Pieza pieza = new Pieza(piezaSeleccionada, Pieza.Color.BLANCO, coordenadaSolicitada);
        vectorPiezasBlancas.add(pieza);
        colocaPiezas();
        tvTituloEjercicio.setText("¡La pieza " + pieza.getTipo() + " está en " + coordenadaSolicitada + "!, realizá un movimiento válido");
    }
    //endregion

    //********  CAPTURAR PIEZAS ****************************
    //region Modo capturar piezas
    public void ModoCapturarPiezas() {
        // se encierra en un ciclo mientras la fila sea 7 (8) y la pieza sea Peon
        // no puede haber peon blanco fila 8.
        do {
            piezaSeleccionada = Pieza.Tipo.values()[random.nextInt(Pieza.Tipo.values().length)];
        } while (filaAleatoria == 7 && piezaSeleccionada == Pieza.Tipo.PEON);

        Pieza pieza = new Pieza(piezaSeleccionada, Pieza.Color.BLANCO, coordenadaSolicitada);
        tvTituloEjercicio.setText(String.format("%s en %s, CAPTURA una pieza negra", pieza.getTipo(), coordenadaSolicitada));

        vectorPiezasBlancas.add(pieza);
        colocaPiezas();

        Vector<String> vectorMovValidos = obtenerCasillasMovValido(pieza.getColumna(), pieza.getFila(), validadorResaltarCasillas);
        Vector<String> casillasOcupadas = new Vector<>();
        casillasOcupadas.add(coordenadaSolicitada);
        int piezasValidas = 0;
        for (String casilla : vectorMovValidos) {
            //casillasOcupadas.add(vectorMovValidos.get(random.nextInt(vectorMovValidos.size())));
            casillasOcupadas.add(casilla);
            if (piezasValidas > 1)
                break;

            piezasValidas++;
        }
        ColocarPiezasNegraCapturas(vectorMovValidos, casillasOcupadas);
    }

    public void ColocarPiezasNegraCapturas(Vector<String> vectorMovValidos, Vector<String> casillasOcupadas) {
        Log.w("MovValidos", vectorMovValidos.toString());
        // colocar 5 piezas negras
        for (int i = 0; i < 5; i++) {
            Pieza.Tipo tipo = Pieza.Tipo.values()[random.nextInt(Pieza.Tipo.values().length)];
            if (i == 0) {
                //ya viene una o mas coordenada de pieza negra
                for (int j = 1; j < casillasOcupadas.size(); j++) {
                    vectorPiezasNegras.add(new Pieza(tipo, NEGRO, casillasOcupadas.get(j)));
                    tipo = Pieza.Tipo.values()[random.nextInt(Pieza.Tipo.values().length)];
                }
                continue;
            }

            String casilla = letrasColumnas[random.nextInt(7)] + (1 + random.nextInt(7));
            while (vectorMovValidos.contains(casilla) || casillasOcupadas.contains(casilla)) {
                casilla = letrasColumnas[random.nextInt(7)] + (1 + random.nextInt(7));
            }

            vectorPiezasNegras.add(new Pieza(tipo, NEGRO, casilla));
            casillasOcupadas.add(casilla);
        }

        colocaPiezas();
    }
    //endregion

    private Validador validador = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean validador = false;
            Pieza pieza = MG.getPieza(colOrigen, filaOrigen);

            if (pieza != null) {
                boolean movimientoValidoBlancas = false;
                if (tipoJuego == MODO.MOVPIEZAS) {
                    movimientoValidoBlancas = MG.validadorGenerico.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                }
                if (tipoJuego == MODO.CAPTURAS) {
                    new MovimientosCapturas().MetodoPracticaSeccion5(MG);
                    boolean capturaPieza = (MG.capturaPieza(colOrigen, filaOrigen, colDestino, filaDestino));
                    movimientoValidoBlancas = new MovimientosCapturas().validadorGenerico.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);

                    movimientoValidoBlancas = (capturaPieza && movimientoValidoBlancas);
                }

                Log.d("Ajedrez ", "***movimientoValidoBlancas=" + movimientoValidoBlancas);
                Pieza piezaDestino = MG.getPieza(colDestino, filaDestino);


                validador = movimientoValidoBlancas;

                pieza.setCoordenada(colDestino, filaDestino);
                pieza.setCoordenada(colOrigen, filaOrigen);
                if (piezaDestino != null && piezaDestino.getColor() == NEGRO) {
                    vectorPiezasNegras.add(piezaDestino);
                }
            }
            return (validador);
        }
    };

    protected void retiraPiezas() {
        LinearLayout tabla = findViewById(R.id.tabla);
        for (int f = 1, iMax = tabla.getChildCount() - 1; f < iMax; f++) {
            View vista = tabla.getChildAt(f);
            if (vista instanceof LinearLayout) {
                LinearLayout linea = (LinearLayout) vista;
                for (int c = 1, jMax = linea.getChildCount() - 1; c < jMax; c++) {
                    ImageView imagen = (ImageView) linea.getChildAt(c);
                    imagen.setImageDrawable(null);
                    if (esCuadriculaNegra(imagen)) {
                        imagen.setBackgroundResource(R.color.cuadriculaNegra);
                    } else {
                        imagen.setBackgroundResource(R.color.cuadriculaBlanca);
                    }
                }
            }
        }
    }

    private void colocaPiezas() {

        Vector<Pieza> vectorPiezas = new Vector<>();
        vectorPiezas.addAll(vectorPiezasBlancas);
        vectorPiezas.addAll(vectorPiezasNegras);
        MG.setVectorPiezas(vectorPiezas);

        for (Pieza pieza : vectorPiezasBlancas) {
            MG.colocaPieza(pieza);
        }
        for (Pieza pieza : vectorPiezasNegras) {
            MG.colocaPieza(pieza);
        }
    }

    public void AlternarDisenyo(MODO modo) {

        if (modo == MODO.VALORPIEZAS) {
            (findViewById(R.id.include_tablero)).setVisibility(View.GONE);
            (findViewById(R.id.include_valorpiezas)).setVisibility(View.VISIBLE);
        } else {
            (findViewById(R.id.include_tablero)).setVisibility(View.VISIBLE);
            (findViewById(R.id.include_valorpiezas)).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected boolean onMovimiento(int colOrigen, int filaOrigen, int colDestino,
                                   int filaDestino) {

        cancelaCuentaAtras();
        if (tipoJuego == MODO.MOVPIEZAS) {
            boolean movimientoValido = validador.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
            return movimientoValido;
        }
        if (tipoJuego == MODO.CAPTURAS) {
            boolean movimientoValido = validador.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
            return movimientoValido;
        }
        return false;
    }

    @Override
    protected void onMovimiento(boolean movimientoValido, int colOrigen, int filaOrigen,
                                int colDestino, int filaDestino) {
        Log.d("Ajedrez", "onMovimiento movimientoValido=" + movimientoValido + " colOrigen=" + colOrigen + " filaOrigen=" + filaOrigen + " colDestino=" + colDestino + " filaDestino=" + filaDestino);
        avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
        if (movimientoValido) {
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.ARQUEAR);
            avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
            avatar.habla(R.raw.ok_muy_bien, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                    baseDatos.IncrementaAcierto(idUsuario, "2");
                    seleccionaCoordenada();
                }
            });

        } else {
            avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_INCORRECTO);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);
            avatar.habla(R.raw.mover_pieza_mal, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                }
            });
            resaltarCasilla(colDestino, filaDestino, Movimiento.INCORRECTO);
            resaltarCasilla(colOrigen, filaOrigen, Movimiento.ORIGEN);
            if (tipoJuego == MODO.CAPTURAS) {
                Pieza p = vectorPiezasNegras.get(0);
                resaltarCasilla(p.getColumna(), p.getFila(), Movimiento.CORRECTO);
            } else
                resaltarCasilla(colOrigen, filaOrigen, validadorResaltarCasillas);

            baseDatos.IncrementaEjercicioFalla(idUsuario, "2");
        }
    }

    private Validador validadorResaltarCasillas = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean movimientoValido = false;
            if (MG.getPieza(colOrigen, filaOrigen) != null) {
                if (tipoJuego == MODO.MOVPIEZAS) {
                    movimientoValido = MG.validadorGenerico.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                }
                if (tipoJuego == MODO.CAPTURAS) {
                    new MovimientosCapturas().MetodoPracticaSeccion5(MG);
                    movimientoValido = new MovimientosCapturas().validadorGenerico.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                }
            }
            return (movimientoValido);
        }
    };

}