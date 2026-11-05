import { TriangleAlert } from 'lucide-react'
import { Selo } from '@/shared/ui'
import { calcularSituacao } from '../model/situacao'

export function SeloSituacao({ produto, mostrarNome = false }) {

    const situacao = calcularSituacao(produto);

    if (situacao === 'ZERADO') {
        return (
            <Selo variante="erro">
                {mostrarNome ? 'Zerado' : 'Qtd ' + produto.quantidade}
                {!mostrarNome && <span className="visualmente-oculto"> (estoque zerado)</span>}
            </Selo>
        )
    }

    if (situacao === 'BAIXO') {
        return (
            <Selo variante="alerta" icone={TriangleAlert}>
                {mostrarNome ? 'Baixo' : 'Qtd ' + produto.quantidade}
                {!mostrarNome && <span className="visualmente-oculto"> (estoque baixo)</span>}
            </Selo>
        )
    }

    return null;
}
