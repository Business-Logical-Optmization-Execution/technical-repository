package com.portalblox.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UsuarioExceptionsTest {

	private int statusOf(Class<? extends RuntimeException> exception) {
		ResponseStatus responseStatus = exception.getAnnotation(ResponseStatus.class);
		assertNotNull(responseStatus, exception.getSimpleName() + " deve ter @ResponseStatus");
		return responseStatus.code().value();
	}

	@Test
	void usuarioNaoEncontradoRetorna404() {
		assertEquals(HttpStatus.NOT_FOUND.value(), statusOf(UsuarioNaoEncontradoException.class));
	}

	@Test
	void usuarioJaExisteRetorna409() {
		assertEquals(HttpStatus.CONFLICT.value(), statusOf(UsuarioJaExisteException.class));
	}

	@Test
	void usuarioJaInativoRetorna422() {
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), statusOf(UsuarioJaInativoException.class));
	}

	@Test
	void perfilAcessoNaoEncontradoRetorna404() {
		assertEquals(HttpStatus.NOT_FOUND.value(), statusOf(PerfilAcessoNaoEncontradoException.class));
	}

}
