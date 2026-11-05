package com.portalblox.controller;

import com.portalblox.controller.docs.ProdutoApi;
import com.portalblox.dto.ProdutoAtualizacaoRequestDto;
import com.portalblox.dto.ProdutoQuantidadeRequestDto;
import com.portalblox.dto.ProdutoRequestDto;
import com.portalblox.dto.ProdutoResponseDto;
import com.portalblox.entity.Produto;
import com.portalblox.mapper.ProdutoMapper;
import com.portalblox.service.ProdutoService;
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
@RequestMapping("/produtos")
public class ProdutoController implements ProdutoApi {

	private final ProdutoService produtoService;

	public ProdutoController(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	@GetMapping
	public ResponseEntity<List<ProdutoResponseDto>> listar(
			@RequestParam(defaultValue = "false") boolean incluirInativos) {
		List<Produto> produtos = produtoService.listar(incluirInativos);
		int estoqueMinimoGlobal = produtoService.obterEstoqueMinimoGlobal();
		return ResponseEntity.ok(ProdutoMapper.toResponseDto(produtos, estoqueMinimoGlobal));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProdutoResponseDto> buscarPorId(@PathVariable Integer id) {
		return ResponseEntity.ok(toResponseDto(produtoService.buscarPorId(id)));
	}

	@PostMapping
	public ResponseEntity<ProdutoResponseDto> cadastrar(@Valid @RequestBody ProdutoRequestDto dto) {
		Produto salvo = produtoService.cadastrar(ProdutoMapper.toEntity(dto), dto.getCategoriaId(),
				dto.getFornecedorId(), dto.getUsuarioId(), dto.getLocalizacoes());
		return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDto(salvo));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProdutoResponseDto> atualizar(@PathVariable Integer id,
			@Valid @RequestBody ProdutoAtualizacaoRequestDto dto) {
		Produto atualizado = produtoService.atualizar(id, ProdutoMapper.toEntity(dto), dto.getCategoriaId(),
				dto.getFornecedorId(), dto.getLocalizacoes());
		return ResponseEntity.ok(toResponseDto(atualizado));
	}

	@PatchMapping("/{id}/quantidade")
	public ResponseEntity<ProdutoResponseDto> ajustarQuantidade(@PathVariable Integer id,
			@Valid @RequestBody ProdutoQuantidadeRequestDto dto) {
		Produto produto = produtoService.ajustarQuantidade(id, dto.getQuantidade(), dto.getUsuarioId(),
				dto.getJustificativa());
		return ResponseEntity.ok(toResponseDto(produto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> inativar(@PathVariable Integer id) {
		produtoService.inativar(id);
		return ResponseEntity.noContent().build();
	}

	private ProdutoResponseDto toResponseDto(Produto produto) {
		return ProdutoMapper.toResponseDto(produto, produtoService.obterEstoqueMinimoGlobal());
	}

}
