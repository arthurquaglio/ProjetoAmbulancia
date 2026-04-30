package com.example.projetoambulancia;

import com.example.projetoambulancia.data.GraphData;
import com.example.projetoambulancia.logic.AmbulanciaAtendimento;
import com.example.projetoambulancia.logic.ResultadoAtendimento;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AmbulanciaAtendimentoTest {

    @Test
    public void deveExecutarCasosFixos() {
        GraphData data = criarDados();
        AmbulanciaAtendimento atendimento = new AmbulanciaAtendimento();

        String[] bairrosTeste = {
                "Pinheiros", "Vila Madalena", "Butantã", "Lapa", "Perdizes",
                "Jaguaré", "Vila Sônia", "Rio Pequeno", "Pompéia", "Alto de Pinheiros"
        };
        String[] esperados = {
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

        for (int i = 0; i < bairrosTeste.length; i++) {
            ResultadoAtendimento resultado = atendimento.encontrarHospital(bairrosTeste[i], data);
            assertNotNull(resultado);
            assertNotNull(resultado.getUnidade());
            assertEquals(esperados[i], resultado.getUnidade().getNome());
        }
    }

    private GraphData criarDados() {
        GraphData data = new GraphData();
        String[] bairros = {
                "Pinheiros", "Vila Madalena", "Butantã", "Lapa", "Perdizes",
                "Barra Funda", "Alto de Pinheiros", "Vila Leopoldina", "Jaguaré",
                "Vila Sônia", "Morumbi", "Rio Pequeno", "Pompéia", "Jardim Paulista", "Itaim Bibi"
        };
        for (String bairro : bairros) {
            data.adicionarBairro(bairro);
        }

        data.conectarBairros("Pinheiros", "Vila Madalena");
        data.conectarBairros("Pinheiros", "Butantã");
        data.conectarBairros("Pinheiros", "Alto de Pinheiros");
        data.conectarBairros("Vila Madalena", "Perdizes");
        data.conectarBairros("Perdizes", "Lapa");
        data.conectarBairros("Lapa", "Pinheiros");
        data.conectarBairros("Lapa", "Barra Funda");
        data.conectarBairros("Butantã", "Jaguaré");
        data.conectarBairros("Jaguaré", "Vila Leopoldina");
        data.conectarBairros("Butantã", "Vila Sônia");
        data.conectarBairros("Vila Sônia", "Morumbi");
        data.conectarBairros("Morumbi", "Rio Pequeno");
        data.conectarBairros("Perdizes", "Pompéia");
        data.conectarBairros("Pinheiros", "Jardim Paulista");
        data.conectarBairros("Jardim Paulista", "Itaim Bibi");

        data.adicionarUnidade("Hospital das Clínicas", "Hospital", "Pinheiros", 50, 50);
        data.adicionarUnidade("UPA Vila Madalena", "UPA", "Vila Madalena", 20, 20);
        data.adicionarUnidade("Hospital Universitário USP", "Hospital", "Butantã", 60, 45);
        data.adicionarUnidade("UPA Lapa", "UPA", "Lapa", 30, 30);
        data.adicionarUnidade("Hospital São Camilo", "Hospital", "Perdizes", 40, 39);
        data.adicionarUnidade("Santa Casa Barra Funda", "Hospital", "Barra Funda", 35, 35);
        data.adicionarUnidade("UPA Alto de Pinheiros", "UPA", "Alto de Pinheiros", 25, 25);
        data.adicionarUnidade("Hospital Vila Penteado", "Hospital", "Vila Leopoldina", 30, 30);
        data.adicionarUnidade("UPA Jaguaré", "UPA", "Jaguaré", 20, 18);
        data.adicionarUnidade("Hospital Leforte", "Hospital", "Vila Sônia", 45, 44);
        data.adicionarUnidade("Albert Einstein Morumbi", "Hospital", "Morumbi", 50, 50);
        data.adicionarUnidade("UPA Rio Pequeno", "UPA", "Rio Pequeno", 30, 28);
        data.adicionarUnidade("São Camilo Pompéia", "Hospital", "Pompéia", 40, 40);
        data.adicionarUnidade("Sírio-Libanês Jardins", "Hospital", "Jardim Paulista", 60, 60);
        data.adicionarUnidade("São Luiz Itaim", "Hospital", "Itaim Bibi", 55, 55);

        return data;
    }
}
