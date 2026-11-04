import { ROTAS } from '@/shared/config'
import { TituloPagina } from '@/shared/ui'

export function ImportarProdutosPage() {
  return (
    <TituloPagina
      titulo="Importar produtos do Bling"
      admin
      voltarPara={ROTAS.inicio}
    />
  )
}
