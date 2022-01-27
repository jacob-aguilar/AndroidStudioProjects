package com.example.sesiamendoza.proyectomiagenda;

import org.json.JSONObject;

import java.io.Serializable;

public class Notas implements Serializable {
    private long id;
    private String nota, fecha , hora;

    public Notas() {
    }

    public static JSONObject getText() {
        return null;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
