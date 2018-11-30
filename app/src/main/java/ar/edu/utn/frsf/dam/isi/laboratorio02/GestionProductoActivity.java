package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.CategoriaDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRetrofit;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GestionProductoActivity extends AppCompatActivity {

    private Button btnMenu;
    private Button btnGuardar;
    private Spinner comboCategorias;
    private EditText nombreProducto;
    private EditText descProducto;
    private EditText precioProducto;
    private ToggleButton opcionNuevoBusqueda;
    private EditText idProductoBuscar;
    private Button btnBuscar;
    private Button btnBorrar;
    private Boolean flagActualizacion;
    private ArrayAdapter<Categoria> comboAdapter;
    private Producto productoBuscado;
    private CategoriaDao cDao;
    private ProductoDao pDao;
    private Producto aux;
    private Categoria catSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_producto);

        //Definiciones
        flagActualizacion = false;
        opcionNuevoBusqueda = (ToggleButton)
                findViewById(R.id.abmProductoAltaNuevo);
        idProductoBuscar = (EditText)
                findViewById(R.id.abmProductoIdBuscar);
        nombreProducto = (EditText)
                findViewById(R.id.abmProductoNombre);
        descProducto = (EditText)
                findViewById(R.id.abmProductoDescripcion);
        precioProducto = (EditText)
                findViewById(R.id.abmProductoPrecio);
        comboCategorias = (Spinner)
                findViewById(R.id.abmProductoCategoria);
        btnMenu = (Button) findViewById(R.id.btnAbmProductoVolver);
        btnGuardar = (Button)
                findViewById(R.id.btnAbmProductoCrear);
        btnBuscar = (Button)
                findViewById(R.id.btnAbmProductoBuscar);
        btnBorrar= (Button)
                findViewById(R.id.btnAbmProductoBorrar);
        opcionNuevoBusqueda.setChecked(false);
        btnBuscar.setEnabled(false);
        btnBorrar.setEnabled(false);
        idProductoBuscar.setEnabled(false);


        //Hilo para cargar categorias del API REST / ROOM en el spinner
        final List<Categoria> datosCategoria = new ArrayList<Categoria>();
        //final CategoriaRest apiRest = new CategoriaRest();
        ProductoRepository Repositorio = new ProductoRepository();
        datosCategoria.addAll(Repositorio.getCategorias());
        final ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,datosCategoria);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboCategorias.setAdapter(adaptador);
        comboCategorias.setSelection(0);

        comboCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catSelected = (Categoria) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Thread r = new Thread() {
            @Override
            public void run() {

                try {
                    //List<Categoria> nuevasCat = apiRest.listarTodas();
                    pDao = MyDatabase.getInstance(getApplicationContext()).getProductoDao();
                    cDao = MyDatabase.getInstance(getApplicationContext()).getCategoriaDao();
                    List<Categoria> nuevasCat = cDao.getAll();
                    List<Categoria> aux = new ArrayList<>();
                    // System.out.println(nuevasCat.size());
                    for (Categoria c : datosCategoria){
                        for(Categoria d : nuevasCat){
                            if(c.getNombre().equals(d.getNombre())){
                                aux.add(d); //LOS REPETIDOS
                            }
                        }

                    }
                    nuevasCat.removeAll(aux);
                    System.out.println("Nuevas categorias: "+ nuevasCat.size());
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


        //Métodos
        opcionNuevoBusqueda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView,
                                        boolean isChecked) {
               flagActualizacion=isChecked;
               btnBuscar.setEnabled(isChecked);
               btnBorrar.setEnabled(isChecked);
               idProductoBuscar.setEnabled(isChecked);
           }
       });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread r = new Thread() {
                    @Override
                    public void run() {

                        try {

                            List<Producto> listaP = pDao.getAll();
                            for(Producto p : listaP){
                                if(p.getId()==Integer.valueOf(idProductoBuscar.getText().toString())){
                                    pDao.delete(p);
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
                r.start();
                /*ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                Call<Producto> borrarCall= clienteRest.borrar(Integer.valueOf(idProductoBuscar.getText().toString()));
                borrarCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call,
                                           Response<Producto> resp) {
                        // procesar la respuesta
                    }
                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                    }
                });*/
            }

        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);*/
                if(flagActualizacion==false){

                    Thread r = new Thread() {
                        @Override
                        public void run() {

                            try {

                                List<Producto> listaP = pDao.getAll();
                                Categoria CatElegida = new Categoria();
                                CatElegida = (Categoria) comboCategorias.getSelectedItem();
                                Producto p = new Producto(nombreProducto.getText().toString(),
                                        descProducto.getText().toString(),Double.valueOf(precioProducto.getText().toString()),CatElegida);
                                pDao.insert(p);
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
                    /*Call<Producto> altaCall= clienteRest.crearProducto(p);
                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call,
                                               Response<Producto> resp) {
                            // procesar la respuesta
                        }
                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                        }
                    });*/
                }else{

                    Thread r = new Thread() {
                        @Override
                        public void run() {

                            try {

                                //Categoria CatElegida = new Categoria();
                                //CatElegida = (Categoria) comboCategorias.getSelectedItem();
                                //productoBuscado = new Producto(nombreProducto.getText().toString(),descProducto.getText().toString(),
                                       // Double.valueOf(precioProducto.getText().toString()),CatElegida);
                                aux.setNombre(nombreProducto.getText().toString());
                                aux.setDescripcion(descProducto.getText().toString());
                                aux.setPrecio(Double.valueOf(precioProducto.getText().toString()));
                                aux.setCategoria(catSelected);
                                pDao.update(aux);
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
                    /*productoBuscado.setNombre(nombreProducto.getText().toString());
                    productoBuscado.setCategoria(CatElegida);
                    productoBuscado.setDescripcion(descProducto.getText().toString());
                    productoBuscado.setPrecio(Double.valueOf(precioProducto.getText().toString()));*/

                    /*Call<Producto> ActualizarCall= clienteRest.actualizarProducto(Integer.valueOf(idProductoBuscar.getText().toString()),productoBuscado);
                    ActualizarCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call,
                                               Response<Producto> resp) {
                        }
                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                        }
                    });*/

                }

            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(GestionProductoActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Runnable buscarPorId = new Runnable() {
                    @Override
                    public void run() {
                        aux=null;

                        List<Producto> listaP = pDao.getAll();
                        if(!idProductoBuscar.getText().toString().isEmpty()){
                        for(Producto p : listaP){
                            if(p.getId()==Integer.valueOf(idProductoBuscar.getText().toString())){
                                aux=p;
                            }
                        }}


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (aux != null) {
                                    int i=0;
                                    nombreProducto.setText(aux.getNombre());
                                    descProducto.setText(aux.getDescripcion());
                                    precioProducto.setText(aux.getPrecio().toString());
                                    btnBorrar.setEnabled(true);
                                    System.out.println(aux.getCategoria());
                                    for(Categoria c : datosCategoria){
                                        if(c.getNombre().equals(aux.getCategoria().getNombre()))
                                            comboCategorias.setSelection(i);
                                        i++;
                                    }
                                } else {
                                    nombreProducto.setText("");
                                    descProducto.setText("");
                                    precioProducto.setText("");
                                    Toast.makeText(GestionProductoActivity.this, "EL producto no se encontro", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }

                };
                Thread hiloBuscarProd = new Thread(buscarPorId);
                hiloBuscarProd.start();


                /*ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                Call<Producto> buscarCall= clienteRest.buscarProductoPorId(Integer.valueOf(idProductoBuscar.getText().toString()));
                buscarCall.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Call<Producto> call,
                                           Response<Producto> resp) {
                        nombreProducto.setText(resp.body().getNombre());
                        descProducto.setText(resp.body().getDescripcion());
                        precioProducto.setText(resp.body().getPrecio().toString());
                    }
                    @Override
                    public void onFailure(Call<Producto> call, Throwable t) {
                    }
                });*/
                //clienteRest.buscarProductoPorId(Integer.valueOf(idProductoBuscar.getText().toString()));
        }});
    }
}
