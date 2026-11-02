package com.portalblox.mapper;

import com.portalblox.dto.ProdutoAtualizacaoRequestDto;
import com.portalblox.dto.ProdutoRequestDto;
import com.portalblox.dto.ProdutoResponseDto;
import com.portalblox.entity.Produto;
import com.portalblox.entity.ProdutoLocalizacao;
import java.util.List;

public class ProdutoMapper {

	public static Produto toEntity(ProdutoRequestDto dto) {
		if (dto == null) {
			return null;
		}
		Produto produto = new Produto();
		produto.setSku(texto(dto.getSku()));
		produto.setNome(texto(dto.getNome()));
		produto.setQuantidade(dto.getQuantidade());
		produto.setPeso(dto.getPeso());
		produto.setTamanho(texto(dto.getTamanho()));
		produto.setDescricao(texto(dto.getDescricao()));
		produto.setPreco(dto.getPreco());
		produto.setMarca(texto(dto.getMarca()));

		return produto;
	}

	public static Produto toEntity(ProdutoAtualizacaoRequestDto dto) {
		if (dto == null) {
			return null;
		}
		Produto produto = new Produto();
		produto.setSku(texto(dto.getSku()));
		produto.setNome(texto(dto.getNome()));
		produto.setPeso(dto.getPeso());
		produto.setTamanho(texto(dto.getTamanho()));
		produto.setDescricao(texto(dto.getDescricao()));
		produto.setPreco(dto.getPreco());
		produto.setMarca(texto(dto.getMarca()));

		return produto;
	}

	public static ProdutoResponseDto toResponseDto(Produto entity) {
		if (entity == null) {
			return null;
		}
		ProdutoResponseDto dto = new ProdutoResponseDto();
		dto.setId(entity.getId());
		dto.setSku(entity.getSku());
		dto.setNome(entity.getNome());
		dto.setPeso(entity.getPeso());
		dto.setTamanho(entity.getTamanho());
		dto.setDescricao(entity.getDescricao());
		dto.setPreco(entity.getPreco());
		dto.setMarca(entity.getMarca());
		dto.setQuantidade(entity.getQuantidade());
		dto.setAtivo(entity.getAtivo());

		if (entity.getCategoria() != null) {
			ProdutoResponseDto.CategoriaResponseDto categoriaDto =
					new ProdutoResponseDto.CategoriaResponseDto();
			categoriaDto.setId(entity.getCategoria().getId());
			categoriaDto.setNome(entity.getCategoria().getNome());
			dto.setCategoria(categoriaDto);
		}

		if (entity.getFornecedor() != null) {
			ProdutoResponseDto.FornecedorResumoResponseDto fornecedorDto =
					new ProdutoResponseDto.FornecedorResumoResponseDto();
			fornecedorDto.setId(entity.getFornecedor().getId());
			fornecedorDto.setNome(entity.getFornecedor().getNome());
			fornecedorDto.setCnpj(entity.getFornecedor().getCnpj());
			dto.setFornecedor(fornecedorDto);
		}

		dto.setLocalizacoes(entity.getLocalizacoes().stream()
				.map(ProdutoLocalizacao::getNome)
				.sorted(String.CASE_INSENSITIVE_ORDER)
				.toList());
		return dto;
	}

	public static List<ProdutoResponseDto> toResponseDto(List<Produto> entities) {
		return entities.stream()
				.map(ProdutoMapper::toResponseDto)
				.toList();
	}

	private static String texto(String valor) {
		return valor == null ? null : valor.trim();
	}

}
