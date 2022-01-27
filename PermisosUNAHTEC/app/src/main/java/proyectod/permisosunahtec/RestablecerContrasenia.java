package proyectod.permisosunahtec;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import proyectod.permisosunahtec.utils.Utilidades;

public class RestablecerContrasenia extends AppCompatActivity {

    EditText correo;
    Button enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecercontrasenia);


        getSupportActionBar().setTitle("Restablecer Contraseña");

        correo = (EditText) findViewById(R.id.EditCorreorestablecercontrasenia);
        enviar = (Button)  findViewById(R.id.btnEnviarCorreo);

        enviar.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                      if(correo.getText().toString().equalsIgnoreCase("")){

                      }else {
                          enviarCorreo();
                      }
                    } else {
                        Toast.makeText(getApplicationContext(), "No hay acceso a internet en estos momentos", Toast.LENGTH_LONG).show();

                    }



                }});
    }
    private void enviarCorreo() {

        final ProgressDialog loading = ProgressDialog.show(this, "Por favor espere...", "Actualizando datos...",
                false, false);

        String REGISTER_URL =  Utilidades.URL+"api/password/create";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Revise su correo para generar una nueva password",Toast.LENGTH_LONG).show();
                    }},new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {loading.dismiss();
                if(error.toString().equals("com.android.volley.TimeoutError")){
                    Toast.makeText(getApplicationContext(), "Revise su conexión a internet" , Toast.LENGTH_LONG).show();
                }else {
                Toast.makeText(getApplicationContext(),"correo invalido"+error,Toast.LENGTH_LONG).show();}}}){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", correo.getText().toString());
                return params;}
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }



}