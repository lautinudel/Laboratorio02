package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

@Dao
public interface PedidoDetalleDao {
    @Query( "SELECT * FROM PedidoDetalle")
    List<PedidoDetalle> getAll();

    @Query("SELECT * FROM PedidoDetalle WHERE id IN (:pedidodetalleIds)")
    List<PedidoDetalle> loadAllByIds(int[] pedidodetalleIds);

    @Query("SELECT * FROM PedidoDetalle WHERE ped_id IN (:pedidoId)")
    List<PedidoDetalle> loadAllByPedidoId(int pedidoId);

    @Insert
    long insert(PedidoDetalle p);

    @Update
    void update(PedidoDetalle p);

    @Delete
    void delete(PedidoDetalle p);
}
