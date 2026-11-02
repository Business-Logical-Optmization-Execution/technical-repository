package com.portalblox.mapper;

import com.portalblox.dto.FornecedorRequestDto;
import com.portalblox.dto.FornecedorResponseDto;
import com.portalblox.entity.Fornecedor;
import java.util.List;

public class FornecedorMapper {

	public static Fornecedor toEntity(FornecedorRequestDto dto) {
		if (dto == null) {
			return null;
		}
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setNome(dto.getNome());
		fornecedor.setCnpj(dto.getCnpj());
		return fornecedor;
	}

	public static FornecedorResponseDto toResponseDto(Fornecedor entity) {
		if (entity == null) {
			return null;
		}
		FornecedorResponseDto dto = new FornecedorResponseDto();
		dto.setId(entity.getId());
		dto.setNome(entity.getNome());
		dto.setCnpj(entity.getCnpj());
		dto.setAtivo(entity.getAtivo());
		dto.setCriadoEm(entity.getCriadoEm());
		dto.setAtualizadoEm(entity.getAtualizadoEm());
		return dto;
	}

	public static List<FornecedorResponseDto> toResponseDto(List<Fornecedor> entities) {
		return entities.stream()
				.map(FornecedorMapper::toResponseDto)
				.toList();
	}

}
