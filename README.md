# Simulador de Linguagens Formais e Modelos Computacionais

## Documento Técnico

### Introdução

Este projeto implementa um simulador web para conceitos centrais da Teoria da Computação: linguagens formais, autômatos finitos, gramáticas livres de contexto, autômatos com pilha e máquinas de Turing. A aplicação permite cadastrar modelos computacionais, executar cadeias de entrada e visualizar os resultados com passos intermediários.

#### Problema

O estudo de modelos formais costuma envolver definições matemáticas abstratas, como alfabetos, cadeias, funções de transição, derivações e configurações de máquinas. Embora essas definições sejam essenciais, a compreensão prática pode ser dificultada quando o aluno não consegue observar a execução passo a passo.

O problema abordado é a ausência de uma ferramenta simples, integrada e executável localmente que permita testar diferentes modelos formais e acompanhar seu comportamento sobre entradas específicas.

#### Objetivos

- Implementar uma aplicação capaz de simular operações sobre linguagens formais.
- Simular autômatos finitos determinísticos e não determinísticos.
- Simular gramáticas livres de contexto por derivação.
- Simular autômatos com pilha determinísticos.
- Simular máquinas de Turing determinísticas.
- Exibir resultados de aceitação/rejeição e passos intermediários.
- Organizar o projeto com separação entre frontend, backend, modelos, serviços e controladores.
- Aplicar melhorias de organização e desempenho, como uso de Strategy, Factory, indexação de transições e validações centralizadas.

### Fundamentação Teórica

#### Linguagens Formais

Uma linguagem formal é um conjunto de cadeias construídas a partir de um alfabeto finito. Um alfabeto, geralmente representado por Sigma, contém os símbolos permitidos. Uma cadeia é uma sequência finita desses símbolos.

Entre as operações básicas estão:

- validação de uma cadeia em relação a um alfabeto;
- cálculo do tamanho de uma cadeia;
- concatenação de cadeias;
- geração de Sigma*, conjunto de todas as cadeias possíveis, incluindo a cadeia vazia;
- geração de Sigma+, conjunto de todas as cadeias possíveis, exceto a cadeia vazia.

Como Sigma* e Sigma+ podem ser infinitos, a implementação limita a geração por tamanho máximo e por quantidade máxima de cadeias retornadas.

#### Autômatos

Autômatos são modelos matemáticos usados para reconhecer linguagens. Um autômato finito possui um conjunto de estados, um estado inicial, estados finais e transições entre estados de acordo com símbolos de entrada.

O projeto implementa autômatos finitos com suporte a:

- AFD, quando há apenas uma transição possível para cada par estado/símbolo;
- AFN, quando pode haver múltiplas transições;
- transições epsilon/lambda, que não consomem símbolo da entrada.

Durante a execução, o simulador mantém um conjunto de estados atuais. Isso permite representar tanto o comportamento determinístico quanto o não determinístico. Para transições vazias, é calculado o fecho-epsilon.

Também há suporte a autômatos com pilha. Esse modelo adiciona uma pilha à execução, permitindo reconhecer linguagens que não podem ser reconhecidas por autômatos finitos simples, como a linguagem:

```text
L = { a^n b^n | n >= 1 }
```

#### Gramáticas

Uma gramática formal define regras para gerar cadeias de uma linguagem. O projeto trabalha com gramáticas livres de contexto, compostas por:

- variáveis ou não terminais;
- terminais;
- símbolo inicial;
- produções.

Uma produção possui a forma:

```text
S -> aSb
S -> λ
```

O simulador tenta derivar uma cadeia a partir do símbolo inicial usando busca recursiva com backtracking. Quando a cadeia pertence à linguagem da gramática, a aplicação retorna a árvore de derivação e a derivação mais à esquerda.

#### Máquinas de Turing

A máquina de Turing é um modelo computacional composto por uma fita, um cabeçote de leitura/escrita, estados e transições. A cada passo, a máquina:

1. lê o símbolo sob o cabeçote;
2. procura uma transição compatível com o estado atual e o símbolo lido;
3. escreve um novo símbolo;
4. move o cabeçote para a esquerda ou direita;
5. muda de estado.

