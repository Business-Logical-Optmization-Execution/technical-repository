package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FornecedorNaoEncontradoException extends RuntimeException {

	public FornecedorNaoEncontradoException() {
		super("Fornecedor não encontrado.");
	}

}
