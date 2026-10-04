package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class ProdutoJaInativoException extends RuntimeException {

	public ProdutoJaInativoException() {
		super("Produto já está inativo.");
	}

}
