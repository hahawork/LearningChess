package com.uni.learningchess;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProgresoPuntajeUsuarios extends AppCompatActivity {

    BaseDatos baseDatos;
    MetodosGenerales MG;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    private VistaAvatar avatar;
    Typeface fuente, fuente1;
    String idUsuario="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manejar_usuarios);

        baseDatos = new BaseDatos(this);
        MG = new MetodosGenerales(this);
        setting = PreferenceManager.getDefaultSharedPreferences(this);
        idUsuario = setting.getString("spIdUsurioActual","1");

        avatar = findViewById(R.id.vistaAvatar);
        avatar.setActividad(this);

        fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        fuente1 = Typeface.createFromAsset(getAssets(), "fonts/Gorditas-Bold.ttf");
        TextView textoTituloApp = findViewById(R.id.textoTituloApp);
        textoTituloApp.setTypeface(fuente1);
    }

    private void getData() {
        try {
            String sql = String.format("select from %s as t1 inner join %s as t2 on t1. = t2. inner join %s as t3 on t1. = t3. where t1. '' = and t1. = ''",
                    BaseDatos.iBaseDatos.TBL_USUARIO_AVANCES, BaseDatos.iBaseDatos.TBL_USUARIO, BaseDatos.iBaseDatos.TBL_CAT_SECCIONES);
            Cursor cdata = baseDatos.obtenerDatosRawQuery(sql);

            if (cdata.getCount() > 0) {

                //obtiene los datos de los usuarios.
                LinearLayout llDetalle = findViewById(R.id.llListaDeUsuarios);
                llDetalle.removeAllViews();

                for (cdata.moveToFirst(); !cdata.isAfterLast(); cdata.moveToNext()) {

                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View rowView = inflater.inflate(R.layout.custom_filas_de_usuarios, null);

                    TextView tvid = rowView.findViewById(R.id.tvId);
                    TextView tvnombre = rowView.findViewById(R.id.tvNombreUsuario);
                    TextView tvfecha = rowView.findViewById(R.id.tvFechaRegistro);

                    final String id = cdata.getString(cdata.getColumnIndex("iduser"));
                    final String nombre = cdata.getString(cdata.getColumnIndex("Nombre"));
                    final String edad = cdata.getString(cdata.getColumnIndex("Edad"));
                    String fechaReg = cdata.getString(cdata.getColumnIndex("FechaReg"));

                    tvid.setText(id);
                    tvnombre.setText(nombre);
                    tvfecha.setText(fechaReg.substring(0, 10));

                    // Add the new row before the add field button.
                    llDetalle.addView(rowView);
                }
            } else {
                Toast.makeText(this, "no hay registros aún", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            MG.MostrarAlertaError("Error", "Ha ocurrido un error: " + e.getMessage() + " en: " + e.getLocalizedMessage());
        }
    }

    public void clickBotonUsuario(View view) {
    }
}
