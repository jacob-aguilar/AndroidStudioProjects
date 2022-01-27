package com.example.probook.calculadora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText primerNumero;
    private EditText segundoNumero;
    private TextView salida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        primerNumero = findViewById(R.id.primer_numero);
        segundoNumero = findViewById(R.id.segundo_numero);
        salida = findViewById(R.id.salida);
    }
    public void calcular(View v){
        Button b = (Button)v;
        double n1 = Double.valueOf(primerNumero.getText().toString());
        double n2 = Double.valueOf(segundoNumero.getText().toString());

        if(b.getText().toString().equals("+"))
            salida.setText("La suma es: "+ (n1+n2));

        if(b.getText().toString().equals("-"))
            salida.setText("La resta es: "+ (n1-n2));

        if(b.getText().toString().toLowerCase().equals("x"))
            salida.setText("El producto es: "+ (n1*n2));

        if(b.getText().toString().equals("/"))
            salida.setText("La división es: "+ (n1/n2));

    }
}

