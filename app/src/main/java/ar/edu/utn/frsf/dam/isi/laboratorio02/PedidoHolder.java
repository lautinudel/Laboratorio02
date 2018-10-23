package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PedidoHolder {
    public TextView tvMailPedido;
    public TextView tvHoraEntrega;
    public TextView tvCantidadItems;
    public TextView tvPrecio;
    public TextView estado;
    public ImageView tipoEntrega;
    public Button btnCancelar;
    public Button btnVerDetalle;

    PedidoHolder(View view){
        this.tvMailPedido = view.findViewById(R.id.tvMailPedido);
        this.tvHoraEntrega = view.findViewById(R.id.tvHoraEntrega);
        this.tvCantidadItems = view.findViewById(R.id.tvCantidadItems);
        this.tvPrecio = view.findViewById(R.id.tvPrecio);
        this.estado = view.findViewById(R.id.estado);
        this.tipoEntrega = view.findViewById(R.id.tipoEntrega);
        this.btnCancelar = view.findViewById(R.id.btnCancelar);
        this.btnVerDetalle = view.findViewById(R.id.btnVerDetalle);
    }
}

