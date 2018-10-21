package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class PrepararPedidoService extends IntentService {
    private PedidoRepository repositorioPedido = new PedidoRepository();
    public PrepararPedidoService() {
        super("PrepararPedidoService");
    }

   /* @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
*/
    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {

        Thread r = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(20000);
                }catch (InterruptedException e) {
                    e.printStackTrace(); }
                List<Pedido> lista = repositorioPedido.getLista();
                for(Pedido p:lista){
                    if(p.getEstado().equals(Pedido.Estado.ACEPTADO)) {
                        p.setEstado(Pedido.Estado.EN_PREPARACION);
                        Intent j = new Intent(PrepararPedidoService.this, EstadoPedidoReceiver.class);
                        j.putExtra("idPedido",p.getId());
                        j.putExtra("estado", "ESTADO_EN_PREPARACION");
                        j.setAction("ESTADO_EN_PREPARACION");
                        sendBroadcast(j);
                    }}


            }};
        r.start();
    }
}
