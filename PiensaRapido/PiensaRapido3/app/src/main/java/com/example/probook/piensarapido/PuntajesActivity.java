package com.example.probook.piensarapido;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PuntajesActivity extends AppCompatActivity {

    private static final int REGISTRAR = 1;
    private static final int MODIFICAR = 2;
    private ListView lista;
    private TareasAdapter adapter;
    private OperacionesTarea cmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntajes);
        setContentView(R.layout.activity_main);
        lista = findViewById(R.id.lista_tareas);

        cmd = new OperacionesTarea(this);

        List<Tarea> l = cmd.selectTareas();

        adapter = new TareasAdapter(this, l);


        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tarea t = adapter.getItem(i);
                Intent m = new Intent(PuntajesActivity.this, DetalleTareaActivity.class);
                m.putExtra("id", t.getId());
                startActivityForResult(m, MODIFICAR);
            }
        });

        registerForContextMenu(lista);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_nuevo:
                Intent n = new Intent(this, DetalleTareaActivity.class);
                n.putExtra("id", -1L);
                startActivityForResult(n, REGISTRAR);
                return true;
            case R.id.menu_configuar:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode==RESULT_OK && (requestCode == MODIFICAR || requestCode == REGISTRAR) ){
            recargar();
        }
    }

    private void recargar(){
        cmd = new OperacionesTarea(this);
        List<Tarea> l = cmd.selectTareas();
        adapter.clear();
        adapter.addAll(l);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_tarea, menu);
        menu.setHeaderTitle("Tareas");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Tarea t;
        OperacionesTarea cmd = new OperacionesTarea(this);

        switch (item.getItemId()){
            case R.id.menu_eliminar:
                t = adapter.getItem(info.position);
                cmd.deleteTarea(t.getId());
                recargar();
                return true;
            case R.id.menu_editar:
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
