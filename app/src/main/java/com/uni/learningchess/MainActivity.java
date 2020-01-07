package com.uni.learningchess;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.PopupMenu;
import android.transition.Explode;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import static com.uni.learningchess.R.raw.musica;

public class MainActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Handler handler;
    private Runnable runnable;
    private VistaAvatar avatar;
    private MediaPlayer mediaPlayerMusica;
    private int volumenMusica;
    private boolean musicaActivada;
    private final int SOLICITUD_GRABAR_AUDIO = 0;
    private final int RETRASO_PRESENTACION = 3000; // Milisegundos que pasan hasta que empieza

    BaseDatos BD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.AppTheme);

        handler = new Handler();
        setContentView(R.layout.activity_main);

        BD = new BaseDatos(this);

        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        Typeface fuente1 = Typeface.createFromAsset(getAssets(), "fonts/Gorditas-Bold.ttf");
        //Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/BungeeShade-Regular.ttf");
        //Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/HoltwoodOneSC.ttf");
        Button boton1 = findViewById(R.id.boton1);
        boton1.setTypeface(fuente);
        Button boton2 = findViewById(R.id.boton2);
        boton2.setTypeface(fuente);
        Button boton3 = findViewById(R.id.boton3);
        boton3.setTypeface(fuente);

        Button boton4 = findViewById(R.id.boton4);
        boton4.setTypeface(fuente);

        TextView textoTituloApp = findViewById(R.id.textoTituloApp);
        textoTituloApp.setTypeface(fuente1);
        TextView textoContenidos = findViewById(R.id.textoContenidos);
        textoContenidos.setTypeface(fuente);

        mediaPlayerMusica = MediaPlayer.create(this, musica);
        mediaPlayerMusica.setLooping(true);

        avatar = findViewById(R.id.vistaAvatar);
        avatar.setActividad(this);
        leerPreferencias();
        presentacion();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        modificaVolumenMusica(0, 100, 0, (RETRASO_PRESENTACION / 100));
    }

    @Override
    public void onResume() {
        super.onResume();
        leerPreferencias();
        avatar.reanudar();
    }

    @Override
    public void onPause() {
        avatar.pausar();
        handler.removeCallbacks(runnable);
        mediaPlayerMusica.pause();
        super.onPause();
    }

    // Referencia: http://stackoverflow.com/questions/6884590/android-how-to-create-fade-in-fade-out-sound-effects-for-any-music-file-that-my
    private void actualizaVolumenMusica(int cambio) {
        volumenMusica += cambio;
        if (volumenMusica < 0)
            volumenMusica = 0;
        else if (volumenMusica > 100)
            volumenMusica = 100;
        float floatVolumenMusica = 1 - ((float) Math.log(100 - volumenMusica) / (float) Math.log(100));
        if (floatVolumenMusica < 0)
            floatVolumenMusica = 0;
        else if (floatVolumenMusica > 1)
            floatVolumenMusica = 1;
        mediaPlayerMusica.setVolume(floatVolumenMusica, floatVolumenMusica);
    }

    // Referencia: http://stackoverflow.com/questions/6884590/android-how-to-create-fade-in-fade-out-sound-effects-for-any-music-file-that-my
    private void modificaVolumenMusica(final int volumenInicial, final int volumenFinal, int retraso, int periodo) {
        try {
            volumenMusica = volumenInicial;
            final Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (volumenInicial < volumenFinal)
                        actualizaVolumenMusica(1);
                    else
                        actualizaVolumenMusica(-1);
                    if (volumenMusica == volumenFinal) {
                        if (volumenFinal == 0) {
                            mediaPlayerMusica.pause();
                        }
                        timer.cancel();
                        timer.purge();
                    }
                }
            };
            timer.schedule(timerTask, retraso, periodo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void presentacion() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    avatar.habla(R.raw.presentacion, new VistaAvatar.OnAvatarHabla() {
                        @Override
                        public void onTerminaHabla() {
                            if (musicaActivada) {
                                mediaPlayerMusica.start();
                                modificaVolumenMusica(0, 100, 0, 30);
                            }
                        }
                    });
                }
            };
            handler.postDelayed(runnable, RETRASO_PRESENTACION);
            if (musicaActivada) {
                mediaPlayerMusica.start();
                modificaVolumenMusica(100, 0, 0, (RETRASO_PRESENTACION / 100));
            }
        } else {
            solicitarPermisoRecordAudio();
        }
    }

    public void boton1(View v) {
        startActivity(new Intent(this, Seccion1.class));
    }

    public void boton2(View v) {
        startActivity(new Intent(this, Seccion2.class));
    }

    public void boton3(View v) {
        startActivity(new Intent(this, Seccion3.class));
    }

    public void boton4(View v) {
        startActivity(new Intent(this, Seccion4.class));
    }

    public void boton5(View v) {
        startActivity(new Intent(this, Seccion5.class));
    }

    public void acercaDe(View v) {
        showPopupMenu(v);
    }

    /**
     * Showing popup menu when tapping on menu boton
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(this, view);
        try {
            // Reflection apis to enforce show icon
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("mPopup")) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main_boton_opciones, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_Usuarios:
                    startActivity(new Intent(MainActivity.this, userManejarUsuarios.class));
                    return true;
                case R.id.action_Sobre:
                    startActivity(new Intent(MainActivity.this, AcercaDeActivity.class));
                    return true;
                case R.id.action_Preferencias:
                    startActivity(new Intent(MainActivity.this, Preferencias.class));
                    return true;
                case R.id.action_Ayuda:
                    startActivity(new Intent(MainActivity.this, Ayuda.class));
                    return true;
                default:
            }
            return false;
        }
    }

    void solicitarPermisoRecordAudio() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) &&
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Snackbar snackbar = Snackbar.make(avatar, "Sin el permiso grabación de audio,\n"
                    + "no puedo mostrarte el avatar hablando.", Snackbar.LENGTH_INDEFINITE);
            View snackbarView = snackbar.getView();
            TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(2);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE}, SOLICITUD_GRABAR_AUDIO);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE}, SOLICITUD_GRABAR_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SOLICITUD_GRABAR_AUDIO) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presentacion();
            } else {
                Snackbar snackbar = Snackbar.make(avatar, "Sin el permiso grabación de audio,\n"
                        + "no puedo mostrarte el avatar hablando.", Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(2);
                snackbar.show();
                finish();
            }
        }
    }

    private void leerPreferencias() {
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);
        boolean musicaActivada = preferencias.getBoolean("musica", true);
        if (musicaActivada) {
            mediaPlayerMusica.start();
            if (!this.musicaActivada) {
                this.musicaActivada = musicaActivada;

                int volumen = preferencias.getInt("volume", 0);
                modificaVolumenMusica(0, (volumen * 10), 0, 30);
            }
        }
        avatar.setSonidosActivados(preferencias.getBoolean("sonidos", true));
    }

}
