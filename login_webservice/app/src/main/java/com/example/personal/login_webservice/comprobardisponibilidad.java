package com.example.personal.login_webservice;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class comprobardisponibilidad extends AppCompatActivity {
    TextView espe, fech, hor, doc,cod;
    ProgressDialog progreso;
    Button regist,com;
    EditText ced;
    String esp, fecha,horario,doct;
    int uno;
    StringRequest stringRequest;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprobardisponibilidad);
        espe = (TextView) findViewById(R.id.especialidad1);
        fech = (TextView) findViewById(R.id.fecha1);
        hor = (TextView) findViewById(R.id.horario1);
        doc = (TextView) findViewById(R.id.doctor1);


        com= (Button) findViewById(R.id.comprobar);

        Bundle bundle = getIntent().getExtras();
       esp = bundle.getString("ESPECIALIDAD");


        espe.setText(esp);
        fech.setText(fecha);

        hor.setText(horario);
        doc.setText(doct);





    }
    public void OnLogin(View view) {
        String username = fech.getText().toString();
        String password = hor.getText().toString();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username, password);
        

    }

    public void log(View v) {



        String url="http://hospitalmanabideldia.com/config.php?usu="+ hor.getText().toString()+
                "&pas=" + doc.getText().toString();

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {




                if(response.trim().equals("Ya exite")){

                    Intent i = new Intent(getApplication(), inicio.class);
                    startActivity(i);
                    Toast.makeText(getApplication(),"exitoss",Toast.LENGTH_SHORT).show();

                }else{

                    Intent i = new Intent(getApplication(), condiciones.class);
                    i.putExtra("ESPECIALIDAD", espe.getText().toString());
                    i.putExtra("FECHA", fech.getText().toString());
                    i.putExtra("HORARIO", hor.getText().toString());
                    i.putExtra("DOCTOR", doc.getText().toString());

                    startActivity(i);


                    Toast.makeText(getApplication(),"Cita disponible",Toast.LENGTH_SHORT).show();



                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar" + error,Toast.LENGTH_SHORT).show();

            }
        });

        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }






}

