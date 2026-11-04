import { useParams } from 'react-router-dom'
import { ROTAS } from '@/shared/config'
import { TituloPagina } from '@/shared/ui'

export function RegistrarSaidaPage() {
  const { id } = useParams()

  return (
    <TituloPagina
      titulo="Registrar saída"
      voltarPara={ROTAS.produto(id)}
    />
  )
}
