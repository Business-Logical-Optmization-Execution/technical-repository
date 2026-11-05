package com.portalblox.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Violacao de campo detectada na validacao de entrada ou na regra de negocio.
 * Guarda campo e mensagem na ordem de insercao e resulta em 400 com erros[].campo.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ValidacaoException extends RuntimeException {

	private final Map<String, String> erros;

	public ValidacaoException(String mensagem, Map<String, String> erros) {
		super(mensagem);
		this.erros = erros == null ? Map.of() : Collections.unmodifiableMap(new LinkedHashMap<>(erros));
	}

	public ValidacaoException(String campo, String mensagem) {
		this("Confira os campos destacados.", Map.of(campo, mensagem));
	}

	public Map<String, String> getErros() { return erros; }

}
