package com.portalblox.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class ProdutoRequestDto {

	@NotBlank
	@Size(max = 60)
	private String sku;

	@NotBlank
	@Size(max = 180)
	private String nome;

	@NotNull
	private Integer categoriaId;

	@NotNull
	@PositiveOrZero
	private Integer quantidade;

	@NotNull
	private Integer usuarioId;

	@DecimalMin("0.000")
	@Digits(integer = 7, fraction = 3)
	private BigDecimal peso;

	@Size(max = 60)
	private String tamanho;

	@Size(max = 500)
	private String descricao;

	@DecimalMin("0.000")
	@Digits(integer = 12, fraction = 2)
	private BigDecimal preco;

	@Size(max = 120)
	private String marca;

	private Integer fornecedorId;

	private List<@Size(max = 120) String> localizacoes;

	public String getSku() { return sku; }

	public void setSku(String sku) { this.sku = sku; }

	public String getNome() { return nome; }

	public void setNome(String nome) { this.nome = nome; }

	public Integer getCategoriaId() { return categoriaId; }

	public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }

	public Integer getQuantidade() { return quantidade; }

	public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

	public Integer getUsuarioId() { return usuarioId; }

	public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

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

	public Integer getFornecedorId() { return fornecedorId; }

	public void setFornecedorId(Integer fornecedorId) { this.fornecedorId = fornecedorId; }

	public List<String> getLocalizacoes() { return localizacoes; }

	public void setLocalizacoes(List<String> localizacoes) { this.localizacoes = localizacoes; }

}
