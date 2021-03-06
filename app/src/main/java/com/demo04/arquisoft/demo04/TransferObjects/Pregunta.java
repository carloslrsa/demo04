package com.demo04.arquisoft.demo04.TransferObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class Pregunta implements Serializable{
    private String preguntaId;
    private String nombre;
    private String contenido;
    //private Bitmap fotoPregunta;
    private byte[] fotoPreguntaBytes;

    public Pregunta() {
    }

    public Pregunta(String preguntaId, String nombre, String contenido, Bitmap fotoPregunta) {
        this.preguntaId = preguntaId;
        this.nombre = nombre;
        this.contenido = contenido;
        //this.fotoPregunta = fotoPregunta;

        setFotoPregunta(fotoPregunta);

    }

    public String getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(String preguntaId) {
        this.preguntaId = preguntaId;
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
        if(fotoPreguntaBytes == null)
            return null;
        return BitmapFactory.decodeByteArray(fotoPreguntaBytes, 0, fotoPreguntaBytes.length);
    }

    public void setFotoPregunta(Bitmap fotoPregunta) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        fotoPregunta.compress(Bitmap.CompressFormat.PNG, 100, stream);
        fotoPreguntaBytes = stream.toByteArray();
    }
}
