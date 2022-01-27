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

import proyectod.permisosunahtec.PermisoFragment;
import proyectod.permisosunahtec.R;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.utils.Utilidades;
import proyectod.permisosunahtec.web.PojoEmpleados;
import proyectod.permisosunahtec.web.PojoEmpleados;
import proyectod.permisosunahtec.web.VolleySingleton;

public class SyncAdapterEmpleados extends AbstractThreadedSyncAdapter {
    private static final String TAG = SyncAdapterEmpleados.class.getSimpleName();

    ContentResolver resolver;
    private Gson gson = new Gson();
    PojoEmpleados e;

    private JSONObject json_data;
    private String tokenFirebaseTabla;
    /**
     * Proyección para las consultas
     */
    private static final String[] PROJECTION = new String[]{
            ContractParaEmpleados.Columnas._ID,
            ContractParaEmpleados.Columnas.ID_REMOTA,
            ContractParaEmpleados.Columnas.NAME,
            ContractParaEmpleados.Columnas.EMAIL,
            ContractParaEmpleados.Columnas.PASSWORD,
            ContractParaEmpleados.Columnas.PICTURE,
            ContractParaEmpleados.Columnas.CODDEPTO,
            ContractParaEmpleados.Columnas.TOKENFIREBASE,
            ContractParaEmpleados.Columnas.ROL,

            ContractParaEmpleados.Columnas.PENDIENTE_ELIMINACION,

    };

    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_ID = 0;
    public static final int COLUMNA_ID_REMOTA = 1;
    public static final int COLUMNA_NAME = 2;
    public static final int COLUMNA_EMAIL = 3;
    public static final int COLUMNA_PASSWORD = 4;
    public static final int COLUMNA_PICTURE = 5;
    public static final int COLUMNA_CODDEPTO = 6;
    public static final int COLUMNA_TOKENFIREBASE = 7;
    public static final int COLUMNA_ROL = 8;
    public static final int COLUMNA_ELIMINADO = 9;
    private String DATA_URL;
    private String[] justin;
    private JSONObject objetCursor;
    private JSONArray lista;

    private String estadoNotificacion;
    String idEmpleado ;
    int idRemotaEmpleadp =0;
    private String nombreAdministrador;
    private String idpreference;
    private String rolPreference;
    private String tokenAdministrador;
    String Url;
    private Cursor cursorList;

    public SyncAdapterEmpleados(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        resolver = context.getContentResolver();

    }

