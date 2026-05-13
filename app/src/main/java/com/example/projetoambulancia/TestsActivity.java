package com.example.projetoambulancia;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class TestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TestResultAdapter adapter;
    private TextView textSummary;
    private TextView textMessage;
    private TextView textEnunciado;
    private TextView textSteps;
    private GraphMapView graphView;
    private final android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final List<Runnable> buildSteps = new ArrayList<>();
    private int buildStepIndex;
    private com.example.projetoambulancia.data.GraphData buildData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_tests), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.top_app_bar);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.list_tests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TestResultAdapter();
        recyclerView.setAdapter(adapter);

        textSummary = findViewById(R.id.text_tests_summary);
        textMessage = findViewById(R.id.text_tests_message);
        textEnunciado = findViewById(R.id.text_tests_enunciado);
        textSteps = findViewById(R.id.text_tests_steps);
        graphView = findViewById(R.id.graph_tests_view);

        textEnunciado.setText(FixedTestRunner.getEnunciado());
        textSteps.setText(joinLines(FixedTestRunner.getPassos()));

        Button buttonRun = findViewById(R.id.button_run_tests);
        buttonRun.setOnClickListener(v -> executar());
        graphView.post(this::executar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void executar() {
        textMessage.setText("Executando testes e montando grafo...");

        List<FixedTestRunner.TestCaseResult> resultados = FixedTestRunner.executarTestesDetalhado();
        List<TestResultAdapter.TestResult> parsed = new ArrayList<>();
        int okCount = 0;
        for (FixedTestRunner.TestCaseResult item : resultados) {
            if (item.ok) {
                okCount++;
            }
            String title = "Origem: " + item.origem;
            String detail = "Esperado: " + item.esperado
                    + "\nEncontrado: " + item.encontrado
                    + "\nMotivo: " + item.motivo
                    + "\nStatus: " + item.status;
            String status = item.ok ? "OK" : "FALHOU";
            parsed.add(new TestResultAdapter.TestResult(title, detail, status, item.ok));
        }
        int total = resultados.size();
        int failCount = total - okCount;
        textSummary.setText("Resultados: " + okCount + " OK • " + failCount + " falhou");
        textMessage.setText("Ultima execucao: " + formatNow());
        adapter.submitList(parsed);

        startGraphBuild();
    }

    private void startGraphBuild() {
        handler.removeCallbacksAndMessages(null);
        buildData = new com.example.projetoambulancia.data.GraphData();
        graphView.post(() -> graphView.setData(buildData));

        buildSteps.clear();
        buildStepIndex = 0;

        for (String bairro : FixedTestRunner.getBairrosBase()) {
            buildSteps.add(() -> {
                buildData.adicionarBairro(bairro);
                graphView.setData(buildData);
            });
        }
        for (String[] conexao : FixedTestRunner.getConexoesBase()) {
            buildSteps.add(() -> {
                buildData.conectarBairros(conexao[0], conexao[1]);
                graphView.setData(buildData);
            });
        }
        for (FixedTestRunner.UnidadeSeed unidade : FixedTestRunner.getUnidadesBase()) {
            buildSteps.add(() -> {
                buildData.adicionarUnidade(unidade.nome, unidade.tipo, unidade.bairro, unidade.total, unidade.ocupados);
                graphView.setData(buildData);
            });
        }

        runNextBuildStep();
    }

    private void runNextBuildStep() {
        if (buildStepIndex >= buildSteps.size()) {
            return;
        }
        buildSteps.get(buildStepIndex++).run();
        handler.postDelayed(this::runNextBuildStep, 220);
    }

    private String joinLines(List<String> linhas) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < linhas.size(); i++) {
            builder.append(linhas.get(i));
            if (i < linhas.size() - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private String formatNow() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM HH:mm", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
}
