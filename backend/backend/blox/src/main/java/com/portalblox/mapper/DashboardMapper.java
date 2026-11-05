package com.portalblox.mapper;

import com.portalblox.dto.DashboardResumoResponseDto;
import com.portalblox.service.DashboardResumo;

public class DashboardMapper {

	public static DashboardResumoResponseDto toResponseDto(DashboardResumo resumo) {
		DashboardResumoResponseDto dto = new DashboardResumoResponseDto();
		dto.setTotalProdutosAtivos(resumo.getTotalProdutosAtivos());
		dto.setProdutosBaixoEstoque(resumo.getProdutosBaixoEstoque());
		dto.setProdutosZerados(resumo.getProdutosZerados());
		dto.setMovimentacoesHoje(resumo.getMovimentacoesHoje());
		dto.setEntradasSaidas7Dias(resumo.getEntradasSaidas7Dias().stream()
				.map(DashboardMapper::toResponseDto)
				.toList());
		dto.setMaisMovimentados(resumo.getMaisMovimentados().stream()
				.map(DashboardMapper::toResponseDto)
				.toList());
		return dto;
	}

	private static DashboardResumoResponseDto.EntradasSaidasDia toResponseDto(DashboardResumo.EntradasSaidasDia dia) {
		DashboardResumoResponseDto.EntradasSaidasDia dto = new DashboardResumoResponseDto.EntradasSaidasDia();
		dto.setData(dia.getData());
		dto.setEntradas(dia.getEntradas());
		dto.setSaidas(dia.getSaidas());
		return dto;
	}

	private static DashboardResumoResponseDto.ProdutoMovimentado toResponseDto(DashboardResumo.ProdutoMovimentado item) {
		DashboardResumoResponseDto.ProdutoResumo produto = new DashboardResumoResponseDto.ProdutoResumo();
		produto.setId(item.getId());
		produto.setSku(item.getSku());
		produto.setNome(item.getNome());

		DashboardResumoResponseDto.ProdutoMovimentado dto = new DashboardResumoResponseDto.ProdutoMovimentado();
		dto.setProduto(produto);
		dto.setTotalMovimentado(item.getTotalMovimentado());
		return dto;
	}

}
