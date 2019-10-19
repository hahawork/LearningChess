package com.uni.learningchess;

import android.os.Bundle;

public class MoverPeonActivity extends MoverPiezaActivity {
    public Validador validadorPeon = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean avanzaUno =  (filaDestino==filaOrigen+1) && (colDestino==colOrigen);
            return (diferenteCasilla && avanzaUno);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        inicializa(validadorPeon, R.drawable.peon_blanco, R.raw.mover_dama_presentacion);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.tablero;
    }
}