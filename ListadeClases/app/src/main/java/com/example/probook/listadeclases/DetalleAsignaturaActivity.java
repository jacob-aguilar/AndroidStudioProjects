package com.example.probook.listadeclases;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetalleAsignaturaActivity extends AppCompatActivity {

    private TextView codigo;
    private TextView descripcion;
    private TextView uv;
    private TextView requisito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_asignatura);

        codigo = findViewById(R.id.codigo);
        descripcion = findViewById(R.id.descripcion);
        uv = findViewById(R.id.unidades);
        requisito = findViewById(R.id.requisito);

        Intent intent = getIntent();

        codigo.setText(intent.getStringExtra("codigo"));
        descripcion.setText(intent.getStringExtra("descripcion"));
        uv.setText(""+intent.getIntExtra("uv", 3));
        requisito.setText(intent.getStringExtra("requisito"));
    }
}
