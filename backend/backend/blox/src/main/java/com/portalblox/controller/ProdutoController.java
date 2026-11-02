package com.portalblox.controller;

import com.portalblox.dto.ProdutoAtualizacaoRequestDto;
import com.portalblox.dto.ProdutoQuantidadeRequestDto;
import com.portalblox.dto.ProdutoRequestDto;
import com.portalblox.dto.ProdutoResponseDto;
import com.portalblox.entity.Produto;
import com.portalblox.mapper.ProdutoMapper;
import com.portalblox.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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

@Tag(name = "Produtos", description = "Gestão de produtos do estoque: cadastro, consulta, listagem, "
		+ "atualização cadastral, ajuste auditado de quantidade e inativação lógica")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	private final ProdutoService produtoService;

	public ProdutoController(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}

	@Operation(summary = "Listar produtos",
			description = "Retorna somente produtos ativos por padrão; use ?incluirInativos=true "
					+ "para incluir também os inativos (RN-04).")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista de produtos retornada")
	})
	@GetMapping
	public ResponseEntity<List<ProdutoResponseDto>> listar(
			@RequestParam(defaultValue = "false") boolean incluirInativos) {
		List<Produto> produtos = produtoService.listar(incluirInativos);
		return ResponseEntity.status(200).body(ProdutoMapper.toResponseDto(produtos));
	}

	@Operation(summary = "Buscar produto por id",
			description = "Retorna o produto com sua categoria, fornecedor e localizações. "
					+ "O saldo é global e aparece uma única vez em `quantidade`.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Produto encontrado"),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado")
	})
	@GetMapping("/{id}")
	public ResponseEntity<ProdutoResponseDto> buscarPorId(@PathVariable Integer id) {
		Produto produto = produtoService.buscarPorId(id);
		return ResponseEntity.status(200).body(ProdutoMapper.toResponseDto(produto));
	}

	@Operation(summary = "Cadastrar produto",
			description = "Cria o produto com categoria única, fornecedor opcional e localizações livres. "
					+ "`usuarioId` é obrigatório mesmo com quantidade zero. Quando a quantidade inicial "
					+ "é maior que zero, registra uma movimentação SALDO_INICIAL auditável.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Produto criado"),
			@ApiResponse(responseCode = "400", description = "Payload inválido ou localização duplicada"),
			@ApiResponse(responseCode = "404", description = "Categoria, fornecedor ou usuário inexistente"),
			@ApiResponse(responseCode = "409", description = "SKU já cadastrado"),
			@ApiResponse(responseCode = "422", description = "Fornecedor ou usuário inativo")
	})
	@PostMapping
	public ResponseEntity<ProdutoResponseDto> cadastrar(
			@Valid @RequestBody ProdutoRequestDto dto) {
		Produto produto = ProdutoMapper.toEntity(dto);
		Produto salvo = produtoService.cadastrar(produto, dto.getCategoriaId(), dto.getFornecedorId(),
				dto.getUsuarioId(), dto.getLocalizacoes());
		return ResponseEntity.status(201).body(ProdutoMapper.toResponseDto(salvo));
	}

	@Operation(summary = "Atualizar cadastro do produto",
			description = "Substitui os dados cadastrais, a categoria, o fornecedor e as localizações. "
					+ "Não altera a quantidade nem o estado ativo: o campo `quantidade` é ignorado. "
					+ "Lista de localizações ausente ou nula limpa as localizações; `fornecedorId` nulo "
					+ "remove o vínculo. Produto inativo responde 422.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Produto atualizado"),
			@ApiResponse(responseCode = "400", description = "Payload inválido ou localização duplicada"),
			@ApiResponse(responseCode = "404",
					description = "Produto, categoria ou fornecedor inexistente"),
			@ApiResponse(responseCode = "409", description = "SKU já cadastrado a outro produto"),
			@ApiResponse(responseCode = "422", description = "Produto ou fornecedor inativo")
	})
	@PutMapping("/{id}")
	public ResponseEntity<ProdutoResponseDto> atualizar(@PathVariable Integer id,
			@Valid @RequestBody ProdutoAtualizacaoRequestDto dto) {
		Produto produto = ProdutoMapper.toEntity(dto);
		Produto atualizado = produtoService.atualizar(id, produto, dto.getCategoriaId(),
				dto.getFornecedorId(), dto.getLocalizacoes());
		return ResponseEntity.status(200).body(ProdutoMapper.toResponseDto(atualizado));
	}

	@Operation(summary = "Ajustar quantidade do produto",
			description = "Único ponto de mutação do saldo depois do cadastro: substitui a quantidade "
					+ "pelo valor informado e registra uma movimentação AJUSTE com autor, justificativa, "
					+ "saldo anterior e saldo posterior na mesma transação. Se o valor não mudar, "
					+ "responde 200 sem registrar movimentação. Produto inativo responde 422.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Quantidade atualizada"),
			@ApiResponse(responseCode = "400", description = "Quantidade negativa, ausente ou justificativa vazia"),
			@ApiResponse(responseCode = "404", description = "Produto ou usuário não encontrado"),
			@ApiResponse(responseCode = "422", description = "Produto ou usuário inativo")
	})
	@PatchMapping("/{id}/quantidade")
	public ResponseEntity<ProdutoResponseDto> ajustarQuantidade(@PathVariable Integer id,
			@Valid @RequestBody ProdutoQuantidadeRequestDto dto) {
		Produto produto = produtoService.ajustarQuantidade(id, dto.getQuantidade(), dto.getUsuarioId(),
				dto.getJustificativa());
		return ResponseEntity.status(200).body(ProdutoMapper.toResponseDto(produto));
	}

	@Operation(summary = "Inativar produto",
			description = "Exclusão lógica (RN-03): define ativo=false; o registro permanece no banco.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Produto inativado"),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado"),
			@ApiResponse(responseCode = "422", description = "Produto já inativo (RN-05)")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> inativar(@PathVariable Integer id) {
		produtoService.inativar(id);
		return ResponseEntity.status(204).build();
	}

}
