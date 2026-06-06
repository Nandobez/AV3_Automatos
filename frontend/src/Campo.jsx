/** Campo de texto rotulado. */
export default function Campo({ rotulo, valor, aoMudar, dica }) {
  return (
    <label className="campo">
      <span>{rotulo}</span>
      <input value={valor} onChange={(e) => aoMudar(e.target.value)} placeholder={dica} />
    </label>
  )
}
