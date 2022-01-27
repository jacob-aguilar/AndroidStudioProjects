package com.example.probook.iiexamen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listaArticulos;
    private ArticuloAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listaArticulos = findViewById(R.id.lista_asignatura);
        ArrayList<Articulos> articulos = new ArrayList<>();

        articulos.add(new Articulos( "Articulo","Microprocesador i7", "$2000", 0));

        adapter = new ArticuloAdapter(this, articulos);
        listaArticulos.setAdapter(adapter);

        listaArticulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Articulos artic = adapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, DetalleArticuloActivity.class);
                intent.putExtra("articulo", artic.getNombre());
                intent.putExtra("descripcion", artic.getDescripcion());
                intent.putExtra("precio", artic.getPrecio());
                startActivity(intent);
            }
        });
        listaArticulos.setAdapter(adapter);
    }
}
