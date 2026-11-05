package com.portalblox.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AvisoFaltaRequestDto {

	@Schema(example = "1")
	@NotNull(message = "Informe o produto.")
	private Integer produtoId;

	@Schema(example = "3")
	@NotNull(message = "Informe o usuário.")
	private Integer usuarioId;

	@Schema(example = "Acabou na prateleira")
	@Size(max = 255, message = "A observação deve ter no máximo 255 caracteres.")
	private String observacao;

	public Integer getProdutoId() { return produtoId; }

	public void setProdutoId(Integer produtoId) { this.produtoId = produtoId; }

	public Integer getUsuarioId() { return usuarioId; }

	public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

	public String getObservacao() { return observacao; }

	public void setObservacao(String observacao) { this.observacao = observacao; }

}
