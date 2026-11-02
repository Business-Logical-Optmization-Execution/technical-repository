import { ROTAS } from '@/shared/config'
import { TituloPagina } from '@/shared/ui'

export function BuscaPage() {
  return (
    <TituloPagina
      titulo="Resultado da busca"
      voltarPara={ROTAS.inicio}
    />
  )
}
