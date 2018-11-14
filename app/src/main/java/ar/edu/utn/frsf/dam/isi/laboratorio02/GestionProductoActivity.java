package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

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


        //Hilo para cargar categorias del API REST en el spinner
        final List<Categoria> datosCategoria = new ArrayList<>();
        final CategoriaRest apiRest = new CategoriaRest();

        ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,datosCategoria);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboCategorias.setAdapter(adaptador);
        comboCategorias.setSelection(0);

        Thread r = new Thread() {
            @Override
            public void run() {

                try {
                    List<Categoria> nuevasCat = apiRest.listarTodas();
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
                ProductoRetrofit clienteRest =
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
                });
            }

        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductoRetrofit clienteRest =
                        RestClient.getInstance()
                                .getRetrofit()
                                .create(ProductoRetrofit.class);
                if(flagActualizacion==false){
                    Categoria CatElegida = new Categoria();
                    CatElegida = (Categoria) comboCategorias.getSelectedItem();
                    Producto p = new Producto(nombreProducto.getText().toString(),
                            descProducto.getText().toString(),Double.valueOf(precioProducto.getText().toString()),CatElegida);
                    Call<Producto> altaCall= clienteRest.crearProducto(p);
                    altaCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call,
                                               Response<Producto> resp) {
                            // procesar la respuesta
                        }
                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                        }
                    });
                }else{
                    Categoria CatElegida = new Categoria();
                    CatElegida = (Categoria) comboCategorias.getSelectedItem();
                    productoBuscado = new Producto(nombreProducto.getText().toString(),descProducto.getText().toString(),
                            Double.valueOf(precioProducto.getText().toString()),CatElegida);

                    /*productoBuscado.setNombre(nombreProducto.getText().toString());
                    productoBuscado.setCategoria(CatElegida);
                    productoBuscado.setDescripcion(descProducto.getText().toString());
                    productoBuscado.setPrecio(Double.valueOf(precioProducto.getText().toString()));*/

                    Call<Producto> ActualizarCall= clienteRest.actualizarProducto(Integer.valueOf(idProductoBuscar.getText().toString()),productoBuscado);
                    ActualizarCall.enqueue(new Callback<Producto>() {
                        @Override
                        public void onResponse(Call<Producto> call,
                                               Response<Producto> resp) {
                        }
                        @Override
                        public void onFailure(Call<Producto> call, Throwable t) {
                        }
                    });

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
                ProductoRetrofit clienteRest =
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
                });
                //clienteRest.buscarProductoPorId(Integer.valueOf(idProductoBuscar.getText().toString()));
        }});
    }
}
