import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Proxy: as chamadas para /api vao para o backend Spring Boot (porta 8080),
// evitando configurar CORS durante o desenvolvimento.
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': 'http://localhost:8080'
    }
  }
})
