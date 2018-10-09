package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

import static android.content.Intent.getIntent;

public class EstadoPedidoReceiver extends BroadcastReceiver {
    private PedidoRepository repositorioPedido = new PedidoRepository();
    @Override
    public void onReceive(Context context, Intent intent) {
        int idABuscar;
        if(intent.getExtras()!=null){
            System.out.println("HOLA");
            if(intent.getStringExtra("estado").equals("ESTADO_ACEPTADO")){
                idABuscar = Integer.valueOf(intent.getStringExtra("idPedido"));
                List<Pedido> lista = repositorioPedido.getLista();
                for(Pedido p:lista){
                    if(p.getId().equals(idABuscar))
                        Toast.makeText(context,"Pedido para"+p.getMailContacto()+" ha cambiado a estado ACEPTADO",Toast.LENGTH_LONG).show();

                }
            }

        }
    }
}
