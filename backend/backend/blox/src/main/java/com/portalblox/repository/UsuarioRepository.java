package com.portalblox.repository;

import com.portalblox.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	boolean existsByEmail(String email);

	boolean existsByEmailAndIdNot(String email, Integer id);

	List<Usuario> findByAtivoTrue();

	Optional<Usuario> findByEmail(String email);

}
