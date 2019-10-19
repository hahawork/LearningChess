package com.uni.learningchess;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Random;

import static com.uni.learningchess.Pieza.Color.BLANCO;


public class Seccion3Ejerc5 extends Seccion3BaseActivity {

    Random fila, columna;
    String[] Columnas = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
    Spinner spnColumnas, spnFilas;
    private VistaAvatar avatar;
    private int filaSeleccionada, columnaSeleccionada, aciertos = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout tablero = findViewById(R.id.tableroSeccio3);
        tablero.removeView(findViewById(R.id.ejercicio1));
        tablero.removeView(findViewById(R.id.ejercicio2));
        tablero.removeView(findViewById(R.id.ejercicio3));
        tablero.removeView(findViewById(R.id.ejercicio4));

        LinearLayout linearLayout = findViewById(R.id.ejercicio5);
        linearLayout.setVisibility(View.VISIBLE);

        avatar = getAvatar();
        avatar.habla(R.raw.colocar_piezas_presentacion);

        SelecionarUbicacionPieza();
    }

    private void SelecionarUbicacionPieza() {
        retiraPiezas();
        fila = new Random();
        filaSeleccionada = fila.nextInt(8);//8 filas
        columna = new Random();
        columnaSeleccionada = columna.nextInt(8);//8 columnas


        Pieza pieza = new Pieza(randomEnum(Pieza.Tipo.class), randomEnum(Pieza.Color.class), Columnas[columnaSeleccionada] + (filaSeleccionada + 1));
        colocaPieza(pieza);

        colocarColumna();
        colocarFila();
    }

    /**
     * Metodo para obtener de manera aleatoria un valor del enum recibido. Uso: randomEnum(Clase.Enum.class)
     * @param clazz El Enum.
     * @param <T> ??
     * @return El enum que agarro de modo aleatorio.
     */
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = new Random().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
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

    protected void retiraPiezas() {
        LinearLayout tabla = (LinearLayout) findViewById(R.id.tabla);
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

    private void Ejercicio() {
        String fil = spnFilas.getSelectedItem().toString();
        String col = spnColumnas.getSelectedItem().toString();

        // si ambos campos tiene texto
        if (fil.length() == 1 && col.length() == 1) {

            int NumFila = Integer.parseInt(fil);
            //verifica repuesta correcta
            if (NumFila == (filaSeleccionada + 1) && col.equalsIgnoreCase(Columnas[columnaSeleccionada])) {

                if (aciertos <= 2) {
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
                    avatar.mueveCejas(VistaAvatar.MovimientoCejas.ARQUEAR);
                    avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
                    avatar.habla(R.raw.ok_intenta_otra_vez, new VistaAvatar.OnAvatarHabla() {
                        @Override
                        public void onTerminaHabla() {
                            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                            empiezaCuentaAtras();
                            SelecionarUbicacionPieza();
                        }
                    });

                    aciertos++;
                } else {
                    avatar.lanzaAnimacion(VistaAvatar.Animacion.EJERCICIO_SUPERADO);
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.EJERCICIO_SUPERADO);
                    avatar.habla(R.raw.ok_superado, new VistaAvatar.OnAvatarHabla() {
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
                avatar.habla(R.raw.incorrecto, new VistaAvatar.OnAvatarHabla() {
                    @Override
                    public void onTerminaHabla() {
                        avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                        empiezaCuentaAtras();
                    }
                });

            }
        }
    }

    public void colocarColumna() {

        spnColumnas = findViewById(R.id.spnColumnas_s3e5);
        spnColumnas.setSelection(0);
        spnColumnas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Ejercicio();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void colocarFila() {
        spnFilas = findViewById(R.id.spnFilas_s3e5);
        spnFilas.setSelection(0);
        spnFilas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Ejercicio();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
