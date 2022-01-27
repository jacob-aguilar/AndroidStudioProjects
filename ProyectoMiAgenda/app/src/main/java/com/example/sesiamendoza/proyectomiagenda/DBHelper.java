package com.example.sesiamendoza.proyectomiagenda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper( Context context) {
        super(context, Utlidades_DB.DB_NAME, null, 2);
    }

    public  static  final String DROP_TABLE_AGENDA =
            "DROP TABLE IF EXISTS "+Utlidades_DB.TABLA_AGENDA;
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utlidades_DB.CREATE_TABLE_AGENDA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_TABLE_AGENDA);
        onCreate(db);
    }
}
