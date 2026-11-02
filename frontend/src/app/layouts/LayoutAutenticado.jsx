import { Outlet } from 'react-router-dom'
import estilos from './LayoutAutenticado.module.css'


// TODO (exercício 1.9 / 2.4): adicionar o cabeçalho acima do conteúdo.
export function LayoutAutenticado() {
  return (
    <main className={estilos.conteudo}> 
      <Outlet />
    </main>
  )
}
