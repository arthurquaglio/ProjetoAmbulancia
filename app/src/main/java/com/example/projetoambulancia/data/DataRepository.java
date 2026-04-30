package com.example.projetoambulancia.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projetoambulancia.logic.AmbulanciaAtendimento;
import com.example.projetoambulancia.logic.ResultadoAtendimento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DataRepository {

    private final DatabaseHelper dbHelper;
    private final AmbulanciaAtendimento atendimento;

    public DataRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context.getApplicationContext());
        this.atendimento = new AmbulanciaAtendimento();
    }

    public List<String> listarBairros() {
        List<String> bairros = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nome FROM bairros ORDER BY nome", null);
        while (cursor.moveToNext()) {
            bairros.add(cursor.getString(0));
        }
        cursor.close();
        return bairros;
    }

    public List<UnidadeSaude> listarUnidadesComBairro() {
        List<UnidadeSaude> unidades = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT u.id, u.nome, u.tipo, u.bairro_id, u.leitos_totais, u.leitos_ocupados, b.nome " +
                "FROM unidades u JOIN bairros b ON b.id = u.bairro_id ORDER BY b.nome, u.nome";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            unidades.add(new UnidadeSaude(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getString(6)
            ));
        }
        cursor.close();
        return unidades;
    }

    public List<Chamado> listarChamados() {
        List<Chamado> chamados = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT c.id, c.data_hora, b.nome, u.nome, c.paciente_nome, c.paciente_idade, c.prioridade, c.status " +
                "FROM chamados c " +
                "JOIN bairros b ON b.id = c.bairro_origem_id " +
                "JOIN unidades u ON u.id = c.unidade_destino_id " +
                "ORDER BY c.id DESC";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            chamados.add(new Chamado(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getString(6),
                    cursor.getString(7)
            ));
        }
        cursor.close();
        return chamados;
    }

    public ResultadoAtendimento registrarChamado(String bairroOrigem, String pacienteNome,
                                                  int pacienteIdade, String prioridade) {
        GraphData graphData = carregarGraphData();
        ResultadoAtendimento resultado = atendimento.encontrarHospital(bairroOrigem, graphData);
        if (resultado == null || resultado.getUnidade() == null) {
            return resultado;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int novaOcupacao = resultado.getUnidade().getLeitosOcupados();
        if (resultado.isComVaga()) {
            novaOcupacao = resultado.getUnidade().getLeitosOcupados() + 1;
        }
        atualizarOcupacao(db, resultado.getUnidade().getId(), novaOcupacao);

        ContentValues values = new ContentValues();
        values.put("data_hora", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        values.put("bairro_origem_id", resultado.getBairroOrigemId());
        values.put("unidade_destino_id", resultado.getUnidade().getId());
        values.put("paciente_nome", pacienteNome);
        values.put("paciente_idade", pacienteIdade);
        values.put("prioridade", prioridade == null ? "Nao informado" : prioridade);
        values.put("status", resultado.getStatusRegistro());
        db.insert("chamados", null, values);

        return resultado;
    }

    private void atualizarOcupacao(SQLiteDatabase db, int unidadeId, int ocupados) {
        ContentValues values = new ContentValues();
        values.put("leitos_ocupados", ocupados);
        db.update("unidades", values, "id = ?", new String[]{String.valueOf(unidadeId)});
    }

    public GraphData carregarGraphData() {
        GraphData graphData = new GraphData();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor bairrosCursor = db.rawQuery("SELECT id, nome FROM bairros", null);
        Map<Integer, String> bairrosPorId = new HashMap<>();
        while (bairrosCursor.moveToNext()) {
            int id = bairrosCursor.getInt(0);
            String nome = bairrosCursor.getString(1);
            bairrosPorId.put(id, nome);
            graphData.adicionarBairro(nome);
        }
        bairrosCursor.close();

        Cursor adjCursor = db.rawQuery("SELECT bairro_id, adjacente_id FROM adjacencias", null);
        while (adjCursor.moveToNext()) {
            int bairroId = adjCursor.getInt(0);
            int adjId = adjCursor.getInt(1);
            String bairroNome = bairrosPorId.get(bairroId);
            String adjNome = bairrosPorId.get(adjId);
            if (bairroNome != null && adjNome != null) {
                graphData.adicionarAdjacenciaDireta(bairroNome, adjNome);
            }
        }
        adjCursor.close();

        Cursor unidadesCursor = db.rawQuery(
                "SELECT u.id, u.nome, u.tipo, u.bairro_id, u.leitos_totais, u.leitos_ocupados, b.nome " +
                        "FROM unidades u JOIN bairros b ON b.id = u.bairro_id", null);
        while (unidadesCursor.moveToNext()) {
            graphData.adicionarUnidadeComId(
                    unidadesCursor.getInt(0),
                    unidadesCursor.getString(1),
                    unidadesCursor.getString(2),
                    unidadesCursor.getString(6),
                    unidadesCursor.getInt(4),
                    unidadesCursor.getInt(5)
            );
        }
        unidadesCursor.close();

        return graphData;
    }
}
