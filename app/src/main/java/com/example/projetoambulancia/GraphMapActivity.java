package com.example.projetoambulancia;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projetoambulancia.data.DataRepository;
import com.example.projetoambulancia.data.GraphData;

public class GraphMapActivity extends AppCompatActivity {

    private DataRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_graph_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_graph_map), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new DataRepository(this);
        GraphData data = repository.carregarGraphData();
        GraphMapView graphView = findViewById(R.id.graph_view);
        graphView.setData(data);
    }
}

