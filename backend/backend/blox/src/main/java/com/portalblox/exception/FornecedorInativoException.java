package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class FornecedorInativoException extends RuntimeException {

	public FornecedorInativoException() {
		super("Fornecedor inativo não pode ser vinculado a um produto.");
	}

}
