package com.junrrein.proyectofinal.backend;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Evento implements Serializable {

    private String id;
    private String nombre;
    private String idUsuarioCreador;
    private Ubicacion ubicacion;
    private LocalDate fechaInicio;
    private LocalTime horaInicio;
    private int duracion;
    private String descripcion;
    private String tipo;
    private int dislikes = 0;
    private List<String> idUsuariosInteresados = new ArrayList<>();
    private List<String> idUsuariosAsistentes = new ArrayList<>();

    public Evento(String idEvento,
                  String nombre,
                  String idUsuarioCreador,
                  Ubicacion ubicacion,
                  LocalDate fechaInicio,
                  LocalTime horaInicio,
                  int duracion,
                  String tipo) {
        this.id = idEvento;
        this.nombre = nombre;
        this.idUsuarioCreador = idUsuarioCreador;
        this.ubicacion = ubicacion;
        this.fechaInicio = fechaInicio;
        this.horaInicio = horaInicio;
        this.duracion = duracion;
        this.tipo = tipo;
    }

    Evento(String idEvento, EventoFirebase eventoFirebase) {
        this.id = idEvento;
        nombre = eventoFirebase.nombre;
        idUsuarioCreador = eventoFirebase.creador;
        ubicacion = new Ubicacion(eventoFirebase.latitud, eventoFirebase.longitud);

        LocalDateTime fechaHora = LocalDateTime.parse(eventoFirebase.fechahora,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        fechaInicio = fechaHora.toLocalDate();
        horaInicio = fechaHora.toLocalTime();
        duracion = eventoFirebase.duracion;

        descripcion = eventoFirebase.descripcion;
        tipo = eventoFirebase.tipo;
        dislikes = eventoFirebase.dislikes;

        final String VALOR_MAGICO = "0A";

        if (eventoFirebase.interesados != null) {
            for (String idUsuario : eventoFirebase.interesados.keySet()) {
                if (!idUsuario.equals(VALOR_MAGICO)) {
                    idUsuariosInteresados.add(idUsuario);
                }
            }
        }

        if (eventoFirebase.suscriptos != null) {
            for (String idUsuario : eventoFirebase.suscriptos.keySet()) {
                if (!idUsuario.equals(VALOR_MAGICO)) {
                    idUsuariosAsistentes.add(idUsuario);
                }
            }
        }
    }

    Evento(EventoRoom eventoRoom) {
        id = eventoRoom.id;
        nombre = eventoRoom.nombre;
        idUsuarioCreador = eventoRoom.idUsuarioCreador;
        ubicacion = new Ubicacion(eventoRoom.latitud, eventoRoom.longitud);
        fechaInicio = LocalDate.parse(eventoRoom.fechaInicio);
        horaInicio = LocalTime.parse(eventoRoom.horaInicio);
        duracion = eventoRoom.duracion;
        descripcion = eventoRoom.descripcion;
        tipo = eventoRoom.tipo;
        dislikes = eventoRoom.dislikes;

        if (!eventoRoom.idUsuariosInteresados.isEmpty())
            idUsuariosInteresados = new ArrayList<>(Arrays.asList(eventoRoom.idUsuariosInteresados.split(" ")));

        if (!eventoRoom.idUsuariosAsistentes.isEmpty())
            idUsuariosAsistentes = new ArrayList<>(Arrays.asList(eventoRoom.idUsuariosAsistentes.split(" ")));
    }

    public Evento copy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            return (Evento) in.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdUsuarioCreador() {
        return idUsuarioCreador;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    List<String> getIdUsuariosInteresados() {
        return idUsuariosInteresados;
    }

    List<String> getIdUsuariosAsistentes() {
        return idUsuariosAsistentes;
    }

    public boolean estaInteresado(String idUsuario) {
        return idUsuariosInteresados.contains(idUsuario);
    }

    public boolean asiste(String idUsuario) {
        return idUsuariosAsistentes.contains(idUsuario);
    }

    public void agregarUsuarioInteresado(String idUsuario) {
        idUsuariosInteresados.add(idUsuario);
    }

    public void quitarUsuarioInteresado(String idUsuario) {
        idUsuariosInteresados.remove(idUsuario);
    }

    public void agregarUsuarioAsistente(String idUsuario) {
        idUsuariosAsistentes.add(idUsuario);
    }

    public void quitarUsuarioAsistente(String idUsuario) {
        idUsuariosAsistentes.remove(idUsuario);
    }
}
