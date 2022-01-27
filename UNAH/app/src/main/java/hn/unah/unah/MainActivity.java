package hn.unah.unah;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    private EditText usuario;
    private EditText clave;
    private Context self = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuario = findViewById(R.id.usuario);
        clave = findViewById(R.id.clave);
    }
    protected void ingresar(View view){
        new Tarea().execute();
    }
    private void mensaje(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(self, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
    private String getInformacion(){
        try {

            URL server = new URL("https://api.web-hn.com/unah.php");

            HttpsURLConnection conexion =
                    (HttpsURLConnection) server.openConnection();
            conexion.setRequestProperty("User-Agent", "plan-informatica-1.0");
            //
            conexion.setRequestMethod("POST");

            String params = "usuario="+usuario.getText().toString();
            params += "&clave="+clave.getText().toString();

            conexion.setDoOutput(true);
            conexion.getOutputStream().write(params.getBytes());

            if (conexion.getResponseCode() == 200) {

                InputStream cuerpo = conexion.getInputStream();

                BufferedReader r = new BufferedReader(new InputStreamReader(cuerpo, "UTF-8"));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line).append("\n");
                }
                return total.toString();
            }
        }catch (Exception ex){
            mensaje(ex.getMessage());
        }
        return "";
    }

    private void mostrarDatos(String data){
        Intent intent = new Intent(this, PerfilAlumnoActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    class Tarea extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            return getInformacion();
        }

        @Override
        protected void onPostExecute(String data) {
            try{
                JSONObject json = new JSONObject(data);
                int cod = json.getInt("cod");
                if(cod == 0){
                    mostrarDatos(data);
                }
            }catch (Exception ex){
                mensaje(ex.getMessage());
            }
        }
    }
}
