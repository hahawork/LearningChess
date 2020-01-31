package com.uni.learningchess;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.Random;
import java.util.Vector;

import static com.uni.learningchess.Pieza.Color.BLANCO;
import static com.uni.learningchess.Pieza.Tipo.REY;
import static com.uni.learningchess.Pieza.Tipo.TORRE;

public class MovimientosEnroque extends EjercicioBaseActivity {

    private VistaAvatar avatar;
    private boolean movioRey = false;
    private int TipoEnroqueCorto_largo = 0;
    private Random random;
    private Vector<Pieza> vectorPiezasBlancas;
    private int contadorMovimientos = 0;

    private Validador validadorTorre = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esTorre = (getTipoPieza(colOrigen, filaOrigen) == TORRE);
            boolean diferenteCasilla = (colOrigen != colDestino) || (0 == filaDestino);
            boolean mismaFila = (filaOrigen == filaDestino);
            boolean saltaPiezas = saltaPiezas(colOrigen, filaOrigen, colDestino, filaDestino);

            Log.w("Torre info", String.format("Es torre: %s; " +
                    "Diferentcasilla: %s; " +
                    "Misma fila: %s; " +
                    "SaltaPiezas: %s; " +
                    "movioRey: %s.", esTorre, diferenteCasilla, mismaFila, saltaPiezas, movioRey));

