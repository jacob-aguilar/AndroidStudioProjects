package hn.unah.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class RegistroActivity extends AppCompatActivity {

    private TextView salida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Recuperar informacion extra de una actividad
        Intent intent = getIntent();
        String n = intent.getStringExtra("nombre");
        int e = intent.getIntExtra("edad", 0);

        /*String n = getIntent().getStringExtra("nombre");
        int e = getIntent().getIntExtra("edad", 0);*/

        salida = findViewById(R.id.mensaje);
        salida.setText(String.format("Bienvenido\nNombre:%s\nEdad:%d\n", n, e));
    }
}
