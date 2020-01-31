package com.uni.learningchess;

import android.os.Bundle;

public class MoverTorreActivity extends MoverPiezaActivity {

    public Validador validadorTorre = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean mismaColumna = (colOrigen == colDestino);
            boolean mismaFila = (filaOrigen == filaDestino);
            return (diferenteCasilla && (mismaColumna || mismaFila));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        inicializa(validadorTorre, R.drawable.torre_blanca, R.raw.mover_pieza_presentacion); //Todo: reemplazar mover_dama_presentacion
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.tablero;
    }

}
