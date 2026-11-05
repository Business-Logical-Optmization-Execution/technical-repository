package com.portalblox.repository;

import com.portalblox.entity.AvisoFalta;
import com.portalblox.entity.StatusAviso;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AvisoFaltaRepository extends JpaRepository<AvisoFalta, Integer> {

	Optional<AvisoFalta> findByProdutoIdAndStatus(Integer produtoId, StatusAviso status);

	@EntityGraph(attributePaths = {"produto", "usuario"})
	@Query("""
			select a from AvisoFalta a
			where (:status is null or a.status = :status)
			order by a.criadoEm desc, a.id desc
			""")
	List<AvisoFalta> buscarPorStatus(StatusAviso status);

}
