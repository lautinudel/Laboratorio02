package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Placeholder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;

public class AdaptadorPedido extends ArrayAdapter<Pedido> {
    private Context ctx;
    private List<Pedido> datos; //Guardamos todos los datos que queremos mostrar en esta variable.

    public AdaptadorPedido(Context context,List<Pedido> objects){
        super(context, 0, objects);
        this.ctx = context;
        this.datos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            //Si es nulo le pasamos el row con el layout que queremos (inflamos la vista)
           view = LayoutInflater.from(getContext()).inflate(R.layout.fila_historial, null);
        }
        PedidoHolder modelo = (PedidoHolder) view.getTag();
        if(modelo==null){
            //Si no se creo, lo creamos con la vista
            modelo = new PedidoHolder(view);
        }

        //Obtenemos el elemento en el que estamos clickeando (el pedido)
        Pedido pedido = datos.get(position);

        //Seteamos
            //Mail
            modelo.tvMailPedido.setText("Contacto: "+pedido.getMailContacto());

            //Imagen
            if(pedido.getRetirar()){
               // modelo.tipoEntrega.setImageResource(IMAGEN DE RETIRO EN SUC);
            }else{
                // modelo.tipoEntrega.setImageResource(IMAGEN DE DOMICILIO);
            }

            //Precio de pedido
            double totalPedido = 0;
            List<PedidoDetalle> listaDetallesDelPedido = pedido.getDetalle();
            for (PedidoDetalle PD:listaDetallesDelPedido
                 ) {
                totalPedido=totalPedido+(PD.getProducto().getPrecio()*PD.getCantidad());
            }
            modelo.tvPrecio.setText(" A pagar: $"+totalPedido);

            //Cantidad de items
            int cantidad = listaDetallesDelPedido.size();
            modelo.tvCantidadItems.setText("Items: "+cantidad);

            //Estado
            modelo.estado.setText("Estado: "+pedido.getEstado());
            switch (pedido.getEstado()){
                case LISTO:
                    modelo.estado.setTextColor(Color.DKGRAY);
                    break;
                case ENTREGADO:
                    modelo.estado.setTextColor(Color.BLUE);
                    break;
                case CANCELADO:
                case RECHAZADO:
                    modelo.estado.setTextColor(Color.RED);
                    break;
                case ACEPTADO:
                    modelo.estado.setTextColor(Color.GREEN);
                    break;
                case EN_PREPARACION:
                    modelo.estado.setTextColor(Color.MAGENTA);
                    break;
                case REALIZADO:
                    modelo.estado.setTextColor(Color.BLUE);
                    break;
            }

            //Fecha de entrega

                modelo.tvHoraEntrega.setText("Fecha de entrega: "+pedido.getFecha());

            //BOTONES

                //Boton CANCELAR

                    modelo.btnCancelar.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view) {
                            int  indice = (int) view.getTag();
                            Pedido pedidoSeleccionado = datos.get(indice);
                            System.out.println(datos);
                            if( pedidoSeleccionado.getEstado().equals(Pedido.Estado.REALIZADO)||
                                    pedidoSeleccionado.getEstado().equals(Pedido.Estado.ACEPTADO)||
                                    pedidoSeleccionado.getEstado().equals(Pedido.Estado.EN_PREPARACION)){
                                pedidoSeleccionado.setEstado(Pedido.Estado.CANCELADO);
                                AdaptadorPedido.this.notifyDataSetChanged();
                                System.out.println(datos);
                                return;
                            }
                        }
                    });

                //Boton DETALLES

                    modelo.btnVerDetalle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(), "A implementar.", Toast.LENGTH_SHORT).show();
                        }
                    });

        return view;
    }


}
