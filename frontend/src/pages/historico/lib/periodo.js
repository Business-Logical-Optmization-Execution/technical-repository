const DIAS_ANTERIORES = { hoje: 0, 7: 6, 30: 29 };

function formatarDia(data) {
    const p = (n) => String(n).padStart(2, '0');
    return data.getFullYear() + '-' + p(data.getMonth() + 1) + '-' + p(data.getDate());
}

export function calcularPeriodo(periodo, agora = new Date()) {

    const dias = DIAS_ANTERIORES[periodo];

    if (dias === undefined) {
        return {};
    }

    const inicio = new Date(agora);
    inicio.setDate(agora.getDate() - dias);

    return { de: formatarDia(inicio), ate: formatarDia(agora) };
}
