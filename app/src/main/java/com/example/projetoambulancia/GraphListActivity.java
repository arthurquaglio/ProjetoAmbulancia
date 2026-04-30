package com.example.projetoambulancia;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projetoambulancia.data.DataRepository;
import com.example.projetoambulancia.data.GraphData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphListActivity extends AppCompatActivity {

    private DataRepository repository;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_graph_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_graph_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new DataRepository(this);
        listView = findViewById(R.id.list_graph);
        carregarLista();
    }

    private void carregarLista() {
        GraphData data = repository.carregarGraphData();
        Map<String, List<String>> adjacencias = data.getAdjacenciasPorNome();
        List<String> linhas = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : adjacencias.entrySet()) {
            String linha = entry.getKey() + ": " + String.join(", ", entry.getValue());
            linhas.add(linha);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, linhas);
        listView.setAdapter(adapter);
    }
}

