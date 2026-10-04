package com.portalblox.dto;

public class UsuarioResponseDto {

	private Integer id;

	private String nome;

	private String email;

	private PerfilAcessoResponseDto perfilAcesso;

	private Boolean ativo;                      // sem o campo senha (RN-07)

	public static class PerfilAcessoResponseDto {

		private Integer id;

		private String nome;

		public Integer getId() { return id; }

		public void setId(Integer id) { this.id = id; }

		public String getNome() { return nome; }

		public void setNome(String nome) { this.nome = nome; }

	}

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getNome() { return nome; }

	public void setNome(String nome) { this.nome = nome; }

	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }

	public PerfilAcessoResponseDto getPerfilAcesso() { return perfilAcesso; }

	public void setPerfilAcesso(PerfilAcessoResponseDto perfilAcesso) { this.perfilAcesso = perfilAcesso; }

	public Boolean getAtivo() { return ativo; }

	public void setAtivo(Boolean ativo) { this.ativo = ativo; }

}
