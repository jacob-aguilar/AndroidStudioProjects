package com.example.sesiamendoza.proyectomiagenda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperNotas extends SQLiteOpenHelper {

    public DBHelperNotas (Context context){
        super(context, Notas_DB.DB_NAME, null, 2);
    }


    public  static  final String DROP_TABLE_NOTAS =
            "DROP TABLE IF EXISTS "+Notas_DB.TABLA_NOTAS;
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Notas_DB.CREATE_TABLE_NOTAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_TABLE_NOTAS);
        onCreate(db);
    }
}





