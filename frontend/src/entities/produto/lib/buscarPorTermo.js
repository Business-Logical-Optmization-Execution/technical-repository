import { normalizarTexto } from '@/shared/lib';

export function filtrarProdutos(lista, termo) {

    const palavras = normalizarTexto(termo).split(/\s+/).filter(Boolean);

    if (palavras.length === 0) {
        return lista;
    }

    return lista.filter((produto) => {
        const localizacoes = produto.localizacoes ?? [];
        const texto = normalizarTexto([produto.nome, produto.sku, ...localizacoes].join(' '));

        return palavras.every((palavra) => texto.includes(palavra));
    });
}
