package com.example.sesiamendoza.proyectomiagenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class OperacionesDB {

    private DBHelper conexion;

    DBHelper helper;
    SQLiteDatabase db;
    private String[] columnas = {
            Utlidades_DB.AGENDA_ID,
            Utlidades_DB.AGENDA_TITULO,
            Utlidades_DB.AGENDA_DIRECCION,
            Utlidades_DB.AGENDA_CATEGORIA,
            Utlidades_DB.AGENDA_FECHA,
            Utlidades_DB.AGENDA_HORA,

    };

    public OperacionesDB(Context contexto) {
        helper = new DBHelper(contexto);

    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }


    public void insertarAgenda(Agenda a) {
        ContentValues values = new ContentValues();
        values.put(Utlidades_DB.AGENDA_TITULO, a.getTitulo());
        values.put(Utlidades_DB.AGENDA_CATEGORIA, a.getCategoria());
        values.put(Utlidades_DB.AGENDA_DIRECCION, a.getDireccion());
        values.put(Utlidades_DB.AGENDA_FECHA, a.getFecha());
        values.put(Utlidades_DB.AGENDA_HORA, a.getHora());

        open();
        long id = db.insert(Utlidades_DB.TABLA_AGENDA, null, values);
        close();
        a.setId(id);
    }

    public ArrayList<Agenda> SelectAgenda() {
        open();
        Cursor cur = db.query(Utlidades_DB.TABLA_AGENDA, columnas,
                null, null, null, null, null);
        ArrayList<Agenda> ag = new ArrayList<>();
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                Agenda a = new Agenda();
                a.setId(cur.getLong(cur.getColumnIndex(Utlidades_DB.AGENDA_ID)));
                a.setTitulo(cur.getString(cur.getColumnIndex(Utlidades_DB.AGENDA_TITULO)));
                a.setCategoria(cur.getString(cur.getColumnIndex(Utlidades_DB.AGENDA_CATEGORIA)));
                a.setDireccion(cur.getString(cur.getColumnIndex(Utlidades_DB.AGENDA_DIRECCION)));
                a.setFecha(cur.getString(cur.getColumnIndex(Utlidades_DB.AGENDA_FECHA)));
                a.setHora(cur.getString(cur.getColumnIndex(Utlidades_DB.AGENDA_HORA)));
                ag.add(a);

            }

        }
        close();
        return ag;
    }

    public int updateAgenda(Agenda a){
        ContentValues values = new ContentValues();
        values.put(Utlidades_DB.AGENDA_TITULO, a.getTitulo());
        values.put(Utlidades_DB.AGENDA_CATEGORIA, a.getCategoria());
        values.put(Utlidades_DB.AGENDA_DIRECCION, a.getDireccion());
        values.put(Utlidades_DB.AGENDA_FECHA, a.getFecha());
        values.put(Utlidades_DB.AGENDA_HORA, a.getHora());


        open();
        SQLiteDatabase db = conexion.getWritableDatabase();



        int r = db.update(Utlidades_DB.TABLA_AGENDA,values,
                Utlidades_DB.AGENDA_ID+ "=?",new  String[]{String.valueOf(a.getId())});
        close();
        return r;
    }

    public void deleteAgenda(Agenda a){

        open();

        db.delete(Utlidades_DB.TABLA_AGENDA,
                Utlidades_DB.AGENDA_ID +
                        "="+ a.getId(),null );
        close();
    }
}
