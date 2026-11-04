package com.portalblox.controller;

import com.portalblox.controller.docs.FornecedorApi;
import com.portalblox.dto.FornecedorRequestDto;
import com.portalblox.dto.FornecedorResponseDto;
import com.portalblox.entity.Fornecedor;
import com.portalblox.mapper.FornecedorMapper;
import com.portalblox.service.FornecedorService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController implements FornecedorApi {

	private final FornecedorService fornecedorService;

	public FornecedorController(FornecedorService fornecedorService) {
		this.fornecedorService = fornecedorService;
	}

	@GetMapping
	public ResponseEntity<List<FornecedorResponseDto>> listar(
			@RequestParam(defaultValue = "false") boolean incluirInativos) {
		return ResponseEntity.ok(FornecedorMapper.toResponseDto(fornecedorService.listar(incluirInativos)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<FornecedorResponseDto> buscarPorId(@PathVariable Integer id) {
		return ResponseEntity.ok(FornecedorMapper.toResponseDto(fornecedorService.buscarPorId(id)));
	}

	@PostMapping
	public ResponseEntity<FornecedorResponseDto> cadastrar(@Valid @RequestBody FornecedorRequestDto dto) {
		Fornecedor salvo = fornecedorService.cadastrar(FornecedorMapper.toEntity(dto));
		return ResponseEntity.status(HttpStatus.CREATED).body(FornecedorMapper.toResponseDto(salvo));
	}

	@PutMapping("/{id}")
	public ResponseEntity<FornecedorResponseDto> atualizar(@PathVariable Integer id,
			@Valid @RequestBody FornecedorRequestDto dto) {
		Fornecedor atualizado = fornecedorService.atualizar(id, FornecedorMapper.toEntity(dto));
		return ResponseEntity.ok(FornecedorMapper.toResponseDto(atualizado));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> inativar(@PathVariable Integer id) {
		fornecedorService.inativar(id);
		return ResponseEntity.noContent().build();
	}

}
