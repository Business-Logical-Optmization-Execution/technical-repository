package com.portalblox.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Linha unica (id = 1) com os parametros globais da loja.
 */
@Entity
@Table(name = "configuracoes")
public class Configuracao {

	@Id
	private Integer id;

	@Column(name = "estoque_minimo_global", nullable = false)
	private Integer estoqueMinimoGlobal;

	@Column(name = "exibir_fotos", nullable = false)
	private Boolean exibirFotos;

	protected Configuracao() {
	}

	public Configuracao(Integer id, Integer estoqueMinimoGlobal, Boolean exibirFotos) {
		this.id = id;
		this.estoqueMinimoGlobal = estoqueMinimoGlobal;
		this.exibirFotos = exibirFotos;
	}

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public Integer getEstoqueMinimoGlobal() { return estoqueMinimoGlobal; }

	public void setEstoqueMinimoGlobal(Integer estoqueMinimoGlobal) {
		this.estoqueMinimoGlobal = estoqueMinimoGlobal;
	}

	public Boolean getExibirFotos() { return exibirFotos; }

	public void setExibirFotos(Boolean exibirFotos) { this.exibirFotos = exibirFotos; }

}
