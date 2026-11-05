import { useNavigate, useSearchParams } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'
import { ROTAS } from '@/shared/config'
import { obterMensagemErro } from '@/shared/api'
import { useRequisicao, useTituloDocumento } from '@/shared/lib'
import { Botao, BotaoIcone, Skeleton } from '@/shared/ui'
import { useExibirFotos } from '@/entities/configuracao'
import { listarProdutos } from '@/entities/produto'
import { CampoBusca } from '@/features/buscar-produto'
import { ResultadosBusca } from './ResultadosBusca'
import estilos from './BuscaPage.module.css'

export function BuscaPage() {

  useTituloDocumento('Resultado da busca')

  const navegar = useNavigate()
  const [parametros] = useSearchParams()
  const termo = (parametros.get('q') ?? '').trim()
  const { dados: produtos, carregando, erro, recarregar } = useRequisicao(listarProdutos)
  const { exibirFotos, carregando: carregandoConfiguracao } = useExibirFotos()

  function resultados() {

    if (!termo) {
      return null
    }

    if (carregando || carregandoConfiguracao) {
      return (
        <div className={estilos.grade} aria-busy="true">
          <p className="visualmente-oculto">Carregando resultados...</p>
          {Array.from({ length: 4 }, (_, indice) => <Skeleton key={indice} altura="244px" />)}
        </div>
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

    return <ResultadosBusca key={termo} produtos={produtos} termo={termo} exibirFotos={exibirFotos} />
  }

  return (
    <div className={estilos.pagina}>
      <h1 className="visualmente-oculto">Resultado da busca</h1>
      <div className={estilos.barra}>
        <div className={estilos.voltar}>
          <BotaoIcone icone={ArrowLeft} rotulo="Voltar" onClick={() => navegar(ROTAS.inicio)} />
        </div>
        <div className={estilos.campo}>
          <CampoBusca
            comBotao
            valorInicial={termo}
            focoAutomatico={!termo}
            placeholder="Pesquisar produto por nome ou ID"
          />
        </div>
      </div>
      {resultados()}
    </div>
  )
}
