package com.portalblox.repository;

import com.portalblox.entity.Categoria;
import com.portalblox.entity.PerfilAcesso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PerfilAcessoCategoriaRepositoryTest {

	@Autowired
	private PerfilAcessoRepository perfilAcessoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Test
	void persisteEConsultaPerfilDeAcesso() {
		PerfilAcesso perfil = new PerfilAcesso();
		perfil.setNome("Supervisor");
		perfil.setDescricao("Acesso de supervisao");
		PerfilAcesso salvo = perfilAcessoRepository.save(perfil);
		assertNotNull(salvo.getId());

		PerfilAcesso buscado = perfilAcessoRepository.findById(salvo.getId()).orElseThrow();
		assertEquals("Supervisor", buscado.getNome());
		assertEquals("Acesso de supervisao", buscado.getDescricao());
	}

	@Test
	void persisteEConsultaCategoria() {
		Categoria categoria = new Categoria();
		categoria.setNome("Hidraulica");
		categoria.setDescricao("Tubos e conexoes");
		Categoria salva = categoriaRepository.save(categoria);
		assertNotNull(salva.getId());

		Categoria buscada = categoriaRepository.findById(salva.getId()).orElseThrow();
		assertEquals("Hidraulica", buscada.getNome());
		assertEquals("Tubos e conexoes", buscada.getDescricao());
	}

	@Test
	void cargaInicialDoScriptEstaVisivel() {
		assertTrue(perfilAcessoRepository.count() >= 2, "carga inicial de perfis deve existir");
		assertTrue(categoriaRepository.count() >= 3, "carga inicial de categorias deve existir");
	}

}
