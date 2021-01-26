package com.junrrein.proyectofinal.backend;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class BaseDatosRemota {

    private static final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private static final String nodoUsuarios = "usuarios";
    private static final String nodoEventos = "eventos";

    static Task<Usuario> getUsuario(String idUsuario) {
        TaskCompletionSource<Usuario> taskCompletionSource = new TaskCompletionSource<>();

        database.child(nodoUsuarios).child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Usuario usuario = new Usuario(dataSnapshot.getKey(),
                            Objects.requireNonNull(dataSnapshot.getValue(UsuarioFirebase.class)));
                    taskCompletionSource.setResult(usuario);
                } else {
                    taskCompletionSource.setException(new Exception("El usuario con ese id no existe"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    static Task<Void> guardarUsuario(Usuario usuario) {
        return database.child(nodoUsuarios).child(usuario.getId())
                .updateChildren(new UsuarioFirebase(usuario).toMap());
    }

    static Task<Void> eliminarUsuario(String idUsuario) {
        return database.child(nodoUsuarios).child(idUsuario).removeValue();
    }

    static String crearIdDeEvento() {
        return database.child(nodoEventos).push().getKey();
    }

    static Task<Evento> getEvento(String idEvento) {
        TaskCompletionSource<Evento> taskCompletionSource = new TaskCompletionSource<>();

        database.child(nodoEventos).child(idEvento).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Evento evento = new Evento(dataSnapshot.getKey(),
                            Objects.requireNonNull(dataSnapshot.getValue(EventoFirebase.class)));
                    taskCompletionSource.setResult(evento);
                } else {
                    taskCompletionSource.setException(new Exception("El evento con ese id no existe"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    static Task<List<Evento>> getEventos() {
        TaskCompletionSource<List<Evento>> taskCompletionSource = new TaskCompletionSource<>();

        database.child(nodoEventos).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Evento> result = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Evento evento = new Evento(child.getKey(), Objects.requireNonNull(child.getValue(EventoFirebase.class)));
                    result.add(evento);
                }

                taskCompletionSource.setResult(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    static Task<Void> guardarEvento(Evento evento) {
        return database.child(nodoEventos).child(evento.getId())
                .updateChildren(new EventoFirebase(evento).toMap());
    }

    static Task<Void> eliminarEvento(String idEvento) {
        return database.child(nodoEventos).child(idEvento).removeValue();
    }
}
