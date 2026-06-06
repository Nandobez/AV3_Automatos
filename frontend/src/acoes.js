// Cada acao mapeia um endpoint do backend, com um exemplo pre-preenchido (do PDF).
// method POST -> envia 'exemplo' como JSON no corpo.
// method GET  -> usa 'path' com query string (editavel pela UI).

export const ACOES = [
  {
    modulo: 'Modulo 1 - Linguagens Formais',
    label: 'Validar cadeia',
    method: 'POST',
    path: '/api/linguagem-formal/validar',
    exemplo: { alfabeto: 'ab', cadeia: 'abba' }
  },
  {
    modulo: 'Modulo 1 - Linguagens Formais',
    label: 'Concatenar',
    method: 'POST',
    path: '/api/linguagem-formal/concatenar',
    exemplo: { w1: 'ab', w2: 'ba' }
  },
  {
    modulo: 'Modulo 1 - Linguagens Formais',
    label: 'Sigma* (ate tamanho)',
    method: 'GET',
    path: '/api/linguagem-formal/sigma-estrela?alfabeto=ab&maxTamanho=2'
  },
  {
    modulo: 'Modulo 1 - Linguagens Formais',
    label: 'Sigma+ (ate tamanho)',
    method: 'GET',
    path: '/api/linguagem-formal/sigma-mais?alfabeto=ab&maxTamanho=2'
  },
  {
    modulo: 'Modulo 2 - Automato Finito',
    label: 'Executar cadeia',
    method: 'POST',
    path: '/api/automato-finito/executar',
    exemplo: {
      automato: {
        estados: ['q0', 'q1', 'q2'],
        estadoInicial: 'q0',
        estadosFinais: ['q2'],
        transicoes: [
          { origem: 'q0', simbolo: 'a', destino: 'q1' },
          { origem: 'q1', simbolo: 'b', destino: 'q2' }
        ]
      },
      cadeia: 'ab'
    }
  },
  {
    modulo: 'Modulo 3 - Gramatica (GLC)',
    label: 'Derivar cadeia',
    method: 'POST',
    path: '/api/gramatica/derivar',
    exemplo: {
      gramatica: {
        variaveis: ['S'],
        terminais: ['a', 'b'],
        simboloInicial: 'S',
        producoes: [
          { variavel: 'S', producao: 'aSb' },
          { variavel: 'S', producao: 'λ' }
        ]
      },
      cadeia: 'aaabbb'
    }
  },
  {
    modulo: 'Modulo 4 - Automato com Pilha',
    label: 'Executar cadeia (a^n b^n)',
    method: 'POST',
    path: '/api/automato-pilha/executar',
    exemplo: {
      automato: {
        estados: ['q0', 'q1', 'qf'],
        estadoInicial: 'q0',
        estadosFinais: ['qf'],
        simboloInicialPilha: 'Z',
        transicoes: [
          { estadoAtual: 'q0', simboloLido: 'a', desempilha: 'Z', novoEstado: 'q0', empilha: 'AZ' },
          { estadoAtual: 'q0', simboloLido: 'a', desempilha: 'A', novoEstado: 'q0', empilha: 'AA' },
          { estadoAtual: 'q0', simboloLido: 'b', desempilha: 'A', novoEstado: 'q1', empilha: 'λ' },
          { estadoAtual: 'q1', simboloLido: 'b', desempilha: 'A', novoEstado: 'q1', empilha: 'λ' },
          { estadoAtual: 'q1', simboloLido: 'λ', desempilha: 'Z', novoEstado: 'qf', empilha: 'Z' }
        ]
      },
      cadeia: 'aabb'
    }
  },
  {
    modulo: 'Modulo 5 - Maquina de Turing',
    label: 'Soma unaria (3+2)',
    method: 'POST',
    path: '/api/maquina-turing/executar',
    exemplo: {
      maquina: {
        estadoInicial: 'q0',
        estadosFinais: ['q3'],
        branco: 'B',
        transicoes: [
          { estadoAtual: 'q0', simboloLido: '0', novoEstado: 'q0', simboloEscrito: '0', movimento: 'R' },
          { estadoAtual: 'q0', simboloLido: '1', novoEstado: 'q1', simboloEscrito: '0', movimento: 'R' },
          { estadoAtual: 'q1', simboloLido: '0', novoEstado: 'q1', simboloEscrito: '0', movimento: 'R' },
          { estadoAtual: 'q1', simboloLido: 'B', novoEstado: 'q2', simboloEscrito: 'B', movimento: 'L' },
          { estadoAtual: 'q2', simboloLido: '0', novoEstado: 'q3', simboloEscrito: 'B', movimento: 'R' }
        ]
      },
      entrada: '000100'
    }
  }
]
