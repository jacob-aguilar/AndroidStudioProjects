package com.example.user.agenda3;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AgendaDB extends SQLiteOpenHelper {
    private final static String BASE_DATOS = "agenda.db";
    private final static int VERSION = 1;

    //CAMPOS
    public final static String TABLA_CONTACTO = "contacto";
    public final static String CONTACTO_ID = "id";
    public final static String CONTACTO_NOMBRE = "nombre";
    public final static String CONTACTO_APELLIDO = "apellido";
    public final static String CONTACTO_TELEFONO = "telefono";
    public final static String CONTACTO_CORREO = "correo";
    public final static String CONTACTO_FECHA = "fecha";

    /*public static class CONTACTO{
        public final static String TABLE_NAME ="contacto";
        public final static String ID = "id";
        public final static String NOMBRE = "nombre";
        public final static String APELLIDO = "apellido";
        public final static String TELEFONO = "telefono";
        public final static String CORREO = "correo";
        public final static String FECHA = "fecha";
    }*/

    private final static String CREATE_CONTACTO =
            "create table " + TABLA_CONTACTO + " ( "+
                    CONTACTO_ID +  " integer primary key autoincrement, "+
                    CONTACTO_NOMBRE+ " text not null, "+
                    CONTACTO_APELLIDO+" text not null, "+
                    CONTACTO_TELEFONO+ " text, "+
                    CONTACTO_CORREO +" text, "+
                    CONTACTO_FECHA+ " text)";

    private final static String DROP_CONTATO =
            "drop table " + TABLA_CONTACTO;


    public AgendaDB(Context context) {
        super(context, BASE_DATOS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CONTACTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_CONTATO);
        sqLiteDatabase.execSQL(CREATE_CONTACTO);
    }
}