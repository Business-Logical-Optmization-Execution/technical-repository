package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class UsuarioInativoException extends RuntimeException {

	public UsuarioInativoException() {
		this("Usuário inativo não pode ser autor de movimentação de estoque.");
	}

	public UsuarioInativoException(String mensagem) {
		super(mensagem);
	}

}
