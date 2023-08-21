package br.com.demo.service;

import br.com.demo.dao.UsuarioDao;
import br.com.demo.dto.RespostaDTO;
import br.com.demo.dto.UsuarioDTO;
import br.com.demo.model.UsuarioModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioDao usuarioDao;

    public List<UsuarioDTO> getUsuarios() {
        return usuarioDao.listarUsuarios();
    }

    public ResponseEntity<RespostaDTO> cadastrarUsuario(UsuarioModel usuarioModel) {
        boolean usuarioCadastrado = usuarioDao.verificarSeUsuarioExiste(usuarioModel);
        if (usuarioCadastrado) {
            return ResponseEntity.badRequest().body(new RespostaDTO("Usuário já cadastrado."));
        }
        return usuarioDao.cadastrarUsuario(usuarioModel);
    }
}
