package com.example.personal.login_webservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class
inicio extends AppCompatActivity {
CardView cita,anul,ubic, inf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        cita= (CardView) findViewById(R.id.agendar);
        ubic= (CardView) findViewById(R.id.ubicacion);
        anul= (CardView) findViewById(R.id.card_anularcit);
        inf= (CardView) findViewById(R.id.card_informacion);

        cita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getApplication(), agendarcita.class);
                    startActivity(i);
                }catch (Exception e){
                    Toast.makeText(getApplication(),e.getMessage(),Toast.LENGTH_LONG).show();
                }


            }
        });

        anul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getApplication(), anular_cita.class);
                    startActivity(i);
                }catch (Exception e){
                    Toast.makeText(getApplication(),e.getMessage(),Toast.LENGTH_LONG).show();
                }


            }
        });
        ubic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getApplication(), MapsActivity.class);
                    startActivity(i);
                }catch (Exception e){
                    Toast.makeText(getApplication(),e.getMessage(),Toast.LENGTH_LONG).show();
                }


            }
        });
        inf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getApplication(), informacion.class);
                    startActivity(i);
                }catch (Exception e){
                    Toast.makeText(getApplication(),e.getMessage(),Toast.LENGTH_LONG).show();
                }


            }
        });



    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }








}
