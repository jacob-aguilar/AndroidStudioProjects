package com.example.user.agenda3;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ListView lista;
    private ContactoAdapter adapter;
    private OperacionesContacto cmd;
    private static final int REGISTRAR = 1;
    private static final int EDITAR = 2;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lista = findViewById(R.id.lista_contactos);
        cmd = new OperacionesContacto(this);
        adapter = new ContactoAdapter(this, cmd.seleccion());
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ContactoActivity.class);
                Contacto c = adapter.getItem(i);
                intent.putExtra("id", c.getId());
                startActivityForResult(intent, EDITAR);
            }
        });
        registerForContextMenu(lista);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item =menu.findItem(R.id.buscar);
        searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.agregar_contacto){
            Intent intent = new Intent(this, ContactoActivity.class);
            startActivityForResult(intent, REGISTRAR);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.contex_lista_contactos, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.eliminar_contato:
                Contacto c = adapter.getItem(menuInfo.position);
                mostrarConfirmacion(c);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void mostrarConfirmacion(final Contacto c){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar");
        builder.setMessage("¿Esta seguro que desea borrar el contacto?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cmd.borrar(c);
                adapter.remove(c);
            }
        });
        builder.setNegativeButton("No", null);
        builder.setCancelable(false);
        AlertDialog dlg = builder.create();
        dlg.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== REGISTRAR && resultCode == RESULT_OK){
            long id = data.getLongExtra("id", -1);
            Contacto c = cmd.seleccion(id);
            adapter.add(c);
        }
        if(requestCode== EDITAR && resultCode == RESULT_OK){
            long id = data.getLongExtra("id", -1);
            Contacto c1 = cmd.seleccion(id);
            Contacto c2;
            for(int i =0; i < adapter.getCount(); i++){
                c2 = adapter.getItem(i);
                if(c1.getId() == c2.getId()){
                    c2.setNombre(c1.getNombre());
                    c2.setApellido(c1.getApellido());
                    c2.setTelefono(c1.getTelefono());
                    c2.setCorreo(c1.getCorreo());
                    c2.setFecha(c1.getFecha());
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    //ANDROIDDHIVE



    private void recargarContactos(){
        adapter.clear();
        adapter.addAll(cmd.seleccion());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        cmd.seleccion(newText);
        adapter= new ContactoAdapter(this, cmd.seleccion(newText));
        lista.setAdapter(adapter);
        return true;
    }
}
