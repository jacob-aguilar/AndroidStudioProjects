package com.example.probook.iiexamen;

import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetalleArticuloActivity extends AppCompatActivity {

    private TextView nombre;
    private TextView descripcion;
    private TextView precio;
    private ImageView imagen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_articulo);

        nombre = findViewById(R.id.articulo);
        descripcion = findViewById(R.id.descripcion);
        precio = findViewById(R.id.precio);
        imagen = findViewById(R.id.imagen);

        Intent intent = getIntent();

        nombre.setText(intent.getStringExtra("articulo"));
        descripcion.setText(intent.getStringExtra("descripcion"));
        precio.setText(intent.getStringExtra("precio"));
        imagen.setImageDrawable(getDrawable(0));
    }
}
