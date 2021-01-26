package com.junrrein.proyectofinal.backend;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Ubicacion implements Serializable {
    public Double latitud;
    public Double longitud;

    public Ubicacion(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    @NonNull
    @Override
    public String toString() {
        return "Ubicacion{" +
                "latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}
