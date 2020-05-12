package com.uni.learningchess;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Vector;

import static com.uni.learningchess.Pieza.Color.BLANCO;
import static com.uni.learningchess.Pieza.Color.NEGRO;
import static com.uni.learningchess.Pieza.Tipo.ALFIL;
import static com.uni.learningchess.Pieza.Tipo.CABALLO;
import static com.uni.learningchess.Pieza.Tipo.DAMA;
import static com.uni.learningchess.Pieza.Tipo.PEON;
import static com.uni.learningchess.Pieza.Tipo.REY;
import static com.uni.learningchess.Pieza.Tipo.TORRE;

public class Seccion5Practica4 extends EjercicioBaseActivity {

    private enum MODO {AHOGADO, MATEENUNO, SALVARDELJAQUE}

    private Random random;
    private VistaAvatar avatar;
    TextView tvTituloEjercicio;
    ImageView ivSaltarEjercicio;
    MODO tipoJuego;
    MetodosGenerales mg;
    Vector<Pieza> vectorPiezasBlancas;
    Vector<Pieza> vectorPiezasNegras;

    SharedPreferences setting;
    BaseDatos baseDatos;
    String idUsuario = "";

    Seccion4Ahogado s4Ah;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.seccion5practica4;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        random = new Random(System.currentTimeMillis());

        baseDatos = new BaseDatos(this);
        setting = PreferenceManager.getDefaultSharedPreferences(this);
        idUsuario = setting.getString("spIdUsurioActual", "1");

        mg = new MetodosGenerales(this);
        s4Ah = new Seccion4Ahogado();

        vectorPiezasBlancas = new Vector<>();
        vectorPiezasNegras = new Vector<>();

        tvTituloEjercicio = findViewById(R.id.tvTituloEjerciciosPracticas);
        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        tvTituloEjercicio.setTypeface(fuente);


