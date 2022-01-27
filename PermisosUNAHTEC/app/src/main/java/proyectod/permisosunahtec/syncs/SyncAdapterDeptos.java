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
import android.nfc.Tag;
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
import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.providers.ContractParaPermisos;
import proyectod.permisosunahtec.providers.OperacionesBaseDatos;
import proyectod.permisosunahtec.utils.Utilidades;
import proyectod.permisosunahtec.web.DepartamentosGetYSet;
import proyectod.permisosunahtec.web.VolleySingleton;


/**
 * Maneja la transferencia de datos entre el servidor y el cliente
 */
public class SyncAdapterDeptos extends AbstractThreadedSyncAdapter {
    private static final String TAG = SyncAdapterDeptos.class.getSimpleName();

    ContentResolver resolver;
    private Gson gson = new Gson();
    DepartamentosGetYSet e;
    /**
     * Proyección para las consultas
     */
    private static final String[] PROJECTION = new String[]{
            ContractParaDepartamentos.Columnas._ID,
            ContractParaDepartamentos.Columnas.ID_REMOTA,
            ContractParaDepartamentos.Columnas.NOMBREDEPTO,
            ContractParaDepartamentos.Columnas.PENDIENTE_ELIMINACION,

    };

    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_ID = 0;
    public static final int COLUMNA_ID_REMOTA = 1;
    public static final int COLUMNA_DEPTO = 2;
    public static final int COLUMNA_ELIMINADO = 3;
    private String DATA_URL;
    private String[] justin;
    int idRemotaVolley;
    private JSONObject json_data;
    private String tokenFirebaseTabla;
    private JSONArray lista;
    private String estadoNotificacion;
    private OperacionesBaseDatos datos;
    private Cursor cursorList;

    private String nombreAdministrador;
    private String idEmpleado;
    private String tokenAdministrador;
    private String rolPreference;
    private String codDeptoPreference;
    private String idPreference;


    public SyncAdapterDeptos(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        resolver = context.getContentResolver();
    }

    /**
     * Constructor para mantener compatibilidad en versiones inferiores a 3.0
     */
    public SyncAdapterDeptos(
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

        boolean soloSubida = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);

