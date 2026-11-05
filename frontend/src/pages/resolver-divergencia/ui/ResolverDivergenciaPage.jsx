import { ROTAS } from '@/shared/config'
import { TituloPagina } from '@/shared/ui'

export function ResolverDivergenciaPage() {
  return (
    <TituloPagina
      titulo="Resolver divergência"
      admin
      voltarPara={ROTAS.divergencias}
    />
  )
}
