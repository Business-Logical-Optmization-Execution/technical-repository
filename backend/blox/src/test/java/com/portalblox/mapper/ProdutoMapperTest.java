package com.portalblox.mapper;

import com.portalblox.dto.ProdutoAtualizacaoRequestDto;
import com.portalblox.dto.ProdutoRequestDto;
import com.portalblox.dto.ProdutoResponseDto;
import com.portalblox.entity.Categoria;
import com.portalblox.entity.Produto;
import com.portalblox.entity.ProdutoCategoria;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProdutoMapperTest {

	@Test
	void toEntityConverteDtoParaEntidade() {
		ProdutoRequestDto dto = new ProdutoRequestDto();
		dto.setSku("SKU-100");
		dto.setNome("Broca 10mm");
		dto.setQuantidade(25);
		dto.setPreco(new BigDecimal("12.50"));
		dto.setCategoriaIds(List.of(1, 2));

		Produto produto = ProdutoMapper.toEntity(dto);

		assertEquals("SKU-100", produto.getSku());
		assertEquals("Broca 10mm", produto.getNome());
		assertEquals(25, produto.getQuantidade());
		assertEquals(new BigDecimal("12.50"), produto.getPreco());
		assertNull(produto.getAtivo(), "ativo é tratado pelo service");
		assertTrue(produto.getCategorias().isEmpty(), "categorias são tratadas pelo service");
	}

	@Test
	void toEntityComDtoNuloRetornaNulo() {
		assertNull(ProdutoMapper.toEntity((ProdutoRequestDto) null));
	}

	@Test
	void toEntityDeAtualizacaoIgnoraQuantidade() {
		ProdutoAtualizacaoRequestDto dto = new ProdutoAtualizacaoRequestDto();
		dto.setSku("SKU-300");
		dto.setNome("Martelo");
		dto.setPreco(new BigDecimal("89.90"));
		dto.setCategoriaIds(List.of(2));

		Produto produto = ProdutoMapper.toEntity(dto);

		assertEquals("SKU-300", produto.getSku());
		assertEquals("Martelo", produto.getNome());
		assertEquals(new BigDecimal("89.90"), produto.getPreco());
		assertNull(produto.getQuantidade(), "quantidade não vem no PUT");
		assertNull(produto.getAtivo(), "ativo é tratado pelo service");
		assertTrue(produto.getCategorias().isEmpty(), "categorias são tratadas pelo service");
	}

	@Test
	void toEntityDeAtualizacaoComDtoNuloRetornaNulo() {
		assertNull(ProdutoMapper.toEntity((ProdutoAtualizacaoRequestDto) null));
	}

	@Test
	void toResponseDtoConverteEntidadeComCategorias() {
		Produto produto = new Produto();
		produto.setId(7);
		produto.setSku("SKU-200");
		produto.setNome("Serra Tico-Tico");
		produto.setQuantidade(3);
		produto.setPreco(new BigDecimal("299.99"));
		produto.setAtivo(true);

		Categoria categoria = new Categoria();
		categoria.setId(1);
		categoria.setNome("Ferragens");
		produto.getCategorias().add(new ProdutoCategoria(produto, categoria));

		ProdutoResponseDto dto = ProdutoMapper.toResponseDto(produto);

		assertEquals(7, dto.getId());
		assertEquals("SKU-200", dto.getSku());
		assertEquals("Serra Tico-Tico", dto.getNome());
		assertEquals(3, dto.getQuantidade());
		assertEquals(new BigDecimal("299.99"), dto.getPreco());
		assertEquals(true, dto.getAtivo());
		assertEquals(1, dto.getCategorias().size());
		assertEquals(1, dto.getCategorias().get(0).getId());
		assertEquals("Ferragens", dto.getCategorias().get(0).getNome());
	}

	@Test
	void toResponseDtoComEntidadeNulaRetornaNula() {
		assertNull(ProdutoMapper.toResponseDto((Produto) null));
	}

	@Test
	void toResponseDtoDeListaConverteTodosOsElementos() {
		Produto primeiro = new Produto();
		primeiro.setId(1);
		primeiro.setSku("SKU-A");
		Produto segundo = new Produto();
		segundo.setId(2);
		segundo.setSku("SKU-B");

		List<ProdutoResponseDto> dtos = ProdutoMapper.toResponseDto(List.of(primeiro, segundo));

		assertEquals(2, dtos.size());
		assertEquals("SKU-A", dtos.get(0).getSku());
		assertEquals("SKU-B", dtos.get(1).getSku());
		assertTrue(dtos.get(0).getCategorias().isEmpty());
	}

}