O projeto implementa uma máquina de Turing determinística de uma fita. A execução termina quando a máquina alcança um estado final, não encontra transição válida ou ultrapassa o limite máximo de passos.

### Arquitetura

O sistema está dividido em duas partes principais:

- backend em Spring Boot, responsável pela lógica dos simuladores e exposição da API REST;
- frontend em React/Vite, responsável pelos formulários, envio de requisições e exibição dos resultados.

#### Diagrama UML

A arquitetura do backend segue uma divisão em controladores, DTOs, modelos, serviços e classes utilitárias. Os controladores recebem as requisições HTTP, os DTOs representam os dados de entrada, os modelos representam as estruturas formais e os serviços concentram os algoritmos de execução.

Classes principais:

- `LinguagemFormalController`
- `AutomatoFinitoController`
- `GramaticaController`
- `AutomatoPilhaController`
- `MaquinaTuringController`
- `LinguagemFormalService`
- `AutomatoFinitoService`
- `GramaticaService`
- `AutomatoPilhaService`
- `MaquinaTuringService`
- `ModeloComputacionalService`
- `AutomatoFinitoFactory`
- `AutomatoPilhaFactory`
- `MaquinaTuringFactory`

#### Casos de uso

Casos de uso principais:

- validar cadeia sobre um alfabeto;
- concatenar cadeias;
- gerar Sigma* e Sigma+;
- executar autômato finito;
- executar gramática livre de contexto;
- executar autômato com pilha;
- executar máquina de Turing;
- visualizar passos de execução.

### Implementação

#### Estruturas de dados

O backend utiliza records Java para representar os modelos e resultados. Entre as principais estruturas estão:

- `Alfabeto`: conjunto ordenado de símbolos usando `LinkedHashSet<Character>`;
- `AutomatoFinito`: estados, estado inicial, estados finais e lista de transições;
- `Transicao`: origem, símbolo e destino;
- `Gramatica`: variáveis, terminais, produções e símbolo inicial;
- `Producao`: variável e lado direito da produção;
- `AutomatoPilha`: estados, estado inicial, estados finais, símbolo inicial da pilha e transições;
- `TransicaoPilha`: estado atual, símbolo lido, símbolo desempilhado, novo estado e símbolos empilhados;
- `MaquinaTuring`: estado inicial, estados finais, transições e símbolo branco;
- `Fita`: lista dinâmica de células e posição do cabeçote;
- `PassoExecucao`, `PassoPilha` e `PassoTuring`: registros do passo a passo.

Foram adicionadas estruturas auxiliares para otimizar consultas:

- `AutomatoFinitoFactory`: transforma a lista de transições em mapas indexados por origem e símbolo;
- `AutomatoPilhaFactory`: agrupa transições por estado;
- `MaquinaTuringFactory`: indexa transições por estado e símbolo lido;
- `Simbolos`: centraliza o tratamento de lambda, epsilon e equivalentes.

Também foi aplicada uma interface comum:

```java
public interface ModeloComputacionalService<TModelo, TEntrada, TResultado> {
    TResultado executar(TModelo modelo, TEntrada entrada);
}
```

Essa interface representa o padrão Strategy, pois cada simulador implementa uma estratégia diferente de execução mantendo um contrato comum.

#### Algoritmos

##### Validação de cadeia

Percorre cada símbolo da cadeia e verifica se ele pertence ao alfabeto. Caso algum símbolo não pertença ao conjunto permitido, uma exceção de validação é lançada.

##### Geração de Sigma* e Sigma+

A geração é feita recursivamente. Para cada tamanho de cadeia, o algoritmo monta todos os prefixos possíveis até atingir o tamanho desejado. Como o número de cadeias cresce exponencialmente, a implementação limita o tamanho máximo e o total de cadeias geradas.

##### Execução de autômato finito

O simulador mantém um conjunto de estados atuais. Antes do consumo da entrada, calcula o fecho-epsilon do estado inicial. Para cada símbolo da cadeia:

1. calcula os destinos possíveis a partir dos estados atuais;
2. aplica novamente o fecho-epsilon;
3. registra o passo da execução.

A cadeia é aceita se, ao final, algum estado atual pertencer ao conjunto de estados finais.

##### Execução de autômato com pilha

