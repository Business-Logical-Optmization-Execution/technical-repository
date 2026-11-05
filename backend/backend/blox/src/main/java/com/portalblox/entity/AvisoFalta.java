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
@Table(name = "avisos_falta")
public class AvisoFalta {

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
	private StatusAviso status;

	@Column(length = 255)
	private String observacao;

	@Column(name = "quantidade_avisos", nullable = false)
	private Integer quantidadeAvisos;

	@Column(name = "criado_em", nullable = false)
	private LocalDateTime criadoEm;

	protected AvisoFalta() {
	}

	public AvisoFalta(Produto produto, Usuario usuario, String observacao) {
		this.produto = produto;
		this.usuario = usuario;
		this.observacao = observacao;
		this.status = StatusAviso.ABERTO;
		this.quantidadeAvisos = 1;
	}

	@PrePersist
	public void aoCriar() {
		// Segundos, como na coluna DATETIME: POST e GET devolvem o mesmo criadoEm.
		this.criadoEm = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}

	public void somarAviso() {
		this.quantidadeAvisos++;
	}

	public void resolver() {
		this.status = StatusAviso.RESOLVIDO;
	}

	public boolean foiSomado() {
		return quantidadeAvisos > 1;
	}

	public Integer getId() { return id; }

	public Produto getProduto() { return produto; }

	public Usuario getUsuario() { return usuario; }

	public StatusAviso getStatus() { return status; }

	public String getObservacao() { return observacao; }

	public Integer getQuantidadeAvisos() { return quantidadeAvisos; }

	public LocalDateTime getCriadoEm() { return criadoEm; }

}
