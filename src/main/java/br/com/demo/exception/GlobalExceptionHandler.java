package br.com.demo.exception;

import br.com.demo.dto.RespostaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<RespostaDTO> handleSaldoInsuficienteException(SaldoInsuficienteException ex) {
        RespostaDTO respostaDTO = new RespostaDTO(ex.getMessage());
        return ResponseEntity.badRequest().body(respostaDTO);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<RespostaDTO> handleBadRequestException(BadRequestException ex) {
        RespostaDTO respostaDTO = new RespostaDTO(ex.getMessage());
        return ResponseEntity.badRequest().body(respostaDTO);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<List<String>> handleValidationException(BindException ex) {
        List<String> errors = ex.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }
}

