package com.example.probook.iiexamen;

public class Articulos {

    private String nombre; private String descripcion;

    private String precio;
    private int icono;

    public Articulos() {
    }

    public Articulos(String nombre, String descripcion, String precio, int icono) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.icono = icono;

    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
