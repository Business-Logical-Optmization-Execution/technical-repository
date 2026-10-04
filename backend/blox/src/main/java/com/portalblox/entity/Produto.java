package com.portalblox.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produtos")
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String sku;

	private String nome;

	private Integer quantidade;

	private BigDecimal preco;

	private Boolean ativo;

	private LocalDateTime criadoEm;

	private LocalDateTime atualizadoEm;

	@OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProdutoCategoria> categorias = new ArrayList<>();

	// fornecedores_id: aceita NULL no script; fora da v1, não mapear

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

	public String getSku() { return sku; }

	public void setSku(String sku) { this.sku = sku; }

	public String getNome() { return nome; }

	public void setNome(String nome) { this.nome = nome; }

	public Integer getQuantidade() { return quantidade; }

	public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

	public BigDecimal getPreco() { return preco; }

	public void setPreco(BigDecimal preco) { this.preco = preco; }

	public Boolean getAtivo() { return ativo; }

	public void setAtivo(Boolean ativo) { this.ativo = ativo; }

	public LocalDateTime getCriadoEm() { return criadoEm; }

	public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

	public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

	public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

	public List<ProdutoCategoria> getCategorias() { return categorias; }

	public void setCategorias(List<ProdutoCategoria> categorias) { this.categorias = categorias; }

}
