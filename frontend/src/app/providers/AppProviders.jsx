import { ToastProvider } from '@/shared/ui'
import { SessaoProvider } from '@/entities/sessao'

export function AppProviders({ children }) {
  return (
  <SessaoProvider>
    <ToastProvider>
      {children}
    </ToastProvider>
  </SessaoProvider>
  )
}
