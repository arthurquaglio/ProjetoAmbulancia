package com.example.projetoambulancia;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projetoambulancia.data.DataRepository;
import com.example.projetoambulancia.data.GraphData;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class GraphMapActivity extends AppCompatActivity {

    private DataRepository repository;
    private GraphMapView graphView;
    private View infoPanel;
    private TextView infoTitle;
    private TextView infoBody;

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

        MaterialToolbar toolbar = findViewById(R.id.top_app_bar);
        toolbar.setNavigationOnClickListener(v -> finish());

        repository = new DataRepository(this);
        GraphData data = repository.carregarGraphData();
        graphView = findViewById(R.id.graph_view);
        graphView.setData(data);

        infoPanel = findViewById(R.id.graph_info_panel);
        infoTitle = findViewById(R.id.text_info_title);
        infoBody = findViewById(R.id.text_info_body);
        findViewById(R.id.button_info_close).setOnClickListener(v -> {
            infoPanel.setVisibility(View.GONE);
            graphView.clearSelection();
        });

        graphView.setOnBairroSelectedListener((bairroId, screenX, screenY) -> {
            if (bairroId == null) {
                infoPanel.setVisibility(View.GONE);
                return;
            }

            String title = data.getBairroNomePorId(bairroId);
            infoTitle.setText(title != null ? title : "Detalhes");
            infoBody.setText(buildInfoText(data, bairroId));
            mostrarPainelProximo(screenX, screenY);
        });
    }

    private String buildInfoText(GraphData data, int bairroId) {
        List<com.example.projetoambulancia.data.UnidadeSaude> unidades = data.getUnidadesPorBairro(bairroId);
        if (unidades.isEmpty()) {
            return "Sem unidades cadastradas.";
        }
        StringBuilder builder = new StringBuilder();
        for (com.example.projetoambulancia.data.UnidadeSaude unidade : unidades) {
            builder.append(unidade.getNome())
                    .append(" (")
                    .append(unidade.getTipo())
                    .append(")\n")
                    .append("Leitos: ")
                    .append(unidade.getLeitosOcupados())
                    .append("/")
                    .append(unidade.getLeitosTotais())
                    .append(" | Livres: ")
                    .append(unidade.getLeitosLivres())
                    .append("\n\n");
        }
        return builder.toString().trim();
    }

    private void mostrarPainelProximo(float x, float y) {
        infoPanel.setVisibility(View.VISIBLE);
        infoPanel.post(() -> {
            View container = findViewById(R.id.graph_container);
            int maxX = container.getWidth();
            int maxY = container.getHeight();

            int panelWidth = infoPanel.getWidth();
            int panelHeight = infoPanel.getHeight();
            int margin = (int) getResources().getDimension(R.dimen.space_m);

            int left = (int) Math.min(Math.max(x + margin, margin), maxX - panelWidth - margin);
            int top = (int) Math.min(Math.max(y + margin, margin), maxY - panelHeight - margin);

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) infoPanel.getLayoutParams();
            params.leftMargin = left;
            params.topMargin = top;
            params.gravity = Gravity.TOP | Gravity.START;
            infoPanel.setLayoutParams(params);
        });
    }
}
