package com.example.user.agenda3;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class OperacionesContacto {
    private AgendaDB conexion;

    public OperacionesContacto(Context context){
        conexion = new AgendaDB(context);
    }

    //Create
    public long insertar(Contacto c){
        ContentValues valores = new ContentValues();

        valores.put(AgendaDB.CONTACTO_NOMBRE, c.getNombre());
        valores.put(AgendaDB.CONTACTO_APELLIDO, c.getApellido());
        valores.put(AgendaDB.CONTACTO_CORREO, c.getCorreo());
        valores.put(AgendaDB.CONTACTO_TELEFONO, c.getTelefono());
        valores.put(AgendaDB.CONTACTO_FECHA, c.getFecha());

        SQLiteDatabase db = conexion.getWritableDatabase();
        long id = db.insert(AgendaDB.TABLA_CONTACTO, null, valores);
        db.close();
        return  id;
    }
    // Read un regsitro
    public Contacto seleccion(long id){
        Contacto c = null;
        SQLiteDatabase db = conexion.getReadableDatabase();

        Cursor cursor = db.query(
                // Tabla o vista
                AgendaDB.TABLA_CONTACTO,
                // Columnas a mostrar
                new String[]{
                        AgendaDB.CONTACTO_ID, AgendaDB.CONTACTO_NOMBRE,
                        AgendaDB.CONTACTO_APELLIDO, AgendaDB.CONTACTO_CORREO,
                        AgendaDB.CONTACTO_TELEFONO, AgendaDB.CONTACTO_FECHA},
                // filtro clusula where
                AgendaDB.CONTACTO_ID+"=?",
                // valores para los parametros de where
                new String[] {String.valueOf(id)},
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            c = new Contacto();
            c.setId(cursor.getLong(cursor.getColumnIndex(AgendaDB.CONTACTO_ID)));
            c.setNombre(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_NOMBRE)));
            c.setApellido(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_APELLIDO)));
            c.setCorreo(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_CORREO)));
            c.setTelefono(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_TELEFONO)));
            c.setFecha(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_FECHA)));
        }
        db.close();
        return c;
    }

    // Read algunos  registros
    public ArrayList<Contacto> seleccion(String filtro){
        SQLiteDatabase db =conexion.getReadableDatabase();
        filtro = "%"+filtro+"%";

        ArrayList<Contacto> datos =new ArrayList<>();
        Cursor cursor =db.query(
                AgendaDB.TABLA_CONTACTO,new String[]{
                        AgendaDB.CONTACTO_ID,AgendaDB.CONTACTO_NOMBRE,
                        AgendaDB.CONTACTO_APELLIDO,AgendaDB.CONTACTO_CORREO,
                        AgendaDB.CONTACTO_TELEFONO,AgendaDB.CONTACTO_FECHA
                },"("+AgendaDB.CONTACTO_NOMBRE+" like ?) or ("+AgendaDB.CONTACTO_APELLIDO+" like ?)",
                new String[]{filtro,filtro},null,null,null
        );
        if(cursor.moveToFirst()){
            do {
                Contacto c = new Contacto();
                c.setId(cursor.getLong(cursor.getColumnIndex(AgendaDB.CONTACTO_ID)));
                c.setNombre(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_NOMBRE)));
                c.setApellido(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_APELLIDO)));
                c.setCorreo(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_CORREO)));
                c.setTelefono(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_TELEFONO)));
                c.setFecha(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_FECHA)));

                datos.add(c);
            }while(cursor.moveToNext());
        }

        db.close();
        return  datos;
    }

    // Read todos los registros
    public ArrayList<Contacto> seleccion(){
        ArrayList<Contacto> datos = new ArrayList<>();
        SQLiteDatabase db = conexion.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from "+AgendaDB.TABLA_CONTACTO, null);

        if(cursor.moveToFirst()){
            do {
                Contacto c = new Contacto();
                c.setId(cursor.getLong(cursor.getColumnIndex(AgendaDB.CONTACTO_ID)));
                c.setNombre(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_NOMBRE)));
                c.setApellido(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_APELLIDO)));
                c.setCorreo(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_CORREO)));
                c.setTelefono(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_TELEFONO)));
                c.setFecha(cursor.getString(cursor.getColumnIndex(AgendaDB.CONTACTO_FECHA)));

                datos.add(c);
            }while(cursor.moveToNext());
        }

        db.close();
        return  datos;
    }
    // upadate
    public void actualizar(Contacto c){
        ContentValues valores = new ContentValues();

        valores.put(AgendaDB.CONTACTO_ID, c.getId());
        valores.put(AgendaDB.CONTACTO_NOMBRE, c.getNombre());
        valores.put(AgendaDB.CONTACTO_APELLIDO, c.getApellido());
        valores.put(AgendaDB.CONTACTO_CORREO, c.getCorreo());
        valores.put(AgendaDB.CONTACTO_TELEFONO, c.getTelefono());
        valores.put(AgendaDB.CONTACTO_FECHA, c.getFecha());

        SQLiteDatabase db = conexion.getWritableDatabase();

        db.update(AgendaDB.TABLA_CONTACTO,
                valores,
                AgendaDB.CONTACTO_ID+"=?",
                new String[]{ String.valueOf(c.getId())});

        db.close();
    }
    //delete
    public void borrar(Contacto c){
        SQLiteDatabase db = conexion.getWritableDatabase();
        db.delete(AgendaDB.TABLA_CONTACTO, AgendaDB.CONTACTO_ID+"=?",
                new String[]{ String.valueOf(c.getId())});
        db.close();
    }
}
