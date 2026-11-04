package com.portalblox.exception;

import com.portalblox.dto.ErroResponseDto;
import com.portalblox.dto.ErroResponseDto.ErroCampo;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tools.jackson.databind.exc.MismatchedInputException;

/**
 * Tratamento global de erros: todas as respostas de falha usam o corpo
 * { status, mensagem, erros } e nunca expoem detalhes internos.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	private static final String MENSAGEM_VALIDACAO = "Confira os campos destacados.";
	private static final String MENSAGEM_INTERNA = "Erro interno inesperado. Tente novamente mais tarde.";

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroResponseDto> tratarValidacaoDeCampos(MethodArgumentNotValidException excecao) {
		List<ErroCampo> erros = excecao.getBindingResult().getFieldErrors().stream()
				.map(erro -> new ErroCampo(erro.getField(), mensagemOuPadrao(erro.getDefaultMessage())))
				.toList();
		return resposta(HttpStatus.BAD_REQUEST, MENSAGEM_VALIDACAO, erros);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErroResponseDto> tratarCorpoIlegivel(HttpMessageNotReadableException excecao) {
		if (excecao.getMostSpecificCause() instanceof MismatchedInputException causa && !causa.getPath().isEmpty()) {
			String campo = causa.getPath().get(causa.getPath().size() - 1).getPropertyName();
			if (campo != null) {
				return resposta(HttpStatus.BAD_REQUEST, MENSAGEM_VALIDACAO,
						List.of(new ErroCampo(campo, mensagemDeTipo(causa.getTargetType()))));
			}
		}
		List<ErroCampo> erros = List.of(new ErroCampo("corpo",
				"Envie um JSON válido com os campos e valores esperados."));
		return resposta(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido.", erros);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErroResponseDto> tratarParametroInvalido(MethodArgumentTypeMismatchException excecao) {
		String campo = excecao.getName() == null ? "parametro" : excecao.getName();
		List<ErroCampo> erros = List.of(new ErroCampo(campo, "Informe um valor válido."));
		return resposta(HttpStatus.BAD_REQUEST, "Parâmetro inválido.", erros);
	}

	@ExceptionHandler(ValidacaoException.class)
	public ResponseEntity<ErroResponseDto> tratarValidacaoDeServico(ValidacaoException excecao) {
		return resposta(HttpStatus.BAD_REQUEST, mensagemOuPadrao(excecao.getMessage()), excecao.getErros());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErroResponseDto> tratarIntegridade(DataIntegrityViolationException excecao) {
		if (DetectorChaveDuplicada.ehChaveDuplicada(excecao)) {
			return resposta(HttpStatus.CONFLICT, "Já existe um registro com este valor.", List.of());
		}
		return resposta(HttpStatus.BAD_REQUEST, "Violação de integridade dos dados.", List.of());
	}

	@ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
	public ResponseEntity<ErroResponseDto> tratarRotaInexistente(Exception excecao) {
		return resposta(HttpStatus.NOT_FOUND, "Rota não encontrada.", List.of());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErroResponseDto> tratarMetodoNaoSuportado(HttpRequestMethodNotSupportedException excecao) {
		return resposta(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP não suportado para esta rota.", List.of());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErroResponseDto> tratarRuntimeException(RuntimeException excecao) {
		ResponseStatus anotacao = excecao.getClass().getAnnotation(ResponseStatus.class);
		if (anotacao != null) {
			return resposta(anotacao.code(), mensagemOuPadrao(excecao.getMessage()), List.of());
		}
		log.error("Erro não tratado na API", excecao);
		return resposta(HttpStatus.INTERNAL_SERVER_ERROR, MENSAGEM_INTERNA, List.of());
	}

	private ResponseEntity<ErroResponseDto> resposta(HttpStatus status, String mensagem, List<ErroCampo> erros) {
		return ResponseEntity.status(status)
				.body(ErroResponseDto.comCampos(status.value(), mensagem, erros));
	}

	private String mensagemDeTipo(Class<?> tipo) {
		boolean inteiro = tipo == Integer.class || tipo == int.class || tipo == Long.class || tipo == long.class;
		return inteiro ? "Use um número inteiro." : "Informe um valor válido.";
	}

	private String mensagemOuPadrao(String mensagem) {
		return mensagem == null || mensagem.isBlank() ? "Não foi possível processar a solicitação." : mensagem;
	}

}
