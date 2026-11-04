import { ROTAS } from '@/shared/config'
import { TituloPagina } from '@/shared/ui'

export function ConfiguracoesPage() {
  return (
    <TituloPagina
      titulo="Configurações"
      admin
      voltarPara={ROTAS.inicio}
    />
  )
}
