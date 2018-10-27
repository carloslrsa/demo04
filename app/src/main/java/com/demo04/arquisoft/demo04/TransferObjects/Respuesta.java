package com.demo04.arquisoft.demo04.TransferObjects;

import android.graphics.Bitmap;

public class Respuesta {
    private String preguntaId;
    private String respuestaId;
    private String nombre;
    private String contenido;
    private Bitmap fotoPregunta;

    public Respuesta() {
    }

    public Respuesta(String preguntaId, String respuestaId, String nombre, String contenido, Bitmap fotoPregunta) {
        this.preguntaId = preguntaId;
        this.respuestaId = respuestaId;
        this.nombre = nombre;
        this.contenido = contenido;
        this.fotoPregunta = fotoPregunta;
    }

    public String getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(String preguntaId) {
        this.preguntaId = preguntaId;
    }

    public String getRespuestaId() {
        return respuestaId;
    }

    public void setRespuestaId(String respuestaId) {
        this.respuestaId = respuestaId;
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

    public Bitmap getFotoPregunta() {
        return fotoPregunta;
    }

    public void setFotoPregunta(Bitmap fotoPregunta) {
        this.fotoPregunta = fotoPregunta;
    }
}
