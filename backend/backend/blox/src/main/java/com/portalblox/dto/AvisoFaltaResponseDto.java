package com.portalblox.dto;

import com.portalblox.entity.StatusAviso;
import java.time.LocalDateTime;

public class AvisoFaltaResponseDto {

	private Integer id;

	private StatusAviso status;

	private String observacao;

	private Integer quantidadeAvisos;

	private boolean somado;

	private LocalDateTime criadoEm;

	private MovimentacaoResponseDto.ProdutoResumoResponseDto produto;

	private MovimentacaoResponseDto.UsuarioResumoResponseDto usuario;

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public StatusAviso getStatus() { return status; }

	public void setStatus(StatusAviso status) { this.status = status; }

	public String getObservacao() { return observacao; }

	public void setObservacao(String observacao) { this.observacao = observacao; }

	public Integer getQuantidadeAvisos() { return quantidadeAvisos; }

	public void setQuantidadeAvisos(Integer quantidadeAvisos) { this.quantidadeAvisos = quantidadeAvisos; }

	public boolean isSomado() { return somado; }

	public void setSomado(boolean somado) { this.somado = somado; }

	public LocalDateTime getCriadoEm() { return criadoEm; }

	public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

	public MovimentacaoResponseDto.ProdutoResumoResponseDto getProduto() { return produto; }

	public void setProduto(MovimentacaoResponseDto.ProdutoResumoResponseDto produto) { this.produto = produto; }

	public MovimentacaoResponseDto.UsuarioResumoResponseDto getUsuario() { return usuario; }

	public void setUsuario(MovimentacaoResponseDto.UsuarioResumoResponseDto usuario) { this.usuario = usuario; }

}
