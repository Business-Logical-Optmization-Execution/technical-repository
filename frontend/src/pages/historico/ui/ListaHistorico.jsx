import { useState } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { History } from 'lucide-react'
import { ROTAS } from '@/shared/config'
import { obterMensagemErro } from '@/shared/api'
import { formatarDataHora, useRequisicao } from '@/shared/lib'
import { Avatar, Botao, Card, EstadoVazio, Skeleton } from '@/shared/ui'
import { fraseHistorico, listarMovimentacoes } from '@/entities/movimentacao'
import estilos from './ListaHistorico.module.css'

const POR_VEZ = 25;

export function ListaHistorico({ filtros }) {

  const location = useLocation()
  const [limite, setLimite] = useState(POR_VEZ)
  const { dados: movimentacoes, carregando, erro, recarregar } = useRequisicao(
    () => listarMovimentacoes({ ...filtros, limite }),
    [limite],
  )

  if (erro) {
    return (
      <div className={estilos.erro}>
        <p>{obterMensagemErro(erro)}</p>
        <Botao variante="secundario" onClick={recarregar}>Tentar de novo</Botao>
      </div>
    )
  }

  if (!movimentacoes) {
    return (
      <div aria-busy="true">
        <p className="visualmente-oculto">Carregando movimentações...</p>
        <Skeleton altura="320px" />
      </div>
    )
  }

  if (movimentacoes.length === 0) {
    return <EstadoVazio icone={History} texto="Nenhuma movimentação encontrada." />
  }

  return (
    <Card>
      <ul className={estilos.lista}>
        {movimentacoes.map((mov) => {
          const { antes, depois } = fraseHistorico(mov)
          return (
            <li key={mov.id} className={estilos.item}>
              <Avatar />
              <p className={estilos.frase}>
                {antes}
                <Link to={ROTAS.produto(mov.produto.id)} state={{ de: location.pathname + location.search }} className={estilos.link}>
                  {mov.produto.nome}
                </Link>
                {depois}
              </p>
              <time className={estilos.quando} dateTime={mov.dataMov}>{formatarDataHora(mov.dataMov)}</time>
            </li>
          )
        })}
      </ul>
      {movimentacoes.length === limite && (
        <div className={estilos.rodape}>
          <Botao variante="secundario" carregando={carregando} textoCarregando="Carregando..." onClick={() => setLimite(limite + POR_VEZ)}>
            Carregar mais
          </Botao>
        </div>
      )}
    </Card>
  )
}
