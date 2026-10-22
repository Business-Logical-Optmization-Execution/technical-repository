package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UsuarioJaExisteException extends RuntimeException {

	public UsuarioJaExisteException() {
		super("Usuário já cadastrado com este e-mail.");
	}

}
