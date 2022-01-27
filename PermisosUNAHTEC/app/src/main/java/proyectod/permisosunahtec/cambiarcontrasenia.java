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

public class cambiarcontrasenia extends AppCompatActivity {

    EditText usuario ,passworActual,nuevoPassword,confirmarPassword;
    Button cambiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiarcontrasenia);


        getSupportActionBar().setTitle("Cambiar Contraseña");
        usuario = (EditText) findViewById(R.id.EditCorreo);
        passworActual = (EditText) findViewById(R.id.EditContrasenaActual);
        nuevoPassword = (EditText) findViewById(R.id.EditNuevaContrasena);
        confirmarPassword = (EditText) findViewById(R.id.EditConfirmaContrasena);
        cambiar = (Button) findViewById(R.id.btnCambiarContrasena);



        cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    if(usuario.getText().toString().equalsIgnoreCase("")||passworActual.getText().toString().equalsIgnoreCase("")||nuevoPassword.getText().toString().equalsIgnoreCase("")||confirmarPassword.getText().toString().equalsIgnoreCase("")){

                        Toast.makeText(getApplicationContext(),"Ingrese los datos",Toast.LENGTH_LONG).show();
                    }else {
                        if (nuevoPassword.getText().toString().length() < 8 || confirmarPassword.getText().toString().length() < 8) {
                            Toast.makeText(getApplicationContext(), "El nuevo password no debe contener menos de 8 caracteres", Toast.LENGTH_LONG).show();
                        } else {


                            if (nuevoPassword.getText().toString().equals(confirmarPassword.getText().toString())) {
                                ingresar();

                            } else {

                                Toast.makeText(getApplicationContext(), "nueva password con confirmar password no coinciden", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No hay acceso a internet en estos momentos", Toast.LENGTH_LONG).show();

                }




            }});
    }



    private void ingresar() {
        final ProgressDialog loading = ProgressDialog.show(this, "Por favor espere...", "Actualizando datos...",
                false, false);
        String REGISTER_URL =  Utilidades.URL+"api/auth/recuperarpassword";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                            loading.dismiss();
                            Toast.makeText(getApplicationContext(),"Password cambiada exitosamente",Toast.LENGTH_LONG).show();
                            finish();



                    }},new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {loading.dismiss();
                if(error.toString().equals("com.android.volley.TimeoutError")){
                    Toast.makeText(getApplicationContext(), "Revise su conexión a internet" , Toast.LENGTH_LONG).show();
                }else {
                Toast.makeText(getApplicationContext(),"correo o password invalido",Toast.LENGTH_LONG).show();}}}){
            @Override
            protected Map<String,String> getParams(){ Map<String,String> params = new HashMap<String, String>();
                params.put("email", usuario.getText().toString());
                params.put("password", passworActual.getText().toString());
                params.put("mypassword", nuevoPassword.getText().toString());
                params.put("mypassword_confirmation", confirmarPassword.getText().toString());
                params.put("Content-Type","application/json");
                params.put("X-Requested-With","XMLHttpRequest");


                return params;}};
        RequestQueue requestQueue = Volley.newRequestQueue(this );requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)); }
}
