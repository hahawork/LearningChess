package com.uni.learningchess;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    public Vector<Pieza> vectorPiezasBlancasAtacantes; // Que hacen jaque al Rey negro.
    private Random random;

    MetodosGenerales mg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mg = new MetodosGenerales(this);

        vectorPiezasBlancas = new Vector<>();
        vectorPiezasBlancasAtacantes = new Vector<>();

        vectorPiezasNegras = new Vector<>();

        inicializaJugada(contadorMovimientos);

        avatar = getAvatar();
//        avatar.habla(R.raw.mover_rey_en_jaque, new VistaAvatar.OnAvatarHabla() {
        avatar.habla(R.raw.presentacion_ahogado, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                empiezaCuentaAtras();
            }
        });

        TextView tvNotas= findViewById(R.id.tvTituloEjerciciosPracticas);
        tvNotas.setText("Nota: Esto es con el fin de practicar y quedar claro que es el ahogado, pero te recomendamos utilizar este recurso en una posicion donde te ecuentres en desventaja.");
        tvNotas.setTextSize(12);
        tvNotas.setVisibility(View.VISIBLE);
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

                pieza.setCoordenada(colDestino, filaDestino);
                boolean reyEnJaque = reyEnJaque();
                Log.d("Ajedrez", "***reyEnJaque=" + reyEnJaque);
                validador = movimientoValidoBlancas;// && reyEnJaque;
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
        int variante = random.nextInt(2);
        switch (variante) {
            case 0:

                vectorPiezasBlancas.add(new Pieza(ALFIL, BLANCO, "E5"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "A5", true, "A6"));
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "C4"));

                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "A7"));
                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "A8"));
                break;
            case 1:
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "D5"));
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "E7", true, "C7"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "A8"));
                break;
        }
        colocaPiezas();
    }
    private void inicializaJugada2() {
        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        int variante = random.nextInt(2);
        switch (variante) {
            case 0:

                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "D1"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "A4", true, "A5"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "B7"));
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "D1"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "F6"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "G5"));

                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "F7"));
                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "E6"));
                break;
            case 1:
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "D7"));
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "C6", true, "D6"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "D8"));
                break;
        }
        colocaPiezas();
    }
    private void inicializaJugada3() {
        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        int variante = random.nextInt(2);
        switch (variante) {
            case 0:

                vectorPiezasBlancas.add(new Pieza(CABALLO, BLANCO, "A6"));
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "C6", true, "B6"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "A8"));
                break;
            case 1:
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "D4",true,"C4"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "F4"));
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "B1"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "A3"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "F5"));
                break;
        }
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
        avatar.habla(R.raw.presentacion_ahogado, new VistaAvatar.OnAvatarHabla() {
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
        boolean esPiezaCorrectaJaqueMate = mg.getPieza(colOrigen, filaOrigen).isMoverDarJaqueMate();
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
}