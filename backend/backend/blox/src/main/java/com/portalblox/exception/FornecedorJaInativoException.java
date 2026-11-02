package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class FornecedorJaInativoException extends RuntimeException {

	public FornecedorJaInativoException() {
		super("Fornecedor já está inativo.");
	}

}
