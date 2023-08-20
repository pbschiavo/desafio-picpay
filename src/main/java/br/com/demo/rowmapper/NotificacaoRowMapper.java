package br.com.demo.rowmapper;

import br.com.demo.model.NotificacaoModel;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificacaoRowMapper implements RowMapper<NotificacaoModel> {

    @Override
    public NotificacaoModel mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        NotificacaoModel notificacao = new NotificacaoModel();
        notificacao.setId(resultSet.getInt("id"));
        notificacao.setRecebedor(resultSet.getInt("recebedor"));
        notificacao.setValor(resultSet.getBigDecimal("valor"));
        notificacao.setEmail(resultSet.getString("email"));
        notificacao.setMensagem(resultSet.getString("mensagem"));
        notificacao.setStatus(resultSet.getString("status"));
        notificacao.setDataEnvio(resultSet.getTimestamp("data_notificacao"));
        return notificacao;
    }
}
