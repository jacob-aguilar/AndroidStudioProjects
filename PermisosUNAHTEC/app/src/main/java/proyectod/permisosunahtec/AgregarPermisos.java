package proyectod.permisosunahtec;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import proyectod.permisosunahtec.adapters.AdaptadorDeAllPermisos;
 import proyectod.permisosunahtec.providers.ContractParaPermisos;
import proyectod.permisosunahtec.syncs.SyncAdapterPermisos;
import proyectod.permisosunahtec.utils.Utilidades;


public class AgregarPermisos extends AppCompatActivity {
    private String idEmpleado;
    private int i;
    private String tipoPermisovar;
    private String fechaIniciovar;
    private String fechaFinvar;
    private  String observacionesVar;
    private String codDepartamento;
    private String numeroPermisovar;
    private int contador = 1;
    private String REGISTER_URL;
    private Spinner tipoPermiso;
    private EditText fechaInicio;
    private EditText fechaFin;
    private  EditText observaciones;
    Calendar C;
    AdaptadorDeAllPermisos adapter;
    private int intentListaEMpleados;
     private int mYearIni, mMonthIni, mDayIni, sYearIni, sMonthIni, sDayIni, mYearFin, mMonthFin, mDayFin, sYearFin, sMonthFin, sDayFin;
    static final int DATE_ID = 0;
    static final int DATE_ID1 = 1;
    private String nombreEmpleado;
    private JSONArray lista;
    private JSONObject json_data;
    private String tokenFirebaseTabla;
    private String idAdmin;
    private String estadoNotificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarpermiso);
        setTitle("Agrega un permiso");
        tipoPermiso = (Spinner) findViewById(R.id.spinnerpermisos);
        fechaInicio = (EditText) findViewById(R.id.editTextFechaInicio);
        fechaFin = (EditText) findViewById(R.id.editTextFechaFin);
        observaciones =(EditText) findViewById(R.id.editTextobservaciones);

        C = Calendar.getInstance();
        sMonthIni = C.get(Calendar.MONTH);
        sDayIni = C.get(Calendar.DAY_OF_MONTH);
        sYearIni = C.get(Calendar.YEAR);
