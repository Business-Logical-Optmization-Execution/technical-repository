package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class ProdutoInativoException extends RuntimeException {

	public ProdutoInativoException() {
		super("Produto inativo não aceita atualização cadastral nem ajuste de saldo.");
	}

}
