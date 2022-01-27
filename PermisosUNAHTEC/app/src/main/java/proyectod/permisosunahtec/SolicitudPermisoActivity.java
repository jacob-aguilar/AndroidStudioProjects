package proyectod.permisosunahtec;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;

public class SolicitudPermisoActivity extends AppCompatActivity {

    public Spinner tipoPermiso;
    public EditText fecha_inicio,fecha_final;
    public EditText hora_inicio, hora_final;
    private TemplatePDF templatePDF;
    private  String[]header={"N","Descripción","Marca \n x","Hora\ninicial","Hora\nfinal","Obsevaciones"};
    private  String shortText = "Fecha ";
    private  String numeroEmpleado = "Número de empleado ";
    private  String nameEmpleado = "Nombre de empleado ";
    private  String departamento =   "Departamento ";
    private  String solicitud =   "Solicito permiso para ausentarme de mi centro de trabajo por el siguiente motivo ";
    private  String especificar = "Especificar el motivo e ";
    private  String detalle = "Detalle";
     private  String firmaSolicitante =   "___________________________   \t\t         ___________________________   \t\t         ___________________________";
    private  String firmas =             "             Firma Solicitante\t\t                               Vo.Bo. Jefe Inmediado\t\t                   Vo.Bo. Director(a) UNAHTEC";
    private String idItem;
    private String nameItem;
    private String nombreDepto;
    private EditText detalleEdit;
    private EditText observacionesedit;
    private SimpleDateFormat sdf;
    private Date date1;
    private Date date2;
    private Bitmap bitmap;
    int horasInicial;
    int horasFinal;
    String nota1 ="No será válido el presente permiso sin firmas y sello o si tiene borrones, manchas/tachaduras. Tener en";
     String nota2 =" cuenta al momento de solicitar un permiso en artículo 40 y el artículo 50 inciso 5, del reglamento interno de la               Universidad Nacional Autónoma de Honduras .\n" +
             "" +
             " En caso de emergencia por el cual usted no pueda asistir a su trabajo, favor comunicarse con su jefe inmediato o en su\n defecto, con la Jefatura de Recursos Humanos de UNAH-TEC Danlí, Abogada Sayra Roxana Figueroa\n (504) 2763-9900 Ext, 230004. ";

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solcitud_permiso);

        setTitle("Solicitud de permisos");
        String[] letra = {"TIPO DE PERMISO",
                "PERMISO PERSONAL",
                "ACTIVIDAD UNAH-TEC DANLI",
                "INCAPACIDAD", "VACACIONES",
                "PAA", "IHSS",
                "INTERRUPCION DEL FLUIDO ELECTRICO",
                "PROBLEMAS CON EL RELOJ MARCADOR", "COMPENSATORIO POR TIEMPO EXTRA",
                "PERMISO CON GOCE DE SUELDO",
                "ACTIVIDAD SINDICAL", "DIAS FERIADOS",};



        tipoPermiso = findViewById(R.id.spinerTipoPermiso);
        tipoPermiso.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));

        observacionesedit= findViewById(R.id.observaconesEdit);
        hora_inicio = findViewById(R.id.hora_inicialSolicitud);
        hora_final = findViewById(R.id.horaFinalSolicitud);
        detalleEdit = findViewById(R.id.detalleEdit);
        fecha_final= findViewById(R.id.fechaFinalSolic);
        fecha_inicio = findViewById(R.id.fechaInicialSolicitud);


        fecha_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFechaInicial();
            }
        });
        fecha_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFechaFinal();

            }
        });

        hora_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerHoraInicial();
            }
        });
        hora_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerHoraFinal();
            }
        });
    }

    private void obtenerHoraFinal() {
        Calendar calendar = Calendar.getInstance();

        int horas = calendar.get(Calendar.HOUR_OF_DAY);
        int minutos= calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hora_final.setText(""+hourOfDay+":"+minute);
                horasFinal =hourOfDay;
            }
        },horas,minutos,false);

        timePickerDialog.show();
    }



    private void obtenerHoraInicial() {
        Calendar calendar = Calendar.getInstance();


        int horas = calendar.get(Calendar.HOUR_OF_DAY);
        int minutos= calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hora_inicio.setText(""+hourOfDay+":"+minute);
                horasInicial =hourOfDay;
            }
        },horas,minutos,false);

        timePickerDialog.show();

    }

    private void obtenerFechaFinal() {
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int anio = calendar.get(Calendar.YEAR);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fecha_final.setText(""+dayOfMonth+"/"+month+"/"+year);
            }

        },anio,mes,dia);

        datePickerDialog.show();
    }

    private void obtenerFechaInicial() {
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int anio = calendar.get(Calendar.YEAR);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fecha_inicio.setText(""+dayOfMonth+"/"+month+"/"+year);
            }

        },anio,mes,dia);

        datePickerDialog.show();
    }

    public void generarPDF(View view) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Date horaInicio= null;
        Date horaFin =null;



        try {
            horaInicio = dateFormat.parse(hora_inicio.getText().toString());
            horaFin= dateFormat.parse(hora_final.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }



        try {
            sdf = new SimpleDateFormat("yyyy/MM/dd");

            date1 = sdf.parse(fecha_inicio.getText().toString());
            date2  = sdf.parse(fecha_final.getText().toString());
        } catch (Exception e) {

        }
        if(fecha_inicio.getText().toString().equalsIgnoreCase("")||fecha_final.getText().toString().equalsIgnoreCase("")||tipoPermiso.getSelectedItem().toString().equalsIgnoreCase("TIPO DE PERMISO")||
                hora_inicio.getText().toString().equalsIgnoreCase("")||hora_final.getText().toString().equalsIgnoreCase("")){


            Toast.makeText(this, "Ingrese Los datos", Toast.LENGTH_SHORT).show();
        }else {


            if (date2.before(date1) ) {

                Toast.makeText(getApplicationContext(),"fecha inicial no puede ser mayor a la fecha final", Toast.LENGTH_LONG).show();

            } else  if(horaFin.compareTo(horaInicio) <0){

                if(horasInicial >horasFinal){
                    Toast.makeText(this, "Hora inicial no puede ser mayor a la hora final"+horaFin.compareTo(horaInicio), Toast.LENGTH_SHORT).show();

                }else {
                    crearPDF();
                }

            }else if(horaFin.compareTo(horaInicio) ==0) {
                Toast.makeText(this, "Hora inicial no puede ser igual a la hora final"+horaFin.compareTo(horaInicio), Toast.LENGTH_SHORT).show();



            }
            else {


                crearPDF();

            }



        }


    }

    private void crearPDF(){


        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        } else {

            Bundle bundle = getIntent().getExtras();


            if (bundle != null) {
                idItem = bundle.getString("idEmpleado");
                nameItem = bundle.getString("nombreEmpleado");
                nombreDepto = bundle.getString("nombreDepartamento");
            }


            templatePDF = new TemplatePDF(getApplicationContext());
            templatePDF.openDocument();
            try {



                Drawable d = getResources().getDrawable(R.drawable.unatec);
                BitmapDrawable bitDw = ((BitmapDrawable) d);
                Bitmap bmp = bitDw.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());

                templatePDF.createPdf("JUSTIN",image);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                templatePDF.addTitles("UNIVERSIDAD NACIONAL AUTONOMA DE HONDURAS", "CENTRO UNIVERSITARIO TECNOLOGICO DE DANLI", "UNAH-TEC-DANLI", "CONTROL DE PERMISOS");
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");

                if(fecha_inicio.getText().toString().equalsIgnoreCase(fecha_final.getText().toString())){
                    templatePDF.createFechaID(shortText + ": " , fecha_inicio.getText().toString(),numeroEmpleado + ": " , idItem);
                }else {
                    templatePDF.createFechaID(shortText + ": " , fecha_inicio.getText().toString() + " a " + fecha_final.getText().toString(),numeroEmpleado + ": " , idItem);

                }
                 templatePDF.createEmpleado(nameEmpleado + ": " , nameItem);
                templatePDF.createDepto(departamento + ": " , nombreDepto);
                templatePDF.addEspecificar(solicitud);
                templatePDF.addEspacio("");


                templatePDF.createTable(header, getClients(), tipoPermiso.getSelectedItem().toString(), hora_inicio.getText().toString(), hora_final.getText().toString(), observacionesedit.getText().toString());
                 templatePDF.createEspecificar(especificar,"incluir documentos que respalden la actividad");
                templatePDF.addEspacio("");


                templatePDF.createDetalle(detalle + ": " , detalleEdit.getText().toString());

                templatePDF.addEspacio("");
                //templatePDF.addNota(" Nota:");
                //templatePDF.addnotaParrafo(nota);
                templatePDF.createNota("Nota: ",nota1);
                templatePDF.addnotaParrafo(nota2);

                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addEspacio("");
                templatePDF.addParagraph(firmaSolicitante);
                templatePDF.addParagraph(firmas);


                templatePDF.closeDocument();

                templatePDF.viewPDF();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }
    private ArrayList<String[]> getClients(){
        ArrayList<String[]>rows = new ArrayList<>();
        rows.add(new String[]{"1","ACTIVIDAD UNAH-TEC DANLI","","","",""});
        rows.add(new String[]{"2","VACACIONES","","","",""});
        rows.add(new String[]{"3","PERMISO PERSONAL","","","",""});
        rows.add(new String[]{"4","INCAPACIDAD","","","",""});
        rows.add(new String[]{"5","IHSS","","","",""});
        rows.add(new String[]{"6","PAA","","","",""});
        rows.add(new String[]{"7","PERMISO CON GOCE DE SUELDO","","","",""});
        rows.add(new String[]{"8","PROBLEMAS CON EL RELOJ MARCADOR","","","",""});
        rows.add(new String[]{"9","COMPENSATORIO POR TIEMPO EXTRA","","","",""});
        rows.add(new String[]{"10","INTERRUPCION DEL FLUIDO ELECTRICO","","","",""});
        rows.add(new String[]{"11","ACTIVIDAD SINDICAL","","","",""});
        rows.add(new String[]{"12","DIAS FERIADOS","","","",""});




        return rows;
    }


}
