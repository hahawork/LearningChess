package com.uni.learningchess;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;
import java.util.Vector;

public class Seccion5Practica3 extends MoverPiezaActivity {

    private enum MODO {NOTACION, COLORCASILLA, MOVERPIEZA, COORDENADAPIEZA}

    private Random random;
    private VistaAvatar avatar;
    TextView tvTituloEjercicio;
    ImageView ivSaltarEjercicio;
    int columnaAleatoria, filaAleatoria;
    Pieza.Tipo piezaSeleccionada;
    String coordenadaSolicitada;
    MODO tipoJuego;
    MetodosGenerales MG;
    Vector<Pieza> vectorPiezasBlancas;
    Vector<Pieza> vectorPiezasNegras;
    String[] letrasColumnas = {"A", "B", "C", "D", "E", "F", "G", "H"};

    SharedPreferences setting;
    BaseDatos baseDatos;
    String idUsuario = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        random = new Random(System.currentTimeMillis());

        baseDatos = new BaseDatos(this);
        setting = PreferenceManager.getDefaultSharedPreferences(this);
        idUsuario = setting.getString("spIdUsurioActual", "1");

        MG = new MetodosGenerales(this);
        vectorPiezasBlancas = new Vector<>();
        vectorPiezasNegras = new Vector<>();

        tvTituloEjercicio = findViewById(R.id.tvTituloEjerciciosPracticas);
        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        tvTituloEjercicio.setTypeface(fuente);

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

