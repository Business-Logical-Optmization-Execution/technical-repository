import { useRequisicao } from '@/shared/lib';
import { buscarConfiguracoes } from '../api/configuracaoApi';

export function useExibirFotos() {

    const { dados, carregando } = useRequisicao(buscarConfiguracoes);

    return { exibirFotos: dados?.exibirFotos ?? true, carregando };
}
