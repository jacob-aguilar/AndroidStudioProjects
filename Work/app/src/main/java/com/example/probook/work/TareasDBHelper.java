package com.example.probook.work;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TareasDBHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String BASE_DATOS = "basedatos.db";
    public static final String TABLA_TAREAS = "tareas";
    public static final String TAREAS_ID = "id";
    public static final String TAREAS_TITULO = "titulo";
    public static final String TAREAS_DESCRIPCION ="descripcion";
    public static final String TAREAS_FECHA = "fecha";
    public static final String TAREAS_HORA = "hora";
    public static final String TAREAS_ICONO = "icono";

    private static final String CREATE_TAREAS =
            "create table "+TABLA_TAREAS+" ("+
            TAREAS_ID+" integer primary key autoincrement,"+
            TAREAS_TITULO+" text not null, "+
            TAREAS_DESCRIPCION+" text not null, "+
            TAREAS_FECHA+" text not null, "+
            TAREAS_HORA+" text not null, "+
            TAREAS_ICONO+" integer not null)";

    private static final String DROP_TAREAS = "drop table " + TABLA_TAREAS;
    private Context contexto;
    public TareasDBHelper(Context context) {
        super(context, BASE_DATOS, null, VERSION);
        contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TAREAS);

        Tarea t = new Tarea();
        t.setTitulo("Hacer Tarea");
        t.setDescripcion("Entregaré mi avance del proyecto de lenguaje III para no quedarme sin nota");
        t.setFecha("19/11/2018");
        t.setHora("23:59");
        t.setIcono(R.drawable.tarea_roja);

        sqLiteDatabase.execSQL("insert into tareas(titulo, descripcion, fecha, hora, icono)"+
                String.format("values ('%s', '%s', '%s', '%s', '%d')",
                        t.getTitulo(),
                        t.getDescripcion(),
                        t.getFecha(),
                        t.getHora(),
                        t.getIcono())
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TAREAS);
        onCreate(sqLiteDatabase);
    }
}
