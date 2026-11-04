import { http } from '@/shared/api';

export function listarMovimentacoes(filtros) {

    return http.get('/movimentacoes', { params: filtros }).then((r) => r.data);

}

export function criarMovimentacao(dados) {

    return http.post('/movimentacoes', dados).then((r) => r.data);

}
