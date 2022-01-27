package com.example.sesiamendoza.proyectomiagenda;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragAndDropPermissions;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class Activity_Contenido extends AppCompatActivity {

    EditText fecha;
    private String obtenerFecha;
    private String obtenerHora;
    private int horas, minutos;
    private Spinner categoria_spinner;
    private String dato_spinner;
    EditText hora;
    private EditText titulo;
    private EditText direccion;
    private static final String CERO = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido);

        fecha = findViewById(R.id.fecha);
        hora = findViewById(R.id.hora);
        categoria_spinner = findViewById(R.id.categoría);
        titulo = findViewById(R.id.titulo);
        direccion = findViewById(R.id.direccion);

        categoria_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dato_spinner= (String) categoria_spinner.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fecha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(calendar.MONTH);
                int anio = calendar.get(calendar.YEAR);

                DatePickerDialog dateSetListener = new DatePickerDialog(Activity_Contenido.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fecha.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                    }
                }, anio, mes, dia);

                dateSetListener.show();

            }

        });

        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerHora();
            }
        });
    }

    private void obtenerHora() {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        horas = calendar.get(Calendar.HOUR_OF_DAY);
        minutos = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                String horaFormateada = (hour < 10) ? String.valueOf(CERO + hour) : String.valueOf(hour);
                String minutoFormateado = (min < 10) ? String.valueOf(CERO + min) : String.valueOf(min);
                String AM_PM;
                if (hour < 12) {
                    AM_PM = "a.m";
                } else {
                    AM_PM = "p.m";
                }
                hora.setText(horaFormateada + ":" + minutoFormateado);
            }
        }, horas, minutos, false);
        timePickerDialog.show();

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
                guardar_agenda_DB();
                break;
        }
        return true;
    }

    //metodo guardar
    private void guardar_agenda_DB() {
    Agenda agenda = new Agenda();
    agenda.setTitulo(titulo.getText().toString().trim());
    agenda.setHora(hora.getText().toString().trim());
    agenda.setFecha(fecha.getText().toString().trim());
    agenda.setDireccion(direccion.getText().toString().trim());
    agenda.setCategoria(dato_spinner);

    OperacionesDB operacionesDB = new OperacionesDB(this);
    operacionesDB.insertarAgenda(agenda);

    finish();
    }
}










