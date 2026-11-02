import { useTituloDocumento } from '@/shared/lib'

export function AcessoNegadoPage() {
  useTituloDocumento('Acesso negado')

  return <h1>Acesso negado</h1>
}
