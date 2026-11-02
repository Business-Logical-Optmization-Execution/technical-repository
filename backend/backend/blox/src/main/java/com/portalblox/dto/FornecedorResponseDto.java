package com.portalblox.dto;

import java.time.LocalDateTime;

public class FornecedorResponseDto {

	private Integer id;

	private String nome;

	private String cnpj;

	private Boolean ativo;

	private LocalDateTime criadoEm;

	private LocalDateTime atualizadoEm;

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
