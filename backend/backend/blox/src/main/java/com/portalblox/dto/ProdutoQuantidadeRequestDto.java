package com.portalblox.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ProdutoQuantidadeRequestDto {

	@NotNull
	@PositiveOrZero
	private Integer quantidade;

	public Integer getQuantidade() { return quantidade; }

	public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

}
