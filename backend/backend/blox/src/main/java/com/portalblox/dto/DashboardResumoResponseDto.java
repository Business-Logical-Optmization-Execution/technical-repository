package com.portalblox.dto;

import java.time.LocalDate;
import java.util.List;

public record DashboardResumoResponseDto(
		long totalProdutosAtivos,
		long produtosBaixoEstoque,
		long produtosZerados,
		long movimentacoesHoje,
		List<EntradasSaidasDia> entradasSaidas7Dias,
		List<ProdutoMovimentado> maisMovimentados) {

	public record EntradasSaidasDia(LocalDate data, long entradas, long saidas) {
	}

	public record ProdutoMovimentado(ProdutoResumo produto, long totalMovimentado) {
	}

	public record ProdutoResumo(Integer id, String sku, String nome) {
	}

}
