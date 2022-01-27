package com.example.probook.piensarapido;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*HiloRunnable hilo1runable;
    Thread hilo;
    HiloThread hilo2Tread;
    Context context;

    Button jugar;
    Button sonido;*/

    private TextView bestscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bestscore = (TextView)findViewById(R.id.textscore);

        //Bundle bundle = getIntent().getExtras();
        //String dato = bundle.get("Mejor").toString();
        //bestscore.setText(dato);

        /*context = getApplicationContext();

        jugar = (Button)findViewById(R.id.botonjugar);
        jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hilo2Tread == null || !hilo2Tread.isAlive()){
                    hilo2Tread = new HiloThread(context);
                    //hilo = new Thread(hilo1runable);
                    hilo2Tread.setDaemon(true);// los hilos se cerrarar al destroy
                    hilo2Tread.start();
                }else {
                    Toast.makeText(context, "Detenido", Toast.LENGTH_SHORT).show();
                    hilo2Tread.terminar();  // Variable stop a true
                }

            }
        });

        sonido = (Button)findViewById(R.id.botonacercade);
        sonido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hilo == null || !hilo.isAlive()){
                    hilo1runable = new HiloRunnable(context);
                    hilo = new Thread(hilo1runable);
                    hilo.setDaemon(true);// los hilos se cerrarar al destroy
                    hilo.start();
                }else {
                    Toast.makeText(context, "Detenido", Toast.LENGTH_SHORT).show();
                    hilo1runable.terminar();  // Variable stop a true
                }

            }
        });*/

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_homero2);


        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "BaseDeDatos", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        Cursor consulta = BaseDeDatos.rawQuery("select * from puntaje where score = (select max(score) from puntaje)", null);

        if(consulta.moveToFirst()){
            String temp_score = consulta.getString(0);
            bestscore.setText("Mejor Puntuacion: " + temp_score);
            BaseDeDatos.close();
        } else {
            BaseDeDatos.close();
        }
    }

    //Metodo para mostrar y ocultar el menu overflow
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }

    //Metodo para agregar las funciones a mi overFlow
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.configuracion){

            Intent intent = new Intent(this, AcercaDeActivity.class);
            startActivity(intent);

        }
        return  super.onOptionsItemSelected(item);

    }

    public void clickjugar(View view) {
        Intent intent = new Intent(this, JugarActivity.class);
        startActivity(intent);
    }

    public void clickConfigurar(View view) {
        Intent intent = new Intent(this, ConfigurarActivity.class);
        startActivity(intent);
    }

    public void clickniveles(View view) {
        Intent intent = new Intent(this, NivelesActivity.class);
        startActivity(intent);
    }


    /*@Override
    protected void onResume() {
        super.onResume();
        if(hilo1runable!= null){
            hilo1runable.renaudar();
        }
        if(hilo2Tread!= null){
            hilo2Tread.renaudar();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(hilo1runable!= null){
            hilo1runable.pausar();
        }
        if(hilo2Tread!= null){
            hilo2Tread.pausar();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(hilo1runable!= null){
            hilo1runable.terminar();
        }
        if(hilo2Tread!= null){
            hilo2Tread.terminar();
        }
        super.onDestroy();
    }*/

}