    /**
     * Constructor para mantener compatibilidad en versiones inferiores a 3.0
     */
    public SyncAdapterEmpleados(
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

        String URL;
        if(rolPreference.equals("admin")){
            URL =Utilidades.URL + "api/auth/listaUsuario";
        }else {

            URL = Utilidades.URL + "api/auth/listaEmpleadosId/"+idpreference;
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
                                Log.i(TAG,""+error);


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


        Log.i(TAG, "Actualizando el servidor...");

        iniciarActualizacion();

        Cursor c = obtenerRegistrosSucios();

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios.");

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
              final  int   idLocal = c.getInt(c.getColumnIndex("_id"));


                final int idRemota =c.getInt(c.getColumnIndex("idRemota"));

                final int estadoEliminacion =c.getInt(c.getColumnIndex("eliminado"));
                final String nombreEmpleado =c.getString(c.getColumnIndex("name"));


                if(estadoEliminacion ==1){

                    Url = Utilidades.URL + "api/auth/borrarEmpleado/"+ idRemota;

                }else {
                    if (idRemota == 0) {
                        Log.i(TAG,"entra a registro");
                        Url = Utilidades.URL + "api/auth/registrouser";
                        objetCursor =Utilidades.deCursorAJSONObjectEmpleado(c);
                    } else {
                             if(rolPreference.equals("admin")){
                                 Url = Utilidades.URL + "api/auth/modificarEmpleado/" + idRemota;

                             }else {
                                 Url = Utilidades.URL + "api/auth/actualizarfoto/" + idRemota;

                             }
                            objetCursor = Utilidades.deCursorAJSONObjectEmpModificar(c);
                    }
                }


                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,

                                Url,
                                objetCursor,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse( final JSONObject response) {

                                        if(estadoEliminacion ==1){
                                            Log.i("CHACONEMA",""+idLocal);



                                            final String  estadoDeSubida ="eliminado";

                                            String[] selectionArgs = new String[]{String.valueOf(idLocal)};
                                          cursorList=  getContext().getContentResolver().query(ContractParaEmpleados.CONTENT_URI,null,"_id=?",selectionArgs,null);
                                           if(cursorList.moveToNext()){
                                                tokenFirebaseTabla = cursorList.getString(cursorList.getColumnIndex("tokenFirebase"));


                                                   mandarNotificationIndividual(estadoDeSubida, String.valueOf(idLocal));

                                           }else{
                                                getContext().getContentResolver().delete(ContractParaEmpleados.CONTENT_URI, "_id =?",selectionArgs);


                                               SyncAdapterEmpleados.sincronizarAhora(getContext(), false);
                                           }


                                            invocarTokenFirebaseAdmin(nombreAdministrador,estadoDeSubida,nombreEmpleado);

                                        }else {
                                            if(idRemota !=0){


                                                final String  estadoDeSubida ="modificado";
                                                invocarTokenFirebase(String.valueOf(idLocal),estadoDeSubida);
                                                invocarTokenFirebaseAdmin(nombreAdministrador,estadoDeSubida,nombreEmpleado);
                                                
                                            }else {
                                                final String  estadoDeSubida ="agregó";
                                                invocarTokenFirebaseAdmin(nombreAdministrador,estadoDeSubida,nombreEmpleado);
                                            }
                                            Log.i(TAG,"RESPONDIO ALA INSERCION"+" "+idLocal);
                                            procesarRespuestaInsert(response, idLocal);
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.i(TAG,"no respondio"+error);
                                    }
                                }

                        ){

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
           SyncAdapterEmpleados.sincronizarAhora(getContext(), false);
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
        Uri uri = ContractParaEmpleados.CONTENT_URI;
        String selection = ContractParaEmpleados.Columnas.PENDIENTE_INSERCION + "=? AND "
                + ContractParaEmpleados.Columnas.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", ContractParaEmpleados.ESTADO_SYNC + ""};

        return resolver.query(uri, PROJECTION, selection, selectionArgs, null);
    }


    /**
     * Cambia a estado "de sincronización" el registro que se acaba de insertar localmente
     */
    private void iniciarActualizacion() {
        Uri uri = ContractParaEmpleados.CONTENT_URI;
        String selection = ContractParaEmpleados.Columnas.PENDIENTE_INSERCION + "=? AND "
                + ContractParaEmpleados.Columnas.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", ContractParaEmpleados.ESTADO_OK + ""};

        ContentValues v = new ContentValues();
        v.put(ContractParaEmpleados.Columnas.ESTADO, ContractParaEmpleados.ESTADO_SYNC);

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
    private void finalizarActualizacion( final String idRemota,final int idLocal) {
        Uri uri = ContractParaEmpleados.CONTENT_URI;
        String selection = ContractParaEmpleados.Columnas._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(idLocal)};

        ContentValues v = new ContentValues();
        v.put(ContractParaEmpleados.Columnas.PENDIENTE_INSERCION, "0");
        v.put(ContractParaEmpleados.Columnas.ESTADO, ContractParaEmpleados.ESTADO_OK);
        v.put(ContractParaEmpleados.Columnas.ID_REMOTA, idRemota);

        resolver.update(uri, v, selection, selectionArgs);
        Log.i(TAG, "cheque todo");



            SyncAdapterEmpleados.sincronizarAhora(getContext(), false);

    }

    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en formato Json
     */
    public void procesarRespuestaInsert( final  JSONObject response, int idLocal) {
        Log.i(TAG,"Procesar Respuesta Insert");

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
             // Obtener identificador del nuevo registro creado en el servidor



            switch (estado) {
                case "1":

                    finalizarActualizacion(String.valueOf(idLocal), idLocal);
                    break;

                case "2":
                    Log.i(TAG,"");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG," Error Procesar Respuesta Insert"+e.toString());

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
        PojoEmpleados[] res = gson.fromJson(gastos != null ? gastos.toString() : null, PojoEmpleados[].class);
        List<PojoEmpleados> data = Arrays.asList(res);




        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();


        // Tabla hash para recibir las entradas entrantes
        HashMap<String, PojoEmpleados> expenseMap = new HashMap<String, PojoEmpleados>();
        int i=0;
        for (PojoEmpleados e : data) {

            expenseMap.put(e.id, e);




        }

        // Consultar registros remotos actuales
        Uri uri = ContractParaEmpleados.CONTENT_URI;
        String select = ContractParaEmpleados.Columnas.ID_REMOTA;
        Cursor c = resolver.query(uri, PROJECTION, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros localess.");

        // Encontrar datos obsoletos
        String id =null;
        String name;
        String email;
        String password;
        String picture;
        String tokenFirebase;
        String codDepartamento;
        String rol;


        while (c.moveToNext()) {

            syncResult.stats.numEntries++;

            id = c.getString(COLUMNA_ID_REMOTA);
            name = c.getString(COLUMNA_NAME);
            email = c.getString(COLUMNA_EMAIL);
            password = c.getString(COLUMNA_PASSWORD);
            picture = c.getString(COLUMNA_PICTURE);
            tokenFirebase = c.getString(COLUMNA_TOKENFIREBASE);
            codDepartamento = c.getString(COLUMNA_CODDEPTO);
            rol = c.getString(COLUMNA_ROL);



            PojoEmpleados match = expenseMap.get(id);




            if (match != null) {
                boolean b =true;
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);



                Uri existingUri = ContractParaEmpleados.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado

                if (match.name !=null && match.name.equals(name)){
                    b =false;
                }
                boolean b1 = match.email != null && !match.email.equals(email);

                Log.i(TAG, "Programando actualización de: " + b +" "+b1+" "+match.name+" "+name);
                boolean b2 = match.password != null && !match.password.equals(password);
                boolean b3 = match.picture != null && !match.picture.equals(picture);
                boolean b4 = match.tokenFirebase != null && !match.tokenFirebase.equals(tokenFirebase);
                boolean b5 = match.codDepartamento != null && !match.codDepartamento.equals(codDepartamento);
                boolean b6 = match.rol != null && !match.rol.equals(rol);
                if (b|| b1 || b2 || b3 ||b4||b5||b6 ) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContractParaEmpleados.Columnas.NAME, match.name)
                            .withValue(ContractParaEmpleados.Columnas.EMAIL, match.email)
                            .withValue(ContractParaEmpleados.Columnas.PICTURE, match.picture)
                            .withValue(ContractParaEmpleados.Columnas.PASSWORD, match.password)
                            .withValue(ContractParaEmpleados.Columnas.TOKENFIREBASE, match.tokenFirebase)
                            .withValue(ContractParaEmpleados.Columnas.CODDEPTO, match.codDepartamento)
                            .withValue(ContractParaEmpleados.Columnas.ROL, match.rol)


                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }



            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractParaEmpleados.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (PojoEmpleados e : expenseMap.values()) {


            Log.i(TAG, "Programando inserción de: " + e.name);
            ops.add(ContentProviderOperation.newInsert(ContractParaEmpleados.CONTENT_URI)
                    .withValue(ContractParaEmpleados.Columnas._ID, e.id)
                    .withValue(ContractParaEmpleados.Columnas.ID_REMOTA, e.id)
                    .withValue(ContractParaEmpleados.Columnas.NAME, e.name)
                    .withValue(ContractParaEmpleados.Columnas.EMAIL, e.email)
                    .withValue(ContractParaEmpleados.Columnas.PICTURE, e.picture)
                    .withValue(ContractParaEmpleados.Columnas.PASSWORD, e.password)
                    .withValue(ContractParaEmpleados.Columnas.TOKENFIREBASE, e.tokenFirebase)
                    .withValue(ContractParaEmpleados.Columnas.CODDEPTO,e.codDepartamento)
                    .withValue(ContractParaEmpleados.Columnas.ROL, e.rol)

                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(ContractParaEmpleados.AUTHORITY, ops);
            } catch (Exception a) {
                Log.i(TAG, "no inserto"+a);
            }
            resolver.notifyChange(
                    ContractParaEmpleados.CONTENT_URI,
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
                context.getString(R.string.provider_authority_empleados), bundle);

        ContentResolver.setSyncAutomatically(obtenerCuentaASincronizar(context), context.getString(R.string.provider_authority_empleados), true);






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


        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.provider_authority_empleados), true);
        ContentResolver.addPeriodicSync(
                newAccount, context.getString(R.string.provider_authority_empleados), new Bundle(), 3600);


        // Comprobar existencia de la cuenta
        if (null == accountManager.getPassword(newAccount)) {

            // Añadir la cuenta al account manager sin password y sin datos de usuario
            if (!accountManager.addAccountExplicitly(newAccount, "", null))
                return null;

        }
        Log.i(TAG, "Cuenta de usuario obtenida.");

        return newAccount;
    }

