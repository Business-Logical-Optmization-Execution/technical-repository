import { http } from '@/shared/api';

export function buscarResumo() {

    return http.get('/dashboard/resumo').then((r) => r.data);

}
