import { useState } from 'react'
import { SearchX } from 'lucide-react'
import { Botao, EstadoVazio } from '@/shared/ui'
import { CardProdutoResultado, filtrarProdutos } from '@/entities/produto'
import estilos from './BuscaPage.module.css'

const POR_PAGINA = 25;

export function ResultadosBusca({ produtos, termo, exibirFotos }) {

  const [visiveis, setVisiveis] = useState(POR_PAGINA)

  const encontrados = filtrarProdutos(produtos, termo)

  if (encontrados.length === 0) {
    return (
      <EstadoVazio
        icone={SearchX}
        texto={'Nenhum produto encontrado para "' + termo + '". Confira a escrita ou busque pelo código.'}
      />
    )
  }

  return (
    <>
      <p className={estilos.contagem}>
        <span className={estilos.resultados}>Resultados</span>
        <span>{encontrados.length === 1 ? '1 produto' : encontrados.length + ' produtos'}</span>
      </p>
      <div className={estilos.grade}>
        {encontrados.slice(0, visiveis).map((produto) => (
          <CardProdutoResultado key={produto.id} produto={produto} exibirFoto={exibirFotos} />
        ))}
      </div>
      {visiveis < encontrados.length && (
        <div className={estilos.mais}>
          <Botao variante="secundario" onClick={() => setVisiveis(visiveis + POR_PAGINA)}>Carregar mais</Botao>
        </div>
      )}
    </>
  )
}
