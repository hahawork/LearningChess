package com.uni.learningchess;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;
import java.util.Vector;

import static com.uni.learningchess.Pieza.Color.BLANCO;
import static com.uni.learningchess.Pieza.Color.NEGRO;
import static com.uni.learningchess.Pieza.Tipo.ALFIL;
import static com.uni.learningchess.Pieza.Tipo.CABALLO;
import static com.uni.learningchess.Pieza.Tipo.DAMA;
import static com.uni.learningchess.Pieza.Tipo.PEON;
import static com.uni.learningchess.Pieza.Tipo.REY;
import static com.uni.learningchess.Pieza.Tipo.TORRE;

public class MovimientosCapturas extends MoverPiezaActivity {
    private VistaAvatar avatar;
    private int contadorMovimientos = 0;
    private Pieza PiezaBlanca;
    private Vector<Pieza> vectorPiezasNegras;
    private Random random;
    public static MetodosGenerales MG;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MG = new MetodosGenerales(this);
        getExtra();
        vectorPiezasNegras = new Vector<>();


        inicializaJugada(PiezaBlanca.getTipo());

        avatar = getAvatar();
        avatar.habla(R.raw.captura_pieza_presentacion, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                empiezaCuentaAtras();
            }
        });

    }

    public void MetodoPracticaSeccion5(MetodosGenerales mg) {
        MG = mg;
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int count = bundle.getInt("pieza", 0);
            Pieza.Tipo VienePieza = Pieza.Tipo.values()[count];
            PiezaBlanca = new Pieza(VienePieza, BLANCO, "E4");
        } else {
            PiezaBlanca = new Pieza(PEON, BLANCO, "B4");
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.tablero;
    }

    private Validador validadorPeon = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esPeon = (MG.getTipoPieza(colOrigen, filaOrigen) == PEON);
            boolean esBlanco = (MG.getColorPieza(colOrigen, filaOrigen) == BLANCO);
            boolean diferenteCasilla = (colOrigen != colDestino) && (filaOrigen != filaDestino);
            boolean avanzaUno = (filaDestino == filaOrigen + 1) && (Math.abs(colOrigen - colDestino) == 1);
            return (esPeon && diferenteCasilla && avanzaUno);
        }
    };

    private Validador validadorCaballo = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esCaballo = (MG.getTipoPieza(colOrigen, filaOrigen) == CABALLO);
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean vertical2horizintal1 = (Math.abs(filaOrigen - filaDestino) == 2)
                    && (Math.abs(colOrigen - colDestino) == 1);
            boolean vertical1horizintal2 = (Math.abs(filaOrigen - filaDestino) == 1)
                    && (Math.abs(colOrigen - colDestino) == 2);
            return (esCaballo && diferenteCasilla && (vertical2horizintal1 || vertical1horizintal2));
        }
    };

    private Validador validadorAlfil = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esAlfil = (MG.getTipoPieza(colOrigen, filaOrigen) == ALFIL);
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean mismaDiagonal = (Math.abs(filaOrigen - filaDestino) == Math.abs(colOrigen - colDestino));
            boolean saltaPiezas = MG.saltaPiezas(colOrigen, filaOrigen, colDestino, filaDestino);
            return (esAlfil && diferenteCasilla && mismaDiagonal && !saltaPiezas);
        }
    };

    private Validador validadorTorre = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esTorre = (MG.getTipoPieza(colOrigen, filaOrigen) == TORRE);
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean mismaColumna = (colOrigen == colDestino);
            boolean mismaFila = (filaOrigen == filaDestino);
            boolean saltaPiezas = MG.saltaPiezas(colOrigen, filaOrigen, colDestino, filaDestino);
            return (esTorre && diferenteCasilla && (mismaColumna || mismaFila) && !saltaPiezas);
        }
    };

    private Validador validadorDama = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esDama = (MG.getTipoPieza(colOrigen, filaOrigen) == DAMA);
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean mismaColumna = (colOrigen == colDestino);
            boolean mismaFila = (filaOrigen == filaDestino);
            boolean mismaDiagonal = (Math.abs(filaOrigen - filaDestino) == Math.abs(colOrigen - colDestino));
            boolean saltaPiezas = MG.saltaPiezas(colOrigen, filaOrigen, colDestino, filaDestino);
            return (esDama && diferenteCasilla && (mismaColumna || mismaFila || mismaDiagonal) && !saltaPiezas);
        }
    };

    private Validador validadorRey = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esRey = (MG.getTipoPieza(colOrigen, filaOrigen) == REY);
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean distanciaUno = (Math.abs(filaOrigen - filaDestino) <= 1)
                    && (Math.abs(colOrigen - colDestino) <= 1);
            return (esRey && diferenteCasilla && distanciaUno);
        }
    };

    public Validador validadorGenerico = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean movimientoValidoBlancas = false;
            if (MG.hayPieza(colOrigen, filaOrigen) && MG.casillaDisponible(colOrigen, filaOrigen, colDestino, filaDestino)) {
                switch (MG.getTipoPieza(colOrigen, filaOrigen)) {
                    case PEON:
                        movimientoValidoBlancas = validadorPeon.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case CABALLO:
                        movimientoValidoBlancas = validadorCaballo.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case ALFIL:
                        movimientoValidoBlancas = validadorAlfil.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case TORRE:
                        movimientoValidoBlancas = validadorTorre.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case DAMA:
                        movimientoValidoBlancas = validadorDama.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case REY:
                        movimientoValidoBlancas = validadorRey.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                }
            }
            return (movimientoValidoBlancas);
        }
    };

    private Validador validador = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean validador = false;
            Pieza pieza = MG.getPieza(colOrigen, filaOrigen);

            if (pieza != null) {
                boolean movimientoValidoBlancas = validadorGenerico.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                Log.d("Ajedrez ", "***movimientoValidoBlancas=" + movimientoValidoBlancas);
                Pieza piezaDestino = MG.getPieza(colDestino, filaDestino);
                if (piezaDestino != null && piezaDestino.getColor() == NEGRO) {
                    vectorPiezasNegras.remove(piezaDestino);
                }

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
        vectorPiezas.add(PiezaBlanca);
        vectorPiezas.addAll(vectorPiezasNegras);
        MG.setVectorPiezas(vectorPiezas);

        MG.colocaPieza(PiezaBlanca);

        for (Pieza pieza : vectorPiezasNegras) {
            MG.colocaPieza(pieza);
        }
    }

    private void inicializaJugada(Pieza.Tipo Tipo) {

        retiraPiezas();
        switch (Tipo) {
            case REY:
                inicializaJugadaRey();
                break;
            case DAMA:
                inicializaJugadaDama();
                break;
            case TORRE:
                inicializaJugadaTorre();
                break;
            case CABALLO:
                inicializaJugadaCaballo();
                break;
            case ALFIL:
                inicializaJugadaAlfil();
                break;
            case PEON:
                inicializaJugadaPeon();
                break;
        }
    }

    private void inicializaJugadaRey() {
        vectorPiezasNegras.removeAllElements();
        switch (contadorMovimientos) {
            case 0:
                PiezaBlanca = new Pieza(REY, BLANCO, "E1");

                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "D2"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "A5"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "E3"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "G3"));
                break;
            case 1:
                PiezaBlanca = new Pieza(REY, BLANCO, "A3");

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "C2"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "A4"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "E3"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "A1"));
                break;
            case 2:
                PiezaBlanca = new Pieza(REY, BLANCO, "C1");

                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "C2"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "G2"));
                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "F3"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "A1"));
                break;
            case 3:
                PiezaBlanca = new Pieza(REY, BLANCO, "B3");

                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "H4"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "C1"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "E7"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "C4"));
                break;
        }
        colocaPiezas();
    }

    private void inicializaJugadaDama() {
        vectorPiezasNegras.removeAllElements();

        switch (contadorMovimientos) {
            case 0:
                PiezaBlanca = new Pieza(DAMA, BLANCO, "C1");
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "E3"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "A4"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "D4"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "F3"));
                break;
            case 1:
                PiezaBlanca = new Pieza(DAMA, BLANCO, "H3");
                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "E6"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "H1"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "F5"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "E2"));
                break;
            case 2:
                PiezaBlanca = new Pieza(DAMA, BLANCO, "E4");
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "B6"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "A1"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "G3"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "D3"));
                break;
            case 3:
                PiezaBlanca = new Pieza(DAMA, BLANCO, "B3");
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "B6"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "D4"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "D2"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "C4"));
                break;
        }

        colocaPiezas();
    }

    private void inicializaJugadaTorre() {
        vectorPiezasNegras.removeAllElements();

        switch (contadorMovimientos) {
            case 0:
                PiezaBlanca = new Pieza(TORRE, BLANCO, "E4");
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "C4"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "B5"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "G3"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "B3"));
                break;
            case 1:
                PiezaBlanca = new Pieza(TORRE, BLANCO, "B3");
                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "D5"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "B6"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "D2"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "C1"));
                break;
            case 2:
                PiezaBlanca = new Pieza(TORRE, BLANCO, "F1");
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "H1"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "E4"));
                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "G4"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "D2"));
                break;
            case 3:
                PiezaBlanca = new Pieza(TORRE, BLANCO, "H5");
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "F5"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "G7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "E7"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "G4"));
                break;
        }
        colocaPiezas();
    }

    private void inicializaJugadaCaballo() {
        vectorPiezasNegras.removeAllElements();
        switch (contadorMovimientos) {
            case 0:
                PiezaBlanca = new Pieza(CABALLO, BLANCO, "C1");
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "D3"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "A3"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "F2"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "E1"));
                break;
            case 1:
                PiezaBlanca = new Pieza(CABALLO, BLANCO, "E3");
                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "C3"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "D5"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "H4"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "F3"));
                break;
            case 2:
                PiezaBlanca = new Pieza(CABALLO, BLANCO, "G2");
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "H4"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "E1"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "H1"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "F3"));
                break;
            case 3:
                PiezaBlanca = new Pieza(CABALLO, BLANCO, "E5");
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "C6"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "G3"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "E4"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "H5"));
                break;
        }
        colocaPiezas();
    }

    private void inicializaJugadaAlfil() {
        vectorPiezasNegras.removeAllElements();
        switch (contadorMovimientos) {
            case 0:
                PiezaBlanca = new Pieza(ALFIL, BLANCO, "B1");
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "H7"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "A2"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "D1"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "B2"));
                break;
            case 1:
                PiezaBlanca = new Pieza(ALFIL, BLANCO, "D2");
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "B4"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "D4"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "E1"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "B1"));
                break;
            case 2:
                PiezaBlanca = new Pieza(ALFIL, BLANCO, "G4");
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "E6"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "D1"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "D5"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "H4"));
                break;
            case 3:
                PiezaBlanca = new Pieza(ALFIL, BLANCO, "F1");
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "C4"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "G4"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "E3"));
                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "D8"));
                break;
        }
        colocaPiezas();
    }

    private void inicializaJugadaPeon() {
        vectorPiezasNegras.removeAllElements();
        switch (contadorMovimientos) {
            case 0:
                PiezaBlanca = new Pieza(PEON, BLANCO, "F4");

                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "G5"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "E4"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "F7"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "B3"));
                break;
            case 1:
                PiezaBlanca = new Pieza(PEON, BLANCO, "A3");

                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "B4"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "A5"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "E3"));
                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "D5"));
                break;
            case 2:
                PiezaBlanca = new Pieza(PEON, BLANCO, "F3");

                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "G4"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "H5"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "F6"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "F2"));
                break;
            case 3:
                PiezaBlanca = new Pieza(PEON, BLANCO, "C4");

                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "B5"));
                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "E6"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "F7"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "E3"));
                break;
        }
        colocaPiezas();
    }

    public void eliminaPieza(Pieza pieza) {
        vectorPiezasNegras.removeElement(pieza);
    }

    @Override
    protected void onFinalCuentaAtras() {
        avatar.habla(R.raw.captura_pieza_presentacion, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                empiezaCuentaAtras();
            }
        });
    }

    @Override
    protected boolean onMovimiento(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        Log.d("Ajedrez", "onMovimiento colOrigen=" + colOrigen + " filaOrigen=" + filaOrigen + " colDestino=" + colDestino + " filaDestino=" + filaDestino);
        cancelaCuentaAtras();
        Pieza piezaDestino = MG.getPieza(colDestino, filaDestino);
        boolean movimientoPiezaBlanca = (MG.getColorPieza(colOrigen, filaOrigen) == BLANCO);
        boolean movimientoValido = validador.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
        boolean capturaPieza = (MG.capturaPieza(colOrigen, filaOrigen, colDestino, filaDestino));
        boolean movimientoCorrecto = movimientoPiezaBlanca && movimientoValido && capturaPieza;//en este caso es valido capturar pieza

        if (movimientoCorrecto && capturaPieza) eliminaPieza(piezaDestino);

        Log.d("Ajedrez", "movimientoPiezaBlanca=" + movimientoPiezaBlanca);
        Log.d("Ajedrez", "movimientoValido=" + movimientoValido);
        Log.d("Ajedrez", "capturaPieza=" + capturaPieza);
        Log.d("Ajedrez", "movimientoCorrecto=" + movimientoCorrecto);
        return movimientoCorrecto;
    }

    @Override
    protected void onMovimiento(boolean movimientoValido, int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        Log.d("Ajedrez", "onMovimiento movimientoValido=" + movimientoValido + " colOrigen=" + colOrigen + " filaOrigen=" + filaOrigen + " colDestino=" + colDestino + " filaDestino=" + filaDestino);
        avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
        if (movimientoValido) {
            contadorMovimientos++;
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.ARQUEAR);
            if (contadorMovimientos < 4) {
                avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
                avatar.habla(R.raw.ok_has_acertado, new VistaAvatar.OnAvatarHabla() {
                    @Override
                    public void onTerminaHabla() {
                        avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                        empiezaCuentaAtras();
                    }
                });
                inicializaJugada(PiezaBlanca.getTipo());
            } else {
                avatar.lanzaAnimacion(VistaAvatar.Animacion.EJERCICIO_SUPERADO);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.EJERCICIO_SUPERADO);
                avatar.habla(R.raw.excelente_completaste_ejercicios, new VistaAvatar.OnAvatarHabla() {
                    @Override
                    public void onTerminaHabla() {
                        finish();
                    }
                });
            }
        } else {
            avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_INCORRECTO);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);
            avatar.habla(R.raw.captura_pieza_mal, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                    empiezaCuentaAtras();
                }
            });

            Pieza pieza = MG.getPieza(colOrigen, filaOrigen);
        }
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

}
