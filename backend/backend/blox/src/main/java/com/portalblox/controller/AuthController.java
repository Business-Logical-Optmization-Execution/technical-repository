package com.portalblox.controller;

import com.portalblox.controller.docs.AuthApi;
import com.portalblox.dto.LoginRequestDto;
import com.portalblox.dto.UsuarioResponseDto;
import com.portalblox.entity.Usuario;
import com.portalblox.mapper.UsuarioMapper;
import com.portalblox.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthApi {

	private final UsuarioService usuarioService;

	public AuthController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@PostMapping("/login")
	public ResponseEntity<UsuarioResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
		Usuario usuario = usuarioService.autenticar(dto.getEmail(), dto.getSenha());
		return ResponseEntity.ok(UsuarioMapper.toResponseDto(usuario));
	}

}
