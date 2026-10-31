import { fileURLToPath, URL } from 'node:url'
import react from '@vitejs/plugin-react'
import { defineConfig, loadEnv } from 'vite'

// Toda chamada do front vai para /api/... e o Vite decide o destino:
//  - rotas listadas em ROTAS_NO_SPRING  -> backend Spring (8080)
//  - todo o resto                       -> mock json-server (3001)
// Assim a integração acontece rota por rota, editando só o .env.local.
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const mockUrl = env.MOCK_API_URL || 'http://localhost:3001'
  const springUrl = env.SPRING_API_URL || 'http://localhost:8080'
  const rotasNoSpring = (env.ROTAS_NO_SPRING || '')
    .split(',')
    .map((rota) => rota.trim())
    .filter(Boolean)

  const semPrefixoApi = (caminho) => caminho.replace(/^\/api/, '')
  const proxy = {}
  // A ordem importa: as rotas do Spring vêm antes do "/api" genérico.
  for (const rota of rotasNoSpring) {
    proxy[`/api/${rota}`] = { target: springUrl, changeOrigin: true, rewrite: semPrefixoApi }
  }
  proxy['/api'] = { target: mockUrl, changeOrigin: true, rewrite: semPrefixoApi }

  return {
    plugins: [react()],
    resolve: {
      alias: { '@': fileURLToPath(new URL('./src', import.meta.url)) },
    },
    server: { port: 5173, proxy },
  }
})
