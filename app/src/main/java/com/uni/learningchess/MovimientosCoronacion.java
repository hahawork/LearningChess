package com.uni.learningchess;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;
import java.util.Vector;

import static com.uni.learningchess.Pieza.Color.BLANCO;
import static com.uni.learningchess.Pieza.Tipo.PEON;

public class MovimientosCoronacion extends EjercicioBaseActivity implements DialogoSeleccionarCoronacion.ObtenerPiezaSeleccionada {

    private VistaAvatar avatar;
    private Random random;
    private Vector<Pieza> vectorPiezasBlancas;
    private int contadorMovimientos = 0;

    private Validador validadorPeonBlanco = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean esPeon = (getTipoPieza(colOrigen, filaOrigen) == PEON);
            boolean esBlanco = (getColorPieza(colOrigen, filaOrigen) == BLANCO);
            boolean ColumnaVal = (Math.abs(colDestino - colOrigen) == 1) || ((colDestino - colOrigen) == 0);
            //boolean diferenteCasilla = (colOrigen == colDestino) && (filaOrigen < filaDestino);
            boolean avanzaUno = (esBlanco && (filaDestino == filaOrigen + 1)
                    && ColumnaVal);
            return (esPeon && avanzaUno);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vectorPiezasBlancas = new Vector<>();

        inicializaJugada(contadorMovimientos);

        avatar = getAvatar();
        avatar.habla(R.raw.coronacion, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                empiezaCuentaAtras();
            }
        });
    }

    private void inicializaJugada(int jugada) {
        random = new Random(System.currentTimeMillis());
        retiraPiezas();
        Log.d("Ajedrez", "jugada=" + jugada);
        switch (jugada) {
            case 0:
                inicializaJugadaPrimerPractica();
                break;
            case 1:
                inicializaJugadaSegundaPractica();
                break;

        }
    }

    private void inicializaJugadaPrimerPractica() {
        vectorPiezasBlancas.removeAllElements();
        int variante = random.nextInt(3);
        switch (variante) {
            case 0:
                Pieza piezaBlanca1 = new Pieza(PEON, BLANCO, "E7");
                vectorPiezasBlancas.add(piezaBlanca1);

                break;
            case 1:
                piezaBlanca1 = new Pieza(PEON, BLANCO, "F7");
                vectorPiezasBlancas.add(piezaBlanca1);
                break;
            case 2:
                piezaBlanca1 = new Pieza(PEON, BLANCO, "H7");
                vectorPiezasBlancas.add(piezaBlanca1);
                break;
        }
        colocaPiezas();
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

    private void inicializaJugadaSegundaPractica() {
        vectorPiezasBlancas.removeAllElements();
        int variante = random.nextInt(3);
        switch (variante) {
            case 0:
                Pieza piezaBlanca1 = new Pieza(PEON, BLANCO, "D7");
                vectorPiezasBlancas.add(piezaBlanca1);
                break;
            case 1:
                piezaBlanca1 = new Pieza(PEON, BLANCO, "E7");
                vectorPiezasBlancas.add(piezaBlanca1);

                break;
            case 2:
                piezaBlanca1 = new Pieza(PEON, BLANCO, "G7");
                vectorPiezasBlancas.add(piezaBlanca1);

                break;
        }
        colocaPiezas();
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
        }
        return idDrawable;
    }

    private void colocaPiezas() {
        for (Pieza pieza : vectorPiezasBlancas) {
            colocaPieza(pieza);
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
        boolean movimientoValido = validadorPeonBlanco.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
        boolean movimientoCorrecto = movimientoPiezaBlanca && movimientoValido;
        return movimientoCorrecto;
    }

    @Override
    protected void onMovimiento(boolean movimientoValido, int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        Log.d("Ajedrez guia", "onMovimiento movimientoValido=" + movimientoValido + " colOrigen=" + colOrigen + " filaOrigen=" + filaOrigen + " colDestino=" + colDestino + " filaDestino=" + filaDestino);
        avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
        if (movimientoValido) {

            avatar.mueveCejas(VistaAvatar.MovimientoCejas.ARQUEAR);

            //escoger la pieza de coronacion
            mostrarPiezasCoronar(colDestino, filaDestino);

        } else {
            avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_INCORRECTO);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);
            avatar.habla(R.raw.coronacion, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                    empiezaCuentaAtras();
                }
            });
        }
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

    private void mostrarPiezasCoronar(int colDestino, int filaDestino) {
        DialogoSeleccionarCoronacion dsc = new DialogoSeleccionarCoronacion();
        dsc.showDialog(this, colDestino, filaDestino);
    }

    @Override
    public void PiezaCoronada(Pieza pieza) {
        retiraPiezas();
        colocaPieza(pieza);

        contadorMovimientos++;

        if (contadorMovimientos < 2) {
            avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
            avatar.habla(R.raw.ok_has_acertado, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                    empiezaCuentaAtras();
                    inicializaJugada(contadorMovimientos);
                }
            });

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
    }
}
