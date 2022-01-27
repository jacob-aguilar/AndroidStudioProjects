package com.example.personal.login_webservice;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class agendarcita extends AppCompatActivity {
    CardView traumot,cardiol,pediat,ginec,obstre,cirug,medic;
    TextView espec,card;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_agendarcita);



            espec= (TextView) findViewById(R.id.txt_espe_traumatologia);
            card= (TextView) findViewById(R.id.txt_area_cardiologia);
            //envio de datos de las especialidades
            final String car="Cardiología";
            final String pdt="Pediatría";
            final String gcl="Ginecología";
            final String obt="Obstreticia";
            final String cir="Cirugía Gen.";
            final String med="Medicina Gener.";

        // envio de datos de los diferentes doctores
        final String doc_tra="Fernando-Her.";
        final String doc_car="Carlos-Vin.";
        final String doc_pdt="Dr.Espinoza";
        final String doc_gcl="Cristhina-Val.";
        final String doc_cir="Leopaldo-Val.";


        //------------------------------------------------
            traumot= (CardView) findViewById(R.id.traumatologia);
            cardiol= (CardView) findViewById(R.id.cardiologia);
            pediat=  (CardView) findViewById(R.id.pediatra);
            ginec=   (CardView) findViewById(R.id.ginecologia);
            obstre=  (CardView) findViewById(R.id.obstetricia);
            cirug=   (CardView) findViewById(R.id.cirugia);
            medic=   (CardView) findViewById(R.id.medicinageneral);


            traumot.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(),horarios_traumotologia.class);
                    i.putExtra("uno",espec.getText().toString());
                    i.putExtra("dos",doc_tra);
                    startActivity(i);


                }
            });
        cardiol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(),horarios_traumotologia.class);
                i.putExtra("uno",car);
                i.putExtra("dos",doc_car);
                startActivity(i);
            }
        });

        pediat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(),horarios_traumotologia.class);
                i.putExtra("uno",pdt);
                i.putExtra("dos",doc_pdt);
                startActivity(i);
            }
        });
        ginec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(),horarios_traumotologia.class);
                i.putExtra("uno",gcl);
                i.putExtra("dos",doc_gcl);
                startActivity(i);
            }
        });
        obstre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(),horarios_traumotologia.class);
                i.putExtra("uno",obt);
                i.putExtra("dos",doc_gcl);

                startActivity(i);
            }
        });
        cirug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(),horarios_traumotologia.class);
                i.putExtra("uno",cir);
                i.putExtra("dos",doc_cir);
                startActivity(i);
            }
        });
        medic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(),horarios_cardiologia.class);
                i.putExtra("uno",med);
                i.putExtra("dos",doc_cir);
                startActivity(i);
            }
        });




    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            Intent intent = new Intent(getApplication(), inicio.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
