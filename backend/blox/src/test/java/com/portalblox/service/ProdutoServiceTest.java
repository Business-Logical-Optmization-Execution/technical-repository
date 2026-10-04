package com.portalblox.service;

import com.portalblox.entity.Categoria;
import com.portalblox.entity.Produto;
import com.portalblox.entity.ProdutoCategoria;
import com.portalblox.exception.CategoriaNaoEncontradaException;
import com.portalblox.exception.ProdutoJaExisteException;
import com.portalblox.exception.ProdutoJaInativoException;
import com.portalblox.exception.ProdutoNaoEncontradoException;
import com.portalblox.repository.CategoriaRepository;
import com.portalblox.repository.ProdutoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

	@Mock
	private ProdutoRepository produtoRepository;

	@Mock
	private CategoriaRepository categoriaRepository;

	@InjectMocks
	private ProdutoService produtoService;

	private Categoria categoria(Integer id, String nome) {
		Categoria categoria = new Categoria();
		categoria.setId(id);
		categoria.setNome(nome);
		return categoria;
	}

	private Produto produto(Integer id, String sku, Integer quantidade, Boolean ativo) {
		Produto produto = new Produto();
		produto.setId(id);
		produto.setSku(sku);
		produto.setNome("Produto " + sku);
		produto.setQuantidade(quantidade);
		produto.setAtivo(ativo);
		return produto;
	}

	// ----------------------------------------------------------- cadastrar

	@Test
	void cadastrarCaminhoFeliz() {
		Produto novo = produto(null, "SKU-100", 10, null);
		when(produtoRepository.existsBySku("SKU-100")).thenReturn(false);
		when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria(1, "Ferragens")));
		when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

		Produto salvo = produtoService.cadastrar(novo, List.of(1));

		assertEquals(true, salvo.getAtivo(), "novo produto nasce ativo");
		assertEquals(10, salvo.getQuantidade(), "quantidade inicial vem do request");
		assertEquals(1, salvo.getCategorias().size());
		assertEquals(1, salvo.getCategorias().get(0).getCategoria().getId());
		verify(produtoRepository).save(salvo);
	}

	@Test
	void cadastrarComSkuDuplicadoRetorna409() {
		Produto novo = produto(null, "SKU-100", 10, null);
		when(produtoRepository.existsBySku("SKU-100")).thenReturn(true);

		assertThrows(ProdutoJaExisteException.class,
				() -> produtoService.cadastrar(novo, List.of(1)));

		verify(produtoRepository, never()).save(any(Produto.class));
	}

	@Test
	void cadastrarComCategoriaInexistenteRetorna404SemAlterarVinculos() {
		Produto novo = produto(null, "SKU-100", 10, null);
		when(produtoRepository.existsBySku("SKU-100")).thenReturn(false);
		when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria(1, "Ferragens")));
		when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(CategoriaNaoEncontradaException.class,
				() -> produtoService.cadastrar(novo, List.of(1, 99)));

		assertTrue(novo.getCategorias().isEmpty(), "RN-13: nenhum vínculo pode ser alterado");
		verify(produtoRepository, never()).save(any(Produto.class));
	}

	@Test
	void cadastrarComIdsRepetidosCriaUmUnicoVinculo() {
		Produto novo = produto(null, "SKU-100", 10, null);
		when(produtoRepository.existsBySku("SKU-100")).thenReturn(false);
		when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria(1, "Ferragens")));
		when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

		Produto salvo = produtoService.cadastrar(novo, List.of(1, 1, 1));

		assertEquals(1, salvo.getCategorias().size(), "RN-12: ids repetidos são ignorados");
		verify(categoriaRepository, times(1)).findById(1);
	}

	// ----------------------------------------------------------- buscarPorId

	@Test
	void buscarPorIdCaminhoFeliz() {
		Produto existente = produto(5, "SKU-5", 7, true);
		when(produtoRepository.findById(5)).thenReturn(Optional.of(existente));

		assertEquals(existente, produtoService.buscarPorId(5));
	}

	@Test
	void buscarPorIdInexistenteRetorna404() {
		when(produtoRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(ProdutoNaoEncontradoException.class, () -> produtoService.buscarPorId(99));
	}

	// ----------------------------------------------------------- listar

	@Test
	void listarSemIncluirInativosUsaSomenteAtivos() {
		Produto ativo = produto(1, "SKU-1", 1, true);
		when(produtoRepository.findByAtivoTrue()).thenReturn(List.of(ativo));

		List<Produto> resultado = produtoService.listar(false);

		assertEquals(1, resultado.size());
		verify(produtoRepository, never()).findAll();
	}

	@Test
	void listarIncluindoInativosRetornaTodos() {
		Produto ativo = produto(1, "SKU-1", 1, true);
		Produto inativo = produto(2, "SKU-2", 0, false);
		when(produtoRepository.findAll()).thenReturn(List.of(ativo, inativo));

		List<Produto> resultado = produtoService.listar(true);

		assertEquals(2, resultado.size());
		verify(produtoRepository, never()).findByAtivoTrue();
	}

	// ----------------------------------------------------------- atualizar

	@Test
	void atualizarCaminhoFelizPreservandoQuantidadeEAtivo() {
		Produto existente = produto(5, "SKU-ANTIGO", 50, true);
		Produto dados = produto(null, "SKU-NOVO", 999, null);
		dados.setPreco(new BigDecimal("49.90"));

		when(produtoRepository.findById(5)).thenReturn(Optional.of(existente));
		when(produtoRepository.existsBySkuAndIdNot("SKU-NOVO", 5)).thenReturn(false);
		when(categoriaRepository.findById(2)).thenReturn(Optional.of(categoria(2, "Fixacao")));
		when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

		Produto atualizado = produtoService.atualizar(5, dados, List.of(2));

		assertEquals("SKU-NOVO", atualizado.getSku());
		assertEquals("Produto SKU-NOVO", atualizado.getNome());
		assertEquals(new BigDecimal("49.90"), atualizado.getPreco());
		assertEquals(50, atualizado.getQuantidade(), "quantidade preservada no PUT");
		assertEquals(true, atualizado.getAtivo(), "estado ativo preservado no PUT");
		assertEquals(1, atualizado.getCategorias().size());
		assertEquals(2, atualizado.getCategorias().get(0).getCategoria().getId());
	}

	@Test
	void atualizarRemoveVinculosForaDaLista() {
		Produto existente = produto(5, "SKU-5", 50, true);
		Categoria ferragens = categoria(1, "Ferragens");
		existente.getCategorias().add(new ProdutoCategoria(existente, ferragens));
		Produto dados = produto(null, "SKU-5", 50, null);

		when(produtoRepository.findById(5)).thenReturn(Optional.of(existente));
		when(produtoRepository.existsBySkuAndIdNot("SKU-5", 5)).thenReturn(false);
		when(categoriaRepository.findById(2)).thenReturn(Optional.of(categoria(2, "Fixacao")));
		when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

		Produto atualizado = produtoService.atualizar(5, dados, List.of(2));

		assertEquals(1, atualizado.getCategorias().size(), "RN-11: vínculo fora da lista sai");
		assertEquals(2, atualizado.getCategorias().get(0).getCategoria().getId());
	}

	@Test
	void atualizarComSkuDeOutroProdutoRetorna409() {
		Produto existente = produto(5, "SKU-5", 50, true);
		Produto dados = produto(null, "SKU-6", 1, null);

		when(produtoRepository.findById(5)).thenReturn(Optional.of(existente));
		when(produtoRepository.existsBySkuAndIdNot("SKU-6", 5)).thenReturn(true);

		assertThrows(ProdutoJaExisteException.class,
				() -> produtoService.atualizar(5, dados, List.of(1)));

		verify(produtoRepository, never()).save(any(Produto.class));
	}

	@Test
	void atualizarProdutoInexistenteRetorna404() {
		when(produtoRepository.findById(99)).thenReturn(Optional.empty());

		Produto dados = produto(null, "SKU-X", 1, null);
		assertThrows(ProdutoNaoEncontradoException.class,
				() -> produtoService.atualizar(99, dados, List.of(1)));
	}

	// ----------------------------------------------------------- ajustarQuantidade

	@Test
	void ajustarQuantidadeCaminhoFeliz() {
		Produto existente = produto(5, "SKU-5", 10, true);
		when(produtoRepository.findById(5)).thenReturn(Optional.of(existente));
		when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

		Produto ajustado = produtoService.ajustarQuantidade(5, 30);

		assertEquals(30, ajustado.getQuantidade());
		assertEquals("SKU-5", ajustado.getSku(), "demais campos inalterados");
		assertEquals(true, ajustado.getAtivo());
	}

	@Test
	void ajustarQuantidadeDeProdutoInexistenteRetorna404() {
		when(produtoRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(ProdutoNaoEncontradoException.class,
				() -> produtoService.ajustarQuantidade(99, 30));
	}

	// ----------------------------------------------------------- inativar

	@Test
	void inativarCaminhoFeliz() {
		Produto existente = produto(5, "SKU-5", 10, true);
		when(produtoRepository.findById(5)).thenReturn(Optional.of(existente));
		when(produtoRepository.save(any(Produto.class))).thenAnswer(inv -> inv.getArgument(0));

		produtoService.inativar(5);

		assertFalse(existente.getAtivo(), "RN-03: exclusão lógica altera ativo para false");
		verify(produtoRepository).save(existente);
	}

	@Test
	void inativarProdutoJaInativoRetorna422() {
		Produto inativo = produto(5, "SKU-5", 10, false);
		when(produtoRepository.findById(5)).thenReturn(Optional.of(inativo));

		assertThrows(ProdutoJaInativoException.class, () -> produtoService.inativar(5));

		verify(produtoRepository, never()).save(any(Produto.class));
	}

	@Test
	void inativarProdutoInexistenteRetorna404() {
		when(produtoRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(ProdutoNaoEncontradoException.class, () -> produtoService.inativar(99));
	}

}
