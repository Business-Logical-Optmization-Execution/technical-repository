import { useTituloDocumento } from '@/shared/lib'
import { Abas } from '@/shared/ui'
import { CampoBusca } from '@/features/buscar-produto'
import { PainelBaixoEstoque } from '@/widgets/painel-baixo-estoque'
import estilos from './InicioPage.module.css'

const ABAS = [
  { rotulo: 'Início', ativa: true },
  { rotulo: 'Cadastrar produto', desabilitada: true, title: 'Em breve' },
]

export function InicioPage() {

  useTituloDocumento('Início')

  return (
    <div className={estilos.pagina}>
      <h1 className="visualmente-oculto">Início</h1>
      <div className={estilos.topo}>
        <Abas itens={ABAS} rotulo="Seções do Início" />
        <CampoBusca focoAutomatico />
      </div>
      <PainelBaixoEstoque />
      {/* Widget "Avisos de falta" entra aqui (exercício 4.1). */}
    </div>
  )
}
