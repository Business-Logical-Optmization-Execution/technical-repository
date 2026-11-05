import { calcularSituacao } from '@/entities/produto';

const UM_DIA = 24 * 60 * 60 * 1000;

function diasEntre(inicio, fim) {
    const inicioDoDia = new Date(inicio).setHours(0, 0, 0, 0);
    const fimDoDia = new Date(fim).setHours(0, 0, 0, 0);
    return Math.round((fimDoDia - inicioDoDia) / UM_DIA);
}

// Quem nunca movimentou não tem data conhecida e fica por último, com dias = null.
export function calcularTempoZerado(produtos, movimentacoes, agora = new Date()) {

    return produtos
        .filter((produto) => calcularSituacao(produto) === 'ZERADO')
        .map((produto) => {
            // A lista vem da mais recente para a mais antiga. Contagem sem diferença não muda o saldo.
            const zerou = movimentacoes.find((mov) => mov.produto.id === produto.id && mov.quantidade !== 0);
            return { produto, dias: zerou ? diasEntre(zerou.dataMov, agora) : null };
        })
        .sort((a, b) => (b.dias ?? -1) - (a.dias ?? -1));
}

export function calcularReposicao(produtos) {

    const grupos = {};

    produtos.forEach((produto) => {
        const falta = produto.estoqueMinimoEfetivo - Math.max(produto.quantidade, 0);

        if (falta > 0) {
            const chave = produto.fornecedor?.id ?? 'sem-fornecedor';
            grupos[chave] ??= { chave, nome: produto.fornecedor?.nome ?? null, total: 0, itens: [] };
            grupos[chave].total += falta;
            grupos[chave].itens.push({ produto, falta });
        }
    });

    return Object.values(grupos)
        .map((grupo) => ({ ...grupo, itens: grupo.itens.sort((a, b) => b.falta - a.falta) }))
        .sort((a, b) => {
            if (!a.nome || !b.nome) {
                return a.nome ? -1 : 1;
            }
            return b.total - a.total;
        });
}
