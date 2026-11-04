import { ROTAS } from '@/shared/config';

export const itensMenu = [
    { rotulo: 'Perfil', rota: ROTAS.perfil, somenteAdmin: false },
    { rotulo: 'Dashboard', rota: ROTAS.dashboard, somenteAdmin: true },
    { rotulo: 'Configurações', rota: ROTAS.configuracoes, somenteAdmin: true },
    { rotulo: 'Histórico', rota: ROTAS.historico, somenteAdmin: true },
    { rotulo: 'Divergências', rota: ROTAS.divergencias, somenteAdmin: true },
    { rotulo: 'Integração com o Bling', rota: ROTAS.integracao, somenteAdmin: true },
    { rotulo: 'Importar produtos', rota: ROTAS.importar, somenteAdmin: true },
    { rotulo: 'Usuários', rota: ROTAS.usuarios, somenteAdmin: true },
    { rotulo: 'Buscar', rota: ROTAS.busca, somenteAdmin: false },
];