package proyectod.permisosunahtec;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.barteksc.pdfviewer.PDFView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.utils.Utilidades;

public class ViewPdf extends AppCompatActivity {

    PDFView pdfView;
    private File file;
    private Object ruta;
    private String i;
    private Cursor cursorList;
    private JSONArray lista;
    private JSONObject json_data;
    private String emailAdmin;
    private String tokenAdministrador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);
        pdfView=(PDFView) findViewById(R.id.pdfView);
        cargarPreferencias();
        getSupportActionBar().setTitle("Hoja de Permiso");
        Bundle bundle = getIntent().getExtras();

invocarTokenFirebaseAdmin();

        if(bundle !=null){
            file =new File(bundle.getString("path",""));
            ruta = bundle.getString("file");
            i= bundle.getString("i");
             Toast.makeText(this, "Archivo guardado en: "+ruta, Toast.LENGTH_LONG).show();
        }
        pdfView.fromFile(file)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .load();




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


                    Log.i("JUSTINOO",""+json_data.getString("email"));

                    if(i==0){
                        emailAdmin =json_data.getString("email");

                    }else {


                        emailAdmin = emailAdmin + ", " + json_data.getString("email");
                    }




            }







        } catch (Exception ex) {

        } finally {
        }
    }
    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

         tokenAdministrador = preferences.getString("token", "");







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {



            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.enviaradjunto, menu);


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.enviarAdjunto:
                String[] mailto = {emailAdmin};
                Uri uri = Uri.fromFile(new File(String.valueOf(file)));
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hoja de Permiso");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Hola PDF se adjunta en este correo. ");
                emailIntent.setType("application/pdf");
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(emailIntent, "Send email using:"));



        }

        return super.onOptionsItemSelected(item);
    }

}
