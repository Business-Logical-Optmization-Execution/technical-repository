const CARACTERES = 'ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789';
const TAMANHO = 8;

export function gerarSenhaProvisoria() {

    const sorteados = crypto.getRandomValues(new Uint32Array(TAMANHO));

    return Array.from(sorteados, (numero) => CARACTERES[numero % CARACTERES.length]).join('');
}
