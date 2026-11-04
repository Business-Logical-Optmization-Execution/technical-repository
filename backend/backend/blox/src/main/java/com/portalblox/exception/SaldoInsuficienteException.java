package com.portalblox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class SaldoInsuficienteException extends RuntimeException {

	public SaldoInsuficienteException(int quantidade, int saldo) {
		super("Não dá para tirar " + quantidade + " un.: a quantidade atual é " + saldo + " un.");
	}

}
