package com.portalblox.service;

import java.time.LocalDate;
import java.util.List;

/**
 * Indicadores calculados por DashboardService, sem dependencia da camada HTTP.
 */
public class DashboardResumo {

	private final long totalProdutosAtivos;
	private final long produtosBaixoEstoque;
	private final long produtosZerados;
	private final long movimentacoesHoje;
	private final List<EntradasSaidasDia> entradasSaidas7Dias;
	private final List<ProdutoMovimentado> maisMovimentados;

	public DashboardResumo(long totalProdutosAtivos, long produtosBaixoEstoque, long produtosZerados,
			long movimentacoesHoje, List<EntradasSaidasDia> entradasSaidas7Dias,
			List<ProdutoMovimentado> maisMovimentados) {
		this.totalProdutosAtivos = totalProdutosAtivos;
		this.produtosBaixoEstoque = produtosBaixoEstoque;
		this.produtosZerados = produtosZerados;
		this.movimentacoesHoje = movimentacoesHoje;
		this.entradasSaidas7Dias = entradasSaidas7Dias;
		this.maisMovimentados = maisMovimentados;
	}

	public long getTotalProdutosAtivos() { return totalProdutosAtivos; }

	public long getProdutosBaixoEstoque() { return produtosBaixoEstoque; }

	public long getProdutosZerados() { return produtosZerados; }

	public long getMovimentacoesHoje() { return movimentacoesHoje; }

	public List<EntradasSaidasDia> getEntradasSaidas7Dias() { return entradasSaidas7Dias; }

	public List<ProdutoMovimentado> getMaisMovimentados() { return maisMovimentados; }

	public static class EntradasSaidasDia {

		private final LocalDate data;
		private final long entradas;
		private final long saidas;

		public EntradasSaidasDia(LocalDate data, long entradas, long saidas) {
			this.data = data;
			this.entradas = entradas;
			this.saidas = saidas;
		}

		public LocalDate getData() { return data; }

		public long getEntradas() { return entradas; }

		public long getSaidas() { return saidas; }

	}

	public static class ProdutoMovimentado {

		private final Integer id;
		private final String sku;
		private final String nome;
		private final long totalMovimentado;

		public ProdutoMovimentado(Integer id, String sku, String nome, long totalMovimentado) {
			this.id = id;
			this.sku = sku;
			this.nome = nome;
			this.totalMovimentado = totalMovimentado;
		}

		public Integer getId() { return id; }

		public String getSku() { return sku; }

		public String getNome() { return nome; }

		public long getTotalMovimentado() { return totalMovimentado; }

	}

}
