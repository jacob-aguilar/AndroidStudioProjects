package com.example.probook.tareaasincrona;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ProgressBar barraProgreso;
    private TextView salida;
    private Integer contar =1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barraProgreso = findViewById(R.id.barra_progreso);
        salida = findViewById(R.id.salida);
    }

    protected void ejecutar(View view){
        contar =1;
        barraProgreso.setProgress(0);
        new Tarea().execute(10);
    }

    class Tarea extends AsyncTask<Integer, Integer, String>{

        @Override
        protected String doInBackground(Integer... params) {
            for (; contar <= params[0]; contar++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(contar);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Tarea Completada.";
        }

        @Override
        protected void onPostExecute(String result) {
            salida.setText(result);
        }
        @Override
        protected void onPreExecute() {
            salida.setText("Iniciando tarea ...");
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            salida.setText("Ejecutando ... "+ values[0]);
            barraProgreso.setProgress(values[0]);
        }
    }
}
