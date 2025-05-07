package com.example.ac2;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
                String horarioTexto = etHorario.getText().toString(); // espera algo tipo "23:11"

                try {
                    // Junta a data de hoje com a hora digitada
                    String dataHoje = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    String horarioCompleto = dataHoje + " " + horarioTexto;

                    horario = sdf.parse(horarioCompleto);
                } catch (ParseException e) {
                    Toast.makeText(this, "Formato de horário inválido! Use HH:mm", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!nome.isEmpty() && !descricao.isEmpty()) {
                    long resultado = databaseHelper.adicionarMedicamento(nome, horario, false);
                    if (resultado != -1) {
                        Toast.makeText(this, "Medicamento salvo!", Toast.LENGTH_SHORT).show();
                        etNome.setText("");
                        etDescricao.setText("");
                        etHorario.setText("");

                        agendarNotificacao(nome, horario);

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

    @SuppressLint("ScheduleExactAlarm")
    private void agendarNotificacao(String nome, Date horario) {
        Intent intent = new Intent(this, AppCanalComunicacao.class);
        intent.putExtra("nome", nome);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) horario.getTime(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, horario.getTime(), pendingIntent);
    }
}