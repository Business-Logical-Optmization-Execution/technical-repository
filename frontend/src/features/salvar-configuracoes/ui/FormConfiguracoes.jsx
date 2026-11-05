import { useId, useState } from 'react'
import { TriangleAlert } from 'lucide-react'
import { obterErrosDeCampo, obterMensagemErro } from '@/shared/api'
import { Botao, CaixaMarcar, Faixa, useToast } from '@/shared/ui'
import { useSalvarConfiguracoes } from '../model/useSalvarConfiguracoes'
import estilos from './FormConfiguracoes.module.css'

const SOMENTE_DIGITOS = /^\d+$/;

function textoMinimoProprio(quantidade) {

    const produtos = quantidade === 1 ? '1 produto' : quantidade + ' produtos';

    return produtos + ' com mínimo próprio · os demais usam o mínimo global';
}

export function FormConfiguracoes({ configuracao }) {

    const id = useId()
    const idAjuda = id + '-ajuda'
    const idErro = id + '-erro'

    const toast = useToast()
    const { salvar, enviando, erro: erroEnvio } = useSalvarConfiguracoes()
    const [minimo, setMinimo] = useState(String(configuracao.estoqueMinimoGlobal))
    const [exibirFotos, setExibirFotos] = useState(configuracao.exibirFotos)
    const [erroMinimo, setErroMinimo] = useState(null)
    const [erroGeral, setErroGeral] = useState(false)

    async function aoEnviar(evento) {

        evento.preventDefault()
        setErroGeral(false)

        const minimoLimpo = minimo.trim()

        if (!SOMENTE_DIGITOS.test(minimoLimpo)) {
            setErroMinimo('Use um número inteiro igual ou maior que zero.')
            return
        }

        setErroMinimo(null)

        try {
            const salvo = await salvar({ estoqueMinimoGlobal: Number(minimoLimpo), exibirFotos })
            setMinimo(String(salvo.estoqueMinimoGlobal))
            setExibirFotos(salvo.exibirFotos)
            toast.sucesso('Configuração salva.')
        } catch (e) {
            const errosDoBack = obterErrosDeCampo(e)

            if (errosDoBack.estoqueMinimoGlobal) {
                setErroMinimo(errosDoBack.estoqueMinimoGlobal)
            } else {
                setErroGeral(true)
            }
        }
    }

    return (
        <form className={estilos.formulario} noValidate onSubmit={aoEnviar}>
            <div className={estilos.secao}>
                <div className={estilos.linhaMinimo}>
                    <div className={estilos.textos}>
                        <label className={estilos.rotulo} htmlFor={id}>Quantidade mínima de produtos</label>
                        <p className={estilos.legenda} id={idAjuda}>
                            Abaixo desse valor o produto aparece em "baixo estoque". Vale para produtos sem mínimo próprio.
                        </p>
                    </div>
                    <input
                        id={id}
                        className={estilos.entrada + (erroMinimo ? ' ' + estilos.entradaErro : '')}
                        inputMode="numeric"
                        aria-describedby={erroMinimo ? idErro + ' ' + idAjuda : idAjuda}
                        aria-invalid={erroMinimo ? true : undefined}
                        value={minimo}
                        onChange={(evento) => {
                            setMinimo(evento.target.value)
                            setErroMinimo(null)
                        }}
                    />
                </div>
                {erroMinimo && (
                    <p className={estilos.erro} id={idErro}>
                        <TriangleAlert size={16} aria-hidden="true" />{erroMinimo}
                    </p>
                )}
                <p className={estilos.legenda}>{textoMinimoProprio(configuracao.produtosComMinimoProprio)}</p>
            </div>
            <hr className={estilos.divisoria} />
            <CaixaMarcar
                rotulo="Exibir foto dos produtos"
                checked={exibirFotos}
                onChange={(evento) => setExibirFotos(evento.target.checked)}
            />
            {erroGeral && (
                <Faixa variante="alerta" role="alert">
                    <p>{obterMensagemErro(erroEnvio)}</p>
                </Faixa>
            )}
            <div className={estilos.acoes}>
                <Botao type="submit" carregando={enviando} textoCarregando="Salvando...">Salvar configurações</Botao>
            </div>
        </form>
    )
}
