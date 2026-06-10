#!/usr/bin/env bash
# Sobe backend (Spring Boot) e frontend (Vite) juntos.
# Uso: ./run.sh   (Ctrl+C derruba os dois)
set -e

RAIZ="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$RAIZ"

echo "==> Iniciando backend (Spring Boot) em http://localhost:8080 ..."
./mvnw spring-boot:run &
BACK_PID=$!

echo "==> Preparando frontend ..."
cd "$RAIZ/frontend"
if [ ! -d node_modules ]; then
  echo "    instalando dependencias (npm install) ..."
  npm install
fi

echo "==> Iniciando frontend (Vite) em http://localhost:5173 ..."
npm run dev &
FRONT_PID=$!

# Ao sair (Ctrl+C), derruba os dois processos.
derrubar() {
  echo ""
  echo "==> Derrubando servidores ..."
  kill "$BACK_PID" "$FRONT_PID" 2>/dev/null || true
}
trap derrubar EXIT INT TERM

echo ""
echo "Backend:  http://localhost:8080  (API em /api)"
echo "Frontend: http://localhost:5173"
echo "Pressione Ctrl+C para encerrar."
wait
