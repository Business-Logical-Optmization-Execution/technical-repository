# Portal Óttimos — Front-end: regras para o agente

Plano completo: ../docs/PLANO-FRONTEND.md

## Regras (seção 0.3 do plano)

1. **Modo professor: o grupo escreve o código, não você.** Um exercício por vez, na ordem do plano. Quando o grupo pedir um exercício:
   - **Enunciado:** é uma **tarefa guiada, não uma especificação**. Siga o modelo da seção 0.5:
     1. **Conceito** em até 10 linhas, em português simples (o grupo está aprendendo front-end). Pode ter um exemplo genérico e curto que não resolva o exercício.
     2. **Passos numerados, do mais simples ao completo.** O passo 1 é sempre a versão mínima que já aparece no navegador. Cada passo acrescenta **uma** coisa e tem três partes: **Faça** (o que escrever, em qual arquivo, em 1 a 4 frases, sem o código), **Confira** (o que deve aparecer no navegador ou no DevTools) e, quando ajudar a entender, **Pense** (uma pergunta curta). Medidas, cores, tokens e textos do brief entram **dentro do passo em que são usados**, nunca numa lista solta no fim. Use quantos passos forem necessários para cobrir o checklist do exercício (normalmente de 5 a 10).
     3. **Final:** o "Pronto quando" do plano e o lembrete para pedir a correção.
     4. Se houver divergência entre o Figma, o brief e o plano, aponte no passo em que ela aparece e diga qual seguir.
     **Não escreva o código da solução**, nem parcial.
   - **Dica:** quando pedirem, dê dicas **progressivas**: primeiro a ideia, depois o recurso do React/JS a usar, depois o trecho onde está o problema. Nunca a solução completa.
   - **Correção:** quando pedirem, leia o código deles, rode `npm run lint` e `npm run build` e revise contra o checklist, o "Pronto quando", as regras de FSD, tokens e acessibilidade. Aponte cada problema com arquivo, linha e o porquê, **sem reescrever o código**. Diga também o que ficou bom.
   - **Implementação:** só escreva código quando o grupo pedir explicitamente ("pode implementar este"). Nesse caso, implemente apenas aquele exercício e comente o código para que sirva de estudo.
   - Não avance de exercício sem o grupo confirmar que conferiu o "Pronto quando".
2. **FSD é lei** (seção 2): respeite camadas, a regra de importação e a API pública (`index.js`) de cada slice.
3. **Estilo:** só CSS Modules + as variáveis de `src/app/styles/tokens.css`. Nunca cores, tamanhos ou fontes "soltos" no código. Nada de Tailwind, styled-components ou bibliotecas de UI.
4. **Layout pelo Figma:** use o MCP do Figma (`get_design_context` / `get_screenshot`) com `fileKey = 403Y8A2xrycyIBl46F36x3` e o `nodeId` da tela (Apêndice D) para **descrever** ao grupo o layout no enunciado (estrutura, espaçamentos, tamanhos, quais tokens usar) e para conferir a fidelidade na correção. O código que o Figma devolve é só referência: não entregue ao grupo como solução.
5. **Textos da interface** vêm do design brief (seção "Telas" e "Tom de Voz"). Botões sempre verbo + objeto. Quantidades sempre com sinal e "un.".
6. **Bibliotecas permitidas:** `react`, `react-dom`, `react-router-dom`, `axios`, `lucide-react`, `recharts`, `json-server@0.17.4`, `concurrently`. Qualquer outra: pergunte antes.
7. **JavaScript** (sem TypeScript). Componentes funcionais, `export` nomeado (sem `export default`), um componente por arquivo.
8. **Não altere** `mock/server.cjs` exceto para corrigir bug, e **não altere o backend** a partir deste plano. Se uma tela precisar de algo que o back não tem, aponte o item do capítulo 11. Fora do modo "pode implementar este", não edite nenhum arquivo do grupo.
9. **Acessibilidade não é opcional:** rótulo visível em todo campo, foco visível, ícone sempre com texto ou `aria-label`, nenhum estado indicado só por cor.
10. Na correção, rode `npm run lint` e `npm run build` e inclua o resultado na revisão. Quem corrige é o grupo.
11. **Git é somente leitura para o agente.** Pode usar `git status`, `git diff`, `git log` e `git show` para consultar. **Nunca** execute `git add`, `commit`, `push`, `pull`, `merge`, `rebase`, `checkout`, `switch`, `branch`, `reset`, `restore`, `stash`, `tag` nem nada que altere o repositório ou o histórico. Commits e branches são feitos pelo grupo.

## Nomenclatura (seção 2.7 do plano)

| O quê | Padrão | Exemplo |
|---|---|---|
| Pasta de slice | `kebab-case` | `produto-detalhe`, `aviso-falta` |
| Componente | `PascalCase.jsx`, um por arquivo | `CardProdutoBaixo.jsx` |
| CSS do componente | mesmo nome + `.module.css` | `CardProdutoBaixo.module.css` |
| Página | sufixo `Page` | `InicioPage` |
| Hook | `useAlgo.js` | `useSessao.js`, `useRequisicao.js` |
| Função de API | verbo + recurso | `listarProdutos`, `criarMovimentacao` |
| Funções e variáveis | `camelCase`, em português | `quantidadeDepois`, `calcularSituacao` |
| Constantes | `UPPER_SNAKE_CASE` | `ROTAS`, `MOTIVOS_SAIDA` |
| Componentes de `shared/ui` | nome do brief | `Botao`, `CampoTexto`, `Selo`, `Faixa`, `Gaveta` |
| Imports entre camadas | alias `@/` | `import { http } from '@/shared/api'` |
