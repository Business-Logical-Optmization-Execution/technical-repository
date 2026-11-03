import { http } from '@/shared/api';

export function listarProdutos() {

    return http.get('/produtos').then((r) => r.data);

}

export function buscarProduto(id) {

    return http.get('/produtos/' + id).then((r) => r.data);

}
