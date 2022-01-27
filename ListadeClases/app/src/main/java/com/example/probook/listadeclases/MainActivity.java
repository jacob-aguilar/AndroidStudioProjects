package com.example.probook.listadeclases;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listaAsignaturas;
    private AsignaturaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listaAsignaturas = findViewById(R.id.lista_asignatura);
        ArrayList <Asignatura> asignaturas = new ArrayList<>();

       /* Asignatura as = new Asignatura();
        as.setCodigo("MM-2398");
        as.getDescripcion("Matematicas");
        as.setUv(5);
        as.setRequisito("Ninguno");*/
       //Primer periodo
        asignaturas.add(new Asignatura( "Español","EG-011", 4, "Ninguno"));
        asignaturas.add(new Asignatura( "Filosofia", "FF-0101",4, "Ninguno"));
        asignaturas.add(new Asignatura( "Sociologia","DET-011", 4, "Ninguno"));
        asignaturas.add(new Asignatura( "Metodos Cuantitativos","EQ-011", 4, "Ninguno"));
        asignaturas.add(new Asignatura("Redaccion General", "EQ-011", 4, "Ninguno"));
        asignaturas.add(new Asignatura("Introcuccion", "IA-23", 5, "Ninguno" ));

        adapter = new AsignaturaAdapter(this, asignaturas);
        listaAsignaturas.setAdapter(adapter);

        listaAsignaturas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Asignatura asi = adapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, DetalleAsignaturaActivity.class);
                intent.putExtra("codigo", asi.getCodigo());
                intent.putExtra("descripcion", asi.getDescripcion());
                intent.putExtra("unidades", asi.getUv());
                intent.putExtra("req", asi.getRequisito());
                startActivity(intent);
            }
        });
        listaAsignaturas.setAdapter(adapter);
    }
}
