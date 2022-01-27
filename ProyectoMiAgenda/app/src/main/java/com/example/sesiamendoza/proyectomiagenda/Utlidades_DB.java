package com.example.sesiamendoza.proyectomiagenda;

public class Utlidades_DB {

    public static  final String  DB_NAME="agenda.db";
    public static  final String TABLA_AGENDA = "agenda";
    public static  final String AGENDA_ID ="id";
    public static  final String AGENDA_TITULO = "titulo";
    public static final String AGENDA_CATEGORIA = "categoria";
    public static final String AGENDA_DIRECCION = "direccion";
    public static  final String AGENDA_FECHA = "fecha";
    public static final  String AGENDA_HORA = "hora";

    public static final String CREATE_TABLE_AGENDA =
            "CREATE TABLE "+TABLA_AGENDA+" ( "
            + AGENDA_ID +" INTEGER  PRIMARY KEY AUTOINCREMENT, "
            + AGENDA_TITULO +" TEXT NOT NULL, "
            + AGENDA_DIRECCION +" TEXT NOT NULL, "
            +AGENDA_CATEGORIA + " TEXT NOT NULL, "
            + AGENDA_FECHA +" TEXT NOT NULL, "
            +AGENDA_HORA +" TEXT NOT NULL );";


}
