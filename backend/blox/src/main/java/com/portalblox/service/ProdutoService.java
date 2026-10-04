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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

	private final ProdutoRepository produtoRepository;
	private final CategoriaRepository categoriaRepository;

	public ProdutoService(ProdutoRepository produtoRepository,
			CategoriaRepository categoriaRepository) {
		this.produtoRepository = produtoRepository;
		this.categoriaRepository = categoriaRepository;
	}

	@Transactional
	public Produto cadastrar(Produto produto, List<Integer> categoriaIds) {
		if (produtoRepository.existsBySku(produto.getSku())) {         // RN-01
			throw new ProdutoJaExisteException();
		}
		// saldo inicial veio do request (ver "Quantidade do produto")
		produto.setAtivo(true);
		sincronizarCategorias(produto, categoriaIds);
		return produtoRepository.save(produto);
	}

	public Produto buscarPorId(Integer id) {
		return produtoRepository.findById(id)
				.orElseThrow(ProdutoNaoEncontradoException::new);
	}

	public List<Produto> listar(boolean incluirInativos) {
		if (incluirInativos) {
			return produtoRepository.findAll();
		}
		return produtoRepository.findByAtivoTrue();                    // RN-04
	}

	@Transactional
	public Produto atualizar(Integer id, Produto dados, List<Integer> categoriaIds) {
		Produto existente = buscarPorId(id);
		if (produtoRepository.existsBySkuAndIdNot(dados.getSku(), id)) {
			throw new ProdutoJaExisteException();
		}
		existente.setSku(dados.getSku());                              // só campos editáveis;
		existente.setNome(dados.getNome());                            // a quantidade e o estado
		existente.setPreco(dados.getPreco());                          // ativo são preservados
		sincronizarCategorias(existente, categoriaIds);
		return produtoRepository.save(existente);
	}

	@Transactional
	public Produto ajustarQuantidade(Integer id, Integer novaQuantidade) {
		Produto produto = buscarPorId(id);                             // RN-06: único ponto
		produto.setQuantidade(novaQuantidade);                         // de mutação da quantidade
		return produtoRepository.save(produto);
	}

	@Transactional
	public void inativar(Integer id) {
		Produto produto = buscarPorId(id);
		if (!produto.getAtivo()) {                                    // RN-05
			throw new ProdutoJaInativoException();
		}
		produto.setAtivo(false);                                       // RN-03
		produtoRepository.save(produto);
	}

	private void sincronizarCategorias(Produto produto, List<Integer> categoriaIds) {
		Set<Integer> idsDesejados = new LinkedHashSet<>(categoriaIds); // RN-12

		// valida todos os ids antes de alterar qualquer vínculo (RN-13)
		Map<Integer, Categoria> categorias = new LinkedHashMap<>();
		for (Integer categoriaId : idsDesejados) {
			categorias.put(categoriaId, categoriaRepository.findById(categoriaId)
					.orElseThrow(CategoriaNaoEncontradaException::new));
		}

		// remove somente os vínculos que saíram da lista (RN-11)
		produto.getCategorias().removeIf(
				vinculo -> !idsDesejados.contains(vinculo.getCategoria().getId()));

		// cria somente os vínculos que ainda não existem
		Set<Integer> jaVinculadas = produto.getCategorias().stream()
				.map(vinculo -> vinculo.getCategoria().getId())
				.collect(Collectors.toSet());
		for (Categoria categoria : categorias.values()) {
			if (!jaVinculadas.contains(categoria.getId())) {
				produto.getCategorias().add(new ProdutoCategoria(produto, categoria));
			}
		}
	}

}
