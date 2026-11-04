package com.portalblox.controller.docs;

import com.portalblox.dto.UsuarioResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Perfis de acesso", description = "Perfis disponíveis para o cadastro de usuários")
public interface PerfilAcessoApi {

	@Operation(summary = "Listar perfis de acesso",
			description = "Devolve o identificador e o nome de cada perfil, ordenados por identificador.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista de perfis retornada",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "perfis",
									value = """
										[
										  { "id": 1, "nome": "Administrador" },
										  { "id": 2, "nome": "Operador" }
										]
										""")))
	})
	ResponseEntity<List<UsuarioResponseDto.PerfilAcessoResponseDto>> listar();

}
