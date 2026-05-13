package com.example.projetoambulancia;

import com.example.projetoambulancia.data.GraphData;
import com.example.projetoambulancia.logic.AmbulanciaAtendimento;
import com.example.projetoambulancia.logic.ResultadoAtendimento;

import java.util.ArrayList;
import java.util.List;

public final class FixedTestRunner {

    private FixedTestRunner() {
    }

    public static final class UnidadeSeed {
        public final String nome;
        public final String tipo;
        public final String bairro;
        public final int total;
        public final int ocupados;

        public UnidadeSeed(String nome, String tipo, String bairro, int total, int ocupados) {
            this.nome = nome;
            this.tipo = tipo;
            this.bairro = bairro;
            this.total = total;
            this.ocupados = ocupados;
        }
    }

    public static final class TestCaseResult {
        public final String origem;
        public final String esperado;
        public final String encontrado;
        public final String motivo;
        public final String status;
        public final boolean ok;

        public TestCaseResult(String origem, String esperado, String encontrado,
                              String motivo, String status, boolean ok) {
            this.origem = origem;
            this.esperado = esperado;
            this.encontrado = encontrado;
            this.motivo = motivo;
            this.status = status;
            this.ok = ok;
        }
    }

    public static List<TestCaseResult> executarTestesDetalhado() {
        GraphData data = criarDados();
        AmbulanciaAtendimento atendimento = new AmbulanciaAtendimento();

        String[] bairrosTeste = getBairrosTeste();
        String[] esperados = getEsperados();

        List<TestCaseResult> resultados = new ArrayList<>();
        for (int i = 0; i < bairrosTeste.length; i++) {
            ResultadoAtendimento resultado = atendimento.encontrarHospital(bairrosTeste[i], data);
            String encontrado = resultado == null || resultado.getUnidade() == null
                    ? "(nenhum)" : resultado.getUnidade().getNome();
            boolean ok = encontrado.equals(esperados[i]);
            String motivo = resultado != null ? resultado.getMotivo() : "Sem resultado";
            String status = resultado != null ? resultado.getStatusRegistro() : "Sem status";
            resultados.add(new TestCaseResult(bairrosTeste[i], esperados[i], encontrado, motivo, status, ok));
        }
        return resultados;
    }

    public static String[] getBairrosTeste() {
        return new String[]{
                "Pinheiros", "Vila Madalena", "Butantã", "Lapa", "Perdizes",
                "Jaguaré", "Vila Sônia", "Rio Pequeno", "Pompéia", "Alto de Pinheiros"
        };
    }

    public static String[] getEsperados() {
        return new String[]{
                "Hospital Universitário USP",
                "Hospital Universitário USP",
                "Hospital Universitário USP",
                "Hospital Universitário USP",
                "Hospital São Camilo",
                "UPA Jaguaré",
                "Hospital Leforte",
                "UPA Rio Pequeno",
                "Hospital São Camilo",
                "Hospital Universitário USP"
        };
    }

    public static String[] getBairrosBase() {
        return new String[]{
                "Pinheiros", "Vila Madalena", "Butantã", "Lapa", "Perdizes",
                "Barra Funda", "Alto de Pinheiros", "Vila Leopoldina", "Jaguaré",
                "Vila Sônia", "Morumbi", "Rio Pequeno", "Pompéia", "Jardim Paulista", "Itaim Bibi"
        };
    }

    public static String[][] getConexoesBase() {
        return new String[][]{
                {"Pinheiros", "Vila Madalena"},
                {"Pinheiros", "Butantã"},
                {"Pinheiros", "Alto de Pinheiros"},
                {"Vila Madalena", "Perdizes"},
                {"Perdizes", "Lapa"},
                {"Lapa", "Pinheiros"},
                {"Lapa", "Barra Funda"},
                {"Butantã", "Jaguaré"},
                {"Jaguaré", "Vila Leopoldina"},
                {"Butantã", "Vila Sônia"},
                {"Vila Sônia", "Morumbi"},
                {"Morumbi", "Rio Pequeno"},
                {"Perdizes", "Pompéia"},
                {"Pinheiros", "Jardim Paulista"},
                {"Jardim Paulista", "Itaim Bibi"}
        };
    }

