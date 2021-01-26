package com.junrrein.proyectofinal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.junrrein.proyectofinal.backend.Repositorio;
import com.junrrein.proyectofinal.backend.Usuario;

public class ModeloUsuario extends ViewModel {

    public String idUsuario;
    private LiveData<Usuario> usuarioLiveData;

    void setUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
        usuarioLiveData = Repositorio.getUsuario(idUsuario);
    }

    LiveData<Usuario> getUsuario() {
        return usuarioLiveData;
    }

    void guardarUsuario(Usuario usuario) {
        Repositorio.guardarUsuario(usuario);
    }
}
