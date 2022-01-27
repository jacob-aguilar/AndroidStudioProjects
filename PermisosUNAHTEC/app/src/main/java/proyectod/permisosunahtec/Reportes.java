package proyectod.permisosunahtec;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import proyectod.permisosunahtec.adapters.AdapterListDeptos;
import proyectod.permisosunahtec.adapters.AdapterListEmpleado;
import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.providers.OperacionesBaseDatos;
import proyectod.permisosunahtec.utils.Utilidades;
import proyectod.permisosunahtec.web.DepartamentosGetYSet;
import proyectod.permisosunahtec.web.PojoEmpleados;

import static android.content.ContentValues.TAG;
import static proyectod.permisosunahtec.AgregarPermisos.DATE_ID;
import static proyectod.permisosunahtec.AgregarPermisos.DATE_ID1;

public class Reportes extends AppCompatActivity implements    AdapterView.OnItemClickListener {
    private EditText fechaInicio;
    private EditText fechaFin, filtroEditTextDeptos, filtroEditTextEmpleados;
    private Button botonRepotes;
    private TextView filtroTextView;
    private int anio, mes, dia;
    private CardView cardViewFiltro;
    private int eventoDeDialogo;

    private ArrayList<DepartamentosGetYSet> contes;
    private ArrayList<PojoEmpleados> contesEmpleado;
    private JSONArray lista;
    private String DATA_URL;
    Workbook wb;
    private ListView listaEmpleados, listaDeptos;
      private AdapterListEmpleado adapterListEmpleado;
     private AdapterListDeptos adapterListDeptos;
    private ArrayList<PojoEmpleados> arrayListEmpleado;
     android.app.AlertDialog.Builder imagenD;
    private int mYearIni, mMonthIni, mDayIni, sYearIni, sMonthIni, sDayIni,mYearFin, mMonthFin, mDayFin, sYearFin, sMonthFin, sDayFin;
     View view1;
    FileOutputStream os;
    private  int idDepto;
    private  String idUser;
     String url;
    Calendar C;
    EditText editreporteNombre;
    private  String reporteNombre;
    private Spinner tipoPermiso;
    Date date1;
    Date date2;
    File file;
    SimpleDateFormat sdf;
    private OperacionesBaseDatos datos;
    private Cursor cursorList;
    private DepartamentosGetYSet departamentosGetYSet;
    private PojoEmpleados pojoEmpleados;
    Cursor cursorConsulta;
     android.app.AlertDialog alertDialog;
    private String tipoDeReporte=null;
    private EditText nombreEmpleado;
    private EditText nombreDepartamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        datos = OperacionesBaseDatos
                .obtenerInstancia(this);
        cargarPreferencias();
        fechaInicio = (EditText) findViewById(R.id.fecInicioET);

        fechaFin = (EditText) findViewById(R.id.fecfinET);
        botonRepotes = (Button) findViewById(R.id.btngenerar);
        tipoPermiso = (Spinner) findViewById(R.id.filtroEditTextPermisos);
        C = Calendar.getInstance();
        sMonthIni = C.get(Calendar.MONTH);
        sDayIni = C.get(Calendar.DAY_OF_MONTH);
        sYearIni = C.get(Calendar.YEAR);


        filtroEditTextDeptos = findViewById(R.id.filtroEditTextDeptos);
        filtroEditTextEmpleados = findViewById(R.id.filtroEditTextEmpleados);
        filtroTextView = findViewById(R.id.filtroTextView);

