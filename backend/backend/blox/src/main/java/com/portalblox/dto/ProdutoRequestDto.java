package com.portalblox.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class ProdutoRequestDto {

	@Schema(example = "3001")
	@NotBlank
	@Size(max = 60)
	private String sku;

	@Schema(example = "Martelo de bola")
	@NotBlank
	@Size(max = 180)
	private String nome;

	@Schema(example = "1")
	@NotNull
	private Integer categoriaId;

	@Schema(example = "10")
	@NotNull
	@PositiveOrZero
	private Integer quantidade;

	@Schema(example = "1")
	@NotNull
	private Integer usuarioId;

	@Schema(example = "1.25")
	@DecimalMin("0.000")
	@Digits(integer = 7, fraction = 3)
	private BigDecimal peso;

	@Schema(example = "27 mm")
	@Size(max = 60)
	private String tamanho;

	@Schema(example = "Martelo de bola 300 g, cabo de fibra.")
	@Size(max = 500)
	private String descricao;

	@Schema(example = "59.9")
	@DecimalMin("0.000")
	@Digits(integer = 12, fraction = 2)
	private BigDecimal preco;

	@Schema(example = "Tramontina")
	@Size(max = 120)
	private String marca;

	@Schema(example = "https://exemplo.com/produto.jpg")
	@Size(max = 500, message = "A foto deve ter no máximo 500 caracteres.")
	private String fotoUrl;

	@Schema(example = "2")
	@PositiveOrZero(message = "O estoque mínimo deve ser igual ou maior que zero.")
	private Integer estoqueMinimo;

	@Schema(example = "1")
	private Integer fornecedorId;

	@ArraySchema(schema = @Schema(example = "C3-P2"))
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

	public String getFotoUrl() { return fotoUrl; }

	public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

	public Integer getEstoqueMinimo() { return estoqueMinimo; }

	public void setEstoqueMinimo(Integer estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }

	public Integer getFornecedorId() { return fornecedorId; }

	public void setFornecedorId(Integer fornecedorId) { this.fornecedorId = fornecedorId; }

	public List<String> getLocalizacoes() { return localizacoes; }

	public void setLocalizacoes(List<String> localizacoes) { this.localizacoes = localizacoes; }

}
