package br.com.demo.dao;

import br.com.demo.exception.BadRequestException;
import br.com.demo.exception.SaldoInsuficienteException;
import br.com.demo.model.TransferenciaModel;
import br.com.demo.model.UsuarioModel;
import br.com.demo.util.TipoUsuarioEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TransferenciaDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RestTemplate restTemplate;

    public void validarTransferencia() {
        try {
            String apiUrl = "https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6";

            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            if (response != null && response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                if (responseBody != null && responseBody.contains("Autorizado")) {
                    return; // Transferência autorizada
                }
            }

            throw new BadRequestException("Autorização não concedida para a transferência.");
        } catch (Exception e) {
            log.error("Erro ao validar transferência", e);
            throw new BadRequestException("Erro ao validar transferência.");
        }
    }

    public void validarTipoUsuario(UsuarioModel usuario) {
        if (usuario.getTipo().equals(TipoUsuarioEnum.LOJISTA)) {
            throw new BadRequestException("Usuários lojistas não podem realizar transferência.");
        }
    }

    public void validarSaldo(TransferenciaModel request, UsuarioModel usuario) {
        BigDecimal saldo = usuario.getSaldo().subtract(request.getValor());
        if (saldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar a transferência.");
        }
    }

    @Transactional
    public void registrarTransacao(TransferenciaModel request) {
        try {
            String sqlTransacao = "INSERT INTO transacoes (valor, recebedor, pagador, data) VALUES (:valor, :recebedor, :pagador, :data)";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("valor", request.getValor())
                    .addValue("recebedor", request.getRecebedor())
                    .addValue("pagador", request.getPagador())
                    .addValue("data", new Date());

            namedParameterJdbcTemplate.update(sqlTransacao, params);
        } catch (Exception e) {
            log.error("Erro ao realizar transferência: {}", e.getMessage());
            throw new BadRequestException("Erro ao realizar transferência.");
        }
    }

    public void atualizaSaldoPagador(TransferenciaModel request) {
        try {
            atualizaSaldo("UPDATE usuarios SET saldo = saldo - :valor WHERE id = :id", request.getPagador(), request.getValor());
        } catch (Exception e) {
            log.error("Erro ao atualizar saldo do pagador: {}", e.getMessage());
            throw new BadRequestException("Erro ao atualizar saldo do pagador.");
        }
    }

    public void atualizaSaldoRecebedor(TransferenciaModel request) {
        try {
            atualizaSaldo("UPDATE usuarios SET saldo = saldo + :valor WHERE id = :id", request.getRecebedor(), request.getValor());
        } catch (Exception e) {
            log.error("Erro ao atualizar saldo do recebedor: {}", e.getMessage());
            throw new BadRequestException("Erro ao atualizar saldo do recebedor.");
        }
    }

    public void atualizaSaldo(String sql, Integer id, BigDecimal valor) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("id", id)
                    .addValue("valor", valor);

            namedParameterJdbcTemplate.update(sql, params);
        } catch (Exception e) {
            log.error("Erro ao atualizar saldo: {}", e.getMessage());
            throw new BadRequestException("Erro ao atualizar saldo.");
        }
    }

}
