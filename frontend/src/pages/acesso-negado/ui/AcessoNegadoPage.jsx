import { TituloPagina } from '@/shared/ui'
import { Link } from 'react-router-dom'
import { ROTAS } from '@/shared/config'

export function AcessoNegadoPage() {
  
  return (
    <>
      <TituloPagina titulo="Acesso negado" />
      <p>Você não tem acesso a esta página.</p>
      <Link to={ROTAS.inicio}>Ir para o Início</Link>
    </>

  )
}
