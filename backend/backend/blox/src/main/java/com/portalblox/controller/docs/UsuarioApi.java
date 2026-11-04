package com.portalblox.controller.docs;

import com.portalblox.dto.UsuarioAtualizacaoRequestDto;
import com.portalblox.dto.UsuarioCriacaoRequestDto;
import com.portalblox.dto.UsuarioResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Usuários", description = "Gestão de usuários do sistema: cadastro, consulta, listagem, "
		+ "atualização e inativação lógica — a senha jamais é retornada nas respostas (RN-07)")
public interface UsuarioApi {

	@Operation(summary = "Listar usuários",
			description = "Retorna somente usuários ativos por padrão; use ?incluirInativos=true "
					+ "para incluir também os inativos (RN-04).")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista de usuários retornada")
	})
	ResponseEntity<List<UsuarioResponseDto>> listar(
			@Parameter(description = "Inclui os registros inativos", example = "false")
			boolean incluirInativos);

	@Operation(summary = "Buscar usuário por id",
			description = "Retorna o usuário com seu perfil de acesso; a senha não faz parte da resposta (RN-07).")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Usuário encontrado"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	})
	ResponseEntity<UsuarioResponseDto> buscarPorId(
			@Parameter(description = "Identificador do usuário", example = "1") Integer id);

	@Operation(summary = "Cadastrar usuário",
			description = "Cria um usuário vinculado a um perfil de acesso existente (RN-08); "
					+ "o e-mail deve ser único (RN-02) e o usuário nasce ativo.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Usuário criado"),
			@ApiResponse(responseCode = "400", description = "Payload inválido"),
			@ApiResponse(responseCode = "404", description = "Perfil de acesso inexistente"),
			@ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
	})
	ResponseEntity<UsuarioResponseDto> cadastrar(
			UsuarioCriacaoRequestDto dto);

	@Operation(summary = "Atualizar usuário",
			description = "Altera somente nome, e-mail e perfil de acesso; senha e estado ativo "
					+ "são sempre preservados. O contrato de atualização não tem senha: "
					+ "se o campo for enviado no JSON, é ignorado.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Usuário atualizado"),
			@ApiResponse(responseCode = "400", description = "Payload inválido"),
			@ApiResponse(responseCode = "404", description = "Usuário ou perfil inexistente"),
			@ApiResponse(responseCode = "409", description = "E-mail já cadastrado a outro usuário")
	})
	ResponseEntity<UsuarioResponseDto> atualizar(
			@Parameter(description = "Identificador do usuário", example = "3") Integer id,
			UsuarioAtualizacaoRequestDto dto);

	@Operation(summary = "Inativar usuário",
			description = "Exclusão lógica (RN-03): define ativo=false; o registro permanece no banco. "
					+ "O último administrador ativo não pode ser inativado.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Usuário inativado"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
			@ApiResponse(responseCode = "422", description = "Usuário já inativo (RN-05) ou último administrador ativo",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "ultimoAdministrador",
									value = """
										{
										  "status": 422,
										  "mensagem": "Não é possível desativar o último administrador ativo.",
										  "erros": []
										}
										""")))
	})
	ResponseEntity<Void> inativar(
			@Parameter(description = "Identificador do usuário", example = "4") Integer id);

}
