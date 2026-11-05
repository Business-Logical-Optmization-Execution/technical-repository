package com.portalblox.repository;

import com.portalblox.entity.Produto;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	boolean existsBySku(String sku);

	boolean existsBySkuAndIdNot(String sku, Integer id);

	long countByAtivoTrueAndEstoqueMinimoIsNotNull();

	long countByAtivoTrue();

	@Query("select count(p) from Produto p where p.ativo = true and p.quantidade <= 0")
	long contarZerados();

	@Query("""
			select count(p) from Produto p
			where p.ativo = true
			  and p.quantidade > 0
			  and p.quantidade <= coalesce(p.estoqueMinimo, :estoqueMinimoGlobal)
			""")
	long contarBaixoEstoque(int estoqueMinimoGlobal);

	@EntityGraph(attributePaths = {"categoria", "fornecedor", "localizacoes"})
	@Query("select distinct p from Produto p order by p.id")
	List<Produto> findAllCompletos();

	@EntityGraph(attributePaths = {"categoria", "fornecedor", "localizacoes"})
	@Query("select distinct p from Produto p where p.ativo = true order by p.id")
	List<Produto> findAtivosCompletos();

	@EntityGraph(attributePaths = {"categoria", "fornecedor", "localizacoes"})
	@Query("select distinct p from Produto p where p.id = :id")
	Optional<Produto> findByIdCompleto(Integer id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from Produto p where p.id = :id")
	Optional<Produto> findByIdParaAtualizacao(Integer id);

}
