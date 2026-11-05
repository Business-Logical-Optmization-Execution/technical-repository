package com.portalblox.service;

import com.portalblox.entity.Configuracao;
import com.portalblox.repository.ConfiguracaoRepository;
import com.portalblox.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracaoService {

	private static final int ID_UNICO = 1;
	private static final int ESTOQUE_MINIMO_GLOBAL_PADRAO = 5;
	private static final boolean EXIBIR_FOTOS_PADRAO = true;

	private final ConfiguracaoRepository configuracaoRepository;
	private final ProdutoRepository produtoRepository;

	public ConfiguracaoService(ConfiguracaoRepository configuracaoRepository,
			ProdutoRepository produtoRepository) {
		this.configuracaoRepository = configuracaoRepository;
		this.produtoRepository = produtoRepository;
	}

	/**
	 * Le a linha unica sem cria-la. Sem linha persistida, devolve os padroes.
	 */
	public Configuracao obterConfiguracao() {
		return configuracaoRepository.findById(ID_UNICO)
				.orElseGet(() -> new Configuracao(ID_UNICO, ESTOQUE_MINIMO_GLOBAL_PADRAO, EXIBIR_FOTOS_PADRAO));
	}

	public Configuracao atualizar(Integer estoqueMinimoGlobal, Boolean exibirFotos) {
		return configuracaoRepository.save(new Configuracao(ID_UNICO, estoqueMinimoGlobal, exibirFotos));
	}

	public int obterEstoqueMinimoGlobal() {
		return obterConfiguracao().getEstoqueMinimoGlobal();
	}

	public long contarProdutosComMinimoProprio() {
		return produtoRepository.countByAtivoTrueAndEstoqueMinimoIsNotNull();
	}

}
