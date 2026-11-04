package com.portalblox.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "MovimentacaoRequest",
		description = "Registro manual de entrada, saída ou ajuste de estoque")
public class MovimentacaoRequestDto {

	@Schema(description = "Produto movimentado", example = "1")
	@NotNull(message = "Informe o produto.")
	private Integer produtoId;

	@Schema(description = "Usuário autor da movimentação", example = "1")
	@NotNull(message = "Informe o usuário.")
	private Integer usuarioId;

	@Schema(description = "Tipo da movimentação", example = "ENTRADA",
			allowableValues = {"ENTRADA", "SAIDA", "AJUSTE"})
	@NotBlank(message = "Informe o tipo da movimentação.")
	private String tipo;

	@Schema(description = "Quantidade: maior que zero em ENTRADA/SAIDA e novo saldo em AJUSTE",
			example = "5")
	@NotNull(message = "Informe a quantidade.")
	private Integer quantidade;

	@Schema(description = "Motivo obrigatório em SAIDA e opcional em ENTRADA/AJUSTE",
			example = "AVARIA",
			allowableValues = {"PERDA", "AVARIA", "USO_INTERNO", "VENDA_BALCAO", "OUTRO"})
	private String motivo;

	@Schema(description = "Observação auditável (até 255 caracteres)", example = "Lâmina quebrada")
	@Size(max = 255, message = "A observação deve ter no máximo 255 caracteres.")
	private String observacao;

	@Schema(description = "Documento de referência (até 120 caracteres)", example = "NF 12345")
	@Size(max = 120, message = "O documento deve ter no máximo 120 caracteres.")
	private String documento;

	public Integer getProdutoId() { return produtoId; }

	public void setProdutoId(Integer produtoId) { this.produtoId = produtoId; }

	public Integer getUsuarioId() { return usuarioId; }

	public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

	public String getTipo() { return tipo; }

	public void setTipo(String tipo) { this.tipo = tipo; }

	public Integer getQuantidade() { return quantidade; }

	public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

	public String getMotivo() { return motivo; }

	public void setMotivo(String motivo) { this.motivo = motivo; }

	public String getObservacao() { return observacao; }

	public void setObservacao(String observacao) { this.observacao = observacao; }

	public String getDocumento() { return documento; }

	public void setDocumento(String documento) { this.documento = documento; }

}
