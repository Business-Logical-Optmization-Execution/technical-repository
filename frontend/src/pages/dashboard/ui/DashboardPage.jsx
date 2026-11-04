import { TriangleAlert } from 'lucide-react'
import { ROTAS } from '@/shared/config'
import { obterMensagemErro } from '@/shared/api'
import { formatarHora, useRequisicao } from '@/shared/lib'
import { Botao, Card, Skeleton, TituloPagina } from '@/shared/ui'
import { listarMovimentacoes } from '@/entities/movimentacao'
import { listarProdutos } from '@/entities/produto'
import { buscarResumo } from '../api/dashboardApi'
import { calcularReposicao, calcularTempoZerado } from '../lib/indicadoresEstoque'
import { CardIndicador } from './CardIndicador'
import { GraficoEntradasSaidas } from './GraficoEntradasSaidas'
import { ListaMaisMovimentados } from './ListaMaisMovimentados'
import { ListaReposicao } from './ListaReposicao'
import { ListaTempoZerado } from './ListaTempoZerado'
import estilos from './DashboardPage.module.css'

function buscarComHorario() {
  return Promise.all([buscarResumo(), listarProdutos(), listarMovimentacoes()])
    .then(([resumo, produtos, movimentacoes]) => ({
      resumo,
      tempoZerado: calcularTempoZerado(produtos, movimentacoes),
      reposicao: calcularReposicao(produtos),
      atualizadoEm: new Date(),
    }))
}

export function DashboardPage() {

  const { dados, carregando, erro, recarregar } = useRequisicao(buscarComHorario)

  function conteudo() {

    if (erro) {
      return (
        <div className={estilos.erro}>
          <p>{obterMensagemErro(erro)}</p>
          <Botao variante="secundario" onClick={recarregar}>Tentar de novo</Botao>
        </div>
      )
    }

    if (!dados) {
      return (
        <div className={estilos.esqueleto} aria-busy="true">
          <p className="visualmente-oculto">Carregando indicadores...</p>
          <div className={estilos.indicadores}>
            {Array.from({ length: 4 }, (_, indice) => <Skeleton key={indice} altura="112px" />)}
          </div>
          <Skeleton altura="400px" />
        </div>
      )
    }

    const { resumo } = dados

    return (
      <>
        <div className={estilos.indicadores}>
          <CardIndicador rotulo="Produtos ativos" valor={resumo.totalProdutosAtivos} />
          <CardIndicador rotulo="Com estoque baixo" valor={resumo.produtosBaixoEstoque} icone={TriangleAlert} tom="alerta" />
          <CardIndicador rotulo="Zerados" valor={resumo.produtosZerados} tom="erro" />
          <CardIndicador rotulo="Movimentações hoje" valor={resumo.movimentacoesHoje} />
        </div>
        <div className={estilos.paineis}>
          <Card>
            <h2 className={estilos.tituloPainel}>Entradas e saídas — últimos 7 dias</h2>
            <GraficoEntradasSaidas dias={resumo.entradasSaidas7Dias} />
          </Card>
          <Card>
            <h2 className={estilos.tituloPainel}>Mais movimentados</h2>
            <p className={estilos.explicacao}>Total de unidades movimentadas em todo o histórico.</p>
            <ListaMaisMovimentados itens={resumo.maisMovimentados} />
          </Card>
        </div>
        <div className={estilos.paineisIguais}>
          <Card>
            <h2 className={estilos.tituloPainel}>Reposição por fornecedor</h2>
            <p className={estilos.explicacao}>Unidades que faltam para cada produto voltar ao estoque mínimo.</p>
            <ListaReposicao grupos={dados.reposicao} />
          </Card>
          <Card>
            <h2 className={estilos.tituloPainel}>Tempo zerado</h2>
            <p className={estilos.explicacao}>Contado a partir do último registro que mudou a quantidade.</p>
            <ListaTempoZerado itens={dados.tempoZerado} />
          </Card>
        </div>
      </>
    )
  }

  return (
    <div className={estilos.pagina}>
      <TituloPagina
        titulo="Dashboard"
        admin
        voltarPara={ROTAS.inicio}
        acoes={
          <div className={estilos.acoes}>
            {dados && <span className={estilos.horario}>Atualizado às {formatarHora(dados.atualizadoEm)}</span>}
            <Botao variante="secundario" carregando={carregando} textoCarregando="Atualizando..." onClick={recarregar}>Atualizar</Botao>
          </div>
        }
      />
      {conteudo()}
    </div>
  )
}
