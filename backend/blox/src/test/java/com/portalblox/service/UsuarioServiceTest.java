package com.portalblox.service;

import com.portalblox.entity.PerfilAcesso;
import com.portalblox.entity.Usuario;
import com.portalblox.exception.PerfilAcessoNaoEncontradoException;
import com.portalblox.exception.UsuarioJaExisteException;
import com.portalblox.exception.UsuarioJaInativoException;
import com.portalblox.exception.UsuarioNaoEncontradoException;
import com.portalblox.repository.PerfilAcessoRepository;
import com.portalblox.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private PerfilAcessoRepository perfilAcessoRepository;

	@InjectMocks
	private UsuarioService usuarioService;

	private PerfilAcesso perfil(Integer id, String nome) {
		PerfilAcesso perfil = new PerfilAcesso();
		perfil.setId(id);
		perfil.setNome(nome);
		return perfil;
	}

	private Usuario usuario(Integer id, String email, Boolean ativo) {
		Usuario usuario = new Usuario();
		usuario.setId(id);
		usuario.setNome("Usuario " + email);
		usuario.setEmail(email);
		usuario.setSenha("senha-antiga");
		usuario.setAtivo(ativo);
		return usuario;
	}

	private Usuario dadosNovos(String nome, String email, String senha) {
		Usuario dados = new Usuario();
		dados.setNome(nome);
		dados.setEmail(email);
		dados.setSenha(senha);
		return dados;
	}

	// ----------------------------------------------------------- cadastrar

	@Test
	void cadastrarCaminhoFeliz() {
		Usuario novo = dadosNovos("Maria", "maria@portalblox.com", "nova-senha");
		when(usuarioRepository.existsByEmail("maria@portalblox.com")).thenReturn(false);
		when(perfilAcessoRepository.findById(1)).thenReturn(Optional.of(perfil(1, "Operador")));
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

		Usuario salvo = usuarioService.cadastrar(novo, 1);

		assertEquals(true, salvo.getAtivo(), "novo usuário nasce ativo");
		assertEquals(1, salvo.getPerfilAcesso().getId(), "RN-08: perfil associado");
		assertEquals("Operador", salvo.getPerfilAcesso().getNome());
		assertEquals("nova-senha", salvo.getSenha());
	}

	@Test
	void cadastrarComEmailDuplicadoRetorna409() {
		Usuario novo = dadosNovos("Maria", "maria@portalblox.com", "senha");
		when(usuarioRepository.existsByEmail("maria@portalblox.com")).thenReturn(true);

		assertThrows(UsuarioJaExisteException.class,
				() -> usuarioService.cadastrar(novo, 1));

		verify(usuarioRepository, never()).save(any(Usuario.class));
	}

	@Test
	void cadastrarComPerfilInexistenteRetorna404() {
		Usuario novo = dadosNovos("Maria", "maria@portalblox.com", "senha");
		when(usuarioRepository.existsByEmail("maria@portalblox.com")).thenReturn(false);
		when(perfilAcessoRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(PerfilAcessoNaoEncontradoException.class,
				() -> usuarioService.cadastrar(novo, 99));

		verify(usuarioRepository, never()).save(any(Usuario.class));
	}

	// ----------------------------------------------------------- buscarPorId

	@Test
	void buscarPorIdCaminhoFeliz() {
		Usuario existente = usuario(5, "ana@portalblox.com", true);
		when(usuarioRepository.findById(5)).thenReturn(Optional.of(existente));

		assertEquals(existente, usuarioService.buscarPorId(5));
	}

	@Test
	void buscarPorIdInexistenteRetorna404() {
		when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.buscarPorId(99));
	}

	// ----------------------------------------------------------- listar

	@Test
	void listarSemIncluirInativosUsaSomenteAtivos() {
		when(usuarioRepository.findByAtivoTrue()).thenReturn(List.of(usuario(1, "a@x.com", true)));

		List<Usuario> resultado = usuarioService.listar(false);

		assertEquals(1, resultado.size());
		verify(usuarioRepository, never()).findAll();
	}

	@Test
	void listarIncluindoInativosRetornaTodos() {
		when(usuarioRepository.findAll())
				.thenReturn(List.of(usuario(1, "a@x.com", true), usuario(2, "b@x.com", false)));

		List<Usuario> resultado = usuarioService.listar(true);

		assertEquals(2, resultado.size());
		verify(usuarioRepository, never()).findByAtivoTrue();
	}

	// ----------------------------------------------------------- atualizar

	@Test
	void atualizarCaminhoFelizPreservandoSenhaEAtivo() {
		Usuario existente = usuario(5, "antigo@portalblox.com", true);
		Usuario dados = dadosNovos("Novo Nome", "novo@portalblox.com", "senha-mudou");

		when(usuarioRepository.findById(5)).thenReturn(Optional.of(existente));
		when(usuarioRepository.existsByEmailAndIdNot("novo@portalblox.com", 5)).thenReturn(false);
		when(perfilAcessoRepository.findById(2)).thenReturn(Optional.of(perfil(2, "Operador")));
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

		Usuario atualizado = usuarioService.atualizar(5, dados, 2);

		assertEquals("Novo Nome", atualizado.getNome());
		assertEquals("novo@portalblox.com", atualizado.getEmail());
		assertEquals(2, atualizado.getPerfilAcesso().getId());
		assertEquals("senha-antiga", atualizado.getSenha(), "senha preservada no PUT");
		assertEquals(true, atualizado.getAtivo(), "estado ativo preservado no PUT");
	}

	@Test
	void atualizarComEmailDeOutroUsuarioRetorna409() {
		Usuario existente = usuario(5, "antigo@portalblox.com", true);
		Usuario dados = dadosNovos("Nome", "maria@portalblox.com", "senha");

		when(usuarioRepository.findById(5)).thenReturn(Optional.of(existente));
		when(usuarioRepository.existsByEmailAndIdNot("maria@portalblox.com", 5)).thenReturn(true);

		assertThrows(UsuarioJaExisteException.class,
				() -> usuarioService.atualizar(5, dados, 1));

		verify(usuarioRepository, never()).save(any(Usuario.class));
	}

	@Test
	void atualizarComPerfilInexistenteRetorna404SemSalvar() {
		Usuario existente = usuario(5, "antigo@portalblox.com", true);
		Usuario dados = dadosNovos("Nome", "novo@portalblox.com", "senha");

		when(usuarioRepository.findById(5)).thenReturn(Optional.of(existente));
		when(usuarioRepository.existsByEmailAndIdNot("novo@portalblox.com", 5)).thenReturn(false);
		when(perfilAcessoRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(PerfilAcessoNaoEncontradoException.class,
				() -> usuarioService.atualizar(5, dados, 99));

		verify(usuarioRepository, never()).save(any(Usuario.class));
	}

	@Test
	void atualizarUsuarioInexistenteRetorna404() {
		when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

		Usuario dados = dadosNovos("Nome", "novo@portalblox.com", "senha");
		assertThrows(UsuarioNaoEncontradoException.class,
				() -> usuarioService.atualizar(99, dados, 1));
	}

	// ----------------------------------------------------------- inativar

	@Test
	void inativarCaminhoFeliz() {
		Usuario existente = usuario(5, "ana@portalblox.com", true);
		when(usuarioRepository.findById(5)).thenReturn(Optional.of(existente));
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

		usuarioService.inativar(5);

		assertFalse(existente.getAtivo(), "RN-03: exclusão lógica altera ativo para false");
		verify(usuarioRepository).save(existente);
	}

	@Test
	void inativarUsuarioJaInativoRetorna422() {
		Usuario inativo = usuario(5, "ana@portalblox.com", false);
		when(usuarioRepository.findById(5)).thenReturn(Optional.of(inativo));

		assertThrows(UsuarioJaInativoException.class, () -> usuarioService.inativar(5));

		verify(usuarioRepository, never()).save(any(Usuario.class));
	}

	@Test
	void inativarUsuarioInexistenteRetorna404() {
		when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.inativar(99));
	}

}
