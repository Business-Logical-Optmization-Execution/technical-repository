import { Link, useLocation, useNavigate, useParams } from 'react-router-dom'
import { PackageX } from 'lucide-react'
import { ROTAS } from '@/shared/config'
import { obterMensagemErro } from '@/shared/api'
import { useRequisicao } from '@/shared/lib'
import { Botao, Card, EstadoVazio, Skeleton, TituloPagina } from '@/shared/ui'
import { buscarProduto } from '@/entities/produto'
import { FormEntrada, ResumoProduto } from '@/features/registrar-movimentacao'
import estilos from './RegistrarEntradaPage.module.css'

export function RegistrarEntradaPage() {

  const { id } = useParams()
  const location = useLocation()
  const navegar = useNavigate()
  const { dados: produto, carregando, erro, recarregar } = useRequisicao(() => buscarProduto(id), [id])

  const voltarPara = ROTAS.produto(id)
  const naoEncontrado = erro?.response?.status === 404

  function voltarAoDetalhe() {
    navegar(voltarPara, { state: location.state })
  }

  function conteudo() {

    if (carregando) {
      return (
        <div aria-busy="true">
          <p className="visualmente-oculto">Carregando produto...</p>
          <Skeleton altura="496px" />
        </div>
      )
    }

    if (naoEncontrado) {
      return (
        <EstadoVazio
          icone={PackageX}
          texto="Produto não encontrado."
          acao={<Link to={ROTAS.inicio} className={estilos.link}>Ir para o Início</Link>}
        />
      )
    }

    if (erro) {
      return (
        <div className={estilos.erro}>
          <p>{obterMensagemErro(erro)}</p>
          <Botao variante="secundario" onClick={recarregar}>Tentar de novo</Botao>
        </div>
      )
    }

    return (
      <Card>
        <div className={estilos.card}>
          <ResumoProduto produto={produto} />
          <FormEntrada produto={produto} aoConcluir={voltarAoDetalhe} aoCancelar={voltarAoDetalhe} />
        </div>
      </Card>
    )
  }

  return (
    <div className={estilos.pagina}>
      <TituloPagina titulo="Registrar entrada" voltarPara={voltarPara} estadoVoltar={location.state} />
      {conteudo()}
    </div>
  )
}
