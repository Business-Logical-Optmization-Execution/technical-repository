package com.portalblox.controller;

import com.portalblox.dto.UsuarioRequestDto;
import com.portalblox.dto.UsuarioResponseDto;
import com.portalblox.entity.Usuario;
import com.portalblox.mapper.UsuarioMapper;
import com.portalblox.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Usuários", description = "Gestão de usuários do sistema: cadastro, consulta, listagem, "
		+ "atualização e inativação lógica — a senha jamais é retornada nas respostas (RN-07)")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@Operation(summary = "Listar usuários",
			description = "Retorna somente usuários ativos por padrão; use ?incluirInativos=true "
					+ "para incluir também os inativos (RN-04).")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista de usuários retornada")
	})
	@GetMapping
	public ResponseEntity<List<UsuarioResponseDto>> listar(
			@RequestParam(defaultValue = "false") boolean incluirInativos) {
		List<Usuario> usuarios = usuarioService.listar(incluirInativos);
		return ResponseEntity.status(200).body(UsuarioMapper.toResponseDto(usuarios));
	}

	@Operation(summary = "Buscar usuário por id",
			description = "Retorna o usuário com seu perfil de acesso; a senha não faz parte da resposta (RN-07).")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Usuário encontrado"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	})
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponseDto> buscarPorId(@PathVariable Integer id) {
		Usuario usuario = usuarioService.buscarPorId(id);
		return ResponseEntity.status(200).body(UsuarioMapper.toResponseDto(usuario));
	}

	@Operation(summary = "Cadastrar usuário",
			description = "Cria um usuário vinculado a um perfil de acesso existente (RN-08); "
					+ "o e-mail deve ser único (RN-02) e o usuário nasce ativo.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Usuário criado"),
			@ApiResponse(responseCode = "400", description = "Payload inválido"),
			@ApiResponse(responseCode = "404", description = "Perfil de acesso inexistente"),
			@ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
	})
	@PostMapping
	public ResponseEntity<UsuarioResponseDto> cadastrar(
			@Valid @RequestBody UsuarioRequestDto dto) {
		Usuario usuario = UsuarioMapper.toEntity(dto);
		Usuario salvo = usuarioService.cadastrar(usuario, dto.getPerfilAcessoId());
		return ResponseEntity.status(201).body(UsuarioMapper.toResponseDto(salvo));
	}

	@Operation(summary = "Atualizar usuário",
			description = "Altera somente nome, e-mail e perfil de acesso; senha e estado ativo "
					+ "são sempre preservados.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Usuário atualizado"),
			@ApiResponse(responseCode = "400", description = "Payload inválido"),
			@ApiResponse(responseCode = "404", description = "Usuário ou perfil inexistente"),
			@ApiResponse(responseCode = "409", description = "E-mail já cadastrado a outro usuário")
	})
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioResponseDto> atualizar(@PathVariable Integer id,
			@Valid @RequestBody UsuarioRequestDto dto) {
		Usuario usuario = UsuarioMapper.toEntity(dto);
		Usuario atualizado = usuarioService.atualizar(id, usuario, dto.getPerfilAcessoId());
		return ResponseEntity.status(200).body(UsuarioMapper.toResponseDto(atualizado));
	}

	@Operation(summary = "Inativar usuário",
			description = "Exclusão lógica (RN-03): define ativo=false; o registro permanece no banco.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Usuário inativado"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
			@ApiResponse(responseCode = "422", description = "Usuário já inativo (RN-05)")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> inativar(@PathVariable Integer id) {
		usuarioService.inativar(id);
		return ResponseEntity.status(204).build();
	}

}
