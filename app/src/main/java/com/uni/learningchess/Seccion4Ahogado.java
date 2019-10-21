package com.uni.learningchess;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class Seccion4Ahogado extends EjercicioBaseActivity {
    private VistaAvatar avatar;
    private int contadorMovimientos = 0;
    public Vector<Pieza> vectorPiezasBlancas;
    public Vector<Pieza> vectorPiezasNegras;
    public Vector<Pieza> vectorPiezasBlancasDefensoras; // Que pueden evitar el jaque al Rey blanco.
    public Vector<Pieza> vectorPiezasNegasDefensoras; // Que pueden evitar el jaque al Rey negro.
    public Vector<Pieza> vectorPiezasNegrasAtacantes; // Que hacen jaque al Rey blanco.
    public Vector<Pieza> vectorPiezasBlancasAtacantes; // Que hacen jaque al Rey negro.
    private Random random;

    MetodosGenerales MG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MG = new MetodosGenerales(this);

        vectorPiezasBlancas = new Vector<>();
        vectorPiezasBlancasDefensoras = new Vector<>();
        vectorPiezasBlancasAtacantes = new Vector<>();

        vectorPiezasNegras = new Vector<>();
        vectorPiezasNegasDefensoras = new Vector<>();
        vectorPiezasNegrasAtacantes = new Vector<>();

        inicializaJugada(contadorMovimientos);

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

    @Override
    protected int getLayoutResourceId() {
        return R.layout.tablero;
    }

    private Validador validadorPeon = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esPeon = (MG.getTipoPieza(colOrigen, filaOrigen) == PEON);
            boolean esBlanco = (MG.getColorPieza(colOrigen, filaOrigen) == BLANCO);
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

    private Validador validadorGenerico = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean movimientoValidoPiezas = false;
            if (MG.hayPieza(colOrigen, filaOrigen) && casillaDisponible(colOrigen, filaOrigen, colDestino, filaDestino)) {

                switch (MG.getTipoPieza(colOrigen, filaOrigen)) {
                    case PEON:
                        movimientoValidoPiezas = validadorPeon.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case CABALLO:
                        movimientoValidoPiezas = validadorCaballo.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case ALFIL:
                        movimientoValidoPiezas = validadorAlfil.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case TORRE:
                        movimientoValidoPiezas = validadorTorre.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case DAMA:
                        movimientoValidoPiezas = validadorDama.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case REY:
                        movimientoValidoPiezas = validadorRey.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                }
            }
            return (movimientoValidoPiezas);
        }
    };

    private Validador validador = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean validador = false;
            Pieza pieza = MG.getPieza(colOrigen, filaOrigen);

            if (pieza != null) {
                boolean movimientoValidoPieza = validadorGenerico.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                Log.d("Ajedrez ", "***movimientoValidoPieza=" + movimientoValidoPieza);
                Pieza piezaDestino = MG.getPieza(colDestino, filaDestino);

                if (piezaDestino != null && piezaDestino.getColor() == NEGRO) {
                    vectorPiezasNegras.remove(piezaDestino);
                } else if (piezaDestino != null && piezaDestino.getColor() == BLANCO) {
                    vectorPiezasBlancas.remove(piezaDestino);
                }

                pieza.setCoordenada(colDestino, filaDestino);
                boolean reyEnJaque = reyEnJaque();
                Log.d("Ajedrez", "***reyEnJaque=" + reyEnJaque);
                pieza.setCoordenada(colOrigen, filaOrigen);
                if (piezaDestino != null && piezaDestino.getColor() == NEGRO) {
                    if (!vectorPiezasNegras.contains(piezaDestino))
                        vectorPiezasNegras.add(piezaDestino);
                }
                if (piezaDestino != null && piezaDestino.getColor() == BLANCO) {
                    if (!vectorPiezasBlancas.contains(piezaDestino))
                        vectorPiezasBlancas.add(piezaDestino);
                }

                //actualiza de nuevo el vector en MG para el metodo getPieza();
                ActualizarVectoresEnMetodosGenerales();

                validador = movimientoValidoPieza;// && !reyEnJaque;
                // pieza.setCoordenada(colOrigen, filaOrigen);
            }
            return (validador);
        }
    };

    private boolean reyEnJaque() {
        //vectorPiezasBlancasAtacantes.removeAllElements();
        //vectorPiezasNegrasAtacantes.removeAllElements();

        Pieza reyBlanco = null;
        for (int i = 0; (i < vectorPiezasBlancas.size() && reyBlanco == null); i++) {
            Pieza pieza = vectorPiezasBlancas.get(i);
            if (pieza.getTipo() == REY && pieza.getColor() == BLANCO)
                reyBlanco = vectorPiezasBlancas.get(i);
        }
        Pieza reyNegro = null;
        for (int i = 0; (i < vectorPiezasNegras.size() && reyNegro == null); i++) {
            Pieza pieza = vectorPiezasNegras.get(i);
            if (pieza.getTipo() == REY && pieza.getColor() == NEGRO)
                reyNegro = vectorPiezasNegras.get(i);
        }

        //ciclo para recorrer las pieza y determinar las atacantes al rey blanco
        if (reyBlanco != null) {  // SI EN ESTA JUGADA EXISTE EL REY BLANCO
            for (int i = 0; i < vectorPiezasNegras.size(); i++) {
                Pieza piezaNegra = vectorPiezasNegras.get(i);
                Log.d("Ajedrez", "piezaNegra.getTipo()=" + piezaNegra.getTipo());
                switch (piezaNegra.getTipo()) {
                    case PEON:
                        if (validadorPeon.movimientoValido(piezaNegra.getColumna(), piezaNegra.getFila(),
                                reyBlanco.getColumna(), reyBlanco.getFila())) {
                            vectorPiezasNegrasAtacantes.add(piezaNegra);
                        }
                        break;

                    case CABALLO:
                        if (validadorCaballo.movimientoValido(piezaNegra.getColumna(), piezaNegra.getFila(),
                                reyBlanco.getColumna(), reyBlanco.getFila())) {
                            vectorPiezasNegrasAtacantes.add(piezaNegra);
                        }
                        break;

                    case ALFIL:
                        if (validadorAlfil.movimientoValido(piezaNegra.getColumna(), piezaNegra.getFila(),
                                reyBlanco.getColumna(), reyBlanco.getFila())) {
                            vectorPiezasNegrasAtacantes.add(piezaNegra);
                        }
                        break;

                    case TORRE:
                        if (validadorTorre.movimientoValido(piezaNegra.getColumna(), piezaNegra.getFila(),
                                reyBlanco.getColumna(), reyBlanco.getFila())) {
                            vectorPiezasNegrasAtacantes.add(piezaNegra);
                        }
                        break;

                    case DAMA:
                        if (validadorDama.movimientoValido(piezaNegra.getColumna(), piezaNegra.getFila(),
                                reyBlanco.getColumna(), reyBlanco.getFila())) {
                            vectorPiezasNegrasAtacantes.add(piezaNegra);
                        }
                        break;

                    case REY:
                        if (validadorRey.movimientoValido(piezaNegra.getColumna(), piezaNegra.getFila(),
                                reyBlanco.getColumna(), reyBlanco.getFila())) {
                            vectorPiezasNegrasAtacantes.add(piezaNegra);
                        }
                        break;
                }
            }
        }
        //ciclo para recorrer las pieza y determinar las atacantes al rey negro
        if (reyNegro != null) {    // SI EN ESTA JUGADA EXISTE EL REY NEGRO
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
        }
        return (vectorPiezasNegrasAtacantes.size() > 0 || vectorPiezasBlancasAtacantes.size() > 0);
    }

    private boolean reyBlancoEnJaqueMate() {
        vectorPiezasBlancasDefensoras.removeAllElements();
        for (int p = 0; p < vectorPiezasBlancas.size(); p++) {
            Pieza pieza = vectorPiezasBlancas.get(p);
            for (int c = 0; c < 8; c++) {
                for (int f = 0; f < 8; f++) {
                    if (validador.movimientoValido(pieza.getColumna(), pieza.getFila(), c, f)) {
                        vectorPiezasBlancasDefensoras.add(pieza);
                    }
                }
            }
        }
        return (vectorPiezasBlancasDefensoras.size() == 0);
    }

    private boolean reyNegroEnJaqueMate() {
        vectorPiezasNegasDefensoras.removeAllElements();
        for (int p = 0; p < vectorPiezasNegras.size(); p++) {
            Pieza pieza = vectorPiezasNegras.get(p);
            for (int c = 0; c < 8; c++) {
                for (int f = 0; f < 8; f++) {
                    if (validador.movimientoValido(pieza.getColumna(), pieza.getFila(), c, f)) {
                        vectorPiezasNegasDefensoras.add(pieza);
                    }
                }
            }
        }
        return (vectorPiezasNegasDefensoras.size() == 0);
    }

    public boolean capturaPieza(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        Pieza piezaOrigen = MG.getPieza(colOrigen, filaOrigen);
        Pieza piezaDestino = MG.getPieza(colDestino, filaDestino);
        return (piezaOrigen != null && piezaDestino != null && piezaOrigen.getColor() != piezaDestino.getColor());
    }

    public boolean casillaDisponible(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        return (!MG.hayPieza(colDestino, filaDestino) || capturaPieza(colOrigen, filaOrigen, colDestino, filaDestino));
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

        //se actualiza las piezas en el vector
        ActualizarVectoresEnMetodosGenerales();

    }

    private void colocaPiezas() {

        ActualizarVectoresEnMetodosGenerales();

        for (Pieza pieza : vectorPiezasBlancas) {
            MG.colocaPieza(pieza);
        }
        for (Pieza pieza : vectorPiezasNegras) {
            MG.colocaPieza(pieza);
        }
    }

    private void ActualizarVectoresEnMetodosGenerales() {
        Vector<Pieza> vectorPiezas = new Vector<>();
        vectorPiezas.addAll(vectorPiezasBlancas);
        vectorPiezas.addAll(vectorPiezasNegras);
        MG.setVectorPiezas(vectorPiezas);
    }

    private void inicializaJugada(int jugada) {
        random = new Random(System.currentTimeMillis());
        retiraPiezas();
        Log.d("Ajedrez", "jugada=" + jugada);
        switch (jugada) {
            case 0:
                inicializaJugada1();
                break;
        }
    }

    private void inicializaJugada1() {
        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        int variante = random.nextInt(2);
        switch (variante) {
            case 0:

                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "E4"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "G5"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "H3"));
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "H4"));
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "E8"));

                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "C3"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "C7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "E5"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "E6"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "F8"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "F7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "G6"));
                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "G7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "H7"));
                break;
            case 1:
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "C4"));
                vectorPiezasBlancas.add(new Pieza(ALFIL, BLANCO, "E5"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "A6"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "A8"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "A7"));
                break;
            case 2:

                break;
            case 3:

                break;
        }
        colocaPiezas();
    }

    @Override
    protected void onFinalCuentaAtras() {
        avatar.habla(R.raw.mover_rey_en_jaque, new VistaAvatar.OnAvatarHabla() {
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
        boolean movimientoPiezaNegra = (MG.getColorPieza(colOrigen, filaOrigen) == NEGRO);
        boolean movimientoValido = validador.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
        boolean capturaPieza = (capturaPieza(colOrigen, filaOrigen, colDestino, filaDestino));
        boolean movimientoCorrecto = (movimientoPiezaNegra || movimientoPiezaBlanca) && movimientoValido;

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

            colocaPiezas();


            contadorMovimientos++;
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.ARQUEAR);
            if (contadorMovimientos % 2 != 0) {
                avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
                avatar.habla(R.raw.ok_intenta_otra_vez, new VistaAvatar.OnAvatarHabla() {
                    @Override
                    public void onTerminaHabla() {
                        avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                        empiezaCuentaAtras();
                    }
                });
                //inicializaJugada(contadorMovimientos);
            } else {
                avatar.lanzaAnimacion(VistaAvatar.Animacion.EJERCICIO_SUPERADO);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.EJERCICIO_SUPERADO);
                avatar.habla(R.raw.ok_superado, new VistaAvatar.OnAvatarHabla() {
                    @Override
                    public void onTerminaHabla() {
                        finish();
                    }
                });
            }

            for (int i = 0; i < vectorPiezasBlancasAtacantes.size(); i++) {
                Toast.makeText(this, "Jaque de "
                        + vectorPiezasBlancasAtacantes.get(i).getTipo().toString().toLowerCase()
                        + " " + vectorPiezasBlancasAtacantes.get(i).getColor().toString().toLowerCase()
                        + " al rey negro", Toast.LENGTH_SHORT).show();
            }
            for (int i = 0; i < vectorPiezasNegrasAtacantes.size(); i++) {
                Toast.makeText(this, "Jaque de "
                        + vectorPiezasNegrasAtacantes.get(i).getTipo().toString().toLowerCase()
                        + " " + vectorPiezasNegrasAtacantes.get(i).getColor().toString().toLowerCase()
                        + " al rey blanco ", Toast.LENGTH_SHORT).show();
            }

            Pieza pieza = MG.getPieza(colOrigen, filaOrigen);
            if (!reyBlancoEnJaqueMate()) {
                Pieza piezaDefensora = vectorPiezasBlancasDefensoras.get(0);
                resaltarCasilla(colDestino, filaDestino, Movimiento.INCORRECTO);
                resaltarCasilla(piezaDefensora.getColumna(), piezaDefensora.getFila(), Movimiento.ORIGEN);
                resaltarCasilla(piezaDefensora.getColumna(), piezaDefensora.getFila(), validador);
            }
            if (!reyNegroEnJaqueMate()) {
                Pieza piezaDefensora = vectorPiezasNegasDefensoras.get(0);
                resaltarCasilla(colDestino, filaDestino, Movimiento.INCORRECTO);
                resaltarCasilla(piezaDefensora.getColumna(), piezaDefensora.getFila(), Movimiento.ORIGEN);
                resaltarCasilla(piezaDefensora.getColumna(), piezaDefensora.getFila(), validador);
            }

        } else {
            avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_INCORRECTO);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);
            avatar.habla(R.raw.mover_rey_en_jaque_mal, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                    empiezaCuentaAtras();
                }
            });
            for (int i = 0; i < vectorPiezasBlancasAtacantes.size(); i++) {
                Toast.makeText(this, "Jaque de "
                        + vectorPiezasBlancasAtacantes.get(i).getTipo().toString().toLowerCase()
                        + " al rey negro", Toast.LENGTH_SHORT).show();
            }
            for (int i = 0; i < vectorPiezasNegrasAtacantes.size(); i++) {
                Toast.makeText(this, "Jaque de "
                        + vectorPiezasNegrasAtacantes.get(i).getTipo().toString().toLowerCase()
                        + " al rey blanco ", Toast.LENGTH_SHORT).show();
            }

            Pieza pieza = MG.getPieza(colOrigen, filaOrigen);
            if (!reyBlancoEnJaqueMate()) {
                Pieza piezaDefensora = vectorPiezasBlancasDefensoras.get(0);
                resaltarCasilla(colDestino, filaDestino, Movimiento.INCORRECTO);
                resaltarCasilla(piezaDefensora.getColumna(), piezaDefensora.getFila(), Movimiento.ORIGEN);
                resaltarCasilla(piezaDefensora.getColumna(), piezaDefensora.getFila(), validador);
            }
            if (!reyNegroEnJaqueMate()) {
                Pieza piezaDefensora = vectorPiezasNegasDefensoras.get(0);
                resaltarCasilla(colDestino, filaDestino, Movimiento.INCORRECTO);
                resaltarCasilla(piezaDefensora.getColumna(), piezaDefensora.getFila(), Movimiento.ORIGEN);
                resaltarCasilla(piezaDefensora.getColumna(), piezaDefensora.getFila(), validador);
            }
        }
    }
}