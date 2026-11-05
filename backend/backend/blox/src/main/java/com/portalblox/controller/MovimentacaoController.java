package com.portalblox.controller;

import com.portalblox.controller.docs.MovimentacaoApi;
import com.portalblox.dto.MovimentacaoFiltro;
import com.portalblox.dto.MovimentacaoRequestDto;
import com.portalblox.dto.MovimentacaoResponseDto;
import com.portalblox.entity.MovimentacaoEstoque;
import com.portalblox.mapper.MovimentacaoMapper;
import com.portalblox.service.MovimentacaoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController implements MovimentacaoApi {

	private final MovimentacaoService movimentacaoService;

	public MovimentacaoController(MovimentacaoService movimentacaoService) {
		this.movimentacaoService = movimentacaoService;
	}

	@PostMapping
	public ResponseEntity<MovimentacaoResponseDto> criar(@Valid @RequestBody MovimentacaoRequestDto dto) {
		MovimentacaoEstoque movimentacao = movimentacaoService.criar(dto.getProdutoId(), dto.getUsuarioId(),
				dto.getTipo(), dto.getQuantidade(), dto.getMotivo(), dto.getObservacao(), dto.getDocumento());
		return ResponseEntity.status(HttpStatus.CREATED).body(MovimentacaoMapper.toResponseDto(movimentacao));
	}

	@GetMapping
	public ResponseEntity<List<MovimentacaoResponseDto>> listar(
			@RequestParam(required = false) String produtoId,
			@RequestParam(required = false) String usuarioId,
			@RequestParam(required = false) String tipo,
			@RequestParam(required = false) String de,
			@RequestParam(required = false) String ate,
			@RequestParam(required = false) String limite) {
		MovimentacaoFiltro filtro = MovimentacaoFiltro.validar(produtoId, usuarioId, tipo, de, ate, limite);
		List<MovimentacaoEstoque> movimentacoes = movimentacaoService.listar(filtro.getProdutoId(),
				filtro.getUsuarioId(), filtro.getTipo(), filtro.getDe(), filtro.getAte(), filtro.getLimite());
		return ResponseEntity.ok(MovimentacaoMapper.toResponseDto(movimentacoes));
	}

}
