import { Outlet } from 'react-router-dom'
import estilos from './LayoutAutenticado.module.css'

export function LayoutAutenticado() {
  return (
    <main className={estilos.conteudo}>
      <Outlet />
    </main>
  )
}
