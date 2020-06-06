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

public class Seccion4JaqueMateReyActivity extends EjercicioBaseActivity implements DialogoSeleccionarCoronacion.ObtenerPiezaSeleccionada {
    private VistaAvatar avatar;
    private int contadorMovimientos = 0;
    private Vector<Pieza> vectorPiezasBlancas;
    private Vector<Pieza> vectorPiezasNegras;
    private Vector<Pieza> vectorPiezasBlancasAtacantes; // Que hacen jaque al Rey.

    private Random random;
    Pieza PeonCoronado;
    MetodosGenerales mg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vectorPiezasBlancas = new Vector<>();
        vectorPiezasNegras = new Vector<>();
        vectorPiezasBlancasAtacantes = new Vector<>();

        mg=new MetodosGenerales(this);

        inicializaJugada(contadorMovimientos);

        avatar = getAvatar();
//        avatar.habla(R.raw.mover_rey_en_jaque, new VistaAvatar.OnAvatarHabla() {
        avatar.habla(R.raw.presentacion_jaquemate, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                empiezaCuentaAtras();
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.tablero;
    }

    private Validador validadorPeon = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esPeon = (mg.getTipoPieza(colOrigen, filaOrigen) == PEON);
            boolean esBlanco = (mg.getColorPieza(colOrigen, filaOrigen) == BLANCO);
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean avanzaUno = ((esBlanco && (filaDestino == filaOrigen + 1))
                    || (!esBlanco && (filaDestino == filaOrigen - 1))
                    && (colDestino == colOrigen));
            return (esPeon && diferenteCasilla && avanzaUno);
        }
    };

    private Validador validadorCaballo = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esCaballo = (mg.getTipoPieza(colOrigen, filaOrigen) == CABALLO);
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
            boolean esAlfil = (mg.getTipoPieza(colOrigen, filaOrigen) == ALFIL);
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean mismaDiagonal = (Math.abs(filaOrigen - filaDestino) == Math.abs(colOrigen - colDestino));
            boolean saltaPiezas = mg.saltaPiezas(colOrigen, filaOrigen, colDestino, filaDestino);
            return (esAlfil && diferenteCasilla && mismaDiagonal && !saltaPiezas);
        }
    };

    private Validador validadorTorre = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esTorre = (mg.getTipoPieza(colOrigen, filaOrigen) == TORRE);
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean mismaColumna = (colOrigen == colDestino);
            boolean mismaFila = (filaOrigen == filaDestino);
            boolean saltaPiezas = mg.saltaPiezas(colOrigen, filaOrigen, colDestino, filaDestino);
            return (esTorre && diferenteCasilla && (mismaColumna || mismaFila) && !saltaPiezas);
        }
    };

    private Validador validadorDama = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esDama = (mg.getTipoPieza(colOrigen, filaOrigen) == DAMA);
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean mismaColumna = (colOrigen == colDestino);
            boolean mismaFila = (filaOrigen == filaDestino);
            boolean mismaDiagonal = (Math.abs(filaOrigen - filaDestino) == Math.abs(colOrigen - colDestino));
            boolean saltaPiezas = mg.saltaPiezas(colOrigen, filaOrigen, colDestino, filaDestino);
            return (esDama && diferenteCasilla && (mismaColumna || mismaFila || mismaDiagonal) && !saltaPiezas);
        }
    };

    private Validador validadorRey = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esRey = (mg.getTipoPieza(colOrigen, filaOrigen) == REY);
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean distanciaUno = (Math.abs(filaOrigen - filaDestino) <= 1)
                    && (Math.abs(colOrigen - colDestino) <= 1);
            return (esRey && diferenteCasilla && distanciaUno);
        }
    };

    private Validador validadorGenerico = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean movimientoValidoBlancas = false;
            if (mg.hayPieza(colOrigen, filaOrigen) && mg.casillaDisponible(colOrigen, filaOrigen, colDestino, filaDestino)) {
                switch (mg.getTipoPieza(colOrigen, filaOrigen)) {
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
            Pieza pieza = mg.getPieza(colOrigen, filaOrigen);

            if (pieza != null) {
                boolean movimientoValidoBlancas = validadorGenerico.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                Log.d("Ajedrez", "***movimientoValidoBlancas=" + movimientoValidoBlancas);
                Pieza piezaDestino = mg.getPieza(colDestino, filaDestino);
                if (piezaDestino != null && piezaDestino.getColor() == NEGRO) {
                    vectorPiezasNegras.remove(piezaDestino);
                }


                if (movimientoValidoBlancas && (pieza.getTipo() == PEON) && filaDestino == 7) { // fila 7 por que empieza de Cero
                    PeonCoronado = pieza;
                    DialogoSeleccionarCoronacion dsc = new DialogoSeleccionarCoronacion();
                    dsc.showDialog(Seccion4JaqueMateReyActivity.this, colDestino, filaDestino);
                }


                pieza.setCoordenada(colDestino, filaDestino);
                boolean reyEnJaque = reyEnJaque();
                Log.d("Ajedrez", "***reyEnJaque=" + reyEnJaque);
                validador = movimientoValidoBlancas && reyEnJaque;
                pieza.setCoordenada(colOrigen, filaOrigen);
                if (piezaDestino != null && piezaDestino.getColor() == NEGRO) {
                    vectorPiezasNegras.add(piezaDestino);
                }
            }
            return (validador);
        }
    };

    private boolean reyEnJaque() {
        vectorPiezasBlancasAtacantes.removeAllElements();
        Pieza reyNegro = vectorPiezasNegras.get(0);
        Log.d("Ajedrez", "reyBlanco.getTipo()=" + reyNegro.getTipo());
        for (int i = 0; i < vectorPiezasBlancas.size(); i++) {
            Pieza piezaBlanca = vectorPiezasBlancas.get(i);
            Log.d("Ajedrez", "piezaBlanca.getTipo()=" + piezaBlanca.getTipo());
            switch (piezaBlanca.getTipo()) {
                case PEON:
                    if (validadorPeon.movimientoValido(piezaBlanca.getColumna(), piezaBlanca.getFila(),
                            reyNegro.getColumna(), reyNegro.getFila())) {
                        vectorPiezasBlancasAtacantes.add(piezaBlanca);
                    }
                    break;

                case CABALLO:
                    if (validadorCaballo.movimientoValido(piezaBlanca.getColumna(), piezaBlanca.getFila(),
                            reyNegro.getColumna(), reyNegro.getFila())) {
                        vectorPiezasBlancasAtacantes.add(piezaBlanca);
                    }
                    break;

                case ALFIL:
                    if (validadorAlfil.movimientoValido(piezaBlanca.getColumna(), piezaBlanca.getFila(),
                            reyNegro.getColumna(), reyNegro.getFila())) {
                        vectorPiezasBlancasAtacantes.add(piezaBlanca);
                    }
                    break;

                case TORRE:
                    if (validadorTorre.movimientoValido(piezaBlanca.getColumna(), piezaBlanca.getFila(),
                            reyNegro.getColumna(), reyNegro.getFila())) {
                        vectorPiezasBlancasAtacantes.add(piezaBlanca);
                    }
                    break;

                case DAMA:
                    if (validadorDama.movimientoValido(piezaBlanca.getColumna(), piezaBlanca.getFila(),
                            reyNegro.getColumna(), reyNegro.getFila())) {
                        vectorPiezasBlancasAtacantes.add(piezaBlanca);
                    }
                    break;

                case REY:
                    if (validadorRey.movimientoValido(piezaBlanca.getColumna(), piezaBlanca.getFila(),
                            reyNegro.getColumna(), reyNegro.getFila())) {
                        vectorPiezasBlancasAtacantes.add(piezaBlanca);
                    }
                    break;
            }
        }
        return (vectorPiezasBlancasAtacantes.size() > 0);
    }

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

    public void eliminaPieza(Pieza pieza) {
        vectorPiezasBlancas.removeElement(pieza);
        vectorPiezasNegras.removeElement(pieza);
    }

    private void inicializaJugada(int jugada) {
        random = new Random(System.currentTimeMillis());
        retiraPiezas();
        Log.d("Ajedrez", "jugada=" + jugada);
        switch (jugada) {
            case 0:
                inicializaJugada1();
                break;
            case 1:
                inicializaJugada2();
                break;
            case 2:
                inicializaJugada3();
                break;
        }
    }

    private void inicializaJugada1() {
        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        int variante = random.nextInt(3);

        switch (variante) {
            case 0: // tablero 1 segun documento
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "H1"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "A3"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "B2"));
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "B3"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "D1", true, "D8"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "E4"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "G2"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "H2"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "G8"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "A7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "C5"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "C7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "G7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "H7"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "B6"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "F7"));
                break;
            case 1://tablero 2
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "C1"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "A2"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "B2"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "C2"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "H1"));
                vectorPiezasBlancas.add(new Pieza(CABALLO, BLANCO, "E5", true, "G6"));
                vectorPiezasBlancas.add(new Pieza(ALFIL, BLANCO, "B3"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "H8"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "A7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "H7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "G7"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "A8"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "B8"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "C8"));
                break;
            case 2: //tablero 13
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "G1"));
                vectorPiezasBlancas.add(new Pieza(ALFIL, BLANCO, "A5"));
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "F4", true, "C7"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "D7"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "E8"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "E6"));
                break;
        }

        colocaPiezas();

    }

    private void inicializaJugada2() {
        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        int variante = random.nextInt(2);
        switch (variante) {
            case 0://tablero 12
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "F1"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "B7"));
                vectorPiezasBlancas.add(new Pieza(CABALLO, BLANCO, "E6", true, "G7"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "E8"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "D8"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "F8"));
                break;
            case 1://tablero 6
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "G2"));
                vectorPiezasBlancas.add(new Pieza(ALFIL, BLANCO, "G3"));
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "A4", true, "C6"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "C8"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "D8"));
                break;
            case 2://tablero 7
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "H1"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "E6"));
                vectorPiezasBlancas.add(new Pieza(CABALLO, BLANCO, "C5", true, "D7"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "C2"));



                
                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "C7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "B7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "D6"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "D8"));
                break;
        }
        colocaPiezas();
    }

    private void inicializaJugada3() {
        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        int variante = random.nextInt(3);
        switch (variante) {
            case 0://tablero 8
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "G1"));
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "A1"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "D1"));
                vectorPiezasBlancas.add(new Pieza(CABALLO, BLANCO, "D4", true, "E6"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "E2"));
                vectorPiezasBlancas.add(new Pieza(ALFIL, BLANCO, "F7"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "E5"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "E4"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "E6"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "F5"));
                break;
            case 1://tablero 9
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "C1"));
                vectorPiezasBlancas.add(new Pieza(ALFIL, BLANCO, "B2"));
                vectorPiezasBlancas.add(new Pieza(CABALLO, BLANCO, "G5"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "H6", true, "H7"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "G8"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "F8"));
                break;
            case 2://tablero 10
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "H1"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "B1"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "D6"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "G7",true,"C7"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "C8"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "D8"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "C7"));
                break;        }
        colocaPiezas();
    }

    private void colocaPiezas() {

        Vector<Pieza> vectorPiezas = new Vector<>();
        vectorPiezas.addAll(vectorPiezasBlancas);
        vectorPiezas.addAll(vectorPiezasNegras);
        mg.setVectorPiezas(vectorPiezas);

        for (Pieza pieza : vectorPiezasBlancas) {
            mg.colocaPieza(pieza);
        }
        for (Pieza pieza : vectorPiezasNegras) {
            mg.colocaPieza(pieza);
        }

    }

    @Override
    protected void onFinalCuentaAtras() {
        avatar.habla(R.raw.presentacion_jaquemate, new VistaAvatar.OnAvatarHabla() {
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
        Pieza piezaDestino = mg.getPieza(colDestino, filaDestino);
        boolean movimientoPiezaBlanca = (mg.getColorPieza(colOrigen, filaOrigen) == BLANCO);
        boolean movimientoValido = validador.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
        boolean capturaPieza = (mg.capturaPieza(colOrigen, filaOrigen, colDestino, filaDestino));
        boolean movimientoCorrecto = movimientoPiezaBlanca && movimientoValido;
        boolean capturaReyNegro = capturaPieza && (mg.getColorPieza(colOrigen, filaOrigen) == NEGRO);
        boolean esPiezaCorrectaJaqueMate = mg.getPieza(colOrigen,filaOrigen).isMoverDarJaqueMate();
        boolean esCasillaCorrecta = mg.getPieza(colOrigen, filaOrigen).getColumnaCorrecta() == colDestino &&
                mg.getPieza(colOrigen, filaOrigen).getFilaCorrecta() == filaDestino;

        if (movimientoCorrecto && capturaPieza) eliminaPieza(piezaDestino);

        Log.d("Ajedrez", "movimientoPiezaBlanca=" + movimientoPiezaBlanca);
        Log.d("Ajedrez", "movimientoValido=" + movimientoValido);
        Log.d("Ajedrez", "capturaPieza=" + capturaPieza);
        Log.d("Ajedrez", "movimientoCorrecto=" + movimientoCorrecto);
        return movimientoCorrecto && esPiezaCorrectaJaqueMate && esCasillaCorrecta && !capturaReyNegro;
    }

    @Override
    protected void onMovimiento(boolean movimientoValido, int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        Log.d("Ajedrez", "onMovimiento movimientoValido=" + movimientoValido + " colOrigen=" + colOrigen + " filaOrigen=" + filaOrigen + " colDestino=" + colDestino + " filaDestino=" + filaDestino);
        avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
        if (movimientoValido) {
            contadorMovimientos++;
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.ARQUEAR);
            if (contadorMovimientos < 3) {
                avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
                avatar.habla(R.raw.ok_has_acertado, new VistaAvatar.OnAvatarHabla() {
                    @Override
                    public void onTerminaHabla() {
                        avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                        empiezaCuentaAtras();
                    }
                });
                inicializaJugada(contadorMovimientos);
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
            avatar.habla(R.raw.mal_intenta_otra_vez, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                    empiezaCuentaAtras();
                }
            });
            for (int i = 0; i < vectorPiezasBlancasAtacantes.size(); i++) {
                /*Toast.makeText(this, "Jaque de "
                        + vectorPiezasBlancasAtacantes.get(i).getTipo().toString().toLowerCase()
                        + " al rey", Toast.LENGTH_SHORT).show();*/
            }
        }
    }

    @Override
    public void PiezaCoronada(Pieza pieza) {

        vectorPiezasBlancas.add(pieza);
        mg.colocaPieza(pieza);
        eliminaPieza(PeonCoronado);
        PeonCoronado.setTipo(pieza.getTipo());
        validador.movimientoValido(PeonCoronado.getColumna(),PeonCoronado.getFila(),pieza.getColumna(),pieza.getFila());
    }
}