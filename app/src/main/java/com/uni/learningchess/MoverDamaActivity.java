package com.uni.learningchess;

import android.os.Bundle;

public class MoverDamaActivity extends MoverPiezaActivity {
    public Validador validadorDama = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean mismaColumna = (colOrigen == colDestino);
            boolean mismaFila = (filaOrigen == filaDestino);
            boolean mismaDiagonal = (Math.abs(filaOrigen - filaDestino) == Math.abs(colOrigen - colDestino));
            return (diferenteCasilla && (mismaColumna || mismaFila || mismaDiagonal));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        inicializa(validadorDama, R.drawable.dama_blanca, R.raw.mover_pieza_presentacion);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.tablero;
    }
}