import { FormAjuste } from '@/features/registrar-movimentacao'
import { TelaMovimentacao } from '@/widgets/tela-movimentacao'

export function AjustarContagemPage() {
  return <TelaMovimentacao titulo="Ajustar por contagem" Formulario={FormAjuste} />
}
