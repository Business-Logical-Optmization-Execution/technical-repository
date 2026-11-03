export function formatarLocalizacoes(produto) {

    const localizacoes = produto.localizacoes ?? [];

    if (localizacoes.length === 0) {
        return 'Sem localização';
    }

    return localizacoes.join(' · ');
}
