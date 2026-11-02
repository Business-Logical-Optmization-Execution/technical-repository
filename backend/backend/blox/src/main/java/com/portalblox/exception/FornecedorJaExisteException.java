package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class FornecedorJaExisteException extends RuntimeException {

	public FornecedorJaExisteException() {
		super("CNPJ já cadastrado para outro fornecedor.");
	}

}
