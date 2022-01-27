package hn.unah.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private EditText nombre;
    private EditText edad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edad = findViewById(R.id.edad);
        nombre = findViewById(R.id.nombre);
    }
    public void ingresar(View view){

        String n = nombre.getText().toString();
        int e = Integer.valueOf(edad.getText().toString());

        Intent intent = new Intent(this, RegistroActivity.class);

        //Permite pasar datos de una actividad a la otra solo esta acepta
        intent.putExtra("nombre", n);
        intent.putExtra("edad", e);

        startActivity(intent);//Minimos paasos para lamzar un item explicito
    }
}
