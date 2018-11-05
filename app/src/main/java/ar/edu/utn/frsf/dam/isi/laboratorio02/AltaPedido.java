package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.PedidoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class AltaPedido extends AppCompatActivity {
    Intent intent;
    private Pedido unPedido;
    private PedidoRepository repositorioPedido = new PedidoRepository();
    private ProductoRepository repositorioProducto = new ProductoRepository();
    List<PedidoDetalle> ProductosElegidos = new ArrayList<>();

    EditText edtPedidoDireccion;
    ListView lista;
    ArrayAdapter adaptador;
    TextView lblTotalPedido;
    double contador = 0;
    int itemSeleccionado=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_pedido);
        intent = getIntent();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);//getApplicationContext().getSharedPreferences("Preferencias",0);

        Button btnPedidoAddProducto = (Button) findViewById(R.id.btnPedidoAddProducto);
        Button btnPedidoVolver = (Button) findViewById(R.id.btnPedidoVolver);
        Button btnPedidoQuitarProducto = (Button) findViewById(R.id.btnPedidoQuitarProducto);
        final Button btnPedidoHacerPedido = (Button) findViewById(R.id.btnPedidoHacerPedido);
        lblTotalPedido = (TextView) findViewById(R.id.lblTotalPedido);

        //Campos obligatorios
        final RadioButton optPedidoRetira = (RadioButton) findViewById(R.id.optPedidoRetira);
        final RadioButton optPedidoEnviar = (RadioButton) findViewById(R.id.optPedidoEnviar);
        edtPedidoDireccion = (EditText) findViewById(R.id.edtPedidoDireccion);
        lista = (ListView) findViewById(R.id.lstPedidoItems);
        final EditText edtPedidoCorreo = (EditText) findViewById(R.id.edtPedidoCorreo);
        final EditText edtPedidoHoraEntrega = (EditText) findViewById(R.id.edtPedidoHoraEntrega);
        edtPedidoCorreo.setText(sharedPreferences.getString("edit_text_preference_1","Default value"));
        optPedidoRetira.setChecked(sharedPreferences.getBoolean("check_box_preference_1", false));
        //Si vengo de historial de pedidos (detalles)
        int idABuscar = -1;
        if(getIntent().getExtras()!=null){
            idABuscar = Integer.valueOf(getIntent().getStringExtra("id"));
            if(idABuscar>-1){
                unPedido = repositorioPedido.buscarPorId(idABuscar);
                edtPedidoCorreo.setText(unPedido.getMailContacto());
                edtPedidoDireccion.setText(unPedido.getDireccionEnvio());
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                edtPedidoHoraEntrega.setText(sdf.format(unPedido.getFecha()));
                if(unPedido.getRetirar())
                    optPedidoRetira.setChecked(true);
                else optPedidoEnviar.setChecked(true);
            }else unPedido = new Pedido();
        }else
            unPedido = new Pedido();


        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        adaptador = new ArrayAdapter(AltaPedido.this, android.R.layout.simple_list_item_single_choice, ProductosElegidos);
        lista.setAdapter(adaptador);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView parent, View v, int position, long id){
                itemSeleccionado=position;
                System.out.println(position);
            }

        });
        btnPedidoAddProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AltaPedido.this,ListaProductos.class);
                i.putExtra("NUEVO_PEDIDO", 1);
                startActivityForResult(i, 2);
            }

        });

        btnPedidoVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contador=0;
                Intent i = new Intent(AltaPedido.this,MainActivity.class);
                startActivity(i);
            }

        });
        btnPedidoHacerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Campo correo electrónico no vacío.
                //Uno de los dos RadioButton debe estar activo.
                //Si lo esta el de enviar a domicilio, el EditText de Dirección envío debe no estar vacío.
                //Campo hora solicitada debe ser no vacío.
                //Debe haber por lo menos un ítem en la lista de productos.

                        if (edtPedidoCorreo.length() == 0) {
                            Toast.makeText(AltaPedido.this, "Complete el campo 'Correo Electronico'", Toast.LENGTH_SHORT).show();
                        } else if (edtPedidoHoraEntrega.length() == 0) {
                            Toast.makeText(AltaPedido.this, "Complete el campo 'Hora solicitada'", Toast.LENGTH_SHORT).show();
                        } else if (ProductosElegidos.isEmpty()) {
                            Toast.makeText(AltaPedido.this, "Por favor añade un producto", Toast.LENGTH_SHORT).show();
                        } else if (optPedidoEnviar.isChecked() && edtPedidoDireccion.length() == 0) {
                            Toast.makeText(AltaPedido.this, "Complete el campo 'Direccion de envio'", Toast.LENGTH_SHORT).show();
                        } else if (!optPedidoRetira.isChecked() && !optPedidoEnviar.isChecked()) {
                            Toast.makeText(AltaPedido.this, "Por favor selecciona un modo de entrega", Toast.LENGTH_SHORT).show();
                        } else {
                            String[] horaIngresada = edtPedidoHoraEntrega.getText().toString().split(":");
                            GregorianCalendar hora = new GregorianCalendar();

                            int valorHora = Integer.valueOf(horaIngresada[0]);
                            int valorMinuto = Integer.valueOf(horaIngresada[1]);
                            if (valorHora < 0 || valorHora > 23) {
                                Toast.makeText(AltaPedido.this, "La hora ingresada " + valorHora + " es incorrecta", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (valorMinuto < 0 || valorMinuto > 59) {
                                Toast.makeText(AltaPedido.this, "Los minutos " + valorMinuto + " son incorrectos", Toast.LENGTH_LONG).show();
                                return;
                            }
                            hora.set(Calendar.HOUR_OF_DAY, valorHora);
                            hora.set(Calendar.MINUTE, valorMinuto);
                            hora.set(Calendar.SECOND, Integer.valueOf(0));

                            Boolean retirar;
                            if (optPedidoRetira.isChecked()) {
                                retirar = true;
                            } else {
                                retirar = false;
                            }

                            //Seteamos el pedido nuevo
                            unPedido.setFecha(hora.getTime());
                            unPedido.setDetalle(ProductosElegidos);
                            unPedido.setEstado(Pedido.Estado.REALIZADO);
                            unPedido.setMailContacto(edtPedidoCorreo.getText().toString());
                            unPedido.setRetirar(retirar);
                            unPedido.setDireccionEnvio(edtPedidoDireccion.getText().toString());

                            //Guardamos en el repositorio de pedidos y pasamos a la actividad "HistorialPedidos"
                            repositorioPedido.guardarPedido(unPedido);
                            //System.out.println(unPedido.getId());


                            Thread r = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.currentThread().sleep(10000);
                                    }catch (InterruptedException e) {
                                        e.printStackTrace(); }
                                    // buscar pedidos no aceptados y aceptarlos automáticamente
                                    List<Pedido> lista = repositorioPedido.getLista();
                                    for(Pedido p:lista){
                                        if(p.getEstado().equals(Pedido.Estado.REALIZADO)) {
                                            p.setEstado(Pedido.Estado.ACEPTADO);
                                            Intent j = new Intent(AltaPedido.this, EstadoPedidoReceiver.class);
                                            j.putExtra("idPedido",p.getId());
                                            j.putExtra("estado", "ESTADO_ACEPTADO");
                                           j.setAction("ESTADO_ACEPTADO");
                                           sendBroadcast(j);
                                        }
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                           // Toast.makeText(AltaPedido.this,"Informacion de pedidos actualizada!",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }};
                            //Thread unHilo = new Thread();
                            r.start();


                            Intent i = new Intent(AltaPedido.this, HistorialPedidos.class);
                            setResult(RESULT_OK);
                            startActivity(i);
                        }

                    }




                });

                btnPedidoQuitarProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (itemSeleccionado == -1) {
                            Toast.makeText(AltaPedido.this, "Selecciona un producto para poder eliminarlo", Toast.LENGTH_SHORT).show();
                        } else {
                            PedidoDetalle temporal = ProductosElegidos.get(itemSeleccionado);
                            ProductosElegidos.remove(itemSeleccionado);
                            contador = contador - (temporal.getProducto().getPrecio() * temporal.getCantidad());
                            adaptador.notifyDataSetChanged();
                            lblTotalPedido.setText("Total del pedido: $" + contador);
                        }
                    }

                });
                optPedidoEnviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtPedidoDireccion.setEnabled(true);
                    }

                });
                optPedidoRetira.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtPedidoDireccion.setEnabled(false);
                    }

                });
            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==ListaProductos.RESULT_OK){
            if (requestCode==2) {
                int cantidad = Integer.parseInt(data.getExtras().getString("cantidad"));
                int idProducto =  data.getExtras().getInt("idProducto");
                PedidoDetalle nuevo = new PedidoDetalle(cantidad, repositorioProducto.buscarPorId(idProducto));
                ProductosElegidos.add(nuevo);
                adaptador.notifyDataSetChanged();
                contador = contador + repositorioProducto.buscarPorId(idProducto).getPrecio()*cantidad;
                lblTotalPedido.setText("Total del pedido: "+"$"+contador);
            }
        }
    }
}
