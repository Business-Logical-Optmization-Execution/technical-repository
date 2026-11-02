import { useParams } from 'react-router-dom'
import { ROTAS } from '@/shared/config'
import { TituloPagina } from '@/shared/ui'

export function RegistrarEntradaPage() {
  const { id } = useParams()

  return (
    <TituloPagina
      titulo="Registrar entrada"
      voltarPara={ROTAS.produto(id)}
    />
  )
}
