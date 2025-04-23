package com.example.ac2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnSalvar, btnEdit, btnExclude, btnTomado;
    ListView listViewMedicamentos;

    BancoHelper databaseHelper;
    ArrayAdapter<String> adapter;

    ArrayList<String> listaMedicamentos;
    ArrayList<Integer> listaIds;

    int idSelecionado = -1;
    String nomeSelecionado = "";
    Date horarioSelecionado = null;
    Boolean consumoSelecionado = false;

    /*
        OLÁ PROFESSOR,
        INFELIZMENTE NÃO CONSEGUI TESTAR COM O CELULAR NEM O EMULADOR
        NOTEBOOK NÃO TANKOU E NÃO DEU PRA PAREAR :(

        DE QUALQUER FORMA, ESPERO QUE ESTEJA CORRETO
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        try {
            btnSalvar = findViewById(R.id.btnSalvar);
            btnEdit = findViewById(R.id.btnEdit);
            btnExclude = findViewById(R.id.btnExclude);
            btnTomado = findViewById(R.id.btnEditarTomado);
            listViewMedicamentos = findViewById(R.id.lvMedicamentos);

            databaseHelper = new BancoHelper(this);
            carregarMedicamentos();

            btnEdit.setEnabled(false);
            btnExclude.setEnabled(false);
            btnTomado.setEnabled(false);

            btnSalvar.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            });

            listViewMedicamentos.setOnItemClickListener((parent, view, position, id) -> {
                int userId = listaIds.get(position);
                String nome = listaMedicamentos.get(position).split(" - ")[1];
                String consumo = listaMedicamentos.get(position).split(" - ")[3];
                String horarioStr = listaMedicamentos.get(position).split(" - ")[2];
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                Date horario = null;
                try {
                    horario = sdf.parse(horarioStr);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                btnEdit.setEnabled(true);
                btnExclude.setEnabled(true);
                btnTomado.setEnabled(true);

                idSelecionado = userId;
                nomeSelecionado = nome;
                horarioSelecionado = horario;
                consumoSelecionado = Boolean.valueOf(consumo);
            });

            btnEdit.setOnClickListener(v -> {
                if (idSelecionado != -1) {
                    Intent intent = new Intent(MainActivity.this, EditarMedicamento.class);
                    intent.putExtra("id", idSelecionado);
                    intent.putExtra("nome", nomeSelecionado);
                    intent.putExtra("horario", horarioSelecionado);
                    intent.putExtra("consumo", consumoSelecionado);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Selecione um item primeiro", Toast.LENGTH_SHORT).show();
                }
            });

            btnExclude.setOnClickListener(v -> {
                int idMedicamento = listaIds.get(idSelecionado);
                int deletado = databaseHelper.excluirMedicamentos(idMedicamento);
                if (deletado > 0) {
                    Toast.makeText(this, "Medicamento excluído!", Toast.LENGTH_SHORT).show();
                    carregarMedicamentos();
                }
            });

            btnTomado.setOnClickListener(v -> {
                int idMedicamento = listaIds.get(idSelecionado);
                int atualizado = databaseHelper.atualizarMedicamentos(idMedicamento, nomeSelecionado, horarioSelecionado, !consumoSelecionado);
                if (atualizado > 0) {
                    Toast.makeText(this, "Mediamento atualizado!", Toast.LENGTH_SHORT).show();
                    carregarMedicamentos();
                }
            });

            } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void carregarMedicamentos() {
        Cursor cursor = databaseHelper.listarMedicamentos();
        listaMedicamentos = new ArrayList<>();
        listaIds = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nome = cursor.getString(1);
                String email = cursor.getString(2);
                listaMedicamentos.add(id + " - " + nome + " - " + email);
                listaIds.add(id);
            } while (cursor.moveToNext());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaMedicamentos);
        listViewMedicamentos.setAdapter(adapter);
    }

}