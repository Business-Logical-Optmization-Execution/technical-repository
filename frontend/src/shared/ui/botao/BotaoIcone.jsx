import estilos from './BotaoIcone.module.css'

export function BotaoIcone({icone, rotulo, ...resto}) {

   const Icone = icone;

   if (!rotulo) {
        console.warn('BotaoIcone: a prop "rotulo" é obrigatória. Ela vira o aria-label e o title do botão.');
    }

    return (
       <button type={resto.type || 'button'} {...resto} className={estilos.botaoIcone} aria-label={rotulo} title={rotulo}>
            <Icone aria-hidden="true" size={20}/>
        </button>
    )
};