O simulador mantém:

- estado atual;
- posição atual na entrada;
- pilha;
- lista de passos.

A cada passo, procura uma transição compatível com o estado, o símbolo de entrada e o topo da pilha. A transição pode consumir símbolo da entrada, desempilhar, empilhar e mudar de estado. A cadeia é aceita quando a entrada foi totalmente consumida e o estado atual é final.

##### Derivação em gramática livre de contexto

A derivação usa busca em profundidade com backtracking. O algoritmo tenta provar que uma variável deriva exatamente um trecho da cadeia. Para produções com múltiplos símbolos no lado direito, testa diferentes divisões do trecho.

A implementação usa memoização de subderivações bem-sucedidas, evitando recalcular a mesma derivação para o mesmo símbolo e intervalo da cadeia.

##### Execução de máquina de Turing

A máquina executa em laço até parar. Em cada iteração:

1. lê o símbolo atual da fita;
2. registra o passo;
3. verifica se o estado é final;
4. busca a transição por estado e símbolo;
5. escreve na fita;
6. move o cabeçote;
7. atualiza o estado.

Há limite máximo de passos para evitar laços infinitos.

### Complexidade

#### Análise assintótica

##### Validação de cadeia

Para uma cadeia de tamanho `n`, a validação percorre cada símbolo uma vez.

```text
Tempo: O(n)
Espaço: O(1), desconsiderando o alfabeto
```

##### Geração de Sigma* e Sigma+

Para alfabeto de tamanho `k` e tamanho máximo `m`, a quantidade de cadeias geradas é:

```text
1 + k + k^2 + ... + k^m
```

Logo:

```text
Tempo: O(k^m)
Espaço: O(k^m)
```

##### Autômato finito

Se `n` é o tamanho da cadeia, `q` é a quantidade de estados e `t` a quantidade de transições, a versão otimizada indexa as transições antes da execução.

```text
Pré-processamento: O(t)
Execução: O(n * q + custo do fecho-epsilon)
Espaço: O(t + q)
```

O uso de mapas evita varrer toda a lista de transições a cada símbolo.

##### Autômato com pilha

Se `n` é o tamanho da entrada e `p` é o número de passos executados, a execução é limitada por `LIMITE_PASSOS`.

```text
Pré-processamento: O(t)
Execução: O(p * r)
Espaço: O(t + n)
```

Onde `r` é a quantidade de transições candidatas no estado atual. Como as transições são agrupadas por estado, evita-se percorrer todas as transições da máquina em cada passo.

##### Gramática livre de contexto

O reconhecimento por backtracking pode ter custo exponencial no pior caso, especialmente para gramáticas ambíguas ou recursivas.

```text
Tempo no pior caso: exponencial
Espaço: proporcional à profundidade da derivação e ao cache
```

A memoização reduz recomputações de subproblemas já resolvidos, mas não transforma o algoritmo em polinomial para todos os casos. Uma alternativa clássica seria CYK, com custo `O(n^3)`, desde que a gramática esteja em Forma Normal de Chomsky.

##### Máquina de Turing

Se `p` é o número de passos até a parada:

```text
Pré-processamento: O(t)
Execução: O(p)
Espaço: O(t + tamanho da fita utilizada)
```

O lookup de transições por mapa evita busca linear na lista de transições.

### Conclusão

O projeto apresenta uma implementação funcional e modular dos principais modelos estudados em Linguagens Formais e Teoria da Computação. A aplicação permite observar o comportamento dos modelos de forma prática, tornando mais clara a relação entre definições formais e execução computacional.

As melhorias aplicadas aumentam a robustez e a organização do código. O uso de Strategy padroniza os simuladores, enquanto as factories separam a preparação dos modelos da execução. A indexação das transições melhora o desempenho e as validações reduzem falhas inesperadas durante o uso da API.

Como evolução do projeto, a gramática livre de contexto pode receber um algoritmo CYK para reconhecimento polinomial em gramáticas convertidas para Forma Normal de Chomsky. A API também pode incorporar validação declarativa com Bean Validation e testes de integração.

### Código Fonte

O código-fonte está organizado nas seguintes pastas:

```text
src/main/java/br/edu/av3
frontend/src
scripts
```

