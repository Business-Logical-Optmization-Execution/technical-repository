package com.portalblox.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "movimentacoes_estoque")
public class MovimentacaoEstoque {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	@ManyToOne(optional = false)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(nullable = false, length = 20)
	private TipoMovimentacao tipo;

	@Column(nullable = false)
	private Integer quantidade;

	@Column(nullable = false)
	private Integer variacao;

	@Column(name = "quantidade_antes", nullable = false)
	private Integer quantidadeAntes;

	@Column(name = "quantidade_depois", nullable = false)
	private Integer quantidadeDepois;

	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(length = 20)
	private MotivoMovimentacao motivo;

	@Column(length = 255)
	private String observacao;

	@Column(length = 120)
	private String documento;

	@Column(length = 255)
	private String justificativa;

	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(nullable = false, length = 20)
	private OrigemMovimentacao origem;

	@ManyToOne
	@JoinColumn(name = "estorno_de_id")
	private MovimentacaoEstoque estornoDe;

	@Column(nullable = false)
	private LocalDateTime criadoEm;

	protected MovimentacaoEstoque() {
	}

	public MovimentacaoEstoque(Produto produto, Usuario usuario, TipoMovimentacao tipo,
			Integer quantidade, Integer variacao, Integer quantidadeAntes, Integer quantidadeDepois,
			MotivoMovimentacao motivo, String observacao, String documento,
			OrigemMovimentacao origem) {
		this.produto = produto;
		this.usuario = usuario;
		this.tipo = tipo;
		this.quantidade = quantidade;
		this.variacao = variacao;
		this.quantidadeAntes = quantidadeAntes;
		this.quantidadeDepois = quantidadeDepois;
		this.motivo = motivo;
		this.observacao = observacao;
		this.documento = documento;
		this.justificativa = null;
		this.origem = origem;
	}

	@PrePersist
	public void aoCriar() {
		// Segundos, como na coluna DATETIME: POST e GET devolvem o mesmo dataMov.
		this.criadoEm = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}

	public Integer getId() { return id; }

	public Produto getProduto() { return produto; }

	public Usuario getUsuario() { return usuario; }

	public TipoMovimentacao getTipo() { return tipo; }

	public Integer getQuantidade() { return quantidade; }

	public Integer getVariacao() { return variacao; }

	public Integer getQuantidadeAntes() { return quantidadeAntes; }

	public Integer getQuantidadeDepois() { return quantidadeDepois; }

	public MotivoMovimentacao getMotivo() { return motivo; }

	public String getObservacao() { return observacao; }

	public String getDocumento() { return documento; }

	public String getJustificativa() { return justificativa; }

	public OrigemMovimentacao getOrigem() { return origem; }

	public MovimentacaoEstoque getEstornoDe() { return estornoDe; }

	public LocalDateTime getCriadoEm() { return criadoEm; }

}
