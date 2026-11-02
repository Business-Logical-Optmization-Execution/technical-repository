package com.portalblox.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

public class FornecedorRequestDto {

	@NotBlank
	@Size(max = 180)
	private String nome;

	@CNPJ
	@NotBlank
	@Size(max = 18)
	private String cnpj;

	public String getNome() { return nome; }

	public void setNome(String nome) { this.nome = nome; }

	public String getCnpj() { return cnpj; }

	public void setCnpj(String cnpj) { this.cnpj = cnpj; }

}
