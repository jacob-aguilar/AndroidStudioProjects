package com.example.probook.piensarapido;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class PistasActivity extends AppCompatActivity {
    private ListView items2;//
    private AdaptadorPistas adaptador;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pistas);
        items2 = (ListView) findViewById(R.id.lista_pistas);//Inseguridad//
        adaptador = new AdaptadorPistas(this, GetArrayItems());
        items2.setAdapter(adaptador);//
    }

     private ArrayList<Pistas> GetArrayItems(){//
    ArrayList<Pistas>  listItems = new ArrayList<>();
         listItems.add(new Pistas(R.mipmap.calendario, "Nivel 1", "Todos los meses tienen 28 dias"));
         listItems.add(new Pistas(R.mipmap.pregunta2, "Nivel 2", "Imagina al reves el numero"));
         listItems.add(new Pistas(R.mipmap.pregunta3, "Nivel 3", "Suma y luego resta"));
         listItems.add(new Pistas(R.mipmap.pregunta4, "Nivel 4", "Haz la operacion"));
         listItems.add(new Pistas(R.mipmap.pregunta5, "Nivel 5", "Los corazones valen 5"));
         listItems.add(new Pistas(R.mipmap.pregunta6, "Nivel 6", "Solo suma los cubos"));
         listItems.add(new Pistas(R.mipmap.pregunta7, "Nivel 7", "En todas las lineas esta la respuesta"));
         listItems.add(new Pistas(R.mipmap.pregunta8, "Nivel 8", "Primero haz la division"));
         listItems.add(new Pistas(R.mipmap.pregunta9, "Nivel 9", "No sumes los rectangulos"));
         listItems.add(new Pistas(R.mipmap.pregunta10, "Nivel 10", "null"));
         listItems.add(new Pistas(R.mipmap.pregunta11, "Nivel 11", "Solamente suma"));
         listItems.add(new Pistas(R.mipmap.pregunta12, "Nivel 12", "Toma en cuenta los grandes, medianos y pequeños"));
         listItems.add(new Pistas(R.mipmap.pregunta13, "Nivel 13", "El pentagono no cuenta"));
         listItems.add(new Pistas(R.mipmap.pregunta14, "Nivel 14", "La usas cuando tienes frio"));
         listItems.add(new Pistas(R.mipmap.pregunta15, "Nivel 15", "Primero haz la suma"));
         listItems.add(new Pistas(R.mipmap.pregunta16, "Nivel 16", "No cuentes el cero"));
         listItems.add(new Pistas(R.mipmap.pregunta17, "Nivel 17", "null"));
         listItems.add(new Pistas(R.mipmap.pregunta18, "Nivel 18", "El cuadrado vale 2"));
         listItems.add(new Pistas(R.mipmap.pregunta19, "Nivel 19", "En la imagen se encuentra un mono"));
         listItems.add(new Pistas(R.mipmap.pregunta20, "Nivel 20", "Cuenta los grandes, medianos y pequeños"));

         return  listItems;
    }//

}
