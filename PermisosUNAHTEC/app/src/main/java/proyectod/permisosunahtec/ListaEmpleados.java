package proyectod.permisosunahtec;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import proyectod.permisosunahtec.adapters.AdaptadorDeEmpleados;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.syncs.SyncAdapterEmpleados;
import proyectod.permisosunahtec.web.PojoEmpleados;

public class ListaEmpleados extends AppCompatActivity implements SearchView.OnQueryTextListener   , AdaptadorDeEmpleados.OnItemClickListener, AdaptadorDeEmpleados.OnLongClickListener,  LoaderManager.LoaderCallbacks<Cursor>  {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AdaptadorDeEmpleados adapter;
    private Object resolver;


    private String informacionId ;

    public static int c;
    private ImageView imagenEmpleado;
   private   int variable;


     private String idAdmin;

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_empleados);

        Bundle bundle =this.getIntent().getExtras();




        variable =bundle.getInt("variable");

        informacionId =bundle.getString("informacionIdstatic");


        getSupportActionBar().setTitle("Empleados");
        cargarPreferencias();
        recyclerView = (RecyclerView) findViewById(R.id.listaempleados2);
        imagenEmpleado = (ImageView) findViewById(R.id.imageitem);
        resolver = getContentResolver();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdaptadorDeEmpleados(this,this,  this);
        recyclerView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(0, null, this);









    }




    public void nuevoEmpleado(View view){

        Intent intentotra = new Intent(ListaEmpleados.this,AgregarNuevoEmpleado.class);

        intentotra.putExtra("informacionId", informacionId);
         intentotra.putExtra("variable",1);

        startActivity(intentotra);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.solofiltro,menu);
        MenuItem item = menu.findItem(R.id.buscar);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        try {
          if (variable ==2){
              adapter.filtrar(s.trim());
              adapter.notifyDataSetChanged();
          }else {
              adapter.filtrarPorDeptos(s.trim() ,informacionId);
              adapter.notifyDataSetChanged();
          }

        }catch (Exception e){
            e.getStackTrace();
        }
        return true;

    }


    private void cargarPreferencias() {
        SharedPreferences preferences =getSharedPreferences("credenciales", Context.MODE_PRIVATE);


        idAdmin = preferences.getString("id","");


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        CursorLoader cursorLoader;



             if(variable ==2){
                 String[] selectionArgs = new String[]{"0"};
               cursorLoader=  new CursorLoader(this, ContractParaEmpleados.CONTENT_URI,
                         null, "eliminado = ? ",selectionArgs , null);
             }else{
                 String[] selectionArgs = new String[]{"0",informacionId};
                 cursorLoader=  new CursorLoader(this, ContractParaEmpleados.CONTENT_URI,
                         null, "eliminado = ? and codDepartamento =? ",selectionArgs , null);
             }

        return cursorLoader;

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onClick(RecyclerView.ViewHolder holder, int idDepartamento) {
        PojoEmpleados pojoEmpleados = new PojoEmpleados();

       /* pojoEmpleados.setId(adapter.getCursor().getString(adapter.getCursor().getColumnIndex(ContractParaEmpleados.Columnas._ID)));
        pojoEmpleados.setPicture(adapter.getCursor().getString(adapter.getCursor().getColumnIndex(ContractParaEmpleados.Columnas.PICTURE)));
        pojoEmpleados.setEmail(adapter.getCursor().getString(adapter.getCursor().getColumnIndex(ContractParaEmpleados.Columnas.EMAIL)));
        pojoEmpleados.setName(adapter.getCursor().getString(adapter.getCursor().getColumnIndex(ContractParaEmpleados.Columnas.NAME)));
       */if (variable == 2) {


            Intent intentInfo = new Intent(ListaEmpleados.this, AgregarPermisos.class);

            Bundle bundle = new Bundle();

            bundle.putString("id", adapter.getCursor().getString(adapter.getCursor().getColumnIndex(ContractParaEmpleados.Columnas._ID)));
            bundle.putString("codDepartamento", adapter.getCursor().getString(adapter.getCursor().getColumnIndex(ContractParaEmpleados.Columnas.CODDEPTO)));

            bundle.putInt("variable", 2);
            intentInfo.putExtras(bundle);

            startActivity(intentInfo);
            finish();

        }else {
            Intent intentInfo = new Intent(ListaEmpleados.this, InformacionEmpleado.class);

            Bundle bundle = new Bundle();



           bundle.putString("id", adapter.getCursor().getString(adapter.getCursor().getColumnIndex(ContractParaEmpleados.Columnas._ID)));
            bundle.putString("email", adapter.getCursor().getString(adapter.getCursor().getColumnIndex(ContractParaEmpleados.Columnas.EMAIL)));
            bundle.putString("picture", adapter.getCursor().getString(adapter.getCursor().getColumnIndex(ContractParaEmpleados.Columnas.PICTURE)));
            bundle.putString("name", adapter.getCursor().getString(adapter.getCursor().getColumnIndex(ContractParaEmpleados.Columnas.NAME)));
        bundle.putString("codDepartamento", adapter.getCursor().getString(adapter.getCursor().getColumnIndex(ContractParaEmpleados.Columnas.CODDEPTO)));
        bundle.putString("rol", adapter.getCursor().getString(adapter.getCursor().getColumnIndex(ContractParaEmpleados.Columnas.ROL)));
             bundle.putInt("variable", 2);

         intentInfo.putExtras(bundle);

            startActivity(intentInfo);
       }

    }

    @Override
    public void onLongClick(RecyclerView.ViewHolder holder, final int idUser) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                                    Toast.makeText(getApplicationContext(), "No se puede eliminar ud mismo.", Toast.LENGTH_SHORT).show();

                                }else {

                                    ContentValues values = new ContentValues();

                                    values.put(ContractParaEmpleados.Columnas.PENDIENTE_INSERCION, 1);
                                    values.put(ContractParaEmpleados.Columnas.PENDIENTE_ELIMINACION, 1);
                                    values.put(ContractParaEmpleados.Columnas.ESTADO, 0);
                                    String[] selectionArgs = new String[]{String.valueOf(idUser)};

                                    getContentResolver().update(ContractParaEmpleados.CONTENT_URI, values,"_id =?",selectionArgs);

                                    SyncAdapterEmpleados.sincronizarAhora(getApplicationContext(), true);
                                }





                            }

                        });


        AlertDialog alert = builder.create();
        alert.show();

    }





    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}




