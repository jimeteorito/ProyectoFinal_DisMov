package com.junrrein.proyectofinal.backend;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;

@Entity(tableName = "usuarios")
public class UsuarioRoom {

    @NonNull
    @PrimaryKey
    public String id = "";

    public String nombreApellido;
    public String email;
    public String idDispositivo;
    public long ultimaActualizacion;

    UsuarioRoom() {
    }

    UsuarioRoom(Usuario usuario) {
        id = usuario.getId();
        nombreApellido = usuario.getNombreApellido();
        email = usuario.getEmail();
        idDispositivo = usuario.getIdDispositivo();
        ultimaActualizacion = Instant.now().getEpochSecond();
    }
}
