package br.com.demo.dao;

import br.com.demo.exception.BadRequestException;
import br.com.demo.model.NotificacaoModel;
import br.com.demo.model.TransferenciaModel;
import br.com.demo.model.UsuarioModel;
import br.com.demo.rowmapper.NotificacaoRowMapper;
import br.com.demo.util.MensagemEnum;
import br.com.demo.util.NotificacaoEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NotificacaoDao {

    private final NamedParameterJdbcTemplate jdbc;

    @Transactional
    public void registrarNotificacao(TransferenciaModel transferencia, UsuarioModel usuario) {
        try {
            String sqlTransacao = "INSERT INTO notificacoes (recebedor, valor, email, mensagem, status, data_notificacao) VALUES (:recebedor, :valor, :email, :mensagem, :status, :data_notificacao)";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("recebedor", transferencia.getRecebedor());
            paramMap.put("valor", transferencia.getValor());
            paramMap.put("email", usuario.getEmail());
            paramMap.put("mensagem", MensagemEnum.TRANSFERENCIA_SUCESSO.getMensagem() + transferencia.getValor().toString());
            paramMap.put("status", NotificacaoEnum.PENDENTE.name());
            paramMap.put("data_notificacao", new Date());

            jdbc.update(sqlTransacao, paramMap);
        } catch (Exception e) {
            log.error("Erro ao enviar notificação: {}", e);
            throw new BadRequestException("Erro ao enviar notificação.");
        }
    }

    @Transactional
    public void atualizaStatusNotificacao(Integer recebedor) {
        try {
            String sql = "UPDATE notificacoes SET status = :status WHERE recebedor = :recebedor";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("status", NotificacaoEnum.ENVIADO.name());
            paramMap.put("recebedor", recebedor);

            jdbc.update(sql, paramMap);
        } catch (Exception e) {
            log.error("Erro ao atualizar status da notificação: {}", e);
            throw new BadRequestException("Erro ao atualizar status da notificação.");
        }
    }

    public List<NotificacaoModel> listarNotificacoesPendentes() {
        try {
            String sql = "SELECT * FROM notificacoes WHERE status = :status";
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("status", NotificacaoEnum.PENDENTE.name());

            return jdbc.query(sql, paramMap, new NotificacaoRowMapper());
        } catch (DataAccessException e) {
            log.error("Erro ao buscar notificações pendentes", e);
            throw new BadRequestException("Erro ao buscar notificações pendentes");
        }
    }
}
