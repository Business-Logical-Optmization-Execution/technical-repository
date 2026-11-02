export function normalizarTexto(s) {

    if (!s) {
        return '';
    }

    return s.normalize('NFD').replace(/[̀-ͯ]/g, '').toLowerCase();
}

export function nomeProduto({ sku, nome }) {
    return sku + ' – ' + nome;
}
