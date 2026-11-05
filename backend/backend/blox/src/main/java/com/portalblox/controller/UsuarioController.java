package com.portalblox.controller;

import com.portalblox.controller.docs.UsuarioApi;
import com.portalblox.dto.AlterarSenhaRequestDto;
import com.portalblox.dto.UsuarioAtualizacaoRequestDto;
import com.portalblox.dto.UsuarioCriacaoRequestDto;
import com.portalblox.dto.UsuarioResponseDto;
import com.portalblox.entity.Usuario;
import com.portalblox.mapper.UsuarioMapper;
import com.portalblox.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController implements UsuarioApi {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping
	public ResponseEntity<List<UsuarioResponseDto>> listar(
			@RequestParam(defaultValue = "false") boolean incluirInativos) {
		return ResponseEntity.ok(UsuarioMapper.toResponseDto(usuarioService.listar(incluirInativos)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponseDto> buscarPorId(@PathVariable Integer id) {
		return ResponseEntity.ok(UsuarioMapper.toResponseDto(usuarioService.buscarPorId(id)));
	}

	@PostMapping
	public ResponseEntity<UsuarioResponseDto> cadastrar(@Valid @RequestBody UsuarioCriacaoRequestDto dto) {
		Usuario salvo = usuarioService.cadastrar(UsuarioMapper.toEntity(dto), dto.getPerfilAcessoId());
		return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toResponseDto(salvo));
	}

	@PutMapping("/{id}")
	public ResponseEntity<UsuarioResponseDto> atualizar(@PathVariable Integer id,
			@Valid @RequestBody UsuarioAtualizacaoRequestDto dto) {
		Usuario atualizado = usuarioService.atualizar(id, UsuarioMapper.toEntity(dto), dto.getPerfilAcessoId());
		return ResponseEntity.ok(UsuarioMapper.toResponseDto(atualizado));
	}

	@PatchMapping("/{id}/senha")
	public ResponseEntity<Void> alterarSenha(@PathVariable Integer id, @Valid @RequestBody AlterarSenhaRequestDto dto) {
		usuarioService.alterarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> inativar(@PathVariable Integer id) {
		usuarioService.inativar(id);
		return ResponseEntity.noContent().build();
	}

}
