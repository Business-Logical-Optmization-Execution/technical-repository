import { ROTAS } from '@/shared/config'
import { TituloPagina } from '@/shared/ui'

export function ProdutoDetalhePage() {
  return (
    <TituloPagina
      titulo="Detalhe do produto"
      voltarPara={ROTAS.busca}
    />
  )
}
