package br.com.demo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.demo.dto.RespostaDTO;
import br.com.demo.dto.UsuarioDTO;
import br.com.demo.exception.BadRequestException;
import br.com.demo.model.UsuarioModel;
import br.com.demo.util.TipoUsuarioEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UsuarioDao.class})
@ExtendWith(SpringExtension.class)
class UsuarioDaoTest {
    @MockBean
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private UsuarioDao usuarioDao;

    @Test
    void testListarUsuariosSucesso() throws DataAccessException {
        ArrayList<UsuarioDTO> usuarioDTOList = new ArrayList<>();
        when(namedParameterJdbcTemplate.query(Mockito.any(), Mockito.<RowMapper<UsuarioDTO>>any()))
                .thenReturn(usuarioDTOList);
        List<UsuarioDTO> actualListarUsuariosResult = usuarioDao.listarUsuarios();
        assertSame(usuarioDTOList, actualListarUsuariosResult);
        assertTrue(actualListarUsuariosResult.isEmpty());
        verify(namedParameterJdbcTemplate).query(Mockito.any(), Mockito.<RowMapper<UsuarioDTO>>any());
    }

    @Test
    void testListarUsuariosErro() throws DataAccessException {
        when(namedParameterJdbcTemplate.query(Mockito.any(), Mockito.<RowMapper<UsuarioDTO>>any()))
                .thenThrow(new EmptyResultDataAccessException(3));
        assertThrows(BadRequestException.class, () -> usuarioDao.listarUsuarios());
        verify(namedParameterJdbcTemplate).query(Mockito.any(), Mockito.<RowMapper<UsuarioDTO>>any());
    }

    @Test
    void testBuscarUsuarioPorIdSucesso() throws DataAccessException {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setCpfCnpj("Cpf Cnpj");
        usuarioModel.setEmail("pablo@gmail.com");
        usuarioModel.setId(1);
        usuarioModel.setNome("Nome");
        usuarioModel.setSaldo(BigDecimal.valueOf(1L));
        usuarioModel.setSenha("Senha");
        usuarioModel.setTipo(TipoUsuarioEnum.NORMAL);
        when(namedParameterJdbcTemplate.queryForObject(Mockito.<String>any(), Mockito.<SqlParameterSource>any(),
                Mockito.<RowMapper<UsuarioModel>>any())).thenReturn(usuarioModel);
        UsuarioModel actualBuscarUsuarioPorIdResult = usuarioDao.buscarUsuarioPorId(1);
        assertSame(usuarioModel, actualBuscarUsuarioPorIdResult);
        assertEquals("1", actualBuscarUsuarioPorIdResult.getSaldo().toString());
        verify(namedParameterJdbcTemplate).queryForObject(Mockito.<String>any(), Mockito.<SqlParameterSource>any(),
                Mockito.<RowMapper<UsuarioModel>>any());
    }

    @Test
    void testBuscarUsuarioPorIdErro() throws DataAccessException {
        when(namedParameterJdbcTemplate.queryForObject(Mockito.any(), Mockito.<SqlParameterSource>any(),
                Mockito.<RowMapper<UsuarioModel>>any())).thenThrow(new EmptyResultDataAccessException(3));
        assertThrows(BadRequestException.class, () -> usuarioDao.buscarUsuarioPorId(1));
        verify(namedParameterJdbcTemplate).queryForObject(Mockito.any(), Mockito.<SqlParameterSource>any(),
                Mockito.<RowMapper<UsuarioModel>>any());
    }

    @Test
    void testCadastrarUsuario() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.<String>any(), Mockito.<SqlParameterSource>any())).thenReturn(1);

        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setCpfCnpj("Cpf Cnpj");
        usuarioModel.setEmail("pablo@gmail.com");
        usuarioModel.setId(1);
        usuarioModel.setNome("Nome");
        usuarioModel.setSaldo(BigDecimal.valueOf(1L));
        usuarioModel.setSenha("Senha");
        usuarioModel.setTipo(TipoUsuarioEnum.NORMAL);
        ResponseEntity<RespostaDTO> actualCadastrarUsuarioResult = usuarioDao.cadastrarUsuario(usuarioModel);
        assertTrue(actualCadastrarUsuarioResult.hasBody());
        assertTrue(actualCadastrarUsuarioResult.getHeaders().isEmpty());
        assertEquals(201, actualCadastrarUsuarioResult.getStatusCodeValue());
        assertEquals("Usuário inserido com sucesso!", actualCadastrarUsuarioResult.getBody().getMessage());
        verify(namedParameterJdbcTemplate).update(Mockito.<String>any(), Mockito.<SqlParameterSource>any());
    }

    @Test
    void testCadastrarUsuario2() {
        UsuarioModel usuarioModel = mock(UsuarioModel.class);
        when(usuarioModel.getNome()).thenThrow(new BadRequestException("Error"));
        when(usuarioModel.getSenha()).thenReturn("Senha");
        doNothing().when(usuarioModel).setCpfCnpj(Mockito.any());
        doNothing().when(usuarioModel).setEmail(Mockito.any());
        doNothing().when(usuarioModel).setId(Mockito.<Integer>any());
        doNothing().when(usuarioModel).setNome(Mockito.any());
        doNothing().when(usuarioModel).setSaldo(Mockito.any());
        doNothing().when(usuarioModel).setSenha(Mockito.any());
        doNothing().when(usuarioModel).setTipo(Mockito.any());
        usuarioModel.setCpfCnpj("Cpf Cnpj");
        usuarioModel.setEmail("jane.doe@example.org");
        usuarioModel.setId(1);
        usuarioModel.setNome("Nome");
        usuarioModel.setSaldo(BigDecimal.valueOf(1L));
        usuarioModel.setSenha("Senha");
        usuarioModel.setTipo(TipoUsuarioEnum.NORMAL);
        assertThrows(BadRequestException.class, () -> usuarioDao.cadastrarUsuario(usuarioModel));
        verify(usuarioModel).getNome();
        verify(usuarioModel).getSenha();
        verify(usuarioModel).setCpfCnpj(Mockito.any());
        verify(usuarioModel).setEmail(Mockito.any());
        verify(usuarioModel).setId(Mockito.<Integer>any());
        verify(usuarioModel).setNome(Mockito.any());
        verify(usuarioModel).setSaldo(Mockito.any());
        verify(usuarioModel).setSenha(Mockito.any());
        verify(usuarioModel).setTipo(Mockito.any());
    }
}

