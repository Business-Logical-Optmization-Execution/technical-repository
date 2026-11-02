import { ROTAS } from '@/shared/config'
import { TituloPagina } from '@/shared/ui'

export function DivergenciasPage() {
  return (
    <TituloPagina
      titulo="Divergências e negativos"
      admin
      voltarPara={ROTAS.inicio}
    />
  )
}
