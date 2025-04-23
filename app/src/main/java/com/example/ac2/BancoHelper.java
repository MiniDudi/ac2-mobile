package com.example.ac2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class BancoHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "meubanco.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "medicamentos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_HORARIO = "horario";
    private static final Boolean COLUMN_CONSUMO = Boolean.valueOf("consumo");


    public BancoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOME + " TEXT, "
                + COLUMN_HORARIO + " TEXT" +
                 COLUMN_CONSUMO + "TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long adicionarMedicamento(String nome, Date horario, Boolean consumo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, nome);
        values.put(COLUMN_HORARIO, horario.getTime());
        values.put(String.valueOf(COLUMN_CONSUMO), consumo);
        return db.insert(TABLE_NAME, null, values);
    }

    public Cursor listarMedicamentos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public int atualizarMedicamentos(int id, String nome, Date horario, Boolean consumo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, nome);
        values.put(COLUMN_HORARIO, horario.getTime());
        values.put(String.valueOf(COLUMN_CONSUMO), consumo);
        return db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int excluirMedicamentos(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

}
