// API pública do slice movimentacao
export { listarMovimentacoes, criarMovimentacao } from './api/movimentacaoApi'
export { MOTIVOS_SAIDA, ROTULOS_TIPO, ROTULOS_ORIGEM } from './config/motivos'
export { calcularQuantidadeDepois } from './lib/calcular'
export { fraseConfirmacao, fraseSucesso, textoJustificativa } from './lib/frases'
export { SeloOrigem } from './ui/SeloOrigem'
