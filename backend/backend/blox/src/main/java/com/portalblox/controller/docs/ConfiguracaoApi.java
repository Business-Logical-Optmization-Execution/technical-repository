package com.portalblox.controller.docs;

import com.portalblox.dto.ConfiguracaoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Configurações", description = "Parâmetros globais da loja")
public interface ConfiguracaoApi {

	@Operation(summary = "Consultar configuração da loja",
			description = "Devolve o estoque mínimo global, a preferência de exibição de fotos e "
					+ "a quantidade de produtos ativos com mínimo próprio. Quando não há configuração "
					+ "persistida, devolve os padrões 5 e true.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Configuração retornada",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "configuracaoPadrao",
									value = """
										{
										  "estoqueMinimoGlobal": 5,
										  "exibirFotos": true,
										  "produtosComMinimoProprio": 4
										}
										""")))
	})
	ResponseEntity<ConfiguracaoResponseDto> obter();

}
