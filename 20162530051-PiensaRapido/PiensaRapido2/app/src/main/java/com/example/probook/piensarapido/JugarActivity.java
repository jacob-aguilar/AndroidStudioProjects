package com.example.probook.piensarapido;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class JugarActivity extends AppCompatActivity{


    private TextView puntos, contador, vidas, pregunta;
    private ImageView imagen;
    private EditText txtedit;
    private Button btnconfirmar;
    MediaPlayer mediaPlayer, bueno, malo, feo;

    String[] respuesta_pregunta = {"R", "nueve", "129", "igual", "70", "9", "7",
           "9", "11", "doce", "4100", "20", "13", "Bufanda", "15", "7", "null", "1", "6", "40"};

    String[] respuesta_preguntas = {"r", "9", "129", "iguales", "setenta", "nueve", "siete",
            "nueve", "once", "12", "cuatro mil cien", "veinte", "trece", "bufanda", "quince",
            "siete", "null", "uno", "seis", "cuarenta"};

    String[] imagen_preguntas = {"pregunta10","pregunta2", "pregunta3", "pregunta4", "pregunta5", "pregunta6", "pregunta7",
            "pregunta8", "pregunta9", "calendario", "pregunta11", "pregunta12", "pregunta13", "pregunta14", "pregunta15",
            "pregunta16", "pregunta17", "pregunta18", "pregunta19", "pregunta20"};

   // int[] imagenes = {R.drawable.calendario};

    int puntaje = 0;
    int intentos = 3;
    int imagengenerada = 0;

    // int preguntasimagenes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

        puntos = (TextView)findViewById(R.id.textPuntos);
        contador = (TextView)findViewById(R.id.textConteo);
        vidas = (TextView)findViewById(R.id.textIntentos);
        //pregunta = (TextView)findViewById(R.id.pregunta1);
        imagen = (ImageView)findViewById(R.id.image1);
        txtedit = (EditText) findViewById(R.id.respuesta);
        btnconfirmar = (Button)findViewById(R.id.botonconfirmar);

        mediaPlayer = MediaPlayer.create(this, R.raw.piano);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        malo = MediaPlayer.create(this, R.raw.ouch);
        bueno = MediaPlayer.create(this, R.raw.umm);
        feo = MediaPlayer.create(this, R.raw.feo);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_homero2);

        //establecer_imagen(imagengenerada);


          //toLowerCase pondra el texto que ponga, en minuscula
        /*btnconfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String textorespuesta = txtedit.getText().toString().toLowerCase();

                if (textorespuesta.equals(respuesta_pregunta[imagengenerada])| textorespuesta.equals(respuesta_preguntas[imagengenerada])){
                    Toast.makeText(JugarActivity.this, "Respuesta Correcta", Toast.LENGTH_SHORT).show();
                    puntaje = puntaje+1;
                    puntos.setText("Puntos: "+ puntaje);
                    bueno.start();

                esperar1();
                }

                else{
                    Toast.makeText(JugarActivity.this, "Respuesta incorrecta, vuelve a intentarlo", Toast.LENGTH_LONG).show();
                    intentos = intentos-1;
                    vidas.setText("Intentos: "+ intentos);
                    txtedit.setText("");
                    malo.start();
                }
            }
        });*/
    }

    public void clickconfirmar(View view) {
        final String textorespuesta = txtedit.getText().toString().toLowerCase();

        if(!textorespuesta.equals("")){

            if (textorespuesta.equals(respuesta_pregunta[imagengenerada])| textorespuesta.equals(respuesta_preguntas[imagengenerada])){
                Toast.makeText(JugarActivity.this, "Respuesta Correcta", Toast.LENGTH_SHORT).show();
                puntaje = puntaje+1;
                puntos.setText("Puntos: "+ puntaje);
                bueno.start();

                esperar1();
            }

            else{
                //Toast.makeText(JugarActivity.this, "Respuesta incorrecta, vuelve a intentarlo", Toast.LENGTH_LONG).show();
                intentos = intentos-1;
                vidas.setText("Intentos: "+ intentos);
                txtedit.setText("");
                malo.start();

                switch (intentos){
                    /*case 3:
                        Toast.makeText(this, "Te quedan 3 intentos", Toast.LENGTH_LONG).show();
                        break;*/
                    case 2:
                        Toast.makeText(this, "Te quedan 2 intentos", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(this, "Te quedan 1 intentos", Toast.LENGTH_LONG).show();
                        break;
                    case 0:
                        Toast.makeText(this, "Fin de la partida", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(this, MainActivity.class);
                        //startActivity(intent);

                        //Intent intentar = new Intent(this, MainActivity.class);
                        intent.putExtra("Mejor:", puntos.getText().toString());
                        startActivity(intent);
                        finish();

                        mediaPlayer.stop();
                        feo.start();
                        mediaPlayer.release();
                        break;
                }
            }

        }else {
            Toast.makeText(this, "Escribe tu respuesta", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
            public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(JugarActivity.this);
        builder.setMessage("¿Desea abandonar esta partida?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogo, int which) {
                finish();
                mediaPlayer.stop();

            }
        });
        AlertDialog alertadialogo = builder.create();
        alertadialogo.show();
    }

    //Metodo para establecer la imagen
    void establecer_imagen(int numero){
       int id = getResources().getIdentifier(imagen_preguntas[numero], "mipmap", getPackageName());
        //imagen.setImageResource(imagenes[numero]);
        imagen.setImageResource(id);
    }

    void esperar1(){
        new CountDownTimer(4000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                contador.setText(""+(millisUntilFinished/1000));
                btnconfirmar.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                btnconfirmar.setVisibility(View.VISIBLE);
                imagengenerada = imagengenerada+1;// Va a la siguiente imagen
                contador.setText("");

                establecer_imagen(imagengenerada);
                txtedit.setText("");
                txtedit.setHint("Ingrese su respuesta correcta");
            }
        }.start();
    }


    //Metodo para mostrar y ocultar el menu overflow
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow2, menu);
        return true;
    }

    //Metodo para agregar las funciones a mi overFlow
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.idea){

            Intent intent = new Intent(this, PistasActivity.class);
            startActivity(intent);
             }
        return  super.onOptionsItemSelected(item);
    }
}
