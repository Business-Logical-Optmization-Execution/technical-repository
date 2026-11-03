package com.portalblox.controller;

import com.portalblox.entity.PerfilAcesso;
import com.portalblox.service.PerfilAcessoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/perfis-acesso")
public class PerfilAcessoController {

    private final PerfilAcessoService perfilAcessoService;

    public PerfilAcessoController(PerfilAcessoService perfilAcessoService) {
        this.perfilAcessoService = perfilAcessoService;
    }

    @GetMapping
    public ResponseEntity<List<PerfilAcesso>> listar(){

        List<PerfilAcesso> perfilAcessos = perfilAcessoService.listar();

        return ResponseEntity.status(200).body(perfilAcessos);
    }

}
