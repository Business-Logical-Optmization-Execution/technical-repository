import estilos from './Card.module.css'

export function Card({ children, clicavel, como = 'div', ...resto }) {

    const Elemento = clicavel ? como : 'div'

    return (
        <Elemento {...resto} className={estilos.card + ' ' + (clicavel ? estilos.clicavel : '')}>
            {children}
        </Elemento>
    )
};
