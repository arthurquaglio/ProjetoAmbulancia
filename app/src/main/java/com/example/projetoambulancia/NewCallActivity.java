package com.example.projetoambulancia;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projetoambulancia.data.DataRepository;
import com.example.projetoambulancia.logic.ResultadoAtendimento;

import java.util.List;

public class NewCallActivity extends AppCompatActivity {

    private DataRepository repository;
    private Spinner spinnerBairros;
    private Spinner spinnerPrioridade;
    private EditText editPatientName;
    private EditText editPatientAge;
    private TextView textResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_call);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_new_call), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new DataRepository(this);
        spinnerBairros = findViewById(R.id.spinner_bairros);
        spinnerPrioridade = findViewById(R.id.spinner_priority);
        editPatientName = findViewById(R.id.edit_patient_name);
        editPatientAge = findViewById(R.id.edit_patient_age);
        textResultado = findViewById(R.id.text_resultado);
        Button buttonRegistrar = findViewById(R.id.button_registrar);

        List<String> bairros = repository.listarBairros();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, bairros);
        spinnerBairros.setAdapter(adapter);

        ArrayAdapter<String> prioridadesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Baixa", "Media", "Alta"});
        spinnerPrioridade.setAdapter(prioridadesAdapter);

        buttonRegistrar.setOnClickListener(v -> registrarChamado());
    }

    private void registrarChamado() {
        String bairroSelecionado = (String) spinnerBairros.getSelectedItem();
        String prioridade = (String) spinnerPrioridade.getSelectedItem();
        String nomePaciente = editPatientName.getText().toString().trim();
        String idadeTexto = editPatientAge.getText().toString().trim();

        if (bairroSelecionado == null) {
            textResultado.setText("Selecione um bairro.");
            return;
        }
        if (nomePaciente.isEmpty()) {
            textResultado.setText("Informe o nome do paciente.");
            return;
        }
        if (idadeTexto.isEmpty()) {
            textResultado.setText("Informe a idade do paciente.");
            return;
        }

        int idade;
        try {
            idade = Integer.parseInt(idadeTexto);
        } catch (NumberFormatException ex) {
            textResultado.setText("Idade invalida.");
            return;
        }

        ResultadoAtendimento resultado = repository.registrarChamado(
                bairroSelecionado, nomePaciente, idade, prioridade);
        if (resultado == null || resultado.getUnidade() == null) {
            textResultado.setText("Nao foi possivel encontrar unidade.");
            return;
        }

        String mensagem = "Paciente: " + nomePaciente
                + "\nDestino: " + resultado.getUnidade().getNome()
                + "\nBairro: " + resultado.getBairroDestino()
                + "\nMotivo: " + resultado.getMotivo()
                + "\nLeitos livres: " + resultado.getUnidade().getLeitosLivres();
        textResultado.setText(mensagem);
    }
}
