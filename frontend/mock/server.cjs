/*
 * Servidor de mock do Portal Óttimos (json-server 0.17.4).
 *
 * Imita o CONTRATO do backend Spring (mesmas rotas, mesmos nomes de campo,
 * mesmo formato de erro). As rotas que têm regra de negócio (login,
 * movimentação, inativação...) são escritas à mão abaixo; o resto é o
 * CRUD automático do json-server sobre o mock/db.json.
 *
 * NÃO é exercício: é infraestrutura. Não precisa entender cada linha.
 * Rodar: npm run mock   (porta 3001)
 */
const fs = require('node:fs');
const path = require('node:path');
const jsonServer = require('json-server');

// db.json é a cópia de trabalho (muda a cada requisição de escrita).
// db.seed.json é o original. "npm run mock:reset" restaura.
const DB = path.join(__dirname, 'db.json');
const SEED = path.join(__dirname, 'db.seed.json');
if (!fs.existsSync(DB)) fs.copyFileSync(SEED, DB);

const server = jsonServer.create();
const router = jsonServer.router(DB);
const db = () => router.db; // lowdb (lodash chain)

server.use(jsonServer.defaults({ logger: true }));
server.use(jsonServer.bodyParser);

// Atraso artificial: deixa você ver os estados de "carregando".
server.use((req, res, next) => setTimeout(next, 400));

// ---------- utilitários ----------
const erro = (res, status, mensagem, erros = []) =>
  res.status(status).json({ status, mensagem, erros });

