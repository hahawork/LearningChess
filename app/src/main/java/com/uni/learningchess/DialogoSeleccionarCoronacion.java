package com.uni.learningchess;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogoSeleccionarCoronacion {

    public void showDialog(Activity activity, final int colDestino, final int filaDestino) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.seleccionar_coronacion);
        ImageView ivCaballo, ivAlfil, ivDama, ivTorre;
        ivCaballo = dialog.findViewById(R.id.ivCaballo_dsc);
        ivAlfil = dialog.findViewById(R.id.ivAlfil_dsc);
        ivDama = dialog.findViewById(R.id.ivDama_dsc);
        ivTorre = dialog.findViewById(R.id.ivTorre_dsc);

        final ObtenerPiezaSeleccionada ops = (ObtenerPiezaSeleccionada) activity;

        ivCaballo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pieza pieza = new Pieza(Pieza.Tipo.CABALLO, Pieza.Color.BLANCO, "");
                pieza.setCoordenada(colDestino, filaDestino);
                ops.PiezaCoronada(pieza);
                dialog.dismiss();
            }
        });
        ivAlfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pieza pieza = new Pieza(Pieza.Tipo.ALFIL, Pieza.Color.BLANCO, "");
                pieza.setCoordenada(colDestino, filaDestino);
                ops.PiezaCoronada(pieza);
                dialog.dismiss();
            }
        });
        ivDama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pieza pieza = new Pieza(Pieza.Tipo.DAMA, Pieza.Color.BLANCO, "");
                pieza.setCoordenada(colDestino, filaDestino);
                ops.PiezaCoronada(pieza);
                dialog.dismiss();
            }
        });
        ivTorre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pieza pieza = new Pieza(Pieza.Tipo.TORRE, Pieza.Color.BLANCO, "");
                pieza.setCoordenada(colDestino, filaDestino);
                ops.PiezaCoronada(pieza);
                dialog.cancel();
            }
        });

        dialog.show();

    }

    public interface ObtenerPiezaSeleccionada {
        public void PiezaCoronada(Pieza pieza);
    }
}
