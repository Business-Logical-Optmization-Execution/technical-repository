package com.portalblox.service;

import com.portalblox.entity.MotivoMovimentacao;
import com.portalblox.entity.MovimentacaoEstoque;
import com.portalblox.entity.OrigemMovimentacao;
import com.portalblox.entity.Produto;
import com.portalblox.entity.TipoMovimentacao;
import com.portalblox.entity.Usuario;
import com.portalblox.exception.ProdutoInativoException;
import com.portalblox.exception.ProdutoNaoEncontradoException;
import com.portalblox.exception.SaldoInsuficienteException;
import com.portalblox.exception.UsuarioInativoException;
import com.portalblox.exception.UsuarioNaoEncontradoException;
import com.portalblox.exception.ValidacaoException;
import com.portalblox.repository.MovimentacaoEstoqueRepository;
import com.portalblox.repository.ProdutoRepository;
import com.portalblox.repository.UsuarioRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovimentacaoService {

	private static final Set<TipoMovimentacao> TIPOS_ACEITOS =
			EnumSet.of(TipoMovimentacao.ENTRADA, TipoMovimentacao.SAIDA, TipoMovimentacao.AJUSTE);

	private final MovimentacaoEstoqueRepository movimentacaoRepository;
	private final ProdutoRepository produtoRepository;
	private final UsuarioRepository usuarioRepository;
	private final MovimentacaoEstoqueService movimentacaoEstoqueService;

	public MovimentacaoService(MovimentacaoEstoqueRepository movimentacaoRepository,
			ProdutoRepository produtoRepository,
			UsuarioRepository usuarioRepository,
			MovimentacaoEstoqueService movimentacaoEstoqueService) {
		this.movimentacaoRepository = movimentacaoRepository;
		this.produtoRepository = produtoRepository;
		this.usuarioRepository = usuarioRepository;
		this.movimentacaoEstoqueService = movimentacaoEstoqueService;
	}

	@Transactional
	public MovimentacaoEstoque criar(Integer produtoId, Integer usuarioId, String tipoTexto, Integer quantidade,
			String motivo, String observacao, String documento) {
		Produto produto = produtoRepository.findByIdParaAtualizacao(produtoId)
				.orElseThrow(ProdutoNaoEncontradoException::new);
		if (!Boolean.TRUE.equals(produto.getAtivo())) {
			throw new ProdutoInativoException();
		}

		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(UsuarioNaoEncontradoException::new);
		if (!Boolean.TRUE.equals(usuario.getAtivo())) {
			throw new UsuarioInativoException();
		}

		TipoMovimentacao tipo = validar(tipoTexto, quantidade, motivo, observacao, documento);

		int quantidadeAntes = produto.getQuantidade();
		int quantidadeDepois;
		if (tipo == TipoMovimentacao.ENTRADA) {
			quantidadeDepois = quantidadeAntes + quantidade;
		} else if (tipo == TipoMovimentacao.SAIDA) {
			if (quantidade > quantidadeAntes) {
				throw new SaldoInsuficienteException(quantidade, quantidadeAntes);
			}
			quantidadeDepois = quantidadeAntes - quantidade;
		} else {
			quantidadeDepois = quantidade;
		}

		int variacao = quantidadeDepois - quantidadeAntes;
		produto.setQuantidade(quantidadeDepois);
		produtoRepository.save(produto);

		return movimentacaoEstoqueService.registrar(produto, usuario, tipo,
				Math.abs(variacao), variacao, quantidadeAntes, quantidadeDepois,
				motivoDe(motivo), textoOuNulo(observacao),
				textoOuNulo(documento), OrigemMovimentacao.MANUAL);
	}

	public List<MovimentacaoEstoque> listar(Integer produtoId, Integer usuarioId, TipoMovimentacao tipo,
			LocalDate de, LocalDate ate, Integer limite) {
		LocalDateTime inicio = de == null ? null : de.atStartOfDay();
		LocalDateTime fimExclusivo = ate == null ? null : ate.plusDays(1).atStartOfDay();
		Pageable paginacao = limite == null ? Pageable.unpaged() : PageRequest.of(0, limite);
		return movimentacaoRepository.buscarComFiltros(produtoId, usuarioId, tipo, inicio, fimExclusivo, paginacao);
	}

	/**
	 * Valida tipo, quantidade, motivo, observacao e documento e devolve o tipo
	 * convertido. Reune todos os campos invalidos em uma unica resposta 400.
	 */
	private TipoMovimentacao validar(String tipoTexto, Integer quantidade, String motivoInformado,
			String observacaoInformada, String documento) {
		Map<String, String> erros = new LinkedHashMap<>();

		TipoMovimentacao tipo = converterTipo(tipoTexto, erros);

		if (quantidade == null) {
			erros.putIfAbsent("quantidade", "Informe a quantidade.");
		} else if (tipo == TipoMovimentacao.AJUSTE && quantidade < 0) {
			erros.putIfAbsent("quantidade", "Use um inteiro igual ou maior que zero.");
		} else if ((tipo == TipoMovimentacao.ENTRADA || tipo == TipoMovimentacao.SAIDA)
				&& quantidade <= 0) {
			erros.putIfAbsent("quantidade", "Use um inteiro maior que zero.");
		}

		String motivoTexto = textoOuNulo(motivoInformado);
		MotivoMovimentacao motivo = converterMotivo(motivoTexto, erros);
		if (tipo == TipoMovimentacao.SAIDA && motivoTexto == null) {
			erros.putIfAbsent("motivo", "Escolha o motivo da saída.");
		}

		String observacao = textoOuNulo(observacaoInformada);
		if (motivo == MotivoMovimentacao.OUTRO && observacao == null) {
			erros.putIfAbsent("observacao", "Descreva o motivo na observação.");
		}
		if (tipo == TipoMovimentacao.AJUSTE
				&& (observacao == null || observacao.length() < 10)) {
			erros.putIfAbsent("observacao",
					"Escreva uma justificativa com pelo menos 10 caracteres.");
		}
		if (observacao != null && observacao.length() > 255) {
			erros.putIfAbsent("observacao", "Use no máximo 255 caracteres.");
		}
		if (documento != null && documento.trim().length() > 120) {
			erros.putIfAbsent("documento", "Use no máximo 120 caracteres.");
		}

		if (!erros.isEmpty()) {
			throw new ValidacaoException("Confira os campos destacados.", erros);
		}
		return tipo;
	}

	private TipoMovimentacao converterTipo(String valor, Map<String, String> erros) {
		String normalizado = textoOuNulo(valor);
		if (normalizado == null) {
			erros.putIfAbsent("tipo", "Use ENTRADA, SAIDA ou AJUSTE.");
			return null;
		}
		try {
			TipoMovimentacao tipo = TipoMovimentacao.valueOf(normalizado.toUpperCase(Locale.ROOT));
			if (!TIPOS_ACEITOS.contains(tipo)) {
				erros.putIfAbsent("tipo", "Use ENTRADA, SAIDA ou AJUSTE.");
				return null;
			}
			return tipo;
		} catch (IllegalArgumentException excecao) {
			erros.putIfAbsent("tipo", "Use ENTRADA, SAIDA ou AJUSTE.");
			return null;
		}
	}

	private MotivoMovimentacao converterMotivo(String valor, Map<String, String> erros) {
		if (valor == null) {
			return null;
		}
		try {
			return MotivoMovimentacao.valueOf(valor.toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException excecao) {
			erros.putIfAbsent("motivo", "Escolha um motivo válido.");
			return null;
		}
	}

	private MotivoMovimentacao motivoDe(String valor) {
		String normalizado = textoOuNulo(valor);
		return normalizado == null ? null : MotivoMovimentacao.valueOf(normalizado.toUpperCase(Locale.ROOT));
	}

	private String textoOuNulo(String valor) {
		if (valor == null) {
			return null;
		}
		String normalizado = valor.trim();
		return normalizado.isEmpty() ? null : normalizado;
	}

}
