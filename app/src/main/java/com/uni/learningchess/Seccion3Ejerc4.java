package com.uni.learningchess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class Seccion3Ejerc4 extends EjercicioBaseActivity {

    ArrayList<Seccion3Ejerc3.casilla> Casillas = new ArrayList<>();
    Random fila, columna;
    String[] Columnas = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
    private VistaAvatar avatar;
    private int filaSeleccionada, columnaSeleccionada, aciertos = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout piezas = (LinearLayout) findViewById(R.id.piezas);
        piezas.setVisibility(View.VISIBLE);
        avatar = getAvatar();
        avatar.habla(R.raw.colocar_piezas_presentacion);

        fila = new Random();
        filaSeleccionada = fila.nextInt(8);//8 filas
        columna = new Random();
        columnaSeleccionada = columna.nextInt(8);//8 columnas
        TextView titulo = findViewById(R.id.textoSeccion3Ejerc4Titulo);
        titulo.setText(String.format(getResources().getString(R.string.seccion3Ejerc4Titulo), Columnas[columnaSeleccionada] + "" + (filaSeleccionada + 1)));

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_seccion3ejerc4;
    }

    @Override
    protected boolean onMovimiento(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        return false; //Una vez colocada no permitimos moverla
    }


    @Override
    protected boolean onColocar(char pieza, int colDestino, int filaDestino) {
        boolean salida;
        switch (pieza) {
            case 'P':
                salida = filaDestino == filaSeleccionada && colDestino == columnaSeleccionada;
                break;
            default:
                salida = false;
        }
        if (salida) {

            avatar.lanzaAnimacion(VistaAvatar.Animacion.EJERCICIO_SUPERADO);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.EJERCICIO_SUPERADO);
            avatar.habla(R.raw.ok_superado, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    startActivity(new Intent(Seccion3Ejerc4.this, Seccion3Ejerc5.class));
                    finish();
                }
            });

        } else {
            switch (pieza) {
                case 'P':
                    avatar.habla(R.raw.colocar_piezas_mal_peon);

                    resaltarCasilla(columnaSeleccionada, filaSeleccionada,
                            new Validador() {
                                @Override
                                public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
                                    return filaDestino == filaSeleccionada && colDestino == columnaSeleccionada;
                                }
                            });
                    break;
            }


            avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_INCORRECTO);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);

            // cambia aleatoria las nuevas cordenadas
            fila = new Random();
            filaSeleccionada = fila.nextInt(8);//8 filas
            columna = new Random();
            columnaSeleccionada = columna.nextInt(8);//8 columnas
            TextView titulo = findViewById(R.id.textoSeccion3Ejerc4Titulo);
            titulo.setText(String.format(getResources().getString(R.string.seccion3Ejerc4Titulo), Columnas[columnaSeleccionada] + "" + (filaSeleccionada + 1)));

        }
        return salida;
    }
}