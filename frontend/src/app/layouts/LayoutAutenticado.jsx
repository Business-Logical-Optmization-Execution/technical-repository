import { Outlet } from 'react-router-dom'
import { Cabecalho } from '@/widgets/cabecalho'
import estilos from './LayoutAutenticado.module.css'

export function LayoutAutenticado() {
  return (
    <>
      <Cabecalho />
      <main className={estilos.conteudo}>
        <Outlet />
      </main>
    </>
  )
}
