package com.portalblox.controller;

import com.portalblox.dto.FornecedorRequestDto;
import com.portalblox.dto.FornecedorResponseDto;
import com.portalblox.entity.Fornecedor;
import com.portalblox.mapper.FornecedorMapper;
import com.portalblox.service.FornecedorService;
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

@Tag(name = "Fornecedores", description = "Cadastro, consulta, atualização e inativação lógica de "
		+ "fornecedores usados pelo catálogo de produtos")
@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

	private final FornecedorService fornecedorService;

	public FornecedorController(FornecedorService fornecedorService) {
		this.fornecedorService = fornecedorService;
	}

	@Operation(summary = "Listar fornecedores",
			description = "Retorna somente fornecedores ativos por padrão; use ?incluirInativos=true "
					+ "para incluir também os inativos.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista de fornecedores retornada")
	})
	@GetMapping
	public ResponseEntity<List<FornecedorResponseDto>> listar(
			@RequestParam(defaultValue = "false") boolean incluirInativos) {
		List<Fornecedor> fornecedores = fornecedorService.listar(incluirInativos);
		return ResponseEntity.status(200).body(FornecedorMapper.toResponseDto(fornecedores));
	}

	@Operation(summary = "Buscar fornecedor por id",
			description = "Retorna o fornecedor pelo identificador.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
			@ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
	})
	@GetMapping("/{id}")
	public ResponseEntity<FornecedorResponseDto> buscarPorId(@PathVariable Integer id) {
		Fornecedor fornecedor = fornecedorService.buscarPorId(id);
		return ResponseEntity.status(200).body(FornecedorMapper.toResponseDto(fornecedor));
	}

	@Operation(summary = "Cadastrar fornecedor",
			description = "Cria o fornecedor ativo. O CNPJ é armazenado somente com dígitos, deve ter "
					+ "exatamente 14 caracteres e ser único; os dígitos verificadores não são validados.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Fornecedor criado"),
			@ApiResponse(responseCode = "400", description = "Payload inválido ou CNPJ sem 14 dígitos"),
			@ApiResponse(responseCode = "409", description = "CNPJ já cadastrado")
	})
	@PostMapping
	public ResponseEntity<FornecedorResponseDto> cadastrar(
			@Valid @RequestBody FornecedorRequestDto dto) {
		Fornecedor fornecedor = FornecedorMapper.toEntity(dto);
		Fornecedor salvo = fornecedorService.cadastrar(fornecedor);
		return ResponseEntity.status(201).body(FornecedorMapper.toResponseDto(salvo));
	}

	@Operation(summary = "Atualizar fornecedor",
			description = "Altera nome e CNPJ; o estado ativo e a data de criação são preservados.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Fornecedor atualizado"),
			@ApiResponse(responseCode = "400", description = "Payload inválido ou CNPJ sem 14 dígitos"),
			@ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
			@ApiResponse(responseCode = "409", description = "CNPJ já cadastrado a outro fornecedor")
	})
	@PutMapping("/{id}")
	public ResponseEntity<FornecedorResponseDto> atualizar(@PathVariable Integer id,
			@Valid @RequestBody FornecedorRequestDto dto) {
		Fornecedor fornecedor = FornecedorMapper.toEntity(dto);
		Fornecedor atualizado = fornecedorService.atualizar(id, fornecedor);
		return ResponseEntity.status(200).body(FornecedorMapper.toResponseDto(atualizado));
	}

	@Operation(summary = "Inativar fornecedor",
			description = "Exclusão lógica: define ativo=false; o registro permanece no banco.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Fornecedor inativado"),
			@ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
			@ApiResponse(responseCode = "422", description = "Fornecedor já inativo")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> inativar(@PathVariable Integer id) {
		fornecedorService.inativar(id);
		return ResponseEntity.status(204).build();
	}

}
