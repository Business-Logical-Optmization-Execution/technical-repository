package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ProdutoJaExisteException extends RuntimeException {

	public ProdutoJaExisteException() {
		super("Produto já cadastrado com este SKU.");
	}

}
