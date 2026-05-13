package com.example.projetoambulancia;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projetoambulancia.data.DataRepository;
import com.example.projetoambulancia.data.UnidadeSaude;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class OccupancyActivity extends AppCompatActivity {

    private DataRepository repository;
    private RecyclerView recyclerView;
    private OccupancyAdapter adapter;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable refreshTask = this::carregarLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_occupancy);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_occupancy), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.top_app_bar);
        toolbar.setNavigationOnClickListener(v -> finish());

        repository = new DataRepository(this);
        recyclerView = findViewById(R.id.list_occupancy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OccupancyAdapter();
        recyclerView.setAdapter(adapter);
        carregarLista();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
        handler.postDelayed(refreshTask, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(refreshTask);
    }

    private void carregarLista() {
        List<UnidadeSaude> unidades = repository.listarUnidadesComBairro();
        unidades.sort((a, b) -> {
            int diff = Integer.compare(b.getLeitosLivres(), a.getLeitosLivres());
            if (diff != 0) {
                return diff;
            }
            return a.getNome().compareToIgnoreCase(b.getNome());
        });
        adapter.submitList(unidades);
        handler.removeCallbacks(refreshTask);
        handler.postDelayed(refreshTask, 3000);
    }
}
