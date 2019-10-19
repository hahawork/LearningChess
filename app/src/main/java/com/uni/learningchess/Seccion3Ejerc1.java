package com.uni.learningchess;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Seccion3Ejerc1 extends Seccion3BaseActivity {

    Button botonTh8, botonRf1, botonDd6;
    private VistaAvatar avatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = findViewById(R.id.ejercicio1);
        linearLayout.setVisibility(View.VISIBLE);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        botonRf1 = findViewById(R.id.botonRf1);
        botonRf1.setTypeface(fuente);
        botonTh8 = findViewById(R.id.botonTh8);
        botonTh8.setTypeface(fuente);
        botonDd6 = findViewById(R.id.botondd6);
        botonDd6.setTypeface(fuente);

        avatar = getAvatar();
        avatar.habla(R.raw.mover_rey_en_jaque, new VistaAvatar.OnAvatarHabla() {
            @Override
            public void onTerminaHabla() {
                avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
                avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                empiezaCuentaAtras();
            }
        });

    }

    public void EventosBotones(View view) {
        if (view == botonTh8) {
            avatar.lanzaAnimacion(VistaAvatar.Animacion.EJERCICIO_SUPERADO);
            avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.EJERCICIO_SUPERADO);
            avatar.habla(R.raw.ok_superado, new VistaAvatar.OnAvatarHabla() {
                @Override
                public void onTerminaHabla() {
                    startActivity(new Intent(Seccion3Ejerc1.this, Seccion3Ejerc2.class));
                    finish();
                }
            });
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