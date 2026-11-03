package com.portalblox.service;

import com.portalblox.entity.PerfilAcesso;
import com.portalblox.repository.PerfilAcessoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilAcessoService {

    private final PerfilAcessoRepository perfilAcessoRepository;


    public PerfilAcessoService(PerfilAcessoRepository perfilAcessoRepository) {
        this.perfilAcessoRepository = perfilAcessoRepository;
    }

    public List<PerfilAcesso> listar(){

       List<PerfilAcesso> perfilAcessos = perfilAcessoRepository.findAll();

        return perfilAcessos;
    }
}
