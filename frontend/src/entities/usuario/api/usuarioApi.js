import { http } from '@/shared/api';

export function listarUsuarios(filtros) {

    return http.get('/usuarios', { params: filtros }).then((r) => r.data);

}
