export function calcularSituacao(produto) {

    if (produto.quantidade <= 0) {
        return 'ZERADO';
    }

    if (produto.quantidade <= produto.estoqueMinimoEfetivo) {
        return 'BAIXO';
    }

    return 'OK';
}

function razaoDoMinimo(produto) {

    if (produto.estoqueMinimoEfetivo > 0) {
        return produto.quantidade / produto.estoqueMinimoEfetivo;
    }

    return Infinity;
}

export function ordenarPorCriticidade(lista) {

    return [...lista].sort((a, b) => {

        const aZerado = calcularSituacao(a) === 'ZERADO';
        const bZerado = calcularSituacao(b) === 'ZERADO';

        if (aZerado && !bZerado) {
            return -1;
        }

        if (bZerado && !aZerado) {
            return 1;
        }

        const razaoA = razaoDoMinimo(a);
        const razaoB = razaoDoMinimo(b);

        if (razaoA === razaoB) {
            return 0;
        }

        return razaoA < razaoB ? -1 : 1;
    });
}
