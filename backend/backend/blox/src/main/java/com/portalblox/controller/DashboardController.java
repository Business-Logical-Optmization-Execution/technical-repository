package com.portalblox.controller;

import com.portalblox.controller.docs.DashboardApi;
import com.portalblox.dto.DashboardResumoResponseDto;
import com.portalblox.mapper.DashboardMapper;
import com.portalblox.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController implements DashboardApi {

	private final DashboardService dashboardService;

	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("/resumo")
	public ResponseEntity<DashboardResumoResponseDto> obterResumo() {
		return ResponseEntity.ok(DashboardMapper.toResponseDto(dashboardService.obterResumo()));
	}

}