Principais módulos do backend:

```text
controller
dto
exception
model
service
util
```

Principais módulos do frontend:

```text
forms
api.js
useExecucao.js
Resultado.jsx
Saida.jsx
```

### Publicação

#### GitHub

```text
https://github.com/Nandobez/AV3_Automatos
```

#### GitLab

```text
Não publicado
```

## README de Execução

### Pré-requisitos

- Java JDK 21 ou superior.
- Node.js e npm.
- Git, caso deseje clonar o projeto.

O projeto possui Maven Wrapper, então não é obrigatório ter Maven instalado globalmente.

### Como executar o backend

Na raiz do projeto:

```powershell
.\mvnw.cmd spring-boot:run
```

O backend será iniciado em:

```text
http://localhost:8080
```

As rotas da API começam com:

```text
http://localhost:8080/api
```

Esse comando mantém o terminal em execução enquanto o servidor estiver ativo. Para encerrar o backend, pressione `Ctrl+C` no terminal em que ele foi iniciado.

### Problemas comuns ao rodar o backend

#### O terminal não volta para o prompt

Isso significa que o Spring Boot está rodando. Enquanto o backend estiver ativo, o terminal fica ocupado exibindo os logs da aplicação.

Para confirmar se a API está respondendo, abra outro terminal e rode:

```powershell
Invoke-RestMethod "http://localhost:8080/api/linguagem-formal/sigma-mais?alfabeto=ab&maxTamanho=1"
```

Saída esperada:

```text
a
b
```

#### Erro dizendo que a porta 8080 já está em uso

Esse erro significa que outro processo já está usando a porta do backend.

Verifique qual processo está usando a porta:

```powershell
netstat -ano | findstr :8080
```

Procure o número na última coluna, que é o PID. Depois veja o processo:

```powershell
Get-Process -Id <PID>
```

Para encerrar esse processo:

```powershell
taskkill /F /PID <PID>
```

Depois rode novamente:

```powershell
.\mvnw.cmd spring-boot:run
```

Também é possível rodar o backend em outra porta:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

Nesse caso, a API ficará em:

```text
http://localhost:8081/api
```

Se usar outra porta para o backend, ajuste também o proxy do frontend em `frontend/vite.config.js`, trocando `http://localhost:8080` pela nova porta.

#### Erro dizendo que `mvnw.cmd` não foi encontrado

Confirme que o comando está sendo executado na raiz do projeto, a pasta onde ficam `pom.xml` e `mvnw.cmd`:

```powershell
cd C:\Users\Jean\Documents\Aspectos\AV3_Automatos
dir
```

Você deve ver arquivos como:

```text
pom.xml
mvnw.cmd
frontend
src
```

#### Erro relacionado ao Java

Verifique a versão do Java:

```powershell
java -version
```

O projeto usa Java 21 no `pom.xml`. Versões mais novas também podem funcionar, mas se houver erro de compatibilidade, instale/configure o JDK 21 e ajuste a variável `JAVA_HOME`.

### Como executar o frontend

Em outro terminal:

```powershell
cd frontend
npm install
npm run dev
```

O frontend será iniciado em:

```text
http://localhost:5173
```

### Como executar tudo pelo script

No Windows:

```powershell
.\scripts\run.bat
```

### Como rodar os testes

Na raiz do projeto:

```powershell
.\mvnw.cmd test
```

## Exemplos de Entrada e Saída

### Linguagens Formais - validar cadeia

Entrada:

```json
{
  "alfabeto": "ab",
  "cadeia": "abba"
}
```

Endpoint:

```text
POST /api/linguagem-formal/validar
```

Saída:

```json
{
  "valida": true,
  "tamanho": 4,
  "mensagem": "Cadeia valida para Sigma"
}
```

### Linguagens Formais - concatenar

Entrada:

```json
{
  "w1": "ab",
  "w2": "ba"
}
```

Endpoint:

```text
POST /api/linguagem-formal/concatenar
```

Saída:

```json
{
  "resultado": "abba",
  "tamanho": 4
}
```

### Autômato Finito

Entrada:

