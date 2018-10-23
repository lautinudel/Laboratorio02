package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class RestoMessagingService extends FirebaseMessagingService {
    public RestoMessagingService() {
    }

    @Override
    /*public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }*/
   public void onMessageReceived(RemoteMessage remoteMessage) {

       Object[] todo =  remoteMessage.getData().keySet().toArray();


       PedidoRepository p = new PedidoRepository();

       for(Pedido ped: p.getLista()){
            System.out.println(ped.getId()+ "    "+ todo[0].toString());
           if(ped.getId()==Integer.valueOf(todo[0].toString())){
               System.out.println("LLEGO");
               ped.setEstado(Pedido.Estado.LISTO);
               Intent j = new Intent(this, EstadoPedidoReceiver.class);
               j.putExtra("idPedido",ped.getId());
               j.putExtra("estado", "ESTADO_LISTO");
               j.setAction("ESTADO_LISTO");
               sendBroadcast(j);
           }
       }
   }


}
