import { Selo } from '@/shared/ui'
import { ROTULOS_ORIGEM } from '../config/motivos'

export function SeloOrigem({ origem }) {

    return <Selo variante="informacao">{ROTULOS_ORIGEM[origem]}</Selo>
}
