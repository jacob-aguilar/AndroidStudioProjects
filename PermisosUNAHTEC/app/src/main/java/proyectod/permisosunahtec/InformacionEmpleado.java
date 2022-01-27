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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import proyectod.permisosunahtec.adapters.AdaptadorDePermisos;
import proyectod.permisosunahtec.adapters.AdapterListPermisos;
 import proyectod.permisosunahtec.pojo.PermisosGetYSet;
import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.providers.ContractParaPermisos;
import proyectod.permisosunahtec.providers.OperacionesBaseDatos;
import proyectod.permisosunahtec.syncs.SyncAdapterDeptos;
import proyectod.permisosunahtec.syncs.SyncAdapterEmpleados;
import proyectod.permisosunahtec.syncs.SyncAdapterPermisos;
import proyectod.permisosunahtec.utils.Utilidades;

public class InformacionEmpleado extends AppCompatActivity implements AdaptadorDePermisos.OnItemClickListener, AdaptadorDePermisos.OnLongClickListener , LoaderManager.LoaderCallbacks<Cursor>, AppBarLayout.OnOffsetChangedListener {



    private  TemplatePDF templatePDF;
    private ListView lv1;
    private String DATA_URL;
    private String conte;
    private  String numeroPermiso;
    private String tipoPermiso;
    private String fechaInicio;
    private String fechaFin;
    private String observaciones;
    private String path;
    ImageView fotoNuevoEmp;
    String imagenString;
    private  JSONObject json_data;
    private  ArrayList<PermisosGetYSet> contes;
    private JSONArray lista;
    private static final String RUTAIMAGEN = "PermisosUNAH/Imagenes";
    private static final int TOMARFOTO = 1;
    private static final int VERFOTO = 600;
    File nuevaImagen;
    private ImageView mostrarFoto;
    private TextView mostrarCod;
    private TextView mostrarNombre;
    private TextView mostrarCorreo;
    private String nombreEmpleado;
    Bitmap bitmap;
    String pictureFilePath;
    private  int contador;
    private String idEmpleado="2343";
    ImageView modificarFoto;
    EditText modificarNombre;
    EditText modificarCodigo;
    EditText modificarEmail;
    private int variableLogin;
    String imagenDatos;
    private String informacionId;
    int orientation;
     int variable=4;
     private int position2;
    AdapterListPermisos adapterListPermiso;
    File pictureFile =null;
    private static  final String TAG ="FIREBASE_TOKEN";
    File imgFile;
    private FirebaseAuth mAuth;
    private String idAdmin;
    private String tokenFirebase;
    String refrescheToken;
    private int eventoCerrar;
    private String pictureEmpleado;
    private String tokenUsuario;
    private String emailEmpleado;
    private String nameUsuario;
    private String codDepartamento;
    private String token;
    private String rol;
    private String rolEmpleado;
    private String idItem;
    private String emailItem;
    private String codDepartamentoItem;
    private String nameItem;
    private String pictureItem;
    private String rolItem;




    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AdaptadorDePermisos adapter;
    private Cursor cursorList;
    private OperacionesBaseDatos datos;
    private Cursor cursorDepto;
    private String nombreDepto;
    private Cursor cursorDatos;
    private String picture;

    //--------------------------------------------------------------------------------
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing;
    private FrameLayout framelayoutTitle;
    private LinearLayout linearlayoutTitle;
    private Toolbar toolbar;

    private TextView textviewTitle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Cursor cursorDeparatamentos;

    //___________________________________________________________________________________
    @Override
    protected void onResume() {
        super.onResume();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacionempleado);
        recyclerView = (RecyclerView) findViewById(R.id.listaPermisos);
        refrescheToken = FirebaseInstanceId.getInstance().getToken();

        toolbar = (Toolbar)findViewById( R.id.toolbar);
        appbar = (AppBarLayout)findViewById( R.id.appbar );
        collapsing = (CollapsingToolbarLayout)findViewById( R.id.collapsing);
        linearlayoutTitle = (LinearLayout)findViewById( R.id.linearlayout_title );
        toolbar = (Toolbar)findViewById( R.id.toolbar );
        framelayoutTitle = (FrameLayout)findViewById( R.id.framelayout_title );

           //-------------------------------------------------------

        toolbar.setTitle("");
        textviewTitle= findViewById(R.id.textview_title);


        setSupportActionBar(toolbar);
        startAlphaAnimation(textviewTitle, 0, View.INVISIBLE);

