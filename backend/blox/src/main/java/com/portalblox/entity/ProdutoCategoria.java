package com.portalblox.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "produtos_categorias")
public class ProdutoCategoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "produtos_id", nullable = false)
	private Produto produto;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "categorias_id", nullable = false)
	private Categoria categoria;

	protected ProdutoCategoria() {
	}

	public ProdutoCategoria(Produto produto, Categoria categoria) {
		this.produto = produto;
		this.categoria = categoria;
	}

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public Produto getProduto() { return produto; }

	public void setProduto(Produto produto) { this.produto = produto; }

	public Categoria getCategoria() { return categoria; }

	public void setCategoria(Categoria categoria) { this.categoria = categoria; }

}
