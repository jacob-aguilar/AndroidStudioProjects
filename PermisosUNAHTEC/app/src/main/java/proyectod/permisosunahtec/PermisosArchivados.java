package proyectod.permisosunahtec;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import proyectod.permisosunahtec.adapters.AdapterListDeptos;
import proyectod.permisosunahtec.adapters.AdapterListPermisos;
import proyectod.permisosunahtec.pojo.PermisosGetYSet;
import proyectod.permisosunahtec.utils.Utilidades;
import proyectod.permisosunahtec.web.DepartamentosGetYSet;

public class PermisosArchivados extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private String DATA_URL;
    private ArrayList<PermisosGetYSet> contes;
    private JSONArray lista;
    private JSONObject json_data;
    private ListView lv1;
    private AdapterListPermisos adapterListPermisos;
    private String tokenAdministrador;
    private String observaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisos_archivados);
       cargarPreferencias();
        lv1 = (ListView)  findViewById(R.id.listaPermisosArchivados) ;

        invocarServicio();

        getSupportActionBar().setTitle("Permisos Archivados");


        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                observaciones="" + contes.get(pos).getObservaciones();


                if (observaciones ==null ||observaciones.equals("null")) {

                    Toast.makeText(getApplicationContext(), "Sin observaciones", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PermisosArchivados.this);
                    builder.setMessage(observaciones).setTitle("Observaciones:").setCancelable(false).setNegativeButton("Cerrar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }



            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.solofiltro,menu);
        MenuItem item = menu.findItem(R.id.buscar);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }


    private void invocarServicio() {
         DATA_URL = Utilidades.URL + "api/auth/exportarTodo";
        final ProgressDialog loading = ProgressDialog.show(this, "Por favor espere...", "Actualizando datos...", false, false);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                DATA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loading.dismiss();
                        showListView(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Error al cargar la lista no responde :" + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
         
        })

        {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer" + " " + tokenAdministrador);


                return params;
            }};
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
    }

    private void showListView(JSONObject obj) {
        try{

           PermisosGetYSet permisos =null;


             contes = new ArrayList<>();
             lista = obj.optJSONArray("permiso");
            for (int i = 0; i < lista.length(); i++) {
                permisos = new PermisosGetYSet();
                 json_data = lista.getJSONObject(i);

                permisos.setNameEmpleado(json_data.getString("name"));
                permisos.setTipoPermiso(json_data.getString("tipoPermiso"));
                permisos.setFechaInicio(json_data.getString("fechaInicio"));
                permisos.setFechaFin(json_data.getString("fechaFin"));
                permisos.setObservaciones(json_data.getString("observaciones"));

                contes.add(permisos);
            }
        adapterListPermisos = new AdapterListPermisos(this,contes);
             lv1.setAdapter(adapterListPermisos);


        } catch (Exception ex) {
            Toast.makeText(this, "Error al cargar la lista:" , Toast.LENGTH_LONG).show();
        } finally {
        }
    }
    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        
        tokenAdministrador= preferences.getString("token", "");
        




    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
         if (!(adapterListPermisos == null)) {
            ArrayList<PermisosGetYSet> listaPermisosFiltrada = null;
            try {
                listaPermisosFiltrada = filtrarDatosDeptos(contes, s.trim());
                adapterListPermisos.filtrar(listaPermisosFiltrada);
                adapterListPermisos.notifyDataSetChanged();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "" + listaPermisosFiltrada, Toast.LENGTH_SHORT).show();

            }
             return true;


        }
        return false;
    }
        private ArrayList<PermisosGetYSet> filtrarDatosDeptos(ArrayList<PermisosGetYSet> listaTarea, String dato) {
            ArrayList<PermisosGetYSet> listaFiltradaPermiso = new ArrayList<>();
            try{
                dato = dato.toLowerCase();
                for(PermisosGetYSet permisos: listaTarea){
                    String nombre = permisos.getNameEmpleado().toLowerCase();
                    String tipoPermiso = permisos.getTipoPermiso().toLowerCase();


                    if(nombre.toLowerCase().contains(dato)   ){
                        listaFiltradaPermiso.add(permisos);
                    }
                }
                adapterListPermisos.filtrar(listaFiltradaPermiso);
            }catch (Exception e){
                Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            }

            return listaFiltradaPermiso;

        }
}
