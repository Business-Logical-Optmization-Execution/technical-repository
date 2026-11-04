package com.portalblox.service;

import com.portalblox.dto.DashboardResumoResponseDto;
import com.portalblox.dto.DashboardResumoResponseDto.EntradasSaidasDia;
import com.portalblox.dto.DashboardResumoResponseDto.ProdutoMovimentado;
import com.portalblox.dto.DashboardResumoResponseDto.ProdutoResumo;
import com.portalblox.repository.MovimentacaoEstoqueRepository;
import com.portalblox.repository.MovimentacaoEstoqueRepository.VariacaoNoTempo;
import com.portalblox.repository.ProdutoRepository;
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

	public DashboardResumoResponseDto obterResumo() {
		LocalDate hoje = LocalDate.now();
		int estoqueMinimoGlobal = configuracaoService.obterEstoqueMinimoGlobal();
		long movimentacoesHoje = movimentacaoRepository.contarNoPeriodo(
				hoje.atStartOfDay(), hoje.plusDays(1).atStartOfDay());
		return new DashboardResumoResponseDto(
				produtoRepository.countByAtivoTrue(),
				produtoRepository.contarBaixoEstoque(estoqueMinimoGlobal),
				produtoRepository.contarZerados(),
				movimentacoesHoje,
				montarSerie(hoje),
				buscarMaisMovimentados());
	}

	private List<EntradasSaidasDia> montarSerie(LocalDate hoje) {
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

		List<EntradasSaidasDia> serie = new ArrayList<>();
		for (LocalDate dia = primeiroDia; !dia.isAfter(hoje); dia = dia.plusDays(1)) {
			serie.add(new EntradasSaidasDia(dia, entradas.getOrDefault(dia, 0L), saidas.getOrDefault(dia, 0L)));
		}
		return serie;
	}

	private List<ProdutoMovimentado> buscarMaisMovimentados() {
		return movimentacaoRepository.buscarMaisMovimentados(PageRequest.of(0, LIMITE_MAIS_MOVIMENTADOS)).stream()
				.map(item -> new ProdutoMovimentado(
						new ProdutoResumo(item.getId(), item.getSku(), item.getNome()),
						item.getTotal()))
				.toList();
	}

}
