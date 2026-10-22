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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final PerfilAcessoRepository perfilAcessoRepository;

	public UsuarioService(UsuarioRepository usuarioRepository,
			PerfilAcessoRepository perfilAcessoRepository) {
		this.usuarioRepository = usuarioRepository;
		this.perfilAcessoRepository = perfilAcessoRepository;
	}

	public List<Usuario> listar(boolean incluirInativos) {
		if (incluirInativos) {
			return usuarioRepository.findAll();
		}
		return usuarioRepository.findByAtivoTrue();                    // RN-04
	}

	public Usuario buscarPorId(Integer id) {
		return usuarioRepository.findById(id)
				.orElseThrow(UsuarioNaoEncontradoException::new);
	}

	@Transactional
	public Usuario cadastrar(Usuario usuario, Integer perfilAcessoId) {
		if (usuarioRepository.existsByEmail(usuario.getEmail())) {    // RN-02
			throw new UsuarioJaExisteException();
		}
		usuario.setPerfilAcesso(buscarPerfil(perfilAcessoId));        // RN-08
		usuario.setAtivo(true);
		return usuarioRepository.save(usuario);
	}

	@Transactional
	public Usuario atualizar(Integer id, Usuario dados, Integer perfilAcessoId) {
		Usuario existente = buscarPorId(id);
		if (usuarioRepository.existsByEmailAndIdNot(dados.getEmail(), id)) {
			throw new UsuarioJaExisteException();                      // RN-02
		}
		existente.setNome(dados.getNome());                           // só campos editáveis;
		existente.setEmail(dados.getEmail());                          // senha e ativo preservados
		existente.setPerfilAcesso(buscarPerfil(perfilAcessoId));       // RN-08
		return usuarioRepository.save(existente);
	}

	@Transactional
	public void inativar(Integer id) {
		Usuario usuario = buscarPorId(id);
		if (!usuario.getAtivo()) {                                    // RN-05
			throw new UsuarioJaInativoException();
		}
		usuario.setAtivo(false);                                       // RN-03
		usuarioRepository.save(usuario);
	}

	private PerfilAcesso buscarPerfil(Integer id) {
		return perfilAcessoRepository.findById(id)
				.orElseThrow(PerfilAcessoNaoEncontradoException::new);
	}

}
