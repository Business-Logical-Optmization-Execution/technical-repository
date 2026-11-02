import { useSessao } from '@/entities/sessao'
import { Outlet, Navigate, useLocation } from 'react-router-dom'
import { ROTAS } from '@/shared/config'
import { AcessoNegadoPage } from '@/pages/acesso-negado'
export function ExigeLogin() {

    const { usuario } = useSessao();
    const location = useLocation();

    if (!usuario) {
        return <Navigate to={ROTAS.login} state={{ de: location }} replace />
    }

    return <Outlet />


}

export function ExigeAdmin() {

    const {ehAdmin} = useSessao();

    if (!ehAdmin) {
        return <AcessoNegadoPage />
    }

    return <Outlet />

}