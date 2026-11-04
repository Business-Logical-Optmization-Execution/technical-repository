package com.portalblox.controller.docs;

import com.portalblox.dto.ProdutoAtualizacaoRequestDto;
import com.portalblox.dto.ProdutoQuantidadeRequestDto;
import com.portalblox.dto.ProdutoRequestDto;
import com.portalblox.dto.ProdutoResponseDto;
import com.portalblox.entity.Produto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Produtos", description = "Gestão de produtos do estoque: cadastro, consulta, listagem, "
		+ "atualização cadastral, ajuste auditado de quantidade e inativação lógica")
public interface ProdutoApi {

	@Operation(summary = "Listar produtos",
			description = "Retorna somente produtos ativos por padrão; use ?incluirInativos=true "
					+ "para incluir também os inativos (RN-04).")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista de produtos retornada",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "produtoComMinimoProprio",
									value = """
										[
										  {
										    "id": 1,
										    "sku": "1001",
										    "nome": "Martelo",
										    "peso": null,
										    "tamanho": null,
										    "descricao": "Martelo de unha 27 mm, cabo de madeira.",
										    "preco": 34.90,
										    "marca": null,
										    "fotoUrl": null,
										    "estoqueMinimo": 10,
										    "estoqueMinimoEfetivo": 10,
										    "quantidade": 2,
										    "ativo": true,
										    "categoria": {
										      "id": 1,
										      "nome": "Ferragens"
										    },
										    "fornecedor": {
										      "id": 1,
										      "nome": "Ferragens Silva",
										      "cnpj": "11222333000181"
										    },
										    "localizacoes": ["C3-P2"]
										  }
										]
										""")))
	})
	ResponseEntity<List<ProdutoResponseDto>> listar(
			@Parameter(description = "Inclui os registros inativos", example = "false")
			boolean incluirInativos);

	@Operation(summary = "Buscar produto por id",
			description = "Retorna o produto com sua categoria, fornecedor e localizações. "
					+ "O saldo é global e aparece uma única vez em `quantidade`.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Produto encontrado",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "produtoSemMinimoProprio",
									value = """
										{
										  "id": 2,
										  "sku": "1002",
										  "nome": "Chave de fenda",
										  "peso": null,
										  "tamanho": null,
										  "descricao": null,
										  "preco": 12.50,
										  "marca": null,
										  "fotoUrl": null,
										  "estoqueMinimo": null,
										  "estoqueMinimoEfetivo": 5,
										  "quantidade": 2,
										  "ativo": true,
										  "categoria": {
										    "id": 1,
										    "nome": "Ferragens"
										  },
										  "fornecedor": {
										    "id": 2,
										    "nome": "Distribuidora Central",
										    "cnpj": "22333444000172"
										  },
										  "localizacoes": ["C3-P2"]
										}
										"""))),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado")
	})
	ResponseEntity<ProdutoResponseDto> buscarPorId(
			@Parameter(description = "Identificador do produto", example = "1") Integer id);

	@Operation(summary = "Cadastrar produto",
			description = "Cria o produto com categoria única, fornecedor opcional, localizações livres, "
					+ "`fotoUrl` e `estoqueMinimo` opcionais. `usuarioId` é obrigatório mesmo com quantidade "
					+ "zero. Quando a quantidade inicial é maior que zero, registra uma movimentação "
					+ "SALDO_INICIAL com observação fixa.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Produto criado",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "produtoCriado",
									value = """
										{
										  "id": 13,
										  "sku": "3001",
										  "nome": "Martelo de bola",
										  "peso": null,
										  "tamanho": null,
										  "descricao": "Cabo de madeira.",
										  "preco": 27.5,
										  "marca": null,
										  "fotoUrl": "https://exemplo.com/martelo.jpg",
										  "estoqueMinimo": 2,
										  "estoqueMinimoEfetivo": 2,
										  "quantidade": 4,
										  "ativo": true,
										  "categoria": {
										    "id": 1,
										    "nome": "Ferragens"
										  },
										  "fornecedor": null,
										  "localizacoes": ["C3-P2"]
										}
										"""))),
			@ApiResponse(responseCode = "400", description = "Payload inválido ou localização duplicada"),
			@ApiResponse(responseCode = "404", description = "Categoria, fornecedor ou usuário inexistente"),
			@ApiResponse(responseCode = "409", description = "SKU já cadastrado"),
			@ApiResponse(responseCode = "422", description = "Fornecedor ou usuário inativo")
	})
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do novo produto",
			required = true,
			content = @Content(mediaType = "application/json",
					examples = @ExampleObject(name = "cadastroComMinimoEFoto",
							value = """
								{
								  "sku": "3001",
								  "nome": "Martelo de bola",
								  "categoriaId": 1,
								  "quantidade": 4,
								  "usuarioId": 1,
								  "preco": 27.5,
								  "estoqueMinimo": 2,
								  "descricao": "Cabo de madeira.",
								  "fotoUrl": "https://exemplo.com/martelo.jpg",
								  "localizacoes": ["C3-P2"]
								}
								""")))
	ResponseEntity<ProdutoResponseDto> cadastrar(
			ProdutoRequestDto dto);

	@Operation(summary = "Atualizar cadastro do produto",
			description = "Substitui os dados cadastrais, a categoria, o fornecedor, as localizações, "
					+ "`fotoUrl` e `estoqueMinimo`. Não altera a quantidade nem o estado ativo: o campo "
					+ "`quantidade` é ignorado. Lista de localizações ausente ou nula limpa as localizações; "
					+ "`fornecedorId` nulo remove o vínculo. Produto inativo responde 422.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Produto atualizado",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "produtoAtualizado",
									value = """
										{
										  "id": 1,
										  "sku": "1001",
										  "nome": "Martelo",
										  "peso": null,
										  "tamanho": null,
										  "descricao": "Martelo de unha 27 mm, cabo de madeira.",
										  "preco": 39.9,
										  "marca": null,
										  "fotoUrl": null,
										  "estoqueMinimo": 12,
										  "estoqueMinimoEfetivo": 12,
										  "quantidade": 2,
										  "ativo": true,
										  "categoria": {
										    "id": 1,
										    "nome": "Ferragens"
										  },
										  "fornecedor": {
										    "id": 1,
										    "nome": "Ferragens Silva",
										    "cnpj": "11222333000181"
										  },
										  "localizacoes": ["C3-P2"]
										}
										"""))),
			@ApiResponse(responseCode = "400", description = "Payload inválido ou localização duplicada"),
			@ApiResponse(responseCode = "404",
					description = "Produto, categoria ou fornecedor inexistente"),
			@ApiResponse(responseCode = "409", description = "SKU já cadastrado a outro produto"),
			@ApiResponse(responseCode = "422", description = "Produto ou fornecedor inativo")
	})
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados cadastrais atualizados",
			required = true,
			content = @Content(mediaType = "application/json",
					examples = @ExampleObject(name = "atualizacaoComMinimo",
							value = """
								{
								  "sku": "1001",
								  "nome": "Martelo",
								  "categoriaId": 1,
								  "preco": 39.9,
								  "estoqueMinimo": 12,
								  "fotoUrl": null,
								  "localizacoes": ["C3-P2"]
								}
								""")))
	ResponseEntity<ProdutoResponseDto> atualizar(
			@Parameter(description = "Identificador do produto", example = "1") Integer id,
			ProdutoAtualizacaoRequestDto dto);

	@Operation(summary = "Ajustar quantidade do produto (compatibilidade)",
			description = "Caminho de compatibilidade para substituir o saldo pelo valor informado, "
					+ "exigindo autor e justificativa. A justificativa é gravada em `observacao` e uma "
					+ "movimentação AJUSTE é registrada na mesma transação, inclusive quando o valor não "
					+ "muda (variação zero). Para entradas, saídas e motivos, use POST /movimentacoes. "
					+ "Produto ou usuário inativo responde 422.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Quantidade atualizada",
					content = @Content(mediaType = "application/json",
							examples = @ExampleObject(name = "produtoAjustado",
									value = """
										{
										  "id": 1,
										  "sku": "1001",
										  "nome": "Martelo",
										  "peso": null,
										  "tamanho": null,
										  "descricao": "Martelo de unha 27 mm, cabo de madeira.",
										  "preco": 34.9,
										  "marca": null,
										  "fotoUrl": null,
										  "estoqueMinimo": 10,
										  "estoqueMinimoEfetivo": 10,
										  "quantidade": 6,
										  "ativo": true,
										  "categoria": {
										    "id": 1,
										    "nome": "Ferragens"
										  },
										  "fornecedor": {
										    "id": 1,
										    "nome": "Ferragens Silva",
										    "cnpj": "11222333000181"
										  },
										  "localizacoes": ["C3-P2"]
										}
										"""))),
			@ApiResponse(responseCode = "400", description = "Quantidade negativa, ausente ou justificativa vazia"),
			@ApiResponse(responseCode = "404", description = "Produto ou usuário não encontrado"),
			@ApiResponse(responseCode = "422", description = "Produto ou usuário inativo")
	})
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Novo saldo e justificativa",
			required = true,
			content = @Content(mediaType = "application/json",
					examples = @ExampleObject(name = "ajusteDeSaldo",
							value = """
								{
								  "quantidade": 6,
								  "usuarioId": 1,
								  "justificativa": "Contagem da prateleira C3-P2"
								}
								""")))
	ResponseEntity<ProdutoResponseDto> ajustarQuantidade(
			@Parameter(description = "Identificador do produto", example = "1") Integer id,
			ProdutoQuantidadeRequestDto dto);

	@Operation(summary = "Inativar produto",
			description = "Exclusão lógica (RN-03): define ativo=false; o registro permanece no banco.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Produto inativado"),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado"),
			@ApiResponse(responseCode = "422", description = "Produto já inativo (RN-05)")
	})
	ResponseEntity<Void> inativar(
			@Parameter(description = "Identificador do produto", example = "11") Integer id);

}
