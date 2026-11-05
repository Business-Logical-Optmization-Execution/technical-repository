package com.portalblox.service;

import com.portalblox.entity.Categoria;
import com.portalblox.entity.Fornecedor;
import com.portalblox.entity.OrigemMovimentacao;
import com.portalblox.entity.Produto;
import com.portalblox.entity.ProdutoLocalizacao;
import com.portalblox.entity.TipoMovimentacao;
import com.portalblox.entity.Usuario;
import com.portalblox.exception.CategoriaNaoEncontradaException;
import com.portalblox.exception.DadosInvalidosException;
import com.portalblox.exception.DetectorChaveDuplicada;
import com.portalblox.exception.FornecedorInativoException;
import com.portalblox.exception.FornecedorNaoEncontradoException;
import com.portalblox.exception.ProdutoInativoException;
import com.portalblox.exception.ProdutoJaExisteException;
import com.portalblox.exception.ProdutoJaInativoException;
import com.portalblox.exception.ProdutoNaoEncontradoException;
import com.portalblox.exception.UsuarioInativoException;
import com.portalblox.exception.UsuarioNaoEncontradoException;
import com.portalblox.repository.CategoriaRepository;
import com.portalblox.repository.FornecedorRepository;
import com.portalblox.repository.ProdutoRepository;
import com.portalblox.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

	private static final String OBSERVACAO_SALDO_INICIAL = "Saldo inicial do cadastro.";

	private final ProdutoRepository produtoRepository;
	private final CategoriaRepository categoriaRepository;
	private final FornecedorRepository fornecedorRepository;
	private final UsuarioRepository usuarioRepository;
	private final MovimentacaoEstoqueService movimentacaoService;
	private final ConfiguracaoService configuracaoService;

	public ProdutoService(ProdutoRepository produtoRepository,
			CategoriaRepository categoriaRepository,
			FornecedorRepository fornecedorRepository,
			UsuarioRepository usuarioRepository,
			MovimentacaoEstoqueService movimentacaoService,
			ConfiguracaoService configuracaoService) {
		this.produtoRepository = produtoRepository;
		this.categoriaRepository = categoriaRepository;
		this.fornecedorRepository = fornecedorRepository;
		this.usuarioRepository = usuarioRepository;
		this.movimentacaoService = movimentacaoService;
		this.configuracaoService = configuracaoService;
	}

	public int obterEstoqueMinimoGlobal() {
		return configuracaoService.obterEstoqueMinimoGlobal();
	}

	public List<Produto> listar(boolean incluirInativos) {
		if (incluirInativos) {
			return produtoRepository.findAllCompletos();
		}
		return produtoRepository.findAtivosCompletos();
	}

	public Produto buscarPorId(Integer id) {
		return produtoRepository.findByIdCompleto(id)
				.orElseThrow(ProdutoNaoEncontradoException::new);
	}

	@Transactional
	public Produto cadastrar(Produto produto, Integer categoriaId, Integer fornecedorId,
			Integer usuarioId, List<String> localizacoes) {
		if (produtoRepository.existsBySku(produto.getSku())) {
			throw new ProdutoJaExisteException();
		}
		produto.setCategoria(buscarCategoria(categoriaId));
		produto.setFornecedor(buscarFornecedorOpcional(fornecedorId));
		Usuario autor = buscarUsuarioAtivo(usuarioId);
		produto.setAtivo(true);
		adicionarLocalizacoes(produto, normalizarLocalizacoes(localizacoes));

		Produto salvo = salvar(produto);

		Integer quantidadeInicial = salvo.getQuantidade();
		if (quantidadeInicial != null && quantidadeInicial > 0) {
			movimentacaoService.registrar(salvo, autor, TipoMovimentacao.SALDO_INICIAL,
					quantidadeInicial, quantidadeInicial, 0, quantidadeInicial,
					null, OBSERVACAO_SALDO_INICIAL, null, OrigemMovimentacao.MANUAL);
		}
		return salvo;
	}

	@Transactional
	public Produto atualizar(Integer id, Produto dados, Integer categoriaId, Integer fornecedorId,
			List<String> localizacoes) {
		Produto existente = produtoRepository.findByIdCompleto(id)
				.orElseThrow(ProdutoNaoEncontradoException::new);
		if (!Boolean.TRUE.equals(existente.getAtivo())) {
			throw new ProdutoInativoException();
		}
		if (produtoRepository.existsBySkuAndIdNot(dados.getSku(), id)) {
			throw new ProdutoJaExisteException();
		}

		existente.setSku(dados.getSku());
		existente.setNome(dados.getNome());
		existente.setPeso(dados.getPeso());
		existente.setTamanho(dados.getTamanho());
		existente.setDescricao(dados.getDescricao());
		existente.setPreco(dados.getPreco());
		existente.setMarca(dados.getMarca());
		existente.setFotoUrl(dados.getFotoUrl());
		existente.setEstoqueMinimo(dados.getEstoqueMinimo());
		existente.setCategoria(buscarCategoria(categoriaId));
		existente.setFornecedor(buscarFornecedorOpcional(fornecedorId));

		List<String> nomes = normalizarLocalizacoes(localizacoes);
		existente.getLocalizacoes().clear();
		produtoRepository.flush();
		adicionarLocalizacoes(existente, nomes);
		return salvar(existente);
	}

	@Transactional
	public Produto ajustarQuantidade(Integer id, Integer novaQuantidade, Integer usuarioId,
			String justificativa) {
		Produto produto = produtoRepository.findByIdParaAtualizacao(id)
				.orElseThrow(ProdutoNaoEncontradoException::new);
		if (!Boolean.TRUE.equals(produto.getAtivo())) {
			throw new ProdutoInativoException();
		}
		Usuario autor = buscarUsuarioAtivo(usuarioId);

		int saldoAnterior = produto.getQuantidade();
		int variacao = novaQuantidade - saldoAnterior;
		produto.setQuantidade(novaQuantidade);
		Produto salvo = produtoRepository.save(produto);
		movimentacaoService.registrar(salvo, autor, TipoMovimentacao.AJUSTE,
				Math.abs(variacao), variacao, saldoAnterior, novaQuantidade,
				null, justificativa.trim(), null, OrigemMovimentacao.MANUAL);
		return salvo;
	}

	public void inativar(Integer id) {
		Produto produto = produtoRepository.findById(id)
				.orElseThrow(ProdutoNaoEncontradoException::new);
		if (!Boolean.TRUE.equals(produto.getAtivo())) {
			throw new ProdutoJaInativoException();
		}
		produto.setAtivo(false);
		produtoRepository.save(produto);
	}

	private Produto salvar(Produto produto) {
		try {
			return produtoRepository.saveAndFlush(produto);
		} catch (DataIntegrityViolationException excecao) {
			if (DetectorChaveDuplicada.ehChaveDuplicada(excecao)) {
				throw new ProdutoJaExisteException();
			}
			throw new DadosInvalidosException("Violação de integridade dos dados.");
		}
	}

	private Categoria buscarCategoria(Integer categoriaId) {
		return categoriaRepository.findById(categoriaId)
				.orElseThrow(CategoriaNaoEncontradaException::new);
	}

	private Fornecedor buscarFornecedorOpcional(Integer fornecedorId) {
		if (fornecedorId == null) {
			return null;
		}
		Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId)
				.orElseThrow(FornecedorNaoEncontradoException::new);
		if (!Boolean.TRUE.equals(fornecedor.getAtivo())) {
			throw new FornecedorInativoException();
		}
		return fornecedor;
	}

	private Usuario buscarUsuarioAtivo(Integer usuarioId) {
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(UsuarioNaoEncontradoException::new);
		if (!Boolean.TRUE.equals(usuario.getAtivo())) {
			throw new UsuarioInativoException();
		}
		return usuario;
	}

	private List<String> normalizarLocalizacoes(List<String> localizacoes) {
		if (localizacoes == null) {
			return List.of();
		}
		List<String> normalizadas = new ArrayList<>();
		Set<String> vistas = new HashSet<>();
		for (String localizacao : localizacoes) {
			String nome = localizacao == null ? "" : localizacao.trim();
			if (nome.isEmpty()) {
				throw new DadosInvalidosException("Localização não pode ser vazia.");
			}
			if (nome.length() > 120) {
				throw new DadosInvalidosException("Localização deve ter no máximo 120 caracteres.");
			}
			if (!vistas.add(nome.toLowerCase(Locale.ROOT))) {
				throw new DadosInvalidosException("Localização repetida: " + nome + ".");
			}
			normalizadas.add(nome);
		}
		return normalizadas;
	}

	private void adicionarLocalizacoes(Produto produto, List<String> nomes) {
		for (String nome : nomes) {
			produto.getLocalizacoes().add(new ProdutoLocalizacao(produto, nome));
		}
	}

}
