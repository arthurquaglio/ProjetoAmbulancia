package com.example.projetoambulancia.data;

public class UnidadeSaude {

    private final int id;
    private final String nome;
    private final String tipo;
    private final int bairroId;
    private final int leitosTotais;
    private final int leitosOcupados;
    private final String bairroNome;

    public UnidadeSaude(int id, String nome, String tipo, int bairroId,
                        int leitosTotais, int leitosOcupados, String bairroNome) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.bairroId = bairroId;
        this.leitosTotais = leitosTotais;
        this.leitosOcupados = leitosOcupados;
        this.bairroNome = bairroNome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public int getBairroId() {
        return bairroId;
    }

    public int getLeitosTotais() {
        return leitosTotais;
    }

    public int getLeitosOcupados() {
        return leitosOcupados;
    }

    public String getBairroNome() {
        return bairroNome;
    }

    public int getLeitosLivres() {
        return leitosTotais - leitosOcupados;
    }

    public double getTaxaOcupacao() {
        if (leitosTotais == 0) {
            return 1.0d;
        }
        return (double) leitosOcupados / (double) leitosTotais;
    }
}

