package com.portalblox.mapper;

import com.portalblox.dto.ConfiguracaoResponseDto;
import com.portalblox.entity.Configuracao;

public class ConfiguracaoMapper {

	public static ConfiguracaoResponseDto toResponseDto(Configuracao entity, long produtosComMinimoProprio) {
		ConfiguracaoResponseDto dto = new ConfiguracaoResponseDto();
		dto.setEstoqueMinimoGlobal(entity.getEstoqueMinimoGlobal());
		dto.setExibirFotos(entity.getExibirFotos());
		dto.setProdutosComMinimoProprio(produtosComMinimoProprio);
		return dto;
	}

}
