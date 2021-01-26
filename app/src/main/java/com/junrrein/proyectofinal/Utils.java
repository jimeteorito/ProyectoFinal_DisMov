package com.junrrein.proyectofinal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.junrrein.proyectofinal.backend.EnviadorNotificaciones;
import com.junrrein.proyectofinal.backend.Evento;
import com.junrrein.proyectofinal.backend.Repositorio;
import com.junrrein.proyectofinal.backend.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static void enviarNotificacionDeCambio(Evento evento, String titulo, String mensaje) {
        Utils.observarUnaSolaVez(Repositorio.getUsuariosAsistentesParaEvento(evento), usuarios ->
                EnviadorNotificaciones.enviar(titulo, mensaje, obtenerIdDispositivos(usuarios)));
    }

    public static void notificarEliminacion(Evento eventoEliminado) {
        String titulo = "Evento eliminado";
        String mensaje = "El evento '" + eventoEliminado.getNombre() + "' fue eliminado";

        Utils.observarUnaSolaVez(Repositorio.getUsuariosAsistentesParaEvento(eventoEliminado), usuarios ->
                EnviadorNotificaciones.enviar(titulo, mensaje, Utils.obtenerIdDispositivos(usuarios)));
    }

    private static List<String> obtenerIdDispositivos(List<Usuario> usuarios) {
        List<String> idDispositivos = new ArrayList<>();

        for (Usuario usuario : usuarios)
            idDispositivos.add(usuario.getIdDispositivo());

        return idDispositivos;
    }

    static public <T> void observarUnaSolaVez(LiveData<T> liveData,
                                              Observer<T> observer) {
        liveData.observeForever(new Observer<T>() {
            @Override
            public void onChanged(T t) {
                liveData.removeObserver(this);
                observer.onChanged(t);
            }
        });
    }
}
