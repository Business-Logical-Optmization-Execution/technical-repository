package com.portalblox.exception;

import com.portalblox.dto.ErroResponseDto;
import com.portalblox.dto.ErroResponseDto.ErroCampo;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Violacao de campo detectada na camada de servico (regras que dependem do
 * tipo da movimentacao, por exemplo). Resulta em 400 com erros[].campo.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ValidacaoException extends RuntimeException {

	private final List<ErroCampo> erros;

	public ValidacaoException(String mensagem, List<ErroCampo> erros) {
		super(mensagem);
		this.erros = erros == null ? List.of() : List.copyOf(erros);
	}

	public ValidacaoException(String campo, String mensagem) {
		this("Confira os campos destacados.", List.of(new ErroCampo(campo, mensagem)));
	}

	public List<ErroCampo> getErros() { return erros; }

}
