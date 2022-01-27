package com.example.miltonalfredo.respuestas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ResultadoActivity extends AppCompatActivity {

    private TextView resultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
       resultado = findViewById(R.id.resultado);

       Intent i =getIntent();
       int numero = i.getIntExtra("codigo", 'A');

       if (numero >= 'A' &&numero <= 'Z'){
           resultado.setText("Su nombre inicia con:" + (char)numero);
       }else if (numero >= 'a' && numero <= 'z'){
           resultado.setText("Su nombre inicia con:" + (char)(numero-32));

       }else{
           resultado.setText("Su nombre inicia con: A" );
           }
    }

    public void aceptar(View view ){
        Intent data = new Intent();
        data.putExtra("msg","Gracias por jugar");
        setResult(RESULT_OK, data);
        finish();
    }

    public void cancelar(View view){
        setResult(RESULT_CANCELED);
        finish();


    }
}
