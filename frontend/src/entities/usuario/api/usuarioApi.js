import { http } from '@/shared/api';

export function listarUsuarios(filtros) {

    return http.get('/usuarios', { params: filtros }).then((r) => r.data);

}

export function criarUsuario(dados) {

    return http.post('/usuarios', dados).then((r) => r.data);

}

export function atualizarUsuario(id, dados) {

    return http.put('/usuarios/' + id, dados).then((r) => r.data);

}

export function desativarUsuario(id) {

    return http.delete('/usuarios/' + id);

}

export function listarPerfis() {

    return http.get('/perfis-acesso').then((r) => r.data);

}

