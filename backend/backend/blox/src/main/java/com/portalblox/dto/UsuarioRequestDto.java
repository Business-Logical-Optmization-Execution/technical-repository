package com.portalblox.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioRequestDto {

	@NotBlank
	@Size(max = 120)
	private String nome;

	@NotBlank
	@Email
	@Size(max = 180)
	private String email;

	@NotBlank
	@Size(max = 150)
	private String senha;

	@NotNull
	private Integer perfilAcessoId;             // relacionamento por id

	public String getNome() { return nome; }

	public void setNome(String nome) { this.nome = nome; }

	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }

	public String getSenha() { return senha; }

	public void setSenha(String senha) { this.senha = senha; }

	public Integer getPerfilAcessoId() { return perfilAcessoId; }

	public void setPerfilAcessoId(Integer perfilAcessoId) { this.perfilAcessoId = perfilAcessoId; }

}
