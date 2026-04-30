package com.example.projetoambulancia.data;

public class Chamado {

    private final int id;
    private final String dataHora;
    private final String bairroOrigem;
    private final String unidadeDestino;
    private final String pacienteNome;
    private final int pacienteIdade;
    private final String prioridade;
    private final String status;

    public Chamado(int id, String dataHora, String bairroOrigem, String unidadeDestino,
                   String pacienteNome, int pacienteIdade, String prioridade, String status) {
        this.id = id;
        this.dataHora = dataHora;
        this.bairroOrigem = bairroOrigem;
        this.unidadeDestino = unidadeDestino;
        this.pacienteNome = pacienteNome;
        this.pacienteIdade = pacienteIdade;
        this.prioridade = prioridade;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getDataHora() {
        return dataHora;
    }

    public String getBairroOrigem() {
        return bairroOrigem;
    }

    public String getUnidadeDestino() {
        return unidadeDestino;
    }

    public String getPacienteNome() {
        return pacienteNome;
    }

    public int getPacienteIdade() {
        return pacienteIdade;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public String getStatus() {
        return status;
    }
}
