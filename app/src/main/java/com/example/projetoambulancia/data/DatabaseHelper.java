package com.example.projetoambulancia.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ambulancia.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE bairros (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL UNIQUE" +
                ");");

        db.execSQL("CREATE TABLE adjacencias (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "bairro_id INTEGER NOT NULL," +
                "adjacente_id INTEGER NOT NULL," +
                "FOREIGN KEY (bairro_id) REFERENCES bairros(id)," +
                "FOREIGN KEY (adjacente_id) REFERENCES bairros(id)" +
                ");");

        db.execSQL("CREATE TABLE unidades (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "tipo TEXT NOT NULL," +
                "bairro_id INTEGER NOT NULL," +
                "leitos_totais INTEGER NOT NULL," +
                "leitos_ocupados INTEGER NOT NULL," +
                "FOREIGN KEY (bairro_id) REFERENCES bairros(id)" +
                ");");

        db.execSQL("CREATE TABLE chamados (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "data_hora TEXT NOT NULL," +
                "bairro_origem_id INTEGER NOT NULL," +
                "unidade_destino_id INTEGER NOT NULL," +
                "paciente_nome TEXT NOT NULL," +
                "paciente_idade INTEGER NOT NULL," +
                "prioridade TEXT NOT NULL," +
                "status TEXT NOT NULL," +
                "FOREIGN KEY (bairro_origem_id) REFERENCES bairros(id)," +
                "FOREIGN KEY (unidade_destino_id) REFERENCES unidades(id)" +
                ");");

        seedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chamados");
        db.execSQL("DROP TABLE IF EXISTS unidades");
        db.execSQL("DROP TABLE IF EXISTS adjacencias");
        db.execSQL("DROP TABLE IF EXISTS bairros");
        onCreate(db);
    }

    private void seedData(SQLiteDatabase db) {
        Map<String, Long> bairroIds = new HashMap<>();
        String[] bairros = {
                "Pinheiros", "Vila Madalena", "Butantã", "Lapa", "Perdizes",
                "Barra Funda", "Alto de Pinheiros", "Vila Leopoldina", "Jaguaré",
                "Vila Sônia", "Morumbi", "Rio Pequeno", "Pompéia", "Jardim Paulista", "Itaim Bibi"
        };
        for (String bairro : bairros) {
            ContentValues values = new ContentValues();
            values.put("nome", bairro);
            long id = db.insert("bairros", null, values);
            bairroIds.put(bairro, id);
        }

        conectar(db, bairroIds, "Pinheiros", "Vila Madalena");
        conectar(db, bairroIds, "Pinheiros", "Butantã");
        conectar(db, bairroIds, "Pinheiros", "Alto de Pinheiros");
        conectar(db, bairroIds, "Vila Madalena", "Perdizes");
        conectar(db, bairroIds, "Perdizes", "Lapa");
        conectar(db, bairroIds, "Lapa", "Pinheiros");
        conectar(db, bairroIds, "Lapa", "Barra Funda");
        conectar(db, bairroIds, "Butantã", "Jaguaré");
        conectar(db, bairroIds, "Jaguaré", "Vila Leopoldina");
        conectar(db, bairroIds, "Butantã", "Vila Sônia");
        conectar(db, bairroIds, "Vila Sônia", "Morumbi");
        conectar(db, bairroIds, "Morumbi", "Rio Pequeno");
        conectar(db, bairroIds, "Perdizes", "Pompéia");
        conectar(db, bairroIds, "Pinheiros", "Jardim Paulista");
        conectar(db, bairroIds, "Jardim Paulista", "Itaim Bibi");

        inserirUnidade(db, bairroIds, "Hospital das Clínicas", "Hospital", "Pinheiros", 50, 40);
        inserirUnidade(db, bairroIds, "UPA Vila Madalena", "UPA", "Vila Madalena", 20, 12);
        inserirUnidade(db, bairroIds, "Hospital Universitário USP", "Hospital", "Butantã", 60, 38);
        inserirUnidade(db, bairroIds, "UPA Lapa", "UPA", "Lapa", 30, 20);
        inserirUnidade(db, bairroIds, "Hospital São Camilo", "Hospital", "Perdizes", 40, 28);
        inserirUnidade(db, bairroIds, "Santa Casa Barra Funda", "Hospital", "Barra Funda", 35, 25);
        inserirUnidade(db, bairroIds, "UPA Alto de Pinheiros", "UPA", "Alto de Pinheiros", 25, 15);
        inserirUnidade(db, bairroIds, "Hospital Vila Penteado", "Hospital", "Vila Leopoldina", 30, 18);
        inserirUnidade(db, bairroIds, "UPA Jaguaré", "UPA", "Jaguaré", 20, 10);
        inserirUnidade(db, bairroIds, "Hospital Leforte", "Hospital", "Vila Sônia", 45, 30);
        inserirUnidade(db, bairroIds, "Albert Einstein Morumbi", "Hospital", "Morumbi", 50, 35);
        inserirUnidade(db, bairroIds, "UPA Rio Pequeno", "UPA", "Rio Pequeno", 30, 18);
        inserirUnidade(db, bairroIds, "São Camilo Pompéia", "Hospital", "Pompéia", 40, 26);
        inserirUnidade(db, bairroIds, "Sírio-Libanês Jardins", "Hospital", "Jardim Paulista", 60, 42);
        inserirUnidade(db, bairroIds, "São Luiz Itaim", "Hospital", "Itaim Bibi", 55, 40);
    }

    private void conectar(SQLiteDatabase db, Map<String, Long> bairroIds, String a, String b) {
        inserirAdjacencia(db, bairroIds.get(a), bairroIds.get(b));
        inserirAdjacencia(db, bairroIds.get(b), bairroIds.get(a));
    }

    private void inserirAdjacencia(SQLiteDatabase db, long bairroId, long adjacenteId) {
        ContentValues values = new ContentValues();
        values.put("bairro_id", bairroId);
        values.put("adjacente_id", adjacenteId);
        db.insert("adjacencias", null, values);
    }

    private void inserirUnidade(SQLiteDatabase db, Map<String, Long> bairroIds, String nome, String tipo,
                                String bairroNome, int leitosTotais, int leitosOcupados) {
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("tipo", tipo);
        values.put("bairro_id", bairroIds.get(bairroNome));
        values.put("leitos_totais", leitosTotais);
        values.put("leitos_ocupados", leitosOcupados);
        db.insert("unidades", null, values);
    }

    public boolean temDados(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM bairros", null);
        boolean tem = false;
        if (cursor.moveToFirst()) {
            tem = cursor.getInt(0) > 0;
        }
        cursor.close();
        return tem;
    }
}
