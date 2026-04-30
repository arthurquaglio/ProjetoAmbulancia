package com.example.projetoambulancia.logic;

import com.example.projetoambulancia.data.GraphData;
import com.example.projetoambulancia.data.UnidadeSaude;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AmbulanciaAtendimento {

    private static final int LIMITE_PROFUNDIDADE = 3;

    public ResultadoAtendimento encontrarHospital(String bairroOrigemNome, GraphData data) {
        Integer origemId = data.getBairroIdPorNome(bairroOrigemNome);
        if (origemId == null) {
            return null;
        }

        List<UnidadeSaude> origemDisponiveis = new ArrayList<>();
        for (UnidadeSaude unidade : data.getUnidadesPorBairro(origemId)) {
            if (unidade.getLeitosLivres() > 0) {
                origemDisponiveis.add(unidade);
            }
        }
        if (!origemDisponiveis.isEmpty()) {
            UnidadeSaude escolhida = origemDisponiveis.stream()
                    .max(comparadorMaisLivres())
                    .orElse(null);
            String bairroDestino = data.getBairroNomePorId(escolhida.getBairroId());
            return new ResultadoAtendimento(escolhida, bairroDestino,
                    "Encontrada vaga no bairro de origem", true, origemId);
        }

        List<Integer> camadaAtual = new ArrayList<>();
        camadaAtual.add(origemId);
        Set<Integer> visitados = new HashSet<>();
        visitados.add(origemId);
        List<Integer> bairrosAnalisados = new ArrayList<>();
        bairrosAnalisados.add(origemId);
        List<Candidato> candidatas = new ArrayList<>();

        for (int profundidade = 1; profundidade <= LIMITE_PROFUNDIDADE; profundidade++) {
            camadaAtual = expandirCamada(camadaAtual, data, visitados);
            if (camadaAtual.isEmpty()) {
                break;
            }

            bairrosAnalisados.addAll(camadaAtual);
            for (int bairroId : camadaAtual) {
                for (UnidadeSaude unidade : data.getUnidadesPorBairro(bairroId)) {
                    if (unidade.getLeitosLivres() > 0) {
                        candidatas.add(new Candidato(unidade, profundidade));
                    }
                }
            }
        }

        if (!candidatas.isEmpty()) {
            Candidato escolhido = candidatas.stream()
                    .max((a, b) -> comparadorMaisLivres().compare(a.unidade, b.unidade))
                    .orElse(null);
            UnidadeSaude unidade = escolhido.unidade;
            String bairroDestino = data.getBairroNomePorId(unidade.getBairroId());
            String motivo = "Encontrada vaga ate a profundidade " + escolhido.profundidade;
            return new ResultadoAtendimento(unidade, bairroDestino, motivo, true, origemId);
        }

        UnidadeSaude fallback = escolherMenosSobrecarregada(bairrosAnalisados, data);
        if (fallback == null) {
            return null;
        }
        String bairroDestino = data.getBairroNomePorId(fallback.getBairroId());
        String motivo = "Sem vaga ate a profundidade " + LIMITE_PROFUNDIDADE + ", escolhida menor sobrecarga";
        return new ResultadoAtendimento(fallback, bairroDestino, motivo, false, origemId);
    }

    private static class Candidato {
        private final UnidadeSaude unidade;
        private final int profundidade;

        private Candidato(UnidadeSaude unidade, int profundidade) {
            this.unidade = unidade;
            this.profundidade = profundidade;
        }
    }

    private List<Integer> expandirCamada(List<Integer> atual, GraphData data, Set<Integer> visitados) {
        List<Integer> proxima = new ArrayList<>();
        for (int bairroId : atual) {
            List<Integer> adjacentes = data.getAdjacencias().get(bairroId);
            if (adjacentes == null) {
                continue;
            }
            for (int adj : adjacentes) {
                if (visitados.add(adj)) {
                    proxima.add(adj);
                }
            }
        }
        return proxima;
    }

    private UnidadeSaude escolherMenosSobrecarregada(List<Integer> bairrosAnalisados, GraphData data) {
        List<UnidadeSaude> candidatas = new ArrayList<>();
        for (int bairroId : bairrosAnalisados) {
            candidatas.addAll(data.getUnidadesPorBairro(bairroId));
        }
        if (candidatas.isEmpty()) {
            return null;
        }
        return candidatas.stream().min(comparadorMenosSobrecarregada()).orElse(null);
    }

    private Comparator<UnidadeSaude> comparadorMaisLivres() {
        return Comparator
                .comparingInt(UnidadeSaude::getLeitosLivres)
                .thenComparingInt(this::prioridadeTipo)
                .thenComparing(UnidadeSaude::getNome);
    }

    private Comparator<UnidadeSaude> comparadorMenosSobrecarregada() {
        return Comparator
                .comparingDouble(UnidadeSaude::getTaxaOcupacao)
                .thenComparing(Comparator.comparingInt(UnidadeSaude::getLeitosLivres).reversed())
                .thenComparing(Comparator.comparingInt(this::prioridadeTipo).reversed())
                .thenComparing(UnidadeSaude::getNome);
    }

    private int prioridadeTipo(UnidadeSaude unidade) {
        if (unidade.getTipo() == null) {
            return 0;
        }
        String tipo = unidade.getTipo().toLowerCase();
        if (tipo.contains("hospital")) {
            return 2;
        }
        if (tipo.contains("upa")) {
            return 1;
        }
        return 0;
    }
}
