package com.example.probook.spinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Spinner colores;
    private Spinner frutas;
    private ArrayList<String> itemsFruta;
    private ArrayAdapter<String> adapter;
    private TextView seleccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colores = findViewById(R.id.colores);
        frutas = findViewById(R.id.frutas);
        seleccion = findViewById(R.id.seleccion);

        itemsFruta = new ArrayList<>();
        itemsFruta.add("Manzana");
        itemsFruta.add("Naranja");
        itemsFruta.add("Uva");
        itemsFruta.add("Sandia");
        itemsFruta.add("Melon");
        itemsFruta.add("Banano");

        int recurso = android.R.layout.simple_spinner_dropdown_item;
        adapter = new ArrayAdapter<>(this, recurso, itemsFruta);
        frutas.setAdapter(adapter);

        colores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView txt = (TextView)view;
                seleccion.setText(String.format("Selecciono %d -> %s", i, txt.getText()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });
        frutas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView txt = (TextView)view;
                seleccion.setText(String.format("Selecciono %d -> %s", i, itemsFruta.get(i)));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

    }
}
