import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'
import { formatarQuantidade } from '@/shared/lib'
import { Tabela } from '@/shared/ui'
import estilos from './GraficoEntradasSaidas.module.css'

const ALTURA = 280;

const COLUNAS_TABELA = [
    { titulo: 'Dia', valor: (dia) => dia.rotulo },
    { titulo: 'Entradas', valor: (dia) => formatarQuantidade(dia.entradas, { sinal: true }) },
    { titulo: 'Saídas', valor: (dia) => formatarQuantidade(-dia.saidas) },
];

function formatarDia(data) {
    const [, mes, dia] = data.split('-');
    return dia + '/' + mes;
}

// O Recharts passa as cores como atributos do SVG, onde var() não funciona em todas as props.
function lerToken(nome) {
    return getComputedStyle(document.documentElement).getPropertyValue(nome).trim();
}

function Dica({ active, payload, label }) {

    if (!active || !payload?.length) {
        return null;
    }

    const { entradas, saidas } = payload[0].payload;

    return (
        <div className={estilos.dica}>
            <p className={estilos.diaDica}>{label}</p>
            <p>Entradas: {formatarQuantidade(entradas, { sinal: true })}</p>
            <p>Saídas: {formatarQuantidade(-saidas)}</p>
        </div>
    )
}

export function GraficoEntradasSaidas({ dias }) {

    const linhas = dias.map((dia) => ({ ...dia, id: dia.data, rotulo: formatarDia(dia.data) }));
    const semMovimento = dias.every((dia) => dia.entradas === 0 && dia.saidas === 0);

    if (semMovimento) {
        return <p className={estilos.vazio}>Nenhuma movimentação nos últimos 7 dias.</p>
    }

    const eixo = { fill: lerToken('--cor-texto-secundario'), fontSize: lerToken('--texto-legenda-tamanho') };

    return (
        <div className={estilos.grafico}>
            <ul className={estilos.legenda}>
                <li><span className={estilos.marca + ' ' + estilos.entradas} aria-hidden="true" />Entradas</li>
                <li><span className={estilos.marca + ' ' + estilos.saidas} aria-hidden="true" />Saídas</li>
            </ul>
            <div role="img" aria-label="Gráfico de barras com as entradas e saídas de cada dia. A tabela logo abaixo traz os mesmos dados.">
                <ResponsiveContainer width="100%" height={ALTURA}>
                    <BarChart data={linhas} barGap={2} barCategoryGap="30%" accessibilityLayer={false}>
                        <CartesianGrid vertical={false} stroke={lerToken('--cor-divisoria')} />
                        <XAxis dataKey="rotulo" tick={eixo} axisLine={false} tickLine={false} />
                        <YAxis tick={eixo} axisLine={false} tickLine={false} allowDecimals={false} width={40} />
                        <Tooltip content={<Dica />} cursor={{ fill: lerToken('--cor-primaria-suave') }} />
                        <Bar dataKey="entradas" fill={lerToken('--cor-primaria')} radius={[4, 4, 0, 0]} maxBarSize={28} isAnimationActive={false} />
                        <Bar dataKey="saidas" fill={lerToken('--cor-borda')} radius={[4, 4, 0, 0]} maxBarSize={28} isAnimationActive={false} />
                    </BarChart>
                </ResponsiveContainer>
            </div>
            <details className={estilos.detalhes}>
                <summary>Ver dados em tabela</summary>
                <Tabela legenda="Entradas e saídas dos últimos 7 dias" colunas={COLUNAS_TABELA} linhas={linhas} />
            </details>
        </div>
    )
}