        cardViewFiltro = findViewById(R.id.CV1);
        String[] letra = {"", "PERMISO PERSONAL",
                "ACTIVIDAD UNAH-TEC DANLI",
                "INCAPACIDAD", "VACACIONES",
                "PAA", "IHSS",
                "INTERRUPCION DEL FLUIDO ELECTRICO",
                "PROBLEMAS CON EL RELOJ MARCADOR",
                "COMPENSATORIO POR TIEMPO EXTRA",
                "PERMISOS CON GOCE DE SUELDO"
                ,"ACTIVIDAD SINDICAL",
                "DIAS FERIADOS"};
        tipoPermiso.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));



        fechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_ID);

            }
        });
        fechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_ID);

            }
        });



        fechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_ID1);

            }
        });


        filtroEditTextDeptos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerDeptos();


            }
        });

        filtroEditTextEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerEmpleados();


            }
        });

    }
    private void colocar_fecha() {


        String monthPiker = null;
        String dayPiker = null;
        mMonthIni =mMonthIni +1;
        if (mMonthIni <10){
            monthPiker = "0"+mMonthIni;
        }else {
            monthPiker = String.valueOf(mMonthIni);
        }
        if (mDayIni <10){
            dayPiker = "0"+mDayIni;
        }else{
            dayPiker = String.valueOf(mDayIni);
        }
        fechaInicio.setText(mYearIni + "-" + (monthPiker ) + "-" + dayPiker);
    }

    private void colocar_fecha2() {
        String monthPiker = null;
        String dayPiker = null;
        mMonthFin =mMonthFin +1;
        if (mMonthFin <10){
            monthPiker = "0"+mMonthFin;
        }else {
            monthPiker = String.valueOf(mMonthFin);
        }
        if (mDayFin <10){
            dayPiker = "0"+mDayFin;
        }else{
            dayPiker = String.valueOf(mDayFin);
        }
        fechaFin.setText(mYearIni + "-" + (monthPiker) + "-" + dayPiker);
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYearIni = year;
                    mMonthIni = monthOfYear;
                    mDayIni = dayOfMonth;
                    colocar_fecha();

                }

            };

    private DatePickerDialog.OnDateSetListener mDateSetListener2 =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYearFin = year;
                    mMonthFin = monthOfYear;
                    mDayFin = dayOfMonth;
                    colocar_fecha2();

                }

            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_ID:
                return new DatePickerDialog(this, mDateSetListener, sYearIni, sMonthIni, sDayIni);

            case DATE_ID1:
                return new DatePickerDialog(this, mDateSetListener2, sYearIni, sMonthIni, sDayIni);


        }


        return null;
    }












    private void obtenerEmpleados() {


        try {

            eventoDeDialogo =1;
            SearchView editFiltro = null;

            arrayListEmpleado = new ArrayList<PojoEmpleados>();

            if(idDepto ==0){

                String[] selectionArgs = new String[]{"0"};

                cursorConsulta=  getApplicationContext().getContentResolver().query(ContractParaEmpleados.CONTENT_URI, null,"eliminado =?",selectionArgs,null);

            }else{

                String[] selectionArgs = new String[]{"0", String.valueOf(idDepto)};

                cursorConsulta=  getApplicationContext().getContentResolver().query(ContractParaEmpleados.CONTENT_URI, null,"eliminado =? and codDepartamento =?",selectionArgs,null);

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
                imagenD = new android.app.AlertDialog.Builder(this);


                 LayoutInflater factory = LayoutInflater.from(this);

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
                                Toast.makeText(getApplicationContext(), ""+listaDeptosFiltrada, Toast.LENGTH_SHORT).show();
                            }
                            return true;
                         }

                        return false;
                    }
                });






