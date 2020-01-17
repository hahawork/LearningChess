package com.uni.learningchess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import static com.uni.learningchess.BaseDatos.iBaseDatos.TBL_USUARIO_AVANCES;

public class BaseDatos extends SQLiteOpenHelper {

    Context context;

    public BaseDatos(Context context) {

        super(context, iBaseDatos.BK_DBNAME_EXTERNALSD, null, 1);

        this.context = context;
    }

    String TBLCATSECCIONES = "CREATE TABLE " + iBaseDatos.TBL_CAT_SECCIONES +
			" (idSecc integer not NULL PRIMARY KEY AUTOINCREMENT, " +
			"Descripcion  varchar(20))";

    String TBLUSUARIO = "CREATE TABLE " + iBaseDatos.TBL_USUARIO +
			" ( iduser INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"Nombre VARCHAR(50), " +
			"Edad integer, " +
			"FechaReg TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

    String TBLUSUARIOAVANCES = "CREATE TABLE " + TBL_USUARIO_AVANCES +
            " (idUA integer PRIMARY KEY AUTOINCREMENT, " +
            "iduser integer not NULL, " +
            "idSeccion integer not NULL, " +
            "CantAcertado integer , " +
            "CantFallado integer , " +
            "Fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TBLCATSECCIONES);
        db.execSQL(TBLUSUARIO);
        db.execSQL(TBLUSUARIOAVANCES);

        //inserta valores por defecto o iniciales
        db.execSQL("INSERT OR IGNORE INTO " + iBaseDatos.TBL_USUARIO + " (iduser, Nombre, Edad) VALUES ('1','Invitado','10');");

        db.execSQL("INSERT OR IGNORE INTO " + iBaseDatos.TBL_CAT_SECCIONES + " (idSecc, Descripcion) VALUES " +
                "('1','Tablero')," +
                "('2','Piezas')," +
                "('3','Notaciones')," +
                "('4','Jaque_Ahogado');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor obtenerDatos(String tabla) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(tabla, null, null, null, null, null, null);
            return cursor;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Cursor obtenerDatosRawQuery(String sql) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            return cursor;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Long insertarRegistro(String nombretabla, ContentValues values) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Long result = db.insert(nombretabla, null, values);
            db.close();

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return -1l;
        }
    }

    public void BorrarTodosLosRegistros(String nombreTabla) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "TRUNCATE " + nombreTabla;
            //db.delete(nombreTabla, null, null);
            db.execSQL(sql);

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void BorrarRegistroWhere(String nombreTabla, String where) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "DELETE FROM " + nombreTabla + " WHERE " + where;
            db.execSQL(sql);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int actualizarRegistro(String nombreTabla, ContentValues values, String where) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            int updated = db.update(nombreTabla, values, where, null);
            db.close();
            return updated;


        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void IncrementaAcierto(String idUsuario, String idSeccion) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            //verifica si ya existe para incrementar o inserta un nuevo.
            Cursor cursor = db.rawQuery(String.format("select * from %s where iduser = '%s' and idSeccion = '%s'",
                    TBL_USUARIO_AVANCES, idUsuario, idSeccion), null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int TotalAcierto = cursor.getInt(cursor.getColumnIndex("CantAcertado"));
                ContentValues values = new ContentValues();
                values.put("CantAcertado", (TotalAcierto + 1));
                actualizarRegistro(TBL_USUARIO_AVANCES, values, String.format("iduser = '%s' and idSeccion = '%s'", idUsuario, idSeccion));
            }else {
                ContentValues values = new ContentValues();
                values.put("iduser", idUsuario);
                values.put("idSeccion", idSeccion);
                values.put("CantAcertado", 1);
                values.put("CantFallado", 0);
                insertarRegistro(TBL_USUARIO_AVANCES,values);
            }

            Toast.makeText(context,"Incremeta acierto en la DB",Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void IncrementaEjercicioFalla(String idUsuario, String idSeccion) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            //verifica si ya existe para incrementar o inserta un nuevo.
            Cursor cursor = db.rawQuery(String.format("select * from %s where iduser = '%s' and idSeccion = '%s'",
                    TBL_USUARIO_AVANCES, idUsuario, idSeccion), null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int TotalFallas = cursor.getInt(cursor.getColumnIndex("CantFallado"));
                ContentValues values = new ContentValues();
                values.put("CantFallado", (TotalFallas + 1));
                actualizarRegistro(TBL_USUARIO_AVANCES, values, String.format("iduser = '%s' and idSeccion = '%s'", idUsuario, idSeccion));
            } else {
                ContentValues values = new ContentValues();
                values.put("iduser", idUsuario);
                values.put("idSeccion", idSeccion);
                values.put("CantAcertado", 0);
                values.put("CantFallado", 1);
                insertarRegistro(TBL_USUARIO_AVANCES,values);
            }

            Toast.makeText(context,"Incremeta falla en la DB",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface iBaseDatos {
        String TBL_CAT_SECCIONES = "cat_secciones";
        String TBL_USUARIO = "usuario";
        String TBL_USUARIO_AVANCES = "usuario_avances";

        String PATH_EXTERNAL_RESOURCE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LearningChess/";
        String PATH_EXTERNAL_DB = PATH_EXTERNAL_RESOURCE + "basedatos/";

        String BK_DBNAME_EXTERNALSD = "LearningChess.sqlite";
    }
}
