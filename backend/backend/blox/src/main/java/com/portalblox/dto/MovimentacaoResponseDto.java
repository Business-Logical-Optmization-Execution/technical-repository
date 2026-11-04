package com.portalblox.dto;

import com.portalblox.entity.MotivoMovimentacao;
import com.portalblox.entity.OrigemMovimentacao;
import com.portalblox.entity.TipoMovimentacao;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name = "MovimentacaoResponse",
		description = "Movimentação de estoque registrada, com saldos antes e depois")
public class MovimentacaoResponseDto {

	@Schema(description = "Identificador da movimentação", example = "1")
	private Integer id;

	@Schema(description = "Tipo da movimentação", example = "ENTRADA")
	private TipoMovimentacao tipo;

	@Schema(description = "Variação assinada do saldo", example = "10")
	private Integer quantidade;

	@Schema(description = "Saldo antes da movimentação", example = "0")
	private Integer quantidadeAntes;

	@Schema(description = "Saldo depois da movimentação", example = "10")
	private Integer quantidadeDepois;

	@Schema(description = "Motivo informado, quando houver", example = "AVARIA")
	private MotivoMovimentacao motivo;

	@Schema(description = "Observação auditável", example = "Lâmina quebrada")
	private String observacao;

	@Schema(description = "Documento de referência", example = "NF 12345")
	private String documento;

	@Schema(description = "Origem da movimentação", example = "MANUAL")
	private OrigemMovimentacao origem;

	@Schema(description = "Data e hora da movimentação", example = "2026-10-28T09:12:00")
	private LocalDateTime dataMov;

	private ProdutoResumoResponseDto produto;

	private UsuarioResumoResponseDto usuario;

	public static class ProdutoResumoResponseDto {

		private Integer id;

		private String sku;

		private String nome;

		public Integer getId() { return id; }

		public void setId(Integer id) { this.id = id; }

		public String getSku() { return sku; }

		public void setSku(String sku) { this.sku = sku; }

		public String getNome() { return nome; }

		public void setNome(String nome) { this.nome = nome; }

	}

	public static class UsuarioResumoResponseDto {

		private Integer id;

		private String nome;

		public Integer getId() { return id; }

		public void setId(Integer id) { this.id = id; }

		public String getNome() { return nome; }

		public void setNome(String nome) { this.nome = nome; }

	}

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public TipoMovimentacao getTipo() { return tipo; }

	public void setTipo(TipoMovimentacao tipo) { this.tipo = tipo; }

	public Integer getQuantidade() { return quantidade; }

	public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

	public Integer getQuantidadeAntes() { return quantidadeAntes; }

	public void setQuantidadeAntes(Integer quantidadeAntes) { this.quantidadeAntes = quantidadeAntes; }

	public Integer getQuantidadeDepois() { return quantidadeDepois; }

	public void setQuantidadeDepois(Integer quantidadeDepois) { this.quantidadeDepois = quantidadeDepois; }

	public MotivoMovimentacao getMotivo() { return motivo; }

	public void setMotivo(MotivoMovimentacao motivo) { this.motivo = motivo; }

	public String getObservacao() { return observacao; }

	public void setObservacao(String observacao) { this.observacao = observacao; }

	public String getDocumento() { return documento; }

	public void setDocumento(String documento) { this.documento = documento; }

	public OrigemMovimentacao getOrigem() { return origem; }

	public void setOrigem(OrigemMovimentacao origem) { this.origem = origem; }

	public LocalDateTime getDataMov() { return dataMov; }

	public void setDataMov(LocalDateTime dataMov) { this.dataMov = dataMov; }

	public ProdutoResumoResponseDto getProduto() { return produto; }

	public void setProduto(ProdutoResumoResponseDto produto) { this.produto = produto; }

	public UsuarioResumoResponseDto getUsuario() { return usuario; }

	public void setUsuario(UsuarioResumoResponseDto usuario) { this.usuario = usuario; }

}
