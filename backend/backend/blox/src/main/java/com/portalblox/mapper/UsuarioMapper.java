package com.portalblox.mapper;

import com.portalblox.dto.UsuarioRequestDto;
import com.portalblox.dto.UsuarioResponseDto;
import com.portalblox.entity.Usuario;
import java.util.List;

public class UsuarioMapper {

	public static Usuario toEntity(UsuarioRequestDto dto) {
		if (dto == null) {
			return null;
		}
		Usuario usuario = new Usuario();
		usuario.setNome(dto.getNome());
		usuario.setEmail(dto.getEmail());
		usuario.setSenha(dto.getSenha());
		return usuario;                         // o perfil é associado pelo service
	}

	public static UsuarioResponseDto toResponseDto(Usuario entity) {
		if (entity == null) {
			return null;
		}
		UsuarioResponseDto dto = new UsuarioResponseDto();
		dto.setId(entity.getId());
		dto.setNome(entity.getNome());
		dto.setEmail(entity.getEmail());
		dto.setAtivo(entity.getAtivo());

		UsuarioResponseDto.PerfilAcessoResponseDto perfilDto =
				new UsuarioResponseDto.PerfilAcessoResponseDto();
		perfilDto.setId(entity.getPerfilAcesso().getId());
		perfilDto.setNome(entity.getPerfilAcesso().getNome());
		dto.setPerfilAcesso(perfilDto);
		return dto;                             // RN-07: a senha jamais é copiada
	}

	public static List<UsuarioResponseDto> toResponseDto(List<Usuario> entities) {
		return entities.stream()
				.map(UsuarioMapper::toResponseDto)
				.toList();
	}

}
