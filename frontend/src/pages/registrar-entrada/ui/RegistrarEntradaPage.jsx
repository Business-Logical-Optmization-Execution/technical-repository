import { FormEntrada } from '@/features/registrar-movimentacao'
import { TelaMovimentacao } from '@/widgets/tela-movimentacao'

export function RegistrarEntradaPage() {
  return <TelaMovimentacao titulo="Registrar entrada" Formulario={FormEntrada} />
}
