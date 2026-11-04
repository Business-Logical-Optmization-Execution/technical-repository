import { formatarQuantidade, nomeProduto } from '@/shared/lib';
import { MOTIVOS_SAIDA } from '../config/motivos';
import { calcularQuantidadeDepois } from './calcular';

export function fraseConfirmacao({ tipo, quantidade, produto, atual }) {

    const nome = nomeProduto(produto);
    const depois = calcularQuantidadeDepois(tipo, atual, quantidade);
    const mudanca = 'A quantidade vai de ' + atual + ' para ' + depois + '.';

    if (tipo === 'AJUSTE') {
        return 'Ajuste de ' + nome + '. ' + mudanca;
    }

    const rotulo = tipo === 'ENTRADA' ? 'Entrada' : 'Saída';

    return rotulo + ' de ' + formatarQuantidade(quantidade) + ' de ' + nome + '. ' + mudanca;
}

export function fraseSucesso({ tipo, quantidadeDepois }) {

    const registro = { ENTRADA: 'Entrada registrada', SAIDA: 'Saída registrada', AJUSTE: 'Ajuste registrado' };

    return registro[tipo] + '. Quantidade agora: ' + formatarQuantidade(quantidadeDepois);
}

// Devolve o texto em volta do nome do produto, porque a tela mostra o nome como link.
export function fraseHistorico({ tipo, quantidade, quantidadeDepois, motivo, usuario }) {

    const movimento = formatarQuantidade(quantidade, { sinal: true });

    if (tipo === 'ENTRADA') {
        return { antes: usuario.nome + ' adicionou ' + movimento + ' de ', depois: '' };
    }
    if (tipo === 'SAIDA') {
        const rotuloMotivo = MOTIVOS_SAIDA.find((item) => item.valor === motivo)?.rotulo;
        return { antes: usuario.nome + ' removeu ' + movimento + ' de ', depois: rotuloMotivo ? ' (' + rotuloMotivo.toLowerCase() + ')' : '' };
    }
    if (tipo === 'AJUSTE') {
        return { antes: usuario.nome + ' ajustou ', depois: ' para ' + formatarQuantidade(quantidadeDepois) };
    }
    if (tipo === 'ESTORNO') {
        return { antes: usuario.nome + ' estornou ' + movimento + ' de ', depois: '' };
    }

    return { antes: 'Saldo inicial de ' + formatarQuantidade(quantidade) + ' de ', depois: '' };
}

export function textoJustificativa({ motivo, documento, observacao }) {

    const rotuloMotivo = MOTIVOS_SAIDA.find((item) => item.valor === motivo)?.rotulo;
    const partes = [rotuloMotivo, documento, observacao].filter(Boolean);

    return partes.length > 0 ? partes.join(' — ') : '—';
}
