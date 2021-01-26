package com.junrrein.proyectofinal.backend;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class UsuarioFirebase {
    public String nombre;
    public String email;
    public String token;

    UsuarioFirebase() {
    }

    UsuarioFirebase(Usuario usuario) {
        nombre = usuario.getNombreApellido();
        email = usuario.getEmail();
        token = usuario.getIdDispositivo();
    }

    HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nombre", nombre);
        result.put("email", email);
        result.put("token", token);

        return result;
    }
}
