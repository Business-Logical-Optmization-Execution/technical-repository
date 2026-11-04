package com.portalblox.controller.docs;

import com.portalblox.dto.DashboardResumoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Dashboard", description = "Indicadores de estoque e movimentações já calculados")
public interface DashboardApi {

	@Operation(summary = "Consultar resumo do dashboard",
			description = "Devolve todos os indicadores em uma única chamada. Considera só produtos ativos nas "
					+ "contagens: baixo estoque é 0 < quantidade <= mínimo efetivo, e zerado é quantidade <= 0. "
					+ "A série tem sempre 7 dias, do mais antigo até hoje, com entradas e saídas positivas, "
					+ "calculadas pelo sinal da variação de qualquer tipo de movimentação. Mais movimentados "
					+ "soma o valor absoluto das variações de todo o histórico, com até 5 produtos.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Resumo calculado",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "resumo",
									value = """
										{
										  "totalProdutosAtivos": 11,
										  "produtosBaixoEstoque": 6,
										  "produtosZerados": 2,
										  "movimentacoesHoje": 0,
										  "entradasSaidas7Dias": [
										    { "data": "2026-10-29", "entradas": 0, "saidas": 1 },
										    { "data": "2026-10-30", "entradas": 2, "saidas": 3 },
										    { "data": "2026-10-31", "entradas": 0, "saidas": 0 },
										    { "data": "2026-11-01", "entradas": 0, "saidas": 0 },
										    { "data": "2026-11-02", "entradas": 0, "saidas": 0 },
										    { "data": "2026-11-03", "entradas": 0, "saidas": 0 },
										    { "data": "2026-11-04", "entradas": 0, "saidas": 0 }
										  ],
										  "maisMovimentados": [
										    { "produto": { "id": 7, "sku": "1020", "nome": "Parafuso sextavado 6 mm" }, "totalMovimentado": 10 },
										    { "produto": { "id": 10, "sku": "2002", "nome": "Tomada 2P+T 10 A" }, "totalMovimentado": 3 }
										  ]
										}
										""")))
	})
	ResponseEntity<DashboardResumoResponseDto> obterResumo();

}
