import { Link, useLocation, useParams } from 'react-router-dom'
import { PackageX } from 'lucide-react'
import { ROTAS } from '@/shared/config'
import { obterMensagemErro } from '@/shared/api'
import { useRequisicao } from '@/shared/lib'
import { Botao, EstadoVazio, TituloPagina } from '@/shared/ui'
import { useSessao } from '@/entities/sessao'
import { buscarProduto } from '@/entities/produto'
import { CartaoProduto, CartaoProdutoEsqueleto } from '@/widgets/cartao-produto'
import estilos from './ProdutoDetalhePage.module.css'

export function ProdutoDetalhePage() {

  const { id } = useParams()
  const location = useLocation()
  const { ehAdmin } = useSessao()
  const { dados: produto, carregando, erro, recarregar } = useRequisicao(() => buscarProduto(id), [id])

  const voltarPara = location.state?.de ?? ROTAS.busca
  const naoEncontrado = erro?.response?.status === 404

  return (
    <div className={estilos.pagina}>
      <TituloPagina titulo="Detalhe do produto" voltarPara={voltarPara} admin={ehAdmin} />

      {carregando && <CartaoProdutoEsqueleto />}

      {!carregando && naoEncontrado && (
        <EstadoVazio
          icone={PackageX}
          texto="Produto não encontrado."
          acao={<Link to={ROTAS.inicio} className={estilos.link}>Ir para o Início</Link>}
        />
      )}

      {!carregando && erro && !naoEncontrado && (
        <div className={estilos.erro}>
          <p>{obterMensagemErro(erro)}</p>
          <Botao variante="secundario" onClick={recarregar}>Tentar de novo</Botao>
        </div>
      )}

      {!carregando && !erro && produto && (
        <>
          <CartaoProduto produto={produto} />
          <div className={estilos.movimentacoes}>
            <Botao como={Link} to={ROTAS.registrarEntrada(produto.id)}>Registrar entrada</Botao>
            <Botao variante="secundario" disabled title="Em breve">Registrar saída</Botao>
            <Botao variante="secundario" disabled title="Em breve">Ajustar por contagem</Botao>
            <Botao variante="secundario" disabled title="Em breve">Avisar que está em falta</Botao>
          </div>
        </>
      )}
    </div>
  )
}
