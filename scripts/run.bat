@echo off
REM Sobe backend (Spring Boot) e frontend (Vite) em janelas separadas.
REM Uso: run.bat  (duplo clique ou no terminal)

REM RAIZ = pasta do projeto (pai de scripts\)
pushd "%~dp0.."
set "RAIZ=%CD%"
popd

echo ==> Iniciando backend (Spring Boot) em http://localhost:8080 ...
start "Backend - Spring Boot" cmd /k "cd /d %RAIZ% && mvn spring-boot:run"

echo ==> Preparando frontend ...
cd /d %RAIZ%\frontend
if not exist node_modules (
  echo     instalando dependencias ^(npm install^) ...
  call npm install
)

echo ==> Iniciando frontend (Vite) em http://localhost:5173 ...
start "Frontend - Vite" cmd /k "cd /d %RAIZ%\frontend && npm run dev"

echo.
echo Backend:  http://localhost:8080  (API em /api)
echo Frontend: http://localhost:5173
echo Feche as janelas abertas para encerrar.
