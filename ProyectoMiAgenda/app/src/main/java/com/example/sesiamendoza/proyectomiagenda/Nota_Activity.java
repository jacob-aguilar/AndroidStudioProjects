package com.example.sesiamendoza.proyectomiagenda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Nota_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_nota,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.guardar_nota:
                guardar_notas_DB();
                break;
        }
        return true;
    }

    private void guardar_notas_DB() {

        Notas notas = new Notas();
        notas.setNota(Notas.getText().toString().trim());


        OperacionesNotasDB operacionesNotasDB = new OperacionesNotasDB(this);
        operacionesNotasDB.insertarNotas(notas);

        finish();
    }
}


