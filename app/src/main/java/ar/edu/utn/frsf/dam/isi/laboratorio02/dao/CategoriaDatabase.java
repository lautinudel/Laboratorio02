package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.RoomDatabase;

public abstract class CategoriaDatabase extends RoomDatabase {

public abstract CategoriaDao categoriaDao();

}
