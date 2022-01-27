package com.example.personal.login_webservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class condiciones extends AppCompatActivity {
    TextView espe, fech, hor, doc,cod;
    RequestQueue request;
    Button condic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_condiciones);
        espe= (TextView) findViewById(R.id.especialidas);
        fech= (TextView) findViewById(R.id.fechas);
        hor= (TextView) findViewById(R.id.horarioss);
        doc= (TextView) findViewById(R.id.doctors);
        condic= (Button) findViewById(R.id.cond);


        Bundle bundle = getIntent().getExtras();
        String esp = bundle.getString("ESPECIALIDAD");
        final String fecha = bundle.getString("FECHA");
        String horario = bundle.getString("HORARIO");
        String doct = bundle.getString("DOCTOR");


        espe.setText(esp+" ");
        fech.setText(fecha+ " ");
        request= Volley.newRequestQueue(this);
        hor.setText(horario +" ");
        doc.setText(doct + " ");

        condic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(), agendar_citafin.class);
                i.putExtra("ESPECIALIDAD", espe.getText().toString());
                i.putExtra("FECHA", doc.getText().toString());
                i.putExtra("HORARIO", fech.getText().toString());
                i.putExtra("DOCTOR", hor.getText().toString());


                startActivity(i);

            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            Intent intent = new Intent(getApplication(), agendarcita.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
