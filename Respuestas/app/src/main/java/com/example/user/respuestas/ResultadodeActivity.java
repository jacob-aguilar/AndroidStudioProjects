package com.example.user.respuestas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ResultadodeActivity extends AppCompatActivity {
    private EditText resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultadode);
        resultado = findViewById(R.id.resultado);

        Intent i = getIntent();
        int numero = i.getIntExtra("codigo", 'A');

        if (numero >= 'A' && numero <= 'Z'){
        resultado.setText("Su nombre inicia con: " + (char)numero);
        } else if(numero >= 'a' && numero <= 'z'){
            resultado.setText("Su nombre inicia con:" + (char)(numero-32);
        }else{
        resultado.setText("Su nombre inicia con A");}
        }
    public void aceptar(View view){
    Intent data = new Intent();
    data.putExtra("msg", "");
    //data.putExtra("msg", "");
        setResult(RESULT_OK, data);
    }
}