        appbar.addOnOffsetChangedListener(this);

        //-------------------------------------------------------



        cargarPreferencias();
        if(rol.equals("user")){
            SyncAdapterEmpleados.inicializarSyncAdapter(this);

            SyncAdapterEmpleados.sincronizarAhora(this, true);
            SyncAdapterPermisos.inicializarSyncAdapter(this);

            SyncAdapterPermisos.sincronizarAhora(this, true);
            SyncAdapterDeptos.inicializarSyncAdapter(this);

            SyncAdapterDeptos.sincronizarAhora(this, true);
        }




        RelativeLayout relativeLayout = findViewById(R.id.relativeMenu);
        RelativeLayout relativeLayout2 = findViewById(R.id.relativeMenuUsuario);
        mostrarCod = (TextView) findViewById(R.id.texttipoPermiso);
        mostrarNombre = (TextView) findViewById(R.id.textFechaInicio);
        mostrarCorreo = (TextView) findViewById(R.id.textCorreo);
        mostrarFoto = (ImageView) findViewById(R.id.fotoInformacion);

        modificarFoto = (ImageView) findViewById(R.id.imagePerfil);
        modificarNombre = (EditText) findViewById(R.id.EditNombreNuevoEmpleado);
        modificarCodigo = (EditText) findViewById(R.id.EditCodigoAgregarNuevoEmpleado);
        modificarEmail = (EditText) findViewById(R.id.EditCorreoAgregarNuevoEmpleado);


        Bundle bundle =this.getIntent().getExtras();



        if(bundle != null) {
            informacionId = bundle.getString("informacionIdstatic");


            mAuth = FirebaseAuth.getInstance();

            variable = bundle.getInt("variable");

        }


