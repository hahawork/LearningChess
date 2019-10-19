package com.uni.learningchess;

import android.content.ClipData;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Seccion3BaseActivity extends AppCompatActivity {
    private VistaAvatar avatar;
    private CountDownTimer cuentaAtras;
    private int TIEMPO_CUENTA_ATRAS = 8000; // milisegundos

    public enum Movimiento {
        ORIGEN, INCORRECTO, CORRECTO
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seccion3baseactivity);

        configuraCuentaAtras(TIEMPO_CUENTA_ATRAS);
        avatar = (VistaAvatar) findViewById(R.id.vistaAvatar);
        avatar.setActividad(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        leerPreferencias();
        avatar.reanudar();
    }

    @Override
    public void onPause() {
        cancelaCuentaAtras();
        avatar.pausar();
        super.onPause();
    }

    private void leerPreferencias(){
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        avatar.setSonidosActivados(preferencias.getBoolean("sonidos", true));
    }

    public VistaAvatar getAvatar() {
        return avatar;
    }

    private void configuraCuentaAtras(long millisUntilFinished){
        cuentaAtras = new CountDownTimer(millisUntilFinished, millisUntilFinished) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                onFinalCuentaAtras();
            }
        };
    }

    public void empiezaCuentaAtras(){
        cuentaAtras.start();
    }

    public void cancelaCuentaAtras(){
        cuentaAtras.cancel();
    }

    protected boolean esCuadriculaNegra(ImageView imageview) {
        String tag = imageview.getTag().toString();
        int col = tag.charAt(0) - 'A';
        int fila = tag.charAt(1) - '1';
        return (((col + fila) % 2) == 0);
    }

    protected void onFinalCuentaAtras(){}

    /**
     * permite verificar si el movimiento de una pieza de ajedrez es válido
     *
     * @author Jesús Tomás
     */
    interface Validador {
        /**
         * nos indica si la pieza puede ir de colOrigen, filaOrigen a colDestino, filaDestino
         */
        boolean botonClic(View view);
    }
}


