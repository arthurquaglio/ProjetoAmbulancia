package com.example.projetoambulancia.logic;

import com.example.projetoambulancia.data.UnidadeSaude;

public class ResultadoAtendimento {

    private final UnidadeSaude unidade;
    private final String bairroDestino;
    private final String motivo;
    private final boolean comVaga;
    private final int bairroOrigemId;

    public ResultadoAtendimento(UnidadeSaude unidade, String bairroDestino, String motivo,
                                boolean comVaga, int bairroOrigemId) {
        this.unidade = unidade;
        this.bairroDestino = bairroDestino;
        this.motivo = motivo;
        this.comVaga = comVaga;
        this.bairroOrigemId = bairroOrigemId;
    }

    public UnidadeSaude getUnidade() {
        return unidade;
    }

    public String getBairroDestino() {
        return bairroDestino;
    }

    public String getMotivo() {
        return motivo;
    }

    public boolean isComVaga() {
        return comVaga;
    }

    public int getBairroOrigemId() {
        return bairroOrigemId;
    }

    public String getStatusRegistro() {
        return comVaga ? "Encaminhado com vaga" : "Sem vaga - menor sobrecarga";
    }
}

