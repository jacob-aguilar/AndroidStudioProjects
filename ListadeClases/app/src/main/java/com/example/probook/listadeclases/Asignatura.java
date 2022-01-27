package com.example.probook.listadeclases;

public class Asignatura {
    private String descripcion;
    private String codigo;
    private int uv;
    private String requisito;


    public Asignatura() {
    }

    public Asignatura(String descripcion, String codigo, int uv, String requisito) {
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.uv = uv;
        this.requisito = requisito;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public String getRequisito() {
        return requisito;
    }

    public void setRequisito(String requisito) {
        this.requisito = requisito;
    }
}