cargarPreferencias();

        String[] letra = {"TIPO DE PERMISO",
                "PERMISO PERSONAL",
                "ACTIVIDAD UNAH-TEC DANLI",
                "INCAPACIDAD", "VACACIONES",
                "PAA", "IHSS",
                "INTERRUPCION DEL FLUIDO ELECTRICO",
                "PROBLEMAS CON EL RELOJ MARCADOR", "COMPENSATORIO POR TIEMPO EXTRA",
                "PERMISOS CON GOCE DE SUELDO",
                "ACTIVIDAD SINDICAL", "DIAS FERIADOS",};
        tipoPermiso.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));


        Bundle bundle = this.getIntent().getExtras();
         intentListaEMpleados = bundle.getInt("variable");


        if (intentListaEMpleados == 2) {

            idEmpleado = bundle.getString("id");
            codDepartamento = bundle.getString("codDepartamento");
        } else {

            idEmpleado = bundle.getString("idEmpleado");
            codDepartamento = bundle.getString("informacionId");
            nombreEmpleado = bundle.getString("nombreEmpleado");
        }



        contador = bundle.getInt("contador");


        if (contador == 2) {

            Bundle bundle2 = this.getIntent().getExtras();

            if (bundle2 != null) {
                nombreEmpleado = bundle.getString("nombreEmpleado");
                numeroPermisovar = bundle.getString("numeroPermiso");
                tipoPermisovar = bundle.getString("tipoPermiso");
                fechaIniciovar = bundle.getString("fechaInicio");
                fechaFinvar = bundle.getString("fechaFin");
                idEmpleado= bundle.getString("idEmpleado");
                codDepartamento = bundle.getString("idDepartamento");
                observacionesVar = bundle.getString("observaciones");
                i = bundle.getInt("i");
                if (tipoPermisovar.equals("PERMISO PERSONAL")) {
                    tipoPermiso.setSelection(1);
                }
                if (tipoPermisovar.equals("ACTIVIDAD UNAH-TEC DANLI")) {
                    tipoPermiso.setSelection(2);
                }
                if (tipoPermisovar.equals("INCAPACIDAD")) {
                    tipoPermiso.setSelection(3);
                }
                if (tipoPermisovar.equals("VACACIONES")) {
                    tipoPermiso.setSelection(4);
                }
                if (tipoPermisovar.equals("PAA")) {
                    tipoPermiso.setSelection(5);
                }
                if (tipoPermisovar.equals("IHSS")) {
                    tipoPermiso.setSelection(6);
                }
                if (tipoPermisovar.equals("INTERRUPCION DEL FLUIDO ELECTRICO")) {
                    tipoPermiso.setSelection(7);
                }
                if (tipoPermisovar.equals("PROBLEMAS CON EL RELOJ MARCADOR")) {
                    tipoPermiso.setSelection(8);
                }
                if (tipoPermisovar.equals("COMPENSATORIO POR TIEMPO EXTRA")) {
                    tipoPermiso.setSelection(9);
                }
                if (tipoPermisovar.equals("PERMISOS CON GOCE DE SUELDO")) {
                    tipoPermiso.setSelection(10);
                }
                if (tipoPermisovar.equals("ACTIVIDAD SINDICAL")) {
                    tipoPermiso.setSelection(11);
                }
                if (tipoPermisovar.equals("DIAS FERIADOS")) {
                    tipoPermiso.setSelection(12);
                }

                fechaInicio.setText(fechaIniciovar);
                fechaFin.setText(fechaFinvar);
                observaciones.setText(observacionesVar);
            }
        }


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

    private void ingresarPermiso() {


        ContentValues values = new ContentValues();

        values.put(ContractParaPermisos.Columnas.TIPO_PERMISO, tipoPermiso.getSelectedItem().toString());
        values.put(ContractParaPermisos.Columnas.FECHA_INICIO, fechaInicio.getText().toString());
        values.put(ContractParaPermisos.Columnas.FECHA_FIN, fechaFin.getText().toString());

        values.put(ContractParaPermisos.Columnas.OBSERVACIONES, observaciones.getText().toString());
        values.put(ContractParaPermisos.Columnas.ID_EMPLEADO, idEmpleado);



            values.put(ContractParaPermisos.Columnas.ID_DEPTO,codDepartamento);
        values.put(ContractParaPermisos.Columnas.PENDIENTE_INSERCION, 1);
        try {

            if(contador ==2){
                String[] selectionArgs = new String[]{numeroPermisovar};
                getApplicationContext().getContentResolver().update(ContractParaPermisos.CONTENT_URI, values,"id =?",selectionArgs);

            }else {
                getApplicationContext().getContentResolver().insert(ContractParaPermisos.CONTENT_URI, values);


            }

            SyncAdapterPermisos.sincronizarAhora(this, true);


            if (Utilidades.materialDesign())
                finishAfterTransition();

            else finish();
        }catch (Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }









}




    public void agregarPermiso(View view) {



            if (tipoPermiso.getSelectedItem().toString().equals("TIPO DE PERMISO")) {
                Toast.makeText(getApplicationContext(), "Agrege el tipo de permiso"  , Toast.LENGTH_LONG).show();


            } else {
                if (fechaInicio.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Agregue la fecha de inicio"  , Toast.LENGTH_LONG).show();

                } else {
                    if (fechaFin.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Agregue la fecha de fin" , Toast.LENGTH_LONG).show();
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date date1 = sdf.parse ( fechaInicio.getText().toString());
                            Date date2 = sdf.parse(fechaFin.getText().toString());
                            if(date2.before(date1)  ){

                                Toast.makeText(getApplicationContext(),"fecha inicial no puede ser mayor a la fecha final", Toast.LENGTH_LONG).show();
                            }else{
                                ingresarPermiso();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }

            }



    }

    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);


        idAdmin = preferences.getString("id","");

    }
}