        avatar = getAvatar();
        avatar.habla(R.raw.seccion5_notacion, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                coordenadaSolicitada = seleccionaCoordenada();
                seleccionaTipoJuego();
            }
        });


        try {
            ivSaltarEjercicio = findViewById(R.id.ivSaltarEjercicio);
            ivSaltarEjercicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    coordenadaSolicitada = seleccionaCoordenada();
                    seleccionaTipoJuego();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.seccion5practica3;
    }

    public String seleccionaCoordenada() {
        columnaAleatoria = random.nextInt(7);
        filaAleatoria = random.nextInt(7);
        String columna = letrasColumnas[columnaAleatoria];

        tvTituloEjercicio.setVisibility(View.VISIBLE);
        ivSaltarEjercicio.setVisibility(View.VISIBLE);

        int fila = 1 + filaAleatoria;
        String _coordenada = columna + fila;
        return _coordenada;
    }

    public void seleccionaTipoJuego() {

        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        retiraPiezas();

        tipoJuego = MODO.values()[random.nextInt(MODO.values().length)];
        //tipoJuego = MODO.MOVERPIEZA;
        AlternarDisenyo(tipoJuego);

        switch (tipoJuego) {
            case NOTACION:
                CrearNotacion();
                break;
            case COLORCASILLA:
                ColorDeCasilla();
                break;
            case MOVERPIEZA:
                MoverPieza();
                break;
            case COORDENADAPIEZA:
                coordenadaSolicitada = seleccionaCoordenada();
                seleccionaTipoJuego();
                // SelecionarUbicacionPieza();
                break;
        }
    }

    public void AlternarDisenyo(MODO modo) {

        // LinearLayout llSecciones_layouts = findViewById(R.id.llSecciones_layouts);
        // llSecciones_layouts.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = null;
        //RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.custom_layout, null, false);


        if (modo == MODO.NOTACION) {

            //view = inflater.inflate(R.layout.activity_seccion3ejerc2, null, false);

            (findViewById(R.id.include_notacion)).setVisibility(View.VISIBLE);
            (findViewById(R.id.include_colorcasilla)).setVisibility(View.GONE);
            (findViewById(R.id.include_tablero)).setVisibility(View.GONE);
            (findViewById(R.id.include_coordenadascasilla)).setVisibility(View.GONE);

        } else if (modo == MODO.COLORCASILLA) {

            //view = inflater.inflate(R.layout.activity_seccion3ejerc3, null, false);

            (findViewById(R.id.include_notacion)).setVisibility(View.GONE);
            (findViewById(R.id.include_colorcasilla)).setVisibility(View.VISIBLE);
            (findViewById(R.id.include_tablero)).setVisibility(View.GONE);
            (findViewById(R.id.include_coordenadascasilla)).setVisibility(View.GONE);

        } else if (modo == MODO.MOVERPIEZA) {

            //view = inflater.inflate(R.layout.activity_seccion3ejerc4 , null, false);

            (findViewById(R.id.include_notacion)).setVisibility(View.GONE);
            (findViewById(R.id.include_colorcasilla)).setVisibility(View.GONE);
            (findViewById(R.id.include_tablero)).setVisibility(View.VISIBLE);
            (findViewById(R.id.include_coordenadascasilla)).setVisibility(View.GONE);

        } else {

            //view = inflater.inflate(R.layout.activity_seccion3ejerc5, null, false);

            (findViewById(R.id.include_notacion)).setVisibility(View.GONE);
            (findViewById(R.id.include_colorcasilla)).setVisibility(View.GONE);
            (findViewById(R.id.include_tablero)).setVisibility(View.GONE);
            (findViewById(R.id.include_coordenadascasilla)).setVisibility(View.VISIBLE);
        }

        /*if (view != null) {
            llSecciones_layouts.addView(view);
        } else {
            coordenadaSolicitada = seleccionaCoordenada();
            seleccionaTipoJuego();
        }*/
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

    Button opcion1, opcion2, opcion3;

    public void CrearNotacion() {
        String[][] arrNotaciones = new String[][]{
                {"0-0", "Enroque Corto"},
                {"0-0-0", "Enroque Largo"}
        };

        Pieza.Tipo pieza = Pieza.Tipo.values()[random.nextInt(Pieza.Tipo.values().length)];
        String _coordenada = coordenadaSolicitada.toLowerCase();
        String _pieza = pieza.toString().substring(0, 1);
        boolean _captura = System.currentTimeMillis() % 2 == 0; // si el tiempo es un numero par se hace captura.
        boolean _jaque = System.currentTimeMillis() % 20 == 0;  // si el tiempo es divisible en 20 y el residuo es 0
        boolean _jaquemate = System.currentTimeMillis() % 100 == 0;  // si el tiempo es divisible en 100 y el residuo es 0, para reducirlas posibilidades


        // cuando se mueve el PEON no se reresenta la letra.
        String _notacion = (_pieza.equalsIgnoreCase("P") ? "" : _pieza)
                + (_captura ? "x" : "")     // cuando hay captura se representa con x
                + _coordenada   // seguida de las coordenadas a la que se movio la píeza
                + (_jaque ? "+" : (_jaquemate ? "++" : ""));

        StringBuilder cadena = new StringBuilder();
        cadena.append("Movió ");
        cadena.append(pieza.toString());
        cadena.append(_captura ? " y hace captura a " : " a ");
        cadena.append("la casilla ");
        cadena.append(_coordenada);
        cadena.append((_jaque ? " con jaque" : (_jaquemate ? " con jaque mate" : "")));

        Pieza.Tipo pieza1 = Pieza.Tipo.values()[random.nextInt(Pieza.Tipo.values().length)];
        String _coordenadafake1 = seleccionaCoordenada().toLowerCase();
        String cadena_fake1 = String.format(
                "Movió %s %s la casilla %s ",
                pieza1.toString(),
                (System.currentTimeMillis() % 2 == 0) ? " y hace captura a " : " a ",
                _coordenadafake1);

        Pieza.Tipo pieza2;
        do {
            pieza2 = Pieza.Tipo.values()[random.nextInt(Pieza.Tipo.values().length)];
        } while (pieza2 == pieza);

        //String _coordenadafake2 = seleccionaCoordenada().toLowerCase();
        String cadena_fake2 = String.format("Movió %s %s la casilla %s ",
                pieza2.toString(),
                (System.currentTimeMillis() % 2 == 0) ? " y hace captura a " : " a ",
                _coordenada);

        tvTituloEjercicio.setText("¿Qué significa la notación abreviada " + _notacion + "?");
        findViewById(R.id.tvTituloNotacion_as3e2).setVisibility(View.GONE);//setText("¿Qué significa la notación abreviada " + _notacion + "?");
        opcion1 = findViewById(R.id.botonOpcion1);
        opcion2 = findViewById(R.id.botonOpcion2);
        opcion3 = findViewById(R.id.botonOpcion3);


        // se obtinene el tiempo del sistema en milisegundos
        //esto servira para alternar el orden de las respuestas correcta en los botones
        String tiempo = "" + System.currentTimeMillis();
        //  se agarra el ultimo digito del tiempo.
        int _ultDigito = Integer.parseInt(tiempo.substring(tiempo.length() - 1));
        // segun el ultimo digito obtenido, tenemos 3 botones con 2 falsas y una verdadera.
        // la opcion verdadera se destaca en el tag(1) y la opcion falsa con tag(0)
        switch (_ultDigito) {
            case 0:
            case 3:
            case 6:
            case 9:

                opcion1.setText(cadena.toString());
                opcion1.setTag("1");
                opcion2.setText(cadena_fake1);
                opcion2.setTag("0");
                opcion3.setText(cadena_fake2);
                opcion3.setTag("0");
                break;
            case 1:
            case 4:
            case 7:
                opcion1.setText(cadena_fake1);
                opcion1.setTag("0");
                opcion2.setText(cadena.toString());
                opcion2.setTag("1");
                opcion3.setText(cadena_fake2);
                opcion3.setTag("0");
                break;
            case 2:
            case 5:
            case 8:
                opcion1.setText(cadena_fake2);
                opcion1.setTag("0");
                opcion2.setText(cadena_fake1);
                opcion2.setTag("0");
                opcion3.setText(cadena.toString());
                opcion3.setTag("1");
                break;
        }
    }

    public void ColorDeCasilla() {

        ImageView casilla = getCasilla(columnaAleatoria, filaAleatoria);
        boolean esCuadriculaNegra = esCuadriculaNegra(casilla);

        tvTituloEjercicio.setText(String.format(getResources().getString(R.string.seccion3Ejerc3Titulo), coordenadaSolicitada));
        findViewById(R.id.textoSeccion3Ejerc3Titulo).setVisibility(View.GONE);//setText(String.format(getResources().getString(R.string.seccion3Ejerc3Titulo), coordenadaSolicitada));
        opcion1 = findViewById(R.id.botonBlanca);
        opcion1.setText("Blanca");
        opcion1.setTag(esCuadriculaNegra ? "0" : "1");


        opcion2 = findViewById(R.id.botonNegra);
        opcion2.setText("Negra");
        opcion2.setTag(esCuadriculaNegra ? "1" : "0");

        EventosBotones(null);
    }

    public void EventosBotones(View view) {
        if (view != null) {
            if (view.getTag() == "1") {
                avatar.lanzaAnimacion(VistaAvatar.Animacion.EJERCICIO_SUPERADO);
                //avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.EJERCICIO_SUPERADO);
                avatar.habla(R.raw.correcto, new VistaAvatar.OnAvatarHabla() {
                    @Override
                    public void onTerminaHabla() {
                        baseDatos.IncrementaAcierto(idUsuario, "3");
                        coordenadaSolicitada = seleccionaCoordenada();
                        seleccionaTipoJuego();
                    }
                });
            } else {
                baseDatos.IncrementaEjercicioFalla(idUsuario, "3");
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

    public void MoverPieza() {
        LinearLayout piezas = findViewById(R.id.piezas);
        piezas.setVisibility(View.VISIBLE);
        findViewById(R.id.torre).setVisibility(View.GONE);
        findViewById(R.id.rey).setVisibility(View.GONE);
        findViewById(R.id.caballo).setVisibility(View.GONE);
        findViewById(R.id.dama).setVisibility(View.GONE);
        findViewById(R.id.alfil).setVisibility(View.GONE);

        //TextView titulo = findViewById(R.id.tvTituloEjerciciosPracticas);
        tvTituloEjercicio.setText(String.format(getResources().getString(R.string.seccion3Ejerc4Titulo), coordenadaSolicitada));

    }

    private void SelecionarUbicacionPieza() {
        retiraPiezas();

        Pieza pieza = new Pieza(randomEnum(Pieza.Tipo.class), randomEnum(Pieza.Color.class), coordenadaSolicitada);
       // Toast.makeText(this, coordenadaSolicitada, Toast.LENGTH_LONG).show();
        MG.colocaPieza(pieza);
        colocarColumna();
        colocarFila();
    }

    /**
     * Metodo para obtener de manera aleatoria un valor del enum recibido. Uso: randomEnum(Clase.Enum.class)
     *
     * @param clazz El Enum.
     * @param <T>   ??
     * @return El enum que agarro de modo aleatorio.
     */
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = new Random().nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public void colocarColumna() {

        Spinner spnColumnas = findViewById(R.id.spnColumnas_s3e5);
        spnColumnas.setSelection(0);
        spnColumnas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EjercicioCoordenadasPieza();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void colocarFila() {
        Spinner spnFilas = findViewById(R.id.spnFilas_s3e5);
        spnFilas.setSelection(0);
        spnFilas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EjercicioCoordenadasPieza();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void EjercicioCoordenadasPieza() {
        String fil = ((Spinner) findViewById(R.id.spnFilas_s3e5)).getSelectedItem().toString();
        String col = ((Spinner) findViewById(R.id.spnColumnas_s3e5)).getSelectedItem().toString();

        // si ambos campos tiene texto
        if (fil.length() == 1 && col.length() == 1) {

            int NumFila = Integer.parseInt(fil);
            //verifica repuesta correcta
            if (NumFila == (filaAleatoria + 1) && col.equalsIgnoreCase(letrasColumnas[columnaAleatoria])) {


                avatar.mueveCejas(VistaAvatar.MovimientoCejas.ARQUEAR);
                avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_CORRECTO);
                avatar.lanzaAnimacion(VistaAvatar.Animacion.EJERCICIO_SUPERADO);
                //avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.EJERCICIO_SUPERADO);
                avatar.habla(R.raw.excelente_completaste_ejercicios, new VistaAvatar.OnAvatarHabla() {
                    @Override
                    public void onTerminaHabla() {
                        baseDatos.IncrementaAcierto(idUsuario, "3");
                        coordenadaSolicitada = seleccionaCoordenada();
                        seleccionaTipoJuego();
                    }
                });
            } else {
                baseDatos.IncrementaEjercicioFalla(idUsuario, "3");
                avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_INCORRECTO);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
                avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);
                avatar.habla(R.raw.incorrecto, new VistaAvatar.OnAvatarHabla() {
                    @Override
                    public void onTerminaHabla() {
                        avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                    }
                });

            }
        }
    }


    @Override
    protected boolean onMovimiento(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {

        if (tipoJuego != MODO.MOVERPIEZA) {
        }

        return false;
    }

    @Override
    protected boolean onColocar(char pieza, int colDestino, int filaDestino) {
        final boolean salida;
        switch (pieza) {
            case 'P':
                salida = filaDestino == filaAleatoria && colDestino == columnaAleatoria;
                break;
            default:
                salida = false;
        }
        if (tipoJuego == MODO.MOVERPIEZA) {
            if (salida) {
                avatar.lanzaAnimacion(VistaAvatar.Animacion.EJERCICIO_SUPERADO);
                //avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.EJERCICIO_SUPERADO);
                avatar.habla(R.raw.excelente_completaste_ejercicios, new VistaAvatar.OnAvatarHabla() {
                    @Override
                    public void onTerminaHabla() {

                        LinearLayout piezas = findViewById(R.id.piezas);
                        piezas.setVisibility(View.GONE);

                        baseDatos.IncrementaAcierto(idUsuario, "3");
                        coordenadaSolicitada = seleccionaCoordenada();
                        seleccionaTipoJuego();

                    }
                });

            } else {
                switch (pieza) {
                    case 'P':
                        avatar.habla(R.raw.colocar_piezas_mal_peon);

                        resaltarCasilla(columnaAleatoria, filaAleatoria,
                                new Validador() {
                                    @Override
                                    public boolean movimientoValido(int colOrigen, int filaOrigen, int colDestino, int filaDestino) {
                                        return salida;
                                        //return filaDestino == filaSeleccionada && colDestino == columnaSeleccionada;
                                    }
                                });
                        break;
                }


                avatar.lanzaAnimacion(VistaAvatar.Animacion.MOVIMIENTO_INCORRECTO);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
                avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);

                baseDatos.IncrementaEjercicioFalla(idUsuario, "3");
            }
        }
        return salida;
    }
}