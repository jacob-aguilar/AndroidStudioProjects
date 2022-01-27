package hn.unah.unah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class PerfilAlumnoActivity extends AppCompatActivity {

    private TextView alumno;
    private TextView cuenta;
    private TextView carrera;
    private TextView anio;
    private TextView centro;
    private TextView indice_periodo;
    private TextView indice_global;
    private TextView correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_alumno);
        alumno = findViewById(R.id.alumno);
        cuenta= findViewById(R.id.cuenta);
        carrera= findViewById(R.id.carrera);
        anio= findViewById(R.id.anio);
        centro= findViewById(R.id.centro);
        indice_periodo= findViewById(R.id.indice_periodo);
        indice_global= findViewById(R.id.indice_global);
        correo= findViewById(R.id.correo);

        cargarDatos();
    }

    private void cargarDatos(){
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        try{
            JSONObject json = new JSONObject(data);
            alumno.setText("Alumno:\n" + json.getString("nombre"));
            cuenta.setText("No. de Cuenta:\n"+ json.getString("cuenta"));
            carrera.setText("Carrera:\n" + json.getString("carrera"));
            anio.setText("Año:\n" + json.getString("anio"));
            centro.setText("Centro:\n" + json.getString("centro"));
            indice_periodo.setText("Índice del período:\n" + json.getString("indicePeriodo"));
            indice_global.setText("Índice Global:\n" + json.getString("indiceGlobal"));
            correo.setText("Correo Institucional:\n" + json.getString("correoInstitucional"));
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
