package com.portalblox.service;

import com.portalblox.entity.Fornecedor;
import com.portalblox.exception.DadosInvalidosException;
import com.portalblox.exception.DetectorChaveDuplicada;
import com.portalblox.exception.FornecedorJaExisteException;
import com.portalblox.exception.FornecedorJaInativoException;
import com.portalblox.exception.FornecedorNaoEncontradoException;
import com.portalblox.repository.FornecedorRepository;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class FornecedorService {

	private static final int TAMANHO_CNPJ = 14;

	private final FornecedorRepository fornecedorRepository;

	public FornecedorService(FornecedorRepository fornecedorRepository) {
		this.fornecedorRepository = fornecedorRepository;
	}

	public List<Fornecedor> listar(boolean incluirInativos) {
		if (incluirInativos) {
			return fornecedorRepository.findAllByOrderByNome();
		}
		return fornecedorRepository.findByAtivoTrueOrderByNome();
	}

	public Fornecedor buscarPorId(Integer id) {
		return fornecedorRepository.findById(id)
				.orElseThrow(FornecedorNaoEncontradoException::new);
	}

	public Fornecedor cadastrar(Fornecedor fornecedor) {
		String cnpj = normalizarCnpj(fornecedor.getCnpj());
		if (fornecedorRepository.existsByCnpj(cnpj)) {
			throw new FornecedorJaExisteException();
		}
		fornecedor.setNome(fornecedor.getNome().trim());
		fornecedor.setCnpj(cnpj);
		fornecedor.setAtivo(true);
		return salvar(fornecedor);
	}

	public Fornecedor atualizar(Integer id, Fornecedor dados) {
		Fornecedor existente = fornecedorRepository.findById(id)
				.orElseThrow(FornecedorNaoEncontradoException::new);
		String cnpj = normalizarCnpj(dados.getCnpj());
		if (fornecedorRepository.existsByCnpjAndIdNot(cnpj, id)) {
			throw new FornecedorJaExisteException();
		}
		existente.setNome(dados.getNome().trim());
		existente.setCnpj(cnpj);
		return salvar(existente);
	}

	public void inativar(Integer id) {
		Fornecedor fornecedor = fornecedorRepository.findById(id)
				.orElseThrow(FornecedorNaoEncontradoException::new);
		if (!fornecedor.getAtivo()) {
			throw new FornecedorJaInativoException();
		}
		fornecedor.setAtivo(false);
		fornecedorRepository.save(fornecedor);
	}

	private Fornecedor salvar(Fornecedor fornecedor) {
		try {
			return fornecedorRepository.saveAndFlush(fornecedor);
		} catch (DataIntegrityViolationException excecao) {
			if (DetectorChaveDuplicada.ehChaveDuplicada(excecao)) {
				throw new FornecedorJaExisteException();
			}
			throw new DadosInvalidosException("Violação de integridade dos dados.");
		}
	}

	private String normalizarCnpj(String cnpj) {
		if (cnpj == null) {
			throw new DadosInvalidosException("CNPJ é obrigatório.");
		}
		String digitos = cnpj.replaceAll("\\D", "");
		if (digitos.length() != TAMANHO_CNPJ) {
			throw new DadosInvalidosException("CNPJ deve conter exatamente 14 dígitos.");
		}
		return digitos;
	}

}
