export function calcularQuantidadeDepois(tipo, atual, quantidade) {

    if (tipo === 'ENTRADA') {
        return atual + quantidade;
    }

    if (tipo === 'SAIDA') {
        return atual - quantidade;
    }

    return quantidade;
}
