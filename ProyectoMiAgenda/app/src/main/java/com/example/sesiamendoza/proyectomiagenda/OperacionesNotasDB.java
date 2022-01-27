package com.example.sesiamendoza.proyectomiagenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class OperacionesNotasDB {
    DBHelperNotas helper;
    SQLiteDatabase db;
    private String[] columnas = {
            Notas_DB.NOTAS_ID,
            Notas_DB.NOTAS_ESCRIBIR_NOTAS,


    };

    public OperacionesNotasDB(Context contexto) {
        helper = new DBHelperNotas(contexto);

    }

    public void open() {
        db = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }


    public void insertarNotas(Notas n) {
        ContentValues values = new ContentValues();
        values.put(Notas_DB.NOTAS_ESCRIBIR_NOTAS, n.getNota());

        open();
        long id = db.insert(Notas_DB.TABLA_NOTAS, null, values);
        close();
        n.setId(id);
    }

    public ArrayList<Notas> SelectNotas() {
        open();
        Cursor cur = db.query(Notas_DB.TABLA_NOTAS, columnas,
                null, null, null, null, null);
        ArrayList<Notas> no = new ArrayList<>();
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                Notas n = new Notas();
                n.setId(cur.getLong(cur.getColumnIndex(Notas_DB.NOTAS_ID)));
                n.setNota(cur.getString(cur.getColumnIndex(Notas_DB.NOTAS_ESCRIBIR_NOTAS)));
                no.add(n);

            }

        }
        close();
        return no;
    }

    public int updateNotas(Notas n){
        ContentValues values = new ContentValues();
        values.put(Notas_DB.NOTAS_ESCRIBIR_NOTAS, n.getNota());

        open();
        int s = db.update(Notas_DB.TABLA_NOTAS,values,
                Notas_DB.NOTAS_ID+ "=?",new  String[]{String.valueOf(n.getId())});
        close();
        return s;
    }

    public void deleteNotas(Notas n){
        open();
        db.delete(Notas_DB.TABLA_NOTAS,
                Notas_DB.NOTAS_ID +
                        "="+ n.getId(),null );
        close();
    }
}


