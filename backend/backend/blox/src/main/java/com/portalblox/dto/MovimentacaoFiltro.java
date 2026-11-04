package com.portalblox.dto;

import com.portalblox.dto.ErroResponseDto.ErroCampo;
import com.portalblox.entity.TipoMovimentacao;
import com.portalblox.exception.ValidacaoException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Filtros de GET /movimentacoes ja convertidos. Os parametros chegam como texto para
 * que todos os invalidos sejam reunidos em uma unica resposta 400.
 */
public record MovimentacaoFiltro(Integer produtoId, Integer usuarioId, TipoMovimentacao tipo,
		LocalDate de, LocalDate ate, Integer limite) {

	private static final Set<TipoMovimentacao> TIPOS_FILTRO = EnumSet.of(
			TipoMovimentacao.SALDO_INICIAL, TipoMovimentacao.ENTRADA,
			TipoMovimentacao.SAIDA, TipoMovimentacao.AJUSTE);

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
			List<ErroCampo> campos = new ArrayList<>();
			erros.forEach((campo, mensagem) -> campos.add(new ErroCampo(campo, mensagem)));
			throw new ValidacaoException("Confira os filtros informados.", campos);
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
