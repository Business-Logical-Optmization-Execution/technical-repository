package com.portalblox.controller.docs;

import com.portalblox.dto.FornecedorRequestDto;
import com.portalblox.dto.FornecedorResponseDto;
import com.portalblox.entity.Fornecedor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Fornecedores", description = "Cadastro, consulta, atualização e inativação lógica de "
		+ "fornecedores usados pelo catálogo de produtos")
public interface FornecedorApi {

	@Operation(summary = "Listar fornecedores",
			description = "Retorna somente fornecedores ativos por padrão; use ?incluirInativos=true "
					+ "para incluir também os inativos.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista de fornecedores retornada")
	})
	ResponseEntity<List<FornecedorResponseDto>> listar(
			@Parameter(description = "Inclui os registros inativos", example = "false")
			boolean incluirInativos);

	@Operation(summary = "Buscar fornecedor por id",
			description = "Retorna o fornecedor pelo identificador.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
			@ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
	})
	ResponseEntity<FornecedorResponseDto> buscarPorId(
			@Parameter(description = "Identificador do fornecedor", example = "1") Integer id);

	@Operation(summary = "Cadastrar fornecedor",
			description = "Cria o fornecedor ativo. O CNPJ é armazenado somente com dígitos, deve ter "
					+ "exatamente 14 caracteres e ser único; os dígitos verificadores não são validados.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Fornecedor criado"),
			@ApiResponse(responseCode = "400", description = "Payload inválido ou CNPJ sem 14 dígitos"),
			@ApiResponse(responseCode = "409", description = "CNPJ já cadastrado")
	})
	ResponseEntity<FornecedorResponseDto> cadastrar(
			FornecedorRequestDto dto);

	@Operation(summary = "Atualizar fornecedor",
			description = "Altera nome e CNPJ; o estado ativo e a data de criação são preservados.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Fornecedor atualizado"),
			@ApiResponse(responseCode = "400", description = "Payload inválido ou CNPJ sem 14 dígitos"),
			@ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
			@ApiResponse(responseCode = "409", description = "CNPJ já cadastrado a outro fornecedor")
	})
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados atualizados do fornecedor",
			required = true,
			content = @Content(mediaType = "application/json",
					examples = @ExampleObject(name = "atualizacaoDeFornecedor",
							value = """
								{
								  "nome": "Ferragens Silva",
								  "cnpj": "11222333000181"
								}
								""")))
	ResponseEntity<FornecedorResponseDto> atualizar(
			@Parameter(description = "Identificador do fornecedor", example = "1") Integer id,
			FornecedorRequestDto dto);

	@Operation(summary = "Inativar fornecedor",
			description = "Exclusão lógica: define ativo=false; o registro permanece no banco.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Fornecedor inativado"),
			@ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
			@ApiResponse(responseCode = "422", description = "Fornecedor já inativo")
	})
	ResponseEntity<Void> inativar(
			@Parameter(description = "Identificador do fornecedor", example = "2") Integer id);

}
