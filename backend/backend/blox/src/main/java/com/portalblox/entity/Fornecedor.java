package com.portalblox.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "fornecedores")
public class Fornecedor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 180)
	private String nome;

	@Column(nullable = false, length = 14, unique = true)
	private String cnpj;

	private Boolean ativo;

	private LocalDateTime criadoEm;

	private LocalDateTime atualizadoEm;

	@PrePersist
	public void aoCriar() {
		LocalDateTime agora = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		this.criadoEm = agora;
		this.atualizadoEm = agora;
	}

	@PreUpdate
	public void aoAtualizar() {
		this.atualizadoEm = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getNome() { return nome; }

	public void setNome(String nome) { this.nome = nome; }

	public String getCnpj() { return cnpj; }

	public void setCnpj(String cnpj) { this.cnpj = cnpj; }

	public Boolean getAtivo() { return ativo; }

	public void setAtivo(Boolean ativo) { this.ativo = ativo; }

	public LocalDateTime getCriadoEm() { return criadoEm; }

	public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

	public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

	public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

}
