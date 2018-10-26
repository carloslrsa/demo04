package com.demo04.arquisoft.demo04.TransferObjects;

public class Pregunta {
    private String nombre;
    private String contenido;

    public Pregunta() {
    }

    public Pregunta(String nombre, String contenido) {
        this.nombre = nombre;
        this.contenido = contenido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