        if (!soloSubida) {

            realizarSincronizacionLocal(syncResult);
        } else {


            realizarSincronizacionRemota();
        }
    }

    private void realizarSincronizacionLocal(final SyncResult syncResult) {
        Log.i(TAG, "Actualizando el cliente.");

        String URL = null;

        if(rolPreference.equals("admin")){

            URL =Utilidades.URL + "api/auth/listaDepartamento";
        }else {
            URL =Utilidades.URL + "api/auth/listaDepartamentoId/"+codDeptoPreference+"/"+idPreference;

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


                             }
                        }
                ){

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
                final int idLocal = c.getInt(COLUMNA_ID);
                  final  int     idRemotaVolley =c.getInt(COLUMNA_ID_REMOTA);
                      final int estadoEliminacion =c.getInt(COLUMNA_ELIMINADO);
                      final String nombreDepto =c.getString(c.getColumnIndex("nombreDepto"));



                   if(estadoEliminacion ==1){

                       Url = Utilidades.URL + "api/auth/destroyDeptoEmpleado/"+ idRemotaVolley;
                   }else {
                       if (idRemotaVolley == 0) {
                           Url = Utilidades.URL + "api/auth/registroDepto";
                       } else {

                           Url = Utilidades.URL + "api/auth/modificarDepto/" + idRemotaVolley;
                       }
                   }
                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,

                                Url,
                                Utilidades.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse( final JSONObject response) {

                                        if(estadoEliminacion ==1){
                                            Log.i("CHACON",""+idLocal);



                                            String[] selectionArgs = new String[]{String.valueOf(idLocal)};
                                            getContext().getContentResolver().delete(ContractParaDepartamentos.CONTENT_URI, "_id =?",selectionArgs);
                                           final String  estadoDeSubida ="eliminó";

                                            SyncAdapterDeptos.sincronizarAhora(getContext(), false);

                                            invocarTokenFirebase(nombreAdministrador,estadoDeSubida,nombreDepto);
                                        }else {
                                            if(idRemotaVolley ==0){
                                                final String  estadoDeSubida ="ingresó";

                                                invocarTokenFirebase(nombreAdministrador,estadoDeSubida,nombreDepto);
                                            }else {
                                                final String  estadoDeSubida ="modificó";

                                                invocarTokenFirebase(nombreAdministrador,estadoDeSubida,nombreDepto);

                                            }
                                            procesarRespuestaInsert(response, idLocal ,idRemotaVolley);
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }

                        ) {

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
            SyncAdapterDeptos.sincronizarAhora(getContext(), false);
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
        Uri uri = ContractParaDepartamentos.CONTENT_URI;
        String selection = ContractParaDepartamentos.Columnas.PENDIENTE_INSERCION + "=? AND "
                + ContractParaDepartamentos.Columnas.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", ContractParaDepartamentos.ESTADO_SYNC + ""};

        return resolver.query(uri, PROJECTION, selection, selectionArgs, null);
    }


    /**
     * Cambia a estado "de sincronización" el registro que se acaba de insertar localmente
     */
    private void iniciarActualizacion() {
        Uri uri = ContractParaDepartamentos.CONTENT_URI;
        String selection = ContractParaDepartamentos.Columnas.PENDIENTE_INSERCION + "=? AND "
                + ContractParaDepartamentos.Columnas.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", ContractParaDepartamentos.ESTADO_OK + ""};

        ContentValues v = new ContentValues();
        v.put(ContractParaDepartamentos.Columnas.ESTADO, ContractParaDepartamentos.ESTADO_SYNC);

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
     */
    private void finalizarActualizacion(String idRemota, int idLocal) {
        Uri uri = ContractParaDepartamentos.CONTENT_URI;
        String selection = ContractParaDepartamentos.Columnas._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(idLocal)};

        ContentValues v = new ContentValues();
        v.put(ContractParaDepartamentos.Columnas.PENDIENTE_INSERCION, "0");
        v.put(ContractParaDepartamentos.Columnas.ESTADO, ContractParaDepartamentos.ESTADO_OK);
        v.put(ContractParaDepartamentos.Columnas.ID_REMOTA, idRemota);

        resolver.update(uri, v, selection, selectionArgs);
        Log.i(TAG, "cheque todo");
        SyncAdapterDeptos.sincronizarAhora(getContext(), false);



                SyncAdapterEmpleados.inicializarSyncAdapter(getContext());
                SyncAdapterEmpleados.sincronizarAhora(getContext(), true);
            SyncAdapterPermisos.inicializarSyncAdapter(getContext());
            SyncAdapterPermisos.sincronizarAhora(getContext(), true);



    }

    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en formato Json
     */
    public void procesarRespuestaInsert( final JSONObject response, final int idLocal  ,final  int idRemotaVolley) {


        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");
            // Obtener identificador del nuevo registro creado en el servidor
            String idRemota = response.getString("id");

            if(idRemotaVolley ==0) {
                try {

                    Log.i(TAG,"actializo la remota"+idRemotaVolley+" "+idRemota+" "+idLocal);

                    ContentValues values = new ContentValues();
                    values.put(ContractParaEmpleados.Columnas.CODDEPTO, idRemota);
                    String[] selectionArgs = new String[]{String.valueOf(idLocal)};
                    getContext().getContentResolver().update(ContractParaEmpleados.CONTENT_URI, values, "codDepartamento =?", selectionArgs);

                    ContentValues values2 = new ContentValues();
                    values2.put(ContractParaPermisos.Columnas.ID_DEPTO, idRemota);
                    String[] selectionArgs2 = new String[]{String.valueOf(idLocal)};
                    getContext().getContentResolver().update(ContractParaPermisos.CONTENT_URI, values2, "idDepartamento =?", selectionArgs2);
                }catch (Exception e){
                    Log.i(TAG,""+e);

                }

            }


            switch (estado) {
                case "1":
                    Log.i(TAG, "averrr"+idRemota +" ,  "+idLocal);
                    finalizarActualizacion(idRemota, idLocal);
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

        JSONArray gastos = null;

        try {
            // Obtener array "gastos"
            gastos = response.getJSONArray("gastos");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        DepartamentosGetYSet[] res = gson.fromJson(gastos != null ? gastos.toString() : null, DepartamentosGetYSet[].class);
        List<DepartamentosGetYSet> data = Arrays.asList(res);




        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();


        // Tabla hash para recibir las entradas entrantes
        HashMap<String, DepartamentosGetYSet> expenseMap = new HashMap<String, DepartamentosGetYSet>();
        int i=0;
        for (DepartamentosGetYSet e : data) {

            expenseMap.put(e.id, e);




        }

        // Consultar registros remotos actuales
        Uri uri = ContractParaDepartamentos.CONTENT_URI;
        String select = ContractParaDepartamentos.Columnas.ID_REMOTA;
        Cursor c = resolver.query(uri, PROJECTION, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros localess.");

        // Encontrar datos obsoletos
        String id =null;
        String nombreDepto;
        

        while (c.moveToNext()) {

            syncResult.stats.numEntries++;

            id = c.getString(COLUMNA_ID_REMOTA);
            nombreDepto = c.getString(COLUMNA_DEPTO);


            DepartamentosGetYSet match = expenseMap.get(id);




            if (match != null) {
boolean b =true;
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);



                Uri existingUri = ContractParaDepartamentos.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                if(match.nombreDepto !=null && match.nombreDepto.equals(nombreDepto)){
                    b=false;
                }

                if (b ) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContractParaDepartamentos.Columnas.NOMBREDEPTO, match.nombreDepto)

                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }



            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractParaDepartamentos.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (DepartamentosGetYSet e : expenseMap.values()) {


            Log.i(TAG, "Programando inserción de: " + e.nombreDepto);
            ops.add(ContentProviderOperation.newInsert(ContractParaDepartamentos.CONTENT_URI)
                    .withValue(ContractParaDepartamentos.Columnas.ID_REMOTA, e.id)
                    .withValue(ContractParaDepartamentos.Columnas.NOMBREDEPTO, e.nombreDepto)

                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(ContractParaDepartamentos.AUTHORITY, ops);
            } catch (Exception a) {
                Log.i(TAG, "no inserto"+a);
            }
            resolver.notifyChange(
                    ContractParaDepartamentos.CONTENT_URI,
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
                context.getString(R.string.provider_authority), bundle);



    }
    public static void sincronizarAhoraEmpleado(Context context, boolean onlyUpload) {
        Log.i(TAG, "Realizando petición de sincronización manual.");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        if (onlyUpload)
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        ContentResolver.requestSync(obtenerCuentaASincronizar(context),
                context.getString(R.string.provider_authority_empleados), bundle);



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

    private void invocarTokenFirebase(final String nombreAdministrador,final String estadoSubida ,final  String nombreDepto) {


        String   DATA_URL =  Utilidades.URL+"api/auth/getAdmin";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                DATA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,"respondio la invocacion");
                        showListView(response ,nombreAdministrador,estadoSubida,nombreDepto );




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



    private void showListView(JSONObject obj ,final String nombreAdministrador,final String estadoSubida,final String nombreDepto) {
        try{





            lista = obj.optJSONArray("users");
            for (int i = 0; i < lista.length(); i++) {

                json_data = lista.getJSONObject(i);

                Log.i(TAG ,"jjsjsjsjjs");
                tokenFirebaseTabla = json_data.getString("tokenFirebase");
                idEmpleado= json_data.getString("id");


                if(tokenFirebaseTabla.equals("null")){

                    estadoNotificacion ="pendiente";

                    registrarNotificacion(nombreAdministrador,idEmpleado,estadoSubida,nombreDepto);
                }else {

                    mandarNotification2(nombreAdministrador,estadoSubida,nombreDepto );
                }



            }






        } catch (Exception ex) {

        } finally {
        }
    }
    private void registrarNotificacion( final String nombreAdministrador , final String idEmpleado,final String estadoSubida,final String nombreDepto) {



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

                Log.i(TAG,"no registro nada"+" "+idEmpleado+""+nombreAdministrador);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();



                params.put("idEmpleado", idEmpleado);
                params.put("title", nombreAdministrador);
                params.put("body", estadoSubida+" el departamento de "+nombreDepto);


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
    public void mandarNotification2(final  String nombreAdministrador,final  String estadoSubida,final  String nombreDepto   )

    {
        JSONObject json = new JSONObject();
        try {
            JSONObject userData = new JSONObject();
            userData.put("title", nombreAdministrador );
            userData.put("body",estadoSubida+" el departamento "+nombreDepto);
            userData.put("sound", "default");
            json.put("to", tokenFirebaseTabla);
            json.put("data", userData);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {




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
        tokenAdministrador = preferences.getString("token", "");
        rolPreference = preferences.getString("rol", "");
        codDeptoPreference = preferences.getString("codDepartamento", "");
        idPreference = preferences.getString("id", "");






    }


}