package br.com.demo.dao;

import br.com.demo.dto.RespostaDTO;
import br.com.demo.dto.UsuarioDTO;
import br.com.demo.exception.BadRequestException;
import br.com.demo.model.UsuarioModel;
import br.com.demo.rowmapper.UsuarioDTORowMapper;
import br.com.demo.rowmapper.UsuarioModelRowMapper;
import br.com.demo.util.UsuarioUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UsuarioDao {

    private final NamedParameterJdbcTemplate jdbc;

    public List<UsuarioDTO> listarUsuarios() {
        String sql = "SELECT * FROM usuarios";
        try {
            return jdbc.query(sql, new UsuarioDTORowMapper());
        } catch (DataAccessException e) {
            log.error("Erro ao buscar usuários", e);
            throw new BadRequestException("Erro ao buscar usuários");
        }
    }

    public UsuarioModel buscarUsuarioPorId(Integer id) {
        try {
            String sql = "SELECT * FROM usuarios WHERE id = :id";
            MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
            return jdbc.queryForObject(sql, params, new UsuarioModelRowMapper());
        } catch (Exception e) {
            log.error("Erro ao buscar usuário: {}", e.getMessage());
            throw new BadRequestException("Erro ao buscar usuário.");
        }
    }

    @Transactional
    public ResponseEntity<RespostaDTO> cadastrarUsuario(UsuarioModel usuarioModel) {
        String sql = "INSERT INTO usuarios (nome_completo, cpf_cnpj, email, senha, saldo, tipo) " +
                "VALUES (:nome, :cpfCnpj, :email, :senha, :saldo, :tipo)";
        String senhaHash = UsuarioUtil.hashSenha(usuarioModel.getSenha());

        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("nome", usuarioModel.getNome())
                    .addValue("cpfCnpj", usuarioModel.getCpfCnpj())
                    .addValue("email", usuarioModel.getEmail())
                    .addValue("senha", senhaHash)
                    .addValue("saldo", usuarioModel.getSaldo())
                    .addValue("tipo", usuarioModel.getTipo().name());

            jdbc.update(sql, params);

            return ResponseEntity.status(HttpStatus.CREATED).body(new RespostaDTO("Usuário inserido com sucesso!"));
        } catch (Exception e) {
            log.error("Erro ao cadastrar usuário", e);
            throw new BadRequestException("Erro ao cadastrar usuário ");
        }
    }
}
