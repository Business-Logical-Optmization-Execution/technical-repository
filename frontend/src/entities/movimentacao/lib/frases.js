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

export function textoJustificativa({ motivo, documento, observacao }) {

    const rotuloMotivo = MOTIVOS_SAIDA.find((item) => item.valor === motivo)?.rotulo;
    const partes = [rotuloMotivo, documento, observacao].filter(Boolean);

    return partes.length > 0 ? partes.join(' — ') : '—';
}