    public static List<UnidadeSeed> getUnidadesBase() {
        List<UnidadeSeed> unidades = new ArrayList<>();
        unidades.add(new UnidadeSeed("Hospital das Clínicas", "Hospital", "Pinheiros", 50, 50));
        unidades.add(new UnidadeSeed("UPA Vila Madalena", "UPA", "Vila Madalena", 20, 20));
        unidades.add(new UnidadeSeed("Hospital Universitário USP", "Hospital", "Butantã", 60, 45));
        unidades.add(new UnidadeSeed("UPA Lapa", "UPA", "Lapa", 30, 30));
        unidades.add(new UnidadeSeed("Hospital São Camilo", "Hospital", "Perdizes", 40, 39));
        unidades.add(new UnidadeSeed("Santa Casa Barra Funda", "Hospital", "Barra Funda", 35, 35));
        unidades.add(new UnidadeSeed("UPA Alto de Pinheiros", "UPA", "Alto de Pinheiros", 25, 25));
        unidades.add(new UnidadeSeed("Hospital Vila Penteado", "Hospital", "Vila Leopoldina", 30, 30));
        unidades.add(new UnidadeSeed("UPA Jaguaré", "UPA", "Jaguaré", 20, 18));
        unidades.add(new UnidadeSeed("Hospital Leforte", "Hospital", "Vila Sônia", 45, 44));
        unidades.add(new UnidadeSeed("Albert Einstein Morumbi", "Hospital", "Morumbi", 50, 50));
        unidades.add(new UnidadeSeed("UPA Rio Pequeno", "UPA", "Rio Pequeno", 30, 28));
        unidades.add(new UnidadeSeed("São Camilo Pompéia", "Hospital", "Pompéia", 40, 40));
        unidades.add(new UnidadeSeed("Sírio-Libanês Jardins", "Hospital", "Jardim Paulista", 60, 60));
        unidades.add(new UnidadeSeed("São Luiz Itaim", "Hospital", "Itaim Bibi", 55, 55));
        return unidades;
    }

    public static String getEnunciado() {
        return "Uma ambulância atende uma emergência e deve encontrar a unidade mais próxima com vaga, "
                + "iniciando no bairro atual e expandindo para adjacentes. "
                + "Limite de deslocamento: até 3 bairros. "
                + "Sem vagas, escolher a menos sobrecarregada dentre as analisadas.";
    }

    public static List<String> getPassos() {
        List<String> passos = new ArrayList<>();
        passos.add("1) Montar o grafo de bairros e adjacências.");
        passos.add("2) Para cada solicitação, iniciar a busca no bairro de origem.");
        passos.add("3) Se houver vaga, escolher a unidade com mais leitos livres na camada atual.");
        passos.add("4) Se não houver vaga, expandir a busca até 3 bairros (BFS).");
        passos.add("5) Sem vaga, escolher a unidade menos sobrecarregada.");
        passos.add("6) Exibir destino, motivo e status do encaminhamento.");
        return passos;
    }

    public static List<String> executarTestes() {
        List<TestCaseResult> detalhado = executarTestesDetalhado();
        List<String> resultados = new ArrayList<>();
        for (TestCaseResult item : detalhado) {
            String linha = "Solicitacao em " + item.origem
                    + " -> " + item.encontrado + " [" + (item.ok ? "OK" : "FALHOU") + "]";
            resultados.add(linha);
        }
        return resultados;
    }

    private static GraphData criarDados() {
        GraphData data = new GraphData();
        for (String bairro : getBairrosBase()) {
            data.adicionarBairro(bairro);
        }

        for (String[] conexao : getConexoesBase()) {
            data.conectarBairros(conexao[0], conexao[1]);
        }

        for (UnidadeSeed unidade : getUnidadesBase()) {
            data.adicionarUnidade(unidade.nome, unidade.tipo, unidade.bairro, unidade.total, unidade.ocupados);
        }

        return data;
    }
}

