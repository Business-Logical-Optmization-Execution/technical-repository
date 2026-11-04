import { useEffect, useState } from 'react';

/*
 * useRequisicao(funcao, dependencias = [])
 *
 * Executa "funcao" (que deve devolver uma Promise com os dados) ao montar o
 * componente e sempre que algum valor de "dependencias" mudar.
 *
 * Devolve { dados, carregando, erro, recarregar }:
 *   dados       → o que a Promise resolveu. Começa null.
 *   carregando  → true enquanto a chamada está em andamento. Começa true.
 *   erro        → o erro inteiro do axios (não só a mensagem). Começa null.
 *                 Use obterMensagemErro(erro) ou obterErrosDeCampo(erro), de '@/shared/api'.
 *   recarregar  → função que refaz a chamada (ex.: botão "Tentar de novo").
 *
 * Exemplo:
 *   const { dados, carregando, erro, recarregar } = useRequisicao(
 *     () => http.get('/produtos/' + id).then((r) => r.data),
 *     [id],
 *   )
 *
 *   if (carregando) return <Skeleton />
 *   if (erro) return <p>{obterMensagemErro(erro)}</p>
 *   return <CartaoProduto produto={dados} />
 *
 * Teste "carregando" e "erro" antes de usar "dados": enquanto carrega ou
 * depois de um erro, "dados" pode ser null.
 */
export function useRequisicao(funcao, dependencias = []) {
    const [dados, setDados] = useState(null);
    const [carregando, setCarregando] = useState(true);
    const [erro, setErro] = useState(null);
    const [versao, setVersao] = useState(0);

    useEffect(() => {
        let ativo = true;

        setCarregando(true);
        setErro(null);

        funcao().then((resposta) => {
            if (ativo) {
                setDados(resposta);
            }
        }).catch((erro) => {
            if (ativo) {
                setErro(erro);
            }
        }).finally(() => {
            if (ativo) {
                setCarregando(false);
            }
        });

        return () => {
            ativo = false;
        }

    }, [...dependencias, versao]);

    // Mudar a versão muda as dependências do efeito, e o efeito executa de novo.
    function recarregar() {
        setVersao((atual) => atual + 1);
    }

    return { dados, carregando, erro, recarregar };
}
