import { http } from '@/shared/api';

export function buscarConfiguracoes() {

    return http.get('/configuracoes').then((r) => r.data);

}

export function salvarConfiguracoes(dados) {

    return http.put('/configuracoes', dados).then((r) => r.data);

}
