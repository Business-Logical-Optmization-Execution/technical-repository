import { obterMensagemErro } from '@/shared/api'
import { ROTAS } from '@/shared/config'
import { useRequisicao } from '@/shared/lib'
import { Botao, Card, Skeleton, TituloPagina } from '@/shared/ui'
import { buscarConfiguracoes } from '@/entities/configuracao'
import { FormConfiguracoes } from '@/features/salvar-configuracoes'
import estilos from './ConfiguracoesPage.module.css'

export function ConfiguracoesPage() {

  const { dados, carregando, erro, recarregar } = useRequisicao(buscarConfiguracoes)

  function conteudo() {

    if (carregando) {
      return <Skeleton altura="320px" />
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
      <>
        <Card>
          <FormConfiguracoes configuracao={dados} />
        </Card>
        <p className={estilos.nota}>O mínimo próprio de um produto é definido em Editar, no detalhe do produto.</p>
      </>
    )
  }

  return (
    <div className={estilos.pagina}>
      <TituloPagina
        titulo="Configurações"
        admin
        voltarPara={ROTAS.inicio}
      />
      {conteudo()}
    </div>
  )
}
