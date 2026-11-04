const numero = new Intl.NumberFormat('pt-BR');
const moeda = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });
const data = new Intl.DateTimeFormat('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric' });
const hora = new Intl.DateTimeFormat('pt-BR', { hour: '2-digit', minute: '2-digit' });

export function formatarQuantidade(n, { sinal = false } = {}) {

    let prefixo = '';
    if (n < 0) {
        prefixo = '−';
    } else if (sinal && n > 0) {
        prefixo = '+';
    }

    return prefixo + numero.format(Math.abs(n)) + ' un.';
}

export function formatarMoeda(valor) {

    if (valor === null || valor === undefined) {
        return '—';
    }

    return moeda.format(valor);
}

export function formatarHora(dataHora) {

    return hora.format(dataHora);
}

export function formatarDataHora(iso) {

    const dataHora = new Date(iso);

    return data.format(dataHora) + ' · ' + hora.format(dataHora);
}

function mesmoDia(a, b) {
    return a.getFullYear() === b.getFullYear()
        && a.getMonth() === b.getMonth()
        && a.getDate() === b.getDate();
}
export function formatarDataRelativa(iso, agora = new Date()) {

    const dataHora = new Date(iso);
    const ontem = new Date(agora);
    ontem.setDate(agora.getDate() - 1);

    if (mesmoDia(dataHora, agora)) {
        return 'hoje ' + hora.format(dataHora);
    } else if (mesmoDia(dataHora, ontem)) {
        return 'ontem ' + hora.format(dataHora);
    }

    return formatarDataHora(iso);
}
