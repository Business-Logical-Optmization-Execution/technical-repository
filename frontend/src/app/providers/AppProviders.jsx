import { ToastProvider } from '@/shared/ui'

// Junta os providers globais. O SessaoProvider entra no exercício 2.1.
export function AppProviders({ children }) {
  return <ToastProvider>{children}</ToastProvider>
}