```json
{
  "automato": {
    "estados": ["q0", "q1", "q2"],
    "estadoInicial": "q0",
    "estadosFinais": ["q2"],
    "transicoes": [
      { "origem": "q0", "simbolo": "a", "destino": "q1" },
      { "origem": "q1", "simbolo": "b", "destino": "q2" }
    ]
  },
  "cadeia": "ab"
}
```

Endpoint:

```text
POST /api/automato-finito/executar
```

Saída:

```json
{
  "aceita": true,
  "estadosFinais": ["q2"],
  "passos": [
    {
      "simbolo": "a",
      "estadosAntes": ["q0"],
      "estadosDepois": ["q1"]
    },
    {
      "simbolo": "b",
      "estadosAntes": ["q1"],
      "estadosDepois": ["q2"]
    }
  ]
}
```

### Gramática Livre de Contexto

Entrada:

```json
{
  "gramatica": {
    "variaveis": ["S"],
    "terminais": ["a", "b"],
    "simboloInicial": "S",
    "producoes": [
      { "variavel": "S", "producao": "aSb" },
      { "variavel": "S", "producao": "λ" }
    ]
  },
  "cadeia": "aabb"
}
```

Endpoint:

```text
POST /api/gramatica/derivar
```

Saída esperada:

```json
{
  "aceita": true,
  "passos": ["S", "aSb", "aaSbb", "aabb"],
  "arvore": {
    "simbolo": "S",
    "filhos": []
  }
}
```

### Autômato com Pilha

Entrada:

```json
{
  "automato": {
    "estados": ["q0", "q1", "qf"],
    "estadoInicial": "q0",
    "estadosFinais": ["qf"],
    "simboloInicialPilha": "Z",
    "transicoes": [
      { "estadoAtual": "q0", "simboloLido": "a", "desempilha": "Z", "novoEstado": "q0", "empilha": "AZ" },
      { "estadoAtual": "q0", "simboloLido": "a", "desempilha": "A", "novoEstado": "q0", "empilha": "AA" },
      { "estadoAtual": "q0", "simboloLido": "b", "desempilha": "A", "novoEstado": "q1", "empilha": "λ" },
      { "estadoAtual": "q1", "simboloLido": "b", "desempilha": "A", "novoEstado": "q1", "empilha": "λ" },
      { "estadoAtual": "q1", "simboloLido": "λ", "desempilha": "Z", "novoEstado": "qf", "empilha": "Z" }
    ]
  },
  "cadeia": "aabb"
}
```

Endpoint:

```text
POST /api/automato-pilha/executar
```

Saída resumida:

```json
{
  "aceita": true,
  "motivoParada": "Aceita por estado final",
  "passos": [
    {
      "estado": "q0",
      "entradaRestante": "aabb",
      "pilha": "Z",
      "acao": "le 'a', desempilha Z, empilha AZ -> q0"
    }
  ]
}
```

### Máquina de Turing

Entrada:

```json
{
  "maquina": {
    "estadoInicial": "q0",
    "estadosFinais": ["q3"],
    "branco": "B",
    "transicoes": [
      { "estadoAtual": "q0", "simboloLido": "0", "novoEstado": "q0", "simboloEscrito": "0", "movimento": "R" },
      { "estadoAtual": "q0", "simboloLido": "1", "novoEstado": "q1", "simboloEscrito": "0", "movimento": "R" },
      { "estadoAtual": "q1", "simboloLido": "0", "novoEstado": "q1", "simboloEscrito": "0", "movimento": "R" },
      { "estadoAtual": "q1", "simboloLido": "B", "novoEstado": "q2", "simboloEscrito": "B", "movimento": "L" },
      { "estadoAtual": "q2", "simboloLido": "0", "novoEstado": "q3", "simboloEscrito": "B", "movimento": "R" }
    ]
  },
  "entrada": "000100"
}
```

Endpoint:

```text
POST /api/maquina-turing/executar
```

Saída resumida:

```json
{
  "aceita": true,
  "estadoFinal": "q3",
  "fitaFinal": "B00000BB",
  "motivoParada": "Alcancou estado final",
  "passos": [
    {
      "passo": 0,
      "estado": "q0",
      "simboloLido": "0",
      "fita": "B000100B",
      "cabecote": 1
    }
  ]
}
```
