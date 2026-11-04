package com.portalblox.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "LoginRequest", description = "Credenciais de acesso do usuário")
public class LoginRequestDto {

	@Schema(description = "E-mail cadastrado", example = "marcia@ottimos.com")
	@NotBlank(message = "Informe o e-mail.")
	@Email(message = "Informe um e-mail válido.")
	@Size(max = 180, message = "O e-mail deve ter no máximo 180 caracteres.")
	private String email;

	@Schema(description = "Senha do usuário", example = "123456")
	@NotBlank(message = "Informe a senha.")
	@Size(max = 150, message = "A senha deve ter no máximo 150 caracteres.")
	private String senha;

	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }

	public String getSenha() { return senha; }

	public void setSenha(String senha) { this.senha = senha; }

}
