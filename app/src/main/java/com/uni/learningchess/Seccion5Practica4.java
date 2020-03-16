package com.uni.learningchess;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Vector;

import static com.uni.learningchess.Pieza.Color.BLANCO;
import static com.uni.learningchess.Pieza.Color.NEGRO;
import static com.uni.learningchess.Pieza.Tipo.ALFIL;
import static com.uni.learningchess.Pieza.Tipo.DAMA;
import static com.uni.learningchess.Pieza.Tipo.PEON;
import static com.uni.learningchess.Pieza.Tipo.REY;

public class Seccion5Practica4 extends MoverPiezaActivity {

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
        vectorPiezasBlancas = new Vector<>();
        vectorPiezasNegras = new Vector<>();

        tvTituloEjercicio = findViewById(R.id.tvTituloEjerciciosPracticas);
        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        tvTituloEjercicio.setTypeface(fuente);

        avatar = getAvatar();
		avatar.habla(R.raw.seccion5_jaque, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                 seleccionaCoordenada();
            }
        });


        try {
            ivSaltarEjercicio = findViewById(R.id.ivSaltarEjercicio);
            ivSaltarEjercicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seleccionaCoordenada();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seleccionaCoordenada() {
        tvTituloEjercicio.setVisibility(View.VISIBLE);
        ivSaltarEjercicio.setVisibility(View.VISIBLE);
        seleccionaTipoJuego();
        inicializaJugada1();
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
				//ColorDeCasilla();
				break;
			case SALVARDELJAQUE:
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

	public  void Tipo_Ahogado (){

	}

    private void inicializaJugada1() {
        vectorPiezasBlancas.removeAllElements();
        vectorPiezasNegras.removeAllElements();
        int variante = random.nextInt(2);
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
        }
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
}