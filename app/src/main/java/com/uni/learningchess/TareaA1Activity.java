package com.uni.learningchess;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class TareaA1Activity extends AppCompatActivity {
    private VistaAvatar avatar;
    private final int REQUEST_RECORD_AUDIO = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tarea_a1);
        avatar = (VistaAvatar) findViewById(R.id.vistaAvatar);
        avatar.setActividad(this);

        Spinner spinnerLecciones = (Spinner) findViewById(R.id.spinner_lecciones);
        spinnerLecciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                configuraVoz();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinnerMiradas = (Spinner) findViewById(R.id.spiner_miradas);
        spinnerMiradas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                configuraMirada();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ToggleButton toggleTicTac = (ToggleButton) findViewById(R.id.toggleTicTac);
        toggleTicTac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                } else {
                    avatar.paraEfectoSonido(VistaAvatar.EfectoSonido.TIC_TAC);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        avatar.reanudar();
    }

    @Override
    public void onPause() {
        avatar.pausar();
        super.onPause();
    }

    public void habla(View view) {
        avatar.habla();
    }

    public void calla(View view) {
        avatar.calla();
    }

    public void reproduceEfectoSonido(View view){
        Spinner spinnerEfectosSonido = (Spinner) findViewById(R.id.spinerEfectosSonido);
        String opcion = spinnerEfectosSonido.getSelectedItem().toString();
        if (opcion.equals("incorrecto")) avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_INCORRECTO);
        else if (opcion.equals("correcto")) avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.MOVIMIENTO_CORRECTO);
        else if (opcion.equals("aplausos")) avatar.reproduceEfectoSonido(VistaAvatar.EfectoSonido.EJERCICIO_SUPERADO);
    }

    public void levantaCejas(View view) {
        avatar.mueveCejas(VistaAvatar.MovimientoCejas.FRUNCIR);
    }

    public void parpadea(View view) {
        avatar.parpadea();
    }

    void configuraVoz() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            Spinner spinnerLecciones = (Spinner) findViewById(R.id.spinner_lecciones);
            if (spinnerLecciones == null) return;
            String opcion = spinnerLecciones.getSelectedItem().toString();
            if (opcion.equals("Presentación")) avatar.habla(R.raw.presentacion);
            else if (opcion.equals("Lección 1")) avatar.habla(R.raw.leccion1);
            else if (opcion.equals("Lección 2")) avatar.habla(R.raw.leccion2);
            else if (opcion.equals("Lección 3")) avatar.habla(R.raw.leccion3);
        } else {
            solicitarPermisoRecordAudio();
        }
    }

    void configuraMirada() {
        Spinner spinnerMiradas = (Spinner) findViewById(R.id.spiner_miradas);
        if (spinnerMiradas == null) return;
        String opcion = spinnerMiradas.getSelectedItem().toString();
        if (opcion.equals("centro")) avatar.mueveOjos(VistaAvatar.MovimientoOjos.CENTRO);
        else if (opcion.equals("izquierda"))
            avatar.mueveOjos(VistaAvatar.MovimientoOjos.IZQUIERDA);
        else if (opcion.equals("derecha"))
            avatar.mueveOjos(VistaAvatar.MovimientoOjos.DERECHA);
    }

    void solicitarPermisoRecordAudio() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            Snackbar snackbar = Snackbar.make(avatar, "Sin el permiso grabación de audio,\n"
                    + "no puedo mostrarte el avatar hablando.", Snackbar.LENGTH_INDEFINITE);
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(2);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(TareaA1Activity.this, new String[]{
                            Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configuraVoz();
            } else {
                Snackbar snackbar = Snackbar.make(avatar, "Sin el permiso grabación de audio,\n"
                        + "no puedo mostrarte el avatar hablando.", Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(2);
                snackbar.show();
            }
        }
    }
}
