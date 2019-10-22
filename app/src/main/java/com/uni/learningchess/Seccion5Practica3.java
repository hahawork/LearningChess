package com.uni.learningchess;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Vector;

public class Seccion5Practica3 extends MoverPiezaActivity {

    private enum MODO {NOTACION, COLORCASILLA, MOVERPIEZA, COORDENADAPIEZA}

    private Random random;
    private VistaAvatar avatar;
    TextView tvTituloEjercicio;
    int columnaAleatoria, filaAleatoria;
    Pieza.Tipo piezaSeleccionada;
    String coordenadaSolicitada;
    MODO tipoJuego;
    MetodosGenerales MG;
    Vector<Pieza> vectorPiezasBlancas;
    Vector<Pieza> vectorPiezasNegras;
    String[] letrasColumnas = {"A", "B", "C", "D", "E", "F", "G", "H"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        random = new Random(System.currentTimeMillis());
        MG = new MetodosGenerales(this);
        vectorPiezasBlancas = new Vector<>();
        vectorPiezasNegras = new Vector<>();

        tvTituloEjercicio = findViewById(R.id.tvTituloEjerciciosPracticas);
        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        tvTituloEjercicio.setTypeface(fuente);

        avatar = getAvatar();
        avatar.habla(R.raw.senyala_casilla_presentacion, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                coordenadaSolicitada = seleccionaCoordenada();
                seleccionaTipoJuego();
            }
        });

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.tablero;
    }

    public String seleccionaCoordenada() {
        columnaAleatoria = random.nextInt(7);
        filaAleatoria = random.nextInt(7);
        String columna = letrasColumnas[columnaAleatoria];

        tvTituloEjercicio.setVisibility(View.VISIBLE);

        int fila = 1 + filaAleatoria;
        String _coordenada = columna + fila;
        return _coordenada;
    }

    public void seleccionaTipoJuego() {

        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        retiraPiezas();

        tipoJuego = MODO.values()[random.nextInt(MODO.values().length)];
        AlternarDisenyo(tipoJuego);

        switch (tipoJuego) {
            case NOTACION:
                ModoValorPiezas();
                break;
            case COLORCASILLA:
                ModoCapturarPiezas();
                break;
            case MOVERPIEZA:
                break;
            case COORDENADAPIEZA:
                break;
        }
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

    private JSONObject CrearNotacion() {
        Pieza.Tipo pieza = Pieza.Tipo.values()[random.nextInt(Pieza.Tipo.values().length)];
        String _coordenada = coordenadaSolicitada.toLowerCase();
        String _pieza = pieza.toString().substring(0, 1);
        boolean _captura = System.currentTimeMillis() % 2 == 0;

        String _notacion = _pieza != "P" ? _pieza : ""
                + ((_captura ? "x" : "")
                + _coordenada);

        StringBuilder cadena = new StringBuilder();
        cadena.append("Movió ");
        cadena.append(pieza.toString());
        cadena.append(_captura ? " y hace captura a" : "a ");
        cadena.append("la casilla ");
        cadena.append(_coordenada);

        Pieza.Tipo pieza1 = Pieza.Tipo.values()[random.nextInt(Pieza.Tipo.values().length)];
        String _coordenadafake1 = seleccionaCoordenada().toLowerCase();
        String cadena_fake1 = String.format(
                "Movió %s %s la casilla %s",
                pieza1.toString(),
                (System.currentTimeMillis() % 2 == 0) ? "y hace captura a" : "a",
                _coordenadafake1);

        Pieza.Tipo pieza2 = Pieza.Tipo.values()[random.nextInt(Pieza.Tipo.values().length)];
        String _coordenadafake2 = seleccionaCoordenada().toLowerCase();
        String cadena_fake2 = String.format("Movió %s %s la casilla %s",
                pieza2.toString(),
                (System.currentTimeMillis() % 2 == 0) ? "y hace captura a" : "a",
                _coordenadafake2);

        try {
            String tiempo = "" + System.currentTimeMillis();
            int _ultDigito = Integer.parseInt(tiempo.substring(tiempo.length() - 1));
            switch (_ultDigito) {
                case 0:
                case 3:
                case 6:
                case 9:


                    return new JSONObject("");

                case 1:
                case 4:
                case 7:
                    break;
                case 2:
                case 5:
                case 8:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
