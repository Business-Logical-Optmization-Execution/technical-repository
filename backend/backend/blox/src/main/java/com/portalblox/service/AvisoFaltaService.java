package com.portalblox.service;

import com.portalblox.entity.AvisoFalta;
import com.portalblox.entity.Produto;
import com.portalblox.entity.StatusAviso;
import com.portalblox.entity.Usuario;
import com.portalblox.exception.AvisoFaltaNaoEncontradoException;
import com.portalblox.exception.ProdutoInativoException;
import com.portalblox.exception.ProdutoNaoEncontradoException;
import com.portalblox.exception.UsuarioInativoException;
import com.portalblox.exception.UsuarioNaoEncontradoException;
import com.portalblox.repository.AvisoFaltaRepository;
import com.portalblox.repository.ProdutoRepository;
import com.portalblox.repository.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvisoFaltaService {

	private final AvisoFaltaRepository avisoFaltaRepository;
	private final ProdutoRepository produtoRepository;
	private final UsuarioRepository usuarioRepository;

	public AvisoFaltaService(AvisoFaltaRepository avisoFaltaRepository,
			ProdutoRepository produtoRepository,
			UsuarioRepository usuarioRepository) {
		this.avisoFaltaRepository = avisoFaltaRepository;
		this.produtoRepository = produtoRepository;
		this.usuarioRepository = usuarioRepository;
	}

	/**
	 * Cria o aviso ou soma ao aberto do produto. O bloqueio do produto precisa ser a
	 * primeira consulta: ele serializa os avisos e garante um unico aberto por produto.
	 */
	@Transactional
	public AvisoFalta registrar(Integer produtoId, Integer usuarioId, String observacao) {
		Produto produto = produtoRepository.findByIdParaAtualizacao(produtoId)
				.orElseThrow(ProdutoNaoEncontradoException::new);
		if (!Boolean.TRUE.equals(produto.getAtivo())) {
			throw new ProdutoInativoException("Produto inativo não aceita aviso de falta.");
		}

		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(UsuarioNaoEncontradoException::new);
		if (!Boolean.TRUE.equals(usuario.getAtivo())) {
			throw new UsuarioInativoException("Usuário inativo não pode registrar aviso de falta.");
		}

		AvisoFalta aviso = avisoFaltaRepository.findByProdutoIdAndStatus(produtoId, StatusAviso.ABERTO)
				.orElse(null);
		if (aviso == null) {
			return avisoFaltaRepository.save(new AvisoFalta(produto, usuario, textoOuNulo(observacao)));
		}
		aviso.somarAviso();
		return avisoFaltaRepository.save(aviso);
	}

	public List<AvisoFalta> listar(StatusAviso status) {
		return avisoFaltaRepository.buscarPorStatus(status);
	}

	public AvisoFalta resolver(Integer id) {
		AvisoFalta aviso = avisoFaltaRepository.findById(id)
				.orElseThrow(AvisoFaltaNaoEncontradoException::new);
		aviso.resolver();
		return avisoFaltaRepository.save(aviso);
	}

	private String textoOuNulo(String valor) {
		if (valor == null) {
			return null;
		}
		String normalizado = valor.trim();
		return normalizado.isEmpty() ? null : normalizado;
	}

}
