package proyectod.permisosunahtec;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import android.util.Log;

import proyectod.permisosunahtec.adapters.AdaptadorDeAllPermisos;
import proyectod.permisosunahtec.adapters.AdapterListDeptos;
import proyectod.permisosunahtec.adapters.AdapterListEmpleado;
import proyectod.permisosunahtec.adapters.AdapterListPermisos;
import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.providers.ContractParaPermisos;
 import proyectod.permisosunahtec.providers.OperacionesBaseDatos;
import proyectod.permisosunahtec.syncs.SyncAdapterDeptos;
import proyectod.permisosunahtec.syncs.SyncAdapterPermisos;
import proyectod.permisosunahtec.utils.Utilidades;
import proyectod.permisosunahtec.web.DepartamentosGetYSet;
import proyectod.permisosunahtec.web.PojoEmpleados;

import static android.content.ContentValues.TAG;

public class PermisoFragment extends Fragment implements SearchView.OnQueryTextListener, AdaptadorDeAllPermisos.OnItemClickListener, AdaptadorDeAllPermisos.OnLongClickListener,  LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
private  Calendar fechaDispositivo;
    private ListView lv1;
    private String DATA_URL;
 private  String fechaInicial;
private  String fechaFinal;
    private JSONObject json_data;
     private ArrayList<DepartamentosGetYSet> contes2;
    private JSONArray lista;

    OperacionesBaseDatos datos;
    private int mYearIni, mMonthIni, mDayIni, sYearIni, sMonthIni, sDayIni,mYearFin, mMonthFin, mDayFin, sYearFin, sMonthFin, sDayFin;

    public static int c;

    private  String nombreEmpleadoPermiso;
    private  String tipoPermisoAlert;
     public static   EditText fechaFinAlert;
    private  String observaciones;
    private  String reporteDepto;
    private  String reporteNomUsuario;
    private  String reporteNombre;

    EditText editreporteNombre;
    AlertDialog.Builder imagenD;
  private  int opcionReporte;
    View view1;


    private int position2;
    private String idPermiso;

    AdapterListPermisos adapterListPermiso;
    private String idAdmin;
    private File file;
    private FileOutputStream os;
    private Workbook wb;
    Cursor cursorList;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AdaptadorDeAllPermisos adapter;
    private AlertDialog alertDialog;
    private AdapterListDeptos adapterListDeptos;
    private int eventoDeDialogo;
    private ListView listaDeptos;
    private Cursor cursorConsulta;
    private DepartamentosGetYSet departamentosGetYSet;
    private int idDepto;
    private String idUser;
    EditText editDeptos;
    EditText editEmpleados;
    private ArrayList<PojoEmpleados> arrayListEmpleado;
    private PojoEmpleados pojoEmpleados;
    private ListView listaEmpleados;
    private AdapterListEmpleado adapterListEmpleado;
    public static EditText fechaInicioAlert;
    private SwipeRefreshLayout swipeRefreshLayout;
    Cursor cursorDeparatamentos;
    private String tipoReporte;

    @Override
    public void onResume() {
        super.onResume();

   cargarLista();
        new Handler().postDelayed(new Runnable(){
            public void run(){
                 //----------------------------
               cargarLista();
                //----------------------------

            }
        }, 5000);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
       cargarLista();
    }





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_permiso, container, false);
         setHasOptionsMenu(true);
        cargarPreferencias();
        swipeRefreshLayout = (SwipeRefreshLayout) vista.findViewById(R.id.swipeRefreshLayoutPermisos);

        datos = OperacionesBaseDatos
                .obtenerInstancia(getContext());

        String[] selectionArgs = new String[]{"1"};

         cursorDeparatamentos = getContext().getContentResolver().query(ContractParaDepartamentos.CONTENT_URI, null, "pendiente_insercion =?", selectionArgs, null);
        if(cursorDeparatamentos.getCount()>0){

        }else {

            SyncAdapterPermisos.inicializarSyncAdapter(getContext());

            SyncAdapterPermisos.sincronizarAhora(getActivity(), true);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto

                if(cursorDeparatamentos.getCount()>0){
                    SyncAdapterDeptos.inicializarSyncAdapter(getActivity());


                    SyncAdapterDeptos.sincronizarAhora(getActivity(), true);

                }else {
                    SyncAdapterPermisos.inicializarSyncAdapter(getActivity());


                    SyncAdapterPermisos.sincronizarAhora(getActivity(), false);
                }

                new Handler().postDelayed(new Runnable(){
                    public void run(){
                        //----------------------------
                        cargarLista();
                        //----------------------------

                    }
                }, 5000);
                swipeRefreshLayout.setRefreshing(false);
            }
        });



        fechaDispositivo = Calendar.getInstance();
        sMonthIni = fechaDispositivo.get(Calendar.MONTH) + 1;
        sDayIni = fechaDispositivo.get(Calendar.DAY_OF_MONTH);
        sYearIni = fechaDispositivo.get(Calendar.YEAR);
        sMonthFin = fechaDispositivo.get(Calendar.MONTH) + 1;
        sDayFin = fechaDispositivo.get(Calendar.DAY_OF_MONTH);
        sYearFin = fechaDispositivo.get(Calendar.YEAR);

        SyncAdapterPermisos.inicializarSyncAdapter(getContext());

        FloatingActionButton fab = (FloatingActionButton) vista.findViewById(R.id.bttnGenerarReportes);

        recyclerView = (RecyclerView) vista.findViewById(R.id.listaAllpermisos);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getContext(), ListaEmpleados.class);

                Bundle bundle = new Bundle();

                bundle.putInt("variable", 2);


                intent.putExtras(bundle);
                startActivity(intent);



            }


        });





        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdaptadorDeAllPermisos(getContext(),this,  this);



       cargarLista();











        return vista;


    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.busqueda_menu, menu);
        MenuItem item = menu.findItem(R.id.buscar);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.ExportarTodo:




                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            100);
                }else {
                    opcionReporte =1;

                    cursorList =datos.obtenerJoinsDePermisos();
                   tipoReporte ="Reporte de todos los permisos";
                    if(cursorList.getCount() >0){
                        alertNombreExcel();
                    }else{
                        Toast.makeText(getContext(), "No hay permisos", Toast.LENGTH_SHORT).show();
                    }


                }


                break;

            case R.id.ExportarUltimoMes:



                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            100);
                }else {
                    opcionReporte =2;
                    if (sMonthIni == 1) {
                        sYearIni = sYearIni - 1;


                        fechaInicial = sYearIni + "-" + (sMonthIni + 11) + "-" + sDayIni;

                    } else {



                        fechaInicial = sYearIni + "-" + (sMonthIni - 1) + "-" + sDayIni;


                    }
                    fechaFinal = sYearFin + "-" + (sMonthFin) + "-" + sDayFin;


                        cursorList= datos.obtenerJoinsDePermisosFecha(fechaInicial,fechaFinal);
                     if(cursorList.getCount() >0){
                         tipoReporte ="Reporte del último mes";
                        alertNombreExcel();
                    }else{
                        Toast.makeText(getContext(), "No hay del ultimo mes.", Toast.LENGTH_SHORT).show();
                    }











                    sMonthIni = fechaDispositivo.get(Calendar.MONTH) + 1;
                    sDayIni = fechaDispositivo.get(Calendar.DAY_OF_MONTH);
                    sYearIni = fechaDispositivo.get(Calendar.YEAR);
                    sMonthFin = fechaDispositivo.get(Calendar.MONTH) + 1;
                    sDayFin = fechaDispositivo.get(Calendar.DAY_OF_MONTH);
                    sYearFin = fechaDispositivo.get(Calendar.YEAR);

                }

                break;

            case R.id.ExportarPersonalizado:
                Intent intentInfo = new Intent(getContext(),Reportes.class);
                startActivity(intentInfo);

                break;

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
            case R.id.BorrarPermisos:


                imagenD = new android.app.AlertDialog.Builder(getContext());


                LayoutInflater factory = LayoutInflater.from(getContext());

                View dialogoLayout = factory.inflate(R.layout.alertborrarpermisos, null, false);
                 editDeptos = dialogoLayout.findViewById(R.id.obtenertDeptos);
                 editEmpleados = dialogoLayout.findViewById(R.id.obtenertEmpleadoss);
                 fechaInicioAlert = dialogoLayout.findViewById(R.id.fechaInicioAlert);
                 fechaFinAlert = dialogoLayout.findViewById(R.id.fechaFinAlert);
                Button borrar = dialogoLayout.findViewById(R.id.botonAlert);
                Button reset= dialogoLayout.findViewById(R.id.botonReset);

                imagenD.setMessage("                 Borrar Permisos");

                imagenD.setView(dialogoLayout);

                alertDialog = imagenD.create();
                borrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor cursorPermisos = null;

                        if (editDeptos.getText().toString().equalsIgnoreCase("") && editEmpleados.getText().toString().equalsIgnoreCase("") && fechaInicioAlert.getText().toString().equalsIgnoreCase("") && fechaFinAlert.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(getContext(), "No hay ningun dato ingresado.", Toast.LENGTH_SHORT).show();
                        }if (fechaInicioAlert.getText().toString().equalsIgnoreCase("") && fechaFinAlert.getText().toString().length() >0||fechaInicioAlert.getText().toString().length()>0 && fechaFinAlert.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(getContext(), "Ingrese las 2 fechas.", Toast.LENGTH_SHORT).show();


                        } else if (editEmpleados.getText().toString().equalsIgnoreCase("") && fechaInicioAlert.getText().toString().equalsIgnoreCase("") && fechaFinAlert.getText().toString().equalsIgnoreCase("")) {
                            String[] selectionArgs = new String[]{String.valueOf(idDepto),"0",};
                            cursorPermisos=  getContext().getContentResolver().query(ContractParaPermisos.CONTENT_URI, null,"idDepartamento =? and eliminado =?",selectionArgs,null);
                            alertDialog.dismiss();
                            if(cursorPermisos.getCount()>0){

                                ContentValues values3 = new ContentValues();
                                String[] selectionArgs3 = new String[]{String.valueOf(idDepto),"0"};
                                values3.put(ContractParaPermisos.Columnas.PENDIENTE_INSERCION, 1);
                                values3.put(ContractParaPermisos.Columnas.PENDIENTE_ARCHIVADO, 1);
                                values3.put(ContractParaPermisos.Columnas.ESTADO, 0);
                                getContext().getContentResolver().update(ContractParaPermisos.CONTENT_URI, values3, "idDepartamento =? and eliminado =?", selectionArgs3);
                                SyncAdapterPermisos.inicializarSyncAdapter(getContext());

                                SyncAdapterPermisos.sincronizarAhora(getContext(), true);
                                cargarLista();


                            }else {


                                Toast.makeText(getContext(), "No hay permisos para este departamento.", Toast.LENGTH_SHORT).show();
                            }


                        } else if (editDeptos.getText().toString().equalsIgnoreCase("") && fechaInicioAlert.getText().toString().equalsIgnoreCase("") && fechaFinAlert.getText().toString().equalsIgnoreCase("")) {
                            String[] selectionArgs = new String[]{String.valueOf(idUser),"0",};
                            cursorPermisos=  getContext().getContentResolver().query(ContractParaPermisos.CONTENT_URI, null,"idEmpleado =? and eliminado =?",selectionArgs,null);

                            if(cursorPermisos.getCount()>0){
                                alertDialog.dismiss();
                                ContentValues values3 = new ContentValues();
                                String[] selectionArgs3 = new String[]{String.valueOf(idUser),"0"};
                                values3.put(ContractParaPermisos.Columnas.PENDIENTE_INSERCION, 1);
                                values3.put(ContractParaPermisos.Columnas.PENDIENTE_ARCHIVADO, 1);
                                values3.put(ContractParaPermisos.Columnas.ESTADO, 0);
                                getContext().getContentResolver().update(ContractParaPermisos.CONTENT_URI, values3, "idEmpleado =? and eliminado =?", selectionArgs3);
                                SyncAdapterPermisos.inicializarSyncAdapter(getContext());
                                SyncAdapterPermisos.sincronizarAhora(getContext(), true);
                                cargarLista();

                                Toast.makeText(getContext(), "Listo.", Toast.LENGTH_SHORT).show();

                            }else {
                                alertDialog.dismiss();
                                Toast.makeText(getContext(), "No hay permisos para este empleado.", Toast.LENGTH_SHORT).show();
                            }



                        } else if (fechaInicioAlert.getText().toString().equalsIgnoreCase("") && fechaFinAlert.getText().toString().equalsIgnoreCase("")) {

                            String[] selectionArgs = new String[]{String.valueOf(idUser),"0",};
                            cursorPermisos=  getContext().getContentResolver().query(ContractParaPermisos.CONTENT_URI, null,"idEmpleado =? and eliminado =?",selectionArgs,null);

                            if(cursorPermisos.getCount() >0){
                                alertDialog.dismiss();
                                ContentValues values3 = new ContentValues();
                                String[] selectionArgs3 = new String[]{String.valueOf(idUser),"0"};
                                values3.put(ContractParaPermisos.Columnas.PENDIENTE_INSERCION, 1);
                                values3.put(ContractParaPermisos.Columnas.PENDIENTE_ARCHIVADO, 1);
                                values3.put(ContractParaPermisos.Columnas.ESTADO, 0);
                                getContext().getContentResolver().update(ContractParaPermisos.CONTENT_URI, values3, "idEmpleado =? and eliminado =?", selectionArgs3);
                                SyncAdapterPermisos.inicializarSyncAdapter(getContext());
                                SyncAdapterPermisos.sincronizarAhora(getContext(), true);
                                cargarLista();

                                Toast.makeText(getContext(), "Listo.", Toast.LENGTH_SHORT).show();

                            }else {
                                alertDialog.dismiss();
                                Toast.makeText(getContext(), "No hay permisos para este empleado.", Toast.LENGTH_SHORT).show();
                            }

                        }else if (editDeptos.getText().toString().equalsIgnoreCase("")&&editEmpleados.getText().toString().equalsIgnoreCase(""))
                        {
                            String[] selectionArgs = new String[]{fechaInicioAlert.getText().toString(),fechaFinAlert.getText().toString(),"0"};
                            cursorPermisos=  getContext().getContentResolver().query(ContractParaPermisos.CONTENT_URI, null,"fechaInicio >=? and fechaFin <=? and eliminado =?",selectionArgs,null);

                            if(cursorPermisos.getCount()>0){
                                alertDialog.dismiss();
                                ContentValues values3 = new ContentValues();
                                String[] selectionArgs3 = new String[]{String.valueOf(fechaInicioAlert.getText().toString()),fechaFinAlert.getText().toString(),"0"};
                                values3.put(ContractParaPermisos.Columnas.PENDIENTE_INSERCION, 1);
                                values3.put(ContractParaPermisos.Columnas.PENDIENTE_ARCHIVADO, 1);
                                values3.put(ContractParaPermisos.Columnas.ESTADO, 0);
                                getContext().getContentResolver().update(ContractParaPermisos.CONTENT_URI, values3, "fechaInicio >= ? and fechaFin <=? and eliminado =?", selectionArgs3);
                                SyncAdapterPermisos.inicializarSyncAdapter(getContext());
                                SyncAdapterPermisos.sincronizarAhora(getContext(), true);
                                cargarLista();

                                Toast.makeText(getContext(), "Listo.", Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(getContext(), "No hay permisos registrados con estas fechas.", Toast.LENGTH_SHORT).show();
                            }


                        }else if (editEmpleados.getText().toString().equalsIgnoreCase("")) {
                            String[] selectionArgs = new String[]{String.valueOf(idDepto),fechaInicioAlert.getText().toString(), fechaFinAlert.getText().toString(), "0"};
                            cursorPermisos = getContext().getContentResolver().query(ContractParaPermisos.CONTENT_URI, null, "idDepartamento =? and fechaInicio >=? and fechaFin <=? and eliminado =?", selectionArgs, null);

                            if (cursorPermisos.getCount() > 0) {
                                ContentValues values3 = new ContentValues();
                                alertDialog.dismiss();
                                String[] selectionArgs3 = new String[]{String.valueOf(idDepto),fechaInicioAlert.getText().toString(), fechaFinAlert.getText().toString(), "0"};
                                values3.put(ContractParaPermisos.Columnas.PENDIENTE_INSERCION, 1);
                                values3.put(ContractParaPermisos.Columnas.PENDIENTE_ARCHIVADO, 1);
                                values3.put(ContractParaPermisos.Columnas.ESTADO, 0);
                                getContext().getContentResolver().update(ContractParaPermisos.CONTENT_URI, values3, "idDepartamento =? and fechaInicio >=? and fechaFin <=? and eliminado =?", selectionArgs3);
                                SyncAdapterPermisos.inicializarSyncAdapter(getContext());
                                SyncAdapterPermisos.sincronizarAhora(getContext(), true);
                                cargarLista();

                                Toast.makeText(getContext(), "Listo.", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getContext(), "No hay permisos registrados con esta busqueda.", Toast.LENGTH_SHORT).show();
                            }

                        }


                        else {
                            String[] selectionArgs = new String[]{idUser, String.valueOf(idDepto),fechaInicioAlert.getText().toString(),fechaFinAlert.getText().toString(),"0"};
                            cursorPermisos=  getContext().getContentResolver().query(ContractParaPermisos.CONTENT_URI, null,"idEmpleado =? and idDepartamento and fechaInicio >=? and fechaFin <=? and eliminado =? ",selectionArgs,null);

                            if(cursorPermisos.getCount()>0){
                                alertDialog.dismiss();
                                ContentValues values3 = new ContentValues();
                                String[] selectionArgs3 = new String[]{idUser, String.valueOf(idDepto),fechaInicioAlert.getText().toString(),fechaFinAlert.getText().toString(),"0"};
                                values3.put(ContractParaPermisos.Columnas.PENDIENTE_INSERCION, 1);
                                values3.put(ContractParaPermisos.Columnas.PENDIENTE_ARCHIVADO, 1);
                                values3.put(ContractParaPermisos.Columnas.ESTADO, 0);
                                getContext().getContentResolver().update(ContractParaPermisos.CONTENT_URI, values3, "idEmpleado =? and idDepartamento and fechaInicio >=? and fechaFin <=? and eliminado =? ", selectionArgs3);
                                SyncAdapterPermisos.inicializarSyncAdapter(getContext());
                                SyncAdapterPermisos.sincronizarAhora(getContext(), true);
                                cargarLista();

                                Toast.makeText(getContext(), "Listo.", Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(getContext(), "No hay permisos para esta busqueda.", Toast.LENGTH_SHORT).show();
                            }


                        }
                        if(cursorPermisos.getCount() >1){
                             alertDialog.dismiss();

                        }
                     }
                });






                editDeptos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        obtenerDeptos();


                    }
                });
                reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        editDeptos.setText("");
                        editEmpleados.setText("");
                        fechaFinAlert.setText("");
                        fechaInicioAlert.setText("");
                        idDepto =0;


                    }
                });


                editEmpleados.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        obtenerEmpleados();


                    }
                });

                fechaInicioAlert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DialogFragment newFragment = new SelectDateFragment();
                        newFragment.show(getFragmentManager(), "DatePicker");
                    }
                });

                fechaFinAlert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DialogFragment newFragment = new SelectDateFragmentFin();
                        newFragment.show(getFragmentManager(), "DatePicker");
                    }
                });





                alertDialog.show();

                break;
            case R.id.ArchivarPermisos:
                Intent intent = new Intent(getContext(), PermisosArchivados.class);
                 startActivity(intent);

                break;



        }

        return super.onOptionsItemSelected(item);
    }
    private ArrayList<DepartamentosGetYSet> filtrarDatosDeptos(ArrayList<DepartamentosGetYSet> listaTarea, String dato) {
        ArrayList<DepartamentosGetYSet> listaFiltradaDeptos = new ArrayList<>();
        try{
            dato = dato.toLowerCase();
            for(DepartamentosGetYSet deptos: listaTarea){
                String nombre = deptos.getNombreDepto().toLowerCase();


                if(nombre.toLowerCase().contains(dato) ){
                    listaFiltradaDeptos.add(deptos);
                }
            }
            adapterListDeptos.filtrar(listaFiltradaDeptos);
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
        }

        return listaFiltradaDeptos;

    }

    private void obtenerDeptos() {

        SearchView editFiltro = null;
        try {
             eventoDeDialogo =0;
            imagenD = new android.app.AlertDialog.Builder(getContext());


            LayoutInflater factory = LayoutInflater.from(getContext());
            View dialogoLayout = factory.inflate(R.layout.lista_deptos_dialog, null, false);

            listaDeptos = (ListView) dialogoLayout.findViewById(R.id.listaDeptos);
            imagenD.setMessage("\t\t\tDepartamentos");
            contes2 = new ArrayList<DepartamentosGetYSet>();

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                editFiltro =(SearchView) dialogoLayout.findViewById(R.id.filtroDeptos);
            }

            imagenD.setView(dialogoLayout);


            String[] selectionArgs = new String[]{"0",};

            cursorConsulta=  getContext().getContentResolver().query(ContractParaDepartamentos.CONTENT_URI, null,"eliminado =?",selectionArgs,null);


            while (cursorConsulta.moveToNext()){
                 departamentosGetYSet = new DepartamentosGetYSet();



                departamentosGetYSet.setNombreDepto( cursorConsulta.getString(cursorConsulta.getColumnIndex("nombreDepto")));
                departamentosGetYSet.setId( cursorConsulta.getString(cursorConsulta.getColumnIndex("_id")));
                departamentosGetYSet.setIdRemota(cursorConsulta.getInt(cursorConsulta.getColumnIndex("idRemota")));
                contes2.add(departamentosGetYSet);
            }


            editFiltro.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    if (!(adapterListDeptos == null)) {
                        ArrayList<DepartamentosGetYSet> listaDeptosFiltrada = null;
                        try {
                            listaDeptosFiltrada = filtrarDatosDeptos(contes2, s.trim());
                            adapterListDeptos.filtrar(listaDeptosFiltrada);
                            adapterListDeptos.notifyDataSetChanged();
                        }catch (Exception e){
                            Toast.makeText(getContext(), ""+listaDeptosFiltrada, Toast.LENGTH_SHORT).show();

                        }
                        return true;



                    }

                    return false;
                }
            });




            adapterListDeptos = new AdapterListDeptos(getContext(),contes2);
            listaDeptos.setAdapter(adapterListDeptos);
            listaDeptos.setOnItemClickListener(PermisoFragment.this);






            alertDialog = imagenD.create();


            alertDialog.show();
        }catch (Exception e){
            Toast.makeText(getContext() ,"Problemas con la red",Toast.LENGTH_LONG).show();

        }
    }


    private void obtenerEmpleados() {


        try {

            eventoDeDialogo =1;
            SearchView editFiltro = null;

             arrayListEmpleado = new ArrayList<PojoEmpleados>();

            if(idDepto ==0){

                String[] selectionArgs = new String[]{"0"};

                cursorConsulta=  getContext().getContentResolver().query(ContractParaEmpleados.CONTENT_URI, null,"eliminado =?",selectionArgs,null);

            }else{

                String[] selectionArgs = new String[]{"0", String.valueOf(idDepto)};

                cursorConsulta=  getContext().getContentResolver().query(ContractParaEmpleados.CONTENT_URI, null,"eliminado =? and codDepartamento =?",selectionArgs,null);

            }


            while (cursorConsulta.moveToNext()){
                pojoEmpleados = new PojoEmpleados();



                pojoEmpleados.setName( cursorConsulta.getString(cursorConsulta.getColumnIndex("name")));
                pojoEmpleados.setId( cursorConsulta.getString(cursorConsulta.getColumnIndex("_id")));
                pojoEmpleados.setEmail(cursorConsulta.getString(cursorConsulta.getColumnIndex("email")));
                pojoEmpleados.setPicture(cursorConsulta.getString(cursorConsulta.getColumnIndex("picture")));
                arrayListEmpleado.add(pojoEmpleados);
            }

            if(cursorConsulta.getCount()>0) {
                imagenD = new android.app.AlertDialog.Builder(getContext());


                LayoutInflater factory = LayoutInflater.from(getContext());

                View dialogoLayout = factory.inflate(R.layout.lista_empleados, null, false);


                 listaEmpleados = (ListView) dialogoLayout.findViewById(R.id.listaEmpleadoAlertDialog);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    editFiltro =(SearchView) dialogoLayout.findViewById(R.id.editFiltro);
                }


                imagenD.setMessage("                     Empleados");
                imagenD.setView(dialogoLayout);

                editFiltro.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                         if (!(adapterListEmpleado == null)) {
                            ArrayList<PojoEmpleados> listaDeptosFiltrada = null;
                            try {
                                listaDeptosFiltrada = filtrarDatos(arrayListEmpleado, s.trim());
                                adapterListEmpleado.filtrar(listaDeptosFiltrada);
                                adapterListEmpleado.notifyDataSetChanged();
                            }catch (Exception e){
                                Toast.makeText(getContext(), ""+listaDeptosFiltrada, Toast.LENGTH_SHORT).show();

                            }
                            return true;



                        }

                        return false;
                    }
                });






