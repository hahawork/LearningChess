package com.uni.learningchess;

import android.os.Bundle;

public class MoverAlfilActivity extends MoverPiezaActivity {


    public Validador validadorAlfil = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean diferenteCasilla = (colOrigen != colDestino) || (filaOrigen != filaDestino);
            boolean mismaDiagonal = (Math.abs(filaOrigen - filaDestino) == Math.abs(colOrigen - colDestino));
            return (diferenteCasilla && mismaDiagonal);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        inicializa(validadorAlfil, R.drawable.alfil_blanco, R.raw.mover_dama_presentacion); //Todo: reemplazar mover_dama_presentacion
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.tablero;
    }

}
