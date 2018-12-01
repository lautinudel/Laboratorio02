package ar.edu.utn.frsf.dam.isi.laboratorio02.dao;

import android.arch.persistence.room.Room;
import android.content.Context;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class MyDatabase {
    // variable de clase privada que almacena una instancia unica de esta entidad
    private static MyDatabase _INSTANCIA_UNICA=null;

    // metodo static publico que retorna la unica instancia de esta clase
    // si no existe, cosa que ocurre la primera vez que se invoca
    // la crea, y si existe retorna la instancia existente.

    public static MyDatabase getInstance(Context ctx){
        if(_INSTANCIA_UNICA==null) _INSTANCIA_UNICA = new MyDatabase(ctx);
        return _INSTANCIA_UNICA;
    }

    private MyRoomDatabase db;
    private CategoriaDao categoriaDao;
    private ProductoDao productoDao;
    private PedidoDao pedidoDao;
    private PedidoDetalleDao pedidoDetalleDao;

    // constructor privado para poder implementar SINGLETON
    // al ser privado solo puede ser invocado dentro de esta clase
    // el único lugar donde se invoca es en la linea 16 de esta clase
    // y se invocará UNA Y SOLO UNA VEZ, cuando _INSTANCIA_UNICA sea null
    // luego ya no se invoca nunca más. Nos aseguramos de que haya una
    // sola instancia en toda la aplicacion
    private MyDatabase(Context ctx){
        db = Room.databaseBuilder(ctx,
                MyRoomDatabase.class, "databaseName")
                .fallbackToDestructiveMigration()
                .build();
        categoriaDao = db.categoriaDao();
        productoDao = db.productoDao();
        pedidoDao = db.pedidoDao();
        pedidoDetalleDao = db.pedidoDetalleDao();

    }

    public void borrarTodo(){
        this.db.clearAllTables();
    }

    public CategoriaDao getCategoriaDao() {
        return categoriaDao;
    }

    public void setCategoriaDao(CategoriaDao categoriaDao) {
        this.categoriaDao = categoriaDao;
    }

    public ProductoDao getProductoDao() { return productoDao; }

    public void setProductoDao(ProductoDao productoDao){ this.productoDao=productoDao; }

    public PedidoDao getPedidoDao(){
        return pedidoDao;
    }

    public void setPedidoDao(PedidoDao pedidoDao){
        this.pedidoDao=pedidoDao;
    }

    public PedidoDetalleDao getPedidoDetalleDao(){
        return pedidoDetalleDao;
    }

    public void setPedidoDetalleDao(PedidoDetalleDao pedidoDetalleDao){
        this.pedidoDetalleDao=pedidoDetalleDao;
    }

}
