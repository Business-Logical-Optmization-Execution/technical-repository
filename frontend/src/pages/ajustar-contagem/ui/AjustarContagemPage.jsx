import { useParams } from 'react-router-dom'
import { ROTAS } from '@/shared/config'
import { TituloPagina } from '@/shared/ui'

export function AjustarContagemPage() {
  const { id } = useParams()

  return (
    <TituloPagina
      titulo="Ajustar por contagem"
      voltarPara={ROTAS.produto(id)}
    />
  )
}
