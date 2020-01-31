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
import static com.uni.learningchess.Pieza.Tipo.PEON;
import static com.uni.learningchess.Pieza.Tipo.TORRE;

public class MovimientoPeonAlPaso extends EjercicioBaseActivity {

    private VistaAvatar avatar;
    private int contadorMovimientos = 0;
    private Vector<Pieza> vectorPiezasBlancas;
    private Vector<Pieza> vectorPiezasNegras;
    private Random random;
    private int numeroJugada = 0;
    private boolean practica1MovioPeonBlanco = false, practica2MovioPeonNegro = false;

    private Validador validadorPeonBlanco = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esPeon = (getTipoPieza(colOrigen, filaOrigen) == PEON);
            boolean esBlanco = (getColorPieza(colOrigen, filaOrigen) == BLANCO);
            boolean diferenteCasilla = (colOrigen == colDestino) && (filaOrigen != filaDestino);
            boolean avanzaDos = (esBlanco && (filaDestino == filaOrigen + 2)
                    && (colDestino == colOrigen));
            return (esPeon && diferenteCasilla && avanzaDos);
        }
    };

    private Validador validadorPeonNegro = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esPeon = (getTipoPieza(colOrigen, filaOrigen) == PEON);
            boolean esNegro = (getColorPieza(colOrigen, filaOrigen) == NEGRO);
            boolean diferenteCasilla = (colOrigen != colDestino) && (filaOrigen != filaDestino);
            boolean diferenteColumna = (colDestino != colOrigen);
            boolean casillaDisponible = casillaDisponible(colOrigen, filaOrigen, colDestino, filaDestino);
            boolean avanzaUno = ((esNegro && (filaDestino == filaOrigen - 1))
                    || (!esNegro && (filaDestino == filaOrigen - 1))
                    && (colDestino == colOrigen));
            return (practica1MovioPeonBlanco && esPeon && diferenteCasilla && diferenteColumna && avanzaUno && casillaDisponible);
        }
    };

    private Validador validadorPeonBlancoPractica2 = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esPeon = (getTipoPieza(colOrigen, filaOrigen) == PEON);
            boolean esBlanco = (getColorPieza(colOrigen, filaOrigen) == BLANCO);
            boolean diferenteCasilla = (colOrigen != colDestino) && (filaOrigen + 1 == filaDestino);
            boolean diferenteColumna = (colDestino != colOrigen);
            boolean casillaDisponible = casillaDisponible(colOrigen, filaOrigen, colDestino, filaDestino);
            boolean avanzaUno = ((esBlanco && (filaDestino == filaOrigen + 1))
                    && (colDestino != colOrigen));
            return (practica2MovioPeonNegro && esPeon && diferenteCasilla && diferenteColumna && avanzaUno && casillaDisponible);
        }
    };

    private Validador validadorPeonNegroPractica2 = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esPeon = (getTipoPieza(colOrigen, filaOrigen) == PEON);
            boolean esNegro = (getColorPieza(colOrigen, filaOrigen) == NEGRO);
            boolean diferenteCasilla = (colOrigen == colDestino) && (filaOrigen != filaDestino);
            boolean avanzaDos = (esNegro && (filaDestino == filaOrigen - 2)
                    && (colDestino == colOrigen));
            return (esPeon && diferenteCasilla && avanzaDos);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vectorPiezasBlancas = new Vector<>();
        vectorPiezasNegras = new Vector<>();

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

    public boolean capturaPieza(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        Pieza piezaOrigen = getPieza(colOrigen, filaOrigen);
        Pieza piezaDestino = getPieza(colDestino, filaDestino + 1);
        return (piezaOrigen != null && piezaDestino != null && piezaOrigen.getColor() != piezaDestino.getColor());
    }

    public boolean casillaDisponible(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        return (!hayPieza(colDestino, filaDestino) || capturaPieza(colOrigen, filaOrigen, colDestino, filaDestino));
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

    public Pieza getPieza(int columna, int fila) {
        Pieza pieza = null;
        Vector<Pieza> vectorPiezas = new Vector<>();
        vectorPiezas.addAll(vectorPiezasBlancas);
        vectorPiezas.addAll(vectorPiezasNegras);
        for (int i = 0; i < vectorPiezas.size() && pieza == null; i++) {
            if (vectorPiezas.get(i).getColumna() == columna
                    && vectorPiezas.get(i).getFila() == fila) {
                pieza = vectorPiezas.get(i);
            }
        }
        return pieza;
    }

    public boolean hayPieza(int columna, int fila) {
        Pieza pieza = getPieza(columna, fila);
        return (pieza != null);
    }

    public void eliminaPieza(Pieza pieza) {
        vectorPiezasBlancas.removeElement(pieza);
        vectorPiezasNegras.removeElement(pieza);
    }

    public Pieza.Tipo getTipoPieza(int columna, int fila) {
        Pieza.Tipo tipo = null;
        Pieza pieza = getPieza(columna, fila);
        if (pieza != null) tipo = pieza.getTipo();
        return tipo;
    }

    public Pieza.Color getColorPieza(int columna, int fila) {
        Pieza.Color color = null;
        Pieza pieza = getPieza(columna, fila);
        if (pieza != null) color = pieza.getColor();
        return color;
    }

    public void colocaPieza(Pieza pieza) {
        int idImageView = getResources().getIdentifier(pieza.getCoordenada(), "id", getPackageName());
        ImageView imageView = findViewById(idImageView);
        int idDrawablePieza = getDrawablePieza(pieza);
        imageView.setImageResource(idDrawablePieza);
        Log.d("Ajedrez", "tipo=" + pieza.getTipo() + " color=" + pieza.getColor() + " coordenada=" + pieza.getCoordenada() + " getResourceName(idImageView)=" + getResources().getResourceName(idImageView));
    }

    public int getDrawablePieza(Pieza pieza) {
        int idDrawable = 0;
        if (pieza.getColor() == BLANCO) {
            switch (pieza.getTipo()) {
                case PEON:
                    idDrawable = R.drawable.peon_blanco;
                    break;
                case CABALLO:
                    idDrawable = R.drawable.caballo_blanco;
                    break;
                case ALFIL:
                    idDrawable = R.drawable.alfil_blanco;
                    break;
                case TORRE:
                    idDrawable = R.drawable.torre_blanca;
                    break;
                case DAMA:
                    idDrawable = R.drawable.dama_blanca;
                    break;
                case REY:
                    idDrawable = R.drawable.rey_blanco;
                    break;
            }
        } else {
            switch (pieza.getTipo()) {
                case PEON:
                    idDrawable = R.drawable.peon_negro;
                    break;
                case CABALLO:
                    idDrawable = R.drawable.caballo_negro;
                    break;
                case ALFIL:
                    idDrawable = R.drawable.alfil_negro;
                    break;
                case TORRE:
                    idDrawable = R.drawable.torre_negra;
                    break;
                case DAMA:
                    idDrawable = R.drawable.dama_negra;
                    break;
                case REY:
                    idDrawable = R.drawable.rey_negro;
                    break;
            }
        }
        return idDrawable;
    }

    private void colocaPiezas() {
        for (Pieza pieza : vectorPiezasBlancas) {
            colocaPieza(pieza);
        }
        for (Pieza pieza : vectorPiezasNegras) {
            colocaPieza(pieza);
        }
    }

    private void inicializaJugada(int jugada) {
        random = new Random(System.currentTimeMillis());
        retiraPiezas();
        Log.d("Ajedrez", "jugada=" + jugada);
        switch (jugada) {
            case 0:
                inicializaJugadaPrimerPractica();
                break;
            case 2:
                inicializaJugadaSegundaPractica();
                break;

        }
    }

    private void inicializaJugadaPrimerPractica() {
        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        int variante = random.nextInt(3);
        numeroJugada = 1;
        switch (variante) {
            case 0:
                Pieza piezaBlanca1 = new Pieza(PEON, BLANCO, "E2");
                vectorPiezasBlancas.add(piezaBlanca1);
                Pieza piezaNegra1 = new Pieza(PEON, NEGRO, "D4");
                vectorPiezasNegras.add(piezaNegra1);
                Pieza piezaNegra2 = new Pieza(PEON, BLANCO, "C2");
                vectorPiezasNegras.add(piezaNegra2);
                break;
            case 1:
                piezaBlanca1 = new Pieza(PEON, BLANCO, "F2");
                vectorPiezasBlancas.add(piezaBlanca1);
                piezaNegra1 = new Pieza(PEON, BLANCO, "D2");
                vectorPiezasNegras.add(piezaNegra1);
                piezaNegra2 = new Pieza(PEON, NEGRO, "E4");
                vectorPiezasNegras.add(piezaNegra2);
                break;
            case 2:
                piezaBlanca1 = new Pieza(PEON, BLANCO, "H2");
                vectorPiezasBlancas.add(piezaBlanca1);
                Pieza piezaBlanca2 = new Pieza(PEON, BLANCO, "F2");
                vectorPiezasBlancas.add(piezaBlanca2);
                piezaNegra1 = new Pieza(PEON, NEGRO, "G4");
                vectorPiezasNegras.add(piezaNegra1);
                piezaNegra2 = new Pieza(TORRE, NEGRO, "A8");
                vectorPiezasNegras.add(piezaNegra2);
                Pieza piezaNegra3 = new Pieza(TORRE, NEGRO, "H8");
                vectorPiezasNegras.add(piezaNegra3);
                break;
        }
        colocaPiezas();
    }

    private void inicializaJugadaSegundaPractica() {
        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        int variante = random.nextInt(3);
        numeroJugada = 2;
        switch (variante) {
            case 0:
                Pieza piezaBlanca1 = new Pieza(PEON, BLANCO, "B5");
                vectorPiezasBlancas.add(piezaBlanca1);
                Pieza piezaNegra1 = new Pieza(PEON, NEGRO, "A7");
                vectorPiezasNegras.add(piezaNegra1);
                Pieza piezaNegra2 = new Pieza(PEON, NEGRO, "C7");
                vectorPiezasNegras.add(piezaNegra2);
                break;
            case 1:
                piezaBlanca1 = new Pieza(PEON, BLANCO, "E5");
                vectorPiezasBlancas.add(piezaBlanca1);
                piezaNegra1 = new Pieza(PEON, NEGRO, "D7");
                vectorPiezasNegras.add(piezaNegra1);
                piezaNegra2 = new Pieza(PEON, NEGRO, "F7");
                vectorPiezasNegras.add(piezaNegra2);
                break;
            case 2:
                piezaBlanca1 = new Pieza(PEON, BLANCO, "G5");
                vectorPiezasBlancas.add(piezaBlanca1);
                piezaNegra1 = new Pieza(PEON, NEGRO, "F7");
                vectorPiezasNegras.add(piezaNegra1);
                piezaNegra2 = new Pieza(PEON, NEGRO, "H7");
                vectorPiezasNegras.add(piezaNegra2);
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

        if (numeroJugada == 1) {

            if (getColorPieza(colOrigen, filaOrigen) == BLANCO) {

                boolean movimientoBlanca = validadorPeonBlanco.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                practica1MovioPeonBlanco = movimientoBlanca;
                return movimientoBlanca;
            } else {

                boolean movimientoNegra = validadorPeonNegro.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                //Pieza pieza = getPieza(colDestino, filaOrigen + 1);
                //boolean piezaBlancaAtacada = pieza != null && pieza.getColor() == BLANCO;
                return movimientoNegra;// && piezaBlancaAtacada;
            }
        } else if (numeroJugada == 2) {

            if (getColorPieza(colOrigen, filaOrigen) == BLANCO) {

                boolean movimientoBlanca = validadorPeonBlancoPractica2.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                return movimientoBlanca;
            } else {

                boolean movimientoNegra = validadorPeonNegroPractica2.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                practica2MovioPeonNegro= movimientoNegra;
                return movimientoNegra;
            }
        } else return false;
    }

    @Override
    protected void onMovimiento(boolean movimientoValido, int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        Log.d("Ajedrez", "onMovimiento movimientoValido=" + movimientoValido + " colOrigen=" + colOrigen + " filaOrigen=" + filaOrigen + " colDestino=" + colDestino + " filaDestino=" + filaDestino);
        avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
        if (movimientoValido) {
            contadorMovimientos++;
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.ARQUEAR);
            if (contadorMovimientos < 4) {  //2 practica se dos movimientos cada una
                avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
                if (contadorMovimientos % 2 == 0) {
                    avatar.habla(R.raw.ok_intenta_otra_vez, new VistaAvatar.OnAvatarHabla() {
                        @Override
                        public void onTerminaHabla() {
                            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                            empiezaCuentaAtras();
                        }
                    });
                    inicializaJugada(contadorMovimientos);
                }
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
            avatar.habla(R.raw.mover_rey_en_jaque_mal, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                    empiezaCuentaAtras();
                }
            });

        }
    }
}