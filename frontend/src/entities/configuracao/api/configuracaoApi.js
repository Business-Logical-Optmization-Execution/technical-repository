import { http } from '@/shared/api';

export function buscarConfiguracoes() {

    return http.get('/configuracoes').then((r) => r.data);

}
