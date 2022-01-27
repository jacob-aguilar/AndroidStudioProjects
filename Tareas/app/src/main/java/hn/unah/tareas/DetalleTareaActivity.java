package hn.unah.tareas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DetalleTareaActivity extends AppCompatActivity {

    private long id;
    private EditText titulo;
    private EditText descripcion;
    private EditText hora;
    private EditText fecha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tarea);

        Intent i = getIntent();
        id = i.getLongExtra("id", -1);
        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.descripcion);
        hora = findViewById(R.id.hora);
        fecha = findViewById(R.id.fecha);

        if(id >= 0) {
            cargarDatos();
        }
    }

    private void cargarDatos(){
        OperacionesTarea cmd = new OperacionesTarea(this);
        Tarea t = cmd.selectTarea(id);
        titulo.setText(t.getTitulo());
        descripcion.setText(t.getDescripcion());
        hora.setText(t.getHora());
        fecha.setText(t.getFecha());
    }

    public void guardar(View view){
        OperacionesTarea cmd = new OperacionesTarea(this);

        Tarea t = new Tarea();
        t.setTitulo(titulo.getText().toString());
        t.setDescripcion(descripcion.getText().toString());
        t.setFecha(fecha.getText().toString());
        t.setHora(fecha.getText().toString());
        t.setId(id);

        if(id == -1)
            cmd.insertTarea(t);
        else
            cmd.updateTarea(t);

        setResult(RESULT_OK);
        finish();
    }

    public void cancelar(View view){
        setResult(RESULT_CANCELED);
        finish();
    }
}
