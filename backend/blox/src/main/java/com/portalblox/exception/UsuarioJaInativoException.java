package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class UsuarioJaInativoException extends RuntimeException {

	public UsuarioJaInativoException() {
		super("Usuário já está inativo.");
	}

}
