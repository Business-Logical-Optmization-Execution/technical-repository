package com.portalblox.repository;

import com.portalblox.entity.Usuario;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PerfilAcessoRepository perfilAcessoRepository;

	private Usuario novoUsuario(String nome, String email, boolean ativo) {
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setSenha("senha-de-teste");
		usuario.setPerfilAcesso(perfilAcessoRepository.findById(1).orElseThrow());
		usuario.setAtivo(ativo);
		return usuarioRepository.save(usuario);
	}

	@Test
	void existsByEmailEncontraEmailCadastrado() {
		novoUsuario("Maria", "maria@portalblox.com", true);

		assertTrue(usuarioRepository.existsByEmail("maria@portalblox.com"));
		assertFalse(usuarioRepository.existsByEmail("nao.existe@portalblox.com"));
	}

	@Test
	void existsByEmailAndIdNotIgnoraOProprioRegistro() {
		Usuario primeiro = novoUsuario("Ana", "ana@portalblox.com", true);
		Usuario segundo = novoUsuario("Bruno", "bruno@portalblox.com", true);

		assertTrue(usuarioRepository.existsByEmailAndIdNot("bruno@portalblox.com", primeiro.getId()));
		assertFalse(usuarioRepository.existsByEmailAndIdNot("bruno@portalblox.com", segundo.getId()));
		assertFalse(usuarioRepository.existsByEmailAndIdNot("outro@portalblox.com", primeiro.getId()));
	}

	@Test
	void findByAtivoTrueRetornaSomenteAtivos() {
		Usuario ativo = novoUsuario("Carla", "carla@portalblox.com", true);
		Usuario inativo = novoUsuario("Diego", "diego@portalblox.com", false);

		List<Usuario> ativos = usuarioRepository.findByAtivoTrue();
		assertTrue(ativos.stream().anyMatch(u -> u.getId().equals(ativo.getId())));
		assertTrue(ativos.stream().noneMatch(u -> u.getId().equals(inativo.getId())));
	}

	@Test
	void persistenciaPreencheCriadoEmEAtualizadoEm() {
		Usuario salvo = novoUsuario("Elena", "elena@portalblox.com", true);

		assertNotNull(salvo.getCriadoEm(), "criadoEm deve ser preenchido pelo @PrePersist");
		assertNotNull(salvo.getAtualizadoEm(), "atualizadoEm deve ser preenchido pelo @PrePersist");
	}

}
