const MENSAGEM_SEM_RESPOSTA = 'Não foi possível falar com o servidor. Tente de novo.'

// O backend devolve { status, mensagem, erros } (Apêndice A.1).
export function obterMensagemErro(erro) {
  return erro.response?.data?.mensagem ?? MENSAGEM_SEM_RESPOSTA
}
