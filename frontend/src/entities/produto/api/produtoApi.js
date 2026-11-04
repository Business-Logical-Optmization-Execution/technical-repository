import { http } from '@/shared/api';

export function listarProdutos(filtros) {

    return http.get('/produtos', { params: filtros }).then((r) => r.data);

}

export function buscarProduto(id) {

    return http.get('/produtos/' + id).then((r) => r.data);

}