//https://www.youtube.com/watch?v=rHhpK3JQ1So
                //http://androidcodeon.blogspot.com/2016/04/custom-listview-alertdialog-with-filter.html




                adapterListEmpleado = new AdapterListEmpleado(getContext(), arrayListEmpleado);
                listaEmpleados.setAdapter(adapterListEmpleado);
                listaEmpleados.setOnItemClickListener(this);




                alertDialog = imagenD.create();



                alertDialog.show();
            }else {
                if (idDepto ==0){
                    Toast.makeText(getContext(), "No hay empleados ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "No hay empleados en este departamento. ", Toast.LENGTH_SHORT).show();

                }

            }
        }catch (Exception e){
            Toast.makeText(getContext() ,"Problemas con la red"+e,Toast.LENGTH_LONG).show();
        }

    }

    private ArrayList<PojoEmpleados> filtrarDatos(ArrayList<PojoEmpleados> listaTarea, String dato) {
        ArrayList<PojoEmpleados> listaFiltrada = new ArrayList<>();
        try{
            dato = dato.toLowerCase();
            for(PojoEmpleados empleados: listaTarea){
                String nombre = empleados.getName().toLowerCase();
                String codigo = empleados.getId();

                if(nombre.toLowerCase().contains(dato)||codigo.toLowerCase().contains(dato)){
                    listaFiltrada.add(empleados);
                }
            }
            adapterListEmpleado.filtrar(listaFiltrada);
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
        }

        return listaFiltrada;

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

        }catch (Exception e){
            e.getStackTrace();
        }
        return true;
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
                    Toast.makeText(getContext(), "Intentelo denuevo" , Toast.LENGTH_SHORT).show();
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

    private boolean saveExcelFile(Context context, final String fileName) {

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");

            return false;
        }

        boolean success = false;

        //New Workbook
         wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet(tipoReporte);

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("Numero De Permiso");
        c.setCellStyle(cs);


        c = row.createCell(1);
        c.setCellValue("Tipo de Permiso");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("ID del Empleado");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Departamento");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("Empleado");
        c.setCellStyle(cs);



        c = row.createCell(5);
        c.setCellValue("Fecha de Inicio");
        c.setCellStyle(cs);

        c = row.createCell(6);
        c.setCellValue("Fecha de Fin");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 700));
        sheet1.setColumnWidth(2, (15 * 500));
        sheet1.setColumnWidth(3, (15 * 500));
        sheet1.setColumnWidth(4, (15 * 500));
        sheet1.setColumnWidth(5, (15 * 500));
        sheet1.setColumnWidth(6, (15 * 500));


        try {



            int a =1;


            while (cursorList.moveToNext()){





                row = sheet1.createRow( a );


                c = row.createCell(0 );

                c.setCellValue(cursorList.getString(cursorList.getColumnIndex("id")));



                c = row.createCell(1 );


                c.setCellValue(cursorList.getString(cursorList.getColumnIndex("tipoPermiso")));
                if(cursorList.getString(cursorList.getColumnIndex("tipoPermiso")).equals("PAA")) {
                    CellStyle cs3 = wb.createCellStyle();
                    cs3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                    cs3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);



                    c.setCellStyle(cs3);

                }else
                if(cursorList.getString(cursorList.getColumnIndex("tipoPermiso")).equals("PERMISO PERSONAL")) {
                    CellStyle cs4 = wb.createCellStyle();
                    cs4.setFillForegroundColor(HSSFColor.RED.index);
                    cs4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);



                    c.setCellStyle(cs4);

                }else
                if(cursorList.getString(cursorList.getColumnIndex("tipoPermiso")).equals("ACTIVIDAD UNAH-TEC DANLI")) {
                    CellStyle cs5 = wb.createCellStyle();
                    cs5.setFillForegroundColor(HSSFColor.YELLOW.index);
                    cs5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);



                    c.setCellStyle(cs5);

                }else
                if(cursorList.getString(cursorList.getColumnIndex("tipoPermiso")).equals("INCAPACIDAD")) {
                    CellStyle cs6 = wb.createCellStyle();
                    cs6.setFillForegroundColor(HSSFColor.VIOLET.index);
                    cs6.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);



                    c.setCellStyle(cs6);

                }else
                if(cursorList.getString(cursorList.getColumnIndex("tipoPermiso")).equals("VACACIONES")) {
                    CellStyle cs7 = wb.createCellStyle();
                    cs7.setFillForegroundColor(HSSFColor.GREEN.index);
                    cs7.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);


                    c.setCellStyle(cs7);

                }else
                if(cursorList.getString(cursorList.getColumnIndex("tipoPermiso")).equals("IHSS")) {
                    CellStyle cs8 = wb.createCellStyle();
                    cs8.setFillForegroundColor(HSSFColor.ORANGE.index);
                    cs8.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);



                    c.setCellStyle(cs8);

                }else
                if(cursorList.getString(cursorList.getColumnIndex("tipoPermiso")).equals("INTERRUPCION DEL FLUIDO ELECTRICO")) {
                    CellStyle cs9 = wb.createCellStyle();
                    cs9.setFillForegroundColor(HSSFColor.PINK.index);
                    cs9.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);



                    c.setCellStyle(cs9);

                }else
                if(cursorList.getString(cursorList.getColumnIndex("tipoPermiso")).equals("PROBLEMAS CON EL RELOJ MARCADOR")) {
                    CellStyle cs10 = wb.createCellStyle();
                    cs10.setFillForegroundColor(HSSFColor.BROWN.index);
                    cs10.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);



                    c.setCellStyle(cs10);

                }else
                if(cursorList.getString(cursorList.getColumnIndex("tipoPermiso")).equals("COMPENSATORIO POR TIEMPO EXTRA")) {
                    CellStyle cs11 = wb.createCellStyle();
                    cs11.setFillForegroundColor(HSSFColor.AQUA.index);
                    cs11.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);



                    c.setCellStyle(cs11);

                }else
                if(cursorList.getString(cursorList.getColumnIndex("tipoPermiso")).equals("PERMISO CON GOCE DE SUELDO")) {
                    CellStyle cs12 = wb.createCellStyle();
                    cs12.setFillForegroundColor(HSSFColor.LEMON_CHIFFON.index);
                    cs12.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

                    c.setCellStyle(cs12);

                }else
                if(cursorList.getString(cursorList.getColumnIndex("tipoPermiso")).equals("ACTIVIDAD SINDICAL")) {
                    CellStyle cs13 = wb.createCellStyle();
                    cs13.setFillForegroundColor(HSSFColor.GOLD.index);
                    cs13.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);



                    c.setCellStyle(cs13);

                }else
                if(cursorList.getString(cursorList.getColumnIndex("tipoPermiso")).equals("DIAS FERIADOS")) {
                    CellStyle cs14 = wb.createCellStyle();
                    cs14.setFillForegroundColor(HSSFColor.TURQUOISE.index);
                    cs14.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);



                    c.setCellStyle(cs14);

                }
                c = row.createCell(2 );
                c.setCellValue(cursorList.getString(cursorList.getColumnIndex("idEmpleado")));

                c = row.createCell(3 );
                c.setCellValue(cursorList.getString(cursorList.getColumnIndex("nombreDepto")));




                c = row.createCell(4 );
                c.setCellValue(cursorList.getString(cursorList.getColumnIndex("name")));




                c = row.createCell(5 );
                c.setCellValue(cursorList.getString(cursorList.getColumnIndex("fechaInicio")));
                c = row.createCell(6 );
                c.setCellValue(cursorList.getString(cursorList.getColumnIndex("fechaFin")));
               // sheet1.setColumnWidth(i, (15 * 500));




                ++a;

            }
            row = sheet1.createRow( a );


            c = row.createCell(5 );

            c.setCellValue("Total");
            c = row.createCell(6 );

            c.setCellValue(cursorList.getCount()+" Permisos");



        }catch (Exception e){

        }

        // Create a path where we will place our List of objects on external storage



        file = new File(Environment.getExternalStorageDirectory().toString(), "Permisos Unah-tec/Excel");
        if(!file.exists()) {
            file.mkdirs();
            file = new File(file, "" + fileName);
        }else{

            file = new File(file, "" + fileName);
        }



        os = null;


        try {

            if(file.exists()){
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setMessage("Ya existe un archivo con este nombre desea sobrescribir?")
                        .setTitle("Advertencia")
                        .setCancelable(false)
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dialog.cancel();

                                    }
                                })
                        .setPositiveButton("Guardar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                        try {

                                            os = new FileOutputStream(file);



                                            wb.write(os);

                                        }catch (IOException e){

                                        }
                                        Toast.makeText(getContext() ,"Reporte guardado en: "+file.toString(),Toast.LENGTH_LONG).show();
                                        alertVer(fileName);


                                    }

                                });


                android.app.AlertDialog alert = builder.create();
                alert.show();




            }else {
                os = new FileOutputStream(file);


                wb.write(os);

                Toast.makeText(context, "Reporte guardado en: "+file.toString(), Toast.LENGTH_LONG).show();
                alertVer(fileName);


            }

            Log.w("FileUtils", "Writing file" + file);
            success = true;

        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
            Toast.makeText(context ,"bien"+e,Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
            Toast.makeText(context ,"bien"+file+e,Toast.LENGTH_LONG).show();

        } finally {
            try {
                if (null != os)
                    os.close();

            } catch (Exception ex) {
            }
        }
        return success;
    }
    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;

        }
        return false;

    }


    public void alertNombreExcel()

    {
         imagenD = new AlertDialog.Builder(getContext());
        LayoutInflater factory = LayoutInflater.from(getContext());
        view1 = factory.inflate(R.layout.modelonombreexcel, null);
        editreporteNombre = (EditText) view1.findViewById(R.id.editNombreExcel);

        imagenD.setView(view1);
        //Estableciendole el titulo y la imagen al alertDialog




        imagenD.setInverseBackgroundForced(true);




        imagenD.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(editreporteNombre.getText().toString().equalsIgnoreCase("")){

                    Toast.makeText(getContext() ,"Escriba el nombre del archivo",Toast.LENGTH_LONG).show();
                    alertNombreExcel();



                }else {
                    reporteNombre = editreporteNombre.getText().toString();


                    saveExcelFile(getContext(), reporteNombre + ".xls");

                }

            }
        });
        imagenD.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        imagenD.create().show();

        return;
    }

    private void cargarPreferencias() {
        SharedPreferences preferences =this.getActivity().getSharedPreferences("credenciales",Context.MODE_PRIVATE);


        Utilidades.TOKEN = preferences.getString("token","");
        idAdmin = preferences.getString("id","");



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


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {


        return  null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursorList);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onClick(RecyclerView.ViewHolder holder, int idDepartamento) {
        observaciones = adapter.getCursor().getString(adapter.getCursor().getColumnIndex("observaciones"));
        if (observaciones ==null ||observaciones.equals("")) {

            Toast.makeText(getContext(), "Sin observaciones", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

    @Override
    public void onLongClick(RecyclerView.ViewHolder holder, int idDepartamento) {




        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Modificar o eliminar departamento")
                .setTitle("Advertencia")
                .setCancelable(false)

                .setNegativeButton("Modificar",
                        new DialogInterface.OnClickListener() {
                            private int contador;

                            public void onClick(DialogInterface dialog, int id) {
                                Intent intentotra = new Intent(getContext(), AgregarPermisos.class);
                                contador = 2;
                                Bundle bundle = new Bundle();

                                bundle.putString("numeroPermiso", adapter.getCursor().getString(adapter.getCursor().getColumnIndex("id")));
                                bundle.putString("tipoPermiso", adapter.getCursor().getString(adapter.getCursor().getColumnIndex("tipoPermiso")));
                                bundle.putString("fechaInicio", adapter.getCursor().getString(adapter.getCursor().getColumnIndex("fechaInicio")));
                                bundle.putString("fechaFin", adapter.getCursor().getString(adapter.getCursor().getColumnIndex("fechaFin")));
                                bundle.putString("observaciones", adapter.getCursor().getString(adapter.getCursor().getColumnIndex("observaciones")));
                                bundle.putString("idEmpleado", adapter.getCursor().getString(adapter.getCursor().getColumnIndex("idEmpleado")));
                                bundle.putString("idDepartamento", adapter.getCursor().getString(adapter.getCursor().getColumnIndex("idDepartamento")));
                                bundle.putInt("contador", contador);


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
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Seguro que desea eliminar , si lo hace se eliminaran todos los empleados que pertenecen a este departamento")
                                        .setTitle("Advertencia")
                                        .setCancelable(false)
                                        .setNegativeButton("cancelar",new DialogInterface.OnClickListener() {
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
                                                        String[] selectionArgs = new String[]{String.valueOf(adapter.getCursor().getString(adapter.getCursor().getColumnIndex("id")))};


                                                        getActivity().getContentResolver().update(ContractParaPermisos.CONTENT_URI, values,"id =?",selectionArgs);
                                                        cargarLista();
                                                        SyncAdapterPermisos.sincronizarAhora(getContext(), true);








                                                    }

                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }
    public void cargarLista(){
        datos = OperacionesBaseDatos
                .obtenerInstancia(getContext());

        cursorList = datos.obtenerJoinsDePermisos();


        while (cursorList.moveToNext()){

            cursorList.getString(cursorList.getColumnIndex("name"));
            cursorList.getString(cursorList.getColumnIndex("tipoPermiso"));
            cursorList.getString(cursorList.getColumnIndex("fechaInicio"));
            cursorList.getString(cursorList.getColumnIndex("fechaFin"));



        }
        adapter.swapCursor(cursorList);
        recyclerView.setAdapter(adapter);



    }
    public  void alertVer(final String fileName){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setMessage("Selecione una opción?")
                .setTitle("Advertencia")
                .setCancelable(false)

                .setNeutralButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Compartir",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if(file.exists()) {
                                    String[] mailto = {};
                                    Uri uri = Uri.fromFile(new File(file.toString()));
                                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hoja de Permiso");
                                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hola PDF se adjunta en este correo. ");
                                    emailIntent.setType("application/vnd.ms-excel");
                                    emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                    try {
                                        startActivity(Intent.createChooser(emailIntent, "Send email using:"));

                                    } catch (ActivityNotFoundException e) {

                                    }
                                }else {
                                    Toast.makeText(getContext(), "El archivo no se encontro en su dispositivo", Toast.LENGTH_SHORT).show();

                                }



                            }
                        })
                .setPositiveButton("Vizualizar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (file.exists()) {
                                    String url = file.toString();

                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
                                    intent.setDataAndType(Uri.fromFile(file), mimeType);
                                    Intent intent1 = Intent.createChooser(intent, "Open With");


                                    try {
                                        startActivity(intent1);

                                    } catch (ActivityNotFoundException e) {
                                        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=microsoft.office.excel&hl=es_HN")));
                                    }
                                }else {
                                    Toast.makeText(getContext(), "El archivo no se encontro en su dispositivo", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });




        android.app.AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (eventoDeDialogo==1) {
            PojoEmpleados pojoEmpleados = (PojoEmpleados) parent.getItemAtPosition(position);
            editEmpleados.setText(pojoEmpleados.getName());
             idUser = (pojoEmpleados.getId());
            alertDialog.dismiss();

        }else {

            DepartamentosGetYSet departamentosGetYSet = (DepartamentosGetYSet) parent.getItemAtPosition(position);
            editDeptos.setText(departamentosGetYSet.getNombreDepto());
            if(departamentosGetYSet.getIdRemota() ==0){
                idDepto = Integer.parseInt((departamentosGetYSet.getId()));
            }else{
                idDepto = departamentosGetYSet.getIdRemota();
            }

            alertDialog.dismiss();

           editEmpleados.setText("");
        }

    }
}


























