package proyectod.permisosunahtec;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import proyectod.permisosunahtec.pojo.EmpleadoGetYSet;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.utils.Utilidades;

public class Login extends AppCompatActivity {

    EditText usu, pas;
    Button btningresa;
    TextView btnCambiar, recuperarcontra;
    EmpleadoGetYSet user;

    private ArrayList<EmpleadoGetYSet> contes;

    private JSONArray lista;
    private JSONArray listaNotificacion;
    private String DATA_URL;
    private String rol;
    private int variable = 3;

    private String pictureUsuario;
    private String usuario;
    private JSONObject responceUsuario;
    String token;
    String users;
     private JSONObject json_data;
    public static String idPrueba;
    String pass;
    private static final String TAG = "FIREBASE_TOKEN";
    private String rolEmpleado;
    private int eventorecordar;
    private String nameEmpleado;
    private String codDepartamento;
    private FirebaseAuth miAuth;
    private String name;
    private String picture;
     private String id;
    private String email;
    private String idEMpNotifi;
    private String titleNotifi;
    private String estadoNotifi;
    private String bodyNotifi;
    private String refrescheToken;
    private String tokenAuto;
     private CardView cardView;
    private TextView mas_opciones_login_TV;
    private Cursor cursorDatos;
int i =1;
    private Object idNotificacionPendiente;
    private Bitmap bitmap;
    private String imagenString;

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mas_opciones_login_TV = findViewById(R.id.mas_login_TV);
        cardView = findViewById(R.id.card_view);
        refrescheToken = FirebaseInstanceId.getInstance().getToken();
        miAuth = FirebaseAuth.getInstance();

        usu = (EditText) findViewById(R.id.EditUsuario);
        pas = (EditText) findViewById(R.id.EditContrasena);
        btningresa = (Button) findViewById(R.id.btnIngresar);
        btnCambiar = findViewById(R.id.textvCambiar);
        recuperarcontra = (TextView) findViewById(R.id.textvOlvideContrasenia);
        btnCambiar.setVisibility(View.GONE);
        recuperarcontra.setVisibility(View.GONE);

        btningresa = (Button) findViewById(R.id.btnIngresar);
        cargarPreferencias();




          if(token ==null) {


         }else {

              String[] selectionArgs = new String[]{String.valueOf(id)};
             cursorDatos = getContentResolver().query(ContractParaEmpleados.CONTENT_URI,null,"_id =?",selectionArgs,null);

             while (cursorDatos.moveToNext()){
              id =   cursorDatos.getString(cursorDatos.getColumnIndex("_id"));
              codDepartamento=   cursorDatos.getString(cursorDatos.getColumnIndex("codDepartamento"));
              email=   cursorDatos.getString(cursorDatos.getColumnIndex("email"));
              rol=   cursorDatos.getString(cursorDatos.getColumnIndex("rol"));
               name=  cursorDatos.getString(cursorDatos.getColumnIndex("name"));
               picture=  cursorDatos.getString(cursorDatos.getColumnIndex("picture"));


                  if (rol.equals("admin")) {
                     Log.i("SyncAda","orita");
                     Intent intent = new Intent(getApplicationContext(), ContenFragment.class);

                     guardarPreferenciasLocales(name,id,rol ,codDepartamento);
                     startActivity(intent);

                     finish();
                 } else if (rol.equals("user")) {

                      try {

                          final byte[] bytes = Base64.decode(picture, Base64.DEFAULT);
                          BitmapFactory.Options options = new BitmapFactory.Options();
                          options.inSampleSize = 3;
                          bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                      }catch (Exception e){

                      }


                     Intent intentInfo = new Intent(getApplicationContext(), InformacionEmpleado.class);



                     Bundle bundle = new Bundle();
                     bundle.putString("id", id);
                     bundle.putString("rol", rol);
                     bundle.putString("email", email);
                     bundle.putString("codDepartamento", codDepartamento);
                     bundle.putString("picture", convertirImagenEnString(bitmap));
                     bundle.putString("name", name);
                     bundle.putInt("variable", variable);

                     guardarPreferenciasLocales(name,id,rol,codDepartamento);
                     intentInfo.putExtras(bundle);


                     startActivity(intentInfo);

                     finish();
                 }


             }

         }


