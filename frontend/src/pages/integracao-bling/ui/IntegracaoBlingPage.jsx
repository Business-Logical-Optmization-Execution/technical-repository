import { ROTAS } from '@/shared/config'
import { TituloPagina } from '@/shared/ui'

export function IntegracaoBlingPage() {
  return (
    <TituloPagina
      titulo="Integração com o Bling"
      admin
      voltarPara={ROTAS.inicio}
    />
  )
}
