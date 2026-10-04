package com.portalblox.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portalblox.dto.UsuarioRequestDto;
import com.portalblox.dto.UsuarioResponseDto;
import com.portalblox.entity.PerfilAcesso;
import com.portalblox.entity.Usuario;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioMapperTest {

	private Usuario usuarioComSenha() {
		PerfilAcesso perfil = new PerfilAcesso();
		perfil.setId(1);
		perfil.setNome("Administrador");

		Usuario usuario = new Usuario();
		usuario.setId(10);
		usuario.setNome("Maria");
		usuario.setEmail("maria@portalblox.com");
		usuario.setSenha("super-secreta-123");
		usuario.setPerfilAcesso(perfil);
		usuario.setAtivo(true);
		return usuario;
	}

	@Test
	void toEntityConverteDtoParaEntidade() {
		UsuarioRequestDto dto = new UsuarioRequestDto();
		dto.setNome("Maria");
		dto.setEmail("maria@portalblox.com");
		dto.setSenha("super-secreta-123");
		dto.setPerfilAcessoId(1);

		Usuario usuario = UsuarioMapper.toEntity(dto);

		assertEquals("Maria", usuario.getNome());
		assertEquals("maria@portalblox.com", usuario.getEmail());
		assertEquals("super-secreta-123", usuario.getSenha());
		assertNull(usuario.getPerfilAcesso(), "o perfil é associado pelo service");
	}

	@Test
	void toEntityComDtoNuloRetornaNulo() {
		assertNull(UsuarioMapper.toEntity((UsuarioRequestDto) null));
	}

	@Test
	void toResponseDtoConverteEntidadeComPerfil() {
		UsuarioResponseDto dto = UsuarioMapper.toResponseDto(usuarioComSenha());

		assertEquals(10, dto.getId());
		assertEquals("Maria", dto.getNome());
		assertEquals("maria@portalblox.com", dto.getEmail());
		assertEquals(true, dto.getAtivo());
		assertEquals(1, dto.getPerfilAcesso().getId());
		assertEquals("Administrador", dto.getPerfilAcesso().getNome());
	}

	@Test
	void toResponseDtoComEntidadeNulaRetornaNula() {
		assertNull(UsuarioMapper.toResponseDto((Usuario) null));
	}

	@Test
	void toResponseDtoDeListaConverteTodosOsElementos() {
		List<UsuarioResponseDto> dtos =
				UsuarioMapper.toResponseDto(List.of(usuarioComSenha(), usuarioComSenha()));

		assertEquals(2, dtos.size());
		assertEquals("Maria", dtos.get(0).getNome());
	}

	@Test
	void respostaNuncaContemSenha() throws Exception {
		UsuarioResponseDto dto = UsuarioMapper.toResponseDto(usuarioComSenha());

		// 1) o DTO não possui campo chamado senha (reflection)
		boolean temCampoSenha = Arrays.stream(UsuarioResponseDto.class.getDeclaredFields())
				.anyMatch(campo -> campo.getName().equalsIgnoreCase("senha"));
		assertFalse(temCampoSenha, "RN-07: UsuarioResponseDto não pode ter campo senha");

		// 2) a serialização JSON não contém nem a chave nem o valor da senha
		String json = new ObjectMapper().writeValueAsString(dto);
		assertFalse(json.contains("senha"), "RN-07: JSON não pode conter a chave senha");
		assertFalse(json.contains("super-secreta-123"), "RN-07: JSON não pode conter o valor da senha");
		assertTrue(json.contains("maria@portalblox.com"), "os demais dados seguem na resposta");
	}

}
