package com.junrrein.proyectofinal.backend;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
abstract class EventoRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void save(EventoRoom evento);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void save(List<EventoRoom> eventos);

    @Query("DELETE FROM eventos WHERE id=:idEvento")
    abstract void delete(String idEvento);

    @Query("DELETE FROM eventos WHERE id in (:idEventos)")
    abstract void delete(List<String> idEventos);

    @Query("SELECT * FROM eventos")
    abstract LiveData<List<EventoRoom>> loadAllAsync();

    @Query("SELECT * FROM eventos")
    abstract List<EventoRoom> loadAllSync();

    @Query("SELECT * FROM eventos WHERE id=:idEvento")
    abstract LiveData<EventoRoom> loadById(String idEvento);

    @Query("SELECT * FROM eventos WHERE idUsuarioCreador=:idUsuario")
    abstract LiveData<List<EventoRoom>> loadByCreator(String idUsuario);

    @Query("SELECT * FROM eventos WHERE idUsuariosInteresados like :patronIdUsuario")
    abstract LiveData<List<EventoRoom>> loadByInterestedUser(String patronIdUsuario);

    @Query("SELECT COUNT(*) FROM eventos WHERE id=:idEvento")
    abstract int exists(String idEvento);

    @Query("SELECT ultimaActualizacion FROM eventos WHERE id=:idEvento")
    abstract long ultimaActualizacion(String idEvento);

    @Transaction
    public void batchSaveAndDelete(List<EventoRoom> toSave, List<String> idEventosToDelete) {
        delete(idEventosToDelete);
        save(toSave);
    }
}
