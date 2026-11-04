package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class UltimoAdministradorException extends RuntimeException {

	public UltimoAdministradorException() {
		super("Não é possível desativar o último administrador ativo.");
	}

}
