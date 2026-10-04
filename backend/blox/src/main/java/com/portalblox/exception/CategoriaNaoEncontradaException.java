package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CategoriaNaoEncontradaException extends RuntimeException {

	public CategoriaNaoEncontradaException() {
		super("Categoria não encontrada.");
	}

}
