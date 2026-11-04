package com.portalblox.controller;

import com.portalblox.controller.docs.PerfilAcessoApi;
import com.portalblox.dto.UsuarioResponseDto;
import com.portalblox.mapper.UsuarioMapper;
import com.portalblox.service.UsuarioService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/perfis-acesso")
public class PerfilAcessoController implements PerfilAcessoApi {

	private final UsuarioService usuarioService;

	public PerfilAcessoController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping
	public ResponseEntity<List<UsuarioResponseDto.PerfilAcessoResponseDto>> listar() {
		return ResponseEntity.ok(usuarioService.listarPerfis().stream()
				.map(UsuarioMapper::toPerfilResponseDto)
				.toList());
	}

}
