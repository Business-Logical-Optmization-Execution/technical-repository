import { ROTAS } from '@/shared/config'
import { TituloPagina } from '@/shared/ui'

export function HistoricoPage() {
  return (
    <TituloPagina
      titulo="Histórico"
      admin
      voltarPara={ROTAS.inicio}
    />
  )
}
