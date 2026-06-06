// Helpers de chamada a API e de uso nos formularios.

export async function executar({ method, path, body }) {
  const opcoes = { method, headers: { 'Content-Type': 'application/json' } }
  if (method === 'POST') opcoes.body = JSON.stringify(body)
  const resp = await fetch(path, opcoes)
  const dado = await resp.json()
  return { ok: resp.ok, dado }
}

/** "q0, q1 ,q2" -> ["q0","q1","q2"] (sem vazios). */
export function lista(texto) {
  return texto.split(',').map((x) => x.trim()).filter(Boolean)
}

/** Reune todos os estados citados (inicial + finais + transicoes). */
export function reunirEstados(inicial, finais, transicoes, ...campos) {
  const set = new Set()
  if (inicial) set.add(inicial)
  finais.forEach((f) => set.add(f))
  transicoes.forEach((t) => campos.forEach((c) => t[c] && set.add(t[c])))
  return [...set]
}
