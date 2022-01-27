package com.example.probook.piensarapido;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class JugarActivity extends AppCompatActivity {

    private TextView puntos, contador, vidas, pregunta, correcto, incorrecto;
    private ImageView imagen;
    private EditText txtedit;
    private Button btnconfirmar;


   // String[] preguntas_imagenes = {"¿Cuantos meses tienen 28 dias", "Numero que al reves vale menos"};
    String[] respuesta_pregunta = {"doce", "nueve", "129", "igual", "70", "9", "7",
           "9", "11", "null", "4100", "20", "13", "Bufanda", "15", "7", "null", "null", "null", "null"};

    String[] respuesta_preguntas = {"12", "9", "129", "iguales", "setenta", "nueve", "siete",
            "nueve", "once", "null", "cuatro mil cien", "veinte", "trece", "bufanda", "quince",
            "siete", "null", "null", "null", "null"};

    String[] imagen_preguntas = {"calendario","pregunta2", "pregunta3", "pregunta4", "pregunta5", "pregunta6", "pregunta7",
            "pregunta8", "pregunta9", "pregunta10", "pregunta11", "pregunta12", "pregunta13", "pregunta14", "pregunta15",
            "pregunta16", "pregunta17", "pregunta18", "pregunta19", "pregunta20"};

    int puntaje = 0;
    int intentos = 3;
    int imagengenerada = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

        puntos = (TextView)findViewById(R.id.textPuntos);
        contador = (TextView)findViewById(R.id.textConteo);
        vidas = (TextView)findViewById(R.id.textIntentos);
        pregunta = (TextView)findViewById(R.id.pregunta1);
        imagen = (ImageView)findViewById(R.id.image1);
        txtedit = (EditText) findViewById(R.id.respuesta);
        btnconfirmar = (Button)findViewById(R.id.botonconfirmar);

        establecer_imagen(imagengenerada);

        correcto = (TextView)findViewById(R.id.textocorrecto);
        incorrecto = (TextView)findViewById(R.id.textoincorrecto);

        //Seleccionar el texto que esta dentro del editText
        //toLowerCase pondra el texto que ponga, en minuscula

        btnconfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String textorespuesta = txtedit.getText().toString().toLowerCase();

                if (textorespuesta.equals(respuesta_pregunta[imagengenerada])| textorespuesta.equals(respuesta_preguntas[imagengenerada])){

                correcto.setVisibility(View.VISIBLE);
                puntaje = puntaje+1;
                puntos.setText("Puntos: "+ puntaje);
                esperar1();
                }else{
                    incorrecto.setVisibility(View.VISIBLE);
                    intentos = intentos-1;
                    vidas.setText("Intentos: "+ intentos);
                }
            }
        });
    }

    //Metodo para establecer la imagen
    void establecer_imagen(int numero){
        int id = getResources().getIdentifier(imagen_preguntas[numero], "mipmap", getPackageName());
        imagen.setImageResource(id);
    }

    //Metodo para la cuenta regresiva
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
                correcto.setVisibility(View.INVISIBLE);
                txtedit.setText("");
                txtedit.setHint("Ingrese su respuesta correcta");
            }
        }.start();
    }

   /* void esperar2(){
        new CountDownTimer(2000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnconfirmar.setVisibility(View.GONE);

            }

            @Override
            public void onFinish() {
                btnconfirmar.setVisibility(View.VISIBLE);
                incorrecto.setVisibility(View.INVISIBLE);
                txtedit.setText("");
                txtedit.setHint("Ingrese su respuesta correcta");

            }
        }.start();//Se iniciara el proceso
    }*/

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

    public void clickconfirmar(View view) {
    }
}
