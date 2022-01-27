package proyectod.permisosunahtec;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import proyectod.permisosunahtec.adapters.AdaptadorDeEmpleados ;
import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.providers.ContractParaPermisos;
import proyectod.permisosunahtec.syncs.SyncAdapterDeptos;
import proyectod.permisosunahtec.syncs.SyncAdapterEmpleados;
import proyectod.permisosunahtec.syncs.SyncAdapterPermisos;
import proyectod.permisosunahtec.utils.Utilidades;

public class EmpleadosFragment extends Fragment implements SearchView.OnQueryTextListener, AdaptadorDeEmpleados .OnItemClickListener, AdaptadorDeEmpleados .OnLongClickListener,  LoaderManager.LoaderCallbacks<Cursor>   {

    private RecyclerView recyclerViewEmpleados;
    private LinearLayoutManager layoutManager;
    private AdaptadorDeEmpleados adapterDeEmpleados;



    public static int c;


    View vista;
      private String idAdmin;
    private String refrescheToken;
    private int eventoCerrar;
    private Cursor cursorDeparatamentos;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vista= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_empleados,container,false);
        recyclerViewEmpleados = (RecyclerView) vista.findViewById(R.id.listaAllEmpleados);
        FloatingActionButton fab = (FloatingActionButton) vista.findViewById(R.id.bttnEmpleadoFragment);
        setHasOptionsMenu(true);

        String[] selectionArgs = new String[]{"1"};
        swipeRefreshLayout = (SwipeRefreshLayout) vista.findViewById(R.id.swipeRefreshLayoutEmpleados);

        cursorDeparatamentos=  getContext().getContentResolver().query(ContractParaDepartamentos.CONTENT_URI, null,"pendiente_insercion =?",selectionArgs,null);
        Cursor cursorEmpleados = getContext().getContentResolver().query(ContractParaEmpleados.CONTENT_URI, null, "pendiente_insercion =?", selectionArgs, null);

        if(cursorDeparatamentos.getCount()>0){

        }else {
                 SyncAdapterEmpleados.inicializarSyncAdapter(getContext());

                SyncAdapterEmpleados.sincronizarAhora(getActivity(), true);




        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto

                if(cursorDeparatamentos.getCount()>0){
                    SyncAdapterDeptos.inicializarSyncAdapter(getContext());

                    SyncAdapterDeptos.sincronizarAhora(getActivity(), true);
                }else {
                    SyncAdapterEmpleados.inicializarSyncAdapter(getContext());

                    SyncAdapterEmpleados.sincronizarAhora(getActivity(), true);




                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        cargarPreferencias();
        recyclerViewEmpleados.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewEmpleados.setLayoutManager(layoutManager);
        adapterDeEmpleados = new AdaptadorDeEmpleados (getActivity(),this,  this);
        recyclerViewEmpleados.setAdapter(adapterDeEmpleados);

        getActivity().getSupportLoaderManager().initLoader(1, null, this);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentotra = new Intent(getActivity(), AgregarNuevoEmpleado.class);
                Bundle bundle = new Bundle () ;

                bundle.putInt("variable",3);


                intentotra.putExtras(bundle);
                startActivity(intentotra);

            }
        });








     return  vista;


    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.busquedamenudeptos, menu);
        MenuItem item = menu.findItem(R.id.buscar);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.cerrarSesion:

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Atencion");
                builder.setMessage("¿Esta seguro que desea cerrar sesion?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eventoCerrar =2;
                  updateToken();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create();
                builder.show();

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void cerrarSesion() {
        try {
            final ProgressDialog loading = ProgressDialog.show(getActivity(), "Por favor espere...", "Actualizando datos...",
                    false, false);
            String ip = Utilidades.URL;
            String URL = ip + "api/auth/logout";

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (response != null) {
                            JSONArray array = jsonObject.getJSONArray("sesion");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                String mensaje = object.getString("mensaje");
                                Toast.makeText(getContext(), "" + mensaje, Toast.LENGTH_SHORT).show();
                            }



                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            Intent intentInfo = new Intent(getContext(),Login.class);
                            startActivity(intentInfo);
                            getActivity().finish();
                            loading.dismiss();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Intentelo denuevo."  , Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("Authorization", "Bearer" + " " + Utilidades.TOKEN);
                    return parametros;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }catch (Exception e){
            cerrarSesion();
        }
    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        try {
            adapterDeEmpleados.filtrar(s.trim());
            adapterDeEmpleados.notifyDataSetChanged();
        }catch (Exception e){
            e.getStackTrace();

        }
        return true;
    }


    private void updateToken(  ) {


        String REGISTER_URL = Utilidades.URL+"api/auth/updateToken/"+idAdmin;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                          cerrarSesion();

                    }},new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

               }}){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();


                    params.put("tokenFirebase", "null");




                return params;}

        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Authorization", "Bearer" + " " + Utilidades.TOKEN);


            return params;
        }};
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());requestQueue.add(stringRequest);
    }
    private void cargarPreferencias() {
        SharedPreferences preferences =getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        Utilidades.TOKEN = preferences.getString("token","");
        idAdmin = preferences.getString("id","");
        Log.d("MOISES",""+idAdmin);


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] selectionArgs = new String[]{"0"};



        return new CursorLoader(getActivity(), ContractParaEmpleados.CONTENT_URI,
                null, "eliminado = ?",selectionArgs , null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapterDeEmpleados.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onClick(RecyclerView.ViewHolder holder, int idDepartamento) {

        Intent intentInfo = new Intent(getContext(), InformacionEmpleado.class);

        Bundle bundle = new Bundle();


        bundle.putString("id", adapterDeEmpleados.getCursor().getString(adapterDeEmpleados.getCursor().getColumnIndex(ContractParaEmpleados.Columnas._ID)));
        bundle.putString("email", adapterDeEmpleados.getCursor().getString(adapterDeEmpleados.getCursor().getColumnIndex(ContractParaEmpleados.Columnas.EMAIL)));
        bundle.putString("picture", adapterDeEmpleados.getCursor().getString(adapterDeEmpleados.getCursor().getColumnIndex(ContractParaEmpleados.Columnas.PICTURE)));
        bundle.putString("name", adapterDeEmpleados.getCursor().getString(adapterDeEmpleados.getCursor().getColumnIndex(ContractParaEmpleados.Columnas.NAME)));
        bundle.putString("codDepartamento", adapterDeEmpleados.getCursor().getString(adapterDeEmpleados.getCursor().getColumnIndex(ContractParaEmpleados.Columnas.CODDEPTO)));
        bundle.putString("rol", adapterDeEmpleados.getCursor().getString(adapterDeEmpleados.getCursor().getColumnIndex(ContractParaEmpleados.Columnas.ROL)));
        bundle.putInt("variable", 2);
        intentInfo.putExtras(bundle);
        intentInfo.putExtra("variable",2);

        startActivity(intentInfo);

    }

    @Override
    public void onLongClick(RecyclerView.ViewHolder holder,final int idUser) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Seguro que desea eliminar el empleado?")
                .setTitle("Advertencia")
                .setCancelable(false)
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                            }
                        })
                .setPositiveButton("Eliminar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(idAdmin.equals(String.valueOf(idUser) )){
                                    Toast.makeText(getContext(), "No se puede eliminar ud mismo.", Toast.LENGTH_SHORT).show();

                                }else {

                                    ContentValues values = new ContentValues();

                                    values.put(ContractParaEmpleados.Columnas.PENDIENTE_INSERCION, 1);
                                    values.put(ContractParaEmpleados.Columnas.PENDIENTE_ELIMINACION, 1);
                                    values.put(ContractParaEmpleados.Columnas.ESTADO, 0);
                                    String[] selectionArgs = new String[]{String.valueOf(idUser)};

                                    getActivity().getContentResolver().update(ContractParaEmpleados.CONTENT_URI, values,"_id =?",selectionArgs);

                                    SyncAdapterEmpleados.sincronizarAhora(getContext(), true);


                                    ContentValues values3 = new ContentValues();
                                    String[] selectionArgs3 = new String[]{String.valueOf(idUser)};
                                    values3.put(ContractParaPermisos.Columnas.PENDIENTE_INSERCION, 1);
                                    values3.put(ContractParaPermisos.Columnas.PENDIENTE_ELIMINACION, 1);
                                    values3.put(ContractParaPermisos.Columnas.ESTADO, 0);
                                    getContext().getContentResolver().update(ContractParaPermisos.CONTENT_URI,values3, "idEmpleado =?",selectionArgs3);
                                    SyncAdapterPermisos.inicializarSyncAdapter(getContext());
                                    SyncAdapterPermisos.sincronizarAhora(getContext(), true);
                                }





                            }

                        });


        AlertDialog alert = builder.create();
        alert.show();

    }
}
