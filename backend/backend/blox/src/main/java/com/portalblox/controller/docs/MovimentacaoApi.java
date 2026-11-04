package com.portalblox.controller.docs;

import com.portalblox.dto.MovimentacaoRequestDto;
import com.portalblox.dto.MovimentacaoResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Movimentações", description = "Registro e consulta das movimentações de estoque")
public interface MovimentacaoApi {

	@Operation(summary = "Registrar movimentação",
			description = "Registra ENTRADA (soma), SAIDA (subtrai) ou AJUSTE (substitui o saldo) "
					+ "na mesma transação em que atualiza produtos.quantidade. A resposta traz a "
					+ "variação assinada em `quantidade` e os saldos antes e depois.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Movimentação registrada",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "entradaRegistrada",
									value = """
										{
										  "id": 5,
										  "tipo": "ENTRADA",
										  "quantidade": 5,
										  "quantidadeAntes": 10,
										  "quantidadeDepois": 15,
										  "motivo": null,
										  "observacao": null,
										  "documento": "NF 12345",
										  "origem": "MANUAL",
										  "dataMov": "2026-11-03T10:00:00",
										  "produto": {
										    "id": 7,
										    "sku": "1020",
										    "nome": "Parafuso sextavado 6 mm"
										  },
										  "usuario": {
										    "id": 3,
										    "nome": "Ronaldo"
										  }
										}
										"""))),
			@ApiResponse(responseCode = "400", description = "Payload inválido ou regra de campo violada",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "erroDeCampo",
									value = """
										{
										  "status": 400,
										  "mensagem": "Confira os campos destacados.",
										  "erros": [
										    {
										      "campo": "quantidade",
										      "mensagem": "Use um inteiro maior que zero."
										    }
										  ]
										}
										"""))),
			@ApiResponse(responseCode = "404", description = "Produto ou usuário não encontrado"),
			@ApiResponse(responseCode = "422", description = "Produto/usuário inativo ou saldo insuficiente",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "saldoInsuficiente",
									value = """
										{
										  "status": 422,
										  "mensagem": "Não dá para tirar 5 un.: a quantidade atual é 2 un.",
										  "erros": []
										}
										""")))
	})
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados da movimentação",
			required = true,
			content = @Content(mediaType = "application/json",
					examples = @ExampleObject(name = "entrada",
							value = """
								{
								  "produtoId": 7,
								  "usuarioId": 3,
								  "tipo": "ENTRADA",
								  "quantidade": 5,
								  "motivo": null,
								  "observacao": null,
								  "documento": "NF 12345"
								}
								""")))
	ResponseEntity<MovimentacaoResponseDto> criar(
			MovimentacaoRequestDto dto);

	@Operation(summary = "Listar movimentações",
			description = "Lista da mais recente para a mais antiga. Filtros opcionais: produtoId, "
					+ "usuarioId, tipo (SALDO_INICIAL, ENTRADA, SAIDA ou AJUSTE), de/ate (datas "
					+ "inclusivas no formato AAAA-MM-DD) e limite. Parâmetros inválidos respondem 400.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista de movimentações",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "saidaComSinal",
									value = """
										[
										  {
										    "id": 2,
										    "tipo": "SAIDA",
										    "quantidade": -1,
										    "quantidadeAntes": 1,
										    "quantidadeDepois": 0,
										    "motivo": "AVARIA",
										    "observacao": "Lâmina quebrada",
										    "documento": null,
										    "origem": "MANUAL",
										    "dataMov": "2026-10-29T15:40:00",
										    "produto": {
										      "id": 3,
										      "sku": "1003",
										      "nome": "Serrote"
										    },
										    "usuario": {
										      "id": 4,
										      "nome": "Valdir"
										    }
										  }
										]
										"""))),
			@ApiResponse(responseCode = "400", description = "Parâmetro de consulta inválido",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "filtroInvalido",
									value = """
										{
										  "status": 400,
										  "mensagem": "Confira os filtros informados.",
										  "erros": [
										    {
										      "campo": "limite",
										      "mensagem": "Use um inteiro maior que zero."
										    }
										  ]
										}
										""")))
	})
	ResponseEntity<List<MovimentacaoResponseDto>> listar(
			@Parameter(description = "Filtra por produto", example = "7")
			String produtoId,
			@Parameter(description = "Filtra por usuário autor", example = "3")
			String usuarioId,
			@Parameter(description = "Filtra por tipo", example = "ENTRADA")
			String tipo,
			@Parameter(description = "Data inicial inclusiva (AAAA-MM-DD)", example = "2026-10-01")
			String de,
			@Parameter(description = "Data final inclusiva (AAAA-MM-DD)", example = "2026-10-31")
			String ate,
			@Parameter(description = "Máximo de itens retornados", example = "10")
			String limite);

}
