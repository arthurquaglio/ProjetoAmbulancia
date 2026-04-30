# Projeto Ambulancia

App Android em Java que escolhe a unidade de saude mais adequada usando grafo de bairros e disponibilidade de leitos.

## Funcionalidades
- Novo chamado com selecao de bairro e dados do paciente.
- Decisao por camadas (ate 3 bairros), priorizando mais leitos livres.
- Fallback para menor sobrecarga quando nao ha vaga.
- Lotacao em tempo real (lista com auto atualizacao).
- Historico de chamados com persistencia em SQLite.
- Tela para executar os testes do enunciado.

## Dados de teste
Os dados do enunciado sao inseridos automaticamente no SQLite na primeira execucao do app.

## Testes locais
- Teste de algoritmo com os casos fixos do enunciado.

## Observacao sobre o banco
Se voce ja rodou o app antes, pode ser necessario limpar os dados para recriar o SQLite.

## Como executar
Abra o projeto no Android Studio e execute no emulador ou dispositivo.

## Como rodar os testes
Use o gradle do projeto.

```powershell
cd D:\ProjetosAndroid\ProjetoAmbulancia
.\gradlew test
```
