import { FormSaida } from '@/features/registrar-movimentacao'
import { TelaMovimentacao } from '@/widgets/tela-movimentacao'

export function RegistrarSaidaPage() {
  return <TelaMovimentacao titulo="Registrar saída" Formulario={FormSaida} />
}
