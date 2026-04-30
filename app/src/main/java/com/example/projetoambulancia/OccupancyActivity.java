package com.example.projetoambulancia;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projetoambulancia.data.DataRepository;
import com.example.projetoambulancia.data.UnidadeSaude;

import java.util.ArrayList;
import java.util.List;

public class OccupancyActivity extends AppCompatActivity {

    private DataRepository repository;
    private ListView listView;
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

        repository = new DataRepository(this);
        listView = findViewById(R.id.list_occupancy);
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
        List<String> linhas = new ArrayList<>();
        for (UnidadeSaude unidade : unidades) {
            String linha = unidade.getNome() + " - " + unidade.getBairroNome()
                    + " | Livres: " + unidade.getLeitosLivres()
                    + " | Ocupados: " + unidade.getLeitosOcupados()
                    + "/" + unidade.getLeitosTotais();
            linhas.add(linha);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, linhas);
        listView.setAdapter(adapter);
        handler.removeCallbacks(refreshTask);
        handler.postDelayed(refreshTask, 3000);
    }
}
