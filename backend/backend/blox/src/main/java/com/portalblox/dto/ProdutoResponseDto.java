package com.portalblox.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProdutoResponseDto {

	private Integer id;

	private String sku;

	private String nome;

	private BigDecimal peso;

	private String tamanho;

	private String descricao;

	private BigDecimal preco;

	private String marca;

	private Integer quantidade;

	private Boolean ativo;

	private CategoriaResponseDto categoria;

	private FornecedorResumoResponseDto fornecedor;

	private List<String> localizacoes;

	public static class CategoriaResponseDto {

		private Integer id;

		private String nome;

		public Integer getId() { return id; }

		public void setId(Integer id) { this.id = id; }

		public String getNome() { return nome; }

		public void setNome(String nome) { this.nome = nome; }

	}

	public static class FornecedorResumoResponseDto {

		private Integer id;

		private String nome;

		private String cnpj;

		public Integer getId() { return id; }

		public void setId(Integer id) { this.id = id; }

		public String getNome() { return nome; }

		public void setNome(String nome) { this.nome = nome; }

		public String getCnpj() { return cnpj; }

		public void setCnpj(String cnpj) { this.cnpj = cnpj; }

	}

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getSku() { return sku; }

	public void setSku(String sku) { this.sku = sku; }

	public String getNome() { return nome; }

	public void setNome(String nome) { this.nome = nome; }

	public BigDecimal getPeso() { return peso; }

	public void setPeso(BigDecimal peso) { this.peso = peso; }

	public String getTamanho() { return tamanho; }

	public void setTamanho(String tamanho) { this.tamanho = tamanho; }

	public String getDescricao() { return descricao; }

	public void setDescricao(String descricao) { this.descricao = descricao; }

	public BigDecimal getPreco() { return preco; }

	public void setPreco(BigDecimal preco) { this.preco = preco; }

	public String getMarca() { return marca; }

	public void setMarca(String marca) { this.marca = marca; }

	public Integer getQuantidade() { return quantidade; }

	public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

	public Boolean getAtivo() { return ativo; }

	public void setAtivo(Boolean ativo) { this.ativo = ativo; }

	public CategoriaResponseDto getCategoria() { return categoria; }

	public void setCategoria(CategoriaResponseDto categoria) { this.categoria = categoria; }

	public FornecedorResumoResponseDto getFornecedor() { return fornecedor; }

	public void setFornecedor(FornecedorResumoResponseDto fornecedor) { this.fornecedor = fornecedor; }

	public List<String> getLocalizacoes() { return localizacoes; }

	public void setLocalizacoes(List<String> localizacoes) { this.localizacoes = localizacoes; }

}
