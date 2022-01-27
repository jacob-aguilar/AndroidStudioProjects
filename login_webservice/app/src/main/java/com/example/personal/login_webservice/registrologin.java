package com.example.personal.login_webservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.bitmap;
import static java.security.AccessController.getContext;

public class  registrologin extends AppCompatActivity  implements Response.Listener<JSONObject>, Response.ErrorListener{
    EditText cedula,nombres,contraseña,correo;
    TextView col;
    Button registrar;
    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    ProgressBar pro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrologin);
        cedula= (EditText) findViewById(R.id.registro_cedula);
        nombres= (EditText) findViewById(R.id.registro_nombres);
        correo= (EditText) findViewById(R.id.registro_correo);

        contraseña= (EditText) findViewById(R.id.registro_contraseña);
        registrar= (Button) findViewById(R.id.registrar);
        request= Volley.newRequestQueue(this);




        //bloquiar tecla de salto
        cedula.setSingleLine(false);
        cedula.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT);
        nombres.setSingleLine(false);
        nombres.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT);
        correo.setSingleLine(false);
        correo.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT);
/* pro.setProgress(10);


                            pro.setProgressTintList(ColorStateList.valueOf(Color.RED));
                            col.setText("Debil :(");*/




        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               cargarwebservice();
            }
        });

    }

    public void cargarwebservice(){

        try {
            if(nombres.getText().toString().equals("")||correo.getText().toString().equals("")||cedula.getText().toString().equals("")||contraseña.getText().toString().equals("")){
                Toast.makeText(this,"Al menos un campo vacio, todos los campos son obligatorio, Por favor Completelo",Toast.LENGTH_LONG).show();
            }else {
                progreso = new ProgressDialog(this);
                progreso.setMessage("Cargando...");
                progreso.show();
                //https://clinicaprotoipo.000webhostapp.com/registro.php
                String url = "http://hospitalmanabideldia.com/registro_pacientes.php?Nombres=" + nombres.getText().toString()
                        + "&Correo=" + correo.getText().toString()+ "&username=" + cedula.getText().toString()+ "&password=" + contraseña.getText().toString();
                url = url.replace(" ", "%20");
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
                request.add(jsonObjectRequest);
            }

        }catch (Exception exe){
            Toast.makeText(this,exe.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onResponse(JSONObject response) {


        Toast.makeText(this,"Se registrado correctamente", Toast.LENGTH_SHORT).show();
        progreso.hide();
        nombres.setText("");
        correo.setText("");
        nombres.setText("");
        contraseña.setText("");
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getApplicationContext(),"No se pudo registrar , Hubo un error al conectar por favor verifica la conexión a internet o intente nuevamente , Error : "+ error.toString(), Toast.LENGTH_LONG).show();

        Log.i("ERROR", error.toString());

    }




}