    private void invocarTokenFirebase( final String idLocal, final  String estadoSubida) {


        String   DATA_URL =  Utilidades.URL+"api/auth/listaEmpleadosId2/"+idLocal;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                DATA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,"respondio cuando modifica"+response);
                         showListViewIndividual(response,idLocal,estadoSubida);




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {





            }
        })    {

            @Override
            public Map<String,String> getHeaders(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer"+" "+tokenAdministrador);




                return params;}};

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }



    private void showListViewIndividual(JSONObject obj, final String idEmpleado, final  String estadoSubida) {
        try{





            lista = obj.optJSONArray("users");
            for (int i = 0; i < lista.length(); i++) {

                json_data = lista.getJSONObject(i);


                tokenFirebaseTabla = json_data.getString("tokenFirebase");


                if(tokenFirebaseTabla.equals("null")){

                    estadoNotificacion ="pendiente";
                    Log.i(TAG,"registro cuando modifica");


                    registrarNotificacionIndividual(idEmpleado,estadoSubida);
                }else {

                    Log.i(TAG,"mando  cuando modificar");

                     mandarNotificationIndividual(estadoSubida,idEmpleado);
                }



            }






        } catch (Exception ex) {

        } finally {
        }
    }
    private void registrarNotificacionIndividual(final String idEmpleado,final String estadoSubida) {



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



                params.put("idEmpleado", String.valueOf(idEmpleado));
                params.put("title", "Se a "+estadoSubida+ " su información");



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
    public void mandarNotificationIndividual(final  String estadoSubida, final  String idLocal)

    {
        JSONObject json = new JSONObject();
        try {
            JSONObject userData = new JSONObject();

            userData.put("title", "Se a "+estadoSubida+" su información");
            userData.put("sound", "default");


            json.put("to", tokenFirebaseTabla);
            json.put("data", userData);

            if(estadoSubida.equals("eliminado")){
                String[] selectionArgs = new String[]{String.valueOf(idLocal)};
                getContext().getContentResolver().delete(ContractParaEmpleados.CONTENT_URI, "_id =?",selectionArgs);


                SyncAdapterEmpleados.sincronizarAhora(getContext(), false);
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

           Log.i(TAG,"mando notificacion");


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Log.i(TAG,"no mando ni verga");



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
    private void invocarTokenFirebaseAdmin(final String nombreAdministrador,final String estadoSubida ,final  String nombreDepto) {


        String   DATA_URL =  Utilidades.URL+"api/auth/getAdmin";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                DATA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,"respondio la invocacion");
                        showListViewAdmin(response ,nombreAdministrador,estadoSubida,nombreDepto );




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



    private void showListViewAdmin(JSONObject obj ,final String nombreAdministrador,final String estadoSubida,final String nombreDepto) {
        try{





            lista = obj.optJSONArray("users");
            for (int i = 0; i < lista.length(); i++) {

                json_data = lista.getJSONObject(i);


                tokenFirebaseTabla = json_data.getString("tokenFirebase");
                idEmpleado= json_data.getString("id");


                if(tokenFirebaseTabla.equals("null")){

                    estadoNotificacion ="pendiente";

                    registrarNotificacionAdmin(nombreAdministrador,idEmpleado,estadoSubida,nombreDepto);
                }else {

                    mandarNotificationAdmin(nombreAdministrador,estadoSubida,nombreDepto );
                }



            }






        } catch (Exception ex) {

        } finally {
        }
    }
    private void registrarNotificacionAdmin( final String nombreAdministrador , final String idEmpleado,final String estadoSubida,final String nombreDepto) {



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
                params.put("body", estadoSubida+" el Empleado de "+nombreDepto);


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
    public void mandarNotificationAdmin(final  String nombreAdministrador,final  String estadoSubida,final  String nombreDepto   )

    {
        JSONObject json = new JSONObject();
        try {
            JSONObject userData = new JSONObject();
            userData.put("title", nombreAdministrador );
            userData.put("body",estadoSubida+" el Empleado "+nombreDepto);
            userData.put("sound", "default");
            json.put("to", tokenFirebaseTabla);
            json.put("data", userData);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG,"mando la notifacion");



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, " No mando la notifacion");



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
        idpreference = preferences.getString("id", "");
        tokenAdministrador = preferences.getString("token", "");
        rolPreference = preferences.getString("rol", "");




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


}