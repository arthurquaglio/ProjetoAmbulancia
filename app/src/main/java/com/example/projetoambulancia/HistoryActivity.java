package com.example.projetoambulancia;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projetoambulancia.data.Chamado;
import com.example.projetoambulancia.data.DataRepository;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private DataRepository repository;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_history), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new DataRepository(this);
        listView = findViewById(R.id.list_history);
        carregarLista();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }

    private void carregarLista() {
        List<Chamado> chamados = repository.listarChamados();
        List<String> linhas = new ArrayList<>();
        for (Chamado chamado : chamados) {
            String linha = chamado.getDataHora() + " | " + chamado.getPacienteNome()
                    + " (" + chamado.getPacienteIdade() + ") "
                    + "[" + chamado.getPrioridade() + "] "
                    + "| " + chamado.getBairroOrigem()
                    + " -> " + chamado.getUnidadeDestino() + " | " + chamado.getStatus();
            linhas.add(linha);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, linhas);
        listView.setAdapter(adapter);
    }
}
