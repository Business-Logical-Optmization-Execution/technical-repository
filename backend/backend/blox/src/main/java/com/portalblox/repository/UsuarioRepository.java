package com.portalblox.repository;

import com.portalblox.entity.Usuario;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	boolean existsByEmail(String email);

	boolean existsByEmailAndIdNot(String email, Integer id);

	List<Usuario> findByAtivoTrue();

	Optional<Usuario> findByEmail(String email);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select u from Usuario u where u.ativo = true and u.perfilAcesso.nome = :perfil order by u.id")
	List<Usuario> findAtivosPorPerfilParaAtualizacao(String perfil);

}
