package com.uni.learningchess;

import android.os.Bundle;

public class MoverReyActivity extends MoverPiezaActivity {

    public Validador validadorRey = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean distanciaUno =  (Math.abs(filaOrigen - filaDestino) <=1)
                                 && (Math.abs(colOrigen - colDestino)<=1);
            return (diferenteCasilla && distanciaUno);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        inicializa(validadorRey, R.drawable.rey_blanco, R.raw.mover_pieza_presentacion);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.tablero;
    }
}