import { Link } from 'react-router-dom'
import { History } from 'lucide-react'
import { ROTAS } from '@/shared/config'
import { obterMensagemErro } from '@/shared/api'
import { formatarDataHora, formatarQuantidade, useRequisicao } from '@/shared/lib'
import { Botao, EstadoVazio, Skeleton, Tabela } from '@/shared/ui'
import { useSessao } from '@/entities/sessao'
import { ROTULOS_TIPO, SeloOrigem, listarMovimentacoes, textoJustificativa } from '@/entities/movimentacao'
import estilos from './UltimasMovimentacoes.module.css'

const LIMITE = 10;

const COLUNAS = [
    { titulo: 'Data e hora', valor: (mov) => formatarDataHora(mov.dataMov) },
    { titulo: 'Tipo', valor: (mov) => ROTULOS_TIPO[mov.tipo] },
    { titulo: 'Qtd.', valor: (mov) => formatarQuantidade(mov.quantidade, { sinal: true }) },
    { titulo: 'Quantidade depois', valor: (mov) => formatarQuantidade(mov.quantidadeDepois) },
    { titulo: 'Quem', valor: (mov) => mov.usuario.nome },
    { titulo: 'Origem', valor: (mov) => <SeloOrigem origem={mov.origem} /> },
    { titulo: 'Motivo', valor: textoJustificativa },
];

export function UltimasMovimentacoes({ produtoId }) {

    const { ehAdmin } = useSessao();
    const { dados: movimentacoes, carregando, erro, recarregar } = useRequisicao(
        () => listarMovimentacoes({ produtoId, limite: LIMITE }),
        [produtoId],
    );

    function conteudo() {

        if (carregando) {
            return (
                <div aria-busy="true">
                    <p className="visualmente-oculto">Carregando movimentações...</p>
                    <Skeleton altura="136px" />
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

        if (movimentacoes.length === 0) {
            return <EstadoVazio icone={History} texto="Nenhuma movimentação registrada para este produto." />
        }

        return <Tabela legenda="Últimas movimentações do produto" colunas={COLUNAS} linhas={movimentacoes} />
    }

    return (
        <section className={estilos.secao} aria-labelledby="titulo-ultimas-movimentacoes">
            <div className={estilos.cabecalho}>
                <h2 id="titulo-ultimas-movimentacoes" className={estilos.titulo}>Últimas movimentações</h2>
                {ehAdmin && (
                    <Link to={ROTAS.historico + '?produtoId=' + produtoId} state={{ de: ROTAS.produto(produtoId) }} className={estilos.link}>
                        Ver histórico completo
                    </Link>
                )}
            </div>
            {conteudo()}
        </section>
    )
}
