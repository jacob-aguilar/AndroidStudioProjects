package com.example.user.agenda3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ContactoActivity extends AppCompatActivity {
    private EditText nombres;
    private EditText apellidos;
    private EditText telefono;
    private EditText correo;
    private EditText fecha;
    private long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        nombres = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellido);
        telefono = findViewById(R.id.telefono);
        correo = findViewById(R.id.correo);
        fecha = findViewById(R.id.fecha);

        Intent intent = getIntent();
        id = intent.getLongExtra("id", -1);
        cargarDatos();

    }

    private void cargarDatos(){
        OperacionesContacto cmd = new OperacionesContacto(this);
        Contacto c = cmd.seleccion(id);
        if(c != null) {
            nombres.setText(c.getNombre());
            apellidos.setText(c.getApellido());
            telefono.setText(c.getTelefono());
            correo.setText(c.getCorreo());
            fecha.setText(c.getFecha());
        }
    }
    private void guardarDatos(){
        Contacto c = new Contacto();
        c.setNombre(nombres.getText().toString());
        c.setApellido(apellidos.getText().toString());
        c.setTelefono(telefono.getText().toString());
        c.setCorreo(correo.getText().toString());
        c.setFecha(fecha.getText().toString());
        c.setId(id);

        OperacionesContacto cmd = new OperacionesContacto(this);
        if(id == -1){
            id = cmd.insertar(c);
        }else{
            cmd.actualizar(c);
        }
    }

    public void registrar(View view) {
        guardarDatos();
        Intent intent = new Intent();
        intent.putExtra("id", id);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void cancelar(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
