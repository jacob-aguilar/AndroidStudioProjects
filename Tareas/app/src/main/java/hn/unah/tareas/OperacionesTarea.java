package hn.unah.tareas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class OperacionesTarea {

    private TareasDBHelper helper;
    private Context contexto;
    private SQLiteDatabase db;
    public OperacionesTarea(Context c){
        contexto = c;
        helper = new TareasDBHelper(c);
    }

    private void abrir(){
        db = helper.getWritableDatabase();
    }
    private void cerrar(){
        db.close();
    }

    // Operaciones CRUD
    // Create
    public long insertTarea(Tarea t){
        ContentValues datos = new ContentValues();
        datos.put(TareasDBHelper.TAREAS_TITULO, t.getTitulo());
        datos.put(TareasDBHelper.TAREAS_DESCRIPCION, t.getDescripcion());
        datos.put(TareasDBHelper.TAREAS_FECHA, t.getFecha());
        datos.put(TareasDBHelper.TAREAS_HORA, t.getHora());
        datos.put(TareasDBHelper.TAREAS_ICONO, t.getIcono());
        abrir();
        long id = db.insert(TareasDBHelper.TABLA_TAREAS, null, datos);
        cerrar();
        return id;
    }

    //Read varios registros
    public List<Tarea> selectTareas(){
        List<Tarea> lista = new ArrayList<>();

        abrir();
        // Cursor c = db.rawQuery("Select * from "+TareasDBHelper.TABLA_TAREAS, null);
        Cursor c = db.query(TareasDBHelper.TABLA_TAREAS, new String[]{
                TareasDBHelper.TAREAS_ID,
                TareasDBHelper.TAREAS_TITULO,
                TareasDBHelper.TAREAS_DESCRIPCION,
                TareasDBHelper.TAREAS_FECHA,
                TareasDBHelper.TAREAS_HORA,
                TareasDBHelper.TAREAS_ICONO
        }, null, null, null, null, null);

        if(c.moveToFirst() == false){
            cerrar();
            return lista;
        }

        do{
            Tarea t = new Tarea();
            t.setTitulo(c.getString(c.getColumnIndex(TareasDBHelper.TAREAS_TITULO)));
            t.setDescripcion(c.getString(c.getColumnIndex(TareasDBHelper.TAREAS_DESCRIPCION)));
            t.setFecha(c.getString(c.getColumnIndex(TareasDBHelper.TAREAS_FECHA)));
            t.setHora(c.getString(c.getColumnIndex(TareasDBHelper.TAREAS_HORA)));
            t.setIcono(c.getInt(c.getColumnIndex(TareasDBHelper.TAREAS_ICONO)));
            t.setId(c.getLong(c.getColumnIndex(TareasDBHelper.TAREAS_ID)));
            lista.add(t);
        }while (c.moveToNext());
        cerrar();
        return lista;
    }

    // Read un registro
    public Tarea selectTarea(long id){
        //String filtro = "salir";
        //String[] selectArgs = {filtro, filtro};
        //Cursor c = db.rawQuery("Select * from tareas where titulo = ? or descripcion = ?", selectArgs);

        String[] selectArgs = {String.valueOf(id)};
        abrir();
        Cursor c = db.rawQuery("Select * from "+TareasDBHelper.TABLA_TAREAS + " where id = ?", selectArgs);

        if(c.moveToFirst() == false){
            cerrar();
            return null;
        }

        Tarea t = new Tarea();
        t.setTitulo(c.getString(c.getColumnIndex(TareasDBHelper.TAREAS_TITULO)));
        t.setDescripcion(c.getString(c.getColumnIndex(TareasDBHelper.TAREAS_DESCRIPCION)));
        t.setFecha(c.getString(c.getColumnIndex(TareasDBHelper.TAREAS_FECHA)));
        t.setHora(c.getString(c.getColumnIndex(TareasDBHelper.TAREAS_HORA)));
        t.setIcono(c.getInt(c.getColumnIndex(TareasDBHelper.TAREAS_ICONO)));
        t.setId(c.getLong(c.getColumnIndex(TareasDBHelper.TAREAS_ID)));
        cerrar();
        return t;
    }
    // update
    public void updateTarea(Tarea t){
        ContentValues datos = new ContentValues();
        datos.put(TareasDBHelper.TAREAS_TITULO, t.getTitulo());
        datos.put(TareasDBHelper.TAREAS_DESCRIPCION, t.getDescripcion());
        datos.put(TareasDBHelper.TAREAS_FECHA, t.getFecha());
        datos.put(TareasDBHelper.TAREAS_HORA, t.getHora());
        datos.put(TareasDBHelper.TAREAS_ICONO, t.getIcono());

        String[] whereArgs = {String.valueOf(t.getId())};
        abrir();
        db.update(TareasDBHelper.TABLA_TAREAS, datos, "id = ? ", whereArgs);
        cerrar();
    }

    //delete
    public void deleteTarea(long id){
        String[] whereArgs = {String.valueOf(id)};
        abrir();
        db.delete(TareasDBHelper.TABLA_TAREAS, "id = ?",  whereArgs);
        cerrar();
    }


}
