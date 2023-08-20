package br.com.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.demo.dao.NotificacaoDao;
import br.com.demo.dao.TransferenciaDao;
import br.com.demo.dao.UsuarioDao;
import br.com.demo.exception.BadRequestException;
import br.com.demo.model.TransferenciaModel;
import br.com.demo.model.UsuarioModel;
import br.com.demo.util.TipoUsuarioEnum;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TransferenciaService.class})
@ExtendWith(SpringExtension.class)
class TransferenciaServiceTest {
    @MockBean
    private NotificacaoDao notificacaoDao;

    @MockBean
    private TransferenciaDao transferenciaDao;

    @Autowired
    private TransferenciaService transferenciaService;

    @MockBean
    private UsuarioDao usuarioDao;

    @Test
    void testTransferirSucesso() {
        doNothing().when(transferenciaDao).atualizaSaldoPagador(Mockito.<TransferenciaModel>any());
        doNothing().when(transferenciaDao).atualizaSaldoRecebedor(Mockito.<TransferenciaModel>any());
        doNothing().when(transferenciaDao).registrarTransacao(Mockito.<TransferenciaModel>any());
        doNothing().when(transferenciaDao).validarSaldo(Mockito.<TransferenciaModel>any(), Mockito.<UsuarioModel>any());
        doNothing().when(transferenciaDao).validarTipoUsuario(Mockito.<UsuarioModel>any());
        doNothing().when(transferenciaDao).validarTransferencia();
        doNothing().when(notificacaoDao)
                .registrarNotificacao(Mockito.<TransferenciaModel>any(), Mockito.<UsuarioModel>any());

        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setCpfCnpj("Cpf Cnpj");
        usuarioModel.setEmail("pablo@gmail.com");
        usuarioModel.setId(1);
        usuarioModel.setNome("Nome");
        usuarioModel.setSaldo(BigDecimal.valueOf(1L));
        usuarioModel.setSenha("Senha");
        usuarioModel.setTipo(TipoUsuarioEnum.NORMAL);
        when(usuarioDao.buscarUsuarioPorId(Mockito.<Integer>any())).thenReturn(usuarioModel);

        TransferenciaModel transferencia = new TransferenciaModel();
        transferencia.setPagador(1);
        transferencia.setRecebedor(1);
        BigDecimal valor = BigDecimal.valueOf(1L);
        transferencia.setValor(valor);
        transferenciaService.transferir(transferencia);
        verify(transferenciaDao).atualizaSaldoPagador(Mockito.<TransferenciaModel>any());
        verify(transferenciaDao).atualizaSaldoRecebedor(Mockito.<TransferenciaModel>any());
        verify(transferenciaDao).registrarTransacao(Mockito.<TransferenciaModel>any());
        verify(transferenciaDao).validarSaldo(Mockito.<TransferenciaModel>any(), Mockito.<UsuarioModel>any());
        verify(transferenciaDao).validarTipoUsuario(Mockito.<UsuarioModel>any());
        verify(transferenciaDao).validarTransferencia();
        verify(notificacaoDao).registrarNotificacao(Mockito.<TransferenciaModel>any(), Mockito.<UsuarioModel>any());
        verify(usuarioDao).buscarUsuarioPorId(Mockito.<Integer>any());
        assertEquals(1, transferencia.getPagador().intValue());
        BigDecimal expectedValor = valor.ONE;
        assertSame(expectedValor, transferencia.getValor());
    }

    @Test
    void testTransferirErro() {
        when(usuarioDao.buscarUsuarioPorId(Mockito.<Integer>any()))
                .thenThrow(new BadRequestException("Error"));

        TransferenciaModel transferencia = new TransferenciaModel();
        transferencia.setPagador(1);
        transferencia.setRecebedor(1);
        transferencia.setValor(BigDecimal.valueOf(1L));
        assertThrows(BadRequestException.class, () -> transferenciaService.transferir(transferencia));
        verify(usuarioDao).buscarUsuarioPorId(Mockito.<Integer>any());
    }
}

