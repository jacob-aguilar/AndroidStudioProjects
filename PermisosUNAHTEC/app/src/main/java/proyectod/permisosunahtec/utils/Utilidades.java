package proyectod.permisosunahtec.utils;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.providers.ContractParaPermisos;


public class Utilidades {

    public static String URL ="http://192.168.0.19/";
    public static String TOKEN;

    public static final int COLUMNA_ID = 0;

    public static final int COLUMNA_ID_REMOTA = 1;

    public static final int COLUMNA_DEPTO = 2;





    /**

     * Determina si la aplicación corre en versiones superiores o iguales

     * a Android LOLLIPOP

     *

     * @return booleano de confirmación

     */

    public static boolean materialDesign() {

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

    }


    public static JSONObject deCursorAJSONObject(Cursor c) {

        JSONObject jObject = new JSONObject();

        String nombreDepto;

        nombreDepto = c.getString(COLUMNA_DEPTO);

        try {

            jObject.put(ContractParaDepartamentos.Columnas.NOMBREDEPTO, nombreDepto);



        } catch (JSONException e) {

            e.printStackTrace();

        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;

    }
    public static JSONObject deCursorAJSONObjectEmpleado(Cursor c) {

        JSONObject jObject = new JSONObject();



        try {

            jObject.put(ContractParaEmpleados.Columnas.ID, c.getString(c.getColumnIndex(ContractParaEmpleados.Columnas._ID)));
            jObject.put(ContractParaEmpleados.Columnas.NAME, c.getString(c.getColumnIndex(ContractParaEmpleados.Columnas.NAME)));
            jObject.put(ContractParaEmpleados.Columnas.EMAIL, c.getString(c.getColumnIndex(ContractParaEmpleados.Columnas.EMAIL)));
            jObject.put(ContractParaEmpleados.Columnas.PASSWORD, c.getString(c.getColumnIndex(ContractParaEmpleados.Columnas.PASSWORD)));
            jObject.put(ContractParaEmpleados.Columnas.PASSWORD_CONFIRMATION, c.getString(c.getColumnIndex(ContractParaEmpleados.Columnas.PASSWORD)));
            jObject.put(ContractParaEmpleados.Columnas.CODDEPTO, c.getString(c.getColumnIndex(ContractParaEmpleados.Columnas.CODDEPTO)));
            jObject.put(ContractParaEmpleados.Columnas.ROL, c.getString(c.getColumnIndex(ContractParaEmpleados.Columnas.ROL)));
            jObject.put(ContractParaEmpleados.Columnas.TOKENFIREBASE, c.getString(c.getColumnIndex(ContractParaEmpleados.Columnas.TOKENFIREBASE)));
            jObject.put(ContractParaEmpleados.Columnas.PICTURE, c.getString(c.getColumnIndex(ContractParaEmpleados.Columnas.PICTURE)));




        } catch (JSONException e) {

            e.printStackTrace();

        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;

    }
    public static JSONObject deCursorAJSONObjectEmpModificar(Cursor c) {

        JSONObject jObject = new JSONObject();



        try {

            jObject.put(ContractParaEmpleados.Columnas.ID, c.getString(c.getColumnIndex(ContractParaEmpleados.Columnas._ID)));
            jObject.put(ContractParaEmpleados.Columnas.NAME, c.getString(c.getColumnIndex(ContractParaEmpleados.Columnas.NAME)));
            jObject.put(ContractParaEmpleados.Columnas.EMAIL, c.getString(c.getColumnIndex(ContractParaEmpleados.Columnas.EMAIL)));
             jObject.put(ContractParaEmpleados.Columnas.ROL, c.getString(c.getColumnIndex(ContractParaEmpleados.Columnas.ROL)));
             jObject.put(ContractParaEmpleados.Columnas.PICTURE, c.getString(c.getColumnIndex(ContractParaEmpleados.Columnas.PICTURE)));




        } catch (JSONException e) {

            e.printStackTrace();

        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;

    }

    public static JSONObject deCursorAJSONObjectPermiso(Cursor c) {

        JSONObject jObject = new JSONObject();



        try {

            jObject.put(ContractParaPermisos.Columnas.ID, c.getString(c.getColumnIndex(ContractParaPermisos.Columnas.ID)));
            jObject.put(ContractParaPermisos.Columnas.TIPO_PERMISO, c.getString(c.getColumnIndex(ContractParaPermisos.Columnas.TIPO_PERMISO)));
            jObject.put(ContractParaPermisos.Columnas.ID_DEPTO, c.getString(c.getColumnIndex(ContractParaPermisos.Columnas.ID_DEPTO)));
            jObject.put(ContractParaPermisos.Columnas.ID_EMPLEADO, c.getString(c.getColumnIndex(ContractParaPermisos.Columnas.ID_EMPLEADO)));
            jObject.put(ContractParaPermisos.Columnas.FECHA_INICIO, c.getString(c.getColumnIndex(ContractParaPermisos.Columnas.FECHA_INICIO)));
            jObject.put(ContractParaPermisos.Columnas.FECHA_FIN, c.getString(c.getColumnIndex(ContractParaPermisos.Columnas.FECHA_FIN)));
            jObject.put(ContractParaPermisos.Columnas.OBSERVACIONES, c.getString(c.getColumnIndex(ContractParaPermisos.Columnas.OBSERVACIONES)));
            jObject.put(ContractParaPermisos.Columnas.PENDIENTE_ARCHIVADO, c.getString(c.getColumnIndex(ContractParaPermisos.Columnas.PENDIENTE_ARCHIVADO)));


        } catch (JSONException e) {

            e.printStackTrace();

        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;

    }

    public static JSONObject deCursorAJSONObjectModificarPermiso(Cursor c) {

        JSONObject jObject = new JSONObject();



        try {

            jObject.put(ContractParaPermisos.Columnas.ID, c.getString(c.getColumnIndex(ContractParaPermisos.Columnas._ID)));
            jObject.put(ContractParaPermisos.Columnas.TIPO_PERMISO, c.getString(c.getColumnIndex(ContractParaPermisos.Columnas.TIPO_PERMISO)));
            jObject.put(ContractParaPermisos.Columnas.FECHA_INICIO, c.getString(c.getColumnIndex(ContractParaPermisos.Columnas.FECHA_INICIO)));
            jObject.put(ContractParaPermisos.Columnas.FECHA_FIN, c.getString(c.getColumnIndex(ContractParaPermisos.Columnas.FECHA_FIN)));
            jObject.put(ContractParaPermisos.Columnas.OBSERVACIONES, c.getString(c.getColumnIndex(ContractParaPermisos.Columnas.OBSERVACIONES)));




        } catch (JSONException e) {

            e.printStackTrace();

        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;

    }


}
