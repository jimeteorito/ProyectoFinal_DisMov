package com.junrrein.proyectofinal.backend;

import java.util.HashMap;

public class EventoFirebase {
    public String nombre;
    public String creador;
    public Double latitud;
    public Double longitud;
    public String fechahora;
    public Integer duracion;
    public String descripcion;
    public String tipo;
    public Integer dislikes;
    public HashMap<String, Boolean> interesados = new HashMap<>();
    public HashMap<String, Boolean> suscriptos = new HashMap<>();

    public EventoFirebase() {
    }

    public EventoFirebase(Evento evento) {
        nombre = evento.getNombre();
        creador = evento.getIdUsuarioCreador();
        latitud = evento.getUbicacion().latitud;
        longitud = evento.getUbicacion().longitud;
        descripcion = evento.getDescripcion();
        tipo = evento.getTipo();
        dislikes = evento.getDislikes();

        String fecha = evento.getFechaInicio().toString();
        String hora = evento.getHoraInicio().toString();
        fechahora = fecha + " " + hora;
        duracion = evento.getDuracion();

        for (String idUsuario : evento.getIdUsuariosInteresados())
            interesados.put(idUsuario, true);

        for (String idUsuario : evento.getIdUsuariosAsistentes())
            suscriptos.put(idUsuario, true);
    }

    HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nombre", nombre);
        result.put("creador", creador);
        result.put("latitud", latitud);
        result.put("longitud", longitud);
        result.put("fechahora", fechahora);
        result.put("duracion", duracion);
        result.put("descripcion", descripcion);
        result.put("tipo", tipo);
        result.put("dislikes", dislikes);
        result.put("interesados", interesados);
        result.put("suscriptos", suscriptos);

        return result;
    }
}
