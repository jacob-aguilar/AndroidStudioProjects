package com.example.probook.piensarapido;

public class Pistas {
        private int foto;
        private String titulo;
        private String pistapista;


    public Pistas(int foto, String titulo, String pistapista) {
        this.foto = foto;
        this.titulo = titulo;
        this.pistapista = pistapista;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPistapista() {
        return pistapista;
    }

    public void setPistapista(String pistapista) {
        this.pistapista = pistapista;
    }
}
