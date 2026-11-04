package com.portalblox.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ConfiguracaoResponse",
		description = "Parâmetros globais da loja e contagem de produtos com mínimo próprio")
public class ConfiguracaoResponseDto {

	@Schema(description = "Estoque mínimo global usado quando o produto não tem mínimo próprio",
			example = "5")
	private Integer estoqueMinimoGlobal;

	@Schema(description = "Indica se as fotos dos produtos devem ser exibidas", example = "true")
	private Boolean exibirFotos;

	@Schema(description = "Quantidade de produtos ativos com estoqueMinimo preenchido", example = "4")
	private Long produtosComMinimoProprio;

	public Integer getEstoqueMinimoGlobal() { return estoqueMinimoGlobal; }

	public void setEstoqueMinimoGlobal(Integer estoqueMinimoGlobal) {
		this.estoqueMinimoGlobal = estoqueMinimoGlobal;
	}

	public Boolean getExibirFotos() { return exibirFotos; }

	public void setExibirFotos(Boolean exibirFotos) { this.exibirFotos = exibirFotos; }

	public Long getProdutosComMinimoProprio() { return produtosComMinimoProprio; }

	public void setProdutosComMinimoProprio(Long produtosComMinimoProprio) {
		this.produtosComMinimoProprio = produtosComMinimoProprio;
	}

}
