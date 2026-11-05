package com.portalblox.dto;

import com.portalblox.entity.TipoMovimentacao;
import com.portalblox.exception.ValidacaoException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Filtros de GET /movimentacoes ja convertidos. Os parametros chegam como texto para
 * que todos os invalidos sejam reunidos em uma unica resposta 400.
 */
public class MovimentacaoFiltro {

	private static final Set<TipoMovimentacao> TIPOS_FILTRO = EnumSet.of(
			TipoMovimentacao.SALDO_INICIAL, TipoMovimentacao.ENTRADA,
			TipoMovimentacao.SAIDA, TipoMovimentacao.AJUSTE);

	private final Integer produtoId;
	private final Integer usuarioId;
	private final TipoMovimentacao tipo;
	private final LocalDate de;
	private final LocalDate ate;
	private final Integer limite;

	private MovimentacaoFiltro(Integer produtoId, Integer usuarioId, TipoMovimentacao tipo,
			LocalDate de, LocalDate ate, Integer limite) {
		this.produtoId = produtoId;
		this.usuarioId = usuarioId;
		this.tipo = tipo;
		this.de = de;
		this.ate = ate;
		this.limite = limite;
	}

	public Integer getProdutoId() { return produtoId; }

	public Integer getUsuarioId() { return usuarioId; }

	public TipoMovimentacao getTipo() { return tipo; }

	public LocalDate getDe() { return de; }

	public LocalDate getAte() { return ate; }

	public Integer getLimite() { return limite; }

	public static MovimentacaoFiltro validar(String produtoId, String usuarioId, String tipo,
			String de, String ate, String limite) {
		Map<String, String> erros = new LinkedHashMap<>();

		Integer produto = inteiroPositivo(produtoId, "produtoId", "Use um identificador numérico.", erros);
		Integer usuario = inteiroPositivo(usuarioId, "usuarioId", "Use um identificador numérico.", erros);
		Integer limiteNumerico = inteiroPositivo(limite, "limite", "Use um inteiro maior que zero.", erros);
		TipoMovimentacao tipoEnum = converterTipo(tipo, erros);
		LocalDate dataInicial = converterData(de, "de", erros);
		LocalDate dataFinal = converterData(ate, "ate", erros);

		if (!erros.isEmpty()) {
			throw new ValidacaoException("Confira os filtros informados.", erros);
		}
		return new MovimentacaoFiltro(produto, usuario, tipoEnum, dataInicial, dataFinal, limiteNumerico);
	}

	private static Integer inteiroPositivo(String valor, String campo, String mensagem, Map<String, String> erros) {
		if (valor == null) {
			return null;
		}
		if (!valor.matches("\\d+")) {
			erros.put(campo, mensagem);
			return null;
		}
		try {
			int numero = Integer.parseInt(valor);
			if (numero <= 0) {
				erros.put(campo, mensagem);
				return null;
			}
			return numero;
		} catch (NumberFormatException excecao) {
			erros.put(campo, mensagem);
			return null;
		}
	}

	private static TipoMovimentacao converterTipo(String valor, Map<String, String> erros) {
		if (valor == null) {
			return null;
		}
		try {
			TipoMovimentacao tipo = TipoMovimentacao.valueOf(valor.trim().toUpperCase(Locale.ROOT));
			if (!TIPOS_FILTRO.contains(tipo)) {
				erros.put("tipo", "Use SALDO_INICIAL, ENTRADA, SAIDA ou AJUSTE.");
				return null;
			}
			return tipo;
		} catch (IllegalArgumentException excecao) {
			erros.put("tipo", "Use SALDO_INICIAL, ENTRADA, SAIDA ou AJUSTE.");
			return null;
		}
	}

	private static LocalDate converterData(String valor, String campo, Map<String, String> erros) {
		if (valor == null) {
			return null;
		}
		try {
			return LocalDate.parse(valor);
		} catch (DateTimeParseException excecao) {
			erros.put(campo, "Use uma data no formato AAAA-MM-DD.");
			return null;
		}
	}

}
