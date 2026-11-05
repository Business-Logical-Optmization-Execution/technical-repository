package com.portalblox.controller;

import com.portalblox.controller.docs.ConfiguracaoApi;
import com.portalblox.dto.ConfiguracaoRequestDto;
import com.portalblox.dto.ConfiguracaoResponseDto;
import com.portalblox.mapper.ConfiguracaoMapper;
import com.portalblox.service.ConfiguracaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuracoes")
public class ConfiguracaoController implements ConfiguracaoApi {

	private final ConfiguracaoService configuracaoService;

	public ConfiguracaoController(ConfiguracaoService configuracaoService) {
		this.configuracaoService = configuracaoService;
	}

	@GetMapping
	public ResponseEntity<ConfiguracaoResponseDto> obter() {
		return ResponseEntity.ok(ConfiguracaoMapper.toResponseDto(
				configuracaoService.obterConfiguracao(),
				configuracaoService.contarProdutosComMinimoProprio()));
	}

	@PutMapping
	public ResponseEntity<ConfiguracaoResponseDto> atualizar(@Valid @RequestBody ConfiguracaoRequestDto dto) {
		return ResponseEntity.ok(ConfiguracaoMapper.toResponseDto(
				configuracaoService.atualizar(dto.getEstoqueMinimoGlobal(), dto.getExibirFotos()),
				configuracaoService.contarProdutosComMinimoProprio()));
	}

}
