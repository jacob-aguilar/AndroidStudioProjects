package proyectod.permisosunahtec.syncs;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import proyectod.permisosunahtec.R;
import proyectod.permisosunahtec.pojo.PermisosGetYSet;
import proyectod.permisosunahtec.providers.ContractParaPermisos;
import proyectod.permisosunahtec.providers.OperacionesBaseDatos;
import proyectod.permisosunahtec.utils.Utilidades;
 import proyectod.permisosunahtec.web.VolleySingleton;


/**
 * Maneja la transferencia de datos entre el servidor y el cliente
 */
public class SyncAdapterPermisos extends AbstractThreadedSyncAdapter {
    private static final String TAG = proyectod.permisosunahtec.syncs.SyncAdapterPermisos.class.getSimpleName();

    ContentResolver resolver;
    private Gson gson = new Gson();
    PermisosGetYSet e;
    private JSONObject json_data;
    private String tokenFirebaseTabla;
    /**
     * Proyección para las consultas
     */
    private static final String[] PROJECTION = new String[]{
            ContractParaPermisos.Columnas.ID,
            ContractParaPermisos.Columnas.ID_REMOTA,
            ContractParaPermisos.Columnas.TIPO_PERMISO,
            ContractParaPermisos.Columnas.ID_DEPTO,
            ContractParaPermisos.Columnas.ID_EMPLEADO,
            ContractParaPermisos.Columnas.FECHA_INICIO,
            ContractParaPermisos.Columnas.FECHA_FIN,
            ContractParaPermisos.Columnas.OBSERVACIONES,
            ContractParaPermisos.Columnas.PENDIENTE_ELIMINACION,
            ContractParaPermisos.Columnas.PENDIENTE_ARCHIVADO,

    };

    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNAID = 0;
    public static final int COLUMNA_ID_REMOTA = 1;
    public static final int COLUMNA_TIPOPERMISO = 2;
    public static final int COLUMNA_ID_DEPTO = 3;
    public static final int COLUMNA_ID_EMPLEADO = 4;
    public static final int COLUMNA_FECHA_INICIO = 5;
    public static final int COLUMNA_FECHA_FIN = 6;
    public static final int COLUMNA_OBSERVACIONES = 7;
    public static final int COLUMNA_ELIMINADO = 8;
    public static final int COLUMNA_ARCHIVADO = 9;
    private String DATA_URL;
    private String[] justin;
    int idRemotaVolley;

    private JSONArray lista;
    private String estadoNotificacion;
    private  String idEmpleado;
    private  String tipoPermiso;
    private OperacionesBaseDatos datos;
    private Cursor cursorList;
    private String nombreEmpleado;
    private String nombreAdministrador;
    private String idPreference;
    private String rolPreference;
    private String tokenAdministrador;

    public SyncAdapterPermisos(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        resolver = context.getContentResolver();
    }

    /**
     * Constructor para mantener compatibilidad en versiones inferiores a 3.0
     */
    public SyncAdapterPermisos(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        resolver = context.getContentResolver();

    }

    public static void inicializarSyncAdapter(Context context) {

        obtenerCuentaASincronizar(context);

    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              final SyncResult syncResult) {

        Log.i(TAG, "onPerformSync()...");
        cargarPreferencias();
        datos = OperacionesBaseDatos
                .obtenerInstancia(getContext());

        boolean soloSubida = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);

