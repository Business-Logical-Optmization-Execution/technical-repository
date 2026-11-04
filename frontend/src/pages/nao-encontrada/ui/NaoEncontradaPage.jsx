import { Link } from 'react-router-dom'
import { ROTAS } from '@/shared/config'
import { useTituloDocumento } from '@/shared/lib'
import estilos from './NaoEncontradaPage.module.css'

export function NaoEncontradaPage() {
  useTituloDocumento('Página não encontrada')

  return (
    <main className={estilos.pagina}>
      <h1 className={estilos.titulo}>Página não encontrada.</h1>
      <Link to={ROTAS.inicio} className={estilos.link}>Ir para o Início</Link>
    </main>
  )
}
