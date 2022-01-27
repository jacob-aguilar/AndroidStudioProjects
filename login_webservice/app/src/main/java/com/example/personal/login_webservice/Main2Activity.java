package com.example.personal.login_webservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class Main2Activity extends AppCompatActivity {
    TextView et1, et2,et3,et4;
    String fecha, horario,esp,doct;
    ProgressDialog progreso;
    StringRequest stringRequest;
    Button val;
    String jhon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProgressDialog progreso;
        setContentView(R.layout.activity_main2);
        et1= (TextView) findViewById(R.id.espprac);
        et2= (TextView) findViewById(R.id.docpra);
        et3= (TextView) findViewById(R.id.fechprac);
        et4= (TextView) findViewById(R.id.horaprac);
        val= (Button) findViewById(R.id.veri);
        Bundle bundle = getIntent().getExtras();
        esp = bundle.getString("ESPECIALIDAD");
        fecha = bundle.getString("FECHA");
       horario = bundle.getString("HORARIO");
        doct = bundle.getString("DOCTOR");
        jhon=fecha;







        val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et1.setText(esp);

                et2.setText(fecha);
                et3.setText(horario);
                et4.setText(doct);


                if(et1.getText().toString().equals("Traumotología")) {

                    veritraumo();


                }

                if(et1.getText().toString().equals("Cardiología")) {
                    vericardio();

                }
                if(et1.getText().toString().equals("Pediatría")){
                    veritraumo();

                }
                if(et1.getText().toString().equals("Ginecología")){
                    veriginec();

                }
                if(et1.getText().toString().equals("Obstreticia")){

                    veriobstre();

                }
                if(et1.getText().toString().equals("Cirugía Gen.")){

                     vericirugiagen();

                }if(et1.getText().toString().equals("Medicina Gener.")){
                        verimedicinagen();
                }


            }
        });
    }



    private void veritraumo() {

        progreso = new ProgressDialog(this);
        progreso.setMessage("Verificando Disponibilidad...");
        progreso.show();


        String url="http://hospitalmanabideldia.com/verificaciontrumotologia.php?usu="+ jhon +
                "&pas=" + et3.getText().toString();
        url = url.replace(" ", "%20");

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                if(response.trim().equals("exito")){

                    progreso.hide();
                    Intent i = new Intent(getApplication(), agendarcita.class);
                    startActivity(i);
                    losentimos();
                }else{
                    progreso.hide();
                   felicidades();
                    Intent i = new Intent(getApplication(), condiciones.class);
                    i.putExtra("ESPECIALIDAD", et1.getText().toString());
                    i.putExtra("FECHA", et3.getText().toString());
                    i.putExtra("HORARIO", et4.getText().toString());
                    i.putExtra("DOCTOR", et2.getText().toString());
                    startActivity(i);



                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar" + error,Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }
    private void vericardio() {


        progreso = new ProgressDialog(this);
        progreso.setMessage("Verificando Disponibilidad...");
        progreso.show();





        String url="http://hospitalmanabideldia.com/verificacioncardiologia.php?usu="+ jhon +
                "&pas=" + et3.getText().toString();
        url = url.replace(" ", "%20");

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {




                if(response.trim().equals("exito")){
                    progreso.hide();
                    Intent i = new Intent(getApplication(), agendarcita.class);
                    startActivity(i);
                    losentimos();
                }else{
                    progreso.hide();
                    felicidades();
                    Intent i = new Intent(getApplication(), condiciones.class);
                    i.putExtra("ESPECIALIDAD", et1.getText().toString());
                    i.putExtra("FECHA", et3.getText().toString());
                    i.putExtra("HORARIO", et4.getText().toString());
                    i.putExtra("DOCTOR", et2.getText().toString());
                    startActivity(i);


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar" + error,Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }
    private void veripedia() {


        progreso = new ProgressDialog(this);
        progreso.setMessage("Verificando Disponibilidad...");
        progreso.show();





        String url="http://hospitalmanabideldia.com/verificacionpediatria.php?usu="+ jhon +
                "&pas=" + et3.getText().toString();
        url = url.replace(" ", "%20");

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {




                if(response.trim().equals("exito")){

                    progreso.hide();
                    Intent i = new Intent(getApplication(), agendarcita.class);
                    startActivity(i);

                    felicidades();


                }else{

                    progreso.hide();
                    losentimos();
                    Intent i = new Intent(getApplication(), condiciones.class);
                    i.putExtra("ESPECIALIDAD", et1.getText().toString());
                    i.putExtra("FECHA", et3.getText().toString());
                    i.putExtra("HORARIO", et4.getText().toString());
                    i.putExtra("DOCTOR", et2.getText().toString());
                    startActivity(i);


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar" + error,Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }
    private void veriginec() {



        progreso = new ProgressDialog(this);
        progreso.setMessage("Verificando Disponibilidad...");
        progreso.show();

        String url="http://hospitalmanabideldia.com/verificacionginecologia.php?usu="+ jhon +
                "&pas=" + et3.getText().toString();
        url = url.replace(" ", "%20");

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {




                if(response.trim().equals("exito")){

                    progreso.hide();
                    Intent i = new Intent(getApplication(), agendarcita.class);
                    startActivity(i);

                    losentimos();
                }else{

                    progreso.hide();
                    felicidades();
                    Intent i = new Intent(getApplication(), condiciones.class);
                    i.putExtra("ESPECIALIDAD", et1.getText().toString());
                    i.putExtra("FECHA", et3.getText().toString());
                    i.putExtra("HORARIO", et4.getText().toString());
                    i.putExtra("DOCTOR", et2.getText().toString());
                    startActivity(i);


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar" + error,Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }
    private void veriobstre() {


        progreso = new ProgressDialog(this);
        progreso.setMessage("Verificando Disponibilidad...");
        progreso.show();



        String url="http://hospitalmanabideldia.com/verificacionobstreticia.php?usu="+ jhon +
                "&pas=" + et3.getText().toString();
        url = url.replace(" ", "%20");

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {




                if(response.trim().equals("exito")){
                    progreso.hide();
                    Intent i = new Intent(getApplication(), agendarcita.class);
                    startActivity(i);

                    losentimos();

                }else{

                    progreso.hide();
                    felicidades();
                    Intent i = new Intent(getApplication(), condiciones.class);
                    i.putExtra("ESPECIALIDAD", et1.getText().toString());
                    i.putExtra("FECHA", et3.getText().toString());
                    i.putExtra("HORARIO", et4.getText().toString());
                    i.putExtra("DOCTOR", et2.getText().toString());
                    startActivity(i);


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar" + error,Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }
    private void vericirugiagen() {



        progreso = new ProgressDialog(this);
        progreso.setMessage("Verificando Disponibilidad...");
        progreso.show();


        String url="http://hospitalmanabideldia.com/verificacioncirugiageneral.php?usu="+ jhon +
                "&pas=" + et3.getText().toString();
        url = url.replace(" ", "%20");

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {




                if(response.trim().equals("exito")){


                        progreso.hide();
                    Intent i = new Intent(getApplication(), agendarcita.class);
                    startActivity(i);
losentimos();


                }else{

                    progreso.hide();
                    felicidades();
                    Intent i = new Intent(getApplication(), condiciones.class);
                    i.putExtra("ESPECIALIDAD", et1.getText().toString());
                    i.putExtra("FECHA", et3.getText().toString());
                    i.putExtra("HORARIO", et4.getText().toString());
                    i.putExtra("DOCTOR", et2.getText().toString());
                    startActivity(i);


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar" + error,Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }

    private void verimedicinagen() {


        progreso = new ProgressDialog(this);
        progreso.setMessage("Verificando Disponibilidad...");
        progreso.show();



        String url="http://hospitalmanabideldia.com/verificacionmedicinageneral.php?usu="+ jhon +
                "&pas=" + et3.getText().toString();
        url = url.replace(" ", "%20");

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {




                if(response.trim().equals("exito")){
                    progreso.hide();
                    Intent i = new Intent(getApplication(), agendarcita.class);
                    startActivity(i);

                    losentimos();

                }else{
                    progreso.hide();
                    felicidades();
                    Intent i = new Intent(getApplication(), condiciones.class);
                    i.putExtra("ESPECIALIDAD", et1.getText().toString());
                    i.putExtra("FECHA", et3.getText().toString());
                    i.putExtra("HORARIO", et4.getText().toString());
                    i.putExtra("DOCTOR", et2.getText().toString());
                    startActivity(i);


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar" + error,Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            Intent intent = new Intent(getApplication(), agendarcita
                    .class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void losentimos(){
        LayoutInflater inflater= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View customToast=inflater.inflate(R.layout.toas_personalizado,null);
        TextView txt= (TextView)customToast.findViewById(R.id.txttoas);
        txt.setText("Lo sentimos, Esta cita ya fue agendada Anteriormente ");
        Toast toast =new Toast(this);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(customToast);
        toast.show();
    }

    public void felicidades(){
        LayoutInflater inflater= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View customToast=inflater.inflate(R.layout.toas_personalizado,null);
        TextView txt= (TextView)customToast.findViewById(R.id.txttoas);
        txt.setText("Felicidades, Cita Disponible ");
        Toast toast =new Toast(this);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(customToast);
        toast.show();
    }


}
