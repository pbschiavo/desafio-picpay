package br.com.demo.controller;

import br.com.demo.dto.RespostaDTO;
import br.com.demo.dto.UsuarioDTO;
import br.com.demo.model.UsuarioModel;
import br.com.demo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Listar usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listou os usuários com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)) }),
    })
    @GetMapping("/listarUsuarios")
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioService.getUsuarios();
    }

    @Operation(summary = "Cadastrar um novo usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos",
                    content = @Content) })
    @PostMapping("/cadastrarUsuario")
    public ResponseEntity<RespostaDTO> cadastrarUsuario(@RequestBody @Valid UsuarioModel usuarioModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrarUsuario(usuarioModel).getBody());
    }
}