        if(variable ==3){
            idItem = bundle.getString("id");
            emailItem = bundle.getString("email");
            codDepartamentoItem = bundle.getString("codDepartamento");
            pictureItem = bundle.getString("picture");
            nameItem = bundle.getString("name");
            rolItem = bundle.getString("rol");

            //Este textview muestra el nombre en la appBar cuando le da scroll
            textviewTitle.setText("Permisos de: "+nameItem);
            //---------------------------------------------------
            mostrarCod.setText(idItem);
            idEmpleado = idItem;
            informacionId = codDepartamentoItem;

            mostrarNombre.setText(nameItem);
            nombreEmpleado= nameItem;
            mostrarCorreo.setText(emailItem);
            imagenDatos = pictureItem;
            relativeLayout.setVisibility(View.INVISIBLE);



            variable=1;
            if(imagenDatos == null ||imagenDatos.equals("null")){
                mostrarFoto.setImageResource(R.drawable.empleado_icon);
             }else {

                final byte[] bytes = Base64.decode(imagenDatos, Base64.DEFAULT);

                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                mostrarFoto.setImageBitmap(bitmap);
            }

        }else
        if(variable==2) {
            idItem = bundle.getString("id");
            emailItem = bundle.getString("email");
            codDepartamentoItem = bundle.getString("codDepartamento");
             pictureItem = bundle.getString("picture");
            nameItem = bundle.getString("name");
            rolItem = bundle.getString("rol");

            //Este textview muestra el nombre en la appBar cuando le da scroll
            textviewTitle.setText("Permisos de: "+nameItem);
            //---------------------------------------------------
            mostrarCod.setText(idItem);
            idEmpleado = idItem;
            informacionId = codDepartamentoItem;

            mostrarNombre.setText(nameItem);
            nombreEmpleado= nameItem;
            mostrarCorreo.setText(emailItem);
            imagenDatos = pictureItem;
            variable=1;
            relativeLayout2.setVisibility(View.INVISIBLE);

            if(imagenDatos == null){
                mostrarFoto.setImageResource(R.drawable.empleado_icon);
            }else {

                final byte[] bytes = Base64.decode(imagenDatos, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                mostrarFoto.setImageBitmap(bitmap);
            }

              }else  {
            String[] selectionArgs = new String[]{"0",idAdmin};

            cursorList=  getApplicationContext().getContentResolver().query(ContractParaEmpleados.CONTENT_URI, null,"eliminado =?and _id =?",selectionArgs,null);
             if(cursorList.moveToNext()){
                 idItem =cursorList.getString(cursorList.getColumnIndex("_id"));
                 mostrarCod.setText(idAdmin);
                 idEmpleado =idAdmin;
                 codDepartamentoItem =cursorList.getString(cursorList.getColumnIndex("codDepartamento"));
                 mostrarNombre.setText(cursorList.getString(cursorList.getColumnIndex("name")));
                 mostrarCorreo.setText(cursorList.getString(cursorList.getColumnIndex("email")));
                 emailEmpleado=cursorList.getString(cursorList.getColumnIndex("email"));
                 nameUsuario =cursorList.getString(cursorList.getColumnIndex("name"));
                 imagenDatos =cursorList.getString(cursorList.getColumnIndex("picture"));
                 rol =cursorList.getString(cursorList.getColumnIndex("rol"));
                 if(rol.equals("user")) {
                     relativeLayout.setVisibility(View.INVISIBLE);
                     relativeLayout2.setVisibility(View.VISIBLE);
                 }
                 try {
                     if(imagenDatos.equals("null") || imagenDatos==null){
                         mostrarFoto.setImageResource(R.drawable.empleado_icon);
                     }else {

                         final byte[] bytes = Base64.decode(imagenDatos, Base64.DEFAULT);
                         bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                         mostrarFoto.setImageBitmap(bitmap);
                     }

                 }catch (Exception e){

                 }
                 }




        }


        if(idAdmin.equals(idEmpleado)) {
            updateToken();
        }







        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdaptadorDePermisos(this,this,  this);
        recyclerView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(0, null, this);





    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }
    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }
    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    private void enviarTokenRegistro(String refrescheToken) {
        Log.d(TAG,"RefrechedToken:"+refrescheToken);
    }


    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
       tokenUsuario = preferences.getString("token","");
        rol = preferences.getString("rol","");
        idAdmin = preferences.getString("id","");

    }

    public void modificarEmpleado(View view) {

        Intent intenModificar = new Intent(InformacionEmpleado.this,AgregarNuevoEmpleado.class);
        Bundle bundle = new Bundle();


        if(variable ==4){


            bundle.putString("idEmpleado",idAdmin);
            bundle.putString("nombreEmpleado",nameUsuario);
            bundle.putString("emailEmpleado",emailEmpleado);
            bundle.putInt("variable", 4);
            bundle.putString("picture", convertirImagenEnString(bitmap));
            bundle.putString("rol", rol);
            intenModificar.putExtras(bundle);





        }else {


            bundle.putString("id", idItem);
            bundle.putString("email", emailItem);
            bundle.putString("name", nameItem);
            if(bitmap !=null) {
                bundle.putString("picture", convertirImagenEnString(bitmap));
            }
            bundle.putString("rol", rolItem);
            bundle.putString("codDepartamento",codDepartamentoItem);
            bundle.putInt("variable", 2);

        }

        intenModificar.putExtras(bundle);

        startActivity(intenModificar);
        finish();

    }

    private void updateToken(  ) {

        String REGISTER_URL = Utilidades.URL+"api/auth/updateToken/"+idEmpleado;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG,""+idAdmin);
                        if(eventoCerrar ==2) {
                            cerrarSesion();
                        }

                    }},new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {




             }}){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                if(eventoCerrar ==2){
                    params.put("tokenFirebase", "null");
                }else {

                    params.put("tokenFirebase", refrescheToken);
                }



                return params;}

        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Authorization", "Bearer" + " " + tokenUsuario);


            return params;
        }};
        RequestQueue requestQueue = Volley.newRequestQueue(this);requestQueue.add(stringRequest);
    }






    public void agregarPermiso(View view) {
        Intent intentotra = new Intent(InformacionEmpleado.this, AgregarPermisos.class);
        Bundle bundle = new Bundle();
        bundle.putString("idEmpleado",idItem);

        bundle.putString("informacionId",codDepartamentoItem);

        intentotra.putExtras(bundle);

        startActivity(intentotra);
    }

    private void modificarFotografia() {
        ContentValues values = new ContentValues();

        if(bitmap !=null) {
            values.put(ContractParaEmpleados.Columnas.PICTURE,convertirImagenEnString(bitmap));
        }

        values.put(ContractParaEmpleados.Columnas.PENDIENTE_INSERCION, 1);
        try {
            String[] selectionArgs = new String[]{idItem};

            getApplicationContext().getContentResolver().update(ContractParaEmpleados.CONTENT_URI, values,"_id =?",selectionArgs);
            SyncAdapterEmpleados.sincronizarAhora(this, true);


        }catch (Exception e){
            Toast.makeText(this, "Error al cambiar la fotografia.", Toast.LENGTH_SHORT).show();
        }

    }
    public void agregarFotoInformacion(View view) {

        if(rol.equals("user")) {


            final String opciones[] = {"Ver foto", "Tomar foto", "Elegir de la Galeria", "Cancelar"};

            AlertDialog.Builder alerdialog = new AlertDialog.Builder(this);
            alerdialog.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (opciones[i] == "Ver foto") {


                        alertImagen();

                    }

                    if (opciones[i] == "Tomar foto") {


                        abrirCamara();


                    }
                    if (opciones[i] == "Elegir de la Galeria") {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent, "Selecciones App"), 300);


                    }


                }
            });
            alerdialog.create().show();
        }else{
            alertImagen();


        }
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



    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();


        if(ancho>= anchoNuevo||alto>=altoNuevo){

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
        if(bitmap!=null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, byteArrayOutputStream);
            byte[] imageByte = byteArrayOutputStream.toByteArray();
            imagenString = Base64.encodeToString(imageByte, Base64.DEFAULT);

        }
        return imagenString;
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


                     ;
                        try {



                            ExifInterface exif = new ExifInterface(imgFile.getAbsolutePath());

                            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                            switch (orientation) {

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    bitmap = rotateImage(getApplicationContext(),bitmap, 270);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    bitmap = rotateImage(getApplicationContext(),bitmap, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    bitmap = rotateImage(getApplicationContext(),bitmap, 90);
                                    break;

                            }








                        mostrarFoto.setImageBitmap(bitmap);

                        modificarFotografia();

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Intentelo nuevamente.", Toast.LENGTH_LONG).show();

                        }
                    }else{
                        //en caso de que no haya foto capturada el archivo en blanco se elimina
                          imgFile.delete();
                    }
                        break;
                        case VERFOTO:

                            Toast.makeText(getApplicationContext(), "Error al cambiar la foto", Toast.LENGTH_LONG).show();

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



                                mostrarFoto.setImageBitmap(bitmap);

                                modificarFotografia();

                            } catch (Exception e) {
                                e.printStackTrace();   Toast.makeText(getApplicationContext(), "Intentelo nuevamente.", Toast.LENGTH_LONG).show();}

                            break;

                    }

            }catch(Exception ex){

            }

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



    public static Bitmap rotateImage(Context context,  Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        try {
            matrix.postRotate(angle);

        source = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
        }catch (Exception e){
            Toast.makeText(context, "Intente Nuevamente.", Toast.LENGTH_LONG).show();
        }
        return  source;
    }



    public void alertImagen()

    {
        AlertDialog.Builder imagenD = new AlertDialog.Builder(InformacionEmpleado.this);
        LayoutInflater factory = LayoutInflater.from(InformacionEmpleado.this);
        final View view1 = factory.inflate(R.layout.item_alert_dialog, null);
        imagenD.setView(view1);
        //Estableciendole el titulo y la imagen al alertDialog
        ImageView imagen = view1.findViewById(R.id.imagenAlertDialog);
        if(bitmap ==null) {
            imagen.setImageResource(R.drawable.empleado_icon);


        } else {
            String[] selectionArgs = new String[]{String.valueOf(idItem)};
            cursorDatos = getContentResolver().query(ContractParaEmpleados.CONTENT_URI,null,"_id =?",selectionArgs,null);

            if(cursorDatos.moveToNext()){

                picture = cursorDatos.getString(cursorDatos.getColumnIndex("picture"));
            }
            final byte[] bytes = Base64.decode(picture, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            imagen.setImageBitmap(bitmap);
        }
        imagenD.setInverseBackgroundForced(true);


        imagenD.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        imagenD.create().show();


        return;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(rol.equals("user")) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.quejas:
                Intent intent2 = new Intent(this, EnviarCorreo.class);
                startActivity(intent2);

                break;
            case R.id.cerrarSesion:

                alertLogaut();


                break;
          //  case R.id.solicitudPermiso:
            //Intent intent = new Intent(this, SolicitudPermisoActivity.class);
              //  startActivity(intent);

              //  break;
        }

        return super.onOptionsItemSelected(item);
    }


    private File getPictureFile()throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile =timeStamp;
        File storeImagen = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(pictureFile,".jpg",storeImagen);
         pictureFilePath = imagen.getAbsolutePath();



        return  imagen;
    }
    private void cerrarSesion() {
        final ProgressDialog loading = ProgressDialog.show(this, "Por favor espere...", "Actualizando datos...",
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
                                Toast.makeText(getApplicationContext(), "" + mensaje, Toast.LENGTH_SHORT).show();
                            }
                            SharedPreferences sharedPreferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            Intent intentInfo = new Intent(getApplicationContext(),Login.class);
                            startActivity(intentInfo);
                            finish();
                            loading.dismiss();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("Authorization", "Bearer" + " " + tokenUsuario);
                    return parametros;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    public void alertLogaut(){

        AlertDialog.Builder builder = new AlertDialog.Builder(InformacionEmpleado.this);
        builder.setMessage("Seguro que desea cerrar sesion.?")
                .setTitle("Advertencia")
                .setCancelable(false)
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                            }
                        })
                .setPositiveButton("si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                eventoCerrar=2;
                                updateToken();




                            }

                        });


        AlertDialog alert = builder.create();
        alert.show();



    }


    @Override
    public void onClick(RecyclerView.ViewHolder holder, int idDepartamento) {
        observaciones = adapter.getCursor().getString(adapter.getCursor().getColumnIndex("observaciones"));
        if (observaciones ==null ||observaciones.equals("")) {

            Toast.makeText(InformacionEmpleado.this, "Sin observaciones", Toast.LENGTH_SHORT).show();
        } else {
             AlertDialog.Builder builder = new AlertDialog.Builder(InformacionEmpleado.this);
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onLongClick(RecyclerView.ViewHolder holder, final int idPermiso) {


        if (rol.equals("user")) {

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(InformacionEmpleado.this);
            builder.setMessage("Seguro que desea eliminar el permiso?")
                    .setTitle("Advertencia")
                    .setCancelable(false)


                    .setNegativeButton("Modificar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    Intent intentotra = new Intent(InformacionEmpleado.this, AgregarPermisos.class);
                                    contador = 2;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("idEmpleado", idItem);
                                    bundle.putString("nombreEmpleado", nameItem);
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
                                    ContentValues values = new ContentValues();

                                    values.put(ContractParaDepartamentos.Columnas.PENDIENTE_INSERCION, 1);
                                    values.put(ContractParaDepartamentos.Columnas.PENDIENTE_ELIMINACION, 1);
                                    values.put(ContractParaDepartamentos.Columnas.ESTADO, 0);
                                    String[] selectionArgs = new String[]{String.valueOf(idPermiso)};

                                    getContentResolver().update(ContractParaPermisos.CONTENT_URI, values,"id =?",selectionArgs);

                                    SyncAdapterPermisos.sincronizarAhora(getApplicationContext(), true);


                                }

                            });


            AlertDialog alert = builder.create();
            alert.show();

        }



    }



    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
         
        String[] selectionArgs = new String[]{"0","0",idItem};



        return new CursorLoader(this, ContractParaPermisos.CONTENT_URI,
                null, "eliminado = ? and archivado =? and idEmpleado=? ",selectionArgs , null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public void generarHoja(View view) {
         agregarProcedimiento();
    }


    public void agregarPDF(View view) {
        if(idItem.equals(idAdmin)) {
            agregarProcedimiento();
        }else{

            Toast.makeText(this, "No puede generar peticiones de permisos a otros usuarios.", Toast.LENGTH_SHORT).show();
        }
    }
    public void agregarProcedimiento() {
        datos = OperacionesBaseDatos
                .obtenerInstancia(getApplicationContext());

        String[] selectionArgs = new String[]{String.valueOf(codDepartamentoItem),String.valueOf(codDepartamentoItem)};
        cursorDepto = getContentResolver().query(ContractParaDepartamentos.CONTENT_URI,null,"_id =? or idRemota =?",selectionArgs,null);
         if (cursorDepto.moveToNext()) {
            nombreDepto = cursorDepto.getString(cursorDepto.getColumnIndex("nombreDepto"));

        }


        Intent intentotra = new Intent(getApplicationContext(), SolicitudPermisoActivity.class);
         Bundle bundle = new Bundle();
        bundle.putString("idEmpleado", String.valueOf(idItem));
        bundle.putString("nombreDepartamento", nombreDepto);
        bundle.putString("nombreEmpleado", String.valueOf(nameItem));


        intentotra.putExtras(bundle);
        startActivity(intentotra);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(i) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }
}