        avatar = getAvatar();
        avatar.habla(R.raw.seccion5_jaque, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                seleccionaTipoJuego();
            }
        });


        tvTituloEjercicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Animation animSequential;
                animSequential = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animacion_rotar_elemento);
                tvTituloEjercicio.startAnimation(animSequential);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        try {
            ivSaltarEjercicio = findViewById(R.id.ivSaltarEjercicio);
            ivSaltarEjercicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seleccionaTipoJuego();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvTituloEjercicio.setVisibility(View.VISIBLE);
        tvTituloEjercicio.setText("Ejercicios de prácticas");
        ivSaltarEjercicio.setVisibility(View.VISIBLE);
    }

    public void seleccionaTipoJuego() {

        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        retiraPiezas();

        tipoJuego = MODO.values()[random.nextInt(MODO.values().length)];
        //tipoJuego = MODO.MOVERPIEZA;

        switch (tipoJuego) {
            case AHOGADO:

                Tipo_Ahogado();
                break;
            case MATEENUNO:

                Tipo_MateEnUno();
                break;
            case SALVARDELJAQUE:
                Tipo_Ahogado();
                //MoverPieza();
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

    public void Tipo_Ahogado() {
        tvTituloEjercicio.setText("Ahogado");
        inicializaJugada1();
    }
    public void Tipo_MateEnUno()
    {
        tvTituloEjercicio.setText("Jaque mate en 1 movimiento");
        inicializaJugada2();
    }

    private void inicializaJugada1() {
        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        int variante = random.nextInt(6);
        switch (variante) {
            case 0:

                vectorPiezasBlancas.add(new Pieza(ALFIL, BLANCO, "E5"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "A5", true, "A6"));
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "C4"));

                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "A7"));
                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "A8"));
                break;
            case 1:
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "D5"));
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "E7", true, "C7"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "A8"));
                break;
            case 2:

                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "D1"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "A4", true, "A5"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "B7"));
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "D1"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "F6"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "G5"));

                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "F7"));
                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "E6"));
                break;
            case 3:
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "D7"));
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "C6", true, "D6"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "D8"));
                break;

            case 4:

                vectorPiezasBlancas.add(new Pieza(CABALLO, BLANCO, "A6"));
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "C6", true, "B6"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "A8"));
                break;
            case 5:
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "D4", true, "C4"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "F4"));
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "B1"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "A3"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "F5"));
                break;
        }
        colocaPiezas();
    }

    private void inicializaJugada2() {
        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        int variante = random.nextInt(9);

        switch (variante) {
            case 0: // tablero 1 segun documento
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "H1"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "A3"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "B2"));
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "B3"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "D1", true, "D8"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "E4"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "G2"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "H2"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "G8"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "A7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "C5"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "C7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "G7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "H7"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "B6"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "F7"));
                break;
            case 1://tablero 2
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "C1"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "A2"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "B2"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "C2"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "H1"));
                vectorPiezasBlancas.add(new Pieza(CABALLO, BLANCO, "E5", true, "G6"));
                vectorPiezasBlancas.add(new Pieza(ALFIL, BLANCO, "B3"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "H8"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "A7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "H7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "G7"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "A8"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "B8"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "C8"));
                break;
            case 2: //tablero 13
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "G1"));
                vectorPiezasBlancas.add(new Pieza(ALFIL, BLANCO, "A5"));
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "F4", true, "C7"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "D7"));
                vectorPiezasNegras.add(new Pieza(ALFIL, NEGRO, "E8"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "E6"));
                break;

            case 3://tablero 12
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "F1"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "B7"));
                vectorPiezasBlancas.add(new Pieza(CABALLO, BLANCO, "E6", true, "G7"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "E8"));
                vectorPiezasNegras.add(new Pieza(DAMA, NEGRO, "D8"));
                vectorPiezasNegras.add(new Pieza(CABALLO, NEGRO, "F8"));
                break;
            case 4://tablero 6
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "G2"));
                vectorPiezasBlancas.add(new Pieza(ALFIL, BLANCO, "G3"));
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "A4", true, "C6"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "C8"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "D8"));
                break;
            case 5://tablero 7
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "H1"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "E6"));
                vectorPiezasBlancas.add(new Pieza(CABALLO, BLANCO, "C5", true, "A6"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "C2"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "C7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "B7"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "D6"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "D8"));
                break;

            case 6://tablero 8
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "G1"));
                vectorPiezasBlancas.add(new Pieza(DAMA, BLANCO, "A1"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "D1"));
                vectorPiezasBlancas.add(new Pieza(CABALLO, BLANCO, "D4", true, "C6"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "E2"));
                vectorPiezasBlancas.add(new Pieza(ALFIL, BLANCO, "F7"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "E5"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "E4"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "E6"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "F5"));
                break;
            case 7://tablero 9
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "C1"));
                vectorPiezasBlancas.add(new Pieza(ALFIL, BLANCO, "B2"));
                vectorPiezasBlancas.add(new Pieza(CABALLO, BLANCO, "G5"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "H6", true, "H7"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "G8"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "F8"));
                break;
            case 8://tablero 10
                vectorPiezasBlancas.add(new Pieza(REY, BLANCO, "H1"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "B1"));
                vectorPiezasBlancas.add(new Pieza(PEON, BLANCO, "D6"));
                vectorPiezasBlancas.add(new Pieza(TORRE, BLANCO, "G7",true,"C7"));

                vectorPiezasNegras.add(new Pieza(REY, NEGRO, "C8"));
                vectorPiezasNegras.add(new Pieza(TORRE, NEGRO, "D8"));
                vectorPiezasNegras.add(new Pieza(PEON, NEGRO, "C7"));
                break;        }
        colocaPiezas();
    }

    private void colocaPiezas() {

        Vector<Pieza> vectorPiezas = new Vector<>();
        vectorPiezas.addAll(vectorPiezasBlancas);
        vectorPiezas.addAll(vectorPiezasNegras);
        mg.setVectorPiezas(vectorPiezas);

        for (Pieza pieza : vectorPiezasBlancas) {
            mg.colocaPieza(pieza);
        }
        for (Pieza pieza : vectorPiezasNegras) {
            mg.colocaPieza(pieza);
        }
    }

    public void eliminaPieza(Pieza pieza) {
        vectorPiezasBlancas.removeElement(pieza);
        vectorPiezasNegras.removeElement(pieza);
    }

    private Validador validador = new Validador() {
        @Override
        public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
            boolean validador = false;
            Pieza pieza = mg.getPieza(colOrigen, filaOrigen);

            if (pieza != null) {
                try {
                    boolean movimientoValidoBlancas = mg.validadorGenerico.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
                    Log.d("Ajedrez", "***movimientoValidoBlancas=" + movimientoValidoBlancas);
                    Pieza piezaDestino = mg.getPieza(colDestino, filaDestino);
                    if (piezaDestino != null && piezaDestino.getColor() == NEGRO) {
                        vectorPiezasNegras.remove(piezaDestino);
                    }


                    pieza.setCoordenada(colDestino, filaDestino);
                    //boolean reyEnJaque = reyEnJaque();
                    validador = movimientoValidoBlancas;// && reyEnJaque;
                    pieza.setCoordenada(colOrigen, filaOrigen);
                    if (piezaDestino != null && piezaDestino.getColor() == NEGRO) {
                        vectorPiezasNegras.add(piezaDestino);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return (validador);
        }
    };

    @Override
    protected boolean onMovimiento(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {

        cancelaCuentaAtras();
        Pieza piezaDestino = mg.getPieza(colDestino, filaDestino);
        boolean movimientoPiezaBlanca = (mg.getColorPieza(colOrigen, filaOrigen) == BLANCO);
        boolean movimientoValido = validador.movimientoValido(colOrigen, filaOrigen, colDestino, filaDestino);
        boolean capturaPieza = (mg.capturaPieza(colOrigen, filaOrigen, colDestino, filaDestino));
        boolean movimientoCorrecto = movimientoPiezaBlanca && movimientoValido;
        boolean capturaReyNegro = capturaPieza && (mg.getColorPieza(colOrigen, filaOrigen) == NEGRO);
        boolean esPiezaCorrectaJaqueMate = mg.getPieza(colOrigen, filaOrigen).isMoverDarJaqueMate();
        boolean esCasillaCorrecta = mg.getPieza(colOrigen, filaOrigen).getColumnaCorrecta() == colDestino &&
                mg.getPieza(colOrigen, filaOrigen).getFilaCorrecta() == filaDestino;

        if (movimientoCorrecto && capturaPieza) eliminaPieza(piezaDestino);

        return movimientoCorrecto && esPiezaCorrectaJaqueMate && esCasillaCorrecta && !capturaReyNegro;
    }

    @Override
    protected void onMovimiento(boolean movimientoValido, int colOrigen, int filaOrigen, int colDestino, int filaDestino) {

        avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
        if (movimientoValido) {
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.ARQUEAR);


            avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
            avatar.habla(R.raw.ok_has_acertado, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                    empiezaCuentaAtras();
                }
            });

            baseDatos.IncrementaAcierto(idUsuario, "4");
            seleccionaTipoJuego();

        } else {
            avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_INCORRECTO);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
            avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);
            avatar.habla(R.raw.mal_intenta_otra_vez, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                    empiezaCuentaAtras();
                }
            });
            baseDatos.IncrementaEjercicioFalla(idUsuario, "4");
        }
    }
}