const agora = () => {
  const d = new Date();
  const p = (n) => String(n).padStart(2, '0');
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}T${p(d.getHours())}:${p(d.getMinutes())}:${p(d.getSeconds())}`;
};

const proximoId = (colecao) => (db().get(colecao).maxBy('id').value()?.id ?? 0) + 1;

const semSenha = ({ senha, ...resto }) => resto;

const comMinimoEfetivo = (produto) => ({
  ...produto,
  estoqueMinimoEfetivo:
    produto.estoqueMinimo ?? db().get('configuracoes.estoqueMinimoGlobal').value(),
});

const resumo = (p) => ({ id: p.id, sku: p.sku, nome: p.nome });

// ---------- autenticação ----------
server.post('/auth/login', (req, res) => {
  const { email, senha } = req.body;
  const usuario = db().get('usuarios').find({ email, senha, ativo: true }).value();
  if (!usuario) return erro(res, 401, 'Usuário ou senha incorretos.');
  res.json(semSenha(usuario));
});

// ---------- produtos ----------
server.get('/produtos', (req, res) => {
  const incluirInativos = req.query.incluirInativos === 'true';
  const lista = db().get('produtos').value()
    .filter((p) => incluirInativos || p.ativo)
    .map(comMinimoEfetivo);
  res.json(lista);
});

server.get('/produtos/:id', (req, res) => {
  const produto = db().get('produtos').find({ id: Number(req.params.id) }).value();
  if (!produto) return erro(res, 404, 'Produto não encontrado.');
  res.json(comMinimoEfetivo(produto));
});

// Lê e valida os campos comuns ao POST e ao PUT. Devolve { erroResposta } ou { campos }.
const lerCamposProduto = (req, res, idAtual = null, errosIniciais = []) => {
  const {
    sku, nome, categoriaId, peso = null, tamanho = null, descricao = null, preco = null,
    marca = null, fornecedorId = null, localizacoes = null, estoqueMinimo = null, fotoUrl = null,
  } = req.body;
  const erros = [...errosIniciais];
  if (!sku || !String(sku).trim()) erros.push({ campo: 'sku', mensagem: 'Informe o SKU.' });
  if (!nome || !String(nome).trim()) erros.push({ campo: 'nome', mensagem: 'Informe o nome.' });
  if (categoriaId === undefined || categoriaId === null) erros.push({ campo: 'categoriaId', mensagem: 'Informe a categoria.' });
  if (estoqueMinimo !== null && (!Number.isInteger(estoqueMinimo) || estoqueMinimo < 0)) {
    erros.push({ campo: 'estoqueMinimo', mensagem: 'Use um número inteiro igual ou maior que zero.' });
  }
  if (localizacoes !== null && (!Array.isArray(localizacoes) || localizacoes.some((l) => String(l).length > 120))) {
    erros.push({ campo: 'localizacoes', mensagem: 'Use uma lista de textos com até 120 caracteres.' });
  }
  if (erros.length) return { erroResposta: erro(res, 400, 'Confira os campos destacados.', erros) };

  const categoria = db().get('categorias').find({ id: Number(categoriaId) }).value();
  if (!categoria) return { erroResposta: erro(res, 404, 'Categoria não encontrada.') };

  let fornecedor = null;
  if (fornecedorId !== null) {
    const achado = db().get('fornecedores').find({ id: Number(fornecedorId) }).value();
    if (!achado) return { erroResposta: erro(res, 404, 'Fornecedor não encontrado.') };
    if (!achado.ativo) return { erroResposta: erro(res, 422, 'Este fornecedor está inativo.') };
    fornecedor = { id: achado.id, nome: achado.nome, cnpj: achado.cnpj };
  }

  const duplicado = db().get('produtos').find((p) => p.sku === sku && p.id !== idAtual).value();
  if (duplicado) return { erroResposta: erro(res, 409, 'Já existe um produto com este SKU.') };

  return {
    campos: {
      sku, nome, peso, tamanho, descricao, preco, marca, categoria, fornecedor,
      localizacoes: localizacoes ?? [], estoqueMinimo, fotoUrl,
    },
  };
};

const registrarMovimentacao = (produto, usuario, tipo, antes, depois, observacao) => {
  db().get('movimentacoes').push({
    id: proximoId('movimentacoes'),
    tipo,
    quantidade: depois - antes,
    quantidadeAntes: antes,
    quantidadeDepois: depois,
    motivo: null,
    observacao,
    documento: null,
    origem: 'MANUAL',
    dataMov: agora(),
    produto: resumo(produto),
    usuario: { id: usuario.id, nome: usuario.nome },
  }).write();
};

server.post('/produtos', (req, res) => {
  const { quantidade, usuarioId } = req.body;
  const erros = [];
  if (!Number.isInteger(quantidade) || quantidade < 0) erros.push({ campo: 'quantidade', mensagem: 'Use um número inteiro igual ou maior que zero.' });
  if (usuarioId === undefined || usuarioId === null) erros.push({ campo: 'usuarioId', mensagem: 'Informe o usuário.' });

  const { campos, erroResposta } = lerCamposProduto(req, res, null, erros);
  if (erroResposta) return erroResposta;

  const usuario = db().get('usuarios').find({ id: Number(usuarioId) }).value();
  if (!usuario) return erro(res, 404, 'Usuário não encontrado.');

  const produto = { id: proximoId('produtos'), ...campos, quantidade, ativo: true };
  db().get('produtos').push(produto).write();
  if (quantidade > 0) registrarMovimentacao(produto, usuario, 'SALDO_INICIAL', 0, quantidade, null);
  res.status(201).json(comMinimoEfetivo(produto));
});

// Nunca altera o saldo. O que não for enviado é limpo, como no back.
server.put('/produtos/:id', (req, res) => {
  const id = Number(req.params.id);
  const ref = db().get('produtos').find({ id });
  if (!ref.value()) return erro(res, 404, 'Produto não encontrado.');
  if (!ref.value().ativo) return erro(res, 422, 'Este produto está inativo.');
  const { campos, erroResposta } = lerCamposProduto(req, res, id);
  if (erroResposta) return erroResposta;
  ref.assign(campos).write();
  res.json(comMinimoEfetivo(ref.value()));
});

// Ajuste auditado: valor absoluto. Sem mudança de valor, não grava movimentação.
server.patch('/produtos/:id/quantidade', (req, res) => {
  const ref = db().get('produtos').find({ id: Number(req.params.id) });
  if (!ref.value()) return erro(res, 404, 'Produto não encontrado.');
  if (!ref.value().ativo) return erro(res, 422, 'Este produto está inativo.');

  const { quantidade, usuarioId, justificativa } = req.body;
  const erros = [];
  if (!Number.isInteger(quantidade) || quantidade < 0) erros.push({ campo: 'quantidade', mensagem: 'Use um número inteiro igual ou maior que zero.' });
  if (usuarioId === undefined || usuarioId === null) erros.push({ campo: 'usuarioId', mensagem: 'Informe o usuário.' });
  if (!justificativa || !String(justificativa).trim()) erros.push({ campo: 'justificativa', mensagem: 'Informe a justificativa.' });
  if (erros.length) return erro(res, 400, 'Confira os campos destacados.', erros);

  const usuario = db().get('usuarios').find({ id: Number(usuarioId) }).value();
  if (!usuario) return erro(res, 404, 'Usuário não encontrado.');

  const antes = ref.value().quantidade;
  if (quantidade !== antes) {
    ref.assign({ quantidade }).write();
    registrarMovimentacao(ref.value(), usuario, 'AJUSTE', antes, quantidade, justificativa);
  }
  res.json(comMinimoEfetivo(ref.value()));
});

server.delete('/produtos/:id', (req, res) => {
  const ref = db().get('produtos').find({ id: Number(req.params.id) });
  if (!ref.value()) return erro(res, 404, 'Produto não encontrado.');
  if (!ref.value().ativo) return erro(res, 422, 'Este produto já está inativo.');
  ref.assign({ ativo: false }).write();
  res.status(204).end();
});

// ---------- fornecedores ----------
// Nenhuma tela usa nesta sprint. POST, PUT e DELETE ficam no CRUD automático, sem as regras do back.
server.get('/fornecedores', (req, res) => {
  const incluirInativos = req.query.incluirInativos === 'true';
  res.json(db().get('fornecedores').value().filter((f) => incluirInativos || f.ativo));
});

// ---------- movimentações ----------
server.get('/movimentacoes', (req, res) => {
  const { produtoId, usuarioId, tipo, de, ate, limite } = req.query;
  let lista = db().get('movimentacoes').value().slice();
  if (produtoId) lista = lista.filter((m) => m.produto.id === Number(produtoId));
  if (usuarioId) lista = lista.filter((m) => m.usuario.id === Number(usuarioId));
  if (tipo) lista = lista.filter((m) => m.tipo === tipo);
  if (de) lista = lista.filter((m) => m.dataMov.slice(0, 10) >= de);
  if (ate) lista = lista.filter((m) => m.dataMov.slice(0, 10) <= ate);
  lista.sort((a, b) => b.dataMov.localeCompare(a.dataMov));
  if (limite) lista = lista.slice(0, Number(limite));
  res.json(lista);
});

server.post('/movimentacoes', (req, res) => {
  const { produtoId, usuarioId, tipo, quantidade, motivo = null, observacao = null, documento = null } = req.body;
  const refProduto = db().get('produtos').find({ id: Number(produtoId) });
  const produto = refProduto.value();
  if (!produto) return erro(res, 404, 'Produto não encontrado.');
  const usuario = db().get('usuarios').find({ id: Number(usuarioId) }).value();
  if (!usuario) return erro(res, 404, 'Usuário não encontrado.');

  const q = Number(quantidade);
  const quantidadeValida = Number.isInteger(q) && (tipo === 'AJUSTE' ? q >= 0 : q > 0);
  if (!quantidadeValida) {
    return erro(res, 400, 'Confira os campos destacados.', [
      { campo: 'quantidade', mensagem: 'Informe um número inteiro maior que zero.' },
    ]);
  }
  if (tipo === 'SAIDA' && !motivo) {
    return erro(res, 400, 'Confira os campos destacados.', [
      { campo: 'motivo', mensagem: 'Escolha o motivo da saída.' },
    ]);
  }
  if (tipo === 'AJUSTE' && (!observacao || observacao.trim().length < 10)) {
    return erro(res, 400, 'Confira os campos destacados.', [
      { campo: 'observacao', mensagem: 'Escreva uma justificativa com pelo menos 10 caracteres.' },
    ]);
  }

  const antes = produto.quantidade;
  let depois;
  if (tipo === 'ENTRADA') depois = antes + q;
  else if (tipo === 'SAIDA') {
    if (q > antes) {
      return erro(res, 422, `Não dá para tirar ${q} un.: a quantidade atual é ${antes} un.`);
    }
    depois = antes - q;
  } else if (tipo === 'AJUSTE') depois = q;
  else return erro(res, 400, 'Tipo de movimentação inválido.');

  refProduto.assign({ quantidade: depois }).write();
  const movimentacao = {
    id: proximoId('movimentacoes'),
    tipo,
    quantidade: depois - antes,
    quantidadeAntes: antes,
    quantidadeDepois: depois,
    motivo,
    observacao,
    documento,
    origem: 'MANUAL',
    dataMov: agora(),
    produto: resumo(produto),
    usuario: { id: usuario.id, nome: usuario.nome },
  };
  db().get('movimentacoes').push(movimentacao).write();
  res.status(201).json(movimentacao);
});

// ---------- avisos de falta ----------
server.get('/avisos-falta', (req, res) => {
  const { status } = req.query;
  const lista = db().get('avisosFalta').value()
    .filter((a) => !status || a.status === status)
    .sort((a, b) => b.criadoEm.localeCompare(a.criadoEm));
  res.json(lista);
});

server.post('/avisos-falta', (req, res) => {
  const { produtoId, usuarioId, observacao = null } = req.body;
  const produto = db().get('produtos').find({ id: Number(produtoId) }).value();
  const usuario = db().get('usuarios').find({ id: Number(usuarioId) }).value();
  if (!produto || !usuario) return erro(res, 404, 'Produto ou usuário não encontrado.');

  const refAberto = db().get('avisosFalta').find((a) => a.produto.id === produto.id && a.status === 'ABERTO');
  if (refAberto.value()) {
    refAberto.assign({ quantidadeAvisos: refAberto.value().quantidadeAvisos + 1 }).write();
    return res.status(200).json({ ...refAberto.value(), somado: true });
  }
  const aviso = {
    id: proximoId('avisosFalta'),
    status: 'ABERTO',
    observacao,
    quantidadeAvisos: 1,
    criadoEm: agora(),
    produto: resumo(produto),
    usuario: { id: usuario.id, nome: usuario.nome },
  };
  db().get('avisosFalta').push(aviso).write();
  res.status(201).json({ ...aviso, somado: false });
});

server.patch('/avisos-falta/:id/resolver', (req, res) => {
  const ref = db().get('avisosFalta').find({ id: Number(req.params.id) });
  if (!ref.value()) return erro(res, 404, 'Aviso não encontrado.');
  ref.assign({ status: 'RESOLVIDO' }).write();
  res.json(ref.value());
});

// ---------- configurações ----------
server.get('/configuracoes', (req, res) => {
  const config = db().get('configuracoes').value();
  const produtosComMinimoProprio = db().get('produtos').filter((p) => p.ativo && p.estoqueMinimo !== null).size().value();
  res.json({ ...config, produtosComMinimoProprio });
});

server.put('/configuracoes', (req, res) => {
  const { estoqueMinimoGlobal, exibirFotos } = req.body;
  if (!Number.isInteger(estoqueMinimoGlobal) || estoqueMinimoGlobal < 0) {
    return erro(res, 400, 'Confira os campos destacados.', [
      { campo: 'estoqueMinimoGlobal', mensagem: 'Use um número inteiro igual ou maior que zero.' },
    ]);
  }
  db().get('configuracoes').assign({ estoqueMinimoGlobal, exibirFotos: Boolean(exibirFotos) }).write();
  const produtosComMinimoProprio = db().get('produtos').filter((p) => p.ativo && p.estoqueMinimo !== null).size().value();
  res.json({ ...db().get('configuracoes').value(), produtosComMinimoProprio });
});

// ---------- usuários ----------
server.get('/perfis-acesso', (req, res) => res.json(db().get('perfisAcesso').value()));

const perfil = (id) => db().get('perfisAcesso').find({ id: Number(id) }).value();

server.get('/usuarios', (req, res) => {
  const incluirInativos = req.query.incluirInativos === 'true';
  res.json(db().get('usuarios').value().filter((u) => incluirInativos || u.ativo).map(semSenha));
});

server.get('/usuarios/:id', (req, res) => {
  const usuario = db().get('usuarios').find({ id: Number(req.params.id) }).value();
  if (!usuario) return erro(res, 404, 'Usuário não encontrado.');
  res.json(semSenha(usuario));
});

server.post('/usuarios', (req, res) => {
  const { nome, email, senha, perfilAcessoId } = req.body;
  if (!nome || !email || !senha) return erro(res, 400, 'Preencha nome, e-mail e senha.');
  if (!perfil(perfilAcessoId)) return erro(res, 404, 'Perfil de acesso não encontrado.');
  if (db().get('usuarios').find({ email }).value()) return erro(res, 409, 'Já existe um usuário com este e-mail.');
  const usuario = { id: proximoId('usuarios'), nome, email, senha, perfilAcesso: perfil(perfilAcessoId), ativo: true };
  db().get('usuarios').push(usuario).write();
  res.status(201).json(semSenha(usuario));
});

server.put('/usuarios/:id', (req, res) => {
  const ref = db().get('usuarios').find({ id: Number(req.params.id) });
  if (!ref.value()) return erro(res, 404, 'Usuário não encontrado.');
  const { nome, email, perfilAcessoId } = req.body;
  if (!perfil(perfilAcessoId)) return erro(res, 404, 'Perfil de acesso não encontrado.');
  const outro = db().get('usuarios').find((u) => u.email === email && u.id !== ref.value().id).value();
  if (outro) return erro(res, 409, 'Já existe um usuário com este e-mail.');
  ref.assign({ nome, email, perfilAcesso: perfil(perfilAcessoId) }).write();
  res.json(semSenha(ref.value()));
});

server.patch('/usuarios/:id/senha', (req, res) => {
  const ref = db().get('usuarios').find({ id: Number(req.params.id) });
  if (!ref.value()) return erro(res, 404, 'Usuário não encontrado.');
  const { senhaAtual, novaSenha } = req.body;
  if (senhaAtual !== undefined && senhaAtual !== ref.value().senha) {
    return erro(res, 400, 'Confira os campos destacados.', [
      { campo: 'senhaAtual', mensagem: 'A senha atual está incorreta.' },
    ]);
  }
  if (!novaSenha || novaSenha.length < 8) {
    return erro(res, 400, 'Confira os campos destacados.', [
      { campo: 'novaSenha', mensagem: 'Use pelo menos 8 caracteres.' },
    ]);
  }
  ref.assign({ senha: novaSenha }).write();
  res.status(204).end();
});

server.delete('/usuarios/:id', (req, res) => {
  const ref = db().get('usuarios').find({ id: Number(req.params.id) });
  if (!ref.value()) return erro(res, 404, 'Usuário não encontrado.');
  if (!ref.value().ativo) return erro(res, 422, 'Este usuário já está desativado.');
  const admins = db().get('usuarios').filter((u) => u.ativo && u.perfilAcesso.nome === 'Administrador').size().value();
  if (ref.value().perfilAcesso.nome === 'Administrador' && admins <= 1) {
    return erro(res, 422, 'Não é possível desativar o último administrador ativo.');
  }
  ref.assign({ ativo: false }).write();
  res.status(204).end();
});

// ---------- dashboard ----------
server.get('/dashboard/resumo', (req, res) => {
  const produtos = db().get('produtos').value().filter((p) => p.ativo).map(comMinimoEfetivo);
  const movs = db().get('movimentacoes').value();
  const hoje = agora().slice(0, 10);

  const dias = [];
  for (let i = 6; i >= 0; i--) {
    const d = new Date();
    d.setDate(d.getDate() - i);
    const p = (n) => String(n).padStart(2, '0');
    dias.push(`${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}`);
  }
  const entradasSaidas7Dias = dias.map((data) => {
    const doDia = movs.filter((m) => m.dataMov.slice(0, 10) === data);
    return {
      data,
      entradas: doDia.filter((m) => m.quantidade > 0).reduce((s, m) => s + m.quantidade, 0),
      saidas: doDia.filter((m) => m.quantidade < 0).reduce((s, m) => s - m.quantidade, 0),
    };
  });

  const totais = {};
  for (const m of movs) {
    totais[m.produto.id] ??= { produto: m.produto, totalMovimentado: 0 };
    totais[m.produto.id].totalMovimentado += Math.abs(m.quantidade);
  }

  res.json({
    totalProdutosAtivos: produtos.length,
    produtosBaixoEstoque: produtos.filter((p) => p.quantidade > 0 && p.quantidade <= p.estoqueMinimoEfetivo).length,
    produtosZerados: produtos.filter((p) => p.quantidade <= 0).length,
    movimentacoesHoje: movs.filter((m) => m.dataMov.slice(0, 10) === hoje).length,
    entradasSaidas7Dias,
    maisMovimentados: Object.values(totais).sort((a, b) => b.totalMovimentado - a.totalMovimentado).slice(0, 5),
  });
});

// ---------- prévia Bling (somente leitura, sem regra) ----------
server.get('/divergencias', (req, res) => {
  const { tipo } = req.query;
  const lista = db().get('divergencias').value()
    .filter((d) => !tipo || d.tipo === tipo)
    .map((d) => ({ ...d, diferenca: d.quantidadeBling === null ? null : d.quantidadePortal - d.quantidadeBling }))
    .sort((a, b) => Math.abs(b.diferenca ?? 0) - Math.abs(a.diferenca ?? 0));
  res.json(lista);
});
server.get('/divergencias/:id', (req, res) => {
  const d = db().get('divergencias').find({ id: Number(req.params.id) }).value();
  if (!d) return erro(res, 404, 'Divergência não encontrada.');
  res.json(d);
});
server.get('/integracao/estado', (req, res) => res.json(db().get('integracao').value()));
server.get('/integracao/eventos', (req, res) => res.json(db().get('eventosIntegracao').value()));
server.get('/importacao/previa', (req, res) => res.json(db().get('importacaoPrevia').value()));

server.use(router);
server.listen(3001, () => console.log('Mock do Portal Óttimos em http://localhost:3001'));
