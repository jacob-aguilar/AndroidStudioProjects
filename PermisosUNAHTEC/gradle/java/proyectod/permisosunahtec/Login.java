package proyectod.permisosunahtec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText usu, pas ,rrt;
    Button btningresa ;
     TextView btnCambiar ,recuperarcontra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        usu= (EditText) findViewById(R.id.EditUsuario);
        pas= (EditText) findViewById(R.id.EditContrasena);
        btningresa=(Button) findViewById(R.id.btnIngresar);
        btnCambiar=(Button) findViewById(R.id.textvCambiar);
        recuperarcontra=(TextView) findViewById(R.id.textvOlvideContrasenia);


        btningresa = (Button) findViewById(R.id.btnIngresar);

        btningresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(usu.getText().toString().equalsIgnoreCase("")||pas.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Ingrese los datos",Toast.LENGTH_LONG).show();

                }else {
                    ingresar();
                }


            }});

        btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),cambiarcontrasenia.class);
                startActivity(intent);


            }});



    }


    private void ingresar() {
        final ProgressDialog loading = ProgressDialog.show(this, "Por favor espere...", "Actualizando datos...",
                false, false);
        String REGISTER_URL = "http://192.168.137.1/permisos-ws/nbproject/public/api/auth/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Ingresado exitosamente",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(),ListaDeptos.class);
                        startActivity(intent);
                    }},new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {loading.dismiss();
                    Toast.makeText(getApplicationContext(),"correo o password invalido",Toast.LENGTH_LONG).show();}}){
                    @Override
                    protected Map<String,String> getParams(){ Map<String,String> params = new HashMap<String, String>();
                    params.put("email", usu.getText().toString());
                    params.put("password", pas.getText().toString());
                        params.put("Content-Type","application/json");
                        params.put("X-Requested-With","XMLHttpRequest");


                    return params;}};
                      RequestQueue requestQueue = Volley.newRequestQueue(this);requestQueue.add(stringRequest);}
}
