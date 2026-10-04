package com.portalblox.mapper;

import com.portalblox.dto.ProdutoAtualizacaoRequestDto;
import com.portalblox.dto.ProdutoRequestDto;
import com.portalblox.dto.ProdutoResponseDto;
import com.portalblox.entity.Produto;
import java.util.List;

public class ProdutoMapper {

	public static Produto toEntity(ProdutoRequestDto dto) {
		if (dto == null) {
			return null;
		}
		Produto produto = new Produto();
		produto.setSku(dto.getSku());
		produto.setNome(dto.getNome());
		produto.setQuantidade(dto.getQuantidade());
		produto.setPreco(dto.getPreco());       // opcional
		return produto;      // ativo e categorias são tratados pelo service
	}

	public static Produto toEntity(ProdutoAtualizacaoRequestDto dto) {
		if (dto == null) {
			return null;
		}
		Produto produto = new Produto();
		produto.setSku(dto.getSku());
		produto.setNome(dto.getNome());
		produto.setPreco(dto.getPreco());       // opcional
		return produto;      // quantidade, ativo e categorias são tratados pelo service
	}

	public static ProdutoResponseDto toResponseDto(Produto entity) {
		if (entity == null) {
			return null;
		}
		ProdutoResponseDto dto = new ProdutoResponseDto();
		dto.setId(entity.getId());
		dto.setSku(entity.getSku());
		dto.setNome(entity.getNome());
		dto.setQuantidade(entity.getQuantidade());
		dto.setPreco(entity.getPreco());
		dto.setAtivo(entity.getAtivo());
		dto.setCategorias(entity.getCategorias().stream()
				.map(vinculo -> {
					ProdutoResponseDto.CategoriaResponseDto categoriaDto =
							new ProdutoResponseDto.CategoriaResponseDto();
					categoriaDto.setId(vinculo.getCategoria().getId());
					categoriaDto.setNome(vinculo.getCategoria().getNome());
					return categoriaDto;
				})
				.toList());
		return dto;
	}

	public static List<ProdutoResponseDto> toResponseDto(List<Produto> entities) {
		return entities.stream()
				.map(ProdutoMapper::toResponseDto)
				.toList();
	}

}
