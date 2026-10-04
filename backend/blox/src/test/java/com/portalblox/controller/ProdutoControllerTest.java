package com.portalblox.controller;

import com.portalblox.entity.Categoria;
import com.portalblox.entity.Produto;
import com.portalblox.entity.ProdutoCategoria;
import com.portalblox.exception.ProdutoJaExisteException;
import com.portalblox.exception.ProdutoJaInativoException;
import com.portalblox.exception.ProdutoNaoEncontradoException;
import com.portalblox.service.ProdutoService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

	@MockitoBean
	private ProdutoService produtoService;

	@Autowired
	private MockMvc mockMvc;

	private static final String POST_VALIDO = """
			{"sku":"SKU-1","nome":"Parafuso M8","quantidade":10,"preco":19.90,"categoriaIds":[1]}
			""";

	private static final String POST_INVALIDO = """
			{"nome":"","quantidade":-1,"categoriaIds":[1]}
			""";

	private static final String PUT_VALIDO = """
			{"sku":"SKU-1","nome":"Parafuso M8","preco":19.90,"categoriaIds":[1]}
			""";

	private static final String PUT_INVALIDO = """
			{"sku":"SKU-1","categoriaIds":[1]}
			""";

	private static final String PATCH_VALIDO = """
			{"quantidade":25}
			""";

	private static final String PATCH_INVALIDO = """
			{"quantidade":-1}
			""";

	private Produto produtoAtivo() {
		Produto produto = new Produto();
		produto.setId(1);
		produto.setSku("SKU-1");
		produto.setNome("Parafuso M8");
		produto.setQuantidade(10);
		produto.setPreco(new BigDecimal("19.90"));
		produto.setAtivo(true);
		Categoria categoria = new Categoria();
		categoria.setId(1);
		categoria.setNome("Ferragens");
		produto.getCategorias().add(new ProdutoCategoria(produto, categoria));
		return produto;
	}

	// ------------------------------------------------------------- GET

	@Test
	void listarRetorna200ComSomenteAtivosPorPadrao() throws Exception {
		when(produtoService.listar(false)).thenReturn(List.of(produtoAtivo()));

		mockMvc.perform(get("/produtos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].sku").value("SKU-1"));

		verify(produtoService).listar(false);
	}

	@Test
	void listarComIncluirInativosRetorna200() throws Exception {
		when(produtoService.listar(true)).thenReturn(List.of(produtoAtivo()));

		mockMvc.perform(get("/produtos").param("incluirInativos", "true"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1));

		verify(produtoService).listar(true);
	}

	@Test
	void buscarPorIdRetorna200ComCategorias() throws Exception {
		when(produtoService.buscarPorId(1)).thenReturn(produtoAtivo());

		mockMvc.perform(get("/produtos/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.quantidade").value(10))
				.andExpect(jsonPath("$.categorias[0].nome").value("Ferragens"));
	}

	@Test
	void buscarPorIdInexistenteRetorna404() throws Exception {
		when(produtoService.buscarPorId(99)).thenThrow(new ProdutoNaoEncontradoException());

		mockMvc.perform(get("/produtos/99"))
				.andExpect(status().isNotFound());
	}

	// ------------------------------------------------------------- POST

	@Test
	void cadastrarRetorna201ComAtivoECategorias() throws Exception {
		when(produtoService.cadastrar(any(Produto.class), anyList())).thenReturn(produtoAtivo());

		mockMvc.perform(post("/produtos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(POST_VALIDO))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.quantidade").value(10))
				.andExpect(jsonPath("$.ativo").value(true))
				.andExpect(jsonPath("$.categorias[0].nome").value("Ferragens"));
	}

	@Test
	void cadastrarPayloadInvalidoRetorna400() throws Exception {
		mockMvc.perform(post("/produtos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(POST_INVALIDO))
				.andExpect(status().isBadRequest());

		verify(produtoService, never()).cadastrar(any(Produto.class), anyList());
	}

	@Test
	void cadastrarSkuDuplicadoRetorna409() throws Exception {
		when(produtoService.cadastrar(any(Produto.class), anyList()))
				.thenThrow(new ProdutoJaExisteException());

		mockMvc.perform(post("/produtos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(POST_VALIDO))
				.andExpect(status().isConflict());
	}

	// ------------------------------------------------------------- PUT

	@Test
	void atualizarRetorna200PreservandoQuantidade() throws Exception {
		when(produtoService.atualizar(eq(1), any(Produto.class), anyList()))
				.thenReturn(produtoAtivo());

		mockMvc.perform(put("/produtos/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(PUT_VALIDO))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.quantidade").value(10))
				.andExpect(jsonPath("$.ativo").value(true));
	}

	@Test
	void atualizarPayloadInvalidoRetorna400() throws Exception {
		mockMvc.perform(put("/produtos/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(PUT_INVALIDO))
				.andExpect(status().isBadRequest());

		verify(produtoService, never()).atualizar(any(), any(Produto.class), anyList());
	}

	@Test
	void atualizarProdutoInexistenteRetorna404() throws Exception {
		when(produtoService.atualizar(eq(99), any(Produto.class), anyList()))
				.thenThrow(new ProdutoNaoEncontradoException());

		mockMvc.perform(put("/produtos/99")
						.contentType(MediaType.APPLICATION_JSON)
						.content(PUT_VALIDO))
				.andExpect(status().isNotFound());
	}

	@Test
	void atualizarSkuDeOutroProdutoRetorna409() throws Exception {
		when(produtoService.atualizar(eq(1), any(Produto.class), anyList()))
				.thenThrow(new ProdutoJaExisteException());

		mockMvc.perform(put("/produtos/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(PUT_VALIDO))
				.andExpect(status().isConflict());
	}

	// ------------------------------------------------------------- PATCH

	@Test
	void ajustarQuantidadeRetorna200ComNovaQuantidade() throws Exception {
		Produto produto = produtoAtivo();
		produto.setQuantidade(25);
		when(produtoService.ajustarQuantidade(1, 25)).thenReturn(produto);

		mockMvc.perform(patch("/produtos/1/quantidade")
						.contentType(MediaType.APPLICATION_JSON)
						.content(PATCH_VALIDO))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.quantidade").value(25))
				.andExpect(jsonPath("$.sku").value("SKU-1"));
	}

	@Test
	void ajustarQuantidadeNegativaRetorna400() throws Exception {
		mockMvc.perform(patch("/produtos/1/quantidade")
						.contentType(MediaType.APPLICATION_JSON)
						.content(PATCH_INVALIDO))
				.andExpect(status().isBadRequest());

		verify(produtoService, never()).ajustarQuantidade(any(), any());
	}

	@Test
	void ajustarQuantidadeProdutoInexistenteRetorna404() throws Exception {
		when(produtoService.ajustarQuantidade(99, 25))
				.thenThrow(new ProdutoNaoEncontradoException());

		mockMvc.perform(patch("/produtos/99/quantidade")
						.contentType(MediaType.APPLICATION_JSON)
						.content(PATCH_VALIDO))
				.andExpect(status().isNotFound());
	}

	// ------------------------------------------------------------- DELETE

	@Test
	void inativarRetorna204() throws Exception {
		mockMvc.perform(delete("/produtos/1"))
				.andExpect(status().isNoContent());

		verify(produtoService).inativar(1);
	}

	@Test
	void inativarProdutoJaInativoRetorna422() throws Exception {
		org.mockito.Mockito.doThrow(new ProdutoJaInativoException())
				.when(produtoService).inativar(1);

		mockMvc.perform(delete("/produtos/1"))
				.andExpect(status().isUnprocessableEntity());
	}

	@Test
	void inativarProdutoInexistenteRetorna404() throws Exception {
		org.mockito.Mockito.doThrow(new ProdutoNaoEncontradoException())
				.when(produtoService).inativar(99);

		mockMvc.perform(delete("/produtos/99"))
				.andExpect(status().isNotFound());
	}

}
