package com.portalblox.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class ProdutoQuantidadeRequestDto {

	@NotNull
	@PositiveOrZero
	private Integer quantidade;

	@NotNull
	private Integer usuarioId;

	@NotBlank
	@Size(max = 255)
	private String justificativa;

	public Integer getQuantidade() { return quantidade; }

	public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

	public Integer getUsuarioId() { return usuarioId; }

	public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

	public String getJustificativa() { return justificativa; }

	public void setJustificativa(String justificativa) { this.justificativa = justificativa; }

}
