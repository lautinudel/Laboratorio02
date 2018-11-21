package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.CategoriaDao;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.CategoriaDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.MyDatabase;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class CategoriaActivity extends AppCompatActivity {
    private EditText textoCat;
    private Button btnCrear;
    private Button btnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        textoCat = (EditText) findViewById(R.id.txtNombreCategoria);
        btnCrear = (Button) findViewById(R.id.btnCrearCategoria);
        //final CategoriaDao cDao = MyDatabase.getInstance(this).getCategoriaDao();
        final Categoria nuevaCat = new Categoria();
        btnCrear.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               nuevaCat.setNombre(textoCat.getText().toString());
              //cDao.insert(nuevaCat);
               Toast.makeText(getApplicationContext(),"La categoria "+textoCat.getText().toString()+" se ha creado",Toast.LENGTH_SHORT).show();
               textoCat.setText("");
                /*Thread r = new Thread() {
                    @Override
                    public void run() {
                        CategoriaRest cr = new CategoriaRest();
                        try {
                            cr.crearCategoria(new Categoria(textoCat.getText().toString()));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"La categoria "+textoCat.getText().toString()+" se ha creado",Toast.LENGTH_SHORT).show();
                                textoCat.setText("");
                            }
                        });
                    }};
                r.start();*/
                }
        });
        btnMenu= (Button) findViewById(R.id.btnCategoriaVolver);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent i = new Intent(CategoriaActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
