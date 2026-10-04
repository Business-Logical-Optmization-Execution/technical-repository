package com.portalblox.repository;

import com.portalblox.entity.Categoria;
import com.portalblox.entity.Produto;
import com.portalblox.entity.ProdutoCategoria;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProdutoRepositoryTest {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private EntityManager entityManager;

	private Produto novoProduto(String sku, String nome, boolean ativo) {
		Produto produto = new Produto();
		produto.setSku(sku);
		produto.setNome(nome);
		produto.setQuantidade(10);
		produto.setPreco(new BigDecimal("19.90"));
		produto.setAtivo(ativo);
		return produtoRepository.save(produto);
	}

	@Test
	void existsBySkuEncontraSkuCadastrado() {
		novoProduto("SKU-001", "Parafuso M8", true);

		assertTrue(produtoRepository.existsBySku("SKU-001"));
		assertFalse(produtoRepository.existsBySku("SKU-INEXISTENTE"));
	}

	@Test
	void existsBySkuAndIdNotIgnoraOProprioRegistro() {
		Produto primeiro = novoProduto("SKU-002", "Porca M8", true);
		Produto segundo = novoProduto("SKU-003", "Arruela M8", true);

		assertTrue(produtoRepository.existsBySkuAndIdNot("SKU-003", primeiro.getId()));
		assertFalse(produtoRepository.existsBySkuAndIdNot("SKU-003", segundo.getId()));
		assertFalse(produtoRepository.existsBySkuAndIdNot("SKU-999", primeiro.getId()));
	}

	@Test
	void findByAtivoTrueRetornaSomenteAtivos() {
		Produto ativo = novoProduto("SKU-004", "Furadeira", true);
		Produto inativo = novoProduto("SKU-005", "Serra", false);

		List<Produto> ativos = produtoRepository.findByAtivoTrue();
		assertTrue(ativos.stream().anyMatch(p -> p.getId().equals(ativo.getId())));
		assertTrue(ativos.stream().noneMatch(p -> p.getId().equals(inativo.getId())));
	}

	@Test
	void gravaVinculoProdutoCategoria() {
		Produto produto = novoProduto("SKU-006", "Chave Phillips", true);
		Categoria categoria = categoriaRepository.findById(1).orElseThrow();
		produto.getCategorias().add(new ProdutoCategoria(produto, categoria));
		produtoRepository.save(produto);

		entityManager.flush();
		entityManager.clear();

		Produto recarregado = produtoRepository.findById(produto.getId()).orElseThrow();
		assertNotNull(recarregado);
		assertEquals(1, recarregado.getCategorias().size(), "produto deve ter 1 categoria vinculada");
		assertEquals(categoria.getId(), recarregado.getCategorias().get(0).getCategoria().getId());
	}

}