//https://www.youtube.com/watch?v=rHhpK3JQ1So
                //http://androidcodeon.blogspot.com/2016/04/custom-listview-alertdialog-with-filter.html




                 adapterListEmpleado = new AdapterListEmpleado(this, arrayListEmpleado);
                listaEmpleados.setAdapter(adapterListEmpleado);
                listaEmpleados.setOnItemClickListener(this);




               alertDialog = imagenD.create();



                alertDialog.show();
            }else {
                if (idDepto ==0){
                    Toast.makeText(this, "No hay empleados ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "No hay empleados en este departamento. ", Toast.LENGTH_SHORT).show();

                }

            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext() ,"Problemas con la red"+e,Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }

        return listaFiltrada;

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
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }

        return listaFiltradaDeptos;

    }


    private void obtenerDeptos() {

        SearchView editFiltro = null;
try {
    eventoDeDialogo =0;
    imagenD = new android.app.AlertDialog.Builder(this);


    LayoutInflater factory = LayoutInflater.from(this);
    View dialogoLayout = factory.inflate(R.layout.lista_deptos_dialog, null, false);

    listaDeptos = (ListView) dialogoLayout.findViewById(R.id.listaDeptos);
    imagenD.setMessage("\t\t\tDepartamentos");
    contes = new ArrayList<DepartamentosGetYSet>();

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
        editFiltro =(SearchView) dialogoLayout.findViewById(R.id.filtroDeptos);
    }

    imagenD.setView(dialogoLayout);


    String[] selectionArgs = new String[]{"0",};

    cursorConsulta=  getApplicationContext().getContentResolver().query(ContractParaDepartamentos.CONTENT_URI, null,"eliminado =?",selectionArgs,null);


    while (cursorConsulta.moveToNext()){
        departamentosGetYSet = new DepartamentosGetYSet();



        departamentosGetYSet.setNombreDepto( cursorConsulta.getString(cursorConsulta.getColumnIndex("nombreDepto")));
        departamentosGetYSet.setId( cursorConsulta.getString(cursorConsulta.getColumnIndex("_id")));
        departamentosGetYSet.setIdRemota(cursorConsulta.getInt(cursorConsulta.getColumnIndex("idRemota")));
        contes.add(departamentosGetYSet);
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
                    listaDeptosFiltrada = filtrarDatosDeptos(contes, s.trim());
                    adapterListDeptos.filtrar(listaDeptosFiltrada);
                    adapterListDeptos.notifyDataSetChanged();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), ""+listaDeptosFiltrada, Toast.LENGTH_SHORT).show();

                }
                return true;



            }

            return false;
        }
    });




    adapterListDeptos = new AdapterListDeptos(this,contes);
    listaDeptos.setAdapter(adapterListDeptos);
    listaDeptos.setOnItemClickListener(this);






    alertDialog = imagenD.create();


    alertDialog.show();
}catch (Exception e){
    Toast.makeText(getApplicationContext() ,"Problemas con la red",Toast.LENGTH_LONG).show();

}
    }




