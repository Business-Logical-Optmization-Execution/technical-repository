package com.portalblox.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProdutoExceptionsTest {

	private int statusOf(Class<? extends RuntimeException> exception) {
		ResponseStatus responseStatus = exception.getAnnotation(ResponseStatus.class);
		assertNotNull(responseStatus, exception.getSimpleName() + " deve ter @ResponseStatus");
		return responseStatus.code().value();
	}

	@Test
	void produtoNaoEncontradoRetorna404() {
		assertEquals(HttpStatus.NOT_FOUND.value(), statusOf(ProdutoNaoEncontradoException.class));
	}

	@Test
	void produtoJaExisteRetorna409() {
		assertEquals(HttpStatus.CONFLICT.value(), statusOf(ProdutoJaExisteException.class));
	}

	@Test
	void produtoJaInativoRetorna422() {
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), statusOf(ProdutoJaInativoException.class));
	}

	@Test
	void categoriaNaoEncontradaRetorna404() {
		assertEquals(HttpStatus.NOT_FOUND.value(), statusOf(CategoriaNaoEncontradaException.class));
	}

}
