package com.portalblox.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProdutoResponseDto {

	private Integer id;

	private String sku;

	private String nome;

	private Integer quantidade;

	private BigDecimal preco;

	private Boolean ativo;

	private List<CategoriaResponseDto> categorias;

	public static class CategoriaResponseDto {

		private Integer id;

		private String nome;

		public Integer getId() { return id; }

		public void setId(Integer id) { this.id = id; }

		public String getNome() { return nome; }

		public void setNome(String nome) { this.nome = nome; }

	}

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getSku() { return sku; }

	public void setSku(String sku) { this.sku = sku; }

	public String getNome() { return nome; }

	public void setNome(String nome) { this.nome = nome; }

	public Integer getQuantidade() { return quantidade; }

	public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

	public BigDecimal getPreco() { return preco; }

	public void setPreco(BigDecimal preco) { this.preco = preco; }

	public Boolean getAtivo() { return ativo; }

	public void setAtivo(Boolean ativo) { this.ativo = ativo; }

	public List<CategoriaResponseDto> getCategorias() { return categorias; }

	public void setCategorias(List<CategoriaResponseDto> categorias) { this.categorias = categorias; }

}
