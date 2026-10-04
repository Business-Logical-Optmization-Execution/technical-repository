package com.portalblox.controller;

import com.portalblox.entity.PerfilAcesso;
import com.portalblox.entity.Usuario;
import com.portalblox.exception.UsuarioJaExisteException;
import com.portalblox.exception.UsuarioJaInativoException;
import com.portalblox.exception.UsuarioNaoEncontradoException;
import com.portalblox.service.UsuarioService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

	@MockitoBean
	private UsuarioService usuarioService;

	@Autowired
	private MockMvc mockMvc;

	private static final String POST_VALIDO = """
			{"nome":"Maria","email":"maria@portalblox.com","senha":"abc123","perfilAcessoId":1}
			""";

	private static final String POST_INVALIDO = """
			{"nome":"Maria","email":"email-invalido","senha":"","perfilAcessoId":1}
			""";

	private static final String PUT_VALIDO = """
			{"nome":"Maria Silva","email":"maria@portalblox.com","senha":"abc123","perfilAcessoId":1}
			""";

	private static final String PUT_INVALIDO = """
			{"nome":"","email":"maria@portalblox.com","senha":"abc123","perfilAcessoId":1}
			""";

	private Usuario usuarioAtivo() {
		PerfilAcesso perfil = new PerfilAcesso();
		perfil.setId(1);
		perfil.setNome("Administrador");

		Usuario usuario = new Usuario();
		usuario.setId(10);
		usuario.setNome("Maria");
		usuario.setEmail("maria@portalblox.com");
		usuario.setSenha("abc123");
		usuario.setPerfilAcesso(perfil);
		usuario.setAtivo(true);
		return usuario;
	}

	// ------------------------------------------------------------- GET

	@Test
	void listarRetorna200ComSomenteAtivosPorPadrao() throws Exception {
		when(usuarioService.listar(false)).thenReturn(List.of(usuarioAtivo()));

		mockMvc.perform(get("/usuarios"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].email").value("maria@portalblox.com"))
				.andExpect(jsonPath("$[0].senha").doesNotExist());

		verify(usuarioService).listar(false);
	}

	@Test
	void listarComIncluirInativosRetorna200() throws Exception {
		when(usuarioService.listar(true)).thenReturn(List.of(usuarioAtivo()));

		mockMvc.perform(get("/usuarios").param("incluirInativos", "true"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));

		verify(usuarioService).listar(true);
	}

	@Test
	void buscarPorIdRetorna200SemSenha() throws Exception {
		when(usuarioService.buscarPorId(10)).thenReturn(usuarioAtivo());

		mockMvc.perform(get("/usuarios/10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(10))
				.andExpect(jsonPath("$.perfilAcesso.nome").value("Administrador"))
				.andExpect(jsonPath("$.senha").doesNotExist());
	}

	@Test
	void buscarPorIdInexistenteRetorna404() throws Exception {
		when(usuarioService.buscarPorId(99)).thenThrow(new UsuarioNaoEncontradoException());

		mockMvc.perform(get("/usuarios/99"))
				.andExpect(status().isNotFound());
	}

	// ------------------------------------------------------------- POST

	@Test
	void cadastrarRetorna201ComPerfilSemSenha() throws Exception {
		when(usuarioService.cadastrar(any(Usuario.class), eq(1))).thenReturn(usuarioAtivo());

		mockMvc.perform(post("/usuarios")
						.contentType(MediaType.APPLICATION_JSON)
						.content(POST_VALIDO))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(10))
				.andExpect(jsonPath("$.ativo").value(true))
				.andExpect(jsonPath("$.perfilAcesso.nome").value("Administrador"))
				.andExpect(jsonPath("$.senha").doesNotExist());
	}

	@Test
	void cadastrarPayloadInvalidoRetorna400() throws Exception {
		mockMvc.perform(post("/usuarios")
						.contentType(MediaType.APPLICATION_JSON)
						.content(POST_INVALIDO))
				.andExpect(status().isBadRequest());

		verify(usuarioService, never()).cadastrar(any(Usuario.class), any());
	}

	@Test
	void cadastrarEmailDuplicadoRetorna409() throws Exception {
		when(usuarioService.cadastrar(any(Usuario.class), eq(1)))
				.thenThrow(new UsuarioJaExisteException());

		mockMvc.perform(post("/usuarios")
						.contentType(MediaType.APPLICATION_JSON)
						.content(POST_VALIDO))
				.andExpect(status().isConflict());
	}

	// ------------------------------------------------------------- PUT

	@Test
	void atualizarRetorna200SemExporSenha() throws Exception {
		when(usuarioService.atualizar(eq(10), any(Usuario.class), eq(1)))
				.thenReturn(usuarioAtivo());

		mockMvc.perform(put("/usuarios/10")
						.contentType(MediaType.APPLICATION_JSON)
						.content(PUT_VALIDO))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Maria"))
				.andExpect(jsonPath("$.senha").doesNotExist());
	}

	@Test
	void atualizarPayloadInvalidoRetorna400() throws Exception {
		mockMvc.perform(put("/usuarios/10")
						.contentType(MediaType.APPLICATION_JSON)
						.content(PUT_INVALIDO))
				.andExpect(status().isBadRequest());

		verify(usuarioService, never()).atualizar(any(), any(Usuario.class), any());
	}

	@Test
	void atualizarUsuarioInexistenteRetorna404() throws Exception {
		when(usuarioService.atualizar(eq(99), any(Usuario.class), eq(1)))
				.thenThrow(new UsuarioNaoEncontradoException());

		mockMvc.perform(put("/usuarios/99")
						.contentType(MediaType.APPLICATION_JSON)
						.content(PUT_VALIDO))
				.andExpect(status().isNotFound());
	}

	@Test
	void atualizarEmailDeOutroUsuarioRetorna409() throws Exception {
		when(usuarioService.atualizar(eq(10), any(Usuario.class), eq(1)))
				.thenThrow(new UsuarioJaExisteException());

		mockMvc.perform(put("/usuarios/10")
						.contentType(MediaType.APPLICATION_JSON)
						.content(PUT_VALIDO))
				.andExpect(status().isConflict());
	}

	// ------------------------------------------------------------- DELETE

	@Test
	void inativarRetorna204() throws Exception {
		mockMvc.perform(delete("/usuarios/10"))
				.andExpect(status().isNoContent());

		verify(usuarioService).inativar(10);
	}

	@Test
	void inativarUsuarioJaInativoRetorna422() throws Exception {
		org.mockito.Mockito.doThrow(new UsuarioJaInativoException())
				.when(usuarioService).inativar(10);

		mockMvc.perform(delete("/usuarios/10"))
				.andExpect(status().isUnprocessableEntity());
	}

	@Test
	void inativarUsuarioInexistenteRetorna404() throws Exception {
		org.mockito.Mockito.doThrow(new UsuarioNaoEncontradoException())
				.when(usuarioService).inativar(99);

		mockMvc.perform(delete("/usuarios/99"))
				.andExpect(status().isNotFound());
	}

}
