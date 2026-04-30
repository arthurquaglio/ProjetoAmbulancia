package com.example.projetoambulancia.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphData {

    private final Map<String, Integer> bairroIdPorNome = new LinkedHashMap<>();
    private final Map<Integer, String> bairroNomePorId = new HashMap<>();
    private final Map<Integer, List<Integer>> adjacencias = new HashMap<>();
    private final Map<Integer, List<UnidadeSaude>> unidadesPorBairro = new HashMap<>();
    private int proximoBairroId = 1;
    private int proximoUnidadeId = 1;

    public int adicionarBairro(String nome) {
        Integer existente = bairroIdPorNome.get(nome);
        if (existente != null) {
            return existente;
        }
        int id = proximoBairroId++;
        bairroIdPorNome.put(nome, id);
        bairroNomePorId.put(id, nome);
        adjacencias.put(id, new ArrayList<>());
        return id;
    }

    public void conectarBairros(String nomeA, String nomeB) {
        int idA = adicionarBairro(nomeA);
        int idB = adicionarBairro(nomeB);
        adjacencias.get(idA).add(idB);
        adjacencias.get(idB).add(idA);
    }

    public void adicionarAdjacenciaDireta(String nomeA, String nomeB) {
        int idA = adicionarBairro(nomeA);
        int idB = adicionarBairro(nomeB);
        adjacencias.get(idA).add(idB);
    }

    public void adicionarUnidade(String nome, String tipo, String bairroNome,
                                 int leitosTotais, int leitosOcupados) {
        adicionarUnidadeComId(proximoUnidadeId++, nome, tipo, bairroNome, leitosTotais, leitosOcupados);
    }

    public void adicionarUnidadeComId(int id, String nome, String tipo, String bairroNome,
                                      int leitosTotais, int leitosOcupados) {
        int bairroId = adicionarBairro(bairroNome);
        UnidadeSaude unidade = new UnidadeSaude(
                id, nome, tipo, bairroId, leitosTotais, leitosOcupados, bairroNome);
        unidadesPorBairro.computeIfAbsent(bairroId, key -> new ArrayList<>()).add(unidade);
        if (id >= proximoUnidadeId) {
            proximoUnidadeId = id + 1;
        }
    }

    public Integer getBairroIdPorNome(String nome) {
        return bairroIdPorNome.get(nome);
    }

    public String getBairroNomePorId(int id) {
        return bairroNomePorId.get(id);
    }

    public Map<Integer, List<Integer>> getAdjacencias() {
        return adjacencias;
    }

    public List<UnidadeSaude> getUnidadesPorBairro(int bairroId) {
        List<UnidadeSaude> unidades = unidadesPorBairro.get(bairroId);
        if (unidades == null) {
            return Collections.emptyList();
        }
        return unidades;
    }

    public List<UnidadeSaude> getTodasUnidades() {
        List<UnidadeSaude> unidades = new ArrayList<>();
        for (List<UnidadeSaude> lista : unidadesPorBairro.values()) {
            unidades.addAll(lista);
        }
        return unidades;
    }

    public List<Integer> getBairrosOrdenados() {
        return new ArrayList<>(bairroIdPorNome.values());
    }

    public Map<String, List<String>> getAdjacenciasPorNome() {
        Map<String, List<String>> resultado = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : bairroIdPorNome.entrySet()) {
            int bairroId = entry.getValue();
            List<Integer> adj = adjacencias.get(bairroId);
            List<String> nomes = new ArrayList<>();
            if (adj != null) {
                for (int adjId : adj) {
                    String nome = bairroNomePorId.get(adjId);
                    if (nome != null) {
                        nomes.add(nome);
                    }
                }
            }
            resultado.put(entry.getKey(), nomes);
        }
        return resultado;
    }
}
