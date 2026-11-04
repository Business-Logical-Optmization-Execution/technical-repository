import { PackageCheck } from 'lucide-react'
import { obterMensagemErro } from '@/shared/api'
import { useRequisicao } from '@/shared/lib'
import { Botao, EstadoVazio, Skeleton } from '@/shared/ui'
import { useExibirFotos } from '@/entities/configuracao'
import { CardProdutoBaixo, calcularSituacao, listarProdutos, ordenarPorCriticidade } from '@/entities/produto'
import estilos from './PainelBaixoEstoque.module.css'

const MAXIMO_CARDS = 6;

export function PainelBaixoEstoque() {

    const { dados: produtos, carregando, erro, recarregar } = useRequisicao(listarProdutos);
    const { exibirFotos, carregando: carregandoConfiguracao } = useExibirFotos();

    const produtosBaixos = produtos
        ? ordenarPorCriticidade(produtos.filter((produto) => calcularSituacao(produto) !== 'OK')).slice(0, MAXIMO_CARDS)
        : [];

    function conteudo() {

        if (carregando || carregandoConfiguracao) {
            return (
                <div className={estilos.grade} aria-busy="true">
                    <p className="visualmente-oculto">Carregando produtos...</p>
                    {Array.from({ length: MAXIMO_CARDS }, (_, indice) => <Skeleton key={indice} altura="260px" />)}
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

        if (produtosBaixos.length === 0) {
            return <EstadoVazio icone={PackageCheck} texto="Nenhum produto com baixo estoque agora." />
        }

        return (
            <div className={estilos.grade}>
                {produtosBaixos.map((produto) => (
                    <CardProdutoBaixo key={produto.id} produto={produto} exibirFoto={exibirFotos} />
                ))}
            </div>
        )
    }

    return (
        <section className={estilos.painel} aria-labelledby="titulo-baixo-estoque">
            <div className={estilos.cabecalho}>
                <h2 id="titulo-baixo-estoque" className={estilos.titulo}>Produtos com baixo estoque</h2>
                <p className={estilos.legenda}>Abaixo da qtd. mínima</p>
            </div>
            {conteudo()}
        </section>
    )
}
