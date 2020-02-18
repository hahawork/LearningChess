package com.uni.learningchess;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class userProgreso extends AppCompatActivity {

	BaseDatos BD;
	MetodosGenerales MG;
	SharedPreferences setting;
	SharedPreferences.Editor editor;
	private VistaAvatar avatar;
	Typeface fuente, fuente1;
	String stIdUsuarioSeleccionado = "", stNombreUsuarioSeleccionado = "", stEdadUsuarioSeleccionado = "";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_progreso);

		BD = new BaseDatos(this);
		MG = new MetodosGenerales(this);
		setting = PreferenceManager.getDefaultSharedPreferences(this);

		avatar = findViewById(R.id.vistaAvatar);
		avatar.setActividad(this);

		fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
		fuente1 = Typeface.createFromAsset(getAssets(), "fonts/Gorditas-Bold.ttf");
		TextView textoTituloApp = findViewById(R.id.textoTituloApp);
		textoTituloApp.setTypeface(fuente1);

		TextView tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
		tvNombreUsuario.setText("Usuario: "+
				setting.getString("spNombreUsuarioActual","Invitado.") +"\nEdad: "+
				setting.getString("spEdadUsuarioActual","5") +" años");
		getData();
	}

	public void getData(){
		try {
			Cursor cdata = BD.obtenerDatosRawQuery("SELECT *  FROM " + BaseDatos.iBaseDatos.TBL_USUARIO_AVANCES
					+ " AS T1 INNER JOIN " + BaseDatos.iBaseDatos.TBL_CAT_SECCIONES + " AS T2 ON T1.idSeccion = T2.idSecc	INNER JOIN " +
					BaseDatos.iBaseDatos.TBL_USUARIO + " AS T3 ON  T1.iduser = T3.iduser WHERE T1.iduser = " + setting.getString("spIdUsurioActual", "1"));

			if (cdata.getCount() > 0) {

				//obtiene los datos de los usuarios.
				LinearLayout llDetalle = findViewById(R.id.llDatos_UP);
				llDetalle.removeAllViews();

				for (cdata.moveToFirst(); !cdata.isAfterLast(); cdata.moveToNext()) {

					LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View rowView = inflater.inflate(R.layout.custom_filas_progreso_secciones, null);

					TextView tvSeccion = rowView.findViewById(R.id.tvSeccionDDBB);
					TextView tvAciertos = rowView.findViewById(R.id.tvCantAciertosDDBB);
					TextView tvFallos = rowView.findViewById(R.id.tvCantFallosDDB);
					TextView tvEvaluacion = rowView.findViewById(R.id.tvEvaluacion);

					final String seccion = cdata.getString(cdata.getColumnIndex("Descripcion"));
					final int acierto = cdata.getInt(cdata.getColumnIndex("CantAcertado"));
					final int fallos = cdata.getInt(cdata.getColumnIndex("CantFallado"));
					String evaluacion ="0%";
					if (acierto>0) {  // esto se hace para evitar division por cero
						evaluacion = (acierto * 100) / (acierto + fallos) + "%";
					}

					tvSeccion.setText(seccion);
					tvAciertos.setText(String.valueOf(acierto));
					tvFallos.setText(String.valueOf(fallos));
					tvEvaluacion.setText(evaluacion);

					// Add the new row before the add field button.
					llDetalle.addView(rowView);
				}
			} else {
				Toast.makeText(this, "no hay registros aún.", Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
			MG.MostrarAlertaError("Error", "Ha ocurrido un error: " + e.getMessage() + " en: " + e.getLocalizedMessage());
		}
	}

	public void LimpiarDatosUsuario(View view) {
		new AlertDialog.Builder(this)
				.setTitle("Borrar datos del usuario")
				.setIcon(R.drawable.ic_delete)
				.setMessage("Esta acción borrará definitivamente los datos  y no se puede revertir.\n\n" +
						"¿Estas seguro de borrar?")
				.setPositiveButton("SI, BORRAR", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						BD.BorrarRegistroWhere(BaseDatos.iBaseDatos.TBL_USUARIO_AVANCES, "iduser = " + stIdUsuarioSeleccionado);
						getData();
					}
				})
				.setNegativeButton("NO, SALIR", null)
				.show();
	}
}
