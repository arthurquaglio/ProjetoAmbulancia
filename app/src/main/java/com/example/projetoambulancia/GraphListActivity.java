package com.example.projetoambulancia;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projetoambulancia.data.DataRepository;
import com.example.projetoambulancia.data.GraphData;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphListActivity extends AppCompatActivity {

    private DataRepository repository;
    private RecyclerView recyclerView;
    private SimpleTextAdapter adapter;

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

        MaterialToolbar toolbar = findViewById(R.id.top_app_bar);
        toolbar.setNavigationOnClickListener(v -> finish());

        repository = new DataRepository(this);
        recyclerView = findViewById(R.id.list_graph);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleTextAdapter();
        recyclerView.setAdapter(adapter);
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
        adapter.submitList(linhas);
    }
}

