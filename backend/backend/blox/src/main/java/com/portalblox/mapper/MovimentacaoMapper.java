package com.portalblox.mapper;

import com.portalblox.dto.MovimentacaoResponseDto;
import com.portalblox.entity.MovimentacaoEstoque;
import java.util.List;

public class MovimentacaoMapper {

	public static MovimentacaoResponseDto toResponseDto(MovimentacaoEstoque entity) {
		if (entity == null) {
			return null;
		}
		MovimentacaoResponseDto dto = new MovimentacaoResponseDto();
		dto.setId(entity.getId());
		dto.setTipo(entity.getTipo());
		dto.setQuantidade(entity.getVariacao());
		dto.setQuantidadeAntes(entity.getQuantidadeAntes());
		dto.setQuantidadeDepois(entity.getQuantidadeDepois());
		dto.setMotivo(entity.getMotivo());
		dto.setObservacao(entity.getObservacao());
		dto.setDocumento(entity.getDocumento());
		dto.setOrigem(entity.getOrigem());
		dto.setDataMov(entity.getCriadoEm());

		if (entity.getProduto() != null) {
			MovimentacaoResponseDto.ProdutoResumoResponseDto produto =
					new MovimentacaoResponseDto.ProdutoResumoResponseDto();
			produto.setId(entity.getProduto().getId());
			produto.setSku(entity.getProduto().getSku());
			produto.setNome(entity.getProduto().getNome());
			dto.setProduto(produto);
		}

		if (entity.getUsuario() != null) {
			MovimentacaoResponseDto.UsuarioResumoResponseDto usuario =
					new MovimentacaoResponseDto.UsuarioResumoResponseDto();
			usuario.setId(entity.getUsuario().getId());
			usuario.setNome(entity.getUsuario().getNome());
			dto.setUsuario(usuario);
		}

		return dto;
	}

	public static List<MovimentacaoResponseDto> toResponseDto(List<MovimentacaoEstoque> entities) {
		return entities.stream()
				.map(MovimentacaoMapper::toResponseDto)
				.toList();
	}

}
