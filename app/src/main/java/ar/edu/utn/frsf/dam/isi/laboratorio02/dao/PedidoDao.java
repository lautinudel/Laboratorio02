package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoConDetalles;

@Dao
public interface PedidoDao {
    @Query("SELECT * FROM Pedido")
    List<Pedido> getAll();

    @Query("SELECT * FROM pedido WHERE id = (:pedidoIds)")
    List<Pedido> loadAllByIds(int pedidoIds);

    @Insert
    long insert(Pedido p);

    @Update
    void update(Pedido p);

    @Delete
    void delete(Pedido p);
}
