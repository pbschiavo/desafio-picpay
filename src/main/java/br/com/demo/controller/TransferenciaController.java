package br.com.demo.controller;

import br.com.demo.dto.RespostaDTO;
import br.com.demo.model.TransferenciaModel;
import br.com.demo.service.TransferenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transferencia")
@RequiredArgsConstructor
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransferenciaModel transferenciaModel) {
        transferenciaService.transferir(transferenciaModel);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
