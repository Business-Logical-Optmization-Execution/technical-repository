package com.portalblox.controller;

import com.portalblox.controller.docs.AvisoFaltaApi;
import com.portalblox.dto.AvisoFaltaRequestDto;
import com.portalblox.dto.AvisoFaltaResponseDto;
import com.portalblox.entity.AvisoFalta;
import com.portalblox.entity.StatusAviso;
import com.portalblox.mapper.AvisoFaltaMapper;
import com.portalblox.service.AvisoFaltaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/avisos-falta")
public class AvisoFaltaController implements AvisoFaltaApi {

	private final AvisoFaltaService avisoFaltaService;

	public AvisoFaltaController(AvisoFaltaService avisoFaltaService) {
		this.avisoFaltaService = avisoFaltaService;
	}

	@GetMapping
	public ResponseEntity<List<AvisoFaltaResponseDto>> listar(@RequestParam(required = false) StatusAviso status) {
		return ResponseEntity.ok(AvisoFaltaMapper.toResponseDto(avisoFaltaService.listar(status)));
	}

	@PostMapping
	public ResponseEntity<AvisoFaltaResponseDto> registrar(@Valid @RequestBody AvisoFaltaRequestDto dto) {
		AvisoFalta aviso = avisoFaltaService.registrar(dto.getProdutoId(), dto.getUsuarioId(), dto.getObservacao());
		HttpStatus status = aviso.foiSomado() ? HttpStatus.OK : HttpStatus.CREATED;
		return ResponseEntity.status(status).body(AvisoFaltaMapper.toResponseDto(aviso));
	}

	@PatchMapping("/{id}/resolver")
	public ResponseEntity<AvisoFaltaResponseDto> resolver(@PathVariable Integer id) {
		return ResponseEntity.ok(AvisoFaltaMapper.toResponseDto(avisoFaltaService.resolver(id)));
	}

}
