package com.portalblox.service;

import com.portalblox.entity.MotivoMovimentacao;
import com.portalblox.entity.MovimentacaoEstoque;
import com.portalblox.entity.OrigemMovimentacao;
import com.portalblox.entity.Produto;
import com.portalblox.entity.TipoMovimentacao;
import com.portalblox.entity.Usuario;
import com.portalblox.exception.DadosInvalidosException;
import com.portalblox.repository.MovimentacaoEstoqueRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class MovimentacaoEstoqueService {

	private final MovimentacaoEstoqueRepository movimentacaoRepository;

	public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository movimentacaoRepository) {
		this.movimentacaoRepository = movimentacaoRepository;
	}

	public MovimentacaoEstoque registrar(Produto produto, Usuario usuario, TipoMovimentacao tipo,
			Integer quantidade, Integer variacao, Integer quantidadeAntes, Integer quantidadeDepois,
			MotivoMovimentacao motivo, String observacao, String documento,
			OrigemMovimentacao origem) {
		MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(produto, usuario, tipo,
				quantidade, variacao, quantidadeAntes, quantidadeDepois,
				motivo, observacao, documento, origem);
		try {
			return movimentacaoRepository.saveAndFlush(movimentacao);
		} catch (DataIntegrityViolationException excecao) {
			throw new DadosInvalidosException("Violação de integridade dos dados.");
		}
	}

}
