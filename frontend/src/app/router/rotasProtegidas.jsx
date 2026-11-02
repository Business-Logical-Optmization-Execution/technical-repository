import { useSessao } from '@/entities/sessao'
import { Outlet, Navigate, useLocation } from 'react-router-dom'
import { ROTAS } from '@/shared/config'
import { AcessoNegadoPage } from '@/pages/acesso-negado'
import { useState } from 'react'
export function ExigeLogin() {

    const { usuario } = useSessao();
    const location = useLocation();

    const [chegouLogado] = useState(Boolean(usuario));

    if (!usuario) {
        return <Navigate to={ROTAS.login} state={chegouLogado ? undefined : { de: location }} replace />
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