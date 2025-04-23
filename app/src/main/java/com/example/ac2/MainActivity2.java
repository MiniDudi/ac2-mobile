package com.example.ac2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    EditText etNome, etDescricao, etHorario;
    Button btnSalvar, btnVoltar;
    BancoHelper databaseHelper;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    Date horario = null;

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
        setContentView(R.layout.activity_main2);

        try {
            etNome = findViewById(R.id.etNome);
            etDescricao = findViewById(R.id.etDescricao);
            etHorario = findViewById(R.id.etHorario);

            btnSalvar = findViewById(R.id.btnSave);
            btnVoltar = findViewById(R.id.btnBack);

            databaseHelper = new BancoHelper(this);

            btnSalvar.setOnClickListener(v -> {
                String nome = etNome.getText().toString();
                String descricao = etDescricao.getText().toString();
                String horarioTexto = etHorario.getText().toString();
                try {
                    horario = sdf.parse(horarioTexto);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (!nome.isEmpty() && !descricao.isEmpty()) {
                    long resultado = databaseHelper.adicionarMedicamento(nome, horario, false);
                    if (resultado != -1) {
                        Toast.makeText(this, "Medicamento salvo!", Toast.LENGTH_SHORT).show();
                        etNome.setText("");
                        etDescricao.setText("");
                        etHorario.setText("");

                        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}