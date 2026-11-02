package com.portalblox.repository;

import com.portalblox.entity.Fornecedor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {

	List<Fornecedor> findByAtivoTrueOrderByNome();

	List<Fornecedor> findAllByOrderByNome();

	boolean existsByCnpj(String cnpj);

	boolean existsByCnpjAndIdNot(String cnpj, Integer id);

}
