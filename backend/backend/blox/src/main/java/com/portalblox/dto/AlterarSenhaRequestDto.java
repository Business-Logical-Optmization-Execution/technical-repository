package com.portalblox.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AlterarSenhaRequestDto {

	@Schema(description = "Senha atual; sem ela, a senha é redefinida sem conferência", example = "123456")
	private String senhaAtual;

	@Schema(example = "novaSenha123")
	@NotBlank(message = "Informe a nova senha.")
	@Size(min = 8, max = 150, message = "Use de 8 a 150 caracteres.")
	private String novaSenha;

	public String getSenhaAtual() { return senhaAtual; }

	public void setSenhaAtual(String senhaAtual) { this.senhaAtual = senhaAtual; }

	public String getNovaSenha() { return novaSenha; }

	public void setNovaSenha(String novaSenha) { this.novaSenha = novaSenha; }

}
