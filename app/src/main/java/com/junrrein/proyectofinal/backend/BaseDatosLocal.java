package com.junrrein.proyectofinal.backend;

import androidx.lifecycle.LiveData;

import com.junrrein.proyectofinal.MiAplicacion;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class BaseDatosLocal {

    static private AppDatabase database = MiAplicacion.getDatabase();
    static private EventoRoomDao eventoRoomDao = database.eventoRoomDao();
    static private UsuarioRoomDao usuarioRoomDao = database.usuarioRoomDao();

    static void guardarEvento(Evento evento) {
        eventoRoomDao.save(new EventoRoom(evento));
    }

    static void eliminarEvento(String idEvento) {
        eventoRoomDao.delete(idEvento);
    }

    static void guardarYEliminarEventosEnMasa(List<Evento> aGuardar, List<String> idEventosABorrar) {
        List<EventoRoom> aGuardarRoom = new ArrayList<>();

        for (Evento evento : aGuardar)
            aGuardarRoom.add(new EventoRoom(evento));

        eventoRoomDao.batchSaveAndDelete(aGuardarRoom, idEventosABorrar);
    }

    static LiveData<EventoRoom> getEvento(String idEvento) {
        return eventoRoomDao.loadById(idEvento);
    }

    static LiveData<List<EventoRoom>> getEventosAsync() {
        return eventoRoomDao.loadAllAsync();
    }

    static List<EventoRoom> getEventosSync() {
        return eventoRoomDao.loadAllSync();
    }

    static LiveData<List<EventoRoom>> getEventosParaUsuarioCreador(String idUsuario) {
        return eventoRoomDao.loadByCreator(idUsuario);
    }

    static LiveData<List<EventoRoom>> getEventosParaUsuarioInteresado(String idUsuario) {
        return eventoRoomDao.loadByInterestedUser("%" + idUsuario + "%");
    }

    static boolean existeEvento(String idEvento) {
        return eventoRoomDao.exists(idEvento) != 0;
    }

    static Instant ultimaActualizacionEvento(String idEvento) {
        return Instant.ofEpochSecond(eventoRoomDao.ultimaActualizacion(idEvento));
    }

    static void guardarUsuario(Usuario usuario) {
        usuarioRoomDao.save(new UsuarioRoom(usuario));
    }

    static LiveData<UsuarioRoom> getUsuario(String idUsuario) {
        return usuarioRoomDao.loadById(idUsuario);
    }

    static LiveData<List<UsuarioRoom>> getUsuarios(List<String> idUsuarios) {
        return usuarioRoomDao.loadById(idUsuarios);
    }

    static boolean existeUsuario(String idUsuario) {
        return usuarioRoomDao.exists(idUsuario) != 0;
    }

    static Instant ultimaActualizacionUsuario(String idUsuario) {
        return Instant.ofEpochSecond(usuarioRoomDao.ultimaActualizacion(idUsuario));
    }

    static void eliminarUsuario(String idUsuario) {
        usuarioRoomDao.delete(idUsuario);
    }
}
