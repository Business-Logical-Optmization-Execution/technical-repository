package com.portalblox.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "produto_localizacoes")
public class ProdutoLocalizacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	@Column(nullable = false, length = 120)
	private String nome;

	protected ProdutoLocalizacao() {
	}

	public ProdutoLocalizacao(Produto produto, String nome) {
		this.produto = produto;
		this.nome = nome;
	}

	public Integer getId() { return id; }

	public Produto getProduto() { return produto; }

	public void setProduto(Produto produto) { this.produto = produto; }

	public String getNome() { return nome; }

	public void setNome(String nome) { this.nome = nome; }

}
