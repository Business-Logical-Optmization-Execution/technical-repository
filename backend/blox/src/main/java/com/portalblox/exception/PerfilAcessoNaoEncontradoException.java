package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PerfilAcessoNaoEncontradoException extends RuntimeException {

	public PerfilAcessoNaoEncontradoException() {
		super("Perfil de acesso não encontrado.");
	}

}
