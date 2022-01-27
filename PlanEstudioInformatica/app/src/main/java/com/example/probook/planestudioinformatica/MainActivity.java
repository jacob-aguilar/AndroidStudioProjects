package com.example.probook.planestudioinformatica;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private ListView plan;
    private View espera;
    private View panelPlan;
    private final Context self = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        plan =findViewById(R.id.plan_estudio);
        espera = findViewById(R.id.panel_espera);
        panelPlan = findViewById(R.id.panel_plan);
        cargarData();
    }

    private void cargarData(){
        new Tarea().execute();
    }

    private void cargar(final JSONArray json){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AdaptadorClase ac = new AdaptadorClase(self, json);
                plan.setAdapter(ac);

            }
        });
    }

    private void mensaje(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(self, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private JSONArray getClases(){
        try {
            //URL server = new URL("http://api.web-hn.com/informatica.json");
            //URL server = new URL("http://10.0.2.2:9000/informatica.php");

            URL server = new URL("https://api.web-hn.com/informatica.php?periodo=");

            HttpsURLConnection conexion = (HttpsURLConnection) server.openConnection();
            conexion.setRequestProperty("User-Agent", "plan-informatica-1.0");

            if (conexion.getResponseCode() == 200) {

                InputStream cuerpo = conexion.getInputStream();

                BufferedReader r = new BufferedReader(new InputStreamReader(cuerpo, "UTF-8"));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line).append("\n");
                }
                return new JSONArray(total.toString());
            }


        }catch (Exception ex){
            mensaje(ex.getMessage());
        }
        return null;
    }

    class Tarea extends AsyncTask<Object, Object, JSONArray>{
        @Override
        protected JSONArray doInBackground(Object... objects) {
            return  getClases();
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            if(json!= null) {
                cargar(json);
                espera.setVisibility(View.GONE);
                panelPlan.setVisibility(View.VISIBLE);
            }
        }
    }
}
