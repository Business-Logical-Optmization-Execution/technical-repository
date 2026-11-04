package com.portalblox.repository;

import com.portalblox.entity.MovimentacaoEstoque;
import com.portalblox.entity.TipoMovimentacao;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Integer> {

	@EntityGraph(attributePaths = {"produto", "usuario"})
	@Query("""
			select m from MovimentacaoEstoque m
			where (:produtoId is null or m.produto.id = :produtoId)
			  and (:usuarioId is null or m.usuario.id = :usuarioId)
			  and (:tipo is null or m.tipo = :tipo)
			  and (:de is null or m.criadoEm >= :de)
			  and (:ateExclusivo is null or m.criadoEm < :ateExclusivo)
			order by m.criadoEm desc, m.id desc
			""")
	List<MovimentacaoEstoque> buscarComFiltros(
			Integer produtoId,
			Integer usuarioId,
			TipoMovimentacao tipo,
			LocalDateTime de,
			LocalDateTime ateExclusivo,
			Pageable pageable);

}
