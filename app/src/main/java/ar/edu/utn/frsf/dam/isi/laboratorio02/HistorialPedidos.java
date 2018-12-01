package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class HistorialPedidos extends AppCompatActivity {
    AdaptadorPedido adapter;
    PedidoDao pDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedidos);
        Button btnHistorialNuevo = (Button) findViewById(R.id.btnHistorialNuevo);
        Button btnHistorialMenu = (Button) findViewById(R.id.btnHistorialMenu);
        ListView lstHistorialPedidos = (ListView) findViewById(R.id.lstHistorialPedidos);
        //PedidoRepository pedidosRealizados = new PedidoRepository();
        final List<Pedido> allPedidos = new ArrayList<>();
        adapter = new AdaptadorPedido(this,allPedidos);
        lstHistorialPedidos.setAdapter(adapter);


        Thread r = new Thread() {
            @Override
            public void run() {

                try {
                    pDao = MyDatabase.getInstance(getApplicationContext()).getPedidoDao();
                    //List<Categoria> nuevasCat = apiRest.listarTodas();
                    List<Pedido> nuevosPed = pDao.getAll();
                    System.out.println(nuevosPed.size());
                    List<Pedido> aux = new ArrayList<>();
                    // System.out.println(nuevasCat.size());
                    for (Pedido c : allPedidos ){
                        for(Pedido d : nuevosPed){
                            if(c.getId()==d.getId()){
                                aux.add(d); //LOS REPETIDOS
                            }
                        }

                    }
                    nuevosPed.removeAll(aux);
                    System.out.println("Nuevas categorias: "+ nuevosPed.size());
                    allPedidos.addAll(nuevosPed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }};
        r.start();



        btnHistorialMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HistorialPedidos.this,MainActivity.class);
                startActivity(i);
            }

        });
        btnHistorialNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HistorialPedidos.this,AltaPedido.class);
                //i.putExtra("NUEVO_PEDIDO",1);
                startActivityForResult(i,3);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);
        if (requestCode == 3){
            if(resultCode == RESULT_OK){
                adapter.notifyDataSetChanged();
            }
        }
    }
}
