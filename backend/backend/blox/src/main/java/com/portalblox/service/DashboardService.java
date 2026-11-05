package com.portalblox.service;

import com.portalblox.entity.Produto;
import com.portalblox.repository.MovimentacaoEstoqueRepository;
import com.portalblox.repository.MovimentacaoEstoqueRepository.VariacaoNoTempo;
import com.portalblox.repository.ProdutoRepository;
import com.portalblox.repository.ProdutoRepository.EstoqueDeProduto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

	private static final int DIAS_DA_SERIE = 7;
	private static final int LIMITE_MAIS_MOVIMENTADOS = 5;

	private final ProdutoRepository produtoRepository;
	private final MovimentacaoEstoqueRepository movimentacaoRepository;
	private final ConfiguracaoService configuracaoService;

	public DashboardService(ProdutoRepository produtoRepository,
			MovimentacaoEstoqueRepository movimentacaoRepository,
			ConfiguracaoService configuracaoService) {
		this.produtoRepository = produtoRepository;
		this.movimentacaoRepository = movimentacaoRepository;
		this.configuracaoService = configuracaoService;
	}

	public DashboardResumo obterResumo() {
		LocalDate hoje = LocalDate.now();
		int estoqueMinimoGlobal = configuracaoService.obterEstoqueMinimoGlobal();
		List<EstoqueDeProduto> estoques = produtoRepository.buscarEstoqueDeAtivos();

		long baixoEstoque = 0;
		long zerados = 0;
		for (EstoqueDeProduto estoque : estoques) {
			int minimoEfetivo = Produto.calcularEstoqueMinimoEfetivo(estoque.getEstoqueMinimo(), estoqueMinimoGlobal);
			if (estoque.getQuantidade() <= 0) {
				zerados++;
			} else if (estoque.getQuantidade() <= minimoEfetivo) {
				baixoEstoque++;
			}
		}

		long movimentacoesHoje = movimentacaoRepository.contarNoPeriodo(
				hoje.atStartOfDay(), hoje.plusDays(1).atStartOfDay());
		return new DashboardResumo(estoques.size(), baixoEstoque, zerados, movimentacoesHoje,
				montarSerie(hoje), buscarMaisMovimentados());
	}

	private List<DashboardResumo.EntradasSaidasDia> montarSerie(LocalDate hoje) {
		LocalDate primeiroDia = hoje.minusDays(DIAS_DA_SERIE - 1L);
		Map<LocalDate, Long> entradas = new HashMap<>();
		Map<LocalDate, Long> saidas = new HashMap<>();
		List<VariacaoNoTempo> variacoes = movimentacaoRepository.buscarVariacoesNoPeriodo(
				primeiroDia.atStartOfDay(), hoje.plusDays(1).atStartOfDay());
		for (VariacaoNoTempo item : variacoes) {
			LocalDate dia = item.getCriadoEm().toLocalDate();
			int variacao = item.getVariacao();
			if (variacao > 0) {
				entradas.merge(dia, (long) variacao, Long::sum);
			} else if (variacao < 0) {
				saidas.merge(dia, (long) -variacao, Long::sum);
			}
		}

		List<DashboardResumo.EntradasSaidasDia> serie = new ArrayList<>();
		for (LocalDate dia = primeiroDia; !dia.isAfter(hoje); dia = dia.plusDays(1)) {
			serie.add(new DashboardResumo.EntradasSaidasDia(dia,
					entradas.getOrDefault(dia, 0L), saidas.getOrDefault(dia, 0L)));
		}
		return serie;
	}

	private List<DashboardResumo.ProdutoMovimentado> buscarMaisMovimentados() {
		return movimentacaoRepository.buscarMaisMovimentados(PageRequest.of(0, LIMITE_MAIS_MOVIMENTADOS)).stream()
				.map(item -> new DashboardResumo.ProdutoMovimentado(
						item.getId(), item.getSku(), item.getNome(), item.getTotal()))
				.toList();
	}

}
