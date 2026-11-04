package com.portalblox.controller.docs;

import com.portalblox.dto.LoginRequestDto;
import com.portalblox.dto.UsuarioResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Autenticação", description = "Login de usuários ativos por e-mail e senha")
public interface AuthApi {

	@Operation(summary = "Autenticar usuário",
			description = "Confere e-mail e senha de um usuário ativo e devolve os dados do usuário "
					+ "sem o campo senha. Não emite token nesta fase (POC).")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Credencial válida",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "loginValido",
									value = """
										{
										  "id": 1,
										  "nome": "Márcia",
										  "email": "marcia@ottimos.com",
										  "perfilAcesso": {
										    "id": 1,
										    "nome": "Administrador"
										  },
										  "ativo": true
										}
										"""))),
			@ApiResponse(responseCode = "400", description = "Payload inválido"),
			@ApiResponse(responseCode = "401", description = "Usuário ou senha incorretos",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "credencialInvalida",
									value = """
										{
										  "status": 401,
										  "mensagem": "Usuário ou senha incorretos.",
										  "erros": []
										}
										""")))
	})
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credenciais do usuário",
			required = true,
			content = @Content(mediaType = "application/json",
					examples = @ExampleObject(name = "login",
							value = """
								{
								  "email": "marcia@ottimos.com",
								  "senha": "123456"
								}
								""")))
	ResponseEntity<UsuarioResponseDto> login(LoginRequestDto dto);

}
