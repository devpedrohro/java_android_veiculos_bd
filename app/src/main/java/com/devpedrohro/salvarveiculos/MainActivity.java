package com.devpedrohro.salvarveiculos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebHistoryItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText placa;
    private EditText marca;
    private EditText modelo;
    private Spinner tipo;
    private EditText ano;
    private Button salvar;
    private ListView placas;

    private SQLiteDatabase db;
    private ContentValues values;

    private final String DBNAME = "veiculos.db";
    private final String TBNAME = "tbVeiculos";
    private final String CLPLACA = "placa";
    private final String CLMARCA = "marca";
    private final String CLMODELO = "modelo";
    private final String CLTIPO = "tipo";
    private final String CLANO = "ano";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placa = findViewById(R.id.edtPlaca);
        marca = findViewById(R.id.edtMarca);
        modelo = findViewById(R.id.edtModelo);
        tipo = findViewById(R.id.spnTipo);
        ano = findViewById(R.id.edtAno);
        salvar = findViewById(R.id.btnSalvar);
        placas = findViewById(R.id.ltvPlacas);

        db = openOrCreateDatabase(DBNAME, MODE_PRIVATE, null);
        db.execSQL(String.format(
                "create table if not exists %1$s(%2$s varchar(7) primary key not null, %3$s varchar(60) not null, %4$s varchar(60) not null, %5$s varchar(60) not null, %6$s varchar(60) not null)",
                TBNAME, CLPLACA, CLMARCA, CLMODELO, CLTIPO, CLANO));

        values = new ContentValues();

        listAdapter();
    }

    public void salvarVeiculo(View view) {
        values.put(CLPLACA, placa.getText().toString());
        values.put(CLMARCA, marca.getText().toString());
        values.put(CLMODELO, modelo.getText().toString());
        values.put(CLTIPO, tipo.getSelectedItem().toString());
        values.put(CLANO, ano.getText().toString());

        db.insert(TBNAME, null, values);

        Toast.makeText(this, "Veículo Salvo Com Sucesso!!", Toast.LENGTH_LONG).show();

        placa.setText("");
        marca.setText("");
        modelo.setText("");
        ano.setText("");

        listAdapter();
    }

    public List<String> listar(){
        List list = new ArrayList();
        Cursor cursor = db.rawQuery(String.format(
                "select * from %1$s order by %2$s", TBNAME, CLPLACA), null);

        while (cursor.moveToNext()){
            list.add("Placa: " + cursor.getString(0).substring(0, 3) + " - " + cursor.getString(0).substring(3));
        }

        return list;
    }

    public void listAdapter(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listar());
        placas.setAdapter(adapter);
    }
}
