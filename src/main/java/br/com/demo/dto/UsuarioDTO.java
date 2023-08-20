package br.com.demo.dto;

import br.com.demo.util.TipoUsuarioEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UsuarioDTO {

    private String nome;
    private String cpfCnpj;
    private String email;
    private BigDecimal saldo;
    private TipoUsuarioEnum tipo;
}