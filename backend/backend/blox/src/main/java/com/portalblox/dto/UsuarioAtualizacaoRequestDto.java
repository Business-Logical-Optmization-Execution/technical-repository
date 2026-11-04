package com.portalblox.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioAtualizacaoRequestDto {

	@Schema(example = "Ronaldo")
	@NotBlank
	@Size(max = 120)
	private String nome;

	@Schema(example = "ronaldo@ottimos.com")
	@NotBlank
	@Email
	@Size(max = 180)
	private String email;

	@Schema(example = "2")
	@NotNull
	private Integer perfilAcessoId;

	public String getNome() { return nome; }

	public void setNome(String nome) { this.nome = nome; }

	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }

	public Integer getPerfilAcessoId() { return perfilAcessoId; }

	public void setPerfilAcessoId(Integer perfilAcessoId) { this.perfilAcessoId = perfilAcessoId; }

}