        if (!soloSubida) {

            realizarSincronizacionLocal(syncResult);
        } else {


            realizarSincronizacionRemota();
        }
    }

    private void realizarSincronizacionLocal(final SyncResult syncResult) {
        Log.i(TAG, "Actualizando el cliente.");
        String URL;

        if(rolPreference.equals("admin")){
            URL =Utilidades.URL + "api/auth/allPermisos/";
        }else {
            URL =Utilidades.URL + "api/auth/listaPermiso/"+idPreference;
            Log.i(TAG,""+idPreference);
        }

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        URL,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, "respondio lista."+response);
                                procesarRespuestaGet(response, syncResult);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.i(TAG, " no responce"+error);
                            }
                        }
                ) {

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", "Bearer" + " " + tokenAdministrador);


                        return params;
                    }
                }
        );
    }




    /**
     * Procesa la respuesta del servidor al pedir que se retornen todos los gastos.
     *
     * @param response   Respuesta en formato Json
     * @param syncResult Registro de resultados de sincronización
     */


    private void procesarRespuestaGet(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    actualizarDatosLocales(response, syncResult);
                    break;
                case "2": // FALLIDO
                    String mensaje = response.getString("mensaje");
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void realizarSincronizacionRemota() {

        String Url;
        Log.i(TAG, "Actualizando el servidor...");

        iniciarActualizacion();

        Cursor c = obtenerRegistrosSucios();

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios.");

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(COLUMNAID);
              final  int  idRemotaVolley =c.getInt(COLUMNA_ID_REMOTA);
                final int estadoEliminacion =c.getInt(COLUMNA_ELIMINADO);
             final  String  idEmpleado = c.getString(c.getColumnIndex("idEmpleado"));
              final  String  tipoPermiso = c.getString(c.getColumnIndex("tipoPermiso"));
              final  int archivado = c.getInt(c.getColumnIndex("archivado"));


              Log.i(TAG,"archibado   "+archivado);



                cursorList = datos.obtenerJoinsDePermisosIdUSer(String.valueOf(idLocal));

                if (cursorList.moveToNext()){
                    nombreEmpleado =cursorList.getString(cursorList.getColumnIndex("name"));


                }



                if(estadoEliminacion ==1){

                    Url = Utilidades.URL + "api/auth/eliminarPermiso/"+ idRemotaVolley;
                }else {
                    if (idRemotaVolley == 0) {
                        Url = Utilidades.URL + "api/auth/permiso";
                    } else {

                        Url = Utilidades.URL + "api/auth/modificarPermiso/" + idRemotaVolley;
                    }
                }
                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,

                                Url,
                                Utilidades.deCursorAJSONObjectPermiso(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse( final JSONObject response) {



                                        if(estadoEliminacion ==1){
                                            Log.i("CHACON",""+idLocal);


                                            String[] selectionArgs = new String[]{String.valueOf(idLocal)};
                                            getContext().getContentResolver().delete(ContractParaPermisos.CONTENT_URI, "id =?",selectionArgs);
                                               Log.i(TAG,"eliminado de la local");

                                            final String  estadoDeSubida ="eliminó";
                                            invocarTokenFirebaseAdmin(nombreAdministrador,estadoDeSubida,nombreEmpleado);

                                            SyncAdapterPermisos.sincronizarAhora(getContext(), false);

                                        }else {
                                            if (idRemotaVolley ==0){


                                                   if(archivado==1) {

                                                   }else {
                                                       final String estadoDeSubida = "agregó";
                                                       invocarTokenFirebaseAdmin(nombreAdministrador, estadoDeSubida, nombreEmpleado);
                                                       invocarTokenFirebase(idEmpleado, nombreEmpleado, tipoPermiso);
                                                       Log.i(TAG, "" + nombreAdministrador + " " + estadoDeSubida + " " + nombreEmpleado);
                                                   }


                                            }else {
                                                if(archivado==1) {

                                                }else {
                                                    final String estadoDeSubida = "modificó";
                                                    invocarTokenFirebaseAdmin(nombreAdministrador, estadoDeSubida, nombreEmpleado);
                                                }
                                            }


                                            procesarRespuestaInsert(response, idLocal,archivado);
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }

                        )  {

                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", "Bearer" + " " + tokenAdministrador);


                                return params;
                            }



                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();
                            }
                        }
                );
            }


        } else {
            Log.i(TAG, "No se requiere sincronización");
            SyncAdapterPermisos.sincronizarAhora(getContext(), false);
        }
        c.close();
    }



    /**
     * Obtiene el registro que se acaba de marcar como "pendiente por sincronizar" y
     * con "estado de sincronización"
     *
     * @return Cursor con el registro.
     */
    private Cursor obtenerRegistrosSucios() {
        Uri uri = ContractParaPermisos.CONTENT_URI;
        String selection = ContractParaPermisos.Columnas.PENDIENTE_INSERCION + "=? AND "
                + ContractParaPermisos.Columnas.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", ContractParaPermisos.ESTADO_SYNC + ""};

        return resolver.query(uri, PROJECTION, selection, selectionArgs, null);
    }


    /**
     * Cambia a estado "de sincronización" el registro que se acaba de insertar localmente
     */
    private void iniciarActualizacion() {
        Uri uri = ContractParaPermisos.CONTENT_URI;
        String selection = ContractParaPermisos.Columnas.PENDIENTE_INSERCION + "=? AND "
                + ContractParaPermisos.Columnas.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", ContractParaPermisos.ESTADO_OK + ""};

        ContentValues v = new ContentValues();
        v.put(ContractParaPermisos.Columnas.ESTADO, ContractParaPermisos.ESTADO_SYNC);

        try {
            int results = resolver.update(uri, v, selection, selectionArgs);
            Log.i(TAG, "Registros puestos en cola de inserción:" + results);
        }catch (Exception e){
            Log.i(TAG,""+e);
        }


    }

    /**
     * Limpia el registro que se sincronizó y le asigna la nueva id remota proveida
     * por el servidor
     *
     * @param idRemota id remota
     */private void finalizarActualizacion( final String idRemota,final int idLocal, final int archivado) {
        Uri uri = ContractParaPermisos.CONTENT_URI;
        String selection = ContractParaPermisos.Columnas.ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(idLocal)};

        ContentValues v = new ContentValues();
        v.put(ContractParaPermisos.Columnas.PENDIENTE_INSERCION, "0");
        v.put(ContractParaPermisos.Columnas.ESTADO, ContractParaPermisos.ESTADO_OK);
        v.put(ContractParaPermisos.Columnas.ID_REMOTA, idRemota);



        resolver.update(uri, v, selection, selectionArgs);
        Log.i(TAG, "cheque todo");

        if(archivado==1) {

             getContext().getContentResolver().delete(ContractParaPermisos.CONTENT_URI, "id =?", selectionArgs);
        }

        SyncAdapterPermisos.sincronizarAhora(getContext(), false);


    }

    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en formato Json
     */
    public void procesarRespuestaInsert( final JSONObject response,final int idLocal ,final int archivado) {
        Log.i(TAG,"jsutin");

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");
            // Obtener identificador del nuevo registro creado en el servidor
            String idRemota = response.getString("id");




            switch (estado) {
                case "1":
                    Log.i(TAG, "averrr"+idRemota +" ,  "+idLocal);
                    finalizarActualizacion(idRemota, idLocal ,archivado);
                    break;

                case "2":
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarDatosLocales(JSONObject response, SyncResult syncResult) {

        JSONArray permisos = null;

        try {
            // Obtener array "gastos"
            permisos = response.getJSONArray("gastos");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        PermisosGetYSet[] res = gson.fromJson(permisos != null ? permisos.toString() : null, PermisosGetYSet[].class);
        List<PermisosGetYSet> data = Arrays.asList(res);




        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();


        // Tabla hash para recibir las entradas entrantes
        HashMap<String, PermisosGetYSet> expenseMap = new HashMap<String, PermisosGetYSet>();
        int i=0;
        for (PermisosGetYSet e : data) {

            expenseMap.put(e.id, e);




        }

        // Consultar registros remotos actuales
        Uri uri = ContractParaPermisos.CONTENT_URI;
        String select = ContractParaPermisos.Columnas.ID_REMOTA;
        Cursor c = resolver.query(uri, PROJECTION, select, null, null);
        assert c != null;

     //    Log.i(TAG, "Se encontraron " + c.getCount() + " registros localess.");

        // Encontrar datos obsoletos
        String id =null;
        String tipoPermiso;
        String idDepartamento;
        String idEmpleado;
        String fechaInicio;
        String fechaFin;
        String observaciones;



        while (c.moveToNext()) {

            syncResult.stats.numEntries++;

            id = c.getString(COLUMNA_ID_REMOTA);
            tipoPermiso = c.getString(COLUMNA_TIPOPERMISO);
            idDepartamento = c.getString(COLUMNA_ID_DEPTO);
            idEmpleado = c.getString(COLUMNA_ID_EMPLEADO);
            fechaInicio = c.getString(COLUMNA_FECHA_INICIO);
            fechaFin = c.getString(COLUMNA_FECHA_FIN);
            observaciones = c.getString(COLUMNA_OBSERVACIONES);


            PermisosGetYSet match = expenseMap.get(id);




            if (match != null) {
             boolean b =true;
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);



                Uri existingUri = ContractParaPermisos.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                 if(match.tipoPermiso !=null && match.tipoPermiso.equals(tipoPermiso)){
                     b = false;
                 }
                boolean b1 = match.idDepartamento != null && !match.idDepartamento.equals(idDepartamento);
                boolean b2 = match.idEmpleado != null && !match.idEmpleado.equals(idEmpleado);
                boolean b3 = match.fechaInicio != null && !match.fechaInicio.equals(fechaInicio);
                boolean b4 = match.fechaInicio != null && !match.fechaInicio.equals(fechaInicio);
                boolean b5 = match.fechaFin != null && !match.fechaFin.equals(fechaFin);
                boolean b6 = match.observaciones != null && !match.observaciones.equals(observaciones);
                 if (b || b1 || b2 || b3 ||b4||b5||b6  ) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContractParaPermisos.Columnas.TIPO_PERMISO, match.tipoPermiso)
                            .withValue(ContractParaPermisos.Columnas.ID_DEPTO, match.idDepartamento)
                            .withValue(ContractParaPermisos.Columnas.ID_EMPLEADO, match.idEmpleado)
                            .withValue(ContractParaPermisos.Columnas.FECHA_INICIO, match.fechaInicio)
                            .withValue(ContractParaPermisos.Columnas.FECHA_FIN, match.fechaFin)
                            .withValue(ContractParaPermisos.Columnas.OBSERVACIONES, match.observaciones)

                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }



            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractParaPermisos.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (PermisosGetYSet e : expenseMap.values()) {


            Log.i(TAG, "Programando inserción de: " + e.tipoPermiso);
            ops.add(ContentProviderOperation.newInsert(ContractParaPermisos.CONTENT_URI)
                    .withValue(ContractParaPermisos.Columnas.ID_REMOTA, e.id)
                    .withValue(ContractParaPermisos.Columnas.TIPO_PERMISO, e.tipoPermiso)
                    .withValue(ContractParaPermisos.Columnas.ID_DEPTO, e.idDepartamento)
                    .withValue(ContractParaPermisos.Columnas.ID_EMPLEADO, e.idEmpleado)
                    .withValue(ContractParaPermisos.Columnas.FECHA_INICIO, e.fechaInicio)
                    .withValue(ContractParaPermisos.Columnas.FECHA_FIN, e.fechaFin)
                    .withValue(ContractParaPermisos.Columnas.OBSERVACIONES, e.observaciones)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(ContractParaPermisos.AUTHORITY, ops);
            } catch (Exception a) {
                Log.i(TAG, "no inserto"+a);
            }
            resolver.notifyChange(
                    ContractParaPermisos.CONTENT_URI,
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    /**
     * Inicia manualmente la sincronización
     *
     * @param context    Contexto para crear la petición de sincronización
     * @param onlyUpload Usa true para sincronizar el servidor o false para sincronizar el cliente
     */
    public static void sincronizarAhora(Context context, boolean onlyUpload) {
        Log.i(TAG, "Realizando petición de sincronización manual.");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        if (onlyUpload)
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        ContentResolver.requestSync(obtenerCuentaASincronizar(context),
                context.getString(R.string.provider_authority_permisos), bundle);



    }


    /**
     * Crea u obtiene una cuenta existente
     *
     * @param context Contexto para acceder al administrador de cuentas
     * @return cuenta auxiliar.
     */
    public static Account obtenerCuentaASincronizar(Context context) {
        // Obtener instancia del administrador de cuentas
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Crear cuenta por defecto
        Account newAccount = new Account(
                context.getString(R.string.app_name), "com.herprogramacion.permisos_unah.account");

        // Comprobar existencia de la cuenta
        if (null == accountManager.getPassword(newAccount)) {

            // Añadir la cuenta al account manager sin password y sin datos de usuario
            if (!accountManager.addAccountExplicitly(newAccount, "", null))
                return null;

        }
        Log.i(TAG, "Cuenta de usuario obtenida.");

        return newAccount;
    }
    private void invocarTokenFirebase(final String idEmpleado , final String nombreEmpleado , final String tipoPermiso) {


        String   DATA_URL =  Utilidades.URL+"api/auth/listaEmpleadosId2/"+idEmpleado;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                DATA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        showListView(response,idEmpleado,nombreEmpleado,tipoPermiso);




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG," no respondio la invocacion");





            }
        })    {

            @Override
            public Map<String,String> getHeaders(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer"+" "+Utilidades.TOKEN);




                return params;}};

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }



    private void showListView(JSONObject obj, final String idEmpleado, final  String nombreEmpleado ,final  String tipoPermiso) {
        try{






            lista = obj.optJSONArray("users");
            for (int i = 0; i < lista.length(); i++) {
                Log.i(TAG,"entra al for");
                json_data = lista.getJSONObject(i);


                tokenFirebaseTabla = json_data.getString("tokenFirebase");


                if(tokenFirebaseTabla.equals("null") ){

                    estadoNotificacion ="pendiente";

                    registrarNotificacion(idEmpleado,nombreEmpleado,tipoPermiso);
                }else {

                     mandarNotification2(nombreEmpleado,tipoPermiso);
                }



            }






        } catch (Exception ex) {

        } finally {
        }
    }
    private void registrarNotificacion( final String idEmpleado, final String nombreEmpleado , final String tipoPermiso) {



     String  REGISTER_URL = Utilidades.URL + "api/auth/signupNotificacion";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG,"registro la notificacion");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.toString().equals("com.android.volley.TimeoutError")) {
                 } else {

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();



                params.put("idEmpleado", idEmpleado);
                params.put("title", nombreEmpleado+" Se le agregó un nuevo permiso. ");
                params.put("body", "Permiso de: "+ tipoPermiso);


                params.put("estado", estadoNotificacion);



                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer" + " " + Utilidades.TOKEN);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }
    public void mandarNotification2(final  String nombreEmpleado,final  String tipoPermiso)

    {
        JSONObject json = new JSONObject();
        try {
            JSONObject userData = new JSONObject();
            userData.put("title", nombreEmpleado+" Se le agregó un nuevo permiso. ");
            userData.put("body","Permiso de: "+ tipoPermiso);
            userData.put("sound", "default");
            json.put("to", tokenFirebaseTabla);
            json.put("data", userData);


        } catch (JSONException e) {
            e.printStackTrace();
         }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                  Log.i(TAG,"mando 2");



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {




            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                String autorizacionFirebase ="AAAAFXKYB2Y:APA91bGLoCcd89yFHdTds-U7VFMpcwEX8w3rt-l3jZabwi9QKaEZBUju8lIH8VLmANUMw8ciLAuYlqnt-32QzHcIP7dHmeu2klw-MmTPYSGk-X44J6mt7nC3d0RosnQLQXnwEW8WBm3m";

                params.put("Authorization", "Key=" + " " + autorizacionFirebase);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
    private void invocarTokenFirebaseAdmin(final String nombreAdministrador,final String estadoSubida ,final  String nombreEmpleado) {


        String   DATA_URL =  Utilidades.URL+"api/auth/getAdmin";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                DATA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,"respondio la invocacion");
                        showListViewAdmin(response ,nombreAdministrador,estadoSubida,nombreEmpleado );




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {





            }
        })    {

            @Override
            public Map<String,String> getHeaders(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer"+" "+Utilidades.TOKEN);




                return params;}};

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }



    private void showListViewAdmin(JSONObject obj ,final String nombreAdministrador,final String estadoSubida,final String nombreEmpleado) {
        try{





            lista = obj.optJSONArray("users");
            for (int i = 0; i < lista.length(); i++) {

                json_data = lista.getJSONObject(i);

                Log.i(TAG ,"jjsjsjsjjs");
                tokenFirebaseTabla = json_data.getString("tokenFirebase");
                idEmpleado= json_data.getString("id");


                if(tokenFirebaseTabla.equals("null")){

                    estadoNotificacion ="pendiente";

                    registrarNotificacionAdmin(nombreAdministrador,idEmpleado,estadoSubida,nombreEmpleado);
                }else {

                    mandarNotificationAdmin(nombreAdministrador,estadoSubida,nombreEmpleado );
                }



            }






        } catch (Exception ex) {

        } finally {
        }
    }
    private void registrarNotificacionAdmin( final String nombreAdministrador , final String idEmpleado,final String estadoSubida,final String nombreEmpleado) {



        String  REGISTER_URL = Utilidades.URL + "api/auth/signupNotificacion";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG,"registro la notificacion");

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();



                params.put("idEmpleado", idEmpleado);
                params.put("title", nombreAdministrador);



                params.put("body", estadoSubida+" un permiso a "+nombreEmpleado);


                params.put("estado", estadoNotificacion);



                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer" + " " + Utilidades.TOKEN);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }
    public void mandarNotificationAdmin(final  String nombreAdministrador,final  String estadoSubida,final  String nombreEmpleado   )

    {
        JSONObject json = new JSONObject();
        try {
            JSONObject userData = new JSONObject();
            userData.put("title", nombreAdministrador );
            userData.put("body",estadoSubida+" un permiso a "+nombreEmpleado);
            userData.put("sound", "default");
            json.put("to", tokenFirebaseTabla);
            json.put("data", userData);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                     Log.i(TAG,"mando");


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {




            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                String autorizacionFirebase ="AAAAFXKYB2Y:APA91bGLoCcd89yFHdTds-U7VFMpcwEX8w3rt-l3jZabwi9QKaEZBUju8lIH8VLmANUMw8ciLAuYlqnt-32QzHcIP7dHmeu2klw-MmTPYSGk-X44J6mt7nC3d0RosnQLQXnwEW8WBm3m";

                params.put("Authorization", "Key=" + " " + autorizacionFirebase);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
    private void cargarPreferencias() {
        SharedPreferences preferences = getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        nombreAdministrador = preferences.getString("name", "");
        idPreference = preferences.getString("id", "");
        tokenAdministrador= preferences.getString("token", "");
        rolPreference  = preferences.getString("rol", "");




    }


}