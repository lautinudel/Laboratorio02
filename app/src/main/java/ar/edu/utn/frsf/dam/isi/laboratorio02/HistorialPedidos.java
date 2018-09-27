package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;

public class HistorialPedidos extends AppCompatActivity {
    AdaptadorPedido adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedidos);
        Button btnHistorialNuevo = (Button) findViewById(R.id.btnHistorialNuevo);
        Button btnHistorialMenu = (Button) findViewById(R.id.btnHistorialMenu);
        ListView lstHistorialPedidos = (ListView) findViewById(R.id.lstHistorialPedidos);
        PedidoRepository pedidosRealizados = new PedidoRepository();
        adapter = new AdaptadorPedido(this,pedidosRealizados.getLista() );
        lstHistorialPedidos.setAdapter(adapter);

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
                i.putExtra("NUEVO_PEDIDO",1);
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
