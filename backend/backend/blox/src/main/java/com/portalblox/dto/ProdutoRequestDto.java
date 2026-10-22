package com.portalblox.dto;

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
	@PositiveOrZero
	private Integer quantidade;                // saldo inicial; ver "Quantidade do produto"

	@PositiveOrZero                            // opcional; a coluna aceita NULL
	private BigDecimal preco;

	@NotNull                                   // RN-10: obrigatório; [] = sem categorias
	private List<Integer> categoriaIds;

	public String getSku() { return sku; }

	public void setSku(String sku) { this.sku = sku; }

	public String getNome() { return nome; }

	public void setNome(String nome) { this.nome = nome; }

	public Integer getQuantidade() { return quantidade; }

	public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

	public BigDecimal getPreco() { return preco; }

	public void setPreco(BigDecimal preco) { this.preco = preco; }

	public List<Integer> getCategoriaIds() { return categoriaIds; }

	public void setCategoriaIds(List<Integer> categoriaIds) { this.categoriaIds = categoriaIds; }

}
