package com.junrrein.proyectofinal.backend;

import androidx.annotation.NonNull;

public class Usuario {

    private String id;
    private String nombreApellido;
    private String email;
    private String idDispositivo = "";

    public Usuario(String id, String nombreApellido, String email) {
        this.id = id;
        this.nombreApellido = nombreApellido;
        this.email = email;
    }

    Usuario(String id, UsuarioFirebase usuarioFirebase) {
        this.id = id;
        nombreApellido = usuarioFirebase.nombre;
        email = usuarioFirebase.email;
        idDispositivo = usuarioFirebase.token;
    }

    Usuario(UsuarioRoom usuarioRoom) {
        id = usuarioRoom.id;
        nombreApellido = usuarioRoom.nombreApellido;
        email = usuarioRoom.email;
        idDispositivo = usuarioRoom.idDispositivo;
    }

    String getId() {
        return id;
    }

    String getNombreApellido() {
        return nombreApellido;
    }

    String getEmail() {
        return email;
    }

    public String getNombreOEmail() {
        if (nombreApellido != null)
            return nombreApellido;

        if (email != null)
            return email;

        return "sin_nombre";
    }

    public String getIdDispositivo() {
        return idDispositivo;
    }

    public void setIdDispositivo(String idDispositivo) {
        this.idDispositivo = idDispositivo;
    }

    @Override
    @NonNull
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombreApellido='" + nombreApellido + '\'' +
                '}';
    }
}
