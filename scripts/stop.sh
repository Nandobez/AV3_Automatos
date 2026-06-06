#!/usr/bin/env bash
# Derruba backend (Spring Boot) e frontend (Vite).
# Uso: ./stop.sh

echo "==> Encerrando backend (Spring Boot) ..."
pkill -f 'spring-boot:run' 2>/dev/null && echo "    backend encerrado" || echo "    backend nao estava rodando"

echo "==> Encerrando frontend (Vite) ..."
pkill -f 'vite' 2>/dev/null && echo "    frontend encerrado" || echo "    frontend nao estava rodando"

# Garante liberar as portas, caso algum processo tenha ficado preso.
for porta in 8080 5173; do
  pid="$(lsof -ti tcp:$porta 2>/dev/null)"
  if [ -n "$pid" ]; then
    echo "==> Liberando porta $porta (PID $pid) ..."
    kill $pid 2>/dev/null || true
  fi
done

echo "==> Pronto."
