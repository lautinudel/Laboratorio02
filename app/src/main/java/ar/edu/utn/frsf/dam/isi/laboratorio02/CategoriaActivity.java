package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // completer el codigo en el paso “f”
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
