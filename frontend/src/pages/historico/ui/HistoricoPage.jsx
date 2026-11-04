import { useLocation, useSearchParams } from 'react-router-dom'
import { ROTAS } from '@/shared/config'
import { TituloPagina } from '@/shared/ui'
import { calcularPeriodo } from '../lib/periodo'
import { FiltrosHistorico } from './FiltrosHistorico'
import { ListaHistorico } from './ListaHistorico'
import estilos from './HistoricoPage.module.css'

export function HistoricoPage() {

  const location = useLocation()
  const [parametros, setParametros] = useSearchParams()

  const valores = {
    produtoId: parametros.get('produtoId') || undefined,
    usuarioId: parametros.get('usuarioId') || undefined,
    periodo: parametros.get('periodo') || undefined,
    tipo: parametros.get('tipo') || undefined,
  }

  const { periodo, ...demais } = valores
  const filtros = { ...demais, ...calcularPeriodo(periodo) }

  function aoMudar(chave, valor) {

    const novos = new URLSearchParams(parametros)

    if (valor) {
      novos.set(chave, valor)
    } else {
      novos.delete(chave)
    }

    setParametros(novos, { replace: true, state: location.state })
  }

  return (
    <div className={estilos.pagina}>
      <TituloPagina titulo="Histórico" admin voltarPara={location.state?.de ?? ROTAS.inicio} />
      <FiltrosHistorico valores={valores} aoMudar={aoMudar} />
      <ListaHistorico key={location.search} filtros={filtros} />
    </div>
  )
}
