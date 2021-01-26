package com.junrrein.proyectofinal.backend;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {EventoRoom.class, UsuarioRoom.class},
        version = 13,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    abstract EventoRoomDao eventoRoomDao();
    abstract UsuarioRoomDao usuarioRoomDao();
}
