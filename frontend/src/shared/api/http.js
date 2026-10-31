import axios from 'axios'

// Toda chamada vai para /api/...; o proxy do Vite decide entre mock e Spring.
export const http = axios.create({
  baseURL: '/api',
  timeout: 10000,
})
