package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
        List<Pedido> lista = repositorioPedido.getLista();
        String fecha="";
        int i=0;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if(intent.getExtras()!=null){
            idABuscar = intent.getIntExtra("idPedido", 0);
            if(intent.getStringExtra("estado").equals("ESTADO_ACEPTADO")){
                for(Pedido p:lista){
                    if(p.getId().equals(idABuscar)){
                        fecha=p.getFecha().toString();
                        //Toast.makeText(context,"Pedido para"+p.getMailContacto()+" ha cambiado a estado ACEPTADO",Toast.LENGTH_LONG).show();
                        Intent resultIntent = new Intent(context, AltaPedido.class);
                        resultIntent.putExtra("id", p.getId().toString());
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                        stackBuilder.addNextIntentWithParentStack(resultIntent);
                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                        Notification notification = new NotificationCompat.Builder(context, "CANAL01").setContentIntent(resultPendingIntent)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Tu pedido fue aceptado")
                                .setContentText("El costo sera de "+p.total())
                                .setStyle(new NotificationCompat.InboxStyle()
                                        .addLine("Previsto el envio para: ")
                                        .addLine(fecha)
                                        )
                                .build();


                        notificationManager.notify(i, notification);

                        i++;
                    }
                }
            }else{
                if(intent.getStringExtra("estado").equals("ESTADO_EN_PREPARACION")){//EN PREPARACION
                    for(Pedido p:lista){
                        if(p.getId().equals(idABuscar)){
                            fecha=p.getFecha().toString();
                            //Toast.makeText(context,"Pedido para"+p.getMailContacto()+" ha cambiado a estado ACEPTADO",Toast.LENGTH_LONG).show();
                            Intent resultIntent = new Intent(context, HistorialPedidos.class);
                            resultIntent.putExtra("id", p.getId().toString());
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                            stackBuilder.addNextIntentWithParentStack(resultIntent);
                            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                            Notification notification = new NotificationCompat.Builder(context, "CANAL01").setContentIntent(resultPendingIntent)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Tu pedido esta en preparacion")
                                    .setContentText("El costo sera de "+p.total())
                                    .setStyle(new NotificationCompat.InboxStyle()
                                            .addLine("Previsto el envio para: ")
                                            .addLine(fecha)

                                    )
                                    .build();


                            notificationManager.notify(i, notification);
                            i++;
                        }
                    }
                }else{
                    if(intent.getStringExtra("estado").equals("ESTADO_LISTO")){//Listo
                        for(Pedido p:lista){
                            if(p.getId().equals(idABuscar)){
                                fecha=p.getFecha().toString();
                                //Toast.makeText(context,"Pedido para"+p.getMailContacto()+" ha cambiado a estado ACEPTADO",Toast.LENGTH_LONG).show();
                                Intent resultIntent = new Intent(context, HistorialPedidos.class);
                                resultIntent.putExtra("id", p.getId().toString());
                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                                stackBuilder.addNextIntentWithParentStack(resultIntent);
                                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                                Notification notification = new NotificationCompat.Builder(context, "CANAL01").setContentIntent(resultPendingIntent)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle("Tu pedido esta listo")
                                        .setContentText("El costo sera de "+p.total())
                                        .setStyle(new NotificationCompat.InboxStyle()
                                                .addLine("Previsto el envio para: ")
                                                .addLine(fecha)

                                        )
                                        .build();


                                notificationManager.notify(i, notification);
                                i++;
                            }
                        }
                }
            }}

        }
    }
}
