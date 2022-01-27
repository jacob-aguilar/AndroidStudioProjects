package proyectod.permisosunahtec;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.utils.Utilidades;
import proyectod.permisosunahtec.web.VolleySingleton;

public class EnviarCorreo extends AppCompatActivity {

    private TextView textNombre;
    int id_usuario;
    private EditText comentarioEditText;
    Button btnEnviarCorreo;
    private String tokenAdministrador;
    private JSONArray lista;
    private JSONObject json_data;
    private String emailAdmin;
    private Cursor cursorDatos;
    private String idPreference;
    private String nombreEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_correo);
        getSupportActionBar().setTitle("Enviar correo");
        cargarPreferencias();


        btnEnviarCorreo = findViewById(R.id.btnEnviarCorreo);
        SharedPreferences sharedPreferences = getSharedPreferences("credenciales",MODE_PRIVATE);
         id_usuario =Integer.parseInt(sharedPreferences.getString("id",""));

        comentarioEditText = (EditText) findViewById(R.id.cajaComentario);




    }


    private void invocarTokenFirebaseAdmin( ) {


        String   DATA_URL =  Utilidades.URL+"api/auth/getAdmin";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                DATA_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showListViewAdmin(response);




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EnviarCorreo.this, "Revise su conexiójn a internet", Toast.LENGTH_SHORT).show();





            }
        })    {

            @Override
            public Map<String,String> getHeaders(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer"+" "+tokenAdministrador);




                return params;}};

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjReq);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }



    private void showListViewAdmin(JSONObject obj  ) {
        try{





            lista = obj.optJSONArray("users");
            for (int i = 0; i < lista.length(); i++) {

                 json_data = lista.getJSONObject(i);



                if(i==0){
                    emailAdmin =json_data.getString("email");

                }else {


                    emailAdmin = emailAdmin + ", " + json_data.getString("email");
                }




            }
            String[] selectionArgs = new String[]{idPreference};

            cursorDatos = getApplicationContext().getContentResolver().query(ContractParaEmpleados.CONTENT_URI, null, "_id =?", selectionArgs, null);
              if( cursorDatos.moveToNext()){
                  nombreEmpleado = cursorDatos.getString(cursorDatos.getColumnIndex("name"));
            }
            String[] email =new String[] {emailAdmin};
            String subject = new String("Queja del Empleado: "+nombreEmpleado);
            String mensaje = comentarioEditText.getText().toString();

            escribirCorreo(email,subject,mensaje);







        } catch (Exception ex) {

        } finally {
        }
    }
    private void cargarPreferencias() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);

       
        tokenAdministrador = preferences.getString("token", "");
        idPreference =preferences.getString("id", "");






    }

    private  void escribirCorreo(String[] email,String subject,String mensaje){

        Intent intentEnviarCorreo = new Intent(Intent.ACTION_SEND);
        intentEnviarCorreo.setType("*/*");
        intentEnviarCorreo.putExtra(Intent.EXTRA_SUBJECT,subject);
        intentEnviarCorreo.putExtra(Intent.EXTRA_EMAIL,email);
        intentEnviarCorreo.putExtra(Intent.EXTRA_TEXT,mensaje);

        if(intentEnviarCorreo.resolveActivity(getPackageManager()) != null){
            startActivity(intentEnviarCorreo);
        }


    }

    public void enviarCorreo(View view) {

        invocarTokenFirebaseAdmin();
    }
}
