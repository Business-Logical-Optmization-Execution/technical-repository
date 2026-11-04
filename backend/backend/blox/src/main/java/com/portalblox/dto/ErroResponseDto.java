package com.portalblox.dto;

import java.util.List;

/**
 * Corpo padronizado de erro da API: { status, mensagem, erros }.
 * Nunca expoe stack trace, SQL ou nomes internos de tabela/coluna.
 */
public class ErroResponseDto {

	private int status;

	private String mensagem;

	private List<ErroCampo> erros;

	public ErroResponseDto() {
	}

	public ErroResponseDto(int status, String mensagem, List<ErroCampo> erros) {
		this.status = status;
		this.mensagem = mensagem;
		this.erros = erros == null ? List.of() : List.copyOf(erros);
	}

	public static ErroResponseDto semCampos(int status, String mensagem) {
		return new ErroResponseDto(status, mensagem, List.of());
	}

	public static ErroResponseDto comCampos(int status, String mensagem, List<ErroCampo> erros) {
		return new ErroResponseDto(status, mensagem, erros);
	}

	public int getStatus() { return status; }

	public void setStatus(int status) { this.status = status; }

	public String getMensagem() { return mensagem; }

	public void setMensagem(String mensagem) { this.mensagem = mensagem; }

	public List<ErroCampo> getErros() { return erros; }

	public void setErros(List<ErroCampo> erros) {
		this.erros = erros == null ? List.of() : erros;
	}

	public static class ErroCampo {

		private String campo;

		private String mensagem;

		public ErroCampo() {
		}

		public ErroCampo(String campo, String mensagem) {
			this.campo = campo;
			this.mensagem = mensagem;
		}

		public String getCampo() { return campo; }

		public void setCampo(String campo) { this.campo = campo; }

		public String getMensagem() { return mensagem; }

		public void setMensagem(String mensagem) { this.mensagem = mensagem; }

	}

}
