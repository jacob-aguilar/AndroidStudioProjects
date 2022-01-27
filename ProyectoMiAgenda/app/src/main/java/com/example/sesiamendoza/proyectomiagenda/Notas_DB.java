package com.example.sesiamendoza.proyectomiagenda;

public class Notas_DB {

    public static  final String  DB_NAME="notas.db";
    public static  final String TABLA_NOTAS= "notas";
    public static  final String NOTAS_ID ="id";
    public static  final String NOTAS_ESCRIBIR_NOTAS= "escribir_notas";

    public static final String CREATE_TABLE_NOTAS=
            "CREATE TABLE "+TABLA_NOTAS+" ( "
                    + NOTAS_ID +" INTEGER  PRIMARY KEY AUTOINCREMENT, "
                    + NOTAS_ESCRIBIR_NOTAS +" TEXT NOT NULL, );";




}