            return (esTorre && diferenteCasilla && mismaFila && saltaPiezas && movioRey);
        }
    };
    private Validador validadorRey = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esRey = (getTipoPieza(colOrigen, filaOrigen) == REY);
            boolean diferenteCasilla = (colOrigen != colDestino) || (0 == filaDestino);
            boolean distanciaDos = (Math.abs(filaOrigen - filaDestino) == 0) && (Math.abs(colOrigen - colDestino) == 2);
            boolean columnaAmoverse = colDestino == TipoEnroqueCorto_largo; // esto por que puiede tomar dos direcciones
            return (esRey && diferenteCasilla && distanciaDos && !movioRey && columnaAmoverse);
        }
    };
    private Validador validadorGenerico = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean movimientoValidoBlancas = false;
            if (hayPieza(colOrigen, filaOrigen) && casillaDisponible(colOrigen, filaOrigen, colDestino, filaDestino)) {
                switch (getTipoPieza(colOrigen, filaOrigen)) {
                    case TORRE:
                        movimientoValidoBlancas = validadorTorre.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        break;
                    case REY:
                        movimientoValidoBlancas = validadorRey.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                        if (movimientoValidoBlancas) movioRey = true;
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
            Pieza pieza = getPieza(colOrigen, filaOrigen);

            if (pieza != null) {
                boolean movimientoValidoBlancas = validadorGenerico.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                Log.d("Ajedrez", "***movimientoValidoBlancas=" + movimientoValidoBlancas);
                Pieza piezaDestino = getPieza(colDestino, filaDestino);

                pieza.setCoordenada(colDestino, filaDestino);
                boolean reyEnJaque = false;
                Log.d("Ajedrez", "***reyEnJaque=" + reyEnJaque);
                validador = movimientoValidoBlancas && !reyEnJaque;
                pieza.setCoordenada(colOrigen, filaOrigen);
            }
            return (validador);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vectorPiezasBlancas = new Vector<>();

        getExtra();

        avatar = getAvatar();
        avatar.habla(R.raw.enroque, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                empiezaCuentaAtras();
            }
        });
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int tipoEnroque = bundle.getInt("tipoEnroque", 0);
            inicializaJugada(tipoEnroque);
        } else {
            inicializaJugada(0);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.tablero;
    }

    @Override
    protected boolean onMovimiento(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        Pieza piezaDestino = getPieza(colDestino, filaDestino);
        boolean movimientoPiezaBlanca = (getColorPieza(colOrigen, filaOrigen) == BLANCO);
        boolean movimientoValido = validador.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
        boolean movimientoCorrecto = movimientoPiezaBlanca && movimientoValido;
        return movimientoCorrecto;
    }

    @Override
    protected void onMovimiento(boolean movimientoValido, int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        Log.d("Ajedrez guia", "onMovimiento movimientoValido=" + movimientoValido + " colOrigen=" + colOrigen + " filaOrigen=" + filaOrigen + " colDestino=" + colDestino + " filaDestino=" + filaDestino);
        avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
        if (movimientoValido) {
            contadorMovimientos++;
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.ARQUEAR);
            if (contadorMovimientos < 2) {
                avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
                avatar.habla(R.raw.ok_has_acertado, new VistaAvatar.OnAvatarHabla() {
                    @Override
                    public void onTerminaHabla() {
                        avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                        empiezaCuentaAtras();
                    }
                });
                Pieza piezaBlanca1 = new Pieza(REY, BLANCO, "G1");
                Pieza piezaBlanca2 = new Pieza(REY, BLANCO, "C1");
                vectorPiezasBlancas.add(piezaBlanca1);
                vectorPiezasBlancas.add(piezaBlanca2);
                //inicializaJugada(contadorMovimientos);
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
        }
    }

    private void inicializaJugada(int tipoEnroque) {
        random = new Random(System.currentTimeMillis());
        vectorPiezasBlancas.removeAllElements();

        switch (tipoEnroque) {
            case 0:
                Pieza piezaBlanca1 = new Pieza(REY, BLANCO, "E1");
                vectorPiezasBlancas.add(piezaBlanca1);
                Pieza piezaBlanca2 = new Pieza(TORRE, BLANCO, "A1");
                vectorPiezasBlancas.add(piezaBlanca2);
                TipoEnroqueCorto_largo = 2; // columna a la que se debe mover al rey, empezando de 0 (cero)
                break;

            case 1:
                piezaBlanca1 = new Pieza(REY, BLANCO, "E1");
                vectorPiezasBlancas.add(piezaBlanca1);
                piezaBlanca2 = new Pieza(TORRE, BLANCO, "H1");
                vectorPiezasBlancas.add(piezaBlanca2);
                TipoEnroqueCorto_largo = 6; // columna a la que se debe mover al rey, empezando de 0 (cero)
                break;

        }
        colocaPiezas();
    }

    private void colocaPiezas() {
        for (Pieza pieza : vectorPiezasBlancas) {
            colocaPieza(pieza);
        }
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

    public Pieza.Color getColorPieza(int columna, int fila) {
        Pieza.Color color = null;
        Pieza pieza = getPieza(columna, fila);
        if (pieza != null) color = pieza.getColor();
        return color;
    }

    public Pieza getPieza(int columna, int fila) {
        Pieza pieza = null;
        Vector<Pieza> vectorPiezas = new Vector<>();
        vectorPiezas.addAll(vectorPiezasBlancas);
        for (int i = 0; i < vectorPiezas.size() && pieza == null; i++) {
            if (vectorPiezas.get(i).getColumna() == columna
                    && vectorPiezas.get(i).getFila() == fila) {
                pieza = vectorPiezas.get(i);
            }
        }
        return pieza;
    }

    public Pieza.Tipo getTipoPieza(int columna, int fila) {
        Pieza.Tipo tipo = null;
        Pieza pieza = getPieza(columna, fila);
        if (pieza != null) tipo = pieza.getTipo();
        return tipo;
    }

    public boolean saltaPiezas(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        boolean saltaPiezas = false;
        int distanciaCol = Math.abs(colOrigen - colDestino);
        int distanciaFila = Math.abs(filaOrigen - filaDestino);
        int distancia = Math.max(distanciaCol, distanciaFila);
        int col = colOrigen;
        int fila = filaOrigen;
        for (int i = 1; i < distancia && !saltaPiezas; i++) {
            if (col < colDestino) col += 1;
            if (col > colDestino) col -= 1;
            if (fila < filaDestino) fila += 1;
            if (fila > filaDestino) fila -= 1;
            saltaPiezas = hayPieza(col, fila);
        }
        return saltaPiezas;
    }

    public boolean hayPieza(int columna, int fila) {
        Pieza pieza = getPieza(columna, fila);
        return (pieza != null);
    }

    public boolean casillaDisponible(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        return (!hayPieza(colDestino, filaDestino) || capturaPieza(colOrigen, filaOrigen, colDestino, filaDestino));
    }

    public boolean capturaPieza(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        Pieza piezaOrigen = getPieza(colOrigen, filaOrigen);
        Pieza piezaDestino = getPieza(colDestino, filaDestino);
        return (piezaOrigen != null && piezaDestino != null && piezaOrigen.getColor() != piezaDestino.getColor());
    }
}