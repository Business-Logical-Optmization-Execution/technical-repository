package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AvisoFaltaNaoEncontradoException extends RuntimeException {

	public AvisoFaltaNaoEncontradoException() {
		super("Aviso de falta não encontrado.");
	}

}
