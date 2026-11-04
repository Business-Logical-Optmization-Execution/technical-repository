package com.portalblox.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioCriacaoRequestDto {

	@Schema(example = "Joana")
	@NotBlank
	@Size(max = 120)
	private String nome;

	@Schema(example = "joana@ottimos.com")
	@NotBlank
	@Email
	@Size(max = 180)
	private String email;

	@Schema(example = "12345678")
	@NotBlank
	@Size(max = 150)
	private String senha;

	@Schema(example = "2")
	@NotNull
	private Integer perfilAcessoId;

	public String getNome() { return nome; }

	public void setNome(String nome) { this.nome = nome; }

	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }

	public String getSenha() { return senha; }

	public void setSenha(String senha) { this.senha = senha; }

	public Integer getPerfilAcessoId() { return perfilAcessoId; }

	public void setPerfilAcessoId(Integer perfilAcessoId) { this.perfilAcessoId = perfilAcessoId; }

}
