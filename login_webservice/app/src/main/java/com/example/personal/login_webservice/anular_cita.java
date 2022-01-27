package com.example.personal.login_webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class anular_cita extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
TextView esp, doc, fech,hor,idd;
    EditText cod, moti;

    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;
    ProgressDialog pDialog;
    Button anul, motiv;
    ImageButton cons;
    ProgressDialog progreso;
    RequestQueue request;
    TextView adm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anular_cita);
        cod= (EditText) findViewById(R.id.txtcodigo);
        moti= (EditText) findViewById(R.id.anulad);
        esp= (TextView) findViewById(R.id.txtespec);
        doc= (TextView) findViewById(R.id.txtdoctor);
        fech= (TextView) findViewById(R.id.txtfecha);
        hor= (TextView) findViewById(R.id.txthorario);
        request = Volley.newRequestQueue(this);
        adm=(TextView)findViewById(R.id.nombreadmin);


        anul= (Button) findViewById(R.id.btneliminar);
        motiv= (Button) findViewById(R.id.enviar);
        cons= (ImageButton) findViewById(R.id.btnconsultar);
        anul.setEnabled(false);
        motiv.setEnabled(false);
        //act= (Button) findViewByI
        // d(R.id.btneactualizar);
        cod.setSingleLine(false);
        cod.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT);

        cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarWebService();
                motiv.setEnabled(true);

            }
        });
        anul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    if(esp.getText().toString().equals("Traumotología"+" ")) {

                         webServiceEliminar();
                         anular_traumotologia();
                        adm.setText("");


                    }

                    if(esp.getText().toString().equals("Cardiología"+" ")) {

                        webServiceEliminar();
                        anular_cardiologia();
                        adm.setText("");

                    }
                    if(esp.getText().toString().equals("Pediatría"+" ")){

                        webServiceEliminar();
                        anular_pediatra();
                        adm.setText("");

                    }
                    if(esp.getText().toString().equals("Ginecología"+" ")){

                        webServiceEliminar();
                        anular_ginecologia();
                        adm.setText("");
                    }
                    if(esp.getText().toString().equals("Obstreticia"+" ")){

                        webServiceEliminar();
                        anular_obstreticia();
                        adm.setText("");

                    }
                    if(esp.getText().toString().equals("Cirugía Gen."+" ")){

                        webServiceEliminar();
                        anular_cirugiageneral();
                        adm.setText("");

                    }if(esp.getText().toString().equals("Medicina Gener."+" ")){

                        webServiceEliminar();
                        anular_medicinageneral();
                        adm.setText("");

                    }



                }catch (Exception e){
                    Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

        motiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if(moti.getText().toString().equals("")){




                   motivo();

                }else{
                    guardar_motivo();
                    anul.setEnabled(true);

                }
            }
        });

    }

    private void cargarWebService() {


        progreso = new ProgressDialog(this);
        progreso.setMessage("Consultando datos de la cita...");
        progreso.show();




        String url="http://hospitalmanabideldia.com/devolverdatos.php?codigo="+cod.getText().toString();

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Usuario miUsuario=new Usuario();

                JSONArray json=response.optJSONArray("gestion_citas");
                JSONObject jsonObject=null;

                try {
                    jsonObject=json.getJSONObject(0);

                    miUsuario.setEsp(jsonObject.optString("especialidad"));
                    miUsuario.setDoct(jsonObject.optString("doctor"));
                    miUsuario.setFecha(jsonObject.optString("fecha"));
                    miUsuario.setHorario(jsonObject.optString("horario"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
try {
    progreso.hide();
    esp.setText(miUsuario.getEsp());//SE MODIFICA
    doc.setText(miUsuario.getDoct());
    fech.setText(miUsuario.getFecha());
    hor.setText(miUsuario.getHorario());
    //SE MODIFICA
}catch (Exception e){
    Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();

                Log.d("ERROR: ", error.toString());
            }
        });

        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(jsonObjectRequest);
    }


    private void webServiceActualizar() {




        String url="http://192.168.1.7/Prototipo_Clinica/modificar.php?";

        stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.trim().equalsIgnoreCase("actualiza")){
                    // etiNombre.setText("");
                    //  txtDocumento.setText("");
                    //   etiProfesion.setText("");
                    Toast.makeText(getApplication(),"Se ha Actualizado con exito",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplication(),"No se ha Actualizado ",Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String documento=cod.getText().toString();
                String nombre=esp.getText().toString();
                String profesion=doc.getText().toString();
                String fecha=fech.getText().toString();
                String horario=hor.getText().toString();




                Map<String,String> parametros=new HashMap<>();
                parametros.put("codigo",documento);
                parametros.put("especialidad",nombre);
                parametros.put("doctor",profesion);
                parametros.put("fecha",fecha);
                parametros.put("horario",horario);

                return parametros;
            }
        };
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }
    private void webServiceEliminar() {








        String url="http://hospitalmanabideldia.com/anular_cita.php?codigo="+cod.getText().toString();

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.trim().equalsIgnoreCase("elimina")){
                    esp.setText("");
                    doc.setText("");
                    fech.setText("");
                    hor.setText("");
                    Intent i = new Intent(getApplication(), inicio.class);
                    startActivity(i);
                   msjanularcita();

                }else{
                    if (response.trim().equalsIgnoreCase("no Existe")){

                        msjnosepudoanularcita();

                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getApplication(),"No se ha Eliminado su cita  ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }

    private void anular_traumotologia() {





        String url="http://hospitalmanabideldia.com/anularcita_traumotologia.php?codigo="+cod.getText().toString();

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.trim().equalsIgnoreCase("elimina")){



                }else{
                    if (response.trim().equalsIgnoreCase("no Existe")){
                        Toast.makeText(getApplication(),"No exista una cita asignada con ese codigo por favor vuelva a intentar ",Toast.LENGTH_SHORT).show();

                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getApplication(),"No se ha Eliminado su cita  ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }
    private void anular_cardiologia() {





        String url="http://hospitalmanabideldia.com/anularcita_cardiologia.php?codigo="+cod.getText().toString();

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.trim().equalsIgnoreCase("elimina")){



                }else{
                    if (response.trim().equalsIgnoreCase("no Existe")){
                        Toast.makeText(getApplication(),"No exista una cita asignada con ese codigo por favor vuelva a intentar ",Toast.LENGTH_SHORT).show();

                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getApplication(),"No se ha Eliminado su cita  ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }
    private void anular_pediatra() {





        String url="http://hospitalmanabideldia.com/anulacita_pediatra.php?codigo="+cod.getText().toString();

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.trim().equalsIgnoreCase("elimina")){



                }else{
                    if (response.trim().equalsIgnoreCase("no Existe")){
                        Toast.makeText(getApplication(),"No exista una cita asignada con ese codigo por favor vuelva a intentar ",Toast.LENGTH_SHORT).show();

                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getApplication(),"No se ha Eliminado su cita  ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }

    private void anular_ginecologia() {





        String url="http://hospitalmanabideldia.com/anularcita_ginecologia.php?codigo="+cod.getText().toString();

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.trim().equalsIgnoreCase("elimina")){



                }else{
                    if (response.trim().equalsIgnoreCase("no Existe")){
                        Toast.makeText(getApplication(),"No exista una cita asignada con ese codigo por favor vuelva a intentar ",Toast.LENGTH_SHORT).show();

                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getApplication(),"No se ha Eliminado su cita  ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }

    private void anular_obstreticia() {





        String url="http://hospitalmanabideldia.com/anularcita_obstreticia.php?codigo="+cod.getText().toString();

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.trim().equalsIgnoreCase("elimina")){



                }else{
                    if (response.trim().equalsIgnoreCase("no Existe")){
                        Toast.makeText(getApplication(),"No exista una cita asignada con ese codigo por favor vuelva a intentar ",Toast.LENGTH_SHORT).show();

                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getApplication(),"No se ha Eliminado su cita  ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }

    private void anular_cirugiageneral() {





        String url="http://hospitalmanabideldia.com/anulacitar_cirugiageneral.php?codigo="+cod.getText().toString();

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.trim().equalsIgnoreCase("elimina")){



                }else{
                    if (response.trim().equalsIgnoreCase("no Existe")){
                        Toast.makeText(getApplication(),"No exista una cita asignada con ese codigo por favor vuelva a intentar ",Toast.LENGTH_SHORT).show();

                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getApplication(),"No se ha Eliminado su cita  ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }
    private void anular_medicinageneral() {





        String url="http://hospitalmanabideldia.com/anularcita_medicinageneral.php?codigo="+cod.getText().toString();

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.trim().equalsIgnoreCase("elimina")){



                }else{
                    if (response.trim().equalsIgnoreCase("no Existe")){
                        Toast.makeText(getApplication(),"No exista una cita asignada con ese codigo por favor vuelva a intentar ",Toast.LENGTH_SHORT).show();

                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getApplication(),"No se ha Eliminado su cita  ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();

            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getApplication()).addToRequestQueue(stringRequest);
    }

    public void guardar_motivo() {


        try {


            progreso = new ProgressDialog(this);
            progreso.setMessage("Cargando...");
            progreso.show();
            String url="http://hospitalmanabideldia.com/datos_motivos.php?especialidad=" + esp.getText().toString()
                    + "&fecha=" + fech.getText().toString() + "&horario=" + hor.getText().toString() + "&motivo=" + moti.getText().toString();
            url = url.replace(" ", "%20");
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, this, this);
            request.add(jsonObjectRequest);

        } catch (Exception exe) {
            Toast.makeText(this, exe.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getApplicationContext(), "No se pudo registrar :(" + error.toString(), Toast.LENGTH_SHORT).show();
        anul.setEnabled(false);

        Log.i("ERROR", error.toString());


    }

    @Override
    public void onResponse(JSONObject response) {

        ingresemotivo();
        progreso.hide();
        moti.setText("");
    }
    public void msjanularcita(){
        LayoutInflater inflater= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View customToast=inflater.inflate(R.layout.toas_personalizado,null);
        TextView txt= (TextView)customToast.findViewById(R.id.txttoas);
        txt.setText("Cita anulada con Exito");
        Toast toast =new Toast(this);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(customToast);
        toast.show();
    }

    public void msjnosepudoanularcita(){
        LayoutInflater inflater= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View customToast=inflater.inflate(R.layout.toas_personalizado,null);
        TextView txt= (TextView)customToast.findViewById(R.id.txttoas);
        txt.setText("No exista una cita asignada con ese codigo por favor vuelva a intentar ");
        Toast toast =new Toast(this);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(customToast);
        toast.show();
    }
    public void ingresemotivo(){
        LayoutInflater inflater= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View customToast=inflater.inflate(R.layout.toas_personalizado,null);
        TextView txt= (TextView)customToast.findViewById(R.id.txttoas);
        txt.setText("Tu motivo se ha enviado correctamente al administrador, presiona anular cita y el sistema anulara la cita agendada ");
        Toast toast =new Toast(this);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(customToast);
        toast.show();
    }

    public void motivo(){
        LayoutInflater inflater= (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View customToast=inflater.inflate(R.layout.toas_personalizado,null);
        TextView txt= (TextView)customToast.findViewById(R.id.txttoas);
        txt.setText("Por favor, Ingresa un motivo ");
        Toast toast =new Toast(this);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(customToast);
        toast.show();
    }


}
