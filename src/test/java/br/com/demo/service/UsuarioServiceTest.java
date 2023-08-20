package br.com.demo.service;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.demo.dao.UsuarioDao;
import br.com.demo.dto.UsuarioDTO;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UsuarioService.class})
@ExtendWith(SpringExtension.class)
class UsuarioServiceTest {
    @MockBean
    private UsuarioDao usuarioDao;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void testGetUsuarios() {
        ArrayList<UsuarioDTO> usuarioDTOList = new ArrayList<>();
        when(usuarioDao.listarUsuarios()).thenReturn(usuarioDTOList);
        List<UsuarioDTO> actualUsuarios = usuarioService.getUsuarios();
        assertSame(usuarioDTOList, actualUsuarios);
        assertTrue(actualUsuarios.isEmpty());
        verify(usuarioDao).listarUsuarios();
    }

    @Test
    void testCadastrarUsuario() {
        when(usuarioDao.cadastrarUsuario(Mockito.<UsuarioModel>any())).thenReturn(null);

        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setCpfCnpj("Cpf Cnpj");
        usuarioModel.setEmail("pablo@gmail.com");
        usuarioModel.setId(1);
        usuarioModel.setNome("Nome");
        usuarioModel.setSaldo(BigDecimal.valueOf(1L));
        usuarioModel.setSenha("Senha");
        usuarioModel.setTipo(TipoUsuarioEnum.NORMAL);
        assertNull(usuarioService.cadastrarUsuario(usuarioModel));
        verify(usuarioDao).cadastrarUsuario(Mockito.<UsuarioModel>any());
    }
}

