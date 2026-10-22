package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ProdutoNaoEncontradoException extends RuntimeException {

	public ProdutoNaoEncontradoException() {
		super("Produto não encontrado.");
	}

}
