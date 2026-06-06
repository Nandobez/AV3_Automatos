@echo off
REM Derruba backend (porta 8080) e frontend (porta 5173).
REM Uso: stop.bat

echo ==> Encerrando backend (porta 8080) ...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (
  taskkill /F /PID %%a >nul 2>&1 && echo     backend (PID %%a) encerrado
)

echo ==> Encerrando frontend (porta 5173) ...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :5173 ^| findstr LISTENING') do (
  taskkill /F /PID %%a >nul 2>&1 && echo     frontend (PID %%a) encerrado
)

REM Fecha tambem as janelas abertas pelo run.bat, se ainda existirem.
taskkill /F /FI "WINDOWTITLE eq Backend - Spring Boot*" >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Frontend - Vite*" >nul 2>&1

echo ==> Pronto.
