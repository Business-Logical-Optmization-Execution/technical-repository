package com.portalblox.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;

	private String email;

	private String senha;                       // sem hash por enquanto; nunca na resposta (RN-07)

	@ManyToOne
	@JoinColumn(name = "perfil_acesso_id")
	private PerfilAcesso perfilAcesso;

	private Boolean ativo;

	private LocalDateTime criadoEm;

	private LocalDateTime atualizadoEm;

	@PrePersist
	public void aoCriar() {
		LocalDateTime agora = LocalDateTime.now();
		this.criadoEm = agora;
		this.atualizadoEm = agora;
	}

	@PreUpdate
	public void aoAtualizar() {
		this.atualizadoEm = LocalDateTime.now();
	}

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getNome() { return nome; }

	public void setNome(String nome) { this.nome = nome; }

	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }

	public String getSenha() { return senha; }

	public void setSenha(String senha) { this.senha = senha; }

	public PerfilAcesso getPerfilAcesso() { return perfilAcesso; }

	public void setPerfilAcesso(PerfilAcesso perfilAcesso) { this.perfilAcesso = perfilAcesso; }

	public Boolean getAtivo() { return ativo; }

	public void setAtivo(Boolean ativo) { this.ativo = ativo; }

	public LocalDateTime getCriadoEm() { return criadoEm; }

	public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

	public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

	public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

}