        btningresa.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


                if (networkInfo != null && networkInfo.isConnected()) {
                    if (usu.getText().toString().equalsIgnoreCase("") || pas.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Ingrese los datos", Toast.LENGTH_LONG).show();

                    } else {
                        usuario = usu.getText().toString();


                        ingresar();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No hay acceso a internet en estos momentos", Toast.LENGTH_LONG).show();
                }


            }
        });

        btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), cambiarcontrasenia.class);
                startActivity(intent);


            }
        });


    }

    public Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {

        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }




    private void ingresar() {
        final ProgressDialog loading = ProgressDialog.show(this, "Por favor espere...", "Actualizando datos...",
                false, false);
        String REGISTER_URL = Utilidades.URL + "api/auth/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Utilidades.TOKEN = response.trim();
                        Utilidades.TOKEN = Utilidades.TOKEN.substring(1, Utilidades.TOKEN.length() - 1);
                          invocarServicio();

                        loading.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();

                if (error.toString().equals("com.android.volley.ServerError")) {
                    Toast.makeText(getApplicationContext(), "Presentamos problemas intentelo mas tarde.", Toast.LENGTH_LONG).show();

                } else if (error.toString().equals("com.android.volley.TimeoutError")) {
                    Toast.makeText(getApplicationContext(), "Revise su conexión a internet", Toast.LENGTH_LONG).show();
                } else if (error.toString().equals("com.android.volley.AuthFailureError")) {
                    Toast.makeText(getApplicationContext(), "Correo o password invalido.", Toast.LENGTH_LONG).show();
                } else {
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", usu.getText().toString());
                params.put("password", pas.getText().toString());


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void activityResetPassword(View view) {

        Intent intent = new Intent(getApplicationContext(), RestablecerContrasenia.class);
        startActivity(intent);
    }

    private void invocarServicio() {
        DATA_URL = Utilidades.URL + "api/auth/tokenUser";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                DATA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        showListView(response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, ""+Utilidades.TOKEN, Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer" + " " + Utilidades.TOKEN);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


    private void showListView(JSONObject obj) {
        try {

            user = null;


            contes = new ArrayList<>();
            lista = obj.optJSONArray("users");
            for (int i = 0; i < lista.length(); i++) {
                user = new EmpleadoGetYSet();
                json_data = lista.getJSONObject(i);
                user.setRol(json_data.getString("rol"));


                user.setNombre(json_data.getString("name"));
                user.setCodEmpleado(json_data.getString("id"));
                user.setEmail(json_data.getString("email"));
                user.setDatosImagen(json_data.getString("picture"));
                codDepartamento = json_data.getString("codDepartamento");


                email =json_data.getString("email");
                idPrueba = json_data.getString("id");
                pictureUsuario = json_data.getString("picture");
                nameEmpleado = json_data.getString("name");

                contes.add(user);
                rol = user.getRol();

                invocarNotificacion();

            }


        } catch (Exception ex) {

        } finally {
        }
    }

    public void guardarPreferencias(String responce ,String name,String id ,String rol, String codDepartamento) {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);




        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("rol", rol);
        editor.putString("token", responce);
        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("codDepartamento", codDepartamento);



        editor.apply();
    }
    public void guardarPreferenciasLocales(String name,String id ,String rol,String codDepartamento) {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);



        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("rol", rol);

        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("codDepartamento", codDepartamento);



        editor.apply();
    }

    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        id = preferences.getString("id", "");
        token = preferences.getString("token", "");



    }



    public void mandarNotification2() {
        final ProgressDialog loading = ProgressDialog.show(this, "Por favor espere...", "Actualizando datos...",
                false, false);
        JSONObject json = new JSONObject();
        try {
            JSONObject userData = new JSONObject();
            userData.put("title", titleNotifi);
            userData.put("body", bodyNotifi);
            userData.put("sound", "default");
            json.put("to", refrescheToken);
            json.put("data", userData);


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), " error aqui", Toast.LENGTH_LONG).show();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.dismiss();
              if (i == listaNotificacion.length()){
                  borrarNotificacion();
              }
              i++;




            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), " no responde", Toast.LENGTH_LONG).show();
                loading.dismiss();


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                String autorizacionFirebase = "AAAAFXKYB2Y:APA91bGLoCcd89yFHdTds-U7VFMpcwEX8w3rt-l3jZabwi9QKaEZBUju8lIH8VLmANUMw8ciLAuYlqnt-32QzHcIP7dHmeu2klw-MmTPYSGk-X44J6mt7nC3d0RosnQLQXnwEW8WBm3m";

                params.put("Authorization", "Key=" + " " + autorizacionFirebase);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void invocarNotificacion() {
        final ProgressDialog loading = ProgressDialog.show(this, "Por favor espere...", "Actualizando datos...",
                false, false);
        String DATA_URL = Utilidades.URL + "api/auth/listaNotificacion/" + idPrueba;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                DATA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showListView2(response);
                        loading.dismiss();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading.dismiss();


                inten();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer" + " " + Utilidades.TOKEN);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


    private void showListView2(JSONObject obj) {
        try {

            listaNotificacion = obj.optJSONArray("notificacion");
            for (int i = 0; i < listaNotificacion.length(); i++) {

                json_data = listaNotificacion.getJSONObject(i);
                
                idNotificacionPendiente =json_data.get("id");

                idEMpNotifi = json_data.getString("idEmpleado");
                bodyNotifi = json_data.getString("body");
                titleNotifi = json_data.getString("title");
                estadoNotifi = json_data.getString("estado");


                mandarNotification2();


            }
            if (listaNotificacion.length() == 0) {


                inten();

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void borrarNotificacion() {

        final ProgressDialog loading = ProgressDialog.show(this, "Por favor espere...", "Actualizando datos...",
                false, false);

        String REGISTER_URL = Utilidades.URL + "api/auth/deleteNotificacion/" + idPrueba;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        inten();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

;
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer" + " " + Utilidades.TOKEN);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 4, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void inten() {


        if (rol.equals("admin")) {

            Intent intent = new Intent(getApplicationContext(), ContenFragment.class);


                   guardarPreferencias(Utilidades.TOKEN,nameEmpleado,idPrueba,rol,codDepartamento);

            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Ingresado exitosamente", Toast.LENGTH_LONG).show();
            finish();
        } else {
            final byte[] bytes = Base64.decode(pictureUsuario, Base64.DEFAULT);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,options);
            Intent intentInfo = new Intent(getApplicationContext(), InformacionEmpleado.class);



            Bundle bundle = new Bundle();
            bundle.putString("id", idPrueba);
            bundle.putString("rol", rol);
            bundle.putString("email", email);
            bundle.putString("codDepartamento", codDepartamento);
            bundle.putString("picture", convertirImagenEnString(bitmap));
            bundle.putString("name", nameEmpleado);
            bundle.putInt("variable", variable);
            guardarPreferencias(Utilidades.TOKEN,nameEmpleado,idPrueba,rol,codDepartamento);





            intentInfo.putExtras(bundle);


            startActivity(intentInfo);
            Toast.makeText(getApplicationContext(), "Ingresado exitosamente", Toast.LENGTH_LONG).show();
            finish();
        }
    }


    public void ocultarOpciones(View view) {
        btnCambiar.setVisibility(View.VISIBLE);
        recuperarcontra.setVisibility(View.VISIBLE);
        mas_opciones_login_TV.setVisibility(View.GONE);
    }
    private String convertirImagenEnString(Bitmap bitmap) {
        if(bitmap!=null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
            byte[] imageByte = byteArrayOutputStream.toByteArray();
            imagenString = Base64.encodeToString(imageByte, Base64.DEFAULT);

        }
        return imagenString;
    }
}



