import { ROTAS } from '@/shared/config'
import { TituloPagina } from '@/shared/ui'

export function DashboardPage() {
  return (
    <TituloPagina
      titulo="Dashboard"
      admin
      voltarPara={ROTAS.inicio}
    />
  )
}
