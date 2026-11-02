import { TituloPagina } from '@/shared/ui'
import { Link } from 'react-router-dom'
import { ROTAS } from '@/shared/config'
import estilos from './AcessoNegadoPage.module.css'

export function AcessoNegadoPage() {

  return (
    <div className={estilos.pagina}>
      <TituloPagina titulo="Acesso negado" />
      <p>Você não tem acesso a esta página.</p>
      <Link to={ROTAS.inicio} className={estilos.link}>Ir para o Início</Link>
    </div>
  )
}
