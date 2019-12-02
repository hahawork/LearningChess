package com.uni.learningchess;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class Seccion5Practica1 extends EjercicioBaseActivity {

    private enum MODO {SENCASILLA, COLOCPIEZA}

    private String coordenadaSolicitada;
    private Random random;
    private VistaAvatar avatar;
    TextView tvTituloEjercicio;
    ImageView ivSaltarEjercicio;
    int columnaAleatoria, filaAleatoria;
    Pieza.Tipo piezaSeleccionada;
    MODO tipoJuego;
    BaseDatos baseDatos;
    SharedPreferences setting;
    String idUsuario = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        random = new Random(System.currentTimeMillis());
        baseDatos = new BaseDatos(this);
        setting = PreferenceManager.getDefaultSharedPreferences(this);
        idUsuario = setting.getString("spIdUsurioActual", "1");

        tvTituloEjercicio = findViewById(R.id.tvTituloEjerciciosPracticas);
        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        tvTituloEjercicio.setTypeface(fuente);
        ivSaltarEjercicio = findViewById(R.id.ivSaltarEjercicio);
        ivSaltarEjercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionaCoordenada();
            }
        });

        avatar = getAvatar();
        avatar.habla(R.raw.senyala_casilla_presentacion, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                seleccionaCoordenada();
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.tablero;
    }

    public void seleccionaCoordenada() {
        String[] letras = {"A", "B", "C", "D", "E", "F", "G", "H"};
        columnaAleatoria = random.nextInt(7);
        filaAleatoria = random.nextInt(7);
        String columna = letras[columnaAleatoria];

        tvTituloEjercicio.setVisibility(View.VISIBLE);
        ivSaltarEjercicio.setVisibility(View.VISIBLE);

        int fila = 1 + filaAleatoria;
        coordenadaSolicitada = columna + fila;

        preguntaCoordenada();
        seleccionaTipoJuego();
    }

    public void preguntaCoordenada() {
        Toast.makeText(this, coordenadaSolicitada, Toast.LENGTH_LONG).show();
    }

    public void seleccionaTipoJuego() {

        retiraPiezas();

        tipoJuego = MODO.values()[random.nextInt(MODO.values().length)];
        switch (tipoJuego) {
            case SENCASILLA:
                ModoSenyalarCasilla();
                break;
            case COLOCPIEZA:
                ModoColocarPiezas();
                break;
        }
    }

    public void ModoSenyalarCasilla() {
        tvTituloEjercicio.setText("Seleccione la casilla: " + coordenadaSolicitada);
        LinearLayout piezas = findViewById(R.id.piezas);
        piezas.setVisibility(View.GONE);
    }

    public void ModoColocarPiezas() {
        LinearLayout piezas = findViewById(R.id.piezas);
        piezas.setVisibility(View.VISIBLE);

        piezaSeleccionada = Pieza.Tipo.values()[random.nextInt(Pieza.Tipo.values().length)];

        tvTituloEjercicio.setText("Coloque la pieza " + piezaSeleccionada + " en la casilla: " + coordenadaSolicitada);
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

    //********* para la parte se señalar casillas ***************************************
    @Override
    protected boolean onPulsar(ImageView imageView) {
        if (tipoJuego == MODO.SENCASILLA) {

            if (coordenadaSolicitada == null) return false;
            cancelaCuentaAtras();
            avatar.paraEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
            avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);

            int colCoordenadaSolicitada = coordenadaSolicitada.charAt(0) - 'A';
            int filaCoordenadaSolicitada = coordenadaSolicitada.charAt(1) - '1';
            resaltarCasilla(colCoordenadaSolicitada, filaCoordenadaSolicitada, Movimiento.CORRECTO);

            String coordenadaPulsada = imageView.getTag().toString();
            if (coordenadaPulsada.equals(coordenadaSolicitada)) {
                baseDatos.IncrementaAcierto(idUsuario, "1");
                avatar.mueveCejas(VistaAvatar.MovimientoCejas.ARQUEAR);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
                avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
                int audio = random.nextInt(2);
                switch (audio) {
                    case 0:
                        avatar.habla(R.raw.ok_has_acertado, new VistaAvatar.OnAvatarHabla() {
                            @Override
                            public void onTerminaHabla() {
                                seleccionaCoordenada();
                            }
                        });
                        break;

                    case 1:
                        avatar.habla(R.raw.ok_muy_bien, new VistaAvatar.OnAvatarHabla() {
                            @Override
                            public void onTerminaHabla() {
                                seleccionaCoordenada();
                            }
                        });
                        break;

                    case 2:
                        avatar.habla(R.raw.ok_superado, new VistaAvatar.OnAvatarHabla() {
                            @Override
                            public void onTerminaHabla() {
                                seleccionaCoordenada();
                            }
                        });
                        break;
                }
            } else {
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
                avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_INCORRECTO);
                avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);
                int colCoordenadaPulsada = coordenadaPulsada.charAt(0) - 'A';
                int filaCoordenadaPulsada = coordenadaPulsada.charAt(1) - '1';
                resaltarCasilla(colCoordenadaPulsada, filaCoordenadaPulsada, Movimiento.INCORRECTO);
                preguntaCoordenada();
                baseDatos.IncrementaAcierto(idUsuario, "1");
            }
        }
        return true;
    }

    //*******************************************************************************************


    //** para la parte de colocar piezas *******************************************************
    @Override
    protected boolean onMovimiento(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
        return false; //Una vez colocada no permitimos moverla
    }

    @Override
    protected boolean onColocar(char pieza, int colDestino, int filaDestino) {
        boolean salida;
        char p = piezaSeleccionada.toString().charAt(0);

        salida = (p == pieza) &&
                (filaDestino == filaAleatoria) &&
                (colDestino == columnaAleatoria);

        if (salida) {

            avatar.mueveCejas(VistaAvatar.MovimientoCejas.ARQUEAR);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
            avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
            avatar.habla(R.raw.ok_has_acertado, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {

                    baseDatos.IncrementaAcierto(idUsuario, "1");
                    seleccionaCoordenada();
                }
            });

        } else {
            int colCoordenadaSolicitada = coordenadaSolicitada.charAt(0) - 'A';
            int filaCoordenadaSolicitada = coordenadaSolicitada.charAt(1) - '1';
            resaltarCasilla(colCoordenadaSolicitada, filaCoordenadaSolicitada, Movimiento.CORRECTO);

            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
            avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_INCORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);

            resaltarCasilla(colDestino, filaDestino, Movimiento.INCORRECTO);

            baseDatos.IncrementaEjercicioFalla(idUsuario, "1");

            preguntaCoordenada();

        }
        return salida;
    }
    //************************************************************************************************
}