package com.example.probook.piensarapido;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;




public class MainActivity extends AppCompatActivity {

    //Variables recientes



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound_long);
        mediaPlayer.start();

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

    public void clickpuntajes(View view) {
        Intent intent = new Intent(this, PuntajesActivity.class);
        startActivity(intent);
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound_long);
        mediaPlayer.stop();
    }*/

    //


}
