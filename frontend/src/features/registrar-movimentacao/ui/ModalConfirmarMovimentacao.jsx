import { TriangleAlert } from 'lucide-react'
import { Botao, Modal } from '@/shared/ui'
import { fraseConfirmacao } from '@/entities/movimentacao'
import estilos from './ModalConfirmarMovimentacao.module.css'

const TITULOS = {
    ENTRADA: 'Confirmar entrada?',
    SAIDA: 'Confirmar saída?',
    AJUSTE: 'Confirmar ajuste?',
};

const ERRO_DE_REDE = 'Não foi possível registrar. Seus dados foram mantidos.';

export function ModalConfirmarMovimentacao({ aberto, tipo, quantidade, produto, enviando, erro, aoVoltar, aoConfirmar }) {

    function fechar() {
        if (!enviando) {
            aoVoltar();
        }
    }

    return (
        <Modal
            aberto={aberto}
            aoFechar={fechar}
            titulo={TITULOS[tipo]}
            acoes={
                <>
                    <Botao variante="secundario" disabled={enviando} onClick={fechar}>Voltar</Botao>
                    <Botao carregando={enviando} textoCarregando="Salvando..." onClick={aoConfirmar}>
                        {erro ? 'Tentar de novo' : 'Confirmar alteração'}
                    </Botao>
                </>
            }
        >
            <div className={estilos.corpo}>
                <p>{fraseConfirmacao({ tipo, quantidade, produto, atual: produto.quantidade })}</p>
                {erro && (
                    <p className={estilos.erro} role="alert">
                        <TriangleAlert size={16} aria-hidden="true" />
                        {erro.response?.data?.mensagem ?? ERRO_DE_REDE}
                    </p>
                )}
            </div>
        </Modal>
    )
}
