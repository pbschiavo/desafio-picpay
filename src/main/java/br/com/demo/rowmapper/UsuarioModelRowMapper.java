package br.com.demo.rowmapper;

import br.com.demo.model.UsuarioModel;
import br.com.demo.util.TipoUsuarioEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioModelRowMapper implements RowMapper<UsuarioModel> {

    @Override
    public UsuarioModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome_completo"));
        usuario.setCpfCnpj(rs.getString("cpf_cnpj"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setSaldo(rs.getBigDecimal("saldo"));
        usuario.setTipo(TipoUsuarioEnum.valueOf(rs.getString("tipo")));
        return usuario;
    }
}
