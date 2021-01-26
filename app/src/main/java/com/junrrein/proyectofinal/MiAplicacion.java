package com.junrrein.proyectofinal;

import android.app.Application;

import androidx.room.Room;

import com.junrrein.proyectofinal.backend.AppDatabase;

public class MiAplicacion extends Application {

    static private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class,
                "base-datos-local")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}
