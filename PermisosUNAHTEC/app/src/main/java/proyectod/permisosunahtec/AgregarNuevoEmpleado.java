package proyectod.permisosunahtec;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import proyectod.permisosunahtec.adapters.AdaptadorDeDepartamentos;
import proyectod.permisosunahtec.adapters.AdapterListDeptos;
import proyectod.permisosunahtec.pojo.EmpleadoGetYSet;
import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.syncs.SyncAdapterEmpleados;
import proyectod.permisosunahtec.utils.Utilidades;
import proyectod.permisosunahtec.web.DepartamentosGetYSet;

public class AgregarNuevoEmpleado extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private static final String RUTAIMAGEN = "PermisosUNAH/Imagenes";
    private static final int TOMARFOTO = 1;
    private static final int ELEGIRFOTO = 2;
    ImageView fotoNuevoEmp;
    EditText editNombreNuevoEmp;
    EditText editCodNuevoEmp;
    EditText editEmailNuevoEmp;
    EditText editCodDepto;
    EditText editContraseniaNuevoEmp;
    EditText editConfirmarConstrasenia;
    Button aggNuevoEmp;
    private String informacionId;
    private  String REGISTER_URL;

    String codEmpleado;

    public static int c=1;

    private Spinner spinerRol;
     private Bitmap bitmap;
    private int variable;
    EmpleadoGetYSet empleado;
    int orientation;
    private ListView  listaDeptos;
    private String pictureFilePath;
    private File pictureFile;
    private File imgFile;
      private AdapterListDeptos adapterListDeptos;
     String idAdmin;
    private String pictureBundle;
    private String rolUsuario;
    private String idItem;
    private String emailItem;
    private String pictureItem;
    private String rolItem;
    private String nameItem;
    private String codDepartamentoItem;

    private ArrayList<DepartamentosGetYSet> contes;
    DepartamentosGetYSet departamentosGetYSet;
    private Cursor justin;
    private Cursor cursorDatos;
    private String picture;
    private AlertDialog.Builder imagenD;
    private AlertDialog alertDialog;
    private int idDepto;

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarnuevoempleado);


        getSupportActionBar().setTitle("Nuevo Empleado");
        fotoNuevoEmp = findViewById(R.id.imagePerfil);
         spinerRol = (Spinner) findViewById(R.id.sppinerRol);
        editNombreNuevoEmp = findViewById(R.id.EditNombreNuevoEmpleado);
        editCodNuevoEmp = findViewById(R.id.EditCodigoAgregarNuevoEmpleado);
        editCodDepto = findViewById(R.id.EditCodDepto);
        editEmailNuevoEmp = findViewById(R.id.EditCorreoAgregarNuevoEmpleado);
        editContraseniaNuevoEmp = findViewById(R.id.EditContraseniaAgregarNuevoEmpleado);
        editConfirmarConstrasenia = findViewById(R.id.EditConfirmarContraseniaAgregarEmpleado);
        aggNuevoEmp = findViewById(R.id.btnAgregarnuevoEmpleado);

        String[] letra = {"TIPO DE ROL", "ADMINISTRADOR", "USUARIO"};
        spinerRol.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, letra));


        Intent intent = getIntent();
        Bundle bundle = this.getIntent().getExtras();

        informacionId =bundle.getString("informacionId");

        if (intent != null) {
            variable = intent.getIntExtra("variable", 0);
            if (variable == 2) {
                cargarPreferencias();

                idItem = bundle.getString("id");
                emailItem = bundle.getString("email");
                codDepartamentoItem = bundle.getString("codDepartamento");
                pictureItem = bundle.getString("picture");
                nameItem = bundle.getString("name");
                rolItem = bundle.getString("rol");
                informacionId = bundle.getString("codDepartamento");


                editNombreNuevoEmp.setText(nameItem);
                editCodNuevoEmp.setText(idItem);

                codEmpleado = idItem;
                editEmailNuevoEmp.setText(emailItem);
                editContraseniaNuevoEmp.setFocusable(false);
                editConfirmarConstrasenia.setFocusable(false);
                editConfirmarConstrasenia.setEnabled(false);
                editContraseniaNuevoEmp.setEnabled(false);
                editCodNuevoEmp.setFocusable(false);
                editCodNuevoEmp.setEnabled(false);
                editContraseniaNuevoEmp.setVisibility(View.INVISIBLE);
                editConfirmarConstrasenia.setVisibility(View.INVISIBLE);
                getSupportActionBar().setTitle("Modificar Información");

                if(idAdmin.equals(idItem)) {
                    spinerRol.setEnabled(false);
                    spinerRol.setFocusable(false);
                }
                    if (rolItem.equals("admin")) {
                        spinerRol.setSelection(1);

                    } else {
                        spinerRol.setSelection(2);
                    }
                if (pictureItem ==null) {


                    fotoNuevoEmp.setImageResource(R.drawable.empleado_icon);
                }else {

                    String[] selectionArgs = new String[]{String.valueOf(idItem)};
                    cursorDatos = getContentResolver().query(ContractParaEmpleados.CONTENT_URI,null,"_id =?",selectionArgs,null);

                    if(cursorDatos.moveToNext()){

                         picture = cursorDatos.getString(cursorDatos.getColumnIndex("picture"));
                    }
                    final byte[] bytes = Base64.decode(picture, Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    fotoNuevoEmp.setImageBitmap(bitmap);



                }


            }



            else if(variable == 3){

                 editCodDepto.setVisibility(View.VISIBLE);


            }else    if (bundle!= null) {
                variable = intent.getIntExtra("variable", 0);
                if(variable ==4) {
                    editNombreNuevoEmp.setText(bundle.getString("nombreEmpleado"));
                    editCodNuevoEmp.setText(bundle.getString("idEmpleado"));
                    String idEmpleado = bundle.getString("idEmpleado");
                    codEmpleado = bundle.getString("idEmpleado");
                    editEmailNuevoEmp.setText(bundle.getString("emailEmpleado"));
                    pictureBundle = (bundle.getString("picture"));
                    rolUsuario = (bundle.getString("rol"));


                    editContraseniaNuevoEmp.setFocusable(false);
                    editConfirmarConstrasenia.setFocusable(false);
                    editConfirmarConstrasenia.setEnabled(false);
                    editContraseniaNuevoEmp.setEnabled(false);
                    editCodNuevoEmp.setFocusable(false);
                    editCodNuevoEmp.setEnabled(false);
                    editContraseniaNuevoEmp.setVisibility(View.INVISIBLE);
                    editConfirmarConstrasenia.setVisibility(View.INVISIBLE);
                    getSupportActionBar().setTitle("Modificar Información");

                    try {
                        cargarPreferencias();
                        if (idAdmin.equals(idEmpleado)) {
                            spinerRol.setEnabled(false);
                            spinerRol.setFocusable(false);
                        }
                    } catch (Exception e) {

                    }

                    if (rolUsuario.equals("admin")) {
                        spinerRol.setSelection(1);

                    } else {
                        spinerRol.setSelection(2);

                    }


                    if (pictureBundle.equals("")) {


                        fotoNuevoEmp.setImageResource(R.drawable.empleado_icon);
                    } else {

                        String datosImagen = pictureBundle;
                        final byte[] bytes = Base64.decode(datosImagen, Base64.DEFAULT);
                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        fotoNuevoEmp.setImageBitmap(bitmap);

                    }

                }else {
                    Bundle bundle2 = this.getIntent().getExtras();
                    informacionId = bundle2.getString("informacionId");
                }
            }


        }


        editCodDepto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerDeptos();


            }
        });


        aggNuevoEmp.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {



                        if (variable == 1 ||variable ==3) {


                                         if (editNombreNuevoEmp.getText().toString().equalsIgnoreCase("") || editCodNuevoEmp.getText().toString().equalsIgnoreCase("")
                                                 || editEmailNuevoEmp.getText().toString().equalsIgnoreCase("") || editContraseniaNuevoEmp.getText().toString().equalsIgnoreCase("")
                                                 || editConfirmarConstrasenia.getText().toString().equalsIgnoreCase("")) {
                                             Toast.makeText(getApplicationContext(), "Ingrese los datos", Toast.LENGTH_LONG).show();


                                         } else {

                                             if (editContraseniaNuevoEmp.getText().toString().length() < 8 || editConfirmarConstrasenia.getText().toString().length() < 8) {
                                                 Toast.makeText(getApplicationContext(), "La contraseñia no debe ser menor a ocho caracteres", Toast.LENGTH_LONG).show();
                                             } else {
                                                 if (editContraseniaNuevoEmp.getText().toString().equals(editConfirmarConstrasenia.getText().toString())) {
                                                     if (!validarEmail(editEmailNuevoEmp.getText().toString())) {
                                                         Toast.makeText(getApplicationContext(), "Correo no valido", Toast.LENGTH_LONG).show();
                                                     } else {

                                                         if (spinerRol.getSelectedItem().toString().equals("TIPO DE ROL")) {
                                                             Toast.makeText(getApplicationContext(), "Ingrese el tipo de rol", Toast.LENGTH_LONG).show();
                                                         } else {

                                                             if(editCodDepto.getVisibility()==View.VISIBLE ){
                                                                 if(editCodDepto.getText().toString().equalsIgnoreCase("")){
                                                                     Toast.makeText(getApplicationContext(), "Ingrese el departamento.", Toast.LENGTH_LONG).show();
                                                                 }else {
                                                                   ingresarEmpleado();
                                                                 }

                                                             }else {
                                                                 ingresarEmpleado();
                                                             }
                                                         }

                                                     }

                                                 } else {

                                                     Toast.makeText(getApplicationContext(), "nueva password con confirmar password no coinciden", Toast.LENGTH_LONG).show();
                                                 }

                                             }
                                         }

                            }



                            else if (editNombreNuevoEmp.getText().toString().equalsIgnoreCase("") || editCodNuevoEmp.getText().toString().equalsIgnoreCase("")
                                    || editEmailNuevoEmp.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(getApplicationContext(), "Ingrese los datos", Toast.LENGTH_LONG).show();

                            } else {
                                if (!validarEmail(editEmailNuevoEmp.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "Correo no valido", Toast.LENGTH_LONG).show();
                                } else {
                                    if (spinerRol.getSelectedItem().toString().equals("TIPO DE ROL")) {
                                        Toast.makeText(getApplicationContext(), "Ingrese el tipo de rol", Toast.LENGTH_LONG).show();
                                    }else {
                                        cargarPreferencias();
                                       modificarEmpleado();


                                    }


                                }


                            }



                }
            }

        );
    }



    private void obtenerDeptos() {

        SearchView editFiltro = null;
        try {

            imagenD = new AlertDialog.Builder(this);


            LayoutInflater factory = LayoutInflater.from(this);
            View dialogoLayout = factory.inflate(R.layout.lista_deptos_dialog, null, false);

            listaDeptos = (ListView) dialogoLayout.findViewById(R.id.listaDeptos);
            imagenD.setMessage("\t\t\tDepartamentos");
            contes = new ArrayList<DepartamentosGetYSet>();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                editFiltro = (SearchView) dialogoLayout.findViewById(R.id.filtroDeptos);
            }

            imagenD.setView(dialogoLayout);


            String[] selectionArgs = new String[]{"0",};

            cursorDatos = getApplicationContext().getContentResolver().query(ContractParaDepartamentos.CONTENT_URI, null, "eliminado =?", selectionArgs, null);


            while (cursorDatos.moveToNext()) {
                departamentosGetYSet = new DepartamentosGetYSet();


                departamentosGetYSet.setNombreDepto(cursorDatos.getString(cursorDatos.getColumnIndex("nombreDepto")));
                departamentosGetYSet.setId(cursorDatos.getString(cursorDatos.getColumnIndex("_id")));
                departamentosGetYSet.setIdRemota(cursorDatos.getInt(cursorDatos.getColumnIndex("idRemota")));
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




    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
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


 private  void  ingresarEmpleado(){


         ContentValues values = new ContentValues();
         values.put(ContractParaEmpleados.Columnas._ID, editCodNuevoEmp.getText().toString().toLowerCase());
         values.put(ContractParaEmpleados.Columnas.NAME, editNombreNuevoEmp.getText().toString());
         values.put(ContractParaEmpleados.Columnas.EMAIL, editEmailNuevoEmp.getText().toString().toLowerCase());
     values.put(ContractParaEmpleados.Columnas.CODDEPTO, informacionId);

         values.put(ContractParaEmpleados.Columnas.PASSWORD, editContraseniaNuevoEmp.getText().toString());
         values.put(ContractParaEmpleados.Columnas.PASSWORD_CONFIRMATION, editConfirmarConstrasenia.getText().toString());
         if(bitmap !=null) {
             values.put(ContractParaEmpleados.Columnas.PICTURE,convertirImagenEnString(bitmap));
         }

     if (spinerRol.getSelectedItem().equals("USUARIO")) {
         String rol = "user";

         values.put(ContractParaEmpleados.Columnas.ROL, rol);

     }
     if (spinerRol.getSelectedItem().equals("ADMINISTRADOR")) {
         String rol = "admin";
         values.put(ContractParaEmpleados.Columnas.ROL, rol);
     }

         values.put(ContractParaEmpleados.Columnas.PENDIENTE_INSERCION, 1);
     try {

             getApplicationContext().getContentResolver().insert(ContractParaEmpleados.CONTENT_URI, values);
          SyncAdapterEmpleados.sincronizarAhora(this, true);


         if (Utilidades.materialDesign())
             finishAfterTransition();

         else finish();
     }catch (Exception e){
         Log.i("SQLITEJUS",e.toString());

         Toast.makeText(this, "Hubo un error al ingresar el empleado.", Toast.LENGTH_SHORT).show();
     }







 }
    private  void  modificarEmpleado(){


        ContentValues values = new ContentValues();

        values.put(ContractParaEmpleados.Columnas.NAME, editNombreNuevoEmp.getText().toString());
        values.put(ContractParaEmpleados.Columnas.EMAIL, editEmailNuevoEmp.getText().toString());
        values.put(ContractParaEmpleados.Columnas.CODDEPTO, informacionId);



        if(bitmap !=null) {
            values.put(ContractParaEmpleados.Columnas.PICTURE,convertirImagenEnString(bitmap));
        }

        if (spinerRol.getSelectedItem().equals("USUARIO")) {
            String rol = "user";

            values.put(ContractParaEmpleados.Columnas.ROL, rol);

        }
        if (spinerRol.getSelectedItem().equals("ADMINISTRADOR")) {
            String rol = "admin";
            values.put(ContractParaEmpleados.Columnas.ROL, rol);
        }

        values.put(ContractParaEmpleados.Columnas.PENDIENTE_INSERCION, 1);
        try {
            String[] selectionArgs = new String[]{editCodNuevoEmp.getText().toString()};

            getApplicationContext().getContentResolver().update(ContractParaEmpleados.CONTENT_URI, values,"_id =?",selectionArgs);
             SyncAdapterEmpleados.sincronizarAhora(this, true);


            if (Utilidades.materialDesign())
                finishAfterTransition();

            else finish();
        }catch (Exception e){
            Toast.makeText(this, "Codigo o correo ya existen", Toast.LENGTH_SHORT).show();
        }







    }



    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();


        if(ancho>anchoNuevo&&alto>altoNuevo){

            float escalaAncho = anchoNuevo/ancho;
        float escalaAlto = altoNuevo/alto;
            Matrix matrix = new Matrix();

            matrix.postScale(escalaAncho,escalaAlto);
            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);
        }else{
            return bitmap;
        }
    }

    private String convertirImagenEnString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        String imagenString = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return imagenString;

    }

    public void agregarFoto(View view) {


            final String opciones[] = {"Tomar foto", "Elegir de la Galeria", "Cancelar"};

            AlertDialog.Builder alerdialog = new AlertDialog.Builder(this);
            alerdialog.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                        if (opciones[i] == "Tomar foto") {
                            abrirCamara();
                        }
                        if (opciones[i] == "Elegir de la Galeria") {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Selecciones App"), 300);
                        }


                }
            });


            alerdialog.create().show();

    }


    private void abrirCamara() {
        //Capturar la imagen del empleado desde la camara

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);

        }else {




            Intent inten = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


            try {

                //Se crea un achivo de la foto en blanco
                pictureFile = getPictureFile();

            }catch (Exception e){

            }


            //se agrega la imagen capturada al archivo en blanco
            Uri photoUri = FileProvider.getUriForFile(this,"com.permisosunahtec.android.fileprovider",pictureFile);
            inten.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

            startActivityForResult(inten, TOMARFOTO);







        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case TOMARFOTO:
                    imgFile = new File(pictureFilePath);
                    if (resultCode == -1) {

                        bitmap = BitmapFactory.decodeFile(String.valueOf(imgFile));


                        try {




                            ExifInterface exif = new ExifInterface(imgFile.getAbsolutePath());

                            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                            switch (orientation) {

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    bitmap = rotateImage(getApplicationContext(),bitmap, 270);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    bitmap = rotateImage(getApplicationContext() ,bitmap, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    bitmap = rotateImage(getApplicationContext(),bitmap, 90);
                                    break;

                            }








                        fotoNuevoEmp.setImageBitmap(bitmap);

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Intentelo Nuevamente", Toast.LENGTH_LONG).show();

                        }

                         
                    }else{
                        //en caso de que no haya foto capturada el archivo en blanco se elimina
                        imgFile.delete();
                    }
                    break;
                
                case 300:
                    Uri path = data.getData();
                    try {

                        InputStream picture = getContentResolver().openInputStream(path);
                        bitmap = BitmapFactory.decodeStream(picture);
                         orientation = getOrientation(getApplicationContext(), path);

                        switch (orientation) {

                            case 90:
                                bitmap = rotateImage(getApplicationContext(),bitmap, 90);

                                break;

                            case 180:
                                bitmap = rotateImage(getApplicationContext(),bitmap, 180);
                                break;

                            case 270:
                                bitmap = rotateImage(getApplicationContext(),bitmap, 270);
                                break;

                            case ExifInterface.ORIENTATION_NORMAL:

                            default:
                                break;
                        }



                        fotoNuevoEmp.setImageBitmap(bitmap);

                         

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Intentelo Nuevamente", Toast.LENGTH_LONG).show();
                    }

                    break;

            }

        }catch(Exception ex){

        }

    }

    private File getPictureFile()throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile ="Docente"+timeStamp;
        File storeImagen = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(pictureFile,".jpg",storeImagen);
        pictureFilePath = imagen.getAbsolutePath();



        return  imagen;
    }
    public static int getOrientation(Context context, Uri photoUri) {


        Cursor cursor = context.getContentResolver().query(photoUri, new String[]{

                MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        try {
            if (cursor.moveToFirst())
            { return cursor.getInt(0); }
            else
            { return -1; } } finally { cursor.close(); }
    }




    public static Bitmap rotateImage(Context context ,Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        try {

            matrix.postRotate(angle);


        source = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
        }catch (Exception e){

            Toast.makeText(context, "Intentelo Nuevamente", Toast.LENGTH_LONG).show();
        }
        return  source;
    }
    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales",Context.MODE_PRIVATE);

         Utilidades.TOKEN =preferences.getString("token","");
        idAdmin = preferences.getString("id","");



    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        DepartamentosGetYSet departamentosGetYSet = (DepartamentosGetYSet) parent.getItemAtPosition(position);
        editCodDepto.setText(departamentosGetYSet.getNombreDepto());
        if(departamentosGetYSet.getIdRemota() ==0){
          informacionId = departamentosGetYSet.getId();
        }else{
            informacionId = String.valueOf(departamentosGetYSet.getIdRemota());
        }

        alertDialog.dismiss();

    }
}




