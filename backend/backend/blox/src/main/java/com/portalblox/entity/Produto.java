package com.portalblox.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@Column(nullable = false, length = 60, unique = true)
	private String sku;

	@Column(nullable = false, length = 180)
	private String nome;

	@ManyToOne(optional = false)
	@JoinColumn(name = "categoria_id", nullable = false)
	private Categoria categoria;

	@ManyToOne
	@JoinColumn(name = "fornecedor_id")
	private Fornecedor fornecedor;

	@Column(nullable = false)
	private Integer quantidade;

	@Column(precision = 14, scale = 2)
	private BigDecimal preco;

	@Column(precision = 10, scale = 3)
	private BigDecimal peso;

	@Column(length = 60)
	private String tamanho;

	@Column(length = 500)
	private String descricao;

	@Column(length = 120)
	private String marca;

	@Column(name = "foto_url", length = 500)
	private String fotoUrl;

	@Column(name = "estoque_minimo")
	private Integer estoqueMinimo;

	private Boolean ativo;

	private LocalDateTime criadoEm;

	private LocalDateTime atualizadoEm;

	@OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProdutoLocalizacao> localizacoes = new ArrayList<>();

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

	/** O minimo proprio do produto prevalece; sem ele, vale o minimo global da loja. */
	public static int calcularEstoqueMinimoEfetivo(Integer estoqueMinimo, int estoqueMinimoGlobal) {
		return estoqueMinimo != null ? estoqueMinimo : estoqueMinimoGlobal;
	}

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getSku() { return sku; }

	public void setSku(String sku) { this.sku = sku; }

	public String getNome() { return nome; }

	public void setNome(String nome) { this.nome = nome; }

	public Categoria getCategoria() { return categoria; }

	public void setCategoria(Categoria categoria) { this.categoria = categoria; }

	public Fornecedor getFornecedor() { return fornecedor; }

	public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

	public Integer getQuantidade() { return quantidade; }

	public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

	public BigDecimal getPreco() { return preco; }

	public void setPreco(BigDecimal preco) { this.preco = preco; }

	public BigDecimal getPeso() { return peso; }

	public void setPeso(BigDecimal peso) { this.peso = peso; }

	public String getTamanho() { return tamanho; }

	public void setTamanho(String tamanho) { this.tamanho = tamanho; }

	public String getDescricao() { return descricao; }

	public void setDescricao(String descricao) { this.descricao = descricao; }

	public String getMarca() { return marca; }

	public void setMarca(String marca) { this.marca = marca; }

	public String getFotoUrl() { return fotoUrl; }

	public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

	public Integer getEstoqueMinimo() { return estoqueMinimo; }

	public void setEstoqueMinimo(Integer estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }

	public Boolean getAtivo() { return ativo; }

	public void setAtivo(Boolean ativo) { this.ativo = ativo; }

	public LocalDateTime getCriadoEm() { return criadoEm; }

	public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

	public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

	public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

	public List<ProdutoLocalizacao> getLocalizacoes() { return localizacoes; }

	public void setLocalizacoes(List<ProdutoLocalizacao> localizacoes) { this.localizacoes = localizacoes; }

}
