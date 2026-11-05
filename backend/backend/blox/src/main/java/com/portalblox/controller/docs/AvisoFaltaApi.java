package com.portalblox.controller.docs;

import com.portalblox.dto.AvisoFaltaRequestDto;
import com.portalblox.dto.AvisoFaltaResponseDto;
import com.portalblox.entity.StatusAviso;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Avisos de falta", description = "Registro, soma, consulta e resolução de avisos de falta de produto")
public interface AvisoFaltaApi {

	@Operation(summary = "Listar avisos de falta",
			description = "Lista do mais recente para o mais antigo. O filtro status aceita ABERTO ou RESOLVIDO, "
					+ "em maiúsculas; sem filtro, devolve todos.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista de avisos",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "avisoAberto",
									value = """
										[
										  {
										    "id": 1,
										    "status": "ABERTO",
										    "observacao": "acabou na prateleira",
										    "quantidadeAvisos": 1,
										    "somado": false,
										    "criadoEm": "2026-10-31T10:15:00",
										    "produto": { "id": 8, "sku": "1543", "nome": "Fita veda-rosca 18 mm" },
										    "usuario": { "id": 4, "nome": "Valdir" }
										  }
										]
										"""))),
			@ApiResponse(responseCode = "400", description = "Status inválido",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "statusInvalido",
									value = """
										{
										  "status": 400,
										  "mensagem": "Parâmetro inválido.",
										  "erros": [ { "campo": "status", "mensagem": "Informe um valor válido." } ]
										}
										""")))
	})
	ResponseEntity<List<AvisoFaltaResponseDto>> listar(
			@Parameter(description = "Filtra pelo status do aviso", example = "ABERTO")
			StatusAviso status);

	@Operation(summary = "Registrar aviso de falta",
			description = "Cada produto tem no máximo um aviso aberto. Sem aviso aberto, cria o aviso e responde 201. "
					+ "Com aviso aberto, incrementa quantidadeAvisos e responde 200, mantendo o autor, a observação "
					+ "e a data do primeiro aviso.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Aviso criado",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "avisoCriado",
									value = """
										{
										  "id": 2,
										  "status": "ABERTO",
										  "observacao": "Acabou na prateleira",
										  "quantidadeAvisos": 1,
										  "somado": false,
										  "criadoEm": "2026-11-05T09:30:00",
										  "produto": { "id": 1, "sku": "1001", "nome": "Martelo" },
										  "usuario": { "id": 3, "nome": "Ronaldo" }
										}
										"""))),
			@ApiResponse(responseCode = "200", description = "Aviso somado ao aberto",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "avisoSomado",
									value = """
										{
										  "id": 2,
										  "status": "ABERTO",
										  "observacao": "Acabou na prateleira",
										  "quantidadeAvisos": 2,
										  "somado": true,
										  "criadoEm": "2026-11-05T09:30:00",
										  "produto": { "id": 1, "sku": "1001", "nome": "Martelo" },
										  "usuario": { "id": 3, "nome": "Ronaldo" }
										}
										"""))),
			@ApiResponse(responseCode = "400", description = "Campo obrigatório ausente ou observação longa"),
			@ApiResponse(responseCode = "404", description = "Produto ou usuário não encontrado"),
			@ApiResponse(responseCode = "422", description = "Produto ou usuário inativo")
	})
	ResponseEntity<AvisoFaltaResponseDto> registrar(AvisoFaltaRequestDto dto);

	@Operation(summary = "Resolver aviso de falta",
			description = "Muda o aviso para RESOLVIDO. Resolver um aviso já resolvido não altera nada e também responde 200.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Aviso resolvido",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "avisoResolvido",
									value = """
										{
										  "id": 1,
										  "status": "RESOLVIDO",
										  "observacao": "acabou na prateleira",
										  "quantidadeAvisos": 1,
										  "somado": false,
										  "criadoEm": "2026-10-31T10:15:00",
										  "produto": { "id": 8, "sku": "1543", "nome": "Fita veda-rosca 18 mm" },
										  "usuario": { "id": 4, "nome": "Valdir" }
										}
										"""))),
			@ApiResponse(responseCode = "404", description = "Aviso não encontrado")
	})
	ResponseEntity<AvisoFaltaResponseDto> resolver(
			@Parameter(description = "Identificador do aviso", example = "1") Integer id);

}
