package com.uni.learningchess;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class userManejarUsuarios extends AppCompatActivity {

    public enum GUARDAR {GUARDAR, EDITAR}

    BaseDatos BD;
    MetodosGenerales MG;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    private VistaAvatar avatar;
    Typeface fuente, fuente1;
    String stIdUsuarioSeleccionado = "", stNombreUsuarioSeleccionado = "", stEdadUsuarioSeleccionado = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manejar_usuarios);

        BD = new BaseDatos(this);
        MG = new MetodosGenerales(this);
        setting = PreferenceManager.getDefaultSharedPreferences(this);

        avatar = findViewById(R.id.vistaAvatar);
        avatar.setActividad(this);

        fuente = Typeface.createFromAsset(getAssets(), "fonts/BalooPaaji-Regular.ttf");
        fuente1 = Typeface.createFromAsset(getAssets(), "fonts/Gorditas-Bold.ttf");
        TextView textoTituloApp = findViewById(R.id.textoTituloApp);
        textoTituloApp.setTypeface(fuente1);
        getData();


    }

    public void clickBotonUsuario(View view) {
        if (view == findViewById(R.id.botonNuevoUsuario)) {

            DialogoGuardarUsuario(GUARDAR.GUARDAR, "0");
        }

        if (view == findViewById(R.id.botonEditarUsuario)) {

            if (stIdUsuarioSeleccionado.length() > 0) {
                if (!stIdUsuarioSeleccionado.equals("1")) {
                    DialogoGuardarUsuario(GUARDAR.EDITAR, stIdUsuarioSeleccionado, stNombreUsuarioSeleccionado, stEdadUsuarioSeleccionado);
                } else {
                    MG.MostrarAlertaError("Editar", "El usuario Invitado no se debe modificar");
                }

            } else {
                MG.MostrarAlertaError("Editar", "Primero selecciona un usuario");
            }
        }

        if (view == findViewById(R.id.botonBorrarUsuario)) {
            borrarUsuario();
        }
    }

    private void getData() {
        try {
            Cursor cdata = BD.obtenerDatos(BaseDatos.iBaseDatos.TBL_USUARIO);

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

                    String usuarioActual = setting.getString("spIdUsurioActual", "1");
                    if (usuarioActual.equals(id)) {
                        rowView.setBackgroundResource(R.color.colorPrimary);
                        tvid.setText("👉  " + id);
                    }
                    rowView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            stIdUsuarioSeleccionado = id;
                            stNombreUsuarioSeleccionado = nombre;
                            stEdadUsuarioSeleccionado = edad;

                            editor = setting.edit();
                            editor.putString("spIdUsurioActual", id);
                            editor.putString("spNombreUsuarioActual", nombre);
                            editor.putString("spEdadUsuarioActual", edad);
                            editor.commit();
                            Toast.makeText(userManejarUsuarios.this, String.format("%s es ahora el usuario seleccionado", nombre), Toast.LENGTH_LONG).show();

                            getData();
                        }
                    });
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

    /**
     * Muestra un dialogo para guardar un nuevo usuario o editarlo
     *
     * @param guardar Define el tipo de guardado (GUARDAR o EDITAR)
     * @param Usuario Datos del usuario (0=IdUsuario, 1=Nombre, 2= Edad)
     */
    public void DialogoGuardarUsuario(final GUARDAR guardar, final String... Usuario) {

        final Dialog dialogIntro = new Dialog(this);
        dialogIntro.setCancelable(false);
        dialogIntro.setCanceledOnTouchOutside(false);
        dialogIntro.setContentView(R.layout.dialog_guardar_editar_usuario);

        ((TextView) dialogIntro.findViewById(R.id.textoNombreGuardarUsuario)).setTypeface(fuente);
        ((TextView) dialogIntro.findViewById(R.id.textoEdadGuardarUsuario)).setTypeface(fuente);

        final EditText etNombre = dialogIntro.findViewById(R.id.etNombreGuardarUsuario);
        final EditText etEdad = dialogIntro.findViewById(R.id.etEdadGuardarUsuario);
        Button botonGuardar = dialogIntro.findViewById(R.id.botonGuardarUsuario);
        Button botonCancelar = dialogIntro.findViewById(R.id.botonCancelar);

        //si esta en modo editar y hay 3 datos del usuario
        if (guardar == GUARDAR.EDITAR && Usuario.length == 3) {
            etNombre.setText(Usuario[1]);
            etEdad.setText(Usuario[2]);
        }

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(etNombre.getText().toString().replace(" ", "")) &&
                        !TextUtils.isEmpty(etEdad.getText().toString().replace(" ", ""))) {

                    if (guardar == GUARDAR.GUARDAR) {

                        if (!BD.ExisteRegistro(BaseDatos.iBaseDatos.TBL_USUARIO, "Nombre", etNombre.getText().toString())) {
                            ContentValues values = new ContentValues();
                            values.put("iduser", (byte[]) null);
                            values.put("Nombre", etNombre.getText().toString());
                            values.put("Edad", etEdad.getText().toString());
                            Long idInsert = BD.insertarRegistro(BaseDatos.iBaseDatos.TBL_USUARIO, values);

                            if (idInsert != -1) {
                                editor = setting.edit();
                                editor.putString("spIdUsurioActual", String.valueOf(idInsert));
                                editor.putString("spNombreUsuarioActual", etNombre.getText().toString());
                                editor.putString("spEdadUsuarioActual", etEdad.getText().toString());
                                editor.commit();
                                dialogIntro.dismiss();
                                getData();
                            } else {
                                MG.MostrarAlertaError("No se guardó", "Error al guardar los datos");
                            }
                        } else {
                            MG.MostrarAlertaError("Alerta", "Este nombre ya existe");
                        }

                    } else if (guardar == GUARDAR.EDITAR) {

                        ContentValues values = new ContentValues();
                        values.put("Nombre", etNombre.getText().toString());
                        values.put("Edad", etEdad.getText().toString());
                        int idInsert = BD.actualizarRegistro(BaseDatos.iBaseDatos.TBL_USUARIO, values, "iduser = " + Usuario[0]);

                        if (idInsert > 0) {

                            editor = setting.edit();
                            editor.putString("spNombreUsuarioActual", etNombre.getText().toString());
                            editor.putString("spEdadUsuarioActual", etEdad.getText().toString());
                            editor.commit();
                            dialogIntro.dismiss();
                            getData();
                        } else {
                            MG.MostrarAlertaError("No se actualizó", "Error al actualzar los datos");
                        }
                    }

                } else {
                    MG.MostrarAlertaError("No permitido", "Debes llenar los dos campos de texto");
                }
            }
        });

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogIntro.cancel();
            }
        });

        dialogIntro.show();
    }

    private void borrarUsuario() {
        if (stIdUsuarioSeleccionado.length() > 0) {
            if (!stIdUsuarioSeleccionado.equals("1")) {

                new AlertDialog.Builder(this)
                        .setTitle("Borrar usuario")
                        .setMessage("Esta acción borrará definitivamente al usuario '" + stNombreUsuarioSeleccionado + "' y no se puede revertir.\n\n" +
                                "¿Estas seguro de borrar el usuario?")
                        .setPositiveButton("SI, BORRAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BD.BorrarRegistroWhere(BaseDatos.iBaseDatos.TBL_USUARIO, "iduser = " + stIdUsuarioSeleccionado);

                                stIdUsuarioSeleccionado = "";
                                stNombreUsuarioSeleccionado = "";
                                stEdadUsuarioSeleccionado = "";
                                editor = setting.edit();
                                editor.putString("spIdUsurioActual", "1");
                                editor.putString("spNombreUsuarioActual", "Invitado");
                                editor.putString("spEdadUsuarioActual", "0");
                                editor.commit();

                                getData();
                            }
                        })
                        .setNegativeButton("NO, SALIR", null)
                        .show();
            } else {
                MG.MostrarAlertaError("Borrar", "El usuario Invitado no se debe borrar");
            }
        } else {
            MG.MostrarAlertaError("Borrar", "Primero selecciona un usuario");
        }
    }
}
