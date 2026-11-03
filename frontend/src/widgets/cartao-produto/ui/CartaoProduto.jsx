import { LockKeyhole, Pencil } from 'lucide-react'
import { useSessao } from '@/entities/sessao'
import { FotoProduto, SeloSituacao, formatarLocalizacoes } from '@/entities/produto'
import { Botao, Card } from '@/shared/ui'
import { formatarMoeda, formatarQuantidade } from '@/shared/lib'
import { CampoLeitura } from './CampoLeitura'
import estilos from './CartaoProduto.module.css'

function textoEstoqueMinimo(produto) {

    if (produto.estoqueMinimo === null || produto.estoqueMinimo === undefined) {
        return 'Padrão: ' + formatarQuantidade(produto.estoqueMinimoEfetivo);
    }

    return formatarQuantidade(produto.estoqueMinimo);
}

export function CartaoProduto({ produto }) {

    const { ehAdmin } = useSessao();

    return (
        <Card>
            <div className={estilos.corpo}>
                <FotoProduto produto={produto} className={estilos.foto} />
                <dl className={estilos.campos}>
                    <CampoLeitura rotulo="Nome" className={estilos.inteiro}>
                        {produto.nome}
                    </CampoLeitura>
                    <CampoLeitura rotulo="ID" className={estilos.terco}>
                        <span className={estilos.codigo}>{produto.sku}</span>
                    </CampoLeitura>
                    <CampoLeitura rotulo="Qtd" className={estilos.terco}>
                        <span className={estilos.quantidade}>
                            <LockKeyhole aria-hidden="true" size={20} className={estilos.cadeado} />
                            <span>{formatarQuantidade(produto.quantidade)}</span>
                            <span className="visualmente-oculto">(muda só por movimentação)</span>
                            <SeloSituacao produto={produto} mostrarNome />
                        </span>
                    </CampoLeitura>
                    <CampoLeitura rotulo="Valor base" className={estilos.terco}>
                        {formatarMoeda(produto.preco)}
                    </CampoLeitura>
                    <CampoLeitura rotulo="Descrição" className={estilos.inteiro}>
                        {produto.descricao || '—'}
                    </CampoLeitura>
                    <CampoLeitura rotulo="Localização" className={estilos.metade}>
                        {formatarLocalizacoes(produto)}
                    </CampoLeitura>
                    <CampoLeitura rotulo="Estoque mínimo" className={estilos.metade}>
                        {textoEstoqueMinimo(produto)}
                    </CampoLeitura>
                </dl>
            </div>
            {ehAdmin && (
                <div className={estilos.rodape}>
                    <Botao variante="secundario" icone={Pencil} disabled title="Em breve">Editar</Botao>
                    <Botao variante="destrutivo" disabled title="Em breve">Inativar produto</Botao>
                </div>
            )}
        </Card>
    )
}
