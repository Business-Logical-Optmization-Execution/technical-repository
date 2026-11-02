import { http } from '@/shared/api';

export function entrar({email, senha}) {

    return http.post('/auth/login', { email, senha }).then((r) => r.data);

}