// set current date into datepicker



    private void invocarPermiso() {



            try {
                sdf = new SimpleDateFormat("yyyy-MM-dd");

                date1 = sdf.parse(fechaInicio.getText().toString());
                date2 = sdf.parse(fechaFin.getText().toString());
            } catch (Exception e) {

            }
            if (filtroEditTextDeptos.getText().toString().equalsIgnoreCase("") && filtroEditTextEmpleados.getText().toString().equalsIgnoreCase("") &&
                    fechaInicio.getText().toString().equalsIgnoreCase("") && fechaFin.getText().toString().equalsIgnoreCase("") && tipoPermiso.getSelectedItem().toString().equalsIgnoreCase("")) {

                Toast.makeText(getApplicationContext(), "Al menos debe ingresar unos de los datos para generar un reporte", Toast.LENGTH_LONG).show();

            } else if (fechaFin.getText().toString().equalsIgnoreCase("") && fechaInicio.getText().toString().length() > 0 || fechaInicio.getText().toString().equalsIgnoreCase("") && fechaFin.getText().toString().length() > 0) {
                Toast.makeText(getApplicationContext(), "Se debe Agregar la fecha de inicio y de fin.", Toast.LENGTH_LONG).show();

            } else if (filtroEditTextDeptos.getText().toString().equalsIgnoreCase("") && filtroEditTextEmpleados.getText().toString().equalsIgnoreCase("")
                    && tipoPermiso.getSelectedItem().toString().equalsIgnoreCase("")) {
                if (date2.before(date1) ) {

                    Toast.makeText(getApplicationContext(),"fecha inicial no puede ser mayor a la fecha final", Toast.LENGTH_LONG).show();

                } else {

                     cursorList = datos.obtenerJoinsDePermisosFecha(fechaInicio.getText().toString(),fechaFin.getText().toString());
                      if(fechaInicio.getText().toString().equals(fechaFin.getText().toString())){
                         tipoDeReporte = "Reporte de "+fechaInicio.getText().toString() ;

                     }else {
                         tipoDeReporte = "Reporte de " + fechaInicio.getText().toString() + " a " + fechaFin.getText().toString();
                     }
                 }
            } else if (fechaInicio.getText().toString().equalsIgnoreCase("") && filtroEditTextEmpleados.getText().toString().equalsIgnoreCase("")
                    && fechaFin.getText().toString().equalsIgnoreCase("") && tipoPermiso.getSelectedItem().toString().equalsIgnoreCase("")) {

                cursorList = datos.obtenerJoinsDePermisosDeptos(String.valueOf(idDepto));
                tipoDeReporte = "Reporte del departamento de "+filtroEditTextDeptos.getText().toString();



            } else if (fechaInicio.getText().toString().equalsIgnoreCase("") && filtroEditTextDeptos.getText().toString().equalsIgnoreCase("")
                    && fechaFin.getText().toString().equalsIgnoreCase("") && tipoPermiso.getSelectedItem().toString().equalsIgnoreCase("")) {

                     cursorList= datos.obtenerJoinsDePermisosEmpleado(idUser);
                tipoDeReporte = "Reporte del Empleado "+filtroEditTextEmpleados.getText().toString();



            } else if (fechaInicio.getText().toString().equalsIgnoreCase("") && filtroEditTextDeptos.getText().toString().equalsIgnoreCase("")
                    && fechaFin.getText().toString().equalsIgnoreCase("") && filtroEditTextEmpleados.getText().toString().equalsIgnoreCase("")) {

                cursorList =datos.obtenerJoinsDePermisosTipoPermiso(tipoPermiso.getSelectedItem().toString());
                tipoDeReporte = "Reporte por tipo de permiso "+tipoPermiso.getSelectedItem().toString();



            } else if (fechaInicio.getText().toString().equalsIgnoreCase("") && tipoPermiso.getSelectedItem().toString().equalsIgnoreCase("")
                    && fechaFin.getText().toString().equalsIgnoreCase("")) {

                cursorList = datos.obtenerJoinsDePermisosDeptoUsers(idUser, String.valueOf(idDepto));
                tipoDeReporte = "Reporte del empleado "+filtroEditTextEmpleados.getText().toString() ;


            } else if (fechaInicio.getText().toString().equalsIgnoreCase("") && filtroEditTextEmpleados.getText().toString().equalsIgnoreCase("")
                    && fechaFin.getText().toString().equalsIgnoreCase("")) {


                cursorList = datos.obtenerJoinsDePermisosDeptoPermiso(tipoPermiso.getSelectedItem().toString(), String.valueOf(idDepto));
                tipoDeReporte = "Reporte del departamento de "+filtroEditTextDeptos.getText().toString()+" por tipo de permiso "+tipoPermiso.getSelectedItem().toString();

            } else if (fechaInicio.getText().toString().equalsIgnoreCase("") && filtroEditTextDeptos.getText().toString().equalsIgnoreCase("")
                    && fechaFin.getText().toString().equalsIgnoreCase("")) {

                cursorList =datos.obtenerJoinsDePermisosEmpleadoPermiso(tipoPermiso.getSelectedItem().toString(),idUser);
                tipoDeReporte = "Reporte del empleado "+filtroEditTextEmpleados.getText().toString()+" por tipo de permiso "+tipoPermiso.getSelectedItem().toString();


            } else if (filtroEditTextDeptos.getText().toString().equalsIgnoreCase("") && tipoPermiso.getSelectedItem().toString().equalsIgnoreCase("")
            ) {

                if (date2.before(date1) ) {

                    Toast.makeText(getApplicationContext(),"fecha inicial no puede ser mayor a la fecha final", Toast.LENGTH_LONG).show();

                } else {

                    cursorList =datos.obtenerJoinsDePermisosFechaEmpleado(fechaInicio.getText().toString(),fechaFin.getText().toString(),idUser);
                    if (fechaInicio.getText().toString().equals(fechaFin.getText().toString())){
                        tipoDeReporte = "Reporte del empleado "+filtroEditTextEmpleados.getText().toString()+" por fecha "+fechaInicio.getText().toString();

                    }else {
                    tipoDeReporte = "Reporte del empleado "+filtroEditTextEmpleados.getText().toString()+" por fecha "+fechaInicio.getText().toString() +" a "+fechaFin.getText().toString();
                     }

                }
            } else if (filtroEditTextEmpleados.getText().toString().equalsIgnoreCase("") && tipoPermiso.getSelectedItem().toString().equalsIgnoreCase("")
            ) {
                if (date2.before(date1) ) {

                    Toast.makeText(getApplicationContext(),"fecha inicial no puede ser mayor a la fecha final", Toast.LENGTH_LONG).show();

                } else {

                    cursorList = datos.obtenerJoinsDePermisosFechaDepto(fechaInicio.getText().toString(), fechaFin.getText().toString(), String.valueOf(idDepto));
                    if (fechaInicio.getText().toString().equals(fechaFin.getText().toString())) {
                        tipoDeReporte = "Reporte del departamento de" + filtroEditTextDeptos.getText().toString() + " por fecha " + fechaInicio.getText().toString();

                    } else
                        tipoDeReporte = "Reporte del departamento de " + filtroEditTextDeptos.getText().toString() + " por fecha " + fechaInicio.getText().toString() + " a " + fechaFin.getText().toString();
                }

            } else if (filtroEditTextEmpleados.getText().toString().equalsIgnoreCase("") && filtroEditTextDeptos.getText().toString().equalsIgnoreCase("")
            ) {
                if (date2.before(date1)) {

                    Toast.makeText(getApplicationContext(),"fecha inicial no puede ser mayor a la fecha final", Toast.LENGTH_LONG).show();

                } else {
                    cursorList= datos.obtenerJoinsDePermisosFechaPermiso(fechaInicio.getText().toString(),fechaFin.getText().toString(),tipoPermiso.getSelectedItem().toString());
                    if (fechaInicio.getText().toString().equals(fechaFin.getText().toString())) {
                        tipoDeReporte = "Reporte por tipo de permiso " + tipoPermiso.getSelectedItem().toString() + " por fecha " + fechaInicio.getText().toString();

                    } else
                        tipoDeReporte = "Reporte por tipo de permiso " + tipoPermiso.getSelectedItem().toString() + " por fecha " + fechaInicio.getText().toString() + " a " + fechaFin.getText().toString();

                  }
            } else if (fechaInicio.getText().toString().equalsIgnoreCase("") && fechaFin.getText().toString().equalsIgnoreCase("")
            ) {
                cursorList = datos.obtenerJoinsDePermisosDeptoEmpleadoPermiso(String.valueOf(idDepto),idUser,tipoPermiso.getSelectedItem().toString());
                tipoDeReporte = "Reporte del empleado "+filtroEditTextEmpleados.getText().toString()+" por tipo de permiso "+tipoPermiso.getSelectedItem().toString();

            } else if (filtroEditTextDeptos.getText().toString().equalsIgnoreCase("")
            ) {
                if (date2.before(date1) ) {

                    Toast.makeText(getApplicationContext(),"fecha inicial no puede ser mayor a la fecha final", Toast.LENGTH_LONG).show();

                } else {
                    cursorList = datos.obtenerJoinsDePermisosFechaPermisoEmpleado(fechaInicio.getText().toString(),fechaFin.getText().toString(),tipoPermiso.getSelectedItem().toString(),idUser);
                    if (fechaInicio.getText().toString().equals(fechaFin.getText().toString())){
                        tipoDeReporte = "Reporte del empleado "+filtroEditTextEmpleados.getText().toString()+" por fecha "+fechaInicio.getText().toString()+" con tipo de permiso " ;

                    }else {
                        tipoDeReporte = "Reporte del empleado "+filtroEditTextEmpleados.getText().toString()+" por fecha "+fechaInicio.getText().toString() +" a "+fechaFin.getText().toString() ;
                    }
                  }
            } else if (tipoPermiso.getSelectedItem().toString().equalsIgnoreCase("")
            ) {
                if (date2.before(date1) ) {

                    Toast.makeText(getApplicationContext(),"fecha inicial no puede ser mayor a la fecha final", Toast.LENGTH_LONG).show();

                } else {
                    cursorList = datos.obtenerJoinsDePermisosFechaDeptoEmpleado(fechaInicio.getText().toString(),fechaFin.getText().toString(), String.valueOf(idDepto),idUser);
                    if (fechaInicio.getText().toString().equals(fechaFin.getText().toString())){
                        tipoDeReporte = "Reporte del empleado "+filtroEditTextEmpleados.getText().toString()+" por fecha "+fechaInicio.getText().toString();

                    }else {
                        tipoDeReporte = "Reporte del empleado "+filtroEditTextEmpleados.getText().toString()+" por fecha "+fechaInicio.getText().toString() +" a "+fechaFin.getText().toString();
                    }
                  }
            } else if (filtroEditTextEmpleados.getText().toString().equalsIgnoreCase("")
            ) {
                if (date2.before(date1) ) {

                    Toast.makeText(getApplicationContext(),"fecha inicial no puede ser mayor a la fecha final", Toast.LENGTH_LONG).show();

                } else {
                    cursorList = datos.obtenerJoinsDePermisosFechaPermisoDepto(fechaInicio.getText().toString(),fechaFin.getText().toString(),tipoPermiso.getSelectedItem().toString(), String.valueOf(idDepto));
                    if (fechaInicio.getText().toString().equals(fechaFin.getText().toString())){
                        tipoDeReporte = "Reporte del departamento de "+filtroEditTextDeptos.getText().toString()+" por fecha "+fechaInicio.getText().toString() ;

                    }else {
                        tipoDeReporte = "Reporte del departamento de "+filtroEditTextDeptos.getText().toString()+" por fecha "+fechaInicio.getText().toString() +" a "+fechaFin.getText().toString() ;
                    }
                  }
            } else if (fechaInicio.getText().toString().length() > 0 && filtroEditTextDeptos.getText().length() > 0
                    && fechaFin.getText().toString().length() > 0 && filtroEditTextEmpleados.getText().toString().length() > 0 && tipoPermiso.getSelectedItem().toString().length() > 0) {

                cursorList = datos.obtenerJoinsDePermisosFechaPermisoDeptoUser(fechaInicio.getText().toString(),fechaFin.getText().toString(),tipoPermiso.getSelectedItem().toString(), String.valueOf(idDepto),idUser);
                if (fechaInicio.getText().toString().equals(fechaFin.getText().toString())){
                    tipoDeReporte = "Reporte del empleado "+filtroEditTextEmpleados.getText().toString()+" por fecha "+fechaInicio.getText().toString() ;

                }else {
                    tipoDeReporte = "Reporte del empleado "+filtroEditTextEmpleados.getText().toString()+" por fecha "+fechaInicio.getText().toString() +" a "+fechaFin.getText().toString() ;
                }

            }


            try {

                if (cursorList.getCount() > 0) {
                    alertNombreExcel();
                } else {
                    Toast.makeText(this, "No hay permisos registrados con esta busqueda.", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception E){

            }




    }


    public void alertNombreExcel()

    {
        imagenD = new android.app.AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        view1 = factory.inflate(R.layout.modelonombreexcel, null);
        editreporteNombre = (EditText) view1.findViewById(R.id.editNombreExcel);

        imagenD.setView(view1);
        //Estableciendole el titulo y la imagen al alertDialog




        imagenD.setInverseBackgroundForced(true);




        imagenD.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(editreporteNombre.getText().toString().equalsIgnoreCase("")){

                    Toast.makeText(getApplicationContext() ,"Escriba el nombre del archivo",Toast.LENGTH_LONG).show();
                      alertNombreExcel();


                }else {
                    reporteNombre = editreporteNombre.getText().toString();


                    saveExcelFile(getApplicationContext(), reporteNombre + ".xls",tipoDeReporte);

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

    private boolean saveExcelFile(Context context, final String fileName ,final  String tipoDeReporte) {

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
        try {
            sheet1 = wb.createSheet(tipoDeReporte);
        }catch (Exception e){
           Log.i("TIPOREPORTE",e.toString());
        }


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
        sheet1.setColumnWidth(1, (15 * 500));
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
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Reportes.this);
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
                                        Toast.makeText(Reportes.this, "Reporte guardado en: "+file.toString(), Toast.LENGTH_LONG).show();
                                         alertVer(fileName);

                                    }

                                });


                android.app.AlertDialog alert = builder.create();
                alert.show();




            }
           else {
                os = new FileOutputStream(file);

                wb.write(os);
                Toast.makeText(context, "Reporte guardado en: "+file.toString(), Toast.LENGTH_LONG).show();

                  alertVer(fileName);




                /* String[] mailto = {};
                Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/",fileName));
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hoja de Permiso");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Hola PDF se adjunta en este correo. ");
                emailIntent.setType("application/vnd.ms-excel");
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(emailIntent, "Send email using:"));*/



            }

             DATA_URL ="";

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



    public void generarReporte(View view) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }else {

            invocarPermiso();
        }

    }


    public void resetReporte(View view) {
        filtroEditTextEmpleados.setText("");
        filtroEditTextDeptos.setText("");
       fechaInicio.setText("");
        fechaFin.setText("");
        tipoPermiso.setSelection(0);
        idDepto =0;
    }
    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales",Context.MODE_PRIVATE);

        String user = preferences.getString("user","");
        String pass = preferences.getString("pass","");
        Utilidades.TOKEN = preferences.getString("token","");




    }
    public  void alertVer(final String fileName){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Reportes.this);
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
                                    Uri uri = Uri.fromFile(new File(String.valueOf(file)));
                                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporte de permisos");
                                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hola Excel se adjunta en este correo. ");
                                    emailIntent.setType("application/vnd.ms-excel");
                                    emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                    try {
                                        startActivity(Intent.createChooser(emailIntent, "Send email using:"));

                                    } catch (ActivityNotFoundException e) {

                                    }
                                }else {
                                    Toast.makeText(Reportes.this, "El archivo no se encontro en su dispositivo", Toast.LENGTH_SHORT).show();

                                }



                            }
                        })
                .setPositiveButton("Vizualizar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (file.exists()) {
                                    String url = String.valueOf(file);

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
                                    Toast.makeText(Reportes.this, "El archivo no se encontro en su dispositivo", Toast.LENGTH_SHORT).show();
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
            filtroEditTextEmpleados.setText(pojoEmpleados.getName());
            idUser = (pojoEmpleados.getId());
            alertDialog.dismiss();

        }else {

            DepartamentosGetYSet departamentosGetYSet = (DepartamentosGetYSet) parent.getItemAtPosition(position);
            filtroEditTextDeptos.setText(departamentosGetYSet.getNombreDepto());
            if(departamentosGetYSet.getIdRemota() ==0){
                idDepto = Integer.parseInt((departamentosGetYSet.getId()));
            }else{
                idDepto = departamentosGetYSet.getIdRemota();
            }

            alertDialog.dismiss();

            filtroEditTextEmpleados.setText("");
        }

         }
     }




