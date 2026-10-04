package com.portalblox.repository;

import com.portalblox.entity.Produto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	boolean existsBySku(String sku);

	boolean existsBySkuAndIdNot(String sku, Integer id);

	List<Produto> findByAtivoTrue();

}
