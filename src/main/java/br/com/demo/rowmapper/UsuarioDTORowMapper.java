package br.com.demo.rowmapper;

import br.com.demo.dto.UsuarioDTO;
import br.com.demo.util.TipoUsuarioEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDTORowMapper implements RowMapper<UsuarioDTO> {

    @Override
    public UsuarioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNome(rs.getString("nome_completo"));
        usuario.setCpfCnpj(rs.getString("cpf_cnpj"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSaldo(rs.getBigDecimal("saldo"));
        usuario.setTipo(TipoUsuarioEnum.valueOf(rs.getString("tipo")));
        return usuario;
    }
}

