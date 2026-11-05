package com.portalblox.dto;

import java.time.LocalDate;
import java.util.List;

public class DashboardResumoResponseDto {

	private long totalProdutosAtivos;

	private long produtosBaixoEstoque;

	private long produtosZerados;

	private long movimentacoesHoje;

	private List<EntradasSaidasDia> entradasSaidas7Dias;

	private List<ProdutoMovimentado> maisMovimentados;

	public static class EntradasSaidasDia {

		private LocalDate data;

		private long entradas;

		private long saidas;

		public LocalDate getData() { return data; }

		public void setData(LocalDate data) { this.data = data; }

		public long getEntradas() { return entradas; }

		public void setEntradas(long entradas) { this.entradas = entradas; }

		public long getSaidas() { return saidas; }

		public void setSaidas(long saidas) { this.saidas = saidas; }

	}

	public static class ProdutoMovimentado {

		private ProdutoResumo produto;

		private long totalMovimentado;

		public ProdutoResumo getProduto() { return produto; }

		public void setProduto(ProdutoResumo produto) { this.produto = produto; }

		public long getTotalMovimentado() { return totalMovimentado; }

		public void setTotalMovimentado(long totalMovimentado) { this.totalMovimentado = totalMovimentado; }

	}

	public static class ProdutoResumo {

		private Integer id;

		private String sku;

		private String nome;

		public Integer getId() { return id; }

		public void setId(Integer id) { this.id = id; }

		public String getSku() { return sku; }

		public void setSku(String sku) { this.sku = sku; }

		public String getNome() { return nome; }

		public void setNome(String nome) { this.nome = nome; }

	}

	public long getTotalProdutosAtivos() { return totalProdutosAtivos; }

	public void setTotalProdutosAtivos(long totalProdutosAtivos) { this.totalProdutosAtivos = totalProdutosAtivos; }

	public long getProdutosBaixoEstoque() { return produtosBaixoEstoque; }

	public void setProdutosBaixoEstoque(long produtosBaixoEstoque) { this.produtosBaixoEstoque = produtosBaixoEstoque; }

	public long getProdutosZerados() { return produtosZerados; }

	public void setProdutosZerados(long produtosZerados) { this.produtosZerados = produtosZerados; }

	public long getMovimentacoesHoje() { return movimentacoesHoje; }

	public void setMovimentacoesHoje(long movimentacoesHoje) { this.movimentacoesHoje = movimentacoesHoje; }

	public List<EntradasSaidasDia> getEntradasSaidas7Dias() { return entradasSaidas7Dias; }

	public void setEntradasSaidas7Dias(List<EntradasSaidasDia> entradasSaidas7Dias) {
		this.entradasSaidas7Dias = entradasSaidas7Dias;
	}

	public List<ProdutoMovimentado> getMaisMovimentados() { return maisMovimentados; }

	public void setMaisMovimentados(List<ProdutoMovimentado> maisMovimentados) {
		this.maisMovimentados = maisMovimentados;
	}

}
