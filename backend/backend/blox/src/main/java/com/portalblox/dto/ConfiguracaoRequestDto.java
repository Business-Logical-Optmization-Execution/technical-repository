package com.portalblox.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ConfiguracaoRequestDto {

	@Schema(example = "5")
	@NotNull(message = "Use um número inteiro igual ou maior que zero.")
	@PositiveOrZero(message = "Use um número inteiro igual ou maior que zero.")
	private Integer estoqueMinimoGlobal;

	@Schema(example = "true")
	@NotNull(message = "Informe se as fotos devem ser exibidas.")
	private Boolean exibirFotos;

	public Integer getEstoqueMinimoGlobal() { return estoqueMinimoGlobal; }

	public void setEstoqueMinimoGlobal(Integer estoqueMinimoGlobal) { this.estoqueMinimoGlobal = estoqueMinimoGlobal; }

	public Boolean getExibirFotos() { return exibirFotos; }

	public void setExibirFotos(Boolean exibirFotos) { this.exibirFotos = exibirFotos; }

}
