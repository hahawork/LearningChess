package com.uni.learningchess;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Seccion3Ejerc2 extends Seccion3BaseActivity {

    Button botonOpcion1, botonOpcion2, botonOpcion3;
    private VistaAvatar avatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = findViewById(R.id.ejercicio2);
        linearLayout.setVisibility(View.VISIBLE);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        botonOpcion1 = findViewById(R.id.botonOpcion1);
        botonOpcion1.setTypeface(fuente);
        botonOpcion2 = findViewById(R.id.botonOpcion2);
        botonOpcion2.setTypeface(fuente);
        botonOpcion3 = findViewById(R.id.botonOpcion3);
        botonOpcion3.setTypeface(fuente);

        avatar = getAvatar();
        avatar.habla(R.raw.presentacion, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                empiezaCuentaAtras();
            }
        });


    }

    public void EventosBotones(View view) {
        if (view == botonOpcion3) {
            avatar.lanzaAnimacion(VistaAvatar.Animacion.EJERCICIO_SUPERADO);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.EJERCICIO_SUPERADO);
            avatar.habla(R.raw.ok_has_acertado, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    startActivity(new Intent(Seccion3Ejerc2.this,Seccion3Ejerc3.class));
                    finish();
                }
            });
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
        }

    }
}
