package com.portalblox.mapper;

import com.portalblox.dto.AvisoFaltaResponseDto;
import com.portalblox.dto.MovimentacaoResponseDto;
import com.portalblox.entity.AvisoFalta;
import java.util.List;

public class AvisoFaltaMapper {

	public static AvisoFaltaResponseDto toResponseDto(AvisoFalta entity) {
		AvisoFaltaResponseDto dto = new AvisoFaltaResponseDto();
		dto.setId(entity.getId());
		dto.setStatus(entity.getStatus());
		dto.setObservacao(entity.getObservacao());
		dto.setQuantidadeAvisos(entity.getQuantidadeAvisos());
		dto.setSomado(entity.foiSomado());
		dto.setCriadoEm(entity.getCriadoEm());

		MovimentacaoResponseDto.ProdutoResumoResponseDto produto =
				new MovimentacaoResponseDto.ProdutoResumoResponseDto();
		produto.setId(entity.getProduto().getId());
		produto.setSku(entity.getProduto().getSku());
		produto.setNome(entity.getProduto().getNome());
		dto.setProduto(produto);

		MovimentacaoResponseDto.UsuarioResumoResponseDto usuario =
				new MovimentacaoResponseDto.UsuarioResumoResponseDto();
		usuario.setId(entity.getUsuario().getId());
		usuario.setNome(entity.getUsuario().getNome());
		dto.setUsuario(usuario);
		return dto;
	}

	public static List<AvisoFaltaResponseDto> toResponseDto(List<AvisoFalta> entities) {
		return entities.stream()
				.map(AvisoFaltaMapper::toResponseDto)
				.toList();
	}

}
