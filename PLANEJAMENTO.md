# Planejamento - Projeto Ambulancia

## Objetivo
Desenvolver um app Android em Java que encontre o hospital/upa mais adequado com base em um grafo de bairros, considerando disponibilidade de leitos e limite de deslocamento. Persistencia em SQLite (opcional no requisito, mas prevista no plano).

## Premissas (a confirmar)
- Plataforma: Android, Java (sem Kotlin).
- Banco: SQLite local.
- Sem API externa inicialmente (pode ser adicionada depois).
- Data de entrega/apresentacao: 14/05/2026.

## Escopo
### Funcionalidades obrigatorias
- Cadastro e consulta de bairros, hospitais/upas e leitos.
- Algoritmo em grafo para decidir destino da ambulancia.
- Tela de novo chamado (entrada de bairro, paciente, prioridade se aplicavel).
- Tela de lotacao em tempo real (atualizacao ao registrar chamados).
- Execucao do teste fornecido no enunciado.

### Funcionalidades opcionais
- Persistencia completa de chamados e historico.
- API REST e/ou mapa.

## Regras de negocio
1. Se houver vaga no bairro atual, escolher o hospital/upa local com mais leitos livres.
2. Se nao houver vaga no bairro atual, analisar os bairros adjacentes ate o limite de 3 bairros e escolher a unidade com mais leitos livres dentre todas as analisadas.
3. Se nao encontrar vaga em ate 3 bairros, escolher a unidade menos sobrecarregada dentre todas as analisadas.
4. Desempate: preferir tipo "Hospital" e depois ordenar por nome.
5. Menos sobrecarregada: menor taxa de ocupacao (ocupados/total); empate pelo maior numero de leitos livres.

## Dados de teste (fornecidos)
### Bairros
- Pinheiros
- Vila Madalena
- Butanta
- Lapa
- Perdizes
- Barra Funda
- Alto de Pinheiros
- Vila Leopoldina
- Jaguare
- Vila Sonia
- Morumbi
- Rio Pequeno
- Pompeia
- Jardim Paulista
- Itaim Bibi

### Adjacencias
- Pinheiros: Vila Madalena, Butanta, Alto de Pinheiros, Jardim Paulista
- Vila Madalena: Perdizes
- Perdizes: Lapa, Pompeia
- Lapa: Barra Funda, Pinheiros
- Butanta: Jaguare, Vila Sonia
- Jaguare: Vila Leopoldina
- Vila Sonia: Morumbi
- Morumbi: Rio Pequeno
- Jardim Paulista: Itaim Bibi

### Unidades de saude
- Hospital das Clinicas (Pinheiros, 50, 50)
- UPA Vila Madalena (Vila Madalena, 20, 20)
- Hospital Universitario USP (Butanta, 60, 45)
- UPA Lapa (Lapa, 30, 30)
- Hospital Sao Camilo (Perdizes, 40, 39)
- Santa Casa Barra Funda (Barra Funda, 35, 35)
- UPA Alto de Pinheiros (Alto de Pinheiros, 25, 25)
- Hospital Vila Penteado (Vila Leopoldina, 30, 30)
- UPA Jaguare (Jaguare, 20, 18)
- Hospital Leforte (Vila Sonia, 45, 44)
- Albert Einstein Morumbi (Morumbi, 50, 50)
- UPA Rio Pequeno (Rio Pequeno, 30, 28)
- Sao Camilo Pompeia (Pompeia, 40, 40)
- Sirio-Libanes Jardins (Jardim Paulista, 60, 60)
- Sao Luiz Itaim (Itaim Bibi, 55, 55)

### Casos de teste fixos
- Pinheiros -> Hospital Universitario USP
- Vila Madalena -> Hospital Universitario USP
- Butanta -> Hospital Universitario USP
- Lapa -> Hospital Universitario USP
- Perdizes -> Hospital Sao Camilo
- Jaguare -> UPA Jaguare
- Vila Sonia -> Hospital Leforte
- Rio Pequeno -> UPA Rio Pequeno
- Pompeia -> Hospital Sao Camilo
- Alto de Pinheiros -> Hospital Universitario USP

## Modelo de dados (rascunho)
- Bairro(id, nome)
- Adjacencia(bairroId, adjacenteId)
- UnidadeSaude(id, nome, tipo, bairroId, leitosTotais, leitosOcupados)
- Chamado(id, dataHora, bairroOrigemId, unidadeDestinoId, status)

## Algoritmo (rascunho)
- Representar o grafo com lista de adjacencia.
- Se houver vaga no bairro de origem, escolher a unidade com mais leitos livres.
- Caso contrario, BFS por camadas ate profundidade 3 e coletar todas as unidades com vaga.
- Escolher a unidade com mais leitos livres dentre todas as coletadas.
- Se nao encontrar vaga ate profundidade 3, escolher a unidade menos sobrecarregada.

## Telas
1. Novo chamado
   - Campos: bairro de origem, dados do paciente (minimo), botao de resolver.
   - Saida: unidade escolhida, motivo da escolha.
2. Lotacao em tempo real
   - Lista de unidades com leitos livres/ocupados.
   - Atualizacao ao registrar chamados.
3. (Opcional) Cadastro de dados base
   - Bairros, adjacencias e unidades.

## Entregaveis
- App Android (Java) com Activities, formularios e persistencia SQLite.
- Teste do algoritmo com dataset definido.
- Documentacao basica no README.

## Cronograma sugerido
- Semana 1: requisitos finais, modelo de dados, algoritmo.
- Semana 2: telas basicas e fluxo do chamado.
- Semana 3: persistencia SQLite e testes.
- Semana 4: ajustes, validacao e apresentacao.

## Checklist inicial
- [ ] Confirmar se nomes devem manter acentos na tela e no banco.
- [ ] Confirmar se o cadastro de bairros/unidades deve estar no app ou pre-carregado.
- [ ] Confirmar necessidade de API ou mapa.
- [ ] Definir campos minimos do chamado.
- [ ] Definir regra de desempate quando houver leitos iguais.
- [ ] Definir criterio de "menos sobrecarregado" (percentual ocupado ou absoluto de livres).

## Perguntas para confirmarmos o planejamento
1. Podemos armazenar os nomes no banco sem acentos (ex.: "Butanta") e exibir com acento na UI, ou deve ser fiel? deve ser fiel
2. Querem tela de cadastro para bairros/unidades ou dados fixos no app? Não
3. Regra de desempate entre unidades com leitos livres iguais: menor profundidade ja resolve, mas e dentro da mesma profundidade? o tipo dele
4. O limite de 3 bairros considera numero de arestas (como no BFS) ou distancia real? arestas
5. "Tempo real" = atualizacao local ao registrar chamado ou sincronizacao externa? atualização local
6. Persistencia e obrigatoria para a nota final? sim
7. Precisam do historico de chamados visivel no app? seria bom 
8. Confirmamos sem API, tudo local? sim
