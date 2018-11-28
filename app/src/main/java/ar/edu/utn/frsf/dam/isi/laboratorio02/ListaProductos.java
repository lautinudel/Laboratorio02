package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.CategoriaDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.PedidoDetalle;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import static java.lang.Integer.parseInt;


public class ListaProductos extends AppCompatActivity{
    Spinner spinner;
    Intent intent;
    ListView lista;
    EditText edtProdCantidad;
    Producto P;
    private CategoriaDao cDao;
    private ProductoDao pDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);
        intent = getIntent();
       // final CategoriaRest apiRest = new CategoriaRest();

        //Obtenemos el edit text y boton agregar, para habilitarlo si es necesario
        edtProdCantidad = (EditText) findViewById(R.id.edtProdCantidad);
        Button btnProdAddPedido = (Button) findViewById(R.id.btnProdAddPedido);
        lista = (ListView) findViewById(R.id.lstProductos);
        int extras = intent.getIntExtra("NUEVO_PEDIDO",1);

        if(extras==1){
            edtProdCantidad.setEnabled(true);
            btnProdAddPedido.setEnabled(true);
        }

        spinner = (Spinner) findViewById(R.id.cmbProductosCategoria);
        //Definición de adaptador para spinner
         ProductoRepository Repositorio = new ProductoRepository();
            final List<Categoria> datosCategoria;
            //Obtenemos los datos a desplegar con el repositorio que creamos.
            datosCategoria = Repositorio.getCategorias();
        //Creamos el adaptador, pasando por parametro la actividad en al que estamos, el layout y lo que queremos mostrar.
            ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,datosCategoria);
            adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Definimos el adaptador que va a utilizar el spinner
            spinner.setAdapter(adaptador);
            spinner.setSelection(0);

        Thread r = new Thread() {
            @Override
            public void run() {

                try {
                    cDao = MyDatabase.getInstance(getApplicationContext()).getCategoriaDao();
                    //List<Categoria> nuevasCat = apiRest.listarTodas();
                    List<Categoria> nuevasCat = cDao.getAll();
                    System.out.println(nuevasCat.size());
                    datosCategoria.addAll(nuevasCat);
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


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Object item = parent.getItemAtPosition(pos);
                //System.out.println(item.toString());
                obtenerProductosCatElegida(item.toString());

            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Producto listItem = ((Producto) lista.getItemAtPosition(position));
                P=listItem;
            }
        });

        btnProdAddPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(edtProdCantidad.getText().toString().length()>0) {
                Intent i = new Intent();
                i.putExtra("cantidad", edtProdCantidad.getText().toString());
                i.putExtra("idProducto", P.getId());
                //System.out.println(edtProdCantidad.getText().toString()+" "+P.getId().toString());
                setResult(ListaProductos.RESULT_OK, i);
                finish();
            }else{
                Toast.makeText(ListaProductos.this, "Por favor ingrese una cantidad", Toast.LENGTH_SHORT).show();
            }

            }

        });

    }




    public void obtenerProductosCatElegida(final String itemSeleccionado){
        ListView lista = (ListView) findViewById(R.id.lstProductos);
        Spinner spinner = (Spinner) findViewById(R.id.cmbProductosCategoria);
        ProductoRepository Repositorio = new ProductoRepository();
        List<Categoria> datosCategoria;
        datosCategoria = Repositorio.getCategorias();
        Categoria cat=datosCategoria.get(0);

        for (Categoria c: datosCategoria) {
            if(c.toString().equals(itemSeleccionado)){
               cat=c;
            }
        }
        final List<Producto> Datos = Repositorio.buscarPorCategoria(cat);
       /* final Categoria ca = cat;
       //System.out.println(ca);

        Thread r = new Thread() {
            @Override
            public void run() {

                try {
                    pDao = MyDatabase.getInstance(getApplicationContext()).getProductoDao();
                    List<Producto> listaP = pDao.getAll();
                    System.out.println(listaP.size());
                    for(Producto p : listaP){
                        if(p.getCategoria().getNombre().equals(ca.getNombre())){
                           Datos.add(p);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }};
        r.start();*/
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
       ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,Datos);
       lista.setAdapter(adaptador);
    }


}
