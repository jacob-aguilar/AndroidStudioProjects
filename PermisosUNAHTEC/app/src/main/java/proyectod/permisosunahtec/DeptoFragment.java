package proyectod.permisosunahtec;

import android.app.ActivityOptions;
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
import android.widget.TextView;
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

import proyectod.permisosunahtec.adapters.AdaptadorDeDepartamentos;
import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.providers.ContractParaPermisos;
import proyectod.permisosunahtec.syncs.SyncAdapterDeptos;
import proyectod.permisosunahtec.syncs.SyncAdapterEmpleados;
import proyectod.permisosunahtec.syncs.SyncAdapterPermisos;
import proyectod.permisosunahtec.utils.Utilidades;

public class DeptoFragment extends Fragment implements SearchView.OnQueryTextListener, AdaptadorDeDepartamentos.OnItemClickListener, AdaptadorDeDepartamentos.OnLongClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AdaptadorDeDepartamentos adapter;
    private TextView emptyView;
    private Object resolver;
    private int informacionId;
    private SwipeRefreshLayout swipeRefreshLayout;
    Cursor cursorDeparatamentos;




    public static int c;



    View vista;
     private String idAdmin;
    private String codDeptoPreference;


    @Override
    public void onResume() {
        super.onResume();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        vista = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_departamento, container, false);
        cargarPreferencias();

        String[] selectionArgs = new String[]{"1"};
       cursorDeparatamentos=  getContext().getContentResolver().query(ContractParaDepartamentos.CONTENT_URI, null,"pendiente_insercion =?",selectionArgs,null);
        if(cursorDeparatamentos.getCount()>0){
            SyncAdapterDeptos.inicializarSyncAdapter(getActivity());


            SyncAdapterDeptos.sincronizarAhora(getActivity(), true);
        }else {
            SyncAdapterDeptos.inicializarSyncAdapter(getActivity());


            SyncAdapterDeptos.sincronizarAhora(getActivity(), false);
        }

        swipeRefreshLayout = (SwipeRefreshLayout) vista.findViewById(R.id.swipeRefreshLayout);

        recyclerView = (RecyclerView) vista.findViewById(R.id.reciclador);
        FloatingActionButton fab = (FloatingActionButton) vista.findViewById(R.id.bttnDeptoFragment);
        setHasOptionsMenu(true);

        resolver = getContext().getContentResolver();
        // setToolbar();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto

                if(cursorDeparatamentos.getCount()>0){
                    SyncAdapterDeptos.inicializarSyncAdapter(getActivity());


                    SyncAdapterDeptos.sincronizarAhora(getActivity(), true);

                }else {
                    SyncAdapterDeptos.inicializarSyncAdapter(getActivity());


                    SyncAdapterDeptos.sincronizarAhora(getActivity(), false);
                }
                 swipeRefreshLayout.setRefreshing(false);
              }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdaptadorDeDepartamentos(getContext(), this, this);
        recyclerView.setAdapter(adapter);


        getActivity().getSupportLoaderManager().initLoader(0, null, this);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), agregarnuevodepto.class);

                if (Utilidades.materialDesign())
                    startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());

                else startActivity(intent);
            }
        });



        return vista;

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

    private void cerrarSesion() {

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
                        Intent intentInfo = new Intent(getContext(), Login.class);
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
                Toast.makeText(getContext(), "Intentelo denuevo." , Toast.LENGTH_SHORT).show();
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


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        try {

            adapter.filtrar(s.trim());
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return true;
    }

    private void cargarPreferencias() {
        SharedPreferences preferences = getContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        Utilidades.TOKEN = preferences.getString("token", "");
        idAdmin = preferences.getString("id", "");
         codDeptoPreference=preferences.getString("codDepartamento", "");
        Log.d("MOISES", "" + idAdmin);


    }

    private void updateToken() {


        String REGISTER_URL = Utilidades.URL + "api/auth/updateToken/" + idAdmin;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        cerrarSesion();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error.toString().equals("com.android.volley.ServerError")) {
                    Toast.makeText(getContext(), "Presentamos problemas intentelo mas tarde.", Toast.LENGTH_LONG).show();

                } else if (error.toString().equals("com.android.volley.TimeoutError")) {
                    Toast.makeText(getContext(), "Revise su conexión a internet", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Intentelo denuevo.", Toast.LENGTH_LONG).show();


                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("tokenFirebase", "null");


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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {


        String[] selectionArgs = new String[]{"0"};


        return new CursorLoader(getContext(), ContractParaDepartamentos.CONTENT_URI,
                null, "eliminado =? ", selectionArgs, null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }


    @Override
    public void onClick(RecyclerView.ViewHolder holder, int idDepartamento) {
        Intent intentotra = new Intent(getContext(), ListaEmpleados.class);

        int idRemota = adapter.getCursor().getInt(adapter.getCursor().getColumnIndex("idRemota"));
        int c = 2;


        Bundle bundle = new Bundle();

         if (idRemota == 0) {
             bundle.putString("informacionIdstatic", String.valueOf(idDepartamento));
            intentotra.putExtras(bundle);
            startActivity(intentotra);


        } else {

            ContentValues values = new ContentValues();
            values.put(ContractParaEmpleados.Columnas.CODDEPTO, idRemota);
            String[] selectionArgs = new String[]{String.valueOf(idDepartamento)};
            getContext().getContentResolver().update(ContractParaEmpleados.CONTENT_URI, values, "codDepartamento =?", selectionArgs);

            ContentValues values2 = new ContentValues();
            values2.put(ContractParaPermisos.Columnas.ID_DEPTO, idRemota);
            String[] selectionArgs2 = new String[]{String.valueOf(idDepartamento)};
            getContext().getContentResolver().update(ContractParaPermisos.CONTENT_URI, values2, "idDepartamento =?", selectionArgs2);

            bundle.putString("informacionIdstatic", String.valueOf(idRemota));
             intentotra.putExtras(bundle);
            startActivity(intentotra);

        }



    }


    @Override
    public void onLongClick(RecyclerView.ViewHolder holder, final int idDepartamento, final String nombreDepto, final int idRemotaDepto, final int adapterposicion) {



   final   int idRemota = adapter.getCursor().getInt(adapter.getCursor().getColumnIndex("idRemota"));



         if(idRemota ==0){
             informacionId = idDepartamento;


         }else {
             informacionId = idRemota;


         }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Modificar o eliminar departamento")
                .setTitle("Advertencia")
                .setCancelable(false)

                .setNegativeButton("Modificar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intentotra = new Intent(getContext(), agregarnuevodepto.class);
                                int c = 2;
                                Bundle bundle = new Bundle();
                                bundle.putString("informacionId", String.valueOf(idDepartamento));
                                bundle.putString("nombreDepto", nombreDepto);
                                bundle.putString("idRemota", String.valueOf(idRemota));

                                bundle.putInt("c", c);

                                intentotra.putExtras(bundle);
                                startActivity(intentotra);

                            }
                        })
                .setNeutralButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Eliminar",
                        new DialogInterface.OnClickListener() {


                            public void onClick(DialogInterface dialog, int id) {

                                if (Integer.parseInt(codDeptoPreference) == idDepartamento || Integer.parseInt(codDeptoPreference)  == idRemota) {
                                    Toast.makeText(getContext(), "No puede eliminar su departamento.", Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setMessage("Seguro que desea eliminar , si lo hace se eliminaran todos los empleados que pertenecen a este departamento")
                                            .setTitle("Advertencia")
                                            .setCancelable(false)
                                            .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                }
                                            })
                                            .setPositiveButton("Eliminar",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            ContentValues values = new ContentValues();

                                                            values.put(ContractParaDepartamentos.Columnas.PENDIENTE_INSERCION, 1);
                                                            values.put(ContractParaDepartamentos.Columnas.PENDIENTE_ELIMINACION, 1);
                                                            values.put(ContractParaDepartamentos.Columnas.ESTADO, 0);
                                                            String[] selectionArgs = new String[]{String.valueOf(idDepartamento)};

                                                            getActivity().getContentResolver().update(ContractParaDepartamentos.CONTENT_URI, values, "_id =?", selectionArgs);
                                                            SyncAdapterDeptos.inicializarSyncAdapter(getContext());
                                                            SyncAdapterDeptos.sincronizarAhora(getContext(), true);

                                                            ContentValues values2 = new ContentValues();
                                                            String[] selectionArgs2 = new String[]{String.valueOf(informacionId)};

                                                            values2.put(ContractParaEmpleados.Columnas.PENDIENTE_INSERCION, 1);
                                                            values2.put(ContractParaEmpleados.Columnas.PENDIENTE_ELIMINACION, 1);
                                                            values2.put(ContractParaEmpleados.Columnas.ESTADO, 0);
                                                            getContext().getContentResolver().update(ContractParaEmpleados.CONTENT_URI, values2, "codDepartamento =?", selectionArgs2);
                                                            SyncAdapterEmpleados.inicializarSyncAdapter(getContext());
                                                            SyncAdapterEmpleados.sincronizarAhora(getContext(), true);


                                                            ContentValues values3 = new ContentValues();
                                                            String[] selectionArgs3 = new String[]{String.valueOf(informacionId)};
                                                            values3.put(ContractParaPermisos.Columnas.PENDIENTE_INSERCION, 1);
                                                            values3.put(ContractParaPermisos.Columnas.PENDIENTE_ELIMINACION, 1);
                                                            values3.put(ContractParaPermisos.Columnas.ESTADO, 0);
                                                            getContext().getContentResolver().update(ContractParaPermisos.CONTENT_URI, values3, "idDepartamento =?", selectionArgs3);
                                                            SyncAdapterPermisos.inicializarSyncAdapter(getContext());
                                                            SyncAdapterPermisos.sincronizarAhora(getContext(), true);


                                                        }

                                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }







    }

