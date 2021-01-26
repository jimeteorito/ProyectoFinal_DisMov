package com.junrrein.proyectofinal.backend;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Entity(tableName = "eventos")
public class EventoRoom {

    @NonNull
    @PrimaryKey
    public String id = "";

    public String nombre;
    public String idUsuarioCreador;
    public Double latitud;
    public Double longitud;
    public String fechaInicio;
    public String horaInicio;
    public Integer duracion;
    public String descripcion;
    public String tipo;
    public Integer dislikes;
    public String idUsuariosInteresados;
    public String idUsuariosAsistentes;
    public long ultimaActualizacion;

    EventoRoom() {
    }

    EventoRoom(Evento evento) {
        id = evento.getId();
        nombre = evento.getNombre();
        idUsuarioCreador = evento.getIdUsuarioCreador();
        Ubicacion ubicacion = evento.getUbicacion();
        latitud = ubicacion.latitud;
        longitud = ubicacion.longitud;
        fechaInicio = evento.getFechaInicio().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        horaInicio = evento.getHoraInicio().format(DateTimeFormatter.ofPattern("HH:mm"));
        duracion = evento.getDuracion();
        descripcion = evento.getDescripcion();
        tipo = evento.getTipo();
        dislikes = evento.getDislikes();
        idUsuariosInteresados = String.join(" ", evento.getIdUsuariosInteresados());
        idUsuariosAsistentes = String.join(" ", evento.getIdUsuariosAsistentes());
        ultimaActualizacion = Instant.now().getEpochSecond();
    }